package com.example.omikronomikron.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class UtilsClass {

    public boolean checkPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(context, CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}
