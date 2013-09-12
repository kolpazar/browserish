package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;
import java.io.IOException;

public class SettingHeader extends Pref {

	public SettingHeader(String text) {
		super(text, text, "");
	}

	@Override
	public void save(FileOutputStream outStream) throws IOException {

	}

	@Override
	public void setConfig(String name, String value) {

	}

}
