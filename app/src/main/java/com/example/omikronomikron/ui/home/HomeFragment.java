package com.example.omikronomikron.ui.home;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.omikronomikron.R;
import com.example.omikronomikron.receivers.ConnectionLiveData;
import com.example.omikronomikron.receivers.ConnectionModel;
import com.example.omikronomikron.utils.UtilsClass;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class HomeFragment extends Fragment {

    ConnectionLiveData connectionLiveData;
    private static final int PERMISSION_REQUEST_CODE = 200;
    UtilsClass utilsClass;

    public HomeFragment(){

    }

    public static HomeFragment getInstance(){

        return new HomeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        utilsClass = new UtilsClass();

        if (!utilsClass.checkPermission(getContext())){
            requestPermission();
        }else{
            Toast.makeText(getContext(),"Permission already granted.",Toast.LENGTH_LONG).show();
        }

        connectionLiveData =  new ConnectionLiveData(getActivity());
        connectionLiveData.observe(getActivity(), new Observer<ConnectionModel>() {
            @Override
            public void onChanged(@Nullable ConnectionModel connection) {
                if (connection.getIsConnected()) {
                    Toast.makeText(getActivity(), String.format("Wifi turned ON"),         Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), String.format("Connection turned OFF"), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

//    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);
//        int result1 = ContextCompat.checkSelfPermission(getContext(), CAMERA);
//
//        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//    }

    private void requestPermission() {
        requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        Toast.makeText(getContext(), "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}