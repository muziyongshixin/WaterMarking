package com.example.administrator.watermarking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new Mylocationlistener());
        SDKInitializer.initialize(getApplicationContext());
        mapView = (MapView)findViewById(R.id.bmMap);
        baiduMap.setMyLocationEnabled(true);
        positionText=(TextView)findViewById(R.id.location);
        requestLocation();
    }
    protected  void onResume(){
        super.onResume();
        mapView.onResume();
    }
    protected  void onPause(){
        super.onPause();
        mapView.onPause();
    }
    protected  void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }
    private void navigateTo(BDLocation location){
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }
    public class Mylocationlistener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
