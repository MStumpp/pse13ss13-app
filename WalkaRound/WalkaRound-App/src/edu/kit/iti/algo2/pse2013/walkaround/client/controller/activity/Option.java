package edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity;

import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.OptionFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class Option extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.options_view);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager
				.beginTransaction();
		Fragment op = new OptionFragment();
		ft.replace(R.id.options_view_id, op);
		ft.commit();
	}
	

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, WalkaRound.class);
		this.startActivity(intent);
	}
}
