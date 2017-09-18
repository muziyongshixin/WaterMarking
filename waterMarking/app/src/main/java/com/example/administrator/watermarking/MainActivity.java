package com.example.administrator.watermarking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tools.FLAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userAccout;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userAccout  = (TextView)findViewById(R.id.main_account);
        userAccout.setText(FLAG.USERACCOUNT);
        findViewById(R.id.main_upload_assert).setOnClickListener(this);
        findViewById(R.id.main_assert).setOnClickListener(this);
        findViewById(R.id.main_history).setOnClickListener(this);
        findViewById(R.id.main_write_off).setOnClickListener(this);
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
            case R.id.main_upload_assert:
                intent.setClass(MainActivity.this,UploadAssert.class);
                startActivity(intent);
                break;
            case R.id.main_assert:
                intent.setClass(MainActivity.this,AssetActivity.class);
                startActivity(intent);
                break;
            case R.id.main_history:
                break;
            case R.id.main_write_off:
                break;
        }
    }
}
