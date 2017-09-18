package com.example.administrator.watermarking;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import tools.FLAG;

/**
 * Created by Administrator on 2017-08-11.
 * 主要用于获取当前位置
 */
public class BdPosition {
    String TAG = BdPosition.class.getName();
    public LocationClient mLocationClient;
    private boolean isFirstLocate = true;
    public BdPosition(Context context) {
        //SDKInitializer.initialize(context);
        mLocationClient = new LocationClient(context);
        Log.v(TAG,"new the locationClient");
        mLocationClient.registerLocationListener(new MyLocationListener());
        Log.v(TAG,"register the locationListener");
        requestLocation();
    }
    public void stop(){
        mLocationClient.stop();
    }
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(3000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mLocationClient.setLocOption(option);
        Log.v(TAG,"init the option");
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                FLAG.LATITUDE = bdLocation.getLatitude();
                FLAG.LONGTITUDE = bdLocation.getLongitude();
                Log.v(TAG,"location change");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}

