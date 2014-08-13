package com.lzz.positionclient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.ui.widget.NewerGuideDialog;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.IBinder;

public class SendPosition extends Service {

	private String name,x,y,time;
	private Timer timer;
	private SharedPreferences preferences;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 定位初始化
		mLocClient = new LocationClient(this);  				 	//声明LocationClient类
		mLocClient.registerLocationListener(myListener);			//注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		preferences = getSharedPreferences("option", MODE_PRIVATE);
		name = preferences.getString("name", null);
		name = name + "." + df.format(new Date());
		
		timer = new Timer();
		timer.schedule(new Send(), 0, 10000);
	}

	class Send extends TimerTask {
        public void run() {
			Map<String, String> params = new HashMap<String, String>();
			//name += time.substring(0, 10);
			params.put("name", name);
			params.put("x", x);
			params.put("y", y);
			params.put("time", time);
			HttpUtils.submitPostData(params, "utf-8");
            //timer.cancel(); //Terminate the timer thread
        }
    }
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//			} 
			DecimalFormat dcmFmt = new DecimalFormat("0.000000");
			x = String.valueOf(dcmFmt.format(location.getLatitude()));
			y = String.valueOf(dcmFmt.format(location.getLongitude()));
			time = location.getTime();
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}