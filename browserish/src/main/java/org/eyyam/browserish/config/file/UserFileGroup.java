package org.eyyam.browserish.config.file;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.config.Pref;
import org.eyyam.browserish.config.PrefsGroup;

public class UserFileGroup extends PrefsGroup {

	private Class<? extends UserFile> fileClass;
	
	public UserFileGroup(String id, Class<? extends UserFile> fileClass) {
		super(id);
		this.fileClass = fileClass; 
	}

	@Override
	protected void loadPrefs(BufferedReader reader) throws IOException {
		prefs.clear();
		String line;
		UserFile pref = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("[") && line.endsWith("]")) {
				String file = line.substring(1, line.length() - 1);
				Constructor<? extends UserFile> c;
				try {
					c = (Constructor<? extends UserFile>) fileClass.getConstructor(String.class, String.class);
					pref = c.newInstance(id, file);
					prefs.put(file, pref);
				} catch (Exception e) {
				}
				continue;
			}
			
			int i = line.indexOf("=");
			if ((pref == null) || (i < 0)) {
				continue;
			}
			pref.setConfig(line.substring(0, i), line.substring(i + 1));
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
	
	public UserFile getFile(String name) {
		return (UserFile) prefs.get(name);
	}
	
	public void getUserFilesForUrl(String url, ApplyTime time, List<UserFile> list) {
		for (Pref pref: prefs.values()) {
			if (((UserFile) pref).appliesTo(url, time)) {
				list.add((UserFile) pref);
			}
		}
	}

}
