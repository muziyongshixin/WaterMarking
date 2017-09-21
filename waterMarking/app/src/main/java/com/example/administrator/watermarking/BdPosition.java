package com.example.administrator.watermarking;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import tools.FLAG;

/**
 * Created by Administrator on 2017-08-11.
 * 主要用于获取当前位置
 */
public class BdPosition {
    String TAG = BdPosition.class.getName();
    public LocationClient mLocationClient;
    public BdPosition(Context context) {
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
    }
}

