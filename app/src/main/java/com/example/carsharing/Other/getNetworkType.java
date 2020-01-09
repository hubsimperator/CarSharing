package com.example.carsharing.Other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class getNetworkType {

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }
    public static String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        if(isConnectedWifi(context)){
            return "WiFi";
        }
        else {
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return "słaba: typ GPRS";
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "słaba: typ EDGE";
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return "słaba: typ CDMA";
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "słaba: typ 1xRTT";
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return "słaba: typ UMTS";
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return "słaba: typ AVDO 0";
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "słaba: typ AVDO A";
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return "słaba: typ HSDPA";
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "słaba: typ HSUPA";
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return "słaba: typ HSPA";
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return "słaba: typ EVDO B";
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return "słaba: typ EHRPD";
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "LTE";
                default:
                    return "Nie rozpoznano";
            }
        }
    }
}
