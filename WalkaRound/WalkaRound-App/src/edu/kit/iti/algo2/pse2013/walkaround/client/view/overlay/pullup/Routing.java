package edu.kit.iti.algo2.pse2013.walkaround.client.view.overlay.pullup;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity.BootActivity;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * This class shows the Routing Menu
 *
 * @author Ludwig Biermann
 * @version 1.1
 *
 */
public class Routing extends RelativeLayout implements RouteListener {

	private static final String TAG = Routing.class.getSimpleName();
	private Button reset;
	private ImageButton invert;
	private Button addFavorite;
	private Button goToMap;
	private ImageButton save;
	private ScrollView scrollView;
	private LinearLayout content;
	private LinearLayout.LayoutParams textParams;
	private LinearLayout.LayoutParams saveParams;
	private LinearLayout.LayoutParams deleteParams;
	private RelativeLayout.LayoutParams lParams;
	private Context context;

	private LinkedList<GoToFavoriteListener> fav = new LinkedList<GoToFavoriteListener>();
	private LinkedList<GoToMapListener> rl = new LinkedList<GoToMapListener>();
	private android.widget.LinearLayout.LayoutParams moveParams;

	/**
	 * This create a new POIview.
	 *
	 * @param context
	 *            the context of the app
	 * @param attrs
	 *            the needed attributes
	 */
	public Routing(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		this.getRootView().setBackgroundColor(Color.RED);

		Point size = BoundingBox.getInstance(context).getDisplaySize();

		// allocat
		reset = new Button(context, attrs);
		invert = new ImageButton(context, attrs);
		save = new ImageButton(context, attrs);

		scrollView = new ScrollView(context, attrs);
		content = new LinearLayout(context, attrs);
		content.setOrientation(LinearLayout.VERTICAL);

		addFavorite = new Button(context, attrs);
		goToMap = new Button(context, attrs);

		// images and text

		invert.setImageResource(R.drawable.invert);
		invert.setScaleType(ImageView.ScaleType.FIT_CENTER);

		save.setImageResource(R.drawable.favorite);
		save.setScaleType(ImageView.ScaleType.FIT_CENTER);

		reset.setText(context.getString(R.string.reset));
		addFavorite.setText(context.getString(R.string.dialog_header_add,
				context.getString(R.string.term_favorite)));
		goToMap.setText(context.getString(R.string.go_to_map));

		// IDs

		reset.setId(1);
		invert.setId(2);
		addFavorite.setId(3);

		// Params
		// Reset
		RelativeLayout.LayoutParams paramsReset = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsReset.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsReset.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		paramsReset.topMargin = 10;
		paramsReset.bottomMargin = 20;
		paramsReset.height = size.y / 10;
		paramsReset.width = size.x / 3;

		// Invert
		RelativeLayout.LayoutParams paramsInvert = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsInvert.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		paramsInvert.addRule(RelativeLayout.RIGHT_OF, 1);
		paramsInvert.topMargin = 10;
		paramsInvert.bottomMargin = 20;
		paramsInvert.height = size.y / 10;
		paramsInvert.width = size.x / 3;

		// Save
		RelativeLayout.LayoutParams paramsSave = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsSave
				.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		paramsSave.addRule(RelativeLayout.RIGHT_OF, 2);
		paramsSave.topMargin = 10;
		paramsSave.bottomMargin = 20;
		paramsSave.height = size.y / 10;
		paramsSave.width = size.x / 3;

		// ScrollView
		RelativeLayout.LayoutParams paramsScrollView = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		paramsScrollView.addRule(RelativeLayout.BELOW, 1);
		paramsScrollView.addRule(RelativeLayout.ABOVE, 3);
		paramsScrollView.height = 1000;

		// Content
		RelativeLayout.LayoutParams paramsContent = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		// addFavorite
		RelativeLayout.LayoutParams paramsAddFavorite = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsAddFavorite.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		paramsAddFavorite.bottomMargin = 10;
		paramsAddFavorite.topMargin = 20;
		paramsAddFavorite.height = size.y / 10;
		paramsAddFavorite.width = size.x / 2;

		// goToMap
		RelativeLayout.LayoutParams paramsGoToMap = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsGoToMap.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		paramsGoToMap.addRule(RelativeLayout.RIGHT_OF, 3);
		paramsGoToMap.bottomMargin = 10;
		paramsGoToMap.topMargin = 20;
		paramsGoToMap.height = size.y / 10;
		paramsGoToMap.width = size.x / 2;

		// build View

		this.addView(reset, paramsReset);
		this.addView(invert, paramsInvert);
		this.addView(save, paramsSave);

		this.addView(scrollView, paramsScrollView);
		scrollView.getRootView().setBackgroundColor(Color.RED);
		scrollView.addView(content, paramsContent);

		this.addView(addFavorite, paramsAddFavorite);
		this.addView(goToMap, paramsGoToMap);

		// Params for Content

		// Move
		moveParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		moveParams.width = size.y / 10;
		moveParams.height = (size.x * 1 / 6) / 2;
		moveParams.bottomMargin = 0;
		moveParams.topMargin = 0;

		// Delete
		deleteParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		deleteParams.height = size.y / 10;
		deleteParams.width = size.x * 1 / 5;
		// TODO tauschen
		
		// Save
		saveParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		saveParams.height = size.y / 10;
		saveParams.width = size.x * 1 / 5;
		//TODO tauschen
		
		// Text
		textParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		textParams.height = size.y / 10;
		textParams.width = size.x * 3 / 5;
		//TODO tauschem
		
		// vertical Layout
		lParams = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		// Listener

		reset.setOnTouchListener(new ResetListener());
		invert.setOnTouchListener(new InvertListener());
		save.setOnTouchListener(new SaveListener());
		addFavorite.setOnTouchListener(new AddFavoriteListener());
		goToMap.setOnTouchListener(new GoToMapTouchListener());

		this.updateRoute();
		RouteController.getInstance().registerRouteListener(this);
	}

