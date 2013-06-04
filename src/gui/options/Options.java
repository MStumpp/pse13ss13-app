package gui.options;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Options extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        this.setContentView(R.layout.options_view);

        Fragment f = new OptionenFragment();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        
        t.replace(R.id.options_view_id, f);
        t.addToBackStack(null);
        
        t.commit();
        
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new OptionenFragment()).commit();
	}
	public static class OptionenFragment extends PreferenceFragment {
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Log.d("Options",
					"Optionen.xml wird als default Preference Manager ausgeführt.");
			PreferenceManager.setDefaultValues(getActivity(), R.xml.options, false);
			Log.d("Options", "Optionen werden gestartet.");
			addPreferencesFromResource(R.xml.options);
		}
	}

}
