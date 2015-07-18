package com.example.dididafan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmActivity extends Activity{
	private EditText netidConfirmjava;
	private EditText passwordConfirmjava;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmationpage);
		//绑定，你可以直接使用netidConfirmjava.getText().toString()得到它的内容
		netidConfirmjava = (EditText)findViewById(R.id.netidConfirm);
		passwordConfirmjava = (EditText)findViewById(R.id.passwordConfirm);
	}
	
	public void confirmInfoClick(View v){
		
		new Thread(){
			public void run(){

				String baseurl = "http://ddmeal.sinaapp.com/";
				String url = baseurl + "ddmeal/index/verify/";	
				List <NameValuePair> params = new ArrayList <NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存 
				params.add(new BasicNameValuePair("netID",netidConfirmjava.getText().toString()));
				params.add(new BasicNameValuePair("password",passwordConfirmjava.getText().toString()));
				
				//时间戳和flag
				HttpEntity requestHttpEntity;
				try {
					requestHttpEntity = new UrlEncodedFormEntity(params,"UTF-8");
					
					HttpPost httpRequest = new HttpPost(url);
					//System.out.println("hello222 ");
					//header
					Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");          
					httpRequest.setHeader(headers);
					Header headers1 = new BasicHeader("Accept","text/plain");
					httpRequest.setHeader(headers1);
					//Header headers2 = new  BasicHeader("cookie","username="+mNamestr);
					//httpRequest.setHeader(headers2);
					//Header headers3 = new  BasicHeader("Content-Encoding","gzip");
					//httpRequest.setHeader(headers3);
					// 将请求体内容加入请求中
					httpRequest.setEntity(requestHttpEntity);
					// 需要客户端对象来发送请求
					HttpClient httpClient = new DefaultHttpClient();
					// 发送请求
					HttpResponse response = httpClient.execute(httpRequest);    
					
					
					
					
					String nowstr = null;
					//if(response.getStatusLine().getStatusCode() == 200)   {
		        	nowstr = EntityUtils.toString(response.getEntity());   //获取字符串
		        	//System.out.println("hello "+ nowstr);   
		        	JSONObject jsonObject;
		        	System.out.println(nowstr);
		    		jsonObject = new JSONObject(nowstr);
		    		String error = jsonObject.getString("error");
		    		String errorMs = jsonObject.getString("errorMs");
		    		//System.out.println("lt:"+lt);
		    		//System.out.println("hello555 "+outstr);
		    		//解析订单的数据数组
		    		System.out.println(error+" fucking "+errorMs);
		    		if(error=="false"){
		    			handler.obtainMessage(2).sendToTarget();
		    		}
		    		else{
		    			handler.obtainMessage(1).sendToTarget();
		    		}
		    		
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.obtainMessage(1).sendToTarget();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.obtainMessage(1).sendToTarget();
				}
				System.out.println("what the fuck");
			}
			
		}.start();
		
		
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
				if(msg.what==1){
					Toast.makeText(getApplicationContext(), "验证失败",Toast.LENGTH_SHORT).show();
				}
				if(msg.what==2){
					Toast.makeText(getApplicationContext(), "验证成功",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(ConfirmActivity.this, MainPage.class);
					startActivity(intent);
					ConfirmActivity.this.finish();
				}
			};
		};
	
}
