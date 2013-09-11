package org.eyyam.browserish.module;

public class ModuleStyle extends Module {

	public ModuleStyle() {
		super("style");
	}

	@Override
	public String getMimeType(String filename) {
		return "text/css";
	}

	@Override
	public String getEncoding(String filename) {
		return "UTF-8";
	}

}
