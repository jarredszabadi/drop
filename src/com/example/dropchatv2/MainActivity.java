package com.example.dropchatv2;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.IOException;


import drop.to.server.messageapi;
import android.provider.MediaStore.Files.FileColumns;


import com.example.dropchatv2.R;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import android.location.LocationListener;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;




public class MainActivity extends FragmentActivity implements LocationListener, OnMapClickListener, OnClickListener{
	private static GoogleMap map;
	private LocationManager locationManager;
	private String provider;
	double lat;
	double lon;
	Location loc;
	String message;
	Marker dropPoint;



	private static Marker[] dropPoints = new Marker[10];
	Button dropb;
	Button captureb;
	Button openb;
	File file;
	File root;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		setMap(fm.getMap());

	    dropb = (Button) findViewById(R.id.drop);
	    dropb.setOnClickListener(this);
		
	    captureb = (Button) findViewById(R.id.capture);
	    captureb.setOnClickListener(this);
		
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

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.drop){
			EditText location = (EditText) findViewById(R.id.header);
			message = location.getText().toString();
			
			
					 
			messageapi mat = new messageapi(message, lat, lon, "http://fierce-bayou-3100.herokuapp.com/drops");
			messageapi.setMethod(1);
			mat.execute();
		}
		if(v.getId() == R.id.capture){
			 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
			    Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			    setImageURI(fileUri);
			    
			    /*notice how we don't include this line...if we do then data = null in onActivityResult*/
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
		
			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			
		}
		

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//System.out.println("TEST: "+data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	
	        	messageapi mp = new messageapi(fileUri, "http://fierce-bayou-3100.herokuapp.com/pictures");
	        	mp.setMethod(2);
	        	mp.execute();
	        	
	        	} else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	    
	}
	public void setImageURI(Uri fileuri){
		  this.fileUri=fileuri;
	  }

	  /** Create a file Uri for saving an image or video */
		private static Uri getOutputMediaFileUri(int type){
		      return Uri.fromFile(getOutputMediaFile(type));
		}

		/** Create a File for saving an image or video */
		private static File getOutputMediaFile(int type){
		    // To be safe, you should check that the SDCard is mounted
		    // using Environment.getExternalStorageState() before doing this.

		    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "MyCameraApp");
		    // This location works best if you want the created images to be shared
		    // between applications and persist after your app has been uninstalled.

		    // Create the storage directory if it does not exist
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("MyCameraApp", "failed to create directory");
		            return null;
		        }
		    }

		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    File mediaFile;
		    if (type == MEDIA_TYPE_IMAGE){
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "IMG_"+ timeStamp + ".jpg");
		    } else if(type == MEDIA_TYPE_VIDEO) {
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "VID_"+ timeStamp + ".mp4");
		    } else {
		        return null;
		    }

		    return mediaFile;
		}
		
	  

}



  

