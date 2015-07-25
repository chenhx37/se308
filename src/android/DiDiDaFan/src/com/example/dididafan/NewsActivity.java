package com.example.dididafan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewsActivity extends Activity {
	private ListView OIWlv;
	private Button btn_message,btn_call;
	private SimpleAdapter receiveadapter;
	private SimpleAdapter pushdapter;
	private List<Map<String,Object>> mDataList = new ArrayList<Map<String,Object>>();
	private String baseurl = "http://ddmeal.sinaapp.com/";
	private SimpleAdapter adapter;
	
	private String mNamestr;
	private String lasttime;
	private String flag;
	
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){

				if(msg.what==6){
					//update listview
					OIWlv.setAdapter(adapter);
        			adapter.notifyDataSetChanged();
        			Toast.makeText(getApplicationContext(), "消息刷新成功",Toast.LENGTH_SHORT).show();
				}
				
			};
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagepage);
		btn_message = (Button)findViewById(R.id.btn_message);
		btn_call = (Button)findViewById(R.id.btn_call);
		btn_message.setOnClickListener(onClicker);
		btn_call.setOnClickListener(onClicker);
		//显示订单的listview
		OIWlv = (ListView)findViewById(R.id.MessagePagelistView);
		mDataList.clear();
		adapter = new SimpleAdapter(this,mDataList,R.layout.itemdetail,new String[]{"showitem"},new int[]{R.id.showitemNA});
		OIWlv.setAdapter(adapter);
		
		DB snappydb;
		try {
			
			snappydb = DBFactory.open(getApplicationContext());
			mNamestr = snappydb.get("username");
			lasttime = snappydb.get("lasttime_msg");
			flag = snappydb.get("flag_msg");
			snappydb.close();
			
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.obtainMessage(10).sendToTarget();
		}
		
		
		
		
		getAcceptOrder();
	}
	
	//跳转到主页
	public void MessagePageMPBtnClick(View v){
		Intent intent = new Intent();
		intent.setClass(NewsActivity.this, MainPage.class);
		startActivity(intent);
	}
	//跳转到发布
	public void MessagePagePublishBtnClick(View v){
		Intent intent = new Intent();
		intent.setClass(NewsActivity.this, PublishActivity.class);
		startActivity(intent);
	}
	//跳转到消息
	public void MessagePageNewsBtnClick(View v){
		Intent intent = new Intent();
		intent.setClass(NewsActivity.this, NewsActivity.class);
		startActivity(intent);
	}
	private OnClickListener onClicker = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_message:
				btn_message.setTextColor(Color.parseColor("#df3031"));
				btn_call.setTextColor(Color.WHITE);
				btn_message.setBackgroundResource(R.drawable.baike_btn_pink_left_f_96);
				btn_call.setBackgroundResource(R.drawable.baike_btn_trans_right_f_96);
				//switchFragment(MESSAGE_FRAGMENT_TYPE);
				mDataList.clear();
				getAcceptOrder();
				break;
			case R.id.btn_call:
				
				btn_message.setTextColor(Color.WHITE);
				btn_call.setTextColor(Color.parseColor("#df3031"));
				btn_message.setBackgroundResource(R.drawable.baike_btn_trans_left_f_96);
				btn_call.setBackgroundResource(R.drawable.baike_btn_pink_right_f_96);
				//switchFragment(CALL_FRAGMENT_TYPE);	
				mDataList.clear();
				getPostOrder();
				break;			
			}
		}
	};
	//从网上get到用户接收的订单并显示
	public void getAcceptOrder(){
		try{			
            // 发送请求
            Thread hth = new Thread(){
            	@Override
            	public void run(){
            		//先get到个人信息，再post上去
            		//System.out.println("hello111 ");            		          		
            		HttpResponse response;
            		try{	            			
            			String url = baseurl + "ddmeal/index/myMessage/";	
            			
            			HttpGet httpRequest = new HttpGet(url);
            			//System.out.println("hello222 ");
            			//header
            			Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");          
            			httpRequest.setHeader(headers);
            			Header headers1 = new BasicHeader("Accept","text/plain");
            			httpRequest.setHeader(headers1);
            			Header headers2 = new  BasicHeader("cookie","username="+mNamestr);
            			httpRequest.setHeader(headers2);
            			// 将请求体内容加入请求中
            			//httpRequest.setEntity(requestHttpEntity);
            			// 需要客户端对象来发送请求
            			HttpClient httpClient = new DefaultHttpClient();
            			// 发送请求
            			response = httpClient.execute(httpRequest);         			
            			//System.out.println("hello333 ");   
            			String nowstr = null;
            	        nowstr = EntityUtils.toString(response.getEntity());   //获取字符串
            	        System.out.println("hello "+ nowstr);   
            	        JSONObject jsonObject;
            	        try {
            	        		jsonObject = new JSONObject(nowstr);
            	        		String outstr = jsonObject.getString("myAcceptOrders");            	        		
            	        		//System.out.println("hello555 "+outstr);
            	        		//解析订单的数据数组
            	        		JSONArray arr = new JSONArray(outstr);
            	        		//System.out.println("长度shi"+arr.length());
            	        		for(int i=0;i<arr.length();i++){
            	        			JSONObject temp = (JSONObject)arr.get(i);
            	        			//fields里面才有订单详细信息
            	        			//可以直接在这里添加你想获取的数据
            	        			String result = temp.getString("fields");
            	        			//System.out.println("fields " + result);
            	        	
            	        			JSONObject smalltemp;       			
            	        			try {
            							smalltemp = new JSONObject(result);					
            							Map<String,Object> mMap = new HashMap<String,Object>();
            		        			mMap.put("content", "内容: "+smalltemp.getString("description"));
            		        			mMap.put("time", "结束时间: "+smalltemp.getString("endTime"));
            		        			mMap.put("price","悬赏是: "+smalltemp.getString("price"));
            		        			mMap.put("mealPrice", "饭菜价格:"+smalltemp.getString("mealPrice"));
            		        			mMap.put("acceptBy",smalltemp.getString("acceptBy"));
            		        			mMap.put("postUserName", "发布者是:"+smalltemp.getString("postUserName"));
            		        			mMap.put("diningRoomName", "食堂是:"+smalltemp.getString("diningRoomName"));
            		        			mMap.put("showitem","你"+"接了"+smalltemp.getString("postUserName")+"的单");
            		        			mDataList.add(mMap);           		        			
            		        				            		        				            		    				
            		    				//System.out.println("正常"+mDataList.get(i).get("content").toString());
            						} catch (JSONException e) {
            							// TODO Auto-generated catch block
            							e.printStackTrace();
            						}
            	        			//System.out.println("item是这样的"+mDataList.get(i).get("content").toString());
            	        		}
            	        		
            	        		handler.obtainMessage(6).sendToTarget();
            	        		/*
            	        		OIWlv.setAdapter(adapter);
            	        		adapter.notifyDataSetChanged();
            	        		*/
            	        		
            	        	} catch (JSONException e1) {
            	        		// TODO Auto-generated catch block
            	        		e1.printStackTrace();
            	        	}           
						//}							
            	    }catch (Exception e){
						e.printStackTrace();
					}
            		
				}
            };
			hth.start();
			//System.out.println("hello666 ");     
		}catch(Exception e){
        e.printStackTrace();
		}
	}
	//从网上get到用户发送的订单并显示
	public void getPostOrder(){
		try{			
            // 发送请求
            Thread hth = new Thread(){
            	@Override
            	public void run(){
            		//先get到个人信息，再post上去
            		//System.out.println("hello111 ");            		          		
            		HttpResponse response;
            		try{	            			
            			String url = baseurl + "ddmeal/index/myMessage/";	
            			
            			HttpGet httpRequest = new HttpGet(url);
            			System.out.println("hello222 ");
            			//header
            			Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");          
            			httpRequest.setHeader(headers);
            			Header headers1 = new BasicHeader("Accept","text/plain");
            			httpRequest.setHeader(headers1);
            			Header headers2 = new  BasicHeader("cookie","username="+mNamestr);
            			httpRequest.setHeader(headers2);
            			// 将请求体内容加入请求中
            			//httpRequest.setEntity(requestHttpEntity);
            			// 需要客户端对象来发送请求
            			HttpClient httpClient = new DefaultHttpClient();
            			// 发送请求
            			response = httpClient.execute(httpRequest);         			
            			System.out.println("hello333 ");   
            			String nowstr = null;
            	        nowstr = EntityUtils.toString(response.getEntity());   //获取字符串
            	        System.out.println("hello "+ nowstr);   
            	        JSONObject jsonObject;
            	        try {
            	        		jsonObject = new JSONObject(nowstr);
            	        		String outstr = jsonObject.getString("myOrders");            	        		
            	        		System.out.println("hello555 "+outstr);
            	        		//解析订单的数据数组
            	        		JSONArray arr = new JSONArray(outstr);
            	        		System.out.println("长度shi"+arr.length());
            	        		for(int i=0;i<arr.length();i++){
            	        			JSONObject temp = (JSONObject)arr.get(i);
            	        			//fields里面才有订单详细信息
            	        			//可以直接在这里添加你想获取的数据
            	        			String result = temp.getString("fields");
            	        			System.out.println("fields " + result);           	        	
            	        			JSONObject smalltemp;       			
            	        			try {
            							smalltemp = new JSONObject(result);					
            							if(smalltemp.getString("acceptBy")=="null"){
            								continue;
            							}
            							Map<String,Object> mMap = new HashMap<String,Object>();
            		        			mMap.put("content", "内容: "+smalltemp.getString("description"));
            		        			mMap.put("time", "结束时间: "+smalltemp.getString("endTime"));
            		        			mMap.put("price","悬赏是: "+smalltemp.getString("price"));
            		        			mMap.put("mealPrice", "饭菜价格:"+smalltemp.getString("mealPrice"));
            		        			mMap.put("acceptBy",smalltemp.getString("acceptBy"));
            		        			mMap.put("postUserName", "发布者是:"+smalltemp.getString("postUserName"));
            		        			mMap.put("diningRoomName", "食堂是:"+smalltemp.getString("diningRoomName"));
            		        			mMap.put("showitem",smalltemp.getString("acceptBy")+"接了"+"你"+"的单");
            		        			mDataList.add(mMap);           		        			           		        				            		        				            		    				
            		    				//System.out.println("正常"+mDataList.get(i).get("content").toString());
            						} catch (JSONException e) {
            							// TODO Auto-generated catch block
            							e.printStackTrace();
            						}
            	        			//System.out.println("item对么"+mDataList.get(i).get("content").toString());
            	        		}
            	        		
            	        		/*
            	        		OIWlv.setAdapter(adapter);
            	        		adapter.notifyDataSetChanged();
            	        		*/
            	        		handler.obtainMessage(6).sendToTarget();
            	        		
            	        	} catch (JSONException e1) {
            	        		// TODO Auto-generated catch block
            	        		e1.printStackTrace();
            	        	}           
						//}							
            	    }catch (Exception e){
						e.printStackTrace();
					}
            		
				}
            };
			hth.start();
			System.out.println("hello666 ");     
		}catch(Exception e){
        e.printStackTrace();
		}	
	}
}
