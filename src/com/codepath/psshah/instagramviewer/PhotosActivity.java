package com.codepath.psshah.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PhotosActivity extends Activity {
	private static final String CLIENT_ID = "2095ae2e12324605aaa0779590174716";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aphotos;
	private PullToRefreshListView lvPhotos;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        
        fetchPopularPhotos();

        // setup pull-to-refresh listener
        lvPhotos = (PullToRefreshListView) findViewById(R.id.lvPhotos);
        lvPhotos.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
                // Call listView.onRefreshComplete() once the network request has completed successfully.
                fetchPopularPhotos();
                lvPhotos.onRefreshComplete();
			}
		});
	}

	private void fetchPopularPhotos() {
		photos = new ArrayList<InstagramPhoto>();

		// Create adapter; connect it to listview, so that it udpates view as data changes
        aphotos = new InstagramPhotosAdapter(this, photos);
        lvPhotos = (PullToRefreshListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aphotos);

		// Set up popular photo endpoint
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create network client and initiate the network request
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(int statusCode, Header[] headers,
        			JSONObject response) {
                JSONArray photosJson = null;
        		try {
        			photos.clear();
					photosJson = response.getJSONArray("data");
					// Iterate over all elements, extract necessary information
					// and add them to photos array

					for(int i=0; i<photosJson.length(); i++) {
						JSONObject photoJson = photosJson.getJSONObject(i);
						//Log.i("DEBUG", "this json=" + photoJson.toString());
						InstagramPhoto photo = new InstagramPhoto();
						
						if(photoJson.getJSONObject("user") != null) {
							photo.username = photoJson.getJSONObject("user").getString("username");
						}
						else {
							photo.username = "anonymous";
						}
						if(photoJson.has("caption") && !photoJson.isNull("caption")) {
							if(photoJson.getJSONObject("caption") != null) {
								photo.caption = photoJson.getJSONObject("caption").getString("text");
							}
							else {
								Log.i("INFO", "missing caption");
							}
						}
						if(photoJson.getJSONObject("images") != null) {							
							if(photoJson.getJSONObject("images").getJSONObject("standard_resolution") != null) {
								photo.url = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
								photo.height = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
							}
						}
						if(photoJson.getJSONObject("likes") != null) {
							photo.likesCount = photoJson.getJSONObject("likes").getInt("count");
						}
						if(photo != null) {
							photos.add(photo);
						}
					}
					aphotos.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        	
        	@Override
        	public void onFailure(int statusCode, Header[] headers,
        			Throwable throwable, JSONObject errorResponse) {
        		super.onFailure(statusCode, headers, throwable, errorResponse);
        	}
        });
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
