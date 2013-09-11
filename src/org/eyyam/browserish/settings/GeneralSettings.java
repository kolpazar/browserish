package org.eyyam.browserish.settings;

public class GeneralSettings extends SettingsGroup {

	public static final String SETTING_GENERAL_ENABLED = "enabled";
	public static final String SETTING_GENERAL_ENABLED_AOSP = "enabled.com.android.browser";
	
	
	public GeneralSettings() {
		super("general");
		add(new Setting(SETTING_GENERAL_ENABLED, "Enable Browserish", ""));
		add(new Setting(SETTING_GENERAL_ENABLED_AOSP, "Enable AOSP Browser", ""));
	}

}
