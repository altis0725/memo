package com.altis.memo;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemoActivity extends Activity implements OnClickListener {
	 /** Called when the activity is first created. */
	 EditText body;
	 Button saveButton,delButton,expButton;
	 String fileName;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_memo);
		 body = (EditText)findViewById(R.id.body);
		 saveButton = (Button)findViewById(R.id.saveButton);
		 delButton = (Button)findViewById(R.id.delButton);
		 expButton = (Button)findViewById(R.id.expButton);
		 saveButton.setOnClickListener(this);
		 delButton.setOnClickListener(this);
		 expButton.setOnClickListener(this);
		 fileName = getResources().getString(R.id.title);
		 
		 Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();
		 
		 try{
			 FileInputStream fis = openFileInput(fileName);
			 byte[] readBytes = new byte[fis.available()];
			 fis.read(readBytes);
			 body.setText(new String(readBytes));
			 fis.close();
		 }catch(Exception e){}
	 }

	 @Override
	 public void onClick(View v) {
		 if(v == saveButton){
		 //保存
		 try{
			 FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			 fos.write(body.getText().toString().getBytes());
			 fos.close();
			 Toast.makeText(this, getFilesDir() + "に保存しました", Toast.LENGTH_LONG).show();
		 }catch(Exception e){};
		 
		 }else if(v == delButton){
		 //削除
			 body.setText("");
			 deleteFile(fileName);
			 Toast.makeText(this, "削除しました。", Toast.LENGTH_LONG).show();
		 }else if(v == expButton){
		 //呼出
			 try{
				 FileInputStream fis = openFileInput(fileName);
				 byte[] readBytes = new byte[fis.available()];
				 fis.read(readBytes);
				 body.setText(new String(readBytes));
				 fis.close();
				 Toast.makeText(this, "呼び出しました。", Toast.LENGTH_LONG).show();
			 }catch(Exception e){}
		 }
	 }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo, menu);
		return true;
	}

}
