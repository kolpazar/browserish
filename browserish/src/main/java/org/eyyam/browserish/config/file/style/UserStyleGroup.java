package org.eyyam.browserish.config.file.style;

import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.config.file.UserFileGroup;

public class UserStyleGroup extends UserFileGroup {

	public UserStyleGroup() {
		super(Constants.GROUPID_STYLE, UserStyle.class);
	}
	
}
