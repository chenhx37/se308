package com.example.dididafan;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

//发布界面的实现
public class PublishActivity extends Activity {
	private String baseurl = "http://ddmeal.sinaapp.com/";
	private EditText javapocontent;
	private String addressmeal = "first";
	private EditText javapoprice;
	private EditText javapojewal;
	private Button timePicker;
	private int hour1;
	private int minute1;
	Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
	Calendar calendar = Calendar.getInstance();
	private static final String[] canteeList={"第一食堂","第二食堂","第三食堂","第四食堂","行政楼食堂"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Button timePickerBtn;
    private TimePickerDialog tpd;
    private String mNamestr;
    
    Date mydate=new Date(); 
    
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			
			if(msg.what==4){
				Toast.makeText(getApplicationContext(), "发布失败",Toast.LENGTH_SHORT).show();
			}
			if(msg.what==5){
				Toast.makeText(getApplicationContext(), "发布成功",Toast.LENGTH_SHORT).show();
			}
		};
	};
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placeorder);			
		javapocontent = (EditText)findViewById(R.id.POConetntET);
		javapojewal = (EditText)findViewById(R.id.POJewalET);
		javapoprice = (EditText)findViewById(R.id.POPriceET);
		spinner = (Spinner)findViewById(R.id.spinnerCantee);
		//将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,canteeList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);      
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
		mycalendar.setTime(mydate);    	    
	    hour1 = mycalendar.get(Calendar.HOUR_OF_DAY);
	    minute1 = mycalendar.get(Calendar.MINUTE);
	    timePickerBtn = (Button)findViewById(R.id.selectTime);
	    timePickerBtn.setText(hour1+":"+minute1);
        timePickerBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tpd = new TimePickerDialog(PublishActivity.this, mTimeSetListener, hour1, minute1, true);
				tpd.show();
			}
		});
        //获取餐厅名称
        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int which = (int)spinner.getSelectedItemId();
				switch(which){
					case 0:
						addressmeal = "first";
						break;
					case 1:
						addressmeal = "second";
						break;
					case 2:
						addressmeal = "third";
						break;
					case 3:
						addressmeal = "fourth";
						break;
					case 4:
						addressmeal = "administration";
						break;
					default:
						addressmeal = "first";
						break;
				}
				System.out.println("食堂"+addressmeal);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				addressmeal = "first";
			}
        	
        });
        
        //read username
        try {
			DB snappydb = DBFactory.open(getApplicationContext());
			mNamestr = snappydb.get("username");
			snappydb.close();
		} catch (SnappydbException e) {
			// TODO Auto-generated catch blockf
			e.printStackTrace();
		}
			
	}
	//跳转到主页
	public void POMainPublishBtnClick(View v){
		Intent intent = new Intent();
  		intent.setClass(PublishActivity.this, MainPage.class);
  		startActivity(intent);
  		PublishActivity.this.finish();
	}
	//发布页面
	public void PONewsPostBtnClick(View v){
		Intent intent = new Intent();
  		intent.setClass(PublishActivity.this, PublishActivity.class);
  		startActivity(intent);
	}
	//跳转到消息页面
	public void PONewsPulishBtnClick(View v){
		Intent intent = new Intent();
  		intent.setClass(PublishActivity.this, NewsActivity.class);
  		startActivity(intent);
	}
	public void PublishAcSureBtnClick(View v){
		PublishOrderAndroid();
		Intent intent = new Intent();
  		intent.setClass(PublishActivity.this, MainPage.class);
  		startActivity(intent);
  		PublishActivity.this.finish();
	}
	//发布订单操作
	public void PublishOrderAndroid(){
		try{			
            // 发送请求
            Thread hth = new Thread(){
            	@Override
            	public void run(){
            		//先get到个人信息，再post上去
            		System.out.println("hello111 ");           		       		
            		HttpResponse response;
            		try{
            			String contentstr = javapocontent.getText().toString();
            			String jewalstr = javapojewal.getText().toString();
            			String pricestr = javapoprice.getText().toString();
            			String url = baseurl + "ddmeal/index/release/";
            			//获取当前日期
            			int year = mycalendar.get(Calendar.YEAR);
            			int month = mycalendar.get(Calendar.MONTH) + 1;
            			int day = mycalendar.get(Calendar.DAY_OF_MONTH);
            			int minus = 59;
            			List <NameValuePair> params = new ArrayList <NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存 
            			params.add(new BasicNameValuePair("description",contentstr));
            			params.add(new BasicNameValuePair("price",jewalstr));
            			params.add(new BasicNameValuePair("mealPrice",pricestr));
            			params.add(new BasicNameValuePair("diningRoom",addressmeal));
            			params.add(new BasicNameValuePair("endTime",year+"-"+month+"-"+day+" "+hour1+":"+minute1+":"+minus));		
            			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params,"UTF-8");
            			HttpPost httpRequest = new HttpPost(url);
            			System.out.println("hello222 ");
            			//header
            			Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");          
            			httpRequest.setHeader(headers);
            			Header headers1 = new BasicHeader("Accept","text/plain");
            			httpRequest.setHeader(headers1);
            			Header headers2 = new  BasicHeader("Cookie","username="+mNamestr);
            			httpRequest.setHeader(headers2);
            			Header headers3 = new BasicHeader("Content-Encoding","gzip");
            			httpRequest.setHeader(headers3);
            			// 将请求体内容加入请求中
            			httpRequest.setEntity(requestHttpEntity);
            			// 需要客户端对象来发送请求
            			HttpClient httpClient = new DefaultHttpClient();
            			// 发送请求
            			response = httpClient.execute(httpRequest);
            			System.out.println("hello333 ");
            			HttpEntity httpEntity = response.getEntity();
            			String nowstr = null;
            	     //   if(response.getStatusLine().getStatusCode() == 200)   {
            	        	nowstr = EntityUtils.toString(response.getEntity());   //获取字符串
            	        	System.out.println("hello "+ nowstr);   
            	        	JSONObject jsonObject;
            	        	try {
            	        		jsonObject = new JSONObject(nowstr);
            	        		String outstr = jsonObject.getString("error");
            	        		if(outstr == "false"){
            	        			//succeed
            	        			handler.obtainMessage(5).sendToTarget();
            	        		}
            	        		else{
            	        			handler.obtainMessage(4).sendToTarget();
            	        		}
            	        		
            	        	} catch (JSONException e1) {
            	        		// TODO Auto-generated catch block
            	        		e1.printStackTrace();
            	        		handler.obtainMessage(4).sendToTarget();
            	        	}           
						//}							
            	    }catch (Exception e){
						e.printStackTrace();
						handler.obtainMessage(4).sendToTarget();
					}
            		
				}
            };
			hth.start();
			System.out.println("hello666 ");     
		}catch(Exception e){
        e.printStackTrace();
		}
	}
	//取消发布订单
	public void PublishAcCancelBtnClick(View v){
		Intent intent = new Intent();
  		intent.setClass(PublishActivity.this, MainPage.class);
  		startActivity(intent);
  		PublishActivity.this.finish();
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =  
	        new TimePickerDialog.OnTimeSetListener()   
	   		{  	
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					 hour1 = hourOfDay;  
	                   minute1 = minute;    
	                   timePickerBtn.setText(hour1 + ":" + minute1);
	                    Toast.makeText(getBaseContext(),   
	                        "You have selected the time: " + hour1 + ":" + minute1,  
	                        Toast.LENGTH_SHORT).show();
				}
	}; 
	
}
