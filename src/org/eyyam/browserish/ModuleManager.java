package org.eyyam.browserish;

import java.util.HashMap;
import java.util.Map;

import org.eyyam.browserish.module.Module;
import org.eyyam.browserish.module.ModuleStyle;

public class ModuleManager {

	private Map<String,Module> modules = new HashMap<String,Module>();
	
	public ModuleManager() {
		Module m;
		m = new ModuleStyle();
		modules.put(m.getId(), m);
	}
	
	public Module getModule(String id) {
		return modules.get(id);
	}
	
	public void documentStart() {
		
	}
	
	public void documentFinish() {
		
	}
	
}
