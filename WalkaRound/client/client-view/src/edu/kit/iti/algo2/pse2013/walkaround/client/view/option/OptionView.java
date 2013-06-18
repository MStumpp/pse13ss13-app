package edu.kit.iti.algo2.pse2013.walkaround.client.view.option;

//  Importiere die Resourcen
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

//Nötige Imports
import android.os.Bundle;
import android.app.Activity;

//Fragment Import
import android.app.Fragment;
import android.app.FragmentTransaction;

//Preferences Import
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

// Log Ausgabe
import android.util.Log;

/**
 * Ein Controller, der sich um die Anzeige und Verarbeitung des Optionen Menüs kümmert.
 * 
 * Diese Klasse nutzt folgende xml Files:
 * #layout.options_view - Das Grundlayout des Optionen Menüs.
 * 
 * Die hier gespeicherten und veränderten Informationen kann man mithilfe dieses Befehls abrufen:
 * 
 * import android.preference.PreferenceManager;
 * SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 * 
 * @author Ludwig Biermann
 * 
 */
public class OptionView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.options_view);

		Fragment f = new OptionFragment();
		FragmentTransaction t = getFragmentManager().beginTransaction();

		t.replace(R.id.options_view_id, f);
		t.addToBackStack(null);

		t.commit();
	}

	/**
	 * Ein Fragment der grapischen Oberfläche, dass das sich um die Anzeige des
	 * Menüs kümmert.
	 * 
	 * Dieses Fragment beöntigt folgende xml Files:
	 * #xml.optionen - Das xml File aus dem automatisch die Grapische Oberfäche entsteht. 
	 * #string.options_array - Sprachdatei für Listen
	 * #string.options - Sprachdatei
	 * 
	 * Info: Unter values-de werden die deutschsprachigen Dateien gespeichert.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	public static class OptionFragment extends PreferenceFragment {
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Log.d("Options",
					"Optionen.xml wird als default Preference Manager ausgeführt.");
			
			PreferenceManager.setDefaultValues(getActivity(), R.xml.options,
					false);
			Log.d("Options", "Optionen werden gestartet.");
			addPreferencesFromResource(R.xml.options);
		}
	}
}