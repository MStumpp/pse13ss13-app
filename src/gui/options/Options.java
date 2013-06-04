package gui.options;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Options extends PreferenceActivity  {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}
}
