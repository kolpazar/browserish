package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;
import java.io.IOException;

public class Setting extends Pref {

	private String value;
	
	public Setting(String name, String text, String subText) {
		super(name, text, subText);
	}
	
	public Setting(String name, String text) {
		this(name, text, "");
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
	public void set(String name, String value) {
		value = value;
	}

}
