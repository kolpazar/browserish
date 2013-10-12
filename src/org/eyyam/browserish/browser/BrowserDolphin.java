package org.eyyam.browserish.browser;

import java.util.Map;

import org.eyyam.browserish.BrowserishCore;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserDolphin extends BrowserWebView {

	private Class<?> webViewClientClass;
	private Class<?> webChromeClientClass;

	public BrowserDolphin(BrowserishCore browserish) {
		super(browserish);
	}
	
	@Override
	public void initialize(LoadPackageParam loadParam) {
		webViewClass = XposedHelpers.findClass("dolphin.webkit.WebView", loadParam.classLoader);
		webViewClientClass = XposedHelpers.findClass("dolphin.webkit.WebViewClient", loadParam.classLoader);
		webChromeClientClass = XposedHelpers.findClass("dolphin.webkit.WebChromeClient", loadParam.classLoader);
		
		XposedBridge.hookMethod(
				XposedHelpers.findMethodBestMatch(webViewClientClass, "shouldInterceptRequest", webViewClass, String.class), 
				new FileLoaderMethod());
		
		XposedBridge.hookMethod(
				XposedHelpers.findConstructorBestMatch(webViewClass, Context.class, AttributeSet.class, Integer.class, Map.class, Boolean.class), 
				new WebViewConstructor());

		XposedBridge.hookMethod(
				XposedHelpers.findMethodBestMatch(webViewClientClass, "onPageStarted", webViewClass, String.class, Bitmap.class), 
				new PageStartedMethod());
		
		XposedBridge.hookMethod(
				XposedHelpers.findMethodBestMatch(webViewClientClass, "onPageFinished", webViewClass, String.class), 
				new PageFinishedMethod());
		
		XposedBridge.hookMethod(
				XposedHelpers.findMethodBestMatch(webChromeClientClass, "onProgressChanged", webViewClass, Integer.class), 
				new ProgressMethod());

	}

}
