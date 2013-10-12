package org.eyyam.browserish;

import org.eyyam.browserish.browser.Browser;
import org.eyyam.browserish.browser.BrowserAOSP;
import org.eyyam.browserish.browser.BrowserTinfoil;
import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.config.setting.SettingGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserishModule implements IXposedHookLoadPackage {

	private Browser browser = null;
	private static final String MODULE_PATH = null;
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		BrowserishCore browserish = new BrowserishCore();
		browserish.setModulePath(MODULE_PATH);
		
		if (!browserish.getSettings().getByName(SettingGroup.SETTING_GENERAL_ENABLED).getBoolean()) {
			return;
		}
		
		if (checkPackage(browserish, lpparam)) {
			browser = new BrowserAOSP(browserish);
		} else if (checkPackage(browserish, lpparam)) {
			browser = new BrowserTinfoil(browserish);
		}
		
		if (browser != null) {
			browserish.initialize();
			browser.initialize(lpparam);
		}

	}

	private boolean checkPackage(BrowserishCore browserish, LoadPackageParam lpparam) {
		boolean shouldLoad = lpparam.packageName.equals(Constants.APP_BROWSER_STOCK)
				&& browserish.getSettings().getByName(lpparam.packageName).getBoolean(); 
		if (shouldLoad) {
			XposedBridge.log("Browserish load, package: " + lpparam.packageName + ", process: " + 
					lpparam.processName + ", first: " + lpparam.isFirstApplication);
		}
		return shouldLoad;
	}
}
