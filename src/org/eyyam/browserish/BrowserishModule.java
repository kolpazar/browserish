package org.eyyam.browserish;

import org.eyyam.browserish.browser.Browser;
import org.eyyam.browserish.browser.BrowserAOSP;
import org.eyyam.browserish.browser.BrowserDolphin;
import org.eyyam.browserish.browser.BrowserTinfoil;
import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.prefs.Settings;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserishModule implements IXposedHookLoadPackage {

	private Browser browser = null;
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		Settings settings = new Settings();
		settings.load();
		
		if (!settings.getByName(Settings.SETTING_GENERAL_ENABLED).getBoolean()) {
			return;
		}
		
		if (lpparam.packageName.equals(Constants.APP_BROWSER_STOCK)) {
			if (settings.getByName(Constants.APP_BROWSER_STOCK).getBoolean()) {
				XposedBridge.log("Browserish load, package: " + lpparam.packageName + ", process: " + lpparam.processName + ", first: " + lpparam.isFirstApplication);
				browser = new BrowserAOSP(new ModuleManager());
			}
		} else if (lpparam.packageName.equals(Constants.APP_BROWSER_TINFOIL)) {
			if (settings.getByName(Constants.APP_BROWSER_TINFOIL).getBoolean()) {
				XposedBridge.log("Browserish load, package: " + lpparam.packageName + ", process: " + lpparam.processName + ", first: " + lpparam.isFirstApplication);
				browser = new BrowserTinfoil(new ModuleManager());
			}
		} /*else if (lpparam.packageName.equals(Constants.APP_BROWSER_DOLPHIN)) {
			if (settings.getByName(Constants.APP_BROWSER_DOLPHIN).getBoolean()) {
				XposedBridge.log("Browserish load, package: " + lpparam.packageName + ", process: " + lpparam.processName + ", first: " + lpparam.isFirstApplication);
				browser = new BrowserDolphin(new ModuleManager());
			}
		} */
		
		if (browser != null) {
			browser.initialize(lpparam);
		}

	}

}
