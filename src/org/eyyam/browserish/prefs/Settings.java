package org.eyyam.browserish.prefs;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eyyam.browserish.common.Constants;

public class Settings extends PrefsGroup {

	public static final String SETTING_GENERAL_ENABLED = "enabled";
	
	
	public Settings() {
		super("general");
		add(new Setting(SETTING_GENERAL_ENABLED, "Enable Browserish", ""));
		add(new SettingHeader("Browsers"));
		add(new Setting(Constants.APP_BROWSER_STOCK, "Stock Android Browser", ""));
		add(new Setting(Constants.APP_BROWSER_TINFOIL, "Tinfoil for Facebook", ""));
		//add(new Setting(Constants.APP_BROWSER_DOLPHIN, "Dolphin Browser", ""));
	}

	public Setting getByName(String prefName) {
		return (Setting) prefs.get(prefName);
	}

	protected void loadPrefs(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			int i = line.indexOf("=");
			if (i < 0) {
				continue;
			}
			String prefName = line.substring(0, i);
			Pref pref = prefs.get(prefName);
			if (pref != null) {
				pref.setConfig(null, line.substring(i + 1));
			}
		}
	}

	@Override
	protected void savePrefs(FileOutputStream outStream) throws IOException {
		for (Pref pref: prefs.values()) {
			if (pref instanceof Setting) {
				pref.save(outStream);
			}
		}
	}

}
