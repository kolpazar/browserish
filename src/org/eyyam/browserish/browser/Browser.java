package org.eyyam.browserish.browser;

import org.eyyam.browserish.ModuleManager;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public abstract class Browser {

	protected ModuleManager moduleManager;
	
	public Browser(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	public abstract void initialize(LoadPackageParam loadParam);
	
}
