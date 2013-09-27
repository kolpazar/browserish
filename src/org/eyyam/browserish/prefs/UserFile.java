package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.common.PageActionType;

public abstract class UserFile extends Pref {

	protected String includes;
	protected List<UserFileRule> rules = new LinkedList<UserFileRule>();
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
	
	public void setIncludes(String includes) {
		this.includes = includes;
		String[] includesArray = includes.split("\\|");
		for (String include: includesArray) {
			UserFileRule rule = new UserFileRule();
			if (rule.setInclude(include)) {
				rules.add(rule);
			}
		}
	}
	
	@Override
	public void save(FileOutputStream outStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("title=").append(text).append("\nincludes=").append(includes).append("\nenabled=").append(enabled).append("\n\n");
		outStream.write(builder.toString().getBytes());
	}

	@Override
	public void setConfig(String name, String value) {
		if (name.equals("title")) {
			text = value;
		} else if (name.equals("includes")) {
			setIncludes(value);
		} else if (name.equals("enabled")) {
			enabled = value.equals("true");
		} 
	}

	public boolean appliesToUrl(String url) {
		if (!enabled) {
			return false;
		}
		for (UserFileRule rule: rules) {
			if (rule.matches(url)) {
				return true;
			}
		}
		return false;
	}

	public String rulesAsText() {
		StringBuilder builder = new StringBuilder();
		for (UserFileRule rule: rules) {
			if (builder.length() > 0) {
				builder.append(", ");
			}
			builder.append(rule.toString());
		}
		return builder.toString();
	}
}
