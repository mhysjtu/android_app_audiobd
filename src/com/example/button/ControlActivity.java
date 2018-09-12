package com.example.button;

import java.io.OutputStream;
import java.net.Socket;

//import com.example.button.ControlActivity.Connect_Thread2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ControlActivity extends Activity {

	private Button BtnForward, BtnBackward, BtnRight, BtnLeft, BtnUp, BtnDown, BtnAvoid, BtnFollow, BtnGrasp, BtnOneGrasp, BtnStop;
	private TextView status;
	
	public static String CtrlIp="192.168.1.1";
	public static String CtrlPort="2001";
	
	private Socket socket=null;
	OutputStream socketWriter;
	
	private byte[] COMM_FORWARD = {(byte)0xff,(byte)0x00,(byte)0x02,(byte)0x00,(byte)0xff};
	private byte[] COMM_BACKWARD= {(byte)0xff,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0xff};
	private byte[] COMM_LEFT    = {(byte)0xff,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0xff};
	private byte[] COMM_RIGHT   = {(byte)0xff,(byte)0x00,(byte)0x03,(byte)0x00,(byte)0xff};
	private byte[] COMM_STOP    = {(byte)0xff,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xff};
	private byte[] COMM_UP      = {(byte)0xff,(byte)0x01,(byte)0x10,(byte)0x00,(byte)0xff};
	private byte[] COMM_DOWN    = {(byte)0xff,(byte)0x01,(byte)0x11,(byte)0x00,(byte)0xff};
	private byte[] COMM_GRASP   = {(byte)0xff,(byte)0x01,(byte)0x04,(byte)0x00,(byte)0xff};
	private byte[] COMM_ONEGRASP= {(byte)0xff,(byte)0x01,(byte)0x12,(byte)0x00,(byte)0xff};
	private byte[] COMM_AVOID   = {(byte)0xff,(byte)0x13,(byte)0x04,(byte)0x00,(byte)0xff};
	private byte[] COMM_FOLLOW  = {(byte)0xff,(byte)0x13,(byte)0x01,(byte)0x00,(byte)0xff};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
		BtnForward = (Button)findViewById(R.id.up);
		BtnBackward = (Button)findViewById(R.id.down); 
        BtnLeft = (Button)findViewById(R.id.left);
        BtnRight = (Button)findViewById(R.id.right);
        BtnUp = (Button)findViewById(R.id.arm_up);
        BtnDown = (Button)findViewById(R.id.arm_down);
        BtnAvoid = (Button)findViewById(R.id.avoid);
        BtnFollow = (Button)findViewById(R.id.follow);
        BtnOneGrasp = (Button)findViewById(R.id.onegrasp);
        BtnGrasp = (Button)findViewById(R.id.grasp);
        BtnStop = (Button)findViewById(R.id.stop);
        
        status = (TextView)findViewById(R.id.textView1);
        
        //Intent intent = getIntent();
        
        Connect_Thread2 connect_Thread = new Connect_Thread2();
		connect_Thread.start();
		
		BtnForward.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				status.setText(R.string.clicked);
				int action = event.getAction();
				try{
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_FORWARD);
						socketWriter.flush();
						status.setText(R.string.written);
						break;
					case MotionEvent.ACTION_UP:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
						break;
					}
				}
				catch(Exception e){
					e.printStackTrace();
					status.setText(R.string.error);
				}
				return false;
			}
		});
		
		BtnBackward.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				status.setText(R.string.clicked);
				try{
					int action = event.getAction();
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_BACKWARD);
						socketWriter.flush();
						status.setText(R.string.written);
						break;
					case MotionEvent.ACTION_UP:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
						break;
					}
				}
				catch(Exception e){
					e.printStackTrace();
					status.setText(R.string.error);
				}
				return false;
			}
		});
		
		BtnRight.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				status.setText(R.string.clicked);
				try{
					int action = event.getAction();
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_RIGHT);
						socketWriter.flush();
						status.setText(R.string.written);
						break;
					case MotionEvent.ACTION_UP:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
						break;
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
					status.setText(R.string.error);
				}
				return false;
			}
		});
		
		BtnLeft.setOnTouchListener(new Button.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				status.setText(R.string.clicked);
				try{
					int action = event.getAction();
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_LEFT);
						socketWriter.flush();
						status.setText(R.string.written);
						break;
					case MotionEvent.ACTION_UP:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
						break;
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
					status.setText(R.string.error);
				}
				return false;
			}
		});
		
		BtnUp.setOnTouchListener(new Button.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				status.setText(R.string.clicked);
				try{
					int action = event.getAction();
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_UP);
						socketWriter.flush();
						status.setText(R.string.written);
						break;
					case MotionEvent.ACTION_UP:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
						break;
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
					status.setText(R.string.error);
				}
				return false;
			}
		});
		
		BtnDown.setOnTouchListener(new Button.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event){
				status.setText(R.string.clicked);
				try{
					int action = event.getAction();
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_DOWN);
						socketWriter.flush();
						status.setText(R.string.written);
						break;
					case MotionEvent.ACTION_UP:
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
						break;
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
					status.setText(R.string.error);
				}
				return false;
			}
		});
		
		BtnAvoid.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					socketWriter= socket.getOutputStream();
					socketWriter.write(COMM_AVOID);
					socketWriter.flush();
					status.setText(R.string.written);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					status.setText(R.string.error);
				}
			}
		});
		
		BtnFollow.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					socketWriter= socket.getOutputStream();
					socketWriter.write(COMM_FOLLOW);
					socketWriter.flush();
					status.setText(R.string.written);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					status.setText(R.string.error);
				}
			}
		});
		
		BtnOneGrasp.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					socketWriter= socket.getOutputStream();
					socketWriter.write(COMM_ONEGRASP);
					socketWriter.flush();
					status.setText(R.string.written);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					status.setText(R.string.error);
				}
			}
		});
		
		BtnGrasp.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					socketWriter= socket.getOutputStream();
					socketWriter.write(COMM_GRASP);
					socketWriter.flush();
					status.setText(R.string.written);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					status.setText(R.string.error);
				}
			}
		});
		
		BtnStop.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					socketWriter= socket.getOutputStream();
					socketWriter.write(COMM_STOP);
					socketWriter.flush();
					status.setText(R.string.written);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					status.setText(R.string.error);
				}
			}
		});
		
		
	}
	
	class Connect_Thread2 extends Thread{
    	public void start(){
    		try{
    			if(socket == null){
    				socket = new Socket("192.168.1.1",2001);
    				Toast.makeText(ControlActivity.this,"执行了socket创建函数！",Toast.LENGTH_LONG).show();
    				if (socket !=null){
    					runOnUiThread(new Runnable(){
    						public void run(){
    							Toast.makeText(ControlActivity.this, "connection succeed!", Toast.LENGTH_SHORT).show();
    						}
    					});
    				}
    			}
    		}
    		catch(Exception e){
    			e.printStackTrace();
    			Toast.makeText(ControlActivity.this,"初始化网络失败！",Toast.LENGTH_LONG).show();
    		}
    	}
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			Intent myIntent = new Intent();
			myIntent.setClass(ControlActivity.this, FirstActivity.class);
			ControlActivity.this.startActivity(myIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
