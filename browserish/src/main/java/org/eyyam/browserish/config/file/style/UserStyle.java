package org.eyyam.browserish.config.file.style;

import org.eyyam.browserish.common.Constants.ApplyTime;
import org.eyyam.browserish.common.Constants.ApplyType;
import org.eyyam.browserish.config.file.UserFile;

public class UserStyle extends UserFile {

	public UserStyle(String groupId, String name) {
		super(groupId, name);
		this.applyTime = ApplyTime.DOCUMENT_START;
		this.applyType = ApplyType.STYLE;
	}

	@Override
	public String getMimeType() {
		return "text/css";
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
