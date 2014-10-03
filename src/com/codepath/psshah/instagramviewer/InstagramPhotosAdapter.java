package com.codepath.psshah.instagramviewer;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    private static class ViewHolder {
		// Lookup fields in this view for population
		TextView tvCaption;
		ImageView ivPhoto;
		TextView tvLikes;
		TextView tvUserInfo;
		CircularImageView civUserProfile;
    }

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item at this position
		InstagramPhoto photo = getItem(position);
		ViewHolder viewHolder;
		
		// Check if this is a recycled view, if not, create/inflate it
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
			viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
			viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
			viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
			viewHolder.tvUserInfo = (TextView) convertView.findViewById(R.id.tvUserInfo);
			viewHolder.civUserProfile = (CircularImageView)convertView.findViewById(R.id.civUserProfile);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// Populate data into the view's fields
		viewHolder.tvCaption.setText(Html.fromHtml("<font color=\"#206199\"><b>" + photo.username
                + "  " + "</b></font>" + "<font color=\"#000000\">" + photo.caption + "</font>"));
		viewHolder.tvLikes.setText(photo.likesCount + " likes");
		viewHolder.tvUserInfo.setText(Html.fromHtml("<font color=\"#206199\"><b>" + photo.username + " </b></font>"));
		
		// blank out image, to guard against old images from recycled views
		viewHolder.ivPhoto.setImageResource(0);
		// XXX: why just set height?
		viewHolder.ivPhoto.getLayoutParams().height = photo.height;

		// Ask image to be loaded into imageView based on photo url i.e.
		// 	Async, send network request, download bytes, convert to bitmap, insert in imageview.		
		Picasso.with(getContext()).load(photo.url).into(viewHolder.ivPhoto);

		// Load user profile image in circular view
		//civUserProfile.setBorderColor(Color.GRAY);
		//civUserProfile.setBorderWidth(10);
		viewHolder.civUserProfile.setImageResource(0);
		if(!photo.userProfileUrl.isEmpty()) {
			Picasso.with(getContext()).load(photo.userProfileUrl).placeholder(R.drawable.ic_launcher).into(viewHolder.civUserProfile);
		}
		
		// Return completed view to render
		return convertView;
	}
	
}
