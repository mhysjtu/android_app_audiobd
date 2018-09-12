package com.example.button;

//import java.util.ArrayList;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
//import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.appcompat.*;

//import android.Manifest;
import android.app.Activity;
import android.content.Intent;
//import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements EventListener {
	
	//URL videoUrl;
	public static String CameraIp;
	public static String CtrlIp;
	public static String CtrlPort;
	private Socket socket=null;
	OutputStream socketWriter;
	
	private byte[] COMM_FORWARD = {(byte)0xff,(byte)0x00,(byte)0x02,(byte)0x00,(byte)0xff};
	private byte[] COMM_BACKWARD = {(byte)0xff,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0xff};
	private byte[] COMM_STOP = {(byte)0xff,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xff};
	private byte[] COMM_LEFT = {(byte)0xff,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0xff};
	private byte[] COMM_RIGHT = {(byte)0xff,(byte)0x00,(byte)0x03,(byte)0x00,(byte)0xff};
	private byte[] COMM_AVOID = {(byte)0xff,(byte)0x13,(byte)0x04,(byte)0x00,(byte)0xff};
	//private byte[] COMM_GRASP = {(byte)0xff,(byte)0x01,(byte)0x12,(byte)0x00,(byte)0xff};
	private byte[] COMM_FOLLOW = {(byte)0xff,(byte)0x13,(byte)0x01,(byte)0x00,(byte)0xff};
	
	private Button hellobtn;
	private TextView hellotv;
	private Button controlbtn;
	
	private EventManager asr;
	private boolean logTime = true;
	private boolean enableOffline = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        //��ʼ������
        initView();
        //initPermission();
        
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        Intent intent = getIntent();
        
        CameraIp = intent.getStringExtra("CameraIp");
        CtrlIp= intent.getStringExtra("ControlUrl");		
		CtrlPort=intent.getStringExtra("Port");
		
		Connect_Thread connect_Thread = new Connect_Thread();
		connect_Thread.start();
       
        hellobtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method 
				Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
				start();
			}
		});
        
        controlbtn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				
				intent.setClass(MainActivity.this, ControlActivity.class);
				MainActivity.this.startActivity(intent);
				finish();
				System.exit(0);
			}
		});
        
        if (enableOffline){
        	loadOffineEngine();
        }
        
    }
    
    /*private void initPermission() {
		// TODO Auto-generated method stub
    	 String permissions[] = {Manifest.permission.RECORD_AUDIO,
                 Manifest.permission.ACCESS_NETWORK_STATE,
                 Manifest.permission.INTERNET,
                 Manifest.permission.READ_PHONE_STATE,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE
         };

         ArrayList<String> toApplyList = new ArrayList<String>();

         for (String perm :permissions){
             if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                 toApplyList.add(perm);
                 //���뵽�������û��Ȩ��.

             }
         }
         String tmpList[] = new String[toApplyList.size()];
         if (!toApplyList.isEmpty()){
             ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
         }
	}*/

	private void loadOffineEngine() {
		// TODO Auto-generated method
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put(SpeechConstant.DECODER, 2);
		params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://wificar_baidu_speech_grammar.bsg");
		asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
	}

	protected void start() {
		// TODO Auto-generated method stub
		hellotv.setText("");
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		String event = null;
		event = SpeechConstant.ASR_START;
		
		if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
		
		params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
		//params.put(SpeechConstant.PID, 1737);//English
		
		String json = null;
		json = new JSONObject(params).toString();
		asr.send(event, json, null, 0, 0);
		
		printresult("�������"+ json);
	}

	private void initView(){
    	hellobtn=(Button)findViewById(R.id.button1);
    	hellotv = (TextView)findViewById(R.id.textView2);
    	controlbtn=(Button)findViewById(R.id.button2);
    }

	@Override
	public void onEvent(String name, String params, byte[] data, int offset,
			int length) {
		// TODO Auto-generated method stub
		String result = "name:" + name;
		//if(params != null && !params.isEmpty()){
		//	result+=";params:"+params;
		//}
		if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
			if(params.contains("\"final_result\"")){
				result += "\n"+"������������" + params.substring(45, 47);
				if(result.contains("ǰ��")){
					Toast.makeText(MainActivity.this,"ʶ�����ǰ����",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_FORWARD);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("����")){
					Toast.makeText(MainActivity.this,"ʶ����˺��ˣ�",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_BACKWARD);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("��ת")){
					Toast.makeText(MainActivity.this,"ʶ�������ת��",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_LEFT);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("��ת")){
					Toast.makeText(MainActivity.this,"ʶ�������ת��",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_RIGHT);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("ͣ")){
					Toast.makeText(MainActivity.this,"ʶ�����ֹͣ��",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_STOP);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("����")){
					Toast.makeText(MainActivity.this,"ʶ����˱��ϣ�",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_AVOID);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("����")){
					Toast.makeText(MainActivity.this,"ʶ����˸��٣�",Toast.LENGTH_SHORT).show();
					try{
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
						socketWriter= socket.getOutputStream();
						socketWriter.write(COMM_FOLLOW);
						socketWriter.flush();
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Try to send message to car!", Toast.LENGTH_SHORT).show();
					}
				}
				if(result.contains("ץȡ")){
					Toast.makeText(MainActivity.this,"ʶ�����ץȡ��",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, GraspActivity.class);
					MainActivity.this.startActivity(intent);
					finish();
					System.exit(0);
				}
			}
		}else if(data != null){
			result += ";data length=" + data.length;
		}
		
		
		/*if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)){
			result +="����׼�����������Կ�ʼ˵��";
		}else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)){
			result += "��⵽�û��Ѿ���ʼ˵��";
		}else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)){
			result += "��⵽�û��Ѿ�ֹͣ˵��";
			
		}else if (data != null) {
			result += " ;data length=" + data.length;
        }*/
	
		//show the result
		printresult(result);
		
	}
	
	private void printresult(String text){
		if(logTime){
			text += ";time=" + System.currentTimeMillis();
		}
		text += "\n";
		Log.i(getClass().getName(), text);
		hellotv.append(text + "\n");
	}

	//private void parseAsrFinishJsonData(String params) {
		// TODO Auto-generated method stub
		//Gson gson = new Gson();
	//}

	class Connect_Thread extends Thread{
    	public void start(){
    		try{
    			if(socket == null){
    				socket = new Socket(InetAddress.getByName(CtrlIp),Integer.parseInt(CtrlPort));
    				Toast.makeText(MainActivity.this,"ִ����socket����������",Toast.LENGTH_LONG).show();
    				if (socket !=null){
    					runOnUiThread(new Runnable(){
    						public void run(){
    							Toast.makeText(MainActivity.this, "connection succeed!", Toast.LENGTH_SHORT).show();
    						}
    					});
    				}
    			}
    		}
    		catch(Exception e){
    			e.printStackTrace();
    			Toast.makeText(MainActivity.this,"��ʼ������ʧ�ܣ�",Toast.LENGTH_LONG).show();
    		}
    	}
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			Intent myIntent = new Intent();
			myIntent.setClass(MainActivity.this, FirstActivity.class);
			MainActivity.this.startActivity(myIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
    
}
