package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.prefs.UserFiles;
import org.eyyam.browserish.prefs.UserScript;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ScriptFragment extends Fragment {

	private UserFiles scripts;
	
	public ScriptFragment() {
		scripts = new UserFiles("script", UserScript.class);
		scripts.load();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_script, container, false);

		ListView listScript = (ListView) view.findViewById(R.id.listScript);
		UserFileAdapter listAdapter = new UserFileAdapter(container.getContext(), R.layout.row_setting, scripts);
		listScript.setAdapter(listAdapter);
		return view;
	}

}
