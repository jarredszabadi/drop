package com.example.dropchatv2;


import java.io.ByteArrayOutputStream;
import java.io.File;
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
	private static final int REQUEST_CODE = 1;
	private static int TAKE_PICTURE = 1;
	private static final int CAMERA_REQUEST = 1888;

	private Bitmap bitmap;
	private ImageView imageView;
	private static Marker[] dropPoints = new Marker[10];
	Button dropb;
	Button captureb;
	File file;
	File root;
	Uri fileuri;
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
			
			System.out.println("hiiiii");
					 
			messageapi mat = new messageapi(message, lat, lon, "http://172.17.150.149:3000/drops");
			messageapi.setMethod(1);
			mat.execute();
		}
		if(v.getId() == R.id.capture){
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
			fileuri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
			startActivityForResult(intent, TAKE_PICTURE);
			
			/*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			System.out.println("Where to store: "+dir);
            File output = new File(dir, "camerascript.png");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(output));
            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            */
            
           
		}

	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
            /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            //you can create a new file name "test.jpg" in sdcard folder.
            File f = new File(Environment.getExternalStorageDirectory()
                                    + File.separator + "test.jpg");
            try {
				f.createNewFile();
				//write the bytes in file
	            FileOutputStream fo = new FileOutputStream(f);
	            fo.write(bytes.toByteArray());

	            // remember close de FileOutput
	            fo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		if (requestCode == TAKE_PICTURE){
			System.out.println(fileuri.toString());
		}
           
        //}  

	}


}



  

