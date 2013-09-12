package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.prefs.UserFiles;
import org.eyyam.browserish.prefs.UserScript;
import org.eyyam.browserish.prefs.UserStyle;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
		UserFileAdapter listAdapter = new UserFileAdapter(container.getContext(), R.layout.row_setting, scripts.getArrayList());
		listScript.setAdapter(listAdapter);
		listScript.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UserScript script = (UserScript) parent.getItemAtPosition(position);
				script.setEnabled(!script.isEnabled());
				CheckBox check = (CheckBox) view.findViewById(R.id.fileCheck);
				check.toggle();
				scripts.save();
			}
		});
		
		return view;
	}

}
