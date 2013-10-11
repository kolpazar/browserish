package org.eyyam.browserish;

import org.eyyam.browserish.browser.Browser;
import org.eyyam.browserish.browser.BrowserAOSP;
import org.eyyam.browserish.browser.BrowserTinfoil;
import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.config.Configuration;
import org.eyyam.browserish.config.setting.SettingGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserishModule implements IXposedHookLoadPackage {

	private Browser browser = null;
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		Configuration config = new Configuration();
		
		if (!config.getSettings().getByName(SettingGroup.SETTING_GENERAL_ENABLED).getBoolean()) {
			return;
		}
		
		if (checkPackage(config, lpparam)) {
			browser = new BrowserAOSP(config);
		} else if (checkPackage(config, lpparam)) {
			browser = new BrowserTinfoil(config);
		}
		
		if (browser != null) {
			config.loadUserFiles();
			browser.initialize(lpparam);
		}

	}

	private boolean checkPackage(Configuration config, LoadPackageParam lpparam) {
		boolean shouldLoad = lpparam.packageName.equals(Constants.APP_BROWSER_STOCK)
				&& config.getSettings().getByName(lpparam.packageName).getBoolean(); 
		if (shouldLoad) {
			XposedBridge.log("Browserish load, package: " + lpparam.packageName + ", process: " + 
					lpparam.processName + ", first: " + lpparam.isFirstApplication);
		}
		return shouldLoad;
	}
}
