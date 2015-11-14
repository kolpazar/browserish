package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class PrefsCheck extends RelativeLayout {

	public PrefsCheck(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inf.inflate(R.layout.row_setting, this, true);	
	}

}
