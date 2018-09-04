package com.jeong_woochang.findng_airpod;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    RecyclerView log;
    ImageView clear;
    LogAdapter logAdapter;
    SharedPreferences appData;
    ArrayList<Log> logs;
    IntentFilter filter;
    private boolean isAccessFineLocation=false;
    private boolean isPermission=false;
    private boolean isAccessCoarseLocation=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        callPermission();

        //SharedPreferences Instance
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        //RecyclerView Adapter Init
        log = findViewById(R.id.log);
        logAdapter = new LogAdapter(new ArrayList<Log>());
        log.setAdapter(logAdapter);
        log.setLayoutManager(new LinearLayoutManager(this));
        logs=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                logs=load();
                reply();
            }
        }).start();

        //Clear Button Init
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.dialog_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                logs.clear();
                                logAdapter.clear();
                                appData.edit().clear().commit();
                                logAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                return;
                            }
                        });
                if (logs!=null)
                    if (!logs.isEmpty())
                        builder.create().show();
            }
        });
    }

    private ArrayList<Log> load() {
        Type listType = new TypeToken<ArrayList<Log>>(){}.getType();
        ArrayList<Log> datas;
        String json=appData.getString("log", null);
        Gson gson=new Gson();
        datas=gson.fromJson(json, listType);
        System.out.println(datas);
        return datas;
    }

    public void reply() {
        logAdapter.clear();
        if (logs!=null) {
            for (Log log : logs)
                logAdapter.addItem(log.getName(), log.getState(), log.getTime(), log.getLat(), log.getLng());
            logAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("send_log"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (logs!=null)
                logs.clear();
            logs=load();
            reply();
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == 404
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    404);
        } else {
            isPermission = true;
        }
    }
}
