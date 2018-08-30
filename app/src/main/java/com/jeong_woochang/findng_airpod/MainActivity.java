package com.jeong_woochang.findng_airpod;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        appData = getSharedPreferences("appData", MODE_PRIVATE);

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

        filter=new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(new BTStateChangedBroadcastReceiver(), filter);

        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.dialog_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
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
                builder.create().show();
            }
        });

    }

    private void save(Log data) {
        logs.clear();
        logs.add(data);
        if(load()!=null) {
            for (Log log : load())
                logs.add(log);
        }
        Gson gson=new Gson();
        String json=gson.toJson(logs);
        System.out.println(json);
        SharedPreferences.Editor editor=appData.edit();
        editor.putString("log", json);
        editor.commit();
        reply();
    }

    private ArrayList<Log> load() {
        Type listType = new TypeToken<ArrayList<Log>>(){}.getType();
        ArrayList<Log> datas;
        String json=appData.getString("log", null);
        Gson gson=new Gson();
        datas=gson.fromJson(json, listType);
        return datas;
    }

    private void reply() {
        logAdapter.clear();
        if (logs!=null) {
            for (Log log : logs)
                logAdapter.addItem(log.getName(), log.getState(), log.getLat(), log.getLng());
            logAdapter.notifyDataSetChanged();
        }
    }
}
