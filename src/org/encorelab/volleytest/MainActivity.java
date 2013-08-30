package org.encorelab.volleytest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private RequestQueue mRequestQueue;
	private JSONObject jsonObj;
	private JSONObject jsonToPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(new JsonObjectRequest(Method.GET, "http://drowsy.badger.encorelab.org/rollcall/users/52110ef654a6e433a2000006", null, new MyResponseListener(), new MyErrorListener()));
        try {
        	jsonToPost = new JSONObject("{'username':'testy the clown', 'tags':[]}");
        } catch (JSONException e) {
        	Log.e("JSON object creation", e.getLocalizedMessage());
        }
        mRequestQueue.add(new JsonObjectRequest(Method.POST, "http://drowsy.badger.encorelab.org/rollcall/users/", jsonToPost, new MyPostResponseListener(), new MyErrorListener()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onStop() {
		if (mRequestQueue != null)
			mRequestQueue.cancelAll(this);
		super.onStop();
	}
    
    
	class MyResponseListener implements Listener<JSONObject> {

		@Override
		public void onResponse(JSONObject response) {
			MainActivity.this.jsonObj = response;
			try {
				String result = MainActivity.this.jsonObj.getString("username");
				Log.d("The result is: ", result);
//				String timestamp = result.getString("Timestamp");
//				MainActivity.this.resultTxt.setText(new Date(Long.parseLong(timestamp.trim())).toString());
			} catch (Exception e) {
				Log.e("ErrorListener", e.getLocalizedMessage());
			}
		}
	}
	
	class MyPostResponseListener implements Listener<JSONObject> {

		@Override
		public void onResponse(JSONObject response) {
			
			try {
				
				Log.d("The result is: ", response.toString());
//				String timestamp = result.getString("Timestamp");
//				MainActivity.this.resultTxt.setText(new Date(Long.parseLong(timestamp.trim())).toString());
			} catch (Exception e) {
				Log.e("ErrorListener", e.getLocalizedMessage());
			}
		}
	}

	class MyErrorListener implements ErrorListener {
	
		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e("ErrorListener", error.getCause() + "");
			if (error.getCause() != null) {
				Log.e("ErrorListener", error.getLocalizedMessage());
			}
//			MainActivity.this.resultTxt.setText(error.toString());
			mRequestQueue.cancelAll(this);
		}
	}
}
