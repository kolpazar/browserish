package org.eyyam.browserish.settings;

public class Setting {

	private String name;
	private String text;
	private String subText;
	private String value;
	
	public Setting(String name, String text, String subText) {
		this.name = name;
		this.text = text;
		this.subText = subText;
	}
	
	public Setting(String name, String text) {
		this(name, text, "");
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

}
