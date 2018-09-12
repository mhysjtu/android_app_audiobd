package com.example.button;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends Activity {

	EditText CameraIP, ControlIP, Port;
	Button Button_go;
	String videoUrl, controlUrl, port;
	public static String CamerIp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		ControlIP = (EditText)findViewById(R.id.ip);
		Port = (EditText)findViewById(R.id.port);
		
		Button_go = (Button)findViewById(R.id.button_go);
		
		controlUrl = ControlIP.getText().toString();
		port = Port.getText().toString();
		
		Button_go.requestFocusFromTouch();
		
		Button_go.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				
				intent.putExtra("CameraIp", videoUrl);
				intent.putExtra("ControlUrl", controlUrl);
				intent.putExtra("Port", port);
				intent.putExtra("Is_Scale", true);
				
				intent.setClass(FirstActivity.this, MainActivity.class);
				FirstActivity.this.startActivity(intent);
				finish();
				System.exit(0);
			}
		});
		
		
	}
	
	//按下返回键后，提示"再按一次退出程序"
		private long exitTime = 0;
		@Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)   
	    {  
	                 if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
	                 {  
	                           
	                         if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000   
	                         {  
	                          Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();                                  
	                          exitTime = System.currentTimeMillis();  
	                         }  
	                         else  
	                         {  
	                             finish();  
	                             System.exit(0);  
	                         }  
	                                   
	                         return true;  
	                 }  
	                 return super.onKeyDown(keyCode, event);  
	    }  
}
