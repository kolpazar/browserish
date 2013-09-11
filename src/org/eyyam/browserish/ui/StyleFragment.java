package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.prefs.UserFiles;
import org.eyyam.browserish.prefs.UserStyle;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

		ListView listGeneral = (ListView) view.findViewById(R.id.listStyle);
		UserFileAdapter listAdapter = new UserFileAdapter(container.getContext(), R.layout.prefs_check, styles.getArrayList());
		listGeneral.setAdapter(listAdapter);
		
		return view;
	}

}
