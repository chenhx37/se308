package com.example.dididafan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.CookieSpec;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class MainPage extends Activity {
		private Button javamainpersonnewsbtn;
		private ListView ep;
		private List<Map<String,Object>> mDataList = new ArrayList<Map<String,Object>>();
		private List<Map<String,Object>> showList = new ArrayList<Map<String,Object>>();
		private List<Map<String,Object>> tmpDataList = new ArrayList<Map<String,Object>>();
		
		private int temp_id = -1;
		private final int SELECT_A_CONTACT_DIALOG = 1;
		private String baseurl = "http://ddmeal.sinaapp.com/";
		private Map<String,Object> hereList = new HashMap<String,Object>();
		private String mNamestr;
		private String result = null;
		//ʱ���
		private String timelast;
		//flag
		private String flag = "1";
		private SimpleAdapter adapter;
		private String showhere = null;
		//���pkֵ
		private String pkidstr = null;
		private List<Map<String,String>> pklist = new ArrayList<Map<String,String>>();
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.mainpersonpage);	
			/*�õ��û���������ʾ*/
			javamainpersonnewsbtn = (Button)findViewById(R.id.mainpersonnewsbtn);
			
			DB snappydb;
			try {
				snappydb = DBFactory.open(getApplicationContext());
				mNamestr = snappydb.get("username");
				timelast = snappydb.get("lasttime_all");
				flag = snappydb.get("flag_all");
				snappydb.close();
			} catch (SnappydbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.obtainMessage(10).sendToTarget();
			}
			
			//mNamestr = MainActivity.UserBigStr;
			
			//showhere = MainActivity.UserBigStr;
			showhere = mNamestr;
			javamainpersonnewsbtn.setText(showhere);
		//	setData();
			mDataList.clear();
			showList.clear();
			
			//�����ݵ�listview
			ep = (ListView)findViewById(R.id.mainpersonLV);
			adapter = new SimpleAdapter(this,mDataList,R.layout.mainpagelistitem,new String[]{"content","time","price","mealPrice","postUserName","diningRoomName"},new int[]{R.id.MPitemcontentTV,R.id.MPitempriceTV,R.id.MPitemtimeTV,R.id.MPitemmealpriceTV,R.id.MPitempostbyTV,R.id.MPitemdiningroomTV});
			ep.setAdapter(adapter);
			ep.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					temp_id = position;
				//	getOrdernow();
					showDialog(SELECT_A_CONTACT_DIALOG);
					//return true;
					// TODO Auto-generated method stub
					return true;
				}
			});
			adapter.notifyDataSetChanged();
		/*	ep.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					temp_id = position;
					
				}
			});*/
			
			
			
			getOrdernow();
		}

		
		public void refreshList(){
			try{
				//System.out.println("refreshing mDataList "+mDataList);
				adapter = new SimpleAdapter(this,mDataList,R.layout.mainpagelistitem,new String[]{"content","time","price","mealPrice","postUserName","diningRoomName"},new int[]{R.id.MPitemcontentTV,R.id.MPitempriceTV,R.id.MPitemtimeTV,R.id.MPitemmealpriceTV,R.id.MPitempostbyTV,R.id.MPitemdiningroomTV});
				ep.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		//merge the data in tmpDataList into mDataList
		public void mergeDataList(){
			mDataList.clear();
			
			try {
				DB snappydb = DBFactory.open(getApplicationContext());
				//ArrayList<Map<String,Object>> alDataList = (ArrayList<Map<String,Object>>)snappydb.getObject("mDataList",ArrayList.class);
				if(!snappydb.exists("mDataList")){
					System.out.println("2015 not exist");
					for(Iterator i = tmpDataList.iterator();i.hasNext();){
						mDataList.add((Map<String,Object>)i.next());
					}
					mDataList = tmpDataList;
					System.out.println("none exist");
					snappydb.put("mDataList",mDataList);
				}
				else{
					System.out.println("2015 exist");
					mDataList = (ArrayList<Map<String,Object>>)snappydb.getObject("mDataList",ArrayList.class);
					System.out.println("2015 size:"+tmpDataList.size());
					int f = 0;
					//deal with insertions and changes
					for(int t = 0;t < tmpDataList.size();t ++){
						System.out.println("2015 for "+t);
						Map<String,Object> tmpMap = tmpDataList.get(t);
						for(int i = 0;i < mDataList.size();i ++){
							Map<String,Object> tmpMap1 = mDataList.get(i); 
							System.out.println("pk1:"+tmpMap.get("pk")+" pk2:"+tmpMap1.get("pk"));
							if(tmpMap.get("pk") == tmpMap1.get("pk")){
								//��Ҫ���ģ�����tmpMap1��������ֵ
								mDataList.set(i,tmpMap);
								f = 1;
								break;
							}
						}
						//if this order is new
						if(f == 0){
							mDataList.add(0,tmpMap);
							System.out.println("add to front");
						}
					}
					
					System.out.println("2015 p1");
					List<Map<String,Object>> tmplist = new ArrayList<Map<String,Object>>();
					for(int i = 0;i < mDataList.size();i++){
						Map<String,Object> tmpMap1 = mDataList.get(i); 
						System.out.println("2015 tmpMap1:"+tmpMap1);
						if(tmpMap1.get("status") == "1"){
							tmplist.add(tmpMap1);
						}						
					}
					System.out.println("2015 tmplist:"+tmplist);
					System.out.println("2015 p2");
					for(int i = 0;i < tmplist.size();i ++){
						mDataList.remove(tmplist.get(i));
					}
					
					
					snappydb.put("mDataList", mDataList);
					
					
					//System.out.println("fucking"+mDataList);
				}
				//System.out.println("shit!"+snappydb.getObject("mDataList", ArrayList.class));
				
				snappydb.close();
			} catch (SnappydbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			refreshList();
			//System.out.println(mDataList);
		}
		
		public void RefreshBtn(View v){
			getOrdernow();
		}
		
		//���������ť����ת����������
		public void PublishMainBtn(View v){				
			Intent intent = new Intent();
			intent.setClass(MainPage.this, PublishActivity.class);
			startActivity(intent);
		}
		
		//������õ����еĶ���,����ʱ�����flag
		public void getOrdernow(){
			//mDataList.clear();
			tmpDataList.clear();
			try{			
	            // ��������
	            Thread hth = new Thread(){
	            	@Override
	            	public void run(){
	            		//��get��������Ϣ����post��ȥ
	            		//System.out.println("hello111 ");            		          		
	            		HttpResponse response;
	            		
	            		DB snappydb;
	        			try {
	        				
	        				snappydb = DBFactory.open(getApplicationContext());
	        				mNamestr = snappydb.get("username");
	        				timelast = snappydb.get("lasttime_all");
	        				flag = snappydb.get("flag_all");
	        				
	        				snappydb.close();
	        				
	        			} catch (SnappydbException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        				handler.obtainMessage(10).sendToTarget();
	        			}
	            		
	            		try{	            			
	            			String url = baseurl + "ddmeal/index/allMessage/";	
	            			List <NameValuePair> params = new ArrayList <NameValuePair>();   //Post�������ͱ���������NameValuePair[]���鴢�� 
	            			params.add(new BasicNameValuePair("lastTime",timelast));
	            			params.add(new BasicNameValuePair("flag",flag));
	            			//ʱ�����flag
	            			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params,"UTF-8");
	            			HttpPost httpRequest = new HttpPost(url);
	            			//System.out.println("hello222 ");
	            			//header
	            			Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");          
	            			httpRequest.setHeader(headers);
	            			Header headers1 = new BasicHeader("Accept","text/plain");
	            			httpRequest.setHeader(headers1);
	            			Header headers2 = new  BasicHeader("cookie","username="+mNamestr);
	            			httpRequest.setHeader(headers2);
	            			//Header headers3 = new  BasicHeader("Content-Encoding","gzip");
	            			//httpRequest.setHeader(headers3);
	            			// �����������ݼ���������
	            			httpRequest.setEntity(requestHttpEntity);
	            			// ��Ҫ�ͻ��˶�������������
	            			HttpClient httpClient = new DefaultHttpClient();
	            			// ��������
	            			response = httpClient.execute(httpRequest);         			
	            			//System.out.println("hello333 ");   
	            			String nowstr = null;
	            			//if(response.getStatusLine().getStatusCode() == 200)   {
            	        	nowstr = EntityUtils.toString(response.getEntity());   //��ȡ�ַ���
            	        	//System.out.println("hello "+ nowstr);   
            	        	JSONObject jsonObject;
            	        	try {
            	        		jsonObject = new JSONObject(nowstr);
            	        		String outstr = jsonObject.getString("orders");
            	        		String lt = jsonObject.getString("lastTime");
            	        		//System.out.println("lt:"+lt);
            	        		//System.out.println("hello555 "+outstr);
            	        		//������������������
            	        		JSONArray arr = new JSONArray(outstr);
            	        		//System.out.println("����shi"+arr.length());
            	        		for(int i=0;i<arr.length();i++){
            	        			JSONObject temp = (JSONObject)arr.get(i);
            	        			//System.out.println(temp);
            	        			Map<String,String> pkstr = new HashMap<String,String>();
            	        			pkstr.put("pkid",temp.getString("pk"));
            	        			pklist.add(pkstr);
            	        			result = temp.getString("fields");
            	        			//System.out.println("fields " + result);
            	        	
            	        			JSONObject smalltemp;       			
            	        			try {
            							smalltemp = new JSONObject(result);
            							System.out.println("2015 smalltemp:"+smalltemp);
            							Map<String,Object> mMap = new HashMap<String,Object>();
            		        			mMap.put("content", "����: "+smalltemp.getString("description"));
            		        			mMap.put("time", "����ʱ��: "+smalltemp.getString("endTime"));
            		        			mMap.put("price","������: "+smalltemp.getString("price"));
            		        			mMap.put("mealPrice", "���˼۸�:"+smalltemp.getString("mealPrice"));
            		        			mMap.put("acceptBy",smalltemp.getString("acceptBy"));
            		        			mMap.put("postUserName", "��������:"+smalltemp.getString("postUserName"));
            		        			mMap.put("diningRoomName", "ʳ����:"+smalltemp.getString("diningRoomName"));
            		        			mMap.put("pk",pkstr.get("pkid"));
            		        			mMap.put("status",smalltemp.getString("status"));
            		        			System.out.println("2015 status:"+smalltemp.getString("status"));
            		        			
            		        			//mDataList.add(mMap);
            		        			tmpDataList.add(mMap);
            		        			//
            		        			showList.add(mMap);
            		        			
            		        			
            		        			
            		        			//adapter = 	            		        				            		    				
            		    				//System.out.println("����"+mDataList.get(i).get("content").toString());
            						} catch (JSONException e) {
            							// TODO Auto-generated catch block
            							e.printStackTrace();
            							handler.obtainMessage(4).sendToTarget();
            							System.out.println("strange:json�쳣1");
            						}
            	        			//System.out.println("item��ô"+mDataList.get(i).get("content").toString());
            	        		}
            	        		
            	        		//handler.obtainMessage(5).sendToTarget();
            	        		handler.obtainMessage(5, lt).sendToTarget();
            	        		handler.obtainMessage(6).sendToTarget();
            	        		
            	        	} catch (JSONException e1) {
            	        		// TODO Auto-generated catch block
            	        		e1.printStackTrace();
            	        		handler.obtainMessage(4).sendToTarget();
            	        		System.out.println("strange:json�쳣2");
            	        	}           
							//}
            	        	/*
            	        	ep.setAdapter(adapter);
            	        	adapter.notifyDataSetChanged();
            	        	*/
		    				
	            	    }catch (Exception e){
							e.printStackTrace();
							handler.obtainMessage(4).sendToTarget();
							System.out.println("strange:http�쳣");
							handler.obtainMessage(7).sendToTarget();
						}
	            		
					}
	            };
				hth.start();
				//System.out.println("hello666 ");     
			}catch(Exception e){
	        e.printStackTrace();
			}
		}
		//��ת����Ϣ
		public void NewsMainBtn(View v){
			Intent intent = new Intent();
			intent.setClass(MainPage.this, NewsActivity.class);
			startActivity(intent);
			MainPage.this.finish();
		}
		//��ת�����˽���
		public void PersonNewsMainBtn(View v){
			Bundle mbundlenow = new Bundle();
			mbundlenow.putString("name", javamainpersonnewsbtn.getText().toString());
			Intent intent = new Intent();
			intent.setClass(MainPage.this, PersonNews.class);
			intent.putExtras(mbundlenow);
			startActivity(intent);
			MainPage.this.finish();
		}
		
		@SuppressWarnings("deprecation")
		protected Dialog onCreateDialog(int id) {			
			switch (id) {
			case SELECT_A_CONTACT_DIALOG:
				return new AlertDialog.Builder(MainPage.this).setTitle("JIEDAN")
						.setNegativeButton("����", new DialogInterface.OnClickListener() {
							 
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//�鿴����
								System.out.println(temp_id);
								LookDetailAndroid(temp_id);						
							}
						}).setPositiveButton("�ӵ�", new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								AcceptOrderAndroid(temp_id);								
								//System.out.println("temp_id: "+temp_id);							
							}
						}).create();
			default:
				break;
			}
			return super.onCreateDialog(id);
		}
		@SuppressWarnings("deprecation")
		protected void onPrepareDialog(int id, Dialog dialog) {
			super.onPrepareDialog(id, dialog);
		}
		
		
		
		
		
		//���ܶ���
		public void AcceptOrderAndroid(final int itemid){
			try{
	            // ��������
	            Thread hth = new Thread(){
	            	@Override
	            	public void run(){
	            		//��get��������Ϣ����post��ȥ
	            		//System.out.println("hello111 ");       		          		
	            		HttpResponse response;
	            		try{	            			
	            			String url = baseurl + "ddmeal/index/accept/";	
	            			List <NameValuePair> params = new ArrayList <NameValuePair>();   //Post�������ͱ���������NameValuePair[]���鴢�� 
	            			int idnow = itemid;
	            			System.out.println("idnow:"+idnow);
	            			System.out.println("mDataList:"+mDataList);
	            			String orderid = mDataList.get(idnow).get("pk").toString();
	            			params.add(new BasicNameValuePair("id",orderid));
	            			//System.out.println("orderid:"+orderid);
	            			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params);
	            			HttpPost httpRequest = new HttpPost(url);
	            			//System.out.println("hello222 ");
	            			//header
	            			Header headers = new BasicHeader("Content-type","application/x-www-form-urlencoded");          
	            			httpRequest.setHeader(headers);
	            			Header headers1 = new BasicHeader("Accept","text/plain");
	            			httpRequest.setHeader(headers1);
	            			Header headers2 = new  BasicHeader("cookie","username="+mNamestr);
	            			httpRequest.setHeader(headers2);
	            			// �����������ݼ���������
	            			httpRequest.setEntity(requestHttpEntity);
	            			// ��Ҫ�ͻ��˶�������������
	            			HttpClient httpClient = new DefaultHttpClient();
	            			// ��������
	            			response = httpClient.execute(httpRequest);          			
	            			//System.out.println("hello333 ");   
	            			String nowstr = null;
	            	     //   if(response.getStatusLine().getStatusCode() == 200)   {
	            	        	nowstr = EntityUtils.toString(response.getEntity());   //��ȡ�ַ���
	            	        	//System.out.println("hello8 "+ nowstr);
	            	        	JSONObject jsonObject;
	            	        	try {
	            	        		jsonObject = new JSONObject(nowstr);
	            	        		String outstr = jsonObject.getString("error");
	            	        		
	            	        		//System.out.println("hello555 "+outstr);
	            	        		if(outstr.equalsIgnoreCase("false")){
	            	        			//���ܶ���
	            	        			hereList = mDataList.get(itemid);
	            	        			//System.out.println("�ѽ��ܶ���");
	            	        			
	            	        			//pklist.remove(itemid);
	            	        			
	            	        			//
	            	        			//handler.obtainMessage(12).sendToTarget();
	            	        			handler.obtainMessage(11).sendToTarget();
	            	        			
	            	        			temp_id = itemid;
	            	        			//handler.obtainMessage(1,temp_id).sendToTarget();
	            	        			handler.obtainMessage(100).sendToTarget();
	            	        			System.out.println("��ת�ɹ���?");
	            	        			//�޸�������	            	        				            	        			
	            	        		}
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
		//���Բ���handler
		Handler handler = new Handler(){
				public void handleMessage(android.os.Message msg){
						if(msg.what==100){
							Toast.makeText(getApplicationContext(), "�ӵ��ɹ�",Toast.LENGTH_SHORT).show();
						}		
						if(msg.what==1){
							LookDetailAndroid(temp_id);
						}
						if(msg.what==4){
							//fail
							Toast.makeText(getApplicationContext(), "����ˢ��ʧ��",Toast.LENGTH_SHORT).show();
						}
						if(msg.what==5){
							//succeed
							Toast.makeText(getApplicationContext(), "����ˢ�³ɹ�",Toast.LENGTH_SHORT).show();
							try {
								DB snappydb = DBFactory.open(getApplicationContext());
								snappydb.put("lasttime_all", (String)msg.obj);
								snappydb.put("flag_all", "1");
								snappydb.close();
							} catch (SnappydbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						if(msg.what==6){
							//update listview
							mergeDataList();
							
							/*
							ep.setAdapter(adapter);
    	        			adapter.notifyDataSetChanged();
    	        			*/
							
						}
						if(msg.what==7){
							//update listview offline
							mergeDataList();
							
							/*
							ep.setAdapter(adapter);
    	        			adapter.notifyDataSetChanged();
    	        			*/
							
						}
						if(msg.what==10){
							//can't get username
							Toast.makeText(getApplicationContext(), "���˳������µ�¼",Toast.LENGTH_SHORT).show();
						}
						
						if(msg.what==11){
							ep.setAdapter(adapter);//////////////////////////////////////////////////////////////////////////////today
    	        			adapter.notifyDataSetChanged();
						}
						if(msg.what==12){
							getOrdernow();
							mergeDataList();
						}
					};
				};
		//�鿴��������
		public void LookDetailAndroid(final int itemid){
			Map<String,Object> here = new HashMap<String,Object>();
			here = mDataList.get(itemid);
			Bundle mbundle = new Bundle();			
			mbundle.putString("postUserName", here.get("postUserName").toString());
			mbundle.putString("price", here.get("price").toString());
			mbundle.putString("content", here.get("content").toString());
			mbundle.putString("time", here.get("time").toString());
			mbundle.putString("mealPrice", here.get("mealPrice").toString());
			mbundle.putString("acceptBy", here.get("acceptBy").toString());
			//System.out.println(here);
			mbundle.putString("pk", here.get("pk").toString());
			//System.out.println("id:"+here.get("id").toString());
			
			//��ת
			Intent intent = new Intent();
			intent.setClass(MainPage.this, OrderInfoActivity.class);
			intent.putExtras(mbundle);
			startActivity(intent);
			MainPage.this.finish();
		}
}
