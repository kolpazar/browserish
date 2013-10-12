package org.eyyam.browserish.common;

public class Constants {

	public static final String BROWSERISH_FOLDER = "/browserish/";

	public static final String APP_BROWSER_STOCK = "com.android.browser";
	public static final String APP_BROWSER_TINFOIL = "com.danvelazco.fbwrapper";
	public static final String APP_BROWSER_DOLPHIN = "mobi.mgeek.TunnyBrowser";

	public static final String GROUPID_SETTINGS = "general";
	public static final String GROUPID_STYLE = "style";
	public static final String GROUPID_SCRIPT = "script";
	
	public enum ApplyTime {
		DOCUMENT_START, DOCUMENT_FINISH
	}
	
	public enum ApplyType {
		STYLE, SCRIPT
	}
	
}
