package com.Sislab.MagicSpoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Bluetooth_Activities extends Fragment {
    SwitchCompat switchCompat;
    BluetoothAdapter bluetoothAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_bluetooth__activities,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        switchCompat = getView().findViewById(R.id.bluetooth_status);
        if(bluetoothAdapter == null){
            Toast.makeText(getActivity().getApplicationContext(), "Bluetooth does not support on this device", Toast.LENGTH_SHORT).show();
        }
        else if (bluetoothAdapter.isEnabled()) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Bluetooth does not support on this device", Toast.LENGTH_SHORT).show();
                }else {
                    if (switchCompat.isChecked()) {
                            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                    } else {
                        bluetoothAdapter.disable();
                    }
                }
            }
        });
    }
}
