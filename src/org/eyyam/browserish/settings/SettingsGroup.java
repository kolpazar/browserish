package org.eyyam.browserish.settings;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eyyam.browserish.common.Constants;

import android.os.Environment;

public class SettingsGroup {

	private Map<String, Setting> settings = new LinkedHashMap<String, Setting>();
	private String name;
	
	public SettingsGroup(String name) {
		this.name = name;
	}
	
	public Setting getByName(String settingName) {
		return settings.get(settingName);
	}
	
	public void add(Setting setting) {
		settings.put(setting.getName(), setting);
	}
	
	public ArrayList<Setting> getArrayList() {
		return new ArrayList<Setting>(settings.values());
	}
	
	public void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					Environment.getExternalStorageDirectory().getAbsolutePath() + 
					Constants.BROWSERISH_FOLDER + name + ".conf"));
			String line;
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf("=");
				if (i < 0) {
					continue;
				}
				String settingName = line.substring(0, i);
				Setting setting = settings.get(settingName);
				if (setting != null) {
					setting.setString(line.substring(i + 1));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			FileOutputStream out = new FileOutputStream(
					Environment.getExternalStorageDirectory().getAbsolutePath() + 
					Constants.BROWSERISH_FOLDER + name + ".conf");
			for (Setting setting: settings.values()) {
				String line = setting.getName() + "=" + setting.getString() + "\n";
				out.write(line.getBytes());
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
