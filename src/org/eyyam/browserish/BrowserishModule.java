package org.eyyam.browserish;

import org.eyyam.browserish.browser.Browser;
import org.eyyam.browserish.browser.BrowserAOSP;
import org.eyyam.browserish.settings.GeneralSettings;
import org.eyyam.browserish.settings.SettingsGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BrowserishModule implements IXposedHookLoadPackage {

	private Browser browser = null;
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		SettingsGroup generalSettings = new GeneralSettings();
		generalSettings.load();
		
		if (!generalSettings.getByName(GeneralSettings.SETTING_GENERAL_ENABLED).getBoolean()) {
			return;
		}
		
		if (lpparam.packageName.equals("com.android.browser")) {
			if (generalSettings.getByName(GeneralSettings.SETTING_GENERAL_ENABLED_AOSP).getBoolean()) {
				XposedBridge.log("Browserish load, package: " + lpparam.packageName + ", process: " + lpparam.processName + ", first: " + lpparam.isFirstApplication);
				browser = new BrowserAOSP(new ModuleManager());
			}
		}
		
		if (browser != null) {
			browser.initialize(lpparam);
		}

	}

}
