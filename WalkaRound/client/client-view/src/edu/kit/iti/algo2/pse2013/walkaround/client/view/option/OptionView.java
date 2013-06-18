package edu.kit.iti.algo2.pse2013.walkaround.client.view.option;

//  Importiere die Resourcen
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

//N�tige Imports
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
 * Ein Controller, der sich um die Anzeige und Verarbeitung des Optionen Men�s k�mmert.
 * 
 * Diese Klasse nutzt folgende xml Files:
 * #layout.options_view - Das Grundlayout des Optionen Men�s.
 * 
 * Die hier gespeicherten und ver�nderten Informationen kann man mithilfe dieses Befehls abrufen:
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
	 * Ein Fragment der grapischen Oberfl�che, dass das sich um die Anzeige des
	 * Men�s k�mmert.
	 * 
	 * Dieses Fragment be�ntigt folgende xml Files:
	 * #xml.optionen - Das xml File aus dem automatisch die Grapische Oberf�che entsteht. 
	 * #string.options_array - Sprachdatei f�r Listen
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
					"Optionen.xml wird als default Preference Manager ausgef�hrt.");
			
			PreferenceManager.setDefaultValues(getActivity(), R.xml.options,
					false);
			Log.d("Options", "Optionen werden gestartet.");
			addPreferencesFromResource(R.xml.options);
		}
	}
}