package org.eyyam.browserish.ui;


import org.eyyam.browserish.R;
import org.eyyam.browserish.settings.GeneralSettings;
import org.eyyam.browserish.settings.Setting;
import org.eyyam.browserish.settings.SettingsGroup;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class GeneralFragment extends Fragment {

	private SettingsGroup generalSettings;
	
	public GeneralFragment() {
		generalSettings = new GeneralSettings();
		generalSettings.load();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_general, container, false);
		
		ListView listGeneral = (ListView) view.findViewById(R.id.listGeneral);
		GeneralListAdapter listAdapter = new GeneralListAdapter(container.getContext(), R.layout.prefs_check, generalSettings.getArrayList());
		listGeneral.setAdapter(listAdapter);
		listGeneral.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Setting setting = (Setting) parent.getItemAtPosition(position);
				setting.setBoolean(!setting.getBoolean());
				CheckBox check = (CheckBox) view.findViewById(R.id.prefsCheck);
				check.toggle();
				Toast.makeText(getActivity().getApplicationContext(), setting.getText(), Toast.LENGTH_SHORT).show();
				generalSettings.save();
			}
		});
		
		return view;
	}

}
