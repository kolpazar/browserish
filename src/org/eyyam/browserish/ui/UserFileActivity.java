package org.eyyam.browserish.ui;

import org.eyyam.browserish.R;
import org.eyyam.browserish.BrowserishCore;
import org.eyyam.browserish.common.Constants;
import org.eyyam.browserish.config.file.UserFile;
import org.eyyam.browserish.config.file.UserFileGroup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UserFileActivity extends Activity {

	public static final String EXTRA_GROUPID = "GROUPID";
	public static final String EXTRA_FILE = "FILE";
	
	private UserFile file;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userfile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		UserFileGroup userFileGroup = BrowserishCore.getInstance().getUserFileGroup(intent.getStringExtra(EXTRA_GROUPID));
		file = userFileGroup.getFile(intent.getStringExtra(EXTRA_FILE));
		getActionBar().setTitle(file.getText());
		if (file.getGroupId().equals(Constants.GROUPID_SCRIPT)) {
			((TextView) findViewById(R.id.textFileCaptionName)).setText(R.string.caption_script_name);
		} else if (file.getGroupId().equals(Constants.GROUPID_STYLE)) {
			((TextView) findViewById(R.id.textFileCaptionName)).setText(R.string.caption_style_name);
		}
		((TextView) findViewById(R.id.textFileName)).setText(file.getText());
		((TextView) findViewById(R.id.textFileVersion)).setText(R.string.version_na);
		((TextView) findViewById(R.id.textFileFilename)).setText(file.getName());
		((TextView) findViewById(R.id.textFileAffected)).setText(file.rulesAsText());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.userfile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
