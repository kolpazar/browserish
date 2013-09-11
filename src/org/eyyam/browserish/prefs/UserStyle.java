package org.eyyam.browserish.prefs;

import java.io.FileOutputStream;
import java.io.IOException;

import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.common.PageActionType;

public class UserStyle extends UserFile {

	public UserStyle(String name) {
		super(name, PageActionType.STYLE);
	}

	@Override
	public void save(FileOutputStream outStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("title=").append(text).append("\napply=").append(apply).append("\nenabled=").append(enabled).append("\n\n");
		outStream.write(builder.toString().getBytes());
	}

	@Override
	public void set(String name, String value) {
		if (name.equals("title")) {
			text = value;
		} else if (name.equals("apply")) {
			apply = value;
		} else if (name.equals("enabled")) {
			enabled = value.equals("true");
		} 
	}

	@Override
	public String getSubText() {
		return "Affects all pages";
	}

}
