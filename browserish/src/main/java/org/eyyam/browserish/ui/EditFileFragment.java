package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditFileFragment extends DialogFragment {

	public EditFileFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(inflater.inflate(R.layout.fragment_edit_file, null));
		return null;// builder.create();
	}

}
