package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup.Routing.GoToMapListener;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * This class shows the Search View
 *
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class Search extends RelativeLayout {

	private static final String TAG = Search.class.getSimpleName();

	private LinearLayout poiSide;
	private LinearLayout addressSide;
	private LinearLayout main;
	private LinearLayout tabHost;
	private Button addressButton;
	private Button poiButton;
	private boolean selected = true;
	private EditText zipEdit;
	private EditText cityEdit;
	private EditText streetEdit;
	private EditText numberEdit;
	private EditText freeText;

	private LinearLayout result;
	private int width;
	private boolean isResult = false;

	private List<GoToMapListener> rl = new LinkedList<GoToMapListener>();
	private List<UpdateMapListener> ul = new LinkedList<UpdateMapListener>();

	/**
	 * This create a new POIview.
	 *
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public Search(Context context, AttributeSet attrs) {
		super(context, attrs);
		Point size = BoundingBox.getInstance(context).getDisplaySize();

		width = size.x;

		main = new LinearLayout(context, attrs);
		main.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(main, mainParams);

		tabHost = new LinearLayout(context, attrs);
		tabHost.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams tabHostParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		tabHostParams.width = size.x;
		tabHostParams.bottomMargin = 20;

		main.addView(tabHost, tabHostParams);

		addressButton = new Button(context, attrs);
		addressButton.setText(context.getString(R.string.addr_address));
		addressButton.setOnTouchListener(new AddressSideTabListener());

		poiButton = new Button(context, attrs);
		poiButton.setText(context.getString(R.string.term_poi));
		poiButton.setOnTouchListener(new POISideTabListener());

		LinearLayout.LayoutParams waypointButtontParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		waypointButtontParams.height = size.y / 10;
		waypointButtontParams.width = size.x / 2;
		waypointButtontParams.topMargin = 10;
		waypointButtontParams.leftMargin = 0;
		waypointButtontParams.rightMargin = 0;

		LinearLayout.LayoutParams routeButtontParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		routeButtontParams.height = size.y / 10;
		routeButtontParams.width = size.x / 2;
		routeButtontParams.topMargin = 10;
		routeButtontParams.leftMargin = 0;
		routeButtontParams.rightMargin = 0;

		tabHost.addView(addressButton, waypointButtontParams);
		tabHost.addView(poiButton, routeButtontParams);

		poiSide = new LinearLayout(context, attrs);
		poiSide.setOrientation(LinearLayout.VERTICAL);
		poiSide.setVisibility(GONE);

		addressSide = new LinearLayout(context, attrs);
		addressSide.setOrientation(LinearLayout.VERTICAL);
		addressSide.setVisibility(VISIBLE);
		addressButton.setTextColor(Color.RED);

		LinearLayout.LayoutParams routeSiedeParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		LinearLayout.LayoutParams waypointSiedeParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		main.addView(poiSide, routeSiedeParam);
		main.addView(addressSide, waypointSiedeParam);

		LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textParam.width = size.x / 2;
		textParam.height = size.y / 10;

		LinearLayout.LayoutParams editParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		editParam.width = size.x / 2;
		editParam.height = size.y / 10;

		// Address

		// Zip
		LinearLayout zipLayout = new LinearLayout(context);
		zipLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView zip = new TextView(context);
		zip.setText(context.getString(R.string.addr_zip_code));
		zip.setTextSize(30);
		zip.setGravity(Gravity.CENTER);

		zipEdit = new EditText(context);
		zipEdit.setGravity(Gravity.CENTER);
		zipEdit.setInputType(InputType.TYPE_CLASS_NUMBER);

		zipLayout.addView(zip, textParam);
		zipLayout.addView(zipEdit, editParam);

		addressSide.addView(zipLayout, layoutParam);

		// City
		LinearLayout cityLayout = new LinearLayout(context);
		cityLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView city = new TextView(context);
		city.setText(context.getString(R.string.addr_city));
		city.setTextSize(30);
		city.setGravity(Gravity.CENTER);

		cityEdit = new EditText(context);
		cityEdit.setGravity(Gravity.CENTER);
		cityEdit.setInputType(InputType.TYPE_CLASS_TEXT);

		cityLayout.addView(city, textParam);
		cityLayout.addView(cityEdit, editParam);

		addressSide.addView(cityLayout, layoutParam);

		// Street
		LinearLayout streetLayout = new LinearLayout(context);
		streetLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView street = new TextView(context);
		street.setText(context.getString(R.string.addr_street));
		street.setTextSize(30);
		street.setGravity(Gravity.CENTER);

		streetEdit = new EditText(context);
		streetEdit.setGravity(Gravity.CENTER);
		streetEdit.setInputType(InputType.TYPE_CLASS_TEXT);

		streetLayout.addView(street, textParam);
		streetLayout.addView(streetEdit, editParam);

		addressSide.addView(streetLayout, layoutParam);

		// Street
		LinearLayout numberLayout = new LinearLayout(context);
		numberLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView number = new TextView(context);
		number.setText(context.getString(R.string.addr_housenumber));
		number.setTextSize(30);
		number.setGravity(Gravity.CENTER);

		numberEdit = new EditText(context);
		numberEdit.setGravity(Gravity.CENTER);
		numberEdit.setInputType(InputType.TYPE_CLASS_TEXT);

		numberLayout.addView(number, textParam);
		numberLayout.addView(numberEdit, editParam);

		addressSide.addView(numberLayout, layoutParam);

		// Go

		LinearLayout.LayoutParams goParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		goParam.width = size.x 	* 2 / 3;
		goParam.height = size.y / 10;
		goParam.topMargin = 20;
		goParam.gravity = Gravity.CENTER;

		Button go = new Button(context, attrs);
		go.setGravity(Gravity.CENTER);
		go.setText(context.getString(R.string.search));

		go.setOnTouchListener(new GoAdressListener());

		addressSide.addView(go, goParam);

		// POI

		LinearLayout.LayoutParams freeParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		freeParam.height = size.y / 10;
		freeParam.topMargin = 10;
		freeParam.width = width;

		freeText = new EditText(context);
		freeText.setGravity(Gravity.CENTER);
		freeText.setInputType(InputType.TYPE_CLASS_TEXT);

		poiSide.addView(freeText, freeParam);

		// Go

		LinearLayout.LayoutParams goFreeParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		goFreeParam.width = size.x * 2 / 3;
		goFreeParam.height = size.y / 10;
		goFreeParam.topMargin = 10;
		goFreeParam.gravity = Gravity.CENTER;

		Button goFree = new Button(context, attrs);
		goFree.setGravity(Gravity.CENTER);
		goFree.setText(R.string.search);

		goFree.setOnTouchListener(new GoQueryListener());

		poiSide.addView(goFree, goFreeParam);

		// Result
		result = new LinearLayout(context, attrs);
		result.setOrientation(LinearLayout.VERTICAL);
		result.setVisibility(GONE);
		
		ScrollView suggestionsScroll = new ScrollView(context);
		suggestionsScroll.addView(result);

		main.addView(suggestionsScroll);

		//zipEdit.setText("76185");
		cityEdit.setText("Karlsruhe");
		//streetEdit.setText("Yorckstr.");
		//numberEdit.setText("48");
		freeText.setHint(context.getString(R.string.query));
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Catch all Touch events
		return true;
	}

	/**
	 * POI side Tab Listener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class POISideTabListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				setTab(true);
				isResult = false;
				toogleResult();
			}
			return false;
		}

	}

	/**
	 * Address side Tab Listener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class AddressSideTabListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				setTab(false);
				isResult = false;
				toogleResult();
			}
			return false;
		}

	}

	/**
	 * set the tab
	 *
	 * @param b
	 *            true if address Side
	 */
	private void setTab(boolean b) {
		selected = b;
		if (selected) {
			addressButton.setSelected(false);
			addressButton.setTextColor(Color.BLACK);
			poiButton.setSelected(true);
			poiButton.setTextColor(Color.RED);
			poiSide.setVisibility(VISIBLE);
			addressSide.setVisibility(GONE);
		} else {
			addressButton.setSelected(true);
			addressButton.setTextColor(Color.RED);
			poiButton.setSelected(false);
			poiButton.setTextColor(Color.BLACK);
			poiSide.setVisibility(GONE);
			addressSide.setVisibility(VISIBLE);
		}
	}

	/**
	 * toogle tabs
	 */
	private void toogleResult() {
		if (isResult) {
			poiSide.setVisibility(GONE);
			addressSide.setVisibility(GONE);
			result.setVisibility(View.VISIBLE);
		} else {
			result.setVisibility(View.GONE);
			result.removeAllViews();
		}
	}

	/**
	 * Go AddressListener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class GoAdressListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_UP) {
				List<Location> locations;
				Log.d(TAG, "Go wurde gedrückt");
				Log.d(TAG, "" + zipEdit.getText().toString());

				String zip = zipEdit.getText().toString().replaceAll(" ", null);
				String city = cityEdit.getText().toString()
						.replaceAll(" ", null);
				String street = streetEdit.getText().toString()
						.replaceAll(" ", null);
				String number = numberEdit.getText().toString()
						.replaceAll(" ", null);

				if (zip.isEmpty()) {
					Address addr = new Address(street, number, city, 0);
					locations = POIManager.getInstance(getContext())
							.searchPOIsByAddress(getContext(), addr);
				} else {
					Address addr = new Address(street, number, city,
							(Integer.parseInt(zip)));
					locations = POIManager.getInstance(getContext())
							.searchPOIsByAddress(getContext(), addr);
				}

				if (!locations.isEmpty()) {

					// Resultat anzeigen
					isResult = true;
					toogleResult();
					
					TextView title = new TextView(getContext());
					title.setText(R.string.title_suggestions);
					title.setTextSize(30);
					title.setGravity(Gravity.CENTER);
					
					result.addView(title);

					for (Location value : locations) {
						Button location = new Button(getContext());
						Log.d("routingView: ", " " + value.getName() + " "
								+ value.getId());
						location.setText(value.getAddress().toString());
						location.setOnTouchListener(new locationTouch(value,
								location));
						location.setTextSize(20);
						LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						//myParams.topMargin = 5;
						myParams.width = width;
						result.addView(location, myParams);
					}
				} else {
					alertResult(zip + " " + city + " " + street + " " + number);
				}
			}
			return false;
		}
	}

	/**
	 * Go Query Listener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class GoQueryListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "Eine query wurde eingegeben");

			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				String text = freeText.getText().toString();
				if (!text.isEmpty()) {

					List<POI> poiS = POIManager.getInstance(getContext())
							.searchPOIsByQuery(text);

					if (!poiS.isEmpty()) {

						isResult = true;
						toogleResult();
						
						TextView title = new TextView(getContext());
						title.setText(R.string.title_suggestions);
						title.setTextSize(30);
						title.setGravity(Gravity.CENTER);
						
						result.addView(title);

						for (POI value : poiS) {
							Button poi = new Button(getContext());
							Log.d("routingView: ", " " + value.getName() + " "
									+ value.getId());
							poi.setText(value.getName());
							poi.setOnTouchListener(new poiTouch(value, poi));
							poi.setTextSize(20);
							LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);
							//myParams.topMargin = 5;
							myParams.width = width;
							result.addView(poi, myParams);
						}
					} else {
						alertResult(text);
					}
				}
			}
			return false;
		}

	}

	/**
	 * makes result alert
	 *
	 * @param text the showing text
	 */
	public void alertResult(String text) {
		AlertDialog alertDialog = new AlertDialog.Builder(getContext())
				.create();
		alertDialog.setTitle("Sorry..");
		alertDialog.setMessage("Es wurden keine mit Ihrer Suchanfrage: \n \n"
				+ text + "\n \n übereinstimmenden Orte gefunden!");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				});
		alertDialog.show();
	}

	/**
	 * POI Touch calls
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class poiTouch implements OnTouchListener {

		private POI poi;
		private View view;

		/**
		 * construct a new poi Touch
		 * @param p the poi
		 * @param view the view
		 */
		public poiTouch(POI p, View view) {
			this.poi = p;
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_UP) {
				RouteController.getInstance().addWaypoint(new Waypoint(poi.getLatitude(), poi.getLongitude(), poi.getName()));
				BoundingBox.getInstance(getContext()).setCenter(poi);
				notifyGoToMapListener();
			}
			return false;
		}

	}

	/**
	 * Location Touch
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class locationTouch implements OnTouchListener {

		private Location location;
		private View view;

		/**
		 * construct a new location touch
		 * @param p the location
		 * @param view the view
		 */
		public locationTouch(Location p, View view) {
			this.location = p;
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(view) && event.getAction() == MotionEvent.ACTION_UP) {
				RouteController.getInstance().addWaypoint(new Waypoint(location.getLatitude(), location.getLongitude(), location.getName()));
				BoundingBox.getInstance(getContext()).setCenter(location);
				notifyGoToMapListener();
			}
			return false;
		}
	}

	/**
	 * notifiy go to map listener
	 */
	private void notifyGoToMapListener() {
		for (GoToMapListener l : rl) {
			l.onGoToMap();
			notifyUpdateMapListener();
		}
	}

	/**
	 * register a go to map listener
	 * @param listener the new listener
	 */
	public void registerGoToMapListener(GoToMapListener listener) {
		rl.add(listener);
	}


	/**
	 * natify all listener
	 */
	private void notifyUpdateMapListener() {
		for (UpdateMapListener l : ul) {
			l.updateMap();
		}
	}

	/**
	 * register a update map listener
	 * @param listener the new listener
	 */
	public void registerUpdateMapListener(UpdateMapListener listener) {
		ul.add(listener);
	}

	/**
	 * update Map Listener
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface UpdateMapListener {
		/**
		 * calls if map has to be updatet
		 */
		public void updateMap();
	}

}