package drop.to.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class messageapi extends AsyncTask<String, Integer, String>{
	private String message;
	private double latitude;
	private double longitude;
	private String requestURL;


	public messageapi(String message, double latitude, double longitude, String requestURL){
		this.message = message;
		this.latitude = latitude;
		this.longitude = longitude;
		this.requestURL = requestURL;

	}
	
	protected String doInBackground(String... params) {
		
		
			JSONObject obj = new JSONObject();
			JSONObject drop = new JSONObject();
			String returnString = new String();
			String droppoint = new String(Double.toString(latitude)+","+Double.toString(longitude));
			try {
				drop.put("message", message);
				drop.put("droppoint", droppoint);
				obj.put("drop", drop);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Sending: " + obj.toString() + "to "+ requestURL);
			returnString = sendReceiveJSON.createDrop(requestURL, obj);
			System.out.println("Message from " + requestURL + ": "+ returnString + "\n");
			return returnString;


	}


	

}
