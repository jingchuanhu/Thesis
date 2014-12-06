package com.jch.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jch.util.AsyncRequset;
import com.jch.util.Constant;

import org.apache.http.params.BasicHttpParams;

public class ArithmeticService extends Service {

    public ArithmeticService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }


    private final IRemote.Stub mBinder = new IRemote.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {


            int resultInt = a + b;

            Log.v(Constant.LOG_NAME, "remote add method");

            sendLocalInfo(String.valueOf(resultInt));
            return resultInt;
        }

        @Override
        public void sendLocal(String local) throws RemoteException {

            Log.v(Constant.LOG_NAME, "remote sendLocal method");
            sendLocalInfo(local);

        }

        @Override
        public void sendCustomLocal(Location local) throws RemoteException {

        }

    };

    /**
     * 向服务器发送位置信息。
     *
     * @param str
     */
    private void sendLocalInfo(String str) {

        BasicHttpParams params = new BasicHttpParams();
        params.setParameter("param", str);

        new AsyncRequset(Constant.HTTP_URL, params, new AsyncRequset.AsyncCallback() {
            @Override
            public void callback(String string) {
                Log.v(Constant.LOG_NAME, "response str--:" + string);
            }
        }).execute();

    }
}
