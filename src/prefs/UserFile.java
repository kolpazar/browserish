package prefs;

import java.io.FileOutputStream;

public abstract class UserFile extends Pref {

	protected boolean enabled;

	public UserFile(String name) {
		super(name, "", "");
	}
	
	public boolean getEnabled() {
		return enabled;
	}

}
