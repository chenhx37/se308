package com.example.dididafan;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	/**
     * @param path    ����ķ�����URL��ַ
     * @param encode    �����ʽ
     * @return    ���������˷��ص�����ת����String
     */
    public static String sendPostMessage(String path, String encode)
    {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpPost httpPost = new HttpPost(path);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                if(httpEntity != null)
                {
                    result = EntityUtils.toString(httpEntity, encode);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            httpClient.getConnectionManager().shutdown();
        }
        
        return result;
    }
}
