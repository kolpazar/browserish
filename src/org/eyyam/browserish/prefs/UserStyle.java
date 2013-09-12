package org.eyyam.browserish.prefs;

import org.eyyam.browserish.common.PageActionType;

public class UserStyle extends UserFile {

	public UserStyle(String name) {
		super(name, PageActionType.STYLE);
	}

	@Override
	public String getSubText() {
		return "Affects " + rulesAsText();
	}

}
