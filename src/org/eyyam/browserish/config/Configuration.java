package org.eyyam.browserish.config;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.config.file.UserFile;
import org.eyyam.browserish.config.file.UserFileGroup;
import org.eyyam.browserish.config.file.script.UserScriptGroup;
import org.eyyam.browserish.config.file.style.UserStyleGroup;
import org.eyyam.browserish.config.setting.SettingGroup;

public class Configuration {
	
	private SettingGroup settings;
	private Map<String, UserFileGroup> userFileGroups = new LinkedHashMap<String, UserFileGroup>();
	
	public Configuration() {
		settings = new SettingGroup();
		settings.load();
	}
	
	public void loadUserFiles() {
		addUserFileGroup(new UserStyleGroup());
		addUserFileGroup(new UserScriptGroup());
		
		for (UserFileGroup userFileGroup: userFileGroups.values()) {
			userFileGroup.load();
		}
	}
	
	private void addUserFileGroup(UserFileGroup userFileGroup) {
		userFileGroups.put(userFileGroup.getId(), userFileGroup);
	}
	
	public SettingGroup getSettings() {
		return settings;
	}
	
	public UserFileGroup getUserFileGroup(String id) {
		return userFileGroups.get(id);
	}
	
	public List<UserFile> getUserFilesForUrl(String url, ApplyTime time) {
		List<UserFile> list = new LinkedList<UserFile>();
		for (UserFileGroup userFileGroup: userFileGroups.values()) {
			userFileGroup.getUserFilesForUrl(url, time, list);
		}
		return list;
	}

}
