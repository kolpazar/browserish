package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Pref {

	protected String name;
	protected String text;
	protected String subText;

	public Pref(String name, String text, String subText) {
		this.name = name;
		this.text = text;
		this.subText = subText;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public String getSubText() {
		return subText;
	}
	
	public abstract void save(FileOutputStream outStream) throws IOException;

	public abstract void setConfig(String name, String value);
	
}
