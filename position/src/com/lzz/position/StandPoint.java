package com.lzz.position;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StandPoint extends Fragment {
	
	private LocationManager locationManager;
    private TextView textMessage;
    private Button getLocation,saveLocation,exit;
    private EditText note;
    private Button picture;
    private ImageView imageView;
    private Activity activity;
    private DBHelper dbHelper;
    private boolean GPS_status = false;
    private double x,y;
    private String resultX,resultY;
    private float accuracy;
	private Uri outPutFileUri;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.layout_standpoint, container, false);
		activity = getActivity();
		dbHelper = new DBHelper(activity, "positionBackup.db3", 1);
	    locationManager = (LocationManager) activity.getSystemService(android.content.Context.LOCATION_SERVICE);
	    textMessage = (TextView) rootView.findViewById(R.id.textMessage);
	    imageView = (ImageView) rootView.findViewById(R.id.imageView1);
	    note = (EditText) rootView.findViewById(R.id.noteET);
	    picture = (Button) rootView.findViewById(R.id.pictureBT);
//	    note.setVisibility(View.INVISIBLE);
//	    picture.setVisibility(View.INVISIBLE);
//	    imageView.setVisibility(View.INVISIBLE);
	    exit = (Button) rootView.findViewById(R.id.exit);
	    exit.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	activity.finish();
	        }
	    });
        GPS_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GPS_status){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            updateView(location);
        }
        else{
            textMessage.setText("GPS未准备好");
//            note.setVisibility(View.INVISIBLE);
//            picture.setVisibility(View.INVISIBLE);
//            imageView.setVisibility(View.INVISIBLE);
        }
        
        //获取站立点的按键响应
	    getLocation = (Button) rootView.findViewById(R.id.get_location);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPS_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(GPS_status) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    updateView(location);
                }
                else {
                    textMessage.setText("GPS未准备好");
//                    note.setVisibility(View.INVISIBLE);
//                    picture.setVisibility(View.INVISIBLE);
//                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
        //保存当前的站立点信息
	    saveLocation = (Button) rootView.findViewById(R.id.save_location);
	    saveLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(GPS_status){
					String noteString = null;
					noteString = note.getText().toString();
					
					if(outPutFileUri!=null)	{
						insertData(dbHelper.getWritableDatabase(), resultX, resultY, accuracy, noteString, outPutFileUri.getPath());
						outPutFileUri = null;
					}
					else {
						insertData(dbHelper.getWritableDatabase(), resultX, resultY, accuracy, noteString, null);
					}
				}
				else {
					Toast.makeText(activity, "无数据", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	    //获取照片
	    picture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file;
				if (outPutFileUri != null) {
					file = new File(outPutFileUri.getPath());
					file.delete();
				}

				activity.getExternalFilesDir("position");
				file = new File(Environment.getExternalStorageDirectory(), "/position");
				file.mkdir();
				Calendar rightNow = Calendar.getInstance();
				Time formatTime = new Time();
				formatTime.set(rightNow.getTime().getTime());
				file = new File(file,formatTime.format2445() + ".jpg");
				Log.d("filePath", file.getPath());
				outPutFileUri = Uri.fromFile(file);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutFileUri);
				startActivityForResult(intent, 0);
			}
		});

        return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0) {
			if(data != null) {
				if(data.hasExtra("data")) {
					Bitmap thumb = data.getParcelableExtra("data");
					imageView.setImageBitmap(thumb);
				}
			}
			else {
				DisplayMetrics dm = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
				BitmapFactory.Options bitOptions = new BitmapFactory.Options();
				bitOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(outPutFileUri.getPath(),bitOptions);
				int imageWidth = bitOptions.outWidth;
				int imageHeight = bitOptions.outHeight;
				int scaleFactor = Math.min(imageHeight/dm.heightPixels*2, imageWidth/dm.widthPixels*2);
				bitOptions.inJustDecodeBounds = false;
				bitOptions.inSampleSize = scaleFactor;
				bitOptions.inPurgeable = true;
				
				Bitmap thumb = BitmapFactory.decodeFile(outPutFileUri.getPath(),bitOptions);
				imageView.setImageBitmap(thumb);
				
			}
		}
	}
	
	private void insertData(SQLiteDatabase db, String positionx, String positiony, float accuracy, String note, String path){
		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
		db.execSQL("insert into position_info values(null,?,?,?,?,?,?)",
		new String[] {positionx, positiony, Float.toString(accuracy), df.format(new Date()), note, path});
		Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
	}
	
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        	updateView(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void updateView(Location newLocation){
        if(newLocation != null){
        	int part;
        	double fi = newLocation.getLatitude()*Math.PI/180;
        	double lmd = newLocation.getLongitude();
        	part = (int)lmd / 6;
        	lmd = lmd - part * 6 -3;
        	lmd = lmd*Math.PI/180;
        	part++;
        	double a = 6378245;
        	double b = 6356863;
        	double e = (a*a - b*b) / (a*a);//e的平方
        	double e2 = Math.sqrt((a*a - b*b) / (b*b));//e'
        	double yita = e2*Math.cos(fi);
        	double n = a / Math.sqrt(1 - e*Math.pow(Math.sin(fi),2));
        	double sm = 6367558.496 * fi - 16036.48 * Math.sin(2*fi) 
        			+ 16.828 * Math.sin(4*fi) - 0.022 * Math.sin(6*fi) + 0.00003 * Math.sin(8*fi);
        	x = sm + n * Math.sin(fi) * Math.cos(fi) * Math.pow(lmd,2) / 2
        			+ n / 24 * Math.pow(lmd, 4) * Math.sin(fi) * Math.pow(Math.cos(fi), 3) 
        				* (5 - Math.pow(Math.tan(fi),2) + 9 * Math.pow(yita,2) + 4 * Math.pow(yita, 4))
        			+ n / 270 * Math.pow(lmd, 6) * Math.sin(fi) * Math.pow(Math.cos(fi), 5) 
        				* (61 - 58 * Math.pow(Math.tan(fi),2) + Math.pow(Math.tan(fi), 4) + 270 * Math.pow(yita, 4) - 330 * yita * yita * Math.exp(Math.tan(fi)));
        	y = n * lmd * Math.cos(fi) 
        			+ n / 6 * Math.pow(lmd, 3) * Math.pow(Math.cos(fi), 3) * (1 - Math.pow(Math.tan(fi),2) + yita * yita)
        			+ n / 120 * Math.pow(lmd, 5) * Math.pow(Math.cos(fi), 5) 
        				* (5 - 18 * Math.pow(Math.tan(fi),2) + Math.pow(Math.tan(fi), 4) + 14 * yita * yita - 58 *yita*yita* Math.exp(Math.tan(fi)));
            int tempY = (int)y + 500000;
            StringBuilder sb = new StringBuilder();
            String xString = Integer.toString((int)x);
            String yString = Integer.toString((int)tempY);
            resultX = xString.substring(0, xString.length()-5) + "-" 
            		+ xString.substring(xString.length()-5, xString.length()-3) + "-" 
            		+ xString.substring(xString.length()-3, xString.length());
            resultY = Integer.toString(part) + "-" 
            		+ yString.substring(0, yString.length()-5) + "-" 
            		+ yString.substring(yString.length()-5, yString.length()-3) + "-" 
            		+ yString.substring(yString.length()-3, yString.length());
            sb.append("位置信息:\n");
            sb.append("(");
            sb.append(resultX);
            sb.append(",");
            sb.append(resultY);
            sb.append(")\n");
            sb.append("精度:");
            sb.append(accuracy = newLocation.getAccuracy());
            sb.append("米");
            textMessage.setText(sb.toString());
            note.setVisibility(View.VISIBLE);
            picture.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
        	textMessage.setText("正在搜索GPS卫星");
//        	note.setVisibility(View.INVISIBLE);
//        	picture.setVisibility(View.INVISIBLE);
//        	imageView.setVisibility(View.INVISIBLE);
        }

    }
}
	

	
