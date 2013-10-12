package org.eyyam.browserish.ui;

import org.eyyam.browserish.BrowserishCore;
import org.eyyam.browserish.R;
import org.eyyam.browserish.common.Constants;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ScriptFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_script, container, false);

		ListView listScript = (ListView) view.findViewById(R.id.listScript);
		UserFileAdapter listAdapter = new UserFileAdapter(container.getContext(), R.layout.row_setting, 
				BrowserishCore.getInstance().getUserFileGroup(Constants.GROUPID_SCRIPT));
		listScript.setAdapter(listAdapter);
		return view;
	}

}
