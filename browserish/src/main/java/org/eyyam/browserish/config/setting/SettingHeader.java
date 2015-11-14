package org.eyyam.browserish.config.setting;

import java.io.FileOutputStream;
import java.io.IOException;

import org.eyyam.browserish.config.Pref;

public class SettingHeader extends Pref {

	public SettingHeader(String groupId, String text) {
		super(groupId, text, text, "");
	}

	@Override
	public void save(FileOutputStream outStream) throws IOException {

	}

	@Override
	public void setConfig(String name, String value) {

	}

}
