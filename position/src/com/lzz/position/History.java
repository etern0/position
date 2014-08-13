package com.lzz.position;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;

public class History extends ListFragment {

	private DBHelper dbHelper;
	private Cursor cursor;
	private Activity activity;
	private ListView lv;
	private SimpleCursorAdapter lvAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = getActivity();
		dbHelper = new DBHelper(activity, "positionBackup.db3", 1);
		cursor = dbHelper.getReadableDatabase().rawQuery("select * from position_info", null);
		lvAdapter = new SimpleCursorAdapter(activity, R.layout.layout_save, cursor,
				new String[] {"note","positionx","positiony","time"},
				new int[] {R.id.note, R.id.x, R.id.y, R.id.time},
				android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		if (cursor.getCount()==0) {
			setListAdapter(new ArrayAdapter<String>(activity, 
					android.R.layout.simple_list_item_1, new String[] {"Пе"}));
		}
		else {
			setListAdapter(lvAdapter);
		}
		// TODO: handle exception
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		lv = getListView();
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int arg2, final long arg3) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(activity, view);
				popup.inflate(R.menu.historyclick);
				popup.show();
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						cursor.moveToPosition(arg2);
						int mid = cursor.getInt(cursor.getColumnIndex("_id"));
						switch (item.getItemId()) {
						case R.id.del:
							cursor = dbHelper.getReadableDatabase().rawQuery("select picPath from position_info where _id=" + Integer.toString(mid),null);
							cursor.moveToNext();
							String delPath = cursor.getString(0);
							if(delPath != null){
								File delFile = new File(delPath);
								delFile.delete();
							}
							dbHelper.getReadableDatabase().delete("position_info", "_id=?", new String[]{Integer.toString(mid)});
							cursor = dbHelper.getReadableDatabase().rawQuery("select * from position_info", null);
							lvAdapter.changeCursor(cursor);
							lvAdapter.notifyDataSetChanged();
							break;
						case R.id.more:
							Bundle argument = new Bundle();
							Fragment fragment = new More();
							argument.putInt("id", mid);
							fragment.setArguments(argument);
							FragmentTransaction ft = getFragmentManager().beginTransaction();
							ft.replace(R.id.container, fragment,"more");
							ft.addToBackStack(null);
							ft.commit();
							break;
						default:
							break;
						}
						return false;
					}
				});
				return false;
			}
		});
		super.onViewCreated(view, savedInstanceState);
	}
}
