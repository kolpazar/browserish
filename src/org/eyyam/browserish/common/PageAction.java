package org.eyyam.browserish.common;

public class PageAction {

	private PageActionType type;
	private String filename;
	
	public PageAction(PageActionType type, String filename) {
		super();
		this.type = type;
		this.filename = filename;
	}

	public PageActionType getType() {
		return type;
	}

	public String getFilename() {
		return filename;
	}
	
}
