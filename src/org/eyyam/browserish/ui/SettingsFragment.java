package org.eyyam.browserish.ui;


import org.eyyam.browserish.R;

import prefs.Settings;
import prefs.Setting;
import prefs.PrefsGroup;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

	private Settings settings;
	
	public SettingsFragment() {
		settings = new Settings();
		settings.load();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_general, container, false);
		
		ListView listGeneral = (ListView) view.findViewById(R.id.listGeneral);
		SettingsAdapter listAdapter = new SettingsAdapter(container.getContext(), R.layout.prefs_check, settings.getArrayList());
		listGeneral.setAdapter(listAdapter);
		listGeneral.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Setting setting = (Setting) parent.getItemAtPosition(position);
				setting.setBoolean(!setting.getBoolean());
				CheckBox check = (CheckBox) view.findViewById(R.id.prefsCheck);
				check.toggle();
				settings.save();
			}
		});
		
		return view;
	}

}