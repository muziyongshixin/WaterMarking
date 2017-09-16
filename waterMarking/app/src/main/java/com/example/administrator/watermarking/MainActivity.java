package com.example.administrator.watermarking;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tools.Connection;
import tools.ConnectionData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView uploadAsset;
    TextView viewAsset;
    TextView history;
    TextView userWriteOff;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadAsset =(TextView)findViewById(R.id.main_upload_asset);
        viewAsset = (TextView)findViewById(R.id.main_asset);
        history = (TextView)findViewById(R.id.main_history);
        userWriteOff = (TextView)findViewById(R.id.main_write_off);
        intent = new Intent();
    }
    protected  void onResume(){
        super.onResume();
    }
    protected  void onPause(){
        super.onPause();
    }
    protected  void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // 包括上传，历史记录，总资产的显示
        switch (v.getId()){
            case R.id.main_upload_asset:
                intent.setClass(MainActivity.this,UploadAssert.class);
                startActivity(intent);
                break;
            case R.id.main_asset:
                intent.setClass(MainActivity.this,AssetActivity.class);
                break;
            case R.id.main_history:
                break;
            case R.id.main_write_off:
                break;
        }
    }
}
