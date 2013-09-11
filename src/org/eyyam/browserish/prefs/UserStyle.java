package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;
import java.io.IOException;

public class UserStyle extends UserFile {

	private String desc;
	private String apply;
	
	public UserStyle(String name) {
		super(name);
	}

	@Override
	public void save(FileOutputStream outStream) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(String name, String value) {
		if (name.equals("title")) {
			text = value;
		} else if (name.equals("desc")) {
			desc = value;
		} else if (name.equals("apply")) {
			apply = value;
		} else if (name.equals("enabled")) {
			enabled = value.equals("true");
		} 
	}

}
