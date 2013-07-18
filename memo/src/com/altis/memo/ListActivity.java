package com.altis.memo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
//import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity{
	
	static final int MENUITEM_ID_DELETE = 1;
	ListView itemListView;
	EditText noteEditText;
	Button  saveButton;
	static DBAdapter dbAdapter;
	static NoteListAdapter listAdapter;
	static List<MemoData> memoList = new ArrayList<MemoData>();
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        itemListView = (ListView)findViewById(R.id.itemListView);
        //saveButton.setOnClickListener(this);                       
        dbAdapter = new DBAdapter(this);
        listAdapter = new NoteListAdapter();
        registerForContextMenu(itemListView);
        itemListView.setAdapter(listAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, MemoActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
        loadNote();        
    }
    
    protected void loadNote(){
    	memoList.clear();
      
    	// Read
    	dbAdapter.open();
    	Cursor c = dbAdapter.getAllNotes();
      
    	if(c.moveToFirst()){
    		while(c.moveToNext()) {
    			MemoData memo = new MemoData(c.getInt(
    					c.getColumnIndex(DBAdapter.COL_ID)),
    					c.getString(c.getColumnIndex(DBAdapter.COL_TITLE)),
    					c.getString(c.getColumnIndex(DBAdapter.COL_BODY)),
    					c.getString(c.getColumnIndex(DBAdapter.COL_LASTUPDATE)));
    			memoList.add(memo);
    		};
    	}    
    	dbAdapter.close();
      
    	listAdapter.notifyDataSetChanged();
    }
    /*
    protected void saveItem(){
    	dbAdapter.open();
    	dbAdapter.saveMemo(noteEditText.getText().toString());
    	dbAdapter.close();
    	noteEditText.setText("");
    	loadNote();
    }*/
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, 
		  ContextMenuInfo menuInfo) {
    	menu.add(0, MENUITEM_ID_DELETE, 0, "Delete");
    	super.onCreateContextMenu(menu, v, menuInfo);
    	}
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case MENUITEM_ID_DELETE:
    		AdapterView.AdapterContextMenuInfo menuInfo 
    		= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();      
	
	        MemoData memo = memoList.get(menuInfo.position);
	        final int memoId = memo.getId();	        
	        new AlertDialog.Builder(this)
	        	.setTitle("ÉÅÉÇÇçÌèúÇµÇ‹Ç∑Ç©ÅH")
	        	.setPositiveButton(
	        			"Yes", 
	        			new DialogInterface.OnClickListener() {
	        				@Override
	        				public void onClick(DialogInterface dialog, int which) {
	        					dbAdapter.open();
	        					if(dbAdapter.deleteNote(memoId)){
	        						Toast.makeText(
	        								getBaseContext(), 
	        								"ÉÅÉÇÇçÌèúÇµÇ‹ÇµÇΩÅB", 
	        								Toast.LENGTH_SHORT).show();
	        						loadNote();
	        					}
	        					dbAdapter.close();
	        				}
	        			})
    			.setNegativeButton(
    					"Cancel",
    					null)
				.show();
	      
	      return true;
	    }
	    return super.onContextItemSelected(item);
	    }

  /*
  @Override
  	public void onClick(View v) {
	  	switch(v.getId()){
	  	case R.id.saveButton:
	  		saveItem();
	  		break;
	  	}
  	}*/
  
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.item_post:
	    	Intent intent = new Intent(ListActivity.this, MemoActivity.class);
            startActivity(intent);
	    	break;
	    default:
	    	break;
		}
	    return super.onOptionsItemSelected(item);
	}
  
	private static class ViewHolder {
		TextView title;
		TextView body;
		TextView lastupdate;
	}

  
	private class NoteListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return memoList.size();
		}

		@Override
		public Object getItem(int position) {
			return memoList.get(position);
		}	

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MemoData memo = (MemoData)getItem(position);
			ViewHolder holder;
    	
			if(convertView == null){
			  	LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  	convertView = inflater.inflate(R.layout.row, null);
    		
			  	holder = new ViewHolder();
			  	holder.title = (TextView) convertView.findViewById(R.id.titleText);
			  	holder.body = (TextView) convertView.findViewById(R.id.bodyText);
			  	holder.lastupdate = (TextView) convertView.findViewById(R.id.lastupdate);
			  	convertView.setTag(holder);
			}else {
			  	holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(memo.getTitle());
			holder.body.setText(memo.getBody().substring(0, 10));
			holder.lastupdate.setText(memo.getLastupdate());
			return convertView;
		}  
  	}
}