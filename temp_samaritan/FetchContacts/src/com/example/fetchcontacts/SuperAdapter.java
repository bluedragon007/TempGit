package com.example.fetchcontacts;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class SuperAdapter extends BaseAdapter {
	ArrayList<Contact> data;
	Context context;
	private ImageLoader imageLoader;

	public SuperAdapter(Context context, ArrayList<Contact> data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		initImageLoader();
	}

	private void initImageLoader() {

		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.profilepic) // resource
																													// or
																													// drawable
				.showImageForEmptyUri(R.drawable.profilepic) // resource or
																// drawable
				.showImageOnFail(R.drawable.profilepic) // resource or drawable
				.resetViewBeforeLoading(true) // default
				// default

				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default

				.displayer(new SimpleBitmapDisplayer()) // default
				.handler(new Handler()) // default
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(
				options).build();
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = View.inflate(context, R.layout.itemmain, null);
		ImageView img = (ImageView) convertView.findViewById(R.id.imageView1);
		TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
		TextView txt_number = (TextView) convertView.findViewById(R.id.txt_number);
		TextView txt_loc = (TextView) convertView.findViewById(R.id.latlongs);
		txt_loc.setText(data.get(position).location);
		txt_name.setText(data.get(position).contact_name);
		txt_number.setText(data.get(position).contact_number);
		imageLoader.displayImage(data.get(position).contact_pic_uri, img);
		return convertView;
	}

}
