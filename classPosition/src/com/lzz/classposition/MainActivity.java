package com.lzz.classposition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {

	private MapView mMapView;
	private Spinner mSpinner;
	MyLocationData locData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_main);
		
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		mMapView = (MapView) findViewById(R.id.bmapView);
		BaiduMap mBaiduMap = mMapView.getMap();  
		mBaiduMap.setMyLocationEnabled(true);
		ShowMap showMap = new ShowMap(mBaiduMap,mSpinner);
		showMap.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	class ShowMap extends AsyncTask<String, Integer, String> {
		int[] mark = {R.drawable.icon_marka,
				R.drawable.icon_markb,
				R.drawable.icon_markc,
				R.drawable.icon_markd,
				R.drawable.icon_marke,
				R.drawable.icon_markf,
				R.drawable.icon_markg,
				R.drawable.icon_markh,
				R.drawable.icon_markj,
				R.drawable.icon_markj};
		int markNum = 0;
		private BaiduMap mBaiduMap;
		private Spinner	mSpinner;
		private ArrayList<String> allClass;
		private List<LatLng> file = new ArrayList<LatLng>();
		public ShowMap(BaiduMap mBaiduMap,Spinner mSpinner) {
			super();
			this.mSpinner = mSpinner;
			this.mBaiduMap = mBaiduMap;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
//			FTP f = new FTP(true);
//			List<String[]> ftpResult=null;
//			String[] lastStrings={"","",""};
//			try {//地址，端口号，用户名，密码
//				if(f.login("bngsn.bob.buttobi.net",21, "bngsn","911002")){
//					ftpResult = f.ListFile("/pos/"); 
//				}
//				f.disConnection();  
//			} catch (Exception e) {
//				// TODO: handle exception
//				Log.d("ffttpp",e.toString());
//			}
//			lastStrings = ftpResult.get(0);
//			for(int i = 0;i < ftpResult.size();i++) {
//				String[] nowStrings = ftpResult.get(i);
//				if(!nowStrings[0].equals(lastStrings[0])) {
//					//
//					String path = "http://bngsn.bob.buttobi.net/pos/"+lastStrings[0]+"."+lastStrings[1]+".txt";
//					try {
//						URL url = new URL(path);
//						URLConnection conn = url.openConnection();
//						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//						String line = null,lastLine = null;
//						while((line = br.readLine()) != null) {
//							lastLine = line;
//						}
//						double lat = Double.valueOf(lastLine.substring(0, 9));
//						double lng = Double.valueOf(lastLine.substring(10, 20));
//						//构建Marker图标  
//						BitmapDescriptor bitmap = BitmapDescriptorFactory  
//						    .fromResource(mark[markNum]);  
//						markNum++;
//						//构建MarkerOption，用于在地图上添加Marker  
//						OverlayOptions option = new MarkerOptions()  
//						    .position(new LatLng(lat, lng))
//						    .icon(bitmap);  
//						//在地图上添加Marker，并显示  
//						mBaiduMap.addOverlay(option);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						Log.d("URLConnection", e.toString());
//					}
//					lastStrings = nowStrings;
//				}
//				
//			}
			
			try {
				URL url = new URL("http://bngsn.bob.buttobi.net/sqltest.php");
				URLConnection conn = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));//conn.getInputStream();
				String line = null;
				while((line = br.readLine()) != null) {
					JSONArray ori = new JSONArray(line);
			        for(int i=0;i<ori.length();i++){
			        	JSONObject json_data = ori.getJSONObject(i);
			        	double lat = Double.valueOf(json_data.getString("pos_x"));
						double lng = Double.valueOf(json_data.getString("pos_y"));
						//构建Marker图标  
						BitmapDescriptor bitmap = BitmapDescriptorFactory  
						    .fromResource(mark[markNum]);  
						markNum++;
						//构建MarkerOption，用于在地图上添加Marker  
						OverlayOptions option = new MarkerOptions()  
						    .position(new LatLng(lat, lng))
						    .icon(bitmap);  
						//在地图上添加Marker，并显示  
						mBaiduMap.addOverlay(option);
			        }
				}
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				Log.d("URLConnection", e.toString());
			}
			//设置状态
			//mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(file.get(0)).build()));
			//画线
//			OverlayOptions ooPolyline = new PolylineOptions().width(10)
//					.color(0xAAFF0000).points(file);
//			mBaiduMap.addOverlay(ooPolyline);
			//画圆
//			OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
//					.center(file.get(0)).stroke(new Stroke(5, 0xAA000000))
//					.radius(10);
//			mBaiduMap.addOverlay(ooCircle);
			//卫星地图  
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			return null;
		}
		
	}
}
