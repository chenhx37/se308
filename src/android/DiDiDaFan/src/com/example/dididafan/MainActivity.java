package com.example.dididafan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button javamainLoginBtn;
	private EditText javamainLoginuser;
	private EditText javamainLoginpsd;
	private TextView javashowerrorTV;
	private String baseurl = "http://ddmeal.sinaapp.com/";
	private String result = null;
	public static String UserBigStr = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		
		
		setContentView(R.layout.activity_main);
		javamainLoginBtn = (Button)findViewById(R.id.mainLoginBtn);
		javamainLoginuser = (EditText)findViewById(R.id.mainLoginUser);
		javamainLoginpsd = (EditText)findViewById(R.id.mainLoginPassword);
		javashowerrorTV = (TextView)findViewById(R.id.showerrorTV);
		/*Bundle herebun = new Bundle();
		String mname = herebun.getString("usernameout");
		String mpsd = herebun.getString("passwordout");
		javamainLoginuser.setText(mname);
		javamainLoginpsd.setText(mpsd);*/
		UserBigStr = javamainLoginuser.getText().toString();
		//TrySnappy();

		
	}

	@Override
	public void onStart(){
		super.onStart();
		try {
			DB snappydb = DBFactory.open(getApplicationContext());
			
			
			//System.out.println("username="+un);
			if(snappydb.exists("username")){
				snappydb.close();
           		Bundle mbundle = new Bundle();
           		String namestr = javamainLoginuser.getText().toString();
           		mbundle.putString("name", namestr);
           		Intent intent = new Intent();
	        
           		intent.setClass(MainActivity.this, MainPage.class);
           		intent.putExtras(mbundle);
           		startActivity(intent);
           		MainActivity.this.finish();
			}
			else{
				snappydb = DBFactory.open(getApplicationContext());
				snappydb.put("lasttime_all","1.0");
				snappydb.put("flag_all","0");
				snappydb.put("lasttime_msg","1.0");
				snappydb.put("flag_msg","0");
				
				
				//System.out.println("hello "+snappydb.get("lasttime_all"));
				snappydb.close();				
			}
		} catch (SnappydbException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	
	public void TrySnappy(){
		try {
			DB snappydb = DBFactory.open(getApplicationContext());
			snappydb.del("username");
			snappydb.close();
			
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SignUpMain(View v){
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SignUpActivity.class);		
		startActivity(intent);
		//MainActivity.this.finish();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			if(msg.what==1){
				//javashowerrorTV.setText(result);
				Toast.makeText(getApplicationContext(), result,Toast.LENGTH_SHORT).show();
			}
			if(msg.what==7){
				//set username in snappydb;
				try {
					DB snappydb = DBFactory.open(getApplicationContext());
					snappydb.put("username",javamainLoginuser.getText().toString());
					snappydb.close();
				} catch (SnappydbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
	};
	/* 登陆按钮的click事件 */
	public void SignInMain(View v){			
		try{			
            // 发送请求
            Thread hth = new Thread(){
            	@Override
            	public void run(){
            		
            		HttpResponse response;
            		try{
            			//取得账号和密码
            			String supuserstr = javamainLoginuser.getText().toString();
            			String suppsdstr =  javamainLoginpsd.getText().toString();
            			UserBigStr = supuserstr;
            			String url = baseurl+"ddmeal/login/";
            			List <NameValuePair> params = new ArrayList <NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存 
            			params.add(new BasicNameValuePair("username",supuserstr));
            			params.add(new BasicNameValuePair("password",suppsdstr));
            			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params);
            			HttpPost httpRequest = new HttpPost(url);	
            		//	List<Header> headers;
            			//headers.add(new BasicHeader("Content-type","application/x-www-form-urlencoded"));
            		//	headers.add(new BasicHeader("Accept","text/plain"));
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
           	            JSONObject jsonObject;
           	            try {
           	            	jsonObject = new JSONObject(nowstr);
           	            	String outstr = jsonObject.getString("error");
           	            	
           	            	result = jsonObject.getString("errorMs");      	            	          	            	
           	            	if(outstr=="false"){           	            		
           	            		handler.obtainMessage(7).sendToTarget();
           	            		Bundle mbundle = new Bundle();
           	            		String namestr = javamainLoginuser.getText().toString();
           	            		mbundle.putString("name", namestr);
           	            		Intent intent = new Intent();
           		        
           	            		intent.setClass(MainActivity.this, MainPage.class);
           	            		intent.putExtras(mbundle);
           	            		startActivity(intent);
           	            		MainActivity.this.finish();
           	            	}
           	            	if(outstr=="true"){
                    			handler.obtainMessage(1,result).sendToTarget();
                    		}
           	            	
           	            } catch (JSONException e1) {
						// TODO Auto-generated catch block
           	            	e1.printStackTrace();
           	            }
           	     
            	        }
            	       
            		}catch(IOException ec){
            			response = null;
            		}
            		
            		
            	}
            };
            hth.start();
		}catch(Exception e)
        {
            e.printStackTrace();
        }
		//MainActivity.this.finish();
	}
	private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
     /*   try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }

            System.out.println(result);
            if(result.contains("true")){
            	
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        

    }
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
}
