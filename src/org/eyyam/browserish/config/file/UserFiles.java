package org.eyyam.browserish.config.file;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.config.base.Pref;
import org.eyyam.browserish.config.base.PrefsGroup;

public class UserFiles extends PrefsGroup {

	private Class<UserFile> fileClass;
	
	public UserFiles(String id, Class<?> fileClass) {
		super(id);
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
					c = fileClass.getConstructor(String.class, String.class);
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
