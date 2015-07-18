package com.example.dididafan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	private EditText javasignuppageLoginPassword;
	private EditText javasignuppageLoginUser;
	private TextView javaSUPshowerrorTV;
	private String baseurl = "http://ddmeal.sinaapp.com/";
	private String result = null;
	//private String baseurl = "http://www.baidu.com";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signuppage);
		javasignuppageLoginUser = (EditText)findViewById(R.id.signuppageLoginUser);
		javasignuppageLoginPassword = (EditText)findViewById(R.id.signuppageLoginPassword);
		javaSUPshowerrorTV = (TextView)findViewById(R.id.SUPshowerrorTV);
		
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			if(msg.what==1){
				javaSUPshowerrorTV.setText(result);
			}
		};
	};
	public void SignUpnewPage(View v){		
		try{			
            // 发送请求
            Thread hth = new Thread(){
            	@Override
            	public void run(){
            		HttpResponse response;
            		try{
            			//取得账号和密码
            			String supuserstr = javasignuppageLoginUser.getText().toString();
            			String suppsdstr = javasignuppageLoginPassword.getText().toString();
            			
            			String url = baseurl+"ddmeal/regist/";
            			List <NameValuePair> params = new ArrayList <NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存 
            			params.add(new BasicNameValuePair("username",supuserstr));
            			params.add(new BasicNameValuePair("password",suppsdstr));
            			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params);

            			HttpPost httpRequest = new HttpPost(url);	
            			//header
            			Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");
            			httpRequest.setHeader(headers);
            			Header headers1 = new BasicHeader("Accept","text/plain");
            			httpRequest.setHeader(headers1);
            			 // 将请求体内容加入请求中
            			httpRequest.setEntity(requestHttpEntity);
            			// 需要客户端对象来发送请求
                         HttpClient httpClient = new DefaultHttpClient();
                       
                        //返回数据，取得响应
            			response = httpClient.execute(httpRequest);
            			String nowstr = null;
            	        if(response.getStatusLine().getStatusCode() == 200)   {
            	        	nowstr = EntityUtils.toString(response.getEntity());   //获取字符串
            	        	System.out.println("hello "+ nowstr);   
            	        	JSONObject jsonObject;
            	        	try {
            	        		jsonObject = new JSONObject(nowstr);
            	        		String outstr = jsonObject.getString("error");	
            	        		result = jsonObject.getString("errorMs");	
            	        		System.out.println("hello555 "+outstr);
            	        		if(outstr=="false"){
            	        			//将注册的用户名和密码进行传送，省去重新输入
            	        			Bundle herebundle = new Bundle();
            	        			herebundle.putString("usernameout",supuserstr);
            	        			herebundle.putString("passwordout",suppsdstr);
            	        			Intent intent = new Intent();
            	        			intent.setClass(SignUpActivity.this, MainActivity.class);
            	        			intent.putExtras(herebundle);
            	        			startActivity(intent);
            	        			SignUpActivity.this.finish();
            	        		}
            	        	} catch (JSONException e1) {
            	        		// TODO Auto-generated catch block
            	        		e1.printStackTrace();
            	        	}           	               	             	              		        			 
            	      }           	       
            		}catch(IOException ec){
            			response = null;
            		}
            		handler.obtainMessage(1,result).sendToTarget();
            	}
            };
            hth.start();
            System.out.println("hello666 ");
            
		}catch(Exception e)
        {
            e.printStackTrace();
        }
			
		//httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); //发出http请求 
		
		//MainPage.this.finish();
	}
}
