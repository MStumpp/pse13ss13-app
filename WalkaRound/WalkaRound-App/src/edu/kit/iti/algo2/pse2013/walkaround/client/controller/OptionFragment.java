package edu.kit.iti.algo2.pse2013.walkaround.client.controller;

// Android Library
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;

// Walkaround Library

/**
 * Ein Controller, der sich um die Anzeige und Verarbeitung des Optionen Menüs
 * kümmert.
 * 
 * 
 * Dieses Fragment beöntigt folgende xml Files: #xml.optionen - Das xml File aus
 * dem automatisch die Grapische Oberfäche entsteht. #string.options_array -
 * Sprachdatei für Listen #string.options - Sprachdatei #layout.options_view
 * Info: Unter values-de werden die deutschsprachigen Dateien gespeichert.
 * 
 * Die hier gespeicherten und veränderten Informationen kann man mithilfe dieses
 * Befehls abrufen:
 * 
 * import android.preference.PreferenceManager; SharedPreferences sharedPrefs =
 * PreferenceManager.getDefaultSharedPreferences(this);
 * 
 * @author Ludwig Biermann
 * @version 1.0
 * 
 */
public class OptionFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";
	private static boolean on = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!on) {
			Log.d(TAG_PULLUP_CONTENT, "Create FavoriteView");
			on = false;
			Log.d("Options",
					"Optionen.xml wird als default Preference Manager ausgef�hrt.");

			PreferenceManager.setDefaultValues(getActivity(), R.xml.options,
					false);
			PreferenceUtility.getInstance()
					.registerOnSharedPreferenceChangeListener(this);
			Log.d("Options", "Optionen werden gestartet.");
			addPreferencesFromResource(R.xml.options);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT, "Destroy OptionView");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {

	}

}