package com.jch.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

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
            return (a + b);
        }
    };
}
