package com.jch.util;

import android.os.AsyncTask;

import org.apache.http.params.HttpParams;

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

    @Override
    protected String doInBackground(String[] params) {

        return HttpUtil.HttpRequsetGet(urlStr, this.params);        //异步任务执行网络请求。

    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);

        asyncCallback.callback(o);
    }
}
