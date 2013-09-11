package org.eyyam.browserish.module;

import java.util.List;

import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.prefs.UserFiles;
import org.eyyam.browserish.prefs.UserStyle;

import de.robv.android.xposed.XposedBridge;

public class ModuleStyle extends Module {

	private UserFiles styles;
	
	public ModuleStyle() {
		super("style");
		styles = new UserFiles(id, UserStyle.class);
		styles.load();
		XposedBridge.log("Browserish " + styles.size() + " styles loaded.");
	}

	@Override
	public String getMimeType(String filename) {
		return "text/css";
	}

	@Override
	public String getEncoding(String filename) {
		return "UTF-8";
	}

	@Override
	public void getActionsForUrl(String url, List<PageAction> list) {
		styles.getActionsForUrl(url, list);
	}

}
