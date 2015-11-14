package org.eyyam.browserish;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.config.file.UserFile;
import org.eyyam.browserish.config.file.UserFileGroup;
import org.eyyam.browserish.config.file.script.UserScriptGroup;
import org.eyyam.browserish.config.file.style.UserStyleGroup;
import org.eyyam.browserish.config.setting.SettingGroup;

import de.robv.android.xposed.XposedHelpers;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.os.Environment;

public class BrowserishCore {

	private static BrowserishCore _instance;
	
	private String modulePath;
	private SettingGroup settings;
	private Map<String, UserFileGroup> userFileGroups = new LinkedHashMap<String, UserFileGroup>();
	

	public static BrowserishCore getInstance() {
		if (_instance == null) {
			_instance = new BrowserishCore();
			_instance.initialize();
		}
		return _instance;
	}
	
	public BrowserishCore() {
		File rootFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + 
				Constants.BROWSERISH_FOLDER);
		if (!rootFolder.exists()) {
			rootFolder.mkdir();
		}
		settings = new SettingGroup();
		settings.load();
	}
	
	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}
	
	public void initialize() {
		_instance = this;
		loadUserFiles();
	}

	private void loadUserFiles() {
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
	
	public byte[] getAsset(String filename) {
		Resources res = XModuleResources.createInstance(modulePath, null);
		try {
			return XposedHelpers.assetAsByteArray(res, filename);
		} catch (IOException e) {
			return null;
		}
	}

}
