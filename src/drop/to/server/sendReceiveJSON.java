package drop.to.server;

import com.example.dropchatv2.MainActivity;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class sendReceiveJSON {
	
	
	public static void postImage(){
	    RequestParams params = new RequestParams();
	    params.put("picture[name]","MyPictureName");
	    try {
			params.put("picture[photo]", new File(Environment.getExternalStorageDirectory().getPath() + "/test.jpg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    AsyncHttpClient client = new AsyncHttpClient();
	    client.post("http://192.168.1.101:3000/pictures", params, new AsyncHttpResponseHandler() {
	        @Override
	        public void onSuccess(String response) {
	            Log.w("async", "success!!!!");
	        }                                                                                                                                                                     
	    }); 
	} 
	
/*
	Bitmap imagex;
	public static String sendImage(Bitmap b, String requestURL){
		Bitmap immagex=BitmapFactory.decodeFile("mnt/sdcard/test.jpg");
		System.out.println("i am here");
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    
	    immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	    byte[] b1 = baos.toByteArray();
	    
	    String imageEncoded = Base64.encodeToString(b1,Base64.DEFAULT);
	    
	    JSONObject obj = new JSONObject();
	    JSONObject photo = new JSONObject();
	    try {
			photo.put("photo", imageEncoded);
			obj.put("picture", photo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    
	    
	    
	    
		URL url;
		OutputStream os;
		String response = null;
		try {
			url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);

			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(b1.length));
            conn.setRequestProperty("Authorization", "e554a3335cbcbda16891976c6fb43c5e");
            conn.setRequestProperty("User_Authorization", "e6eb6b7133900826101b1c471fcacfd5");

			os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(obj.toString());
			osw.flush();

			response = getCreateResponse(conn);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return response;
	    
	    
	}*/
/*
	public static String sendMessage(String requestURL){
		URL url;
		
		try {
			url = new URL("http://localhost:8080/handler");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setDoInput(true);
		    con.setDoOutput(true);
		    con.setUseCaches(false);
		    con.setRequestProperty("Content-Type", "image/jpeg");
		    con.setRequestMethod("POST");
		    InputStream in = new FileInputStream("c:/temp/poc/img/mytest2.jpg");
		    OutputStream out = con.getOutputStream();
		    copy(in, con.getOutputStream());
				out.flush();
			
		    out.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
		return requestURL;
	}
	
	protected static long copy(InputStream input, OutputStream output){
	    
		byte[] buffer = new byte[12288]; // 12K
	    long count = 0L;
	    int n = 0;
	    
	    try {
	    while (-1 != (n = input.read(buffer))){ 
	        
				output.write(buffer, 0, n);
		 
	        count += n;}
	    }
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return count;
	}*/


	public static String createDrop(String requestURL, JSONObject obj) {
		URL url;
		OutputStream os;
		String response = null;
		try {
			url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);

			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", "e554a3335cbcbda16891976c6fb43c5e");
            conn.setRequestProperty("User_Authorization", "5c4a6360d5891742521a88a4f5cc81ae");

			os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(obj.toString());
			osw.flush();

			response = getCreateResponse(conn);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public static String getCreateResponse(HttpURLConnection c) {
		StringBuilder res = new StringBuilder();
		String output;
		String response = null;
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader((c.getInputStream())));
			while ((output = br.readLine()) != null) {
				res.append(output);
			}

			response = res.toString();
			c.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	
	public static String getSingleDrop(String website) {
		URL url;
		String response = null;
		JSONObject r = new JSONObject();
		try {
			url = new URL(website);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");


			r = getShowResponse(conn);
			response = r.toString();
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONObject getShowResponse(HttpURLConnection c) {
		BufferedReader br = null;
		JSONObject returnObj = new JSONObject();
		try {
			br = new BufferedReader(new InputStreamReader((c.getInputStream())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder res = new StringBuilder();
		String output;
		try {
			while ((output = br.readLine()) != null) {
				res.append(output);
				// System.out.println(output);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultString = res.toString();
		//System.out.println("\n\n"+resultString);
		String[] point = resultString.split(",");
		LatLng pointt = new LatLng(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
		com.example.dropchatv2.MainActivity.getMap().addMarker(new MarkerOptions().position(pointt).title("Drop here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		
		
		// resultString = resultString.substring(1,resultString.length()-1); //
		// remove wrapping "[" and "]"
		c.disconnect();
		try {
			returnObj = new JSONObject(resultString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnObj;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String getAllDrops(String requestURL){
		URL url;
		String response = null;
		try {
			url = new URL(requestURL);
			HttpURLConnection c = (HttpURLConnection) url.openConnection();

			c.setRequestMethod("GET");
			c.setRequestProperty("Content-Type", "application/json");
			c.setRequestProperty("Accept", "application/json");
			c.setRequestProperty("Authorization", "c247233c33aef5cde84c973c474c18c8");
			response = streamAllEvents(c);
			
			
				
			
			
			
			
			
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static String streamAllEvents(HttpURLConnection c){
		String output;
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		JSONObject data; 
		String split[];
		String point[];
		
		
		try {
			br = new BufferedReader(new InputStreamReader(
					(c.getInputStream())));
			//System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			br.close();
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			data = ((JSONObject) new JSONObject(sb.toString())).getJSONObject("data");
			System.out.println("******\n"+data.toString());

			JSONArray events = (JSONArray) data.get("Drops");
			split = events.toString().split(",");
			//JSONObject event;
			
			for(int i =0; i<split.length-1; i++){
				split[i].replace("[", "");
				split[i].replace("\"", "");
				split[i+1].replace("[", "");
				split[i+1].replace("\"", "");
				
				LatLng position = new LatLng(Double.parseDouble(split[i]), Double.parseDouble(split[i+1]));
				
				System.out.println("*********");
				System.out.println(position.toString());
				com.example.dropchatv2.MainActivity.getDropPoints()[i] = com.example.dropchatv2.MainActivity.getMap().addMarker(new MarkerOptions().position(position).title("Drop here")
		        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				
			}
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return sb.toString();
	}


	






}
