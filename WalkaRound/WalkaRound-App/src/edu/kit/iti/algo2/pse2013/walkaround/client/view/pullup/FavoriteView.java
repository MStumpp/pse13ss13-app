package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

public class FavoriteView extends Fragment {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";
	
	private int switcher = R.id.pullupFavoriteSwitcher;
	
	private TabHost tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT,"Create FavoriteView");
		
		Log.d(TAG_PULLUP_CONTENT, "Create Tabs");
		tabHost = (TabHost) this.getActivity().findViewById(R.id.tabhost_favorites);
		tabHost.setup();
		
		TabSpec spec1 = tabHost.newTabSpec("roundtrip_favorites_tab");
		spec1.setContent(R.id.roundtrip_favorites);
		spec1.setIndicator("Roundtrips");
		TabSpec spec2 = tabHost.newTabSpec("route_favorites_tab");
		spec2.setContent(R.id.route_favorites);
		spec2.setIndicator("Routes");
		TabSpec spec3 = tabHost.newTabSpec("poi_favorites_tab");
		spec3.setContent(R.id.poi_favorites);
		spec3.setIndicator("POIs");
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		
		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}
	

	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT,"Destroy FavoriteView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}
	
	public boolean equals(Fragment f){
		if(f.toString().equals(PullUpView.CONTENT_FAVORITE)){
			return true;
		}
		return false;
	}
}
