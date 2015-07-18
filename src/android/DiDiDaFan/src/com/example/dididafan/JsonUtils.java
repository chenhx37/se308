package com.example.dididafan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
	
	/**
     * @param citiesString    从服务器端得到的JSON字符串数据
     * @return    解析JSON字符串数据，放入List当中
     */
    public static String parseCities(String citiesString)
    {
        String errorstr = null;
        
        try
        {
            JSONObject jsonObject = new JSONObject(citiesString);
           // JSONArray jsonArray = jsonObject.getJSONArray("error");
            errorstr = jsonObject.getString("error");
          //  for(int i = 0; i < jsonArray.length(); i++)
          //  {
               // cities=jsonArray.toString();
            //}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return errorstr;
    }
	/*post给服务器进行判断用户是否存在 */
	/*String psdstr = javamainLoginpsd.getText().toString();
	String url = baseurl + "login?username="+namestr+"&password="+psdstr;
	HttpGet httpGet = new HttpGet(url);
	HttpClient httpClient = new DefaultHttpClient();
	//发送请求
	try{
		HttpResponse response = httpClient.execute(httpGet);
		// 显示响应
        showResponseResult(response);// 一个私有方法，将响应结果显示出来
        //get到才跳转
        
	}catch (Exception e){
		e.printStackTrace();
	}*/
}
