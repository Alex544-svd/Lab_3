package com.alexandr.myagkov_3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {
    private final BluetoothAdapter bluetoothAdapter;
    private final BroadcastReceiver receiver;
    private final Set<BluetoothDevice> listOfDevs = new HashSet<>();

    {
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listOfDevs.add(device);
                    refreshList();
                }

            }
        };

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    private void refreshList() {
        EditText list = (EditText) findViewById(R.id.list);
        list.setText("");
        for (BluetoothDevice dev : listOfDevs) {
            list.append("Имя устройства: " + dev.getName() + "\n");
            list.append("Адрес устройства: " + dev.getAddress() + "\n");
            list.append("\n************\n");
        }
    }

    public void onClick(View view) {
        EditText list = (EditText) findViewById(R.id.list);

        if (bluetoothAdapter == null) {
            list.setText("Устройство не поддерживает блютуз");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            list.setText("блютуз выключен");
            return;
        }

        bluetoothAdapter.startDiscovery();
        list.setText("поиск начат!");
    }
}