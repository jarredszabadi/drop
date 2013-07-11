package drop.to.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class messageapi extends AsyncTask<String, Integer, String>{
	private String message;
	private double latitude;
	private double longitude;
	private String requestURL;
	static int metho;
	public static String markerm;


	public messageapi(String message, double latitude, double longitude, String requestURL){
		this.message = message;
		this.latitude = latitude;
		this.longitude = longitude;
		this.requestURL = requestURL;
		markerm = new String();

	}
	
	protected String doInBackground(String... params) {
		String returnString = new String();
			switch(metho){
			case 1:
			
		
			JSONObject obj = new JSONObject();
			JSONObject drop = new JSONObject();
			
			String droppoint = new String(Double.toString(latitude)+","+Double.toString(longitude));
			try {
				drop.put("message", message);
				//drop.put("latitude", latitude);
				//drop.put("longitude", latitude);
				drop.put("droppoint", droppoint);
				obj.put("drop", drop);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Sending: " + obj.toString() + "to "+ requestURL);
			returnString = sendReceiveJSON.createDrop(requestURL, obj);
			System.out.println("Message from " + requestURL + ": "+ returnString + "\n");
			
			
			break;
			case 2:
				System.out.println("Getting: " +requestURL);
				markerm = sendReceiveJSON.getSingleDrop(requestURL);
				System.out.println("Message from " + requestURL + ": "+ markerm + "\n");
				
				
			break;

			/*case 3:
				System.out.println("Receiving from: "+requestURL);
				returnString = sendReceiveJSON.getAllDrops(requestURL);
				System.out.println("Message from " + requestURL + ": "+ returnString + "\n");
				
			break;*/
			}
			return returnString;

	}
	public static void setMethod(int method){
		metho = method;
	}


	

}