	/**
	 * update Route
	 */
	public void updateRoute() {
		BootActivity.getLastStarted().runOnUiThread(new RouteUpdater());
		Log.d(TAG, "Update Routing-View");
	}

	private class RouteUpdater implements Runnable {
		@Override
		public void run() {
			LinkedList<Waypoint> route = RouteController.getInstance()
					.getCurrentRoute().getWaypoints();
			content.removeAllViews();
			for (Waypoint w : route) {
				LinearLayout m = new LinearLayout(context);
				ImageButton moveUp = new ImageButton(context);
				ImageButton moveDown = new ImageButton(context);
				m.setPadding(0, 0, 0, 0);

				LinearLayout l = new LinearLayout(context);
				ImageButton delete = new ImageButton(context);
				ImageButton save = new ImageButton(context);
				Button text = new Button(context);

				moveUp.setImageResource(R.drawable.move_up);
				moveUp.setScaleType(ScaleType.FIT_XY);
				moveDown.setImageResource(R.drawable.move_down);
				moveDown.setRotation(180);
				moveDown.setScaleType(ScaleType.FIT_XY);

				delete.setImageResource(R.drawable.delete);
				save.setImageResource(R.drawable.favorite);
				text.setText(w.getName());
				Log.d(TAG,
						String.format("Setze Name von WP %d auf '%s'",
								w.getId(), w.getName()));

				delete.setTag(w.getId());
				save.setTag(w.getId());
				text.setTag(w.getId());
				moveUp.setTag(w.getId());
				moveDown.setTag(w.getId());
				
				/** PATCH **/
				//TODO 
				moveUp.setVisibility(View.GONE);
				moveDown.setVisibility(View.GONE);
				
				/** PATCH **/

				m.setOrientation(LinearLayout.VERTICAL);
				m.addView(moveUp, moveParams);
				m.addView(moveDown, moveParams);

				l.setOrientation(LinearLayout.HORIZONTAL);
				l.addView(m);
				l.addView(text, textParams);
				l.addView(save, saveParams);
				l.addView(delete, deleteParams);

				content.addView(l, lParams);

				text.setOnTouchListener(new WaypointListener());
				save.setOnTouchListener(new SaveWaypointListener());
				delete.setOnTouchListener(new DeleteListener());
				moveUp.setOnTouchListener(new MoveUpListener());
				moveDown.setOnTouchListener(new MoveDownListener());
			}
		}
	}

	/**
	 * Listen to Waypoint events
	 *
	 * @author Ludwig Biermann
	 * @version 1.1
	 *
	 */
	private class WaypointListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {

			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				int id = Integer.parseInt(view.getTag().toString());
				BoundingBox.getInstance().setCenter(
						RouteController.getInstance().getCurrentRoute()
								.getWaypoint(id));
				notifyGoToMapListener();
			}

