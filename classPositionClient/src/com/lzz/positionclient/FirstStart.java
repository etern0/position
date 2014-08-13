package com.lzz.positionclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class FirstStart extends Activity {

	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("option", MODE_PRIVATE);
		editor = preferences.edit();
		if(preferences.getString("name", null) != null) {
			jump();
		}
		//setContentView(R.layout.first_start);
	}
	
	private void jump() {
		this.startActivity(new Intent(FirstStart.this, MainActivity.class));
		this.finish();
	}
}
