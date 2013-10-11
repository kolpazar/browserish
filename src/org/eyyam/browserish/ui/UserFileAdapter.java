package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.config.base.Pref;
import org.eyyam.browserish.config.file.UserFile;
import org.eyyam.browserish.config.file.UserFileGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class UserFileAdapter extends ArrayAdapter<Pref> {

	private UserFileGroup files;
	private int itemBackgroundId;

	public UserFileAdapter(Context context, int textViewResourceId, UserFileGroup files) {
		super(context, textViewResourceId);
		this.files = files;
		addAll(files.getArrayList());
		
		TypedArray a = getContext().obtainStyledAttributes(new int[] { android.R.attr.selectableItemBackground });
		itemBackgroundId = a.getResourceId(0, 0);
		a.recycle();
	}

	private class ViewHolder {
		UserFile file;
		TextView text;
		TextView subText;
		ToggleButton toggle;
		LinearLayout layoutFile;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_userfile, null);

			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.fileText);
			holder.subText = (TextView) convertView.findViewById(R.id.fileSubText);
			holder.toggle = (ToggleButton) convertView.findViewById(R.id.fileToggle);
			holder.layoutFile = (LinearLayout) convertView.findViewById(R.id.layoutFile);
			holder.layoutFile.setBackgroundResource(itemBackgroundId);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		UserFile file = (UserFile) getItem(position);
		holder.file = file;
		holder.text.setText(file.getText());
		holder.subText.setText(file.getSubText());
		holder.toggle.setChecked(file.isEnabled());
		holder.layoutFile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder holder = (ViewHolder) ((View) v.getParent()).getTag();
				Toast.makeText(getContext(), holder.file.getName(), Toast.LENGTH_SHORT).show();
			}
		});
		holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ViewHolder holder = (ViewHolder) ((View) buttonView.getParent()).getTag();
				holder.file.setEnabled(isChecked);
				Toast.makeText(getContext(), "checked: " + isChecked, Toast.LENGTH_SHORT).show();
				files.save();
			}
		});
		return convertView;

	}

}
