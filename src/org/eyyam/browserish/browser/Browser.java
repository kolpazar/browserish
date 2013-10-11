package org.eyyam.browserish.browser;

import org.eyyam.browserish.config.Configuration;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public abstract class Browser {

	protected Configuration config;
	
	public Browser(Configuration config) {
		this.config = config;
	}

	public abstract void initialize(LoadPackageParam loadParam);
	
}
