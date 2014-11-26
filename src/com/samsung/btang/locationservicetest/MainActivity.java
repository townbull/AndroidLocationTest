package com.samsung.btang.locationservicetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
/*import android.location.LocationListener;*/
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.samsung.btang.locationservicetest.R;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements 
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, 
LocationListener {
	  private TextView latitude;
	  private TextView longitude;
	  private TextView choice;
	  private CheckBox fineAcc;
	  private Button choose;
	  private TextView provText;
	  private LocationManager locationManager;
	  private String provider;
	  private MyLocationListener mylistener;
	  private Criteria criteria;
	  
	// locations objects
      private LocationClient mLocationClient;
      private Location mCurrentLocation;
      private LocationRequest mLocationRequest;
      
      private TextView txtLat, txtLong, txtAddr;
	  
	/** Called when the activity is first created. */

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_main);
		  latitude = (TextView) findViewById(R.id.lat);
		  longitude = (TextView) findViewById(R.id.lon);
		  provText = (TextView) findViewById(R.id.prov);
//		  choice = (TextView) findViewById(R.id.choice);
//		  fineAcc = (CheckBox) findViewById(R.id.fineAccuracy);
//		  choose = (Button) findViewById(R.id.chooseRadio);

		  // Get the location manager
		  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		  // Define the criteria how to select the location provider
		  criteria = new Criteria();
		  criteria.setAccuracy(Criteria.ACCURACY_COARSE);	//default
//		  
//		  // user defines the criteria
//		  choose.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				 if(fineAcc.isChecked()){
//					  criteria.setAccuracy(Criteria.ACCURACY_FINE);
//					  choice.setText("fine accuracy selected");
//					  Toast.makeText(MainActivity.this, "Fine accuracy selected: " + Criteria.ACCURACY_FINE,
//				                Toast.LENGTH_SHORT).show();
//				 }else {
//					 criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//					 choice.setText("coarse accuracy selected");
//					 Toast.makeText(MainActivity.this, "Coarse accuracy selected: " + Criteria.ACCURACY_COARSE,
//                             Toast.LENGTH_SHORT).show();
//				 }  
//			}
//		  });
		  criteria.setCostAllowed(false); 
		  // get the best provider depending on the criteria
		  provider = locationManager.getBestProvider(criteria, false);
	    
		  // the last known location of this provider
		  Location location = locationManager.getLastKnownLocation(provider);

		  mylistener = new MyLocationListener();
	
//		  if (location != null) {
//			  mylistener.onLocationChanged(location);
//		  } else {
//			  // leads to the settings because there is no last known location
//			  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			  startActivity(intent);
//		  }
		  // location updates: at least 1 meter and 200millsecs change
		  locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
		  
	 
		  
		  /*================ Begin Google Location =======================*/
		    // 2. get reference to TextView
		    txtLat = (TextView) findViewById(R.id.txtLat);
	        txtLong = (TextView) findViewById(R.id.txtLong);
	        
	        txtAddr = (TextView) findViewById(R.id.address);
	 
	        // 3. create LocationClient
	        mLocationClient = new LocationClient(this, this, this);
	 
	        // 4. create & set LocationRequest for Location update
	        mLocationRequest = LocationRequest.create();
	        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	        // Set the update interval to 5 seconds
	        mLocationRequest.setInterval(1000 * 5);
	        // Set the fastest update interval to 1 second
	        mLocationRequest.setFastestInterval(200);
		  
		  
	  }

	     @Override
	        protected void onStart() {
	            super.onStart();
	            // 1. connect the client.
	            mLocationClient.connect();
	        }
	     @Override
	        protected void onStop() {
	            super.onStop();
	            // 1. disconnecting the client invalidates it.
	            mLocationClient.disconnect();
	        }
	      
	 
	    // GooglePlayServicesClient.OnConnectionFailedListener
	    @Override
	    public void onConnectionFailed(ConnectionResult connectionResult) {
	        Toast.makeText(this, "gms: Connection Failed", Toast.LENGTH_SHORT).show();
	    }
	 
	    // GooglePlayServicesClient.ConnectionCallbacks 
	    @Override
	    public void onConnected(Bundle arg0) {
	         
	        if(mLocationClient != null)
	            mLocationClient.requestLocationUpdates(mLocationRequest,  this);
	 
	        Toast.makeText(this, "gms: Connected", Toast.LENGTH_SHORT).show();
	         
	        if(mLocationClient != null){
	            // get location
	            mCurrentLocation = mLocationClient.getLastLocation();
	                try{
	                     
	                    // set TextView(s) 
	                    txtLat.setText("Lattitude: " + mCurrentLocation.getLatitude()+"");
	                    txtLong.setText("Longitude: " + mCurrentLocation.getLongitude()+"");
	                     
	                }catch(NullPointerException npe){
	                     
	                    Toast.makeText(this, "gms: Failed to Connect", Toast.LENGTH_SHORT).show();
	 
	                    // switch on location service intent
	                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                    startActivity(intent);
	                }
	        }
	 
	    }
	    @Override
	    public void onDisconnected() {
	        Toast.makeText(this, "gms: Disconnected.", Toast.LENGTH_SHORT).show();
	     
	    }
	 
	    // LocationListener
	    @Override	    
	    public void onLocationChanged(Location location) {
	        // change location value
	        Toast.makeText(this, "gms: Location changed =P", Toast.LENGTH_SHORT).show();
//	        mCurrentLocation = mLocationClient.getLastLocation();
//	        txtLat.setText("Lattitude: " + mCurrentLocation.getLatitude()+"");
//	        txtLong.setText("Longitude: " + mCurrentLocation.getLongitude()+"");
	        
	        txtLat.setText("Lattitude: " + location.getLatitude()+"");
            txtLong.setText("Longitude: " + location.getLongitude()+"");
	    }
	    
	    /*================ End Google Location =======================*/

	    
	    
	  private class MyLocationListener implements android.location.LocationListener {
	
		  @Override
		  public void onLocationChanged(Location location) {
			// Initialize the location fields
			  latitude.setText("Latitude: "+String.valueOf(location.getLatitude()));
			  longitude.setText("Longitude: "+String.valueOf(location.getLongitude()));
			  provText.setText("Provider: " + provider);
			  
			  Toast.makeText(MainActivity.this,  "Location changed!",
				        Toast.LENGTH_SHORT).show();
		  }
	
		  @Override
		  public void onStatusChanged(String provider, int status, Bundle extras) {
			  Toast.makeText(MainActivity.this, provider + "'s status changed to "+status +"!",
				        Toast.LENGTH_SHORT).show();
		  }
	
		  @Override
		  public void onProviderEnabled(String provider) {
			  Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
		        Toast.LENGTH_SHORT).show();
		  }
	
		  @Override
		  public void onProviderDisabled(String provider) {
			  Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
		        Toast.LENGTH_SHORT).show();
		  }
	  }
}
