package org.eyyam.browserish.prefs;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UserFiles extends PrefsGroup {

	private Class<UserFile> fileClass;
	
	public UserFiles(String name, Class<?> fileClass) {
		super(name);
		this.fileClass = (Class<UserFile>) fileClass; 
	}

	@Override
	protected void loadPrefs(BufferedReader reader) throws IOException {
		prefs.clear();
		String line;
		UserFile pref = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("[") && line.endsWith("]")) {
				String file = line.substring(1, line.length() - 1);
				Constructor<UserFile> c;
				try {
					c = fileClass.getConstructor(String.class);
					pref = c.newInstance(file);
					prefs.put(file, pref);
				} catch (Exception e) {
				}
				continue;
			}
			
			int i = line.indexOf("=");
			if ((pref == null) || (i < 0)) {
				continue;
			}
			pref.set(line.substring(0, i), line.substring(i + 1));
		}
	}

	@Override
	protected void savePrefs(FileOutputStream outStream) throws IOException {
		for (Pref pref: prefs.values()) {
			String name = "[" + pref.getName() + "]\n";
			outStream.write(name.getBytes());
			pref.save(outStream);
		}
	}

}
