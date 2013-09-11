package org.eyyam.browserish.ui;

import java.util.ArrayList;

import org.eyyam.browserish.R;
import org.eyyam.browserish.settings.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class GeneralListAdapter extends ArrayAdapter<Setting> {

	private ArrayList<Setting> settingList;

	public GeneralListAdapter(Context context, int textViewResourceId, ArrayList<Setting> settingList) {
		super(context, textViewResourceId, settingList);
		this.settingList = new ArrayList<Setting>();
		this.settingList.addAll(settingList);
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

		Setting setting = settingList.get(position);
		holder.text.setText(setting.getText());
		holder.check.setChecked(setting.getBoolean());
		return convertView;

	}

}
