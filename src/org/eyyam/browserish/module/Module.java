package org.eyyam.browserish.module;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.common.PageActionTime;

import android.os.Environment;

public abstract class Module {
	
	protected String id;
	
	public Module(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public InputStream getFile(String filename) {
		try {
			return new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + 
					Constants.BROWSERISH_FOLDER + id + "/" + filename);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public abstract String getMimeType(String filename);
	
	public abstract String getEncoding(String filename);

	public abstract void getActionsForUrl(String url, PageActionTime time, List<PageAction> list);
	
}
