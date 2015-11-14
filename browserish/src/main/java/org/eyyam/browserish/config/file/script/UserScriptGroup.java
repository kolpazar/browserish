package org.eyyam.browserish.config.file.script;

import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.config.file.UserFileGroup;

public class UserScriptGroup extends UserFileGroup {
	
	public UserScriptGroup() {
		super(Constants.GROUPID_SCRIPT, UserScript.class);
	}

}
