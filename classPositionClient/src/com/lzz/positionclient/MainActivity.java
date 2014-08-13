package com.lzz.positionclient;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private Intent intent;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private TextView textView;
	private EditText editText;
	private Button button;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView1);
		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		preferences = getSharedPreferences("option", MODE_PRIVATE);
		if(preferences.getString("name", null) != null) {
			textView.setVisibility(View.INVISIBLE);
			editText.setVisibility(View.INVISIBLE);
			button.setVisibility(View.INVISIBLE);
			intent = new Intent();
			intent.setAction("com.lzz.service.SEND_POSITION");
			startService(intent);
		}
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = editText.getText().toString();
				if(name != null) {
					editor = preferences.edit();
					editor.putString("name", name);
					editor.commit();
					textView.setVisibility(View.INVISIBLE);
					editText.setVisibility(View.INVISIBLE);
					button.setVisibility(View.INVISIBLE);
					intent = new Intent();
					intent.setAction("com.lzz.service.SEND_POSITION");
					startService(intent);
				}
			}
		});

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(intent);
	}
}
