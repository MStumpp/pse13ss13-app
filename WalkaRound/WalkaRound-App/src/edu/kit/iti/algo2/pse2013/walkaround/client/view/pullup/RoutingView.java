package edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RoutingView extends Fragment {

	public String TAG_PULLUP_CONTENT = "PULLUP_CONTENT";
	
	private int switcher = R.id.pullupRoutingSwitcher;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_PULLUP_CONTENT,"Create RoutingView");
		this.getActivity().findViewById(switcher).setVisibility(View.VISIBLE);
	}
	

	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG_PULLUP_CONTENT,"Destroy RoutingView");
		this.getActivity().findViewById(switcher).setVisibility(View.GONE);
	}
	
	public boolean equals(Fragment f){
		if(f.toString().equals(PullUpView.CONTENT_ROUTING)){
			return true;
		}
		return false;
	}
}