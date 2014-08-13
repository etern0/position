package com.lzz.position;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener{

    private DBHelper db;
	private static Boolean isExit = false;
	private Fragment fragment = null;	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
        if(db != null){
            db.close();
        }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		//actionBar.setHomeButtonEnabled(true);
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1,
						new String[] {"站立点","历史记录"}), this);
        
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
		if(keyCode == KeyEvent.KEYCODE_BACK)  
		{
			Fragment moreFragment = getFragmentManager().findFragmentByTag("more");
			if(moreFragment !=null && moreFragment.isAdded())
			{
				FragmentTransaction ft = getFragmentManager().beginTransaction();
			    ft.replace(R.id.container, new History());
			    ft.commit();
			}
			else
				exitBy2Click();      //调用双击退出函数  
		}
	    return false;
	}
	
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else {  
	        finish();  
	        System.exit(0);  
	    }  
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			fragment = new StandPoint();
			break;
		case 1:
			fragment = new History();
			break;
		default:
			fragment = new StandPoint();
			break;
		}

		if(fragment != null){
			FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.container, fragment);
		    ft.commit();
		}

		return true;
	}

}
