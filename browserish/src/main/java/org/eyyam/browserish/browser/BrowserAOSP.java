package org.eyyam.browserish.browser;

import java.lang.reflect.Field;
import java.util.Map;

import org.eyyam.browserish.BrowserishCore;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserAOSP extends BrowserWebView {

	private static final String FIELD_TAB_INITED_KEY = "browserish-tab-inited";
	
	private Class<?> webViewControllerClass;
	private Class<?> tabClass;
	private Class<?> homeProviderClass;
	
	public BrowserAOSP(BrowserishCore browserish) {
		super(browserish);
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
	
}
