package com.robotqt.robotqt.Utils;

import com.google.gson.Gson;
import com.robotqt.robotqt.bean.ChatMessage;
import com.robotqt.robotqt.bean.Result;
import com.robotqt.robotqt.exception.CommonException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Date;

/**
 * Created by Administrator on 2015/10/21.
 */
public class HttpUtils  {
    private static String API_KEY = "18de8a202e91cd8275ea89184da40275";
    private static String URL = "http://www.tuling123.com/openapi/api";

    /**
     * 发送一个消息，并得到返回的消息 
     * @param msg
     * @return
     */
    public static ChatMessage sendMsg(String msg){
        ChatMessage message=new ChatMessage();
        String url=setParams(msg);
        String res=doGet(url);
        Gson gson=new Gson();
        Result result=gson.fromJson(res,Result.class);

        if(result.getCode()>400000||result.getText()==null||result.getText().trim().equals("")){
            message.setMsg("该功能有待开发....");

        }else {
            message.setMsg(result.getText());
        }
        message.setType(ChatMessage.Type.INPUT);
        message.setDate(new Date());

        return message;
    }

    /**
     * Get请求，获得返回数据
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr)
    {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try
        {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() ==200)
            {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1)
                {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else
            {
                throw new CommonException("服务器连接错误！");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new CommonException("服务器连接错误！");
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (baos != null)
                    baos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            conn.disconnect();
        }

    }


    /**
     * 拼接Url
     * @param msg
     * @return
     */
    private static String setParams(String msg) {
       try{
           msg= URLEncoder.encode(msg,"utf-8");

       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
        return URL+"?key="+API_KEY+"&info="+msg;
    }



}
