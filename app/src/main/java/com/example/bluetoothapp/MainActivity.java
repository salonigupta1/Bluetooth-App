package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    Button button, turnon;
    ConstraintLayout layout;
    ImageView circle;

    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter arrayAdapter;

    ArrayList<String> items = new ArrayList<>();

    public void turnOn(View view){
        listView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        circle.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.drawable.back2);
        turnon.setVisibility(View.INVISIBLE);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action", action);

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                textView.setText("Finished");
                button.setEnabled(true);
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));

                items.add("Name: " + name + "Address: " + address + "RSSI: " + rssi );
                arrayAdapter.notifyDataSetInvalidated();

                //Log.i("Device found", "Name" + name + "Address" + address + "RSSI" + rssi);
            }
        }
    };

    public void search(View view){

        textView.setText("Searching........");
        button.setEnabled(false);

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        bluetoothAdapter.startDiscovery();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lsitView);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.searchButton);
        layout = findViewById(R.id.layout);
        turnon = findViewById(R.id.trunOn);
        circle = findViewById(R.id.circleimg);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(arrayAdapter);

         bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, intentFilter);

    }
}