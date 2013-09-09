package org.eyyam.browserish;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class BrowserishModule implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals("com.android.browser")) {
			XposedBridge.log("Browserish loaded");
			Class<?> controllerClass = XposedHelpers.findClass("com.android.browser.Controller", lpparam.classLoader);
			final Class<?> tabClass = XposedHelpers.findClass("com.android.browser.Tab", lpparam.classLoader);
			final Class<?> webViewClass = XposedHelpers.findClass("android.webkit.WebView", lpparam.classLoader);
			Method pageFinishedMethod = XposedHelpers.findMethodBestMatch(controllerClass, "onPageFinished", tabClass);
			XposedBridge.hookMethod(pageFinishedMethod, new XC_MethodHook() {
	    		@Override
	    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	    			Method getUrlMethod = XposedHelpers.findMethodExact(tabClass, "getUrl");
	    			XposedBridge.log("Browserish page finished: " + (String) (getUrlMethod.invoke(param.args[0])));
	    			Field webViewField = XposedHelpers.findField(tabClass, "mMainView");
	    			Object webView = webViewField.get(param.args[0]);
	    			Method webViewLoadUrlMethod = XposedHelpers.findMethodExact(webViewClass, "loadUrl", String.class);
	    			//webView.loadUrl("javascript:var styles = \"@import url(' http://quinny898.co.uk/css/sw-stripper.css ');\"; var newSS=document.createElement('link'); newSS.rel='stylesheet'; newSS.href='data:text/css,'+escape(styles); document.getElementsByTagName(\"head\")[0].appendChild(newSS);");
	    			webViewLoadUrlMethod.invoke(webView, "javascript:var styles = \"body { color: red !important; }\"; var newSS=document.createElement('link'); newSS.rel='stylesheet'; newSS.href='data:text/css,'+escape(styles); document.getElementsByTagName(\"head\")[0].appendChild(newSS);");
	    		}
			});
		}
	}

}
