package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;

import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.common.PageActionType;

public abstract class UserFile extends Pref {

	protected String apply;
	protected boolean enabled;
	protected PageAction action;
	protected PageActionType actionType;

	public UserFile(String name, PageActionType actionType) {
		super(name, "", "");
		this.actionType = actionType;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public abstract String getSubText();
	
	public PageAction getAction(String moduleId) {
		if (action == null) {
			action = new PageAction(actionType, "/" + moduleId + "/" + name);
		}
		return action;
	}
	
	public boolean appliesToUrl(String url) {
		return enabled;
	}

}
