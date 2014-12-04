package com.jch.thesis.AidlClient;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jch.aidlserver.IRemote;
import com.jch.thesis.R;

public class AidlClientActivity extends Activity {

    private IRemote mService = null;
    private ServiceConnection mServiceConnection = null;
    private TextView resultText;
    private EditText firstValue;
    private EditText secondValue;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);

        initConnection();
        initialize();
    }

    private void initConnection() {

        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                mService = IRemote.Stub.asInterface(service);
                Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding is done - Service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                mService = null;
                Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding - Service disconnected");
            }
        };

        if (mService == null) {
            Intent it = new Intent();
            it.setAction("com.remote.service.CALCULATOR");

            bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void initialize() {

        resultText = (TextView) findViewById(R.id.resultText);
        firstValue = (EditText) findViewById(R.id.firstValue);
        secondValue = (EditText) findViewById(R.id.secondValue);
        add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = Integer.valueOf(firstValue.getText().toString());
                int b = Integer.valueOf(secondValue.getText().toString());

                try {
                    resultText.setText("Result -> Add ->" + mService.add(a, b));
                    Log.d("IRemote", "Binding - Add operation");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
