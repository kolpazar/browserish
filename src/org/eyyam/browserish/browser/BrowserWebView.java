package org.eyyam.browserish.browser;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eyyam.browserish.ModuleManager;
import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.common.PageActionTime;
import org.eyyam.browserish.module.Module;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebResourceResponse;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserWebView extends Browser {

	protected static final String PREFIX = "browserish://";
	protected static final String FIELD_STATE_KEY = "browserish-state";
	protected static final String FIELD_URL_KEY = "browserish-url";
	protected static final String FIELD_INTF_KEY = "browserish-interface";
	
	protected String uuid;

	protected Class<?> webViewClass;

	public BrowserWebView(ModuleManager moduleManager) {
		super(moduleManager);
		uuid = UUID.randomUUID().toString();
	}

	@Override
	public void initialize(LoadPackageParam loadParam) {
		// TODO Auto-generated method stub

	}

	protected String getWebViewUrl(Object webView) {
		String url = (String) XposedHelpers.getAdditionalInstanceField(webView, FIELD_URL_KEY);
		if (url == null) {
			url = "";
		}
		return url;
	}
	
	protected void setWebViewUrl(Object webView, String url) {
		XposedHelpers.setAdditionalInstanceField(webView, FIELD_URL_KEY, url);
	}
	
	protected int getWebViewState(Object webView) {
		Integer state = (Integer) XposedHelpers.getAdditionalInstanceField(webView, FIELD_STATE_KEY);
		if (state != null) {
			return state.intValue();
		} else {
			XposedHelpers.setAdditionalInstanceField(webView, FIELD_STATE_KEY, Integer.valueOf(0));
			return 0;
		}
	}
	
	protected void setWebViewState(Object webView, int state) {
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
	
	protected void executeJS(final Object webView, final String js) {
		/*
		new Handler(Looper.getMainLooper()).post(new Runnable() {
		    @Override
		    public void run() {*/
				try {
					Method webViewLoadUrlMethod = XposedHelpers.findMethodExact(webViewClass, "loadUrl", String.class);
					webViewLoadUrlMethod.invoke(webView, js);
				} catch (Exception e) {
					XposedBridge.log("browserish: JS execution error.");
				}/*
			}
		});*/
	}
	
	protected void injectStyle(Object webView, String filename) {
		String js = "javascript: function browserish_inject() { var elem = document.createElement('link'); elem.rel = 'stylesheet'; elem.href = 'browserish://" + uuid + filename + "'; document.documentElement.appendChild(elem); }; browserish_inject(); ";
		executeJS(webView, js);
	}
	
	protected void injectScript(Object webView, String filename) {
		String js = "javascript: function browserish_inject() { var elem = document.createElement('script'); elem.type = 'text/javascript'; elem.src = 'browserish://" + uuid + filename + "'; document.documentElement.appendChild(elem); }; browserish_inject(); ";
		executeJS(webView, js);
	}
	
	protected void applyAction(Object webView, PageAction action) {
		XposedBridge.log("Browserish action " + action.getType() + " file " + action.getFilename());
		switch(action.getType()) {
		case STYLE:
			injectStyle(webView, action.getFilename());
			break;
		case SCRIPT:
			injectScript(webView, action.getFilename());
			break;
		}
	}
	
	protected void documentStart(Object webView) {
		XposedBridge.log("Browserish DOC START");
		List<PageAction> actions = moduleManager.getActionsForUrl(getWebViewUrl(webView), PageActionTime.DOCUMENT_START);
		XposedBridge.log("Browserish applying " + actions.size() + " actions.");
		for (PageAction action: actions) {
			applyAction(webView, action);
		}
		//String js = "javascript: function browserish_inject() { var elem = document.createElement('link'); elem.rel = 'stylesheet'; elem.href = 'browserish://" + uuid + "/style/red.css'; document.documentElement.appendChild(elem); }; browserish_inject(); ";
		//executeJS(webView, js);
	}
	
	protected void documentLoaded(Object webView) {
		XposedBridge.log("Browserish DOC FINISH");
		List<PageAction> actions = moduleManager.getActionsForUrl(getWebViewUrl(webView), PageActionTime.DOCUMENT_FINISH);
		XposedBridge.log("Browserish applying " + actions.size() + " actions.");
		for (PageAction action: actions) {
			applyAction(webView, action);
		}
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
				XposedBridge.log("Browserish trying to attach");
				executeJS(webView, "javascript: function browserish_checkdoc() { if (window && document && document.documentElement && !window.injected) { window.injected = true; browserish.setState(\"1\"); } }; browserish_checkdoc();");
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
