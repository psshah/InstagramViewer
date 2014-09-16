package com.codepath.psshah.instagramviewer;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item at this position
		InstagramPhoto photo = getItem(position);
		
		// Check if this is a recycled view, if not, create/inflate it
		if(convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		// Lookup fields in this view for population
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
		TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		TextView tvUserInfo = (TextView) convertView.findViewById(R.id.tvUserInfo);
		//CircularImageView civUserProfile = (CircularImageView)convertView.findViewById(R.id.civUserProfile);
		ImageView civUserProfile = (ImageView)convertView.findViewById(R.id.civUserProfile);

		// Populate data into the view's fields
		tvCaption.setText(Html.fromHtml("<font color=\"#206199\"><b>" + photo.username
                + "  " + "</b></font>" + "<font color=\"#000000\">" + photo.caption + "</font>"));
		tvLikes.setText(photo.height + " likes");
		tvUserInfo.setText(Html.fromHtml("<font color=\"#206199\"><b>" + photo.username + " </b></font>"));
		
		// blank out image, to guard against old images from recycled views
		ivPhoto.setImageResource(0);
		// XXX: why just set height?
		ivPhoto.getLayoutParams().height = photo.height;

		// Ask image to be loaded into imageView based on photo url i.e.
		// 	Async, send network request, download bytes, convert to bitmap, insert in imageview.		
		Picasso.with(getContext()).load(photo.url).into(ivPhoto);

		// Load user profile image in circular view
		//civUserProfile.setBorderColor(Color.GRAY);
		//civUserProfile.setBorderWidth(10);
		civUserProfile.setImageResource(0);
		if(!photo.userProfileUrl.isEmpty()) {
			Picasso.with(getContext()).load(photo.userProfileUrl).into(civUserProfile);
		}
		
		// Return completed view to render
		return convertView;
	}
	
}
