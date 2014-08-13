package com.lzz.position;

import java.io.File;

import android.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class More extends Fragment {
    private Activity activity;
	private ImageView imageView;
	private TextView textView;
	private DBHelper dbHelper;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.layout_more, container, false);
		activity = getActivity();
		int mid = getArguments().getInt("id");
		File nFile = null;
		String sql = "select * from position_info where _id = " + String.valueOf(mid);
		imageView = (ImageView)rootView.findViewById(R.id.imageView1);
		textView = (TextView)rootView.findViewById(R.id.textView1);
		dbHelper = new DBHelper(activity, "positionBackup.db3", 1);
		Cursor cur = dbHelper.getReadableDatabase().rawQuery(sql, null);
		cur.moveToNext();
		StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(cur.getString(1));
        sb.append(",");
        sb.append(cur.getString(2));
        sb.append(")\n");
        sb.append("精度:");
        sb.append(cur.getString(3));
        sb.append("米    ");
        sb.append("时间:");
        sb.append(cur.getString(4));
        sb.append("\n");
        sb.append(cur.getString(5));
        textView.setText(sb);
        if(cur.getString(6)!=null){
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			BitmapFactory.Options bitOptions = new BitmapFactory.Options();
			bitOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(cur.getString(6),bitOptions);
			int imageWidth = bitOptions.outWidth;
			int imageHeight = bitOptions.outHeight;
			int scaleFactor = (int) Math.min(imageHeight/dm.heightPixels*1.3, imageWidth/dm.widthPixels*1.3);
			bitOptions.inJustDecodeBounds = false;
			bitOptions.inSampleSize = scaleFactor;
			bitOptions.inPurgeable = true;
        	Log.d("path", cur.getString(6));
    		nFile = new File(cur.getString(6));
			Bitmap thumb = BitmapFactory.decodeFile(nFile.getPath(),bitOptions);
			imageView.setImageBitmap(thumb);
		}
		return rootView;
	}
}
