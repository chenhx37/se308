package com.example.dididafan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
	
	/**
     * @param citiesString    �ӷ������˵õ���JSON�ַ�������
     * @return    ����JSON�ַ������ݣ�����List����
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
	/*post�������������ж��û��Ƿ���� */
	/*String psdstr = javamainLoginpsd.getText().toString();
	String url = baseurl + "login?username="+namestr+"&password="+psdstr;
	HttpGet httpGet = new HttpGet(url);
	HttpClient httpClient = new DefaultHttpClient();
	//��������
	try{
		HttpResponse response = httpClient.execute(httpGet);
		// ��ʾ��Ӧ
        showResponseResult(response);// һ��˽�з���������Ӧ�����ʾ����
        //get������ת
        
	}catch (Exception e){
		e.printStackTrace();
	}*/
}
