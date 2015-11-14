package org.eyyam.browserish.browser;

import org.eyyam.browserish.BrowserishCore;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public abstract class Browser {

	protected BrowserishCore browserish;
	
	public Browser(BrowserishCore browserish) {
		this.browserish = browserish;
	}

	public abstract void initialize(LoadPackageParam loadParam);
	
}
