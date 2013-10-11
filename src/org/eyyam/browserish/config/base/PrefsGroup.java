package org.eyyam.browserish.config.base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eyyam.browserish.common.Constants;

import android.os.Environment;

public abstract class PrefsGroup {

	protected String id;
	protected Map<String, Pref> prefs = new LinkedHashMap<String, Pref>();
	
	public PrefsGroup(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public Pref getByName(String prefName) {
		return prefs.get(prefName);
	}
	
	public void add(Pref pref) {
		prefs.put(pref.getName(), pref);
	}
	
	public ArrayList<Pref> getArrayList() {
		return new ArrayList<Pref>(prefs.values());
	}
	
	public int size() {
		return prefs.size();
	}
	
	public void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					Environment.getExternalStorageDirectory().getAbsolutePath() + 
					Constants.BROWSERISH_FOLDER + id + ".conf"));
			
			loadPrefs(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected abstract void loadPrefs(BufferedReader reader) throws IOException;
	
	public void save() {
		try {
			FileOutputStream out = new FileOutputStream(
					Environment.getExternalStorageDirectory().getAbsolutePath() + 
					Constants.BROWSERISH_FOLDER + id + ".conf");
			savePrefs(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void savePrefs(FileOutputStream outStream) throws IOException;
	
}
