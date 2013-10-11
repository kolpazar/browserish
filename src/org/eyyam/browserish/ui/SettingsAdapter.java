package org.eyyam.browserish.ui;

import java.util.ArrayList;

import org.eyyam.browserish.R;
import org.eyyam.browserish.config.base.Pref;
import org.eyyam.browserish.config.setting.Setting;
import org.eyyam.browserish.config.setting.SettingHeader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsAdapter extends ArrayAdapter<Pref> {

	private ArrayList<Pref> settingList;

	public SettingsAdapter(Context context, int textViewResourceId, ArrayList<Pref> settingList) {
		super(context, textViewResourceId, settingList);
		this.settingList = new ArrayList<Pref>();
		this.settingList.addAll(settingList);
	}

	private class ViewHolder {
		TextView text;
		CheckBox check;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return !(settingList.get(position) instanceof SettingHeader);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Pref pref = settingList.get(position);
		if (((pref instanceof Setting) && !(convertView instanceof RelativeLayout)) ||
				((pref instanceof SettingHeader) && !(convertView instanceof TextView))) {
			convertView = null;
		}
		ViewHolder holder = null;
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			if (pref instanceof Setting) {
				convertView = vi.inflate(R.layout.row_setting, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.settingText);
				holder.check = (CheckBox) convertView.findViewById(R.id.settingCheck);
				convertView.setTag(holder);
				holder.check.setClickable(false);
			} else {
				convertView = vi.inflate(R.layout.row_header, null);
				//convertView.setClickable(false);
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (pref instanceof Setting) {
			holder.text.setText(pref.getText());
			holder.check.setChecked(((Setting) pref).getBoolean());
		} else {
			((TextView) convertView).setText(pref.getText()); 
		}
		return convertView;

	}

}
