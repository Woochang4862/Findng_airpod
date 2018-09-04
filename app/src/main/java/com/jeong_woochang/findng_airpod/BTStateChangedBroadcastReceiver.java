package com.jeong_woochang.findng_airpod;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jeong-woochang on 2018. 8. 30..
 */

public class BTStateChangedBroadcastReceiver extends BroadcastReceiver {

    private static GPSInfo gpsInfo;
    private static double latitude=0;
    private static double longitude=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Bluetooth Receiver Init
        setLocation(context);
        IntentFilter filter;
        filter=new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED); //연결됨
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED); //연결 해제됨
        context.getApplicationContext().registerReceiver(new BTStateChangedBroadcastReceiver(), filter); // Receiver 등록
        BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        System.out.println("#########onReceive#########");
        SharedPreferences appData= context.getSharedPreferences("appData", Context.MODE_PRIVATE);
        String state = intent.getAction();
        System.out.println(state);
        Type listType = new TypeToken<ArrayList<Log>>(){}.getType();
        ArrayList<Log> datas=new ArrayList<>();
        switch (state) {
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                datas.add(new Log(device.getName(),"Connected" , getCurrentTimeStamp(), String.valueOf(latitude), String.valueOf(longitude)));
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                datas.add(new Log(device.getName(),"Disconnected", getCurrentTimeStamp(), String.valueOf(latitude), String.valueOf(longitude)));
                break;
            default:
                return;
        }
        String json1=appData.getString("log", null);
        Gson gson=new Gson();
        ArrayList<Log> tmp=new ArrayList<>();
        if(gson.fromJson(json1, listType)!=null)
            tmp=gson.fromJson(json1, listType);
        for(Log log:tmp)
            datas.add(log);
        String json2=gson.toJson(datas);
        System.out.println(json2);
        SharedPreferences.Editor editor=appData.edit();
        editor.putString("log", json2);
        editor.commit();

        sendMessage(context);
    }

    //브로드 캐스트 보내기
    private void sendMessage(Context context) {
        Intent intent = new Intent("send_log");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void setLocation(Context context) {
        // 권한 요청을 해야 함

        gpsInfo = new GPSInfo(context);
        // GPS 사용유무 가져오기
        if (gpsInfo.isGetLocation()) {

            latitude = gpsInfo.getLatitude();
            longitude = gpsInfo.getLongitude();
        } else {
            System.out.println(gpsInfo.isGetLocation());
            // GPS 를 사용할수 없으므로
            gpsInfo.showSettingsAlert();
        }
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
