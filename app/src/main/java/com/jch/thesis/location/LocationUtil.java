package com.jch.thesis.location;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by ACER on 2014/12/6.
 */
public class LocationUtil {

    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;

    public interface LocalBack {
        public void getLocal(String localInfo);
    }

    private LocationClient locationClient;
    public Activity activity;

    public LocalBack localBack;

    public LocationUtil(Activity activity, LocalBack localBack) {
        this.activity = activity;
        this.localBack = localBack;

    }


    public class SItude {
        public String latitude;
        public String longitude;
    }

    public class SCell {
        public int MCC;
        public int MNC;
        public int LAC;
        public int CID;
    }

    public void stopLocationClient() {
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }

    private void getLocalInfo() {

        locationClient = new LocationClient(activity);

        // 设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("LocationDemo"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);


        // 注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                sb.append("\nError code : ");
                sb.append(location.getLocType());
                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nRadius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\nSpeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nSatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());
                }
                LOCATION_COUTNS++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));
                if (localBack != null) {
                    localBack.getLocal(sb.toString());
                }

                Log.v("Local service", "get location success.");
            }

            @Override
            public void onReceivePoi(BDLocation location) {
            }

        });

        locationClient.start();
        if (locationClient != null && locationClient.isStarted())
            locationClient.requestLocation();
        try {
            SItude ss = getItude(getCellInfo());
            if (localBack != null) {
                localBack.getLocal("latitude:" + ss.latitude + "-" + "longitude:" + ss.longitude);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 通过基站信息获取经纬度
     *
     * @param cell
     * @return
     * @throws Exception
     */
    public SItude getItude(SCell cell) throws Exception {
        SItude itude = new SItude();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://www.google.com/loc/json");
        try {
            JSONObject holder = new JSONObject();
            holder.put("version", "1.1.0");
            holder.put("host", "maps.google.com");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);
            holder.put("radio_type", "gsm");
            holder.put("carrier", "HTC");

            JSONObject tower = new JSONObject();
            tower.put("mobile_country_code", cell.MCC);
            tower.put("mobile_network_code", cell.MNC);
            tower.put("cell_id", cell.CID);
            tower.put("location_area_code", cell.LAC);

            JSONArray towerarray = new JSONArray();
            towerarray.put(tower);
            holder.put("cell_towers", towerarray);

            StringEntity query = new StringEntity(holder.toString());
            post.setEntity(query);

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }
            JSONObject json = new JSONObject(strBuff.toString());
            JSONObject subjosn = new JSONObject(json.getString("location"));

            itude.latitude = subjosn.getString("latitude");
            itude.longitude = subjosn.getString("longitude");

            Log.i("Itude", itude.latitude + itude.longitude);

        } catch (Exception e) {
            Log.e(e.getMessage(), e.toString());
            throw new Exception("" + e.getMessage());
        } finally {
            post.abort();
            client = null;
        }

        return itude;
    }

    /**
     * 获取基站信息
     *
     * @return
     * @throws Exception
     */
    public SCell getCellInfo() throws Exception {
        SCell cell = new SCell();
        TelephonyManager mTelNet = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
        if (location == null)
            throw new Exception("基站信息为空");
        String operator = mTelNet.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int cid = location.getCid();
        int lac = location.getLac();
        cell.MCC = mcc;
        cell.MNC = mnc;
        cell.LAC = lac;
        cell.CID = cid;
        return cell;
    }
}
