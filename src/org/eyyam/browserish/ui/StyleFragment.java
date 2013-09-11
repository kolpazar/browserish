package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.prefs.Setting;
import org.eyyam.browserish.prefs.UserFiles;
import org.eyyam.browserish.prefs.UserStyle;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

public class StyleFragment extends Fragment {

	private UserFiles styles;
	
	public StyleFragment() {
		styles = new UserFiles("style", UserStyle.class);
		styles.load();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_style, container, false);

		ListView listStyle = (ListView) view.findViewById(R.id.listStyle);
		UserFileAdapter listAdapter = new UserFileAdapter(container.getContext(), R.layout.row_setting, styles.getArrayList());
		listStyle.setAdapter(listAdapter);
		listStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UserStyle style = (UserStyle) parent.getItemAtPosition(position);
				style.setEnabled(!style.isEnabled());
				CheckBox check = (CheckBox) view.findViewById(R.id.fileCheck);
				check.toggle();
				styles.save();
			}
		});
		
		return view;
	}

}
