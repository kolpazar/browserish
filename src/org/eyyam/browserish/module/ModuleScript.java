package org.eyyam.browserish.module;

import java.util.List;

import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.common.PageActionTime;
import org.eyyam.browserish.prefs.UserFiles;
import org.eyyam.browserish.prefs.UserScript;
import org.eyyam.browserish.prefs.UserStyle;

import de.robv.android.xposed.XposedBridge;

public class ModuleScript extends Module {

	private UserFiles scripts;
	
	public ModuleScript() {
		super("script");
		scripts = new UserFiles(id, UserScript.class);
		scripts.load();
		XposedBridge.log("Browserish " + scripts.size() + " scripts loaded.");
	}

	@Override
	public String getMimeType(String filename) {
		return "application/javascript";
	}

	@Override
	public String getEncoding(String filename) {
		return "UTF-8";
	}

	@Override
	public void getActionsForUrl(String url, PageActionTime time, List<PageAction> list) {
		if (time.equals(PageActionTime.DOCUMENT_FINISH)) {
			scripts.getActionsForUrl(url, list);
		}
	}

}
