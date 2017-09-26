package com.example.administrator.watermarking;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tools.Connection;
import tools.FLAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userAccout;
    Intent intent;
    Connection connection;
    JSONObject sendData;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            JSONObject  serverInfo = (JSONObject)msg.obj;
            try {
                if(serverInfo.getString("state").equals(FLAG.STATE[0])){
                    String asset_no = serverInfo.getString("asset_no");
                    Bundle bundle = new Bundle();
                    bundle.putString("type","new");
                    bundle.putString("asset_no",asset_no);
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this,UploadAssert.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,serverInfo.getString("wrongInfo"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"exception",Toast.LENGTH_SHORT).show();
            }

        }
    };
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
        connection = Connection.getConnection();
        FLAG.FLAG_HANDLER = mHandler;
        connection.setHandler();
        sendData = new JSONObject();
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
                //封转jsong传递数据
                try {
                    sendData.put("token",FLAG.TOKEN);
                    sendData.put("function",FLAG.FUNCTION[5]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                connection.setData(sendData);
                connection.startSend();
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
