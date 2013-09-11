package org.eyyam.browserish.ui;

import java.util.ArrayList;

import org.eyyam.browserish.R;
import org.eyyam.browserish.prefs.Pref;
import org.eyyam.browserish.prefs.UserFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class UserFileAdapter extends ArrayAdapter<Pref> {

	private ArrayList<Pref> fileList;

	public UserFileAdapter(Context context, int textViewResourceId, ArrayList<Pref> fileList) {
		super(context, textViewResourceId, fileList);
		this.fileList = new ArrayList<Pref>();
		this.fileList.addAll(fileList);
	}

	private class ViewHolder {
		TextView text;
		CheckBox check;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.prefs_check, null);

			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.prefsText);
			holder.check = (CheckBox) convertView.findViewById(R.id.prefsCheck);
			convertView.setTag(holder);
			holder.check.setClickable(false);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		UserFile file = (UserFile) fileList.get(position);
		holder.text.setText(file.getText());
		holder.check.setChecked(file.getEnabled());
		return convertView;

	}

}
