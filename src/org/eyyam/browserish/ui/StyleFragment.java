package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.config.file.UserFiles;
import org.eyyam.browserish.config.file.style.UserStyle;

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

		ListView listStyle = (ListView) view.findViewById(R.id.listStyle);
		UserFileAdapter listAdapter = new UserFileAdapter(container.getContext(), R.layout.row_setting, styles);
		listStyle.setAdapter(listAdapter);
		return view;
	}

}
