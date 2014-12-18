package com.jch.thesis.HttpUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 2014/12/6.
 */
public class HttpUtil {


    /**
     * 利用HttpURLConnection网络连接.
     *
     * @param urlStr
     * @return
     */
    public static String HttpRequset(String urlStr) {

        String resultData = "";
        URL url = null;
        try {
            url = new URL(urlStr);      //创建URL对象.
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //声明HttpURLConnection 对象.
        HttpURLConnection urlConnection = null;
        InputStreamReader in = null;        //声明InputSreamReader对象.
        BufferedReader bufferedReader = null;
        String inputLine = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new InputStreamReader(urlConnection.getInputStream());
            bufferedReader = new BufferedReader(in);
            while ((inputLine = bufferedReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        return resultData;
    }


    /**
     * 可以上传数据的httpConnection请求.
     *
     * @param urlStr
     * @param outStr
     * @return
     */
    public static String HttpRequest(String urlStr, String outStr) {

        URL url = null;
        String resultLine = null;

        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        InputStreamReader in = null;

        BufferedReader buffer = null;

        String inputLine = null;
        //
        DataOutputStream out = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //因为是post请求，所以要设置
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            //post 请求.
            urlConnection.setRequestMethod("POST");
            //post请求不能设置缓存.
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(false);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成
            // 要注意的是connectio.getOutputStream会隐含的进行connect
            urlConnection.connect();
            // DataOutputStream流
            out = new DataOutputStream(urlConnection.getOutputStream());
            String content = "par=" + URLEncoder.encode(outStr, "gb2312");
            // 将要上传的内容写入流中
            out.writeBytes(content);

            //获得内容流。
            in = new InputStreamReader(urlConnection.getInputStream());
            //创建buffer对象,输出时候用到.
            buffer = new BufferedReader(in);
            //使用循环读取数据
            while ((inputLine = buffer.readLine()) != null) {

                resultLine += inputLine + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                in.close();
                urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return resultLine;

    }


    /**
     * 通过HttpGet方式请求。
     *
     * @param urlStr
     * @return
     */
    public static String HttpRequsetGet(String urlStr, HttpParams httpParams) {

        String resultStr = null;

        //http连接对象.
        HttpGet httpRequest = new HttpGet(urlStr);

        // 取得httpClient对象。
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        //请求httpClient, 取得HttpResponse
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);

            //请求成功.
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //取得返回的字符串.
                String result = EntityUtils.toString(httpResponse.getEntity());

                //这个返回值可能会在行尾出现小方格.可过滤掉回车符.
                resultStr = resultStr.replace("\r", "");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * httpPost 请求。
     *
     * @param urlStr
     * @param params
     * @return
     */
    public static String HttpRequestPost(String urlStr, HttpParams params) {

        String resultStr = null;
        //创建httpPost对象.
        HttpPost httpRequest = new HttpPost(urlStr);
        //使用NameValuePair来保存要传递的Post参数.
        List param = new ArrayList();

        //添加要传递的参数.
        param.add(new BasicNameValuePair("par", "HttpClient_android_Post"));

        try {
            //设置字符集
            HttpEntity httpEntity = new UrlEncodedFormEntity(param, "gb2312");
            //请求httpRequest
            httpRequest.setEntity(httpEntity);
            //取得默认的HttpClient
            HttpClient httpClient = new DefaultHttpClient(params);
            //取的HttpResponse
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            //是否连接成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                // 取的返回的字符串
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                // 这个返回值可能会在行尾出现小方格
                // 在TextView要显示的文字过滤掉回车符（"\r"）就可以正常显示了。
                resultStr = strResult.replace("\r", "");

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultStr;
    }


}
