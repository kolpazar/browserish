package org.eyyam.browserish.prefs;

import java.util.regex.Pattern;

public class UserFileRule {

	enum IncludeType {
		NONE, ALL, URL, PREFIX, REGEX
	}

	private IncludeType type = IncludeType.NONE;
	private char prefix;
	private String ruleUrl = "";
	private Pattern pattern;
	
	public UserFileRule() {

	}
	
	public boolean setInclude(String include) {
		if ((include != null) && !include.isEmpty()) {
			prefix = include.charAt(0);
			if (include.equals("*")) {
				type = IncludeType.ALL;
			} else if (include.length() > 1) {
				ruleUrl = include.substring(1);
				switch (prefix) {
				case 'U':
					type = IncludeType.URL;
					break;
				case 'P':
					type = IncludeType.PREFIX;
					break;
				case 'D':
					type = IncludeType.REGEX;
					pattern = Pattern.compile("^https?://" + ruleUrl.replace(".", "\\.") + "(.*)");
					break;
				case 'G':
					type = IncludeType.REGEX;
					pattern = Pattern.compile("^" + ruleUrl.replace("\\", "\\\\").replace(".", "\\.").replace("*", ".*") + "$");
					break;
				case 'R':
					type = IncludeType.REGEX;
					pattern = Pattern.compile(ruleUrl);
					break;
				}
			}
		}
		return !type.equals(IncludeType.NONE);
	}

	public boolean matches(String url) {
		switch (type) {
		case ALL:
			return true;
		case URL:
			return ruleUrl.equals(url);
		case PREFIX:
			return url.startsWith(ruleUrl);
		case REGEX:
			return pattern.matcher(url).matches();
		default:
			return false;
		}
	}
	
	public String toString() {
		switch (type) {
		case ALL:
			return "all pages";
		case PREFIX:
			return ruleUrl + "*";
		default:
			return ruleUrl;
		}
	}
}
