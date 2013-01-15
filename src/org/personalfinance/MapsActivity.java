package org.personalfinance;

import java.util.List;

import org.personalfinance.database.TransactionDAO;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;

@SuppressLint({ "UseValueOf", "UseValueOf" }) public class MapsActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        MapView view = (MapView) findViewById(R.id.themap);
        view.setBuiltInZoomControls(true);
        
        final MapController control = view.getController();
        
		control.setCenter(new GeoPoint(new Double(-35*1E6).intValue(), new Double(-57.55*1E6).intValue()));

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        LocationListener listener = new LocationListener () {

			public void onLocationChanged(Location location) {
				/*
				double Long = location.getLongitude();
				double Lat = location.getLatitude();
				GeoPoint geo = new GeoPoint((int) (Long*1E6), (int) (Lat*1E6));
				control.setCenter(geo);
				*/
			}

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
        	
        };
        
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        List<Overlay> mapOverlays = view.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        Markers itemizedoverlay = new Markers(drawable, this);
        
        TransactionDAO DAO = new TransactionDAO(getApplicationContext());
		DAO.open();
		
	
		List<Transaction> transactions = DAO.getAllTransactions(true);
		for(Transaction trans: transactions) {
			Double Long = trans.getLongitud()*1e6;
			Double Lat = trans.getLatitud()*1e6;
			GeoPoint point = new GeoPoint(Long.intValue(), Lat.intValue());
			OverlayItem overlayitem = new OverlayItem(point, "", trans.getNota());
	        itemizedoverlay.addOverlay(overlayitem);
	        mapOverlays.add(itemizedoverlay);
		}

		DAO.close();
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_maps, menu);
        return false;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}