package org.eyyam.browserish.prefs;

import org.eyyam.browserish.common.PageActionType;

public class UserScript extends UserFile {

	public UserScript(String name) {
		super(name, PageActionType.SCRIPT);
	}

	@Override
	public String getSubText() {
		return "Affects " + rulesAsText();
	}

}
