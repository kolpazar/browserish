package org.eyyam.browserish.browser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eyyam.browserish.ModuleManager;
import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.module.Module;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebResourceResponse;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserAOSP extends Browser {

	private static final String PREFIX = "browserish://";
	private static final String FIELD_STATE_KEY = "browserish-state";
	private static final String FIELD_URL_KEY = "browserish-url";
	private static final String FIELD_TAB_INITED_KEY = "browserish-tab-inited";
	private static final String FIELD_INTF_KEY = "browserish-interface";
	
	private String uuid;
	
	private Class<?> webViewControllerClass;
	private Class<?> tabClass;
	private Class<?> webViewClass;
	private Class<?> homeProviderClass;
	
	public BrowserAOSP(ModuleManager moduleManager) {
		super(moduleManager);
		uuid = UUID.randomUUID().toString();
	}
	
	@Override
	public void initialize(LoadPackageParam loadParam) {
		webViewControllerClass = XposedHelpers.findClass("com.android.browser.WebViewController", loadParam.classLoader);
		tabClass = XposedHelpers.findClass("com.android.browser.Tab", loadParam.classLoader);
		webViewClass = XposedHelpers.findClass("android.webkit.WebView", loadParam.classLoader);
		homeProviderClass = XposedHelpers.findClass("com.android.browser.homepages.HomeProvider", loadParam.classLoader);
		
		XposedBridge.hookMethod(
				XposedHelpers.findMethodBestMatch(homeProviderClass, "shouldInterceptRequest", Context.class, String.class), 
				new FileLoaderMethod());
		
		XposedBridge.hookMethod(
				XposedHelpers.findConstructorBestMatch(webViewClass, Context.class, AttributeSet.class, Integer.class, Map.class, Boolean.class), 
				new WebViewConstructor());

		XposedBridge.hookMethod(
				XposedHelpers.findConstructorBestMatch(tabClass, webViewControllerClass, webViewClass, Bundle.class), 
				new TabConstructor());

	}
	
	private String getWebViewUrl(Object webView) {
		String url = (String) XposedHelpers.getAdditionalInstanceField(webView, FIELD_URL_KEY);
		if (url == null) {
			url = "";
		}
		return url;
	}
	
	private void setWebViewUrl(Object webView, String url) {
		XposedHelpers.setAdditionalInstanceField(webView, FIELD_URL_KEY, url);
	}
	
	private int getWebViewState(Object webView) {
		Integer state = (Integer) XposedHelpers.getAdditionalInstanceField(webView, FIELD_STATE_KEY);
		if (state != null) {
			return state.intValue();
		} else {
			XposedHelpers.setAdditionalInstanceField(webView, FIELD_STATE_KEY, Integer.valueOf(0));
			return 0;
		}
	}
	
	private void setWebViewState(Object webView, int state) {
		if (webView != null) {
			int oldState = getWebViewState(webView);
			XposedBridge.log("Browserish webview state: " + state);
			if ((oldState == 2) && (state == 1)) {
				state = 2;
			}
			XposedHelpers.setAdditionalInstanceField(webView, FIELD_STATE_KEY, Integer.valueOf(state));
			if ((oldState == 0) && (state == 1)) {
				documentStart(webView);
			} else if ((oldState == 1) && (state == 2)) {
				documentLoaded(webView);
			} else if ((oldState == 0) && (state == 2)) {
				documentStart(webView);
				documentLoaded(webView);
			}
		} else {
			XposedBridge.log("Browserish null webview state");
		}
	}
	
	public void executeJS(final Object webView, final String js) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
		    @Override
		    public void run() {
				try {
					Method webViewLoadUrlMethod = XposedHelpers.findMethodExact(webViewClass, "loadUrl", String.class);
					webViewLoadUrlMethod.invoke(webView, js);
				} catch (Exception e) {
					XposedBridge.log("browserish: JS execution error.");
				}
			}
		});
	}
	
	public void injectStyle(Object webView, String filename) {
		String js = "javascript: function browserish_inject() { var elem = document.createElement('link'); elem.rel = 'stylesheet'; elem.href = 'browserish://" + uuid + filename + "'; document.documentElement.appendChild(elem); }; browserish_inject(); ";
		executeJS(webView, js);
	}
	
	public void documentStart(Object webView) {
		XposedBridge.log("Browserish DOC START");
		List<PageAction> actions = moduleManager.documentStart(getWebViewUrl(webView));
		XposedBridge.log("Browserish applying " + actions.size() + " actions.");
		for (PageAction action: actions) {
			XposedBridge.log("Browserish action " + action.getType() + " file " + action.getFilename());
			switch(action.getType()) {
			case STYLE:
				injectStyle(webView, action.getFilename());
				break;
			case SCRIPT:
				
			}
		}
		//String js = "javascript: function browserish_inject() { var elem = document.createElement('link'); elem.rel = 'stylesheet'; elem.href = 'browserish://" + uuid + "/style/red.css'; document.documentElement.appendChild(elem); }; browserish_inject(); ";
		//executeJS(webView, js);
	}
	
	public void documentLoaded(Object webView) {
		XposedBridge.log("Browserish DOC LOADED");
	}
	
	class BrowserishInterface {

		private Object webView = null;
		
		public BrowserishInterface(Object webView) {
			this.webView = webView;
		}
		
		public void setWebView(Object webView) {
			this.webView = webView;
		}
		
		public void setState(String state) {
			XposedBridge.log("Browserish tab state external: " + state);
			setWebViewState(webView, Integer.valueOf(state));
		}
		
	}
	
	class FileLoaderMethod extends XC_MethodHook {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			String url = (String) param.args[1];
			if (url.startsWith(PREFIX)) {
				XposedBridge.log("Browserish request 1: " + url);
				String[] urlParts = url.substring(13).split("/");
				if ((urlParts.length != 3) || (!urlParts[0].equals(uuid))) {
					return;
				}
				Module module = moduleManager.getModule(urlParts[1]);
				if (module != null) {
					InputStream stream = module.getFile(urlParts[2]);
					if (stream != null) {
						param.setResult(new WebResourceResponse(module.getMimeType(urlParts[2]), 
								module.getEncoding(urlParts[2]), stream));
						XposedBridge.log("Browserish request 2: " + urlParts[2]);
					}
				}
			}
		}
	}
	
	class WebViewConstructor extends XC_MethodHook {
		
		@Override
		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
			XposedBridge.log("Browserish before create webview");
			BrowserishInterface intf = new BrowserishInterface(null);
			Map<String, Object> interfaces = ((Map<String,Object>) (param.args[3]));
			if (interfaces == null) {
				interfaces = new HashMap<String, Object>();
				param.args[3] = interfaces;
			}
			interfaces.put("browserish", intf);
		}
		
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			XposedBridge.log("Browserish after create webview " + param.thisObject);
			BrowserishInterface intf = (BrowserishInterface) ((Map<String,Object>) (param.args[3])).get("browserish");
			XposedHelpers.setAdditionalInstanceField(param.thisObject, FIELD_INTF_KEY, intf);
			intf.setWebView(param.thisObject);
			setWebViewState(param.thisObject, 0);
		}
	}
	
	class TabConstructor extends XC_MethodHook {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			if (XposedHelpers.getAdditionalStaticField(param.thisObject, FIELD_TAB_INITED_KEY) == null) {
				Field webViewClientField = XposedHelpers.findField(tabClass, "mWebViewClient");
				Object webViewClient = webViewClientField.get(param.thisObject);
				
				Field webChromeClientField = XposedHelpers.findField(tabClass, "mWebChromeClient");
				Object webChromeClient = webChromeClientField.get(param.thisObject);
	
				XposedBridge.log("Browserish tab create");
				
				XposedBridge.hookMethod(
						XposedHelpers.findMethodBestMatch(webViewClient.getClass(), "onPageStarted", webViewClass, String.class, Bitmap.class), 
						new PageStartedMethod());
				XposedBridge.hookMethod(
						XposedHelpers.findMethodBestMatch(webViewClient.getClass(), "onPageFinished", webViewClass, String.class), 
						new PageFinishedMethod());
				XposedBridge.hookMethod(
						XposedHelpers.findMethodBestMatch(webChromeClient.getClass(), "onProgressChanged", webViewClass, Integer.class), 
						new ProgressMethod());
				
				XposedHelpers.setAdditionalStaticField(param.thisObject, FIELD_TAB_INITED_KEY, Boolean.valueOf(true)); 
			}
		}
	}
	
	class PageStartedMethod extends XC_MethodHook {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			Object webView = param.args[0];
			String url = (String) param.args[1];
			if (!url.startsWith("javascript:")) {
				XposedBridge.log("Browserish start page: " + url);
				setWebViewUrl(webView, url);
				setWebViewState(param.args[0], 0);
			}
		}
	}
	
	class ProgressMethod extends XC_MethodHook {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			Object webView = param.args[0];
			int progress = (Integer) (param.args[1]);
			XposedBridge.log("Browserish progress: " + progress);
			if (getWebViewState(webView) == 0) {
				executeJS(webView, "javascript: function browserish_checkdoc() { if (window && document && document.head && !window.injected) { window.injected = true; browserish.setState(\"1\"); } }; browserish_checkdoc();");
			}
		}
	}
	
	class PageFinishedMethod extends XC_MethodHook {
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			String url = (String) param.args[1];
			setWebViewState(param.args[0], 2);
			XposedBridge.log("Browserish finish page: " + url);
		}
	}
	
}