			return false;
		}

	}

	/**
	 * Shifts the position one step down
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class MoveDownListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				int id = Integer.parseInt(view.getTag().toString());

				RouteController.getInstance().changeOrderOfWaypointsSHIFTbyX(id, 1);
			}

			return false;
		}
	}

	/**
	 * Shifts the position one step upd
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class MoveUpListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				int id = Integer.parseInt(view.getTag().toString());

				RouteController.getInstance().changeOrderOfWaypointsSHIFTbyX(id, -1);
			}

			return false;
		}
	}

	/**
	 * Listen to delete event
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class DeleteListener implements OnTouchListener {

		private int id;
		private String name;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				id = Integer.parseInt(view.getTag().toString());
				name = RouteController.getInstance().getWaypoint(id).getName();
				this.alert();
			}

			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle(context.getString(
					R.string.dialog_header_delete,
					context.getString(R.string.term_waypoint)));
			alertDialog.setMessage(context.getString(R.string.dialog_text_delete, name));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					context.getString(R.string.option_yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							RouteController.getInstance().deleteActiveWaypoint(id);
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					context.getString(R.string.option_no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// EMPTY
						}
					});
			alertDialog.show();
		}

	}

	/**
	 * Listen to a sace event
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class SaveWaypointListener implements OnTouchListener {

		private Waypoint w;
		private EditText edit;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				int id = Integer.parseInt(view.getTag().toString());
				w = RouteController.getInstance().getCurrentRoute()
						.getWaypoint(id);
				if (w != null) {
					this.alert();
				}
			}
			return false;
		}

		/**
		 * alert
		 */
		public void alert() {
			edit = new EditText(getContext());
			edit.setText(w.getName());
			edit.selectAll();
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle(context.getString(R.string.dialog_header_save,
					context.getString(R.string.term_waypoint)));
			alertDialog.setMessage("Wie heißt der Ort?");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					context.getString(R.string.option_add),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Location l = new Location(w.getLatitude(), w
									.getLongitude(), edit.getText().toString());

							if(!FavoriteManager.getInstance(getContext())
									.addLocationToFavorites(l,
											edit.getText().toString())) {
								AlertDialog alert = new AlertDialog.Builder(getContext()).create();
								alert.setTitle(context.getString(R.string.dialog_header_save,
										context.getString(R.string.term_waypoint)));
								alert.setMessage("Dieser Name exisitert bereits, Wegpunkt wurde nicht gespeichert!");
								alert.setButton(DialogInterface.BUTTON_POSITIVE,
								"OK",
								new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
								}
								});

								alert.show();
							}
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					context.getString(R.string.option_cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// EMPTY
						}
					});
			alertDialog.setView(edit);
			alertDialog.show();
		}

	}

	/**
	 * Listen to a reset
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class ResetListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP) {
				this.alert();
			}
			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle("Route löschen");
			alertDialog.setMessage("Route wirklich löschen");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ja",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							RouteController.getInstance().resetRoute();
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Nein!",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// EMPTY
						}
					});
			alertDialog.show();
		}

	}

	/**
	 * Listen to a invert
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class InvertListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (arg1.getAction() == MotionEvent.ACTION_UP) {
				RouteController.getInstance().invertRoute();
			}
			return false;
		}

	}

	/**
	 * Listen to a save event
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class SaveListener implements OnTouchListener {

		private EditText edit;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP) {
				this.alert();
			}
			return false;
		}

		/**
		 * makes a alert
		 */
		public void alert() {
			edit = new EditText(getContext());
			edit.setHint("Name der Route");
			edit.selectAll();
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.create();
			alertDialog.setTitle(context.getString(R.string.dialog_header_save,
					context.getString(R.string.term_route)));
			alertDialog.setMessage(context.getString(
					R.string.dialog_text_add_or_save,
					context.getString(R.string.term_route)));
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					context.getString(R.string.option_add),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(!FavoriteManager.getInstance(getContext())
									.addRouteToFavorites(
											RouteController.getInstance()
													.getCurrentRoute(),
											edit.getText().toString())) {
								AlertDialog alert = new AlertDialog.Builder(getContext()).create();
								alert.setTitle(context.getString(R.string.dialog_header_save,
										context.getString(R.string.term_route)));
								alert.setMessage("Dieser Name exisitert bereits, Route wurde nicht gespeichert!");
								alert.setButton(DialogInterface.BUTTON_POSITIVE,
								"OK",
								new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
								}
								});

								alert.show();
							}
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					context.getString(R.string.option_cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// EMPTY
						}
					});
			alertDialog.setView(edit);
			alertDialog.show();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Catch all Touch events
		return true;
	}

	/**
	 * Listen to a add favorite listener
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class AddFavoriteListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			notifyFavoriteListener();
			return false;
		}

	}

	/**
	 * Listen to a go to map
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	private class GoToMapTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			notifyGoToMapListener();
			return false;
		}

	}

	// GoToMap

	/**
	 * notify go to map
	 */
	private void notifyGoToMapListener() {
		for (GoToMapListener l : rl) {
			l.onGoToMap();
		}
	}

	/**
	 * register go to map listener
	 *
	 * @param listener
	 *            the new Listener
	 */
	public void registerGoToMapListener(GoToMapListener listener) {
		rl.add(listener);
	}

	/**
	 * A Interface for call go to map
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface GoToMapListener {

		/**
		 * is called if the view wants to go to map
		 */
		public void onGoToMap();
	}

	// Favorite

	/**
	 * notif all listener
	 */
	private void notifyFavoriteListener() {
		for (GoToFavoriteListener l : fav) {
			l.goToFavorite();
		}
	}

	/**
	 * register a new listener
	 *
	 * @param listener
	 *            the new listener
	 */
	public void registerFavoriteListener(GoToFavoriteListener listener) {
		fav.add(listener);
	}

	/**
	 *
	 * @author Ludwig Biermann
	 * @version 1.0
	 *
	 */
	public interface GoToFavoriteListener {
		public void goToFavorite();
	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		updateRoute();
	}

}
