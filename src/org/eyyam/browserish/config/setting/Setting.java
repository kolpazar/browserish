package org.eyyam.browserish.config.setting;

import java.io.FileOutputStream;
import java.io.IOException;

import org.eyyam.browserish.config.base.Pref;

public class Setting extends Pref {

	private String value;
	
	public Setting(String groupId, String name, String text, String subText) {
		super(groupId, name, text, subText);
	}
	
	public Setting(String groupId, String name, String text) {
		this(groupId, name, text, "");
	}

	public void setBoolean(boolean value) {
		this.value = Boolean.toString(value);
	}
	
	public boolean getBoolean() {
		return Boolean.valueOf(value);
	}
	
	public void setString(String value) {
		this.value = value;
	}
	
	public String getString() {
		return value;
	}

	@Override
	public void save(FileOutputStream outStream) throws IOException {
		String line = name + "=" + value + "\n";
		outStream.write(line.getBytes());
	}

	@Override
	public void setConfig(String name, String value) {
		this.value = value;
	}

}
