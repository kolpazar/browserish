package org.eyyam.browserish;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eyyam.browserish.common.PageAction;
import org.eyyam.browserish.module.Module;
import org.eyyam.browserish.module.ModuleStyle;

public class ModuleManager {

	private Map<String,Module> modules = new LinkedHashMap<String,Module>();
	
	public ModuleManager() {
		Module m;
		m = new ModuleStyle();
		modules.put(m.getId(), m);
	}
	
	public Module getModule(String id) {
		return modules.get(id);
	}
	
	public List<PageAction> documentStart(String url) {
		List<PageAction> list = new LinkedList<PageAction>();
		for (Module module: modules.values()) {
			module.getActionsForUrl(url, list);
		}
		return list;
	}
	
	public void documentFinish(String url) {
		
	}
	
}
