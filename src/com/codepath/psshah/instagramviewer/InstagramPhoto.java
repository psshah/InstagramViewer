package com.codepath.psshah.instagramviewer;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class InstagramPhoto {
	public String username;
	public String caption;
	public String url;
	public int height;
	public int likesCount;
	public String userProfileUrl;

	public void deserialize(JSONObject photoJson) {
		try {
			if (photoJson.has("user")
					&& photoJson.getJSONObject("user") != null) {
				this.username = photoJson.getJSONObject("user").getString(
						"username");
				this.userProfileUrl = photoJson.getJSONObject("user")
						.getString("profile_picture");
			} else {
				this.username = "anonymous";
			}
			if (photoJson.has("caption") && !photoJson.isNull("caption")) {
				if (photoJson.getJSONObject("caption") != null) {
					this.caption = photoJson.getJSONObject("caption")
							.getString("text");
				} else {
					Log.i("INFO", "missing caption");
				}
			}
			if (photoJson.has("images")
					&& photoJson.getJSONObject("images") != null) {
				if (photoJson.getJSONObject("images").getJSONObject(
						"standard_resolution") != null) {
					this.url = photoJson.getJSONObject("images")
							.getJSONObject("standard_resolution")
							.getString("url");
					this.height = photoJson.getJSONObject("images")
							.getJSONObject("standard_resolution")
							.getInt("height");
				}
			}
			if (photoJson.has("likes")
					&& photoJson.getJSONObject("likes") != null) {
				this.likesCount = photoJson.getJSONObject("likes").getInt(
						"count");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
