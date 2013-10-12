package org.eyyam.browserish.config.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.common.Constants.ApplyType;
import org.eyyam.browserish.config.Pref;

import android.os.Environment;

public abstract class UserFile extends Pref {

	protected String includes;
	protected List<UserFileRule> rules = new LinkedList<UserFileRule>();
	protected boolean enabled;
	protected ApplyTime applyTime;
	protected ApplyType applyType;

	public UserFile(String groupId, String name) {
		super(groupId, name, "", "");
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public ApplyType getApplyType() {
		return applyType;
	}
	
	public String getRelativePath() {
		return groupId + "/" + name;
	}
	
	public abstract String getMimeType();
	
	public abstract String getEncoding();

	public abstract String getSubText();
	
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

	public boolean appliesTo(String url, ApplyTime time) {
		if (!enabled || this.applyTime != time) {
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
	
	public InputStream createStream() {
		try {
			return new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + 
					Constants.BROWSERISH_FOLDER + getRelativePath());
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
