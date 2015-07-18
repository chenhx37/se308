package com.example.dididafan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
//订单详情信息
public class OrderInfoActivity extends Activity{
	private TextView contenttv;
	private TextView pricetv;
	private TextView timetv;
	private TextView accepttv;
	private TextView posttv;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderinfo);
		contenttv = (TextView)findViewById(R.id.OIshowcontent);
		pricetv = (TextView)findViewById(R.id.OIshowprice);
		timetv = (TextView)findViewById(R.id.OIshowtime);
		accepttv = (TextView)findViewById(R.id.OIshowAcceptpeople);
		posttv = (TextView)findViewById(R.id.OIshowPostpeople);
		Bundle herebundle = this.getIntent().getExtras();
		contenttv.setText(herebundle.getString("content"));
		pricetv.setText(herebundle.getString("price"));
		timetv.setText(herebundle.getString("time"));
		accepttv.setText("接收者是:"+herebundle.getString("acceptBy"));
		posttv.setText("发布者是:"+herebundle.getString("postUserName"));
		
	}
	//跳转到主页
	public void OrderInfoMPBtnClick(View v){
		Intent intent = new Intent();
		intent.setClass(OrderInfoActivity.this, MainPage.class);
		startActivity(intent);
	}
	//跳转到发布
	public void OrderInfoPublishBtnClick(View v){
		Intent intent = new Intent();
		intent.setClass(OrderInfoActivity.this, PublishActivity.class);
		startActivity(intent);
	}
	//跳转到消息
	public void OrderInfoNewsBtnClick(View v){
		Intent intent = new Intent();
		intent.setClass(OrderInfoActivity.this, NewsActivity.class);
		startActivity(intent);
	}
}
