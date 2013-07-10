package com.example.dropchatv2;


import java.util.List;

import drop.to.server.messageapi;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.Overlay;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

import android.location.LocationListener;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;




public class MainActivity extends FragmentActivity implements LocationListener, OnMapClickListener{
	private static GoogleMap map;
	private LocationManager locationManager;
	private String provider;
	double lat;
	double lon;
	Location loc;
	String message;
	Marker dropPoint;
	private static Marker[] dropPoints = new Marker[10];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		setMap(fm.getMap());
		
	    Button dropb = (Button) findViewById(R.id.drop);
		Button open = (Button) findViewById(R.id.open);
		
		
		getMap().setMyLocationEnabled(true);
		getMap().setOnMapClickListener(this);
		
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    loc = locationManager.getLastKnownLocation(provider);
		
	    if(loc!=null){
            onLocationChanged(loc);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        
        
        
	    
	    dropb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText location = (EditText) findViewById(R.id.header);
				message = location.getText().toString();
				
						 
				messageapi mat = new messageapi(message, lat, lon, "http://192.168.1.101:3000/drops");
				messageapi.setMethod(1);
				mat.execute();

				//location.setText(lat+","+lon);

			}

		});
	    
	    /*open.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
						 
				messageapi mat = new messageapi(message, lat, lon, "http://192.168.1.101:3000/drops/5");
				messageapi.setMethod(2);
				mat.execute();
				//System.out.println(drop.to.server.messageapi.markerm);
				//dropPoint = getMap().addMarker(new MarkerOptions().position(point).title("Drop here")
		        	//	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				

				

			}

		});*/
	    
		 
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* Request updates at startup */
	  @Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }

	  /* Remove the locationlistener updates when Activity is paused */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	  }

	  @Override
	  public void onLocationChanged(Location location) {
	    lat = (double) (location.getLatitude());
	    lon = (double) (location.getLongitude());
	    loc = location;
	    
	    
	    
	    LatLng latLng = new LatLng(lat, lon);
	    
        // Showing the current location in Google Map
        getMap().moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        getMap().animateCamera(CameraUpdateFactory.zoomTo(15));

        
	    
	  }




	  @Override
	  public void onProviderDisabled(String provider) {
	    Toast.makeText(this, "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	  }

	@Override
	public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();
	}

	@Override
	 public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	 }

	@Override
	public void onMapClick(LatLng point) {
		//map.addMarker(new MarkerOptions().position(point).title("Drop here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		if (dropPoint != null) {
            dropPoint.remove();
        }
        dropPoint = getMap().addMarker(new MarkerOptions().position(point).title("Drop here")
        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        lat = point.latitude;
        lon = point.longitude;
    	}

	public static Marker[] getDropPoints() {
		return dropPoints;
	}

	public static void setDropPoints(Marker[] dropPoints) {
		MainActivity.dropPoints = dropPoints;
	}

	public static GoogleMap getMap() {
		return map;
	}

	public static void setMap(GoogleMap map) {
		MainActivity.map = map;
	}



  
}
