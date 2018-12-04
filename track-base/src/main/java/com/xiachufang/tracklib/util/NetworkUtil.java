package com.xiachufang.tracklib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xiachufang.tracklib.TrackManager;


/**
 * Created by drizzle on 2016/10/26.
 */
public class NetworkUtil {

    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * wifi是否打开
     */
    public static boolean isWifiEnabled(Context context) {
        try {
            ConnectivityManager mgrConn = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mgrConn != null) {
                TelephonyManager mgrTel = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                        .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                        .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前网络是否是wifi网络
     */
    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前网络是否为移动网络
     */
    public static boolean isMobile(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int UNKNOWN = 3;

    private static int getNetworkState() {
        if (connectionType != -1) {
            return connectionType;
        }
        if (TrackManager.getContext() == null) {
            return UNKNOWN;
        }
        //获取系统的网络服务
        ConnectivityManager connManager;
        try {
            connManager = (ConnectivityManager) TrackManager.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }catch (Throwable t){
            return UNKNOWN;
        }
        //如果当前没有网络
        if (null == connManager)
            return UNKNOWN;

        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return UNKNOWN;
        }

        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    connectionType = 2;
                    return connectionType; //如果网络连接为wifi
                }
        }

        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            connectionType = 4;//2g网络
                            break;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            connectionType = 5;//3g网络
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            connectionType = 6;//如果是4g类型
                            break;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                connectionType = 5;
                            } else {
                                connectionType = UNKNOWN;
                            }
                            break;
                    }
                }
        }

        return connectionType;
    }


    public static int getConnectionType(){
        try {
            return getNetworkState();
        }catch (Throwable t){
            t.printStackTrace();
        }
        return UNKNOWN;
    }
    private static int connectionType = -1;

    private static String networkOperatorCode;

    /**
     * 获取网络运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 运营商名称
     */
    public static String getNetworkOperatorCode() {
        if(!TextUtils.isEmpty(networkOperatorCode)){
            return networkOperatorCode;
        }
        if(TrackManager.getContext() == null){
            return "";
        }
        try{
            TelephonyManager tm = (TelephonyManager) TrackManager.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if(tm != null){
                networkOperatorCode  = tm.getSimOperator();
                if(TextUtils.isEmpty(networkOperatorCode) || !TextUtils.isDigitsOnly(networkOperatorCode)){
                    networkOperatorCode =  "UNKNOWN";
                }
            }
        }catch(Throwable t){
            t.printStackTrace();
        }
        return networkOperatorCode;
    }

}
