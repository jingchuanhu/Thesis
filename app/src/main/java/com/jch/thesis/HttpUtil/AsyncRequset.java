package com.jch.thesis.HttpUtil;

import android.os.AsyncTask;

import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 异步请求.
 * Created by jch on 2014/12/6.
 */
public class AsyncRequset extends AsyncTask<String, String, String> {

    public interface AsyncCallback {
        public void callback(String string);
    }

    private String urlStr = null;

    private AsyncCallback asyncCallback = null;

    private HttpParams params;

    private File uploadFile;

    /**
     * @param urlStr
     * @param params
     * @param asyncCallback
     */
    public AsyncRequset(String urlStr, HttpParams params, AsyncCallback asyncCallback) {
        this.asyncCallback = asyncCallback;
        this.urlStr = urlStr;
        this.params = params;
    }

    /**
     * @param urlStr
     * @param params
     * @param asyncCallback
     */
    public AsyncRequset(String urlStr, HttpParams params, AsyncCallback asyncCallback, File uploadFile) {
        this.asyncCallback = asyncCallback;
        this.urlStr = urlStr;
        this.params = params;
        this.uploadFile = uploadFile;
    }


    @Override
    protected String doInBackground(String[] params) {
        if (uploadFile != null) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(uploadFile));
                char[] readChars = new char[(int) uploadFile.length()];
                reader.read(readChars);
                HttpUtil.HttpRequest(urlStr, readChars.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return HttpUtil.HttpRequsetGet(urlStr, this.params);        //异步任务执行网络请求。

    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);

        asyncCallback.callback(o);
    }
}
