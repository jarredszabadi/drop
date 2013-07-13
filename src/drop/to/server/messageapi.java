package drop.to.server;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class messageapi extends AsyncTask<String, Integer, String>{
	private String message;
	private double latitude;
	private double longitude;
	private String requestURL;
	static int metho;
	public static String markerm;
	public static Bitmap b;


	public messageapi(String message, double latitude, double longitude, String requestURL){
		this.message = message;
		this.latitude = latitude;
		this.longitude = longitude;
		this.requestURL = requestURL;
		markerm = new String();

	}
	
	public messageapi(Bitmap imageBitmap, String requestURL){
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
				drop.put("text", message);
				drop.put("lattitude", latitude);
				drop.put("longitude", longitude);
				obj.put("Drop", drop);
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

			case 3:
				System.out.println("Sending Image: "+requestURL);
				sendReceiveJSON.postImage();//returnString = sendReceiveJSON.sendImage(b, requestURL);
				System.out.println("Message from " + requestURL + ": "+ returnString + "\n");
				
			break;
			}
			return returnString;

	}
	public static void setMethod(int method){
		metho = method;
	}


	

}
