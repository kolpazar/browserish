package org.eyyam.browserish.config.file.script;

import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.common.Constants.ApplyType;
import org.eyyam.browserish.config.file.UserFile;

public class UserScript extends UserFile {

	public UserScript(String groupId, String name) {
		super(groupId, name);
		this.applyTime = ApplyTime.DOCUMENT_FINISH;
		this.applyType = ApplyType.SCRIPT;
	}

	@Override
	public String getMimeType() {
		return "application/javascript";
	}

	@Override
	public String getEncoding() {
		return "UTF-8";
	}

	@Override
	public String getSubText() {
		return "Affects " + rulesAsText();
	}

}
