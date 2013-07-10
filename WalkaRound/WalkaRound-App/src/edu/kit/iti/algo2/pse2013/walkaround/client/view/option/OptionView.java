package edu.kit.iti.algo2.pse2013.walkaround.client.view.option;

// Android Library
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

// Walkaround Library
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;

/**
 * Ein Controller, der sich um die Anzeige und Verarbeitung des Optionen Menüs
 * kümmert.
 *  
 * 
 * Dieses Fragment beöntigt folgende xml Files: #xml.optionen - Das xml File aus
 * dem automatisch die Grapische Oberfäche entsteht. 
 * #string.options_array - Sprachdatei für Listen
 * #string.options - Sprachdatei
 * #layout.options_view
 * Info: Unter values-de werden die deutschsprachigen Dateien gespeichert.
 * 
 * Die hier gespeicherten und veränderten Informationen kann man mithilfe dieses
 * Befehls abrufen:
 * 
 * import android.preference.PreferenceManager; SharedPreferences sharedPrefs =
 * PreferenceManager.getDefaultSharedPreferences(this);
 * 
 * @author Ludwig Biermann
 * 
 */
public class OptionView extends PreferenceFragment {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";

	private int switcher = R.id.pullupOptionSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG_PULLUP_CONTENT, "Create FavoriteView");

		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
		Log.d("Options",
				"Optionen.xml wird als default Preference Manager ausgef�hrt.");

		PreferenceManager.setDefaultValues(getActivity(), R.xml.options, false);
		Log.d("Options", "Optionen werden gestartet.");
		addPreferencesFromResource(R.xml.options);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy OptionView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public boolean equals(Fragment f) {
		if (f.toString().equals(PullUpView.CONTENT_FAVORITE)) {
			return true;
		}
		return false;
	}

}