package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

import android.content.Context;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.NaviModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;

public class NaviStarter extends SingleStarter {

	private Context appContext;

	public NaviStarter(Context context) {
		this.appContext = context.getApplicationContext();
	}

	@Override
	public void run() {
		// Favorite- and POI-initialization
		FavoriteManager.initialize(appContext);
		POIManager.initialize(appContext);
		makeStep(0);

		// Navi
		NaviModel.initialize(appContext);
		PreferenceUtility.getInstance().registerOnSharedPreferenceChangeListener(NaviModel.getInstance());
		makeStep(1);

		finish();
	}

	@Override
	public void finish() {
		listener.onStarterFinished();
	}

	@Override
	public int[] getSteps() {
		return new int[]{
			50,	//Init favs and POIs
			100	//Init navi
		};
	}

}
