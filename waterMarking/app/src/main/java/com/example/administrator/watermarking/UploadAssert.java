package com.example.administrator.watermarking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;

import java.io.IOException;

import tools.Connection;
import tools.ConnectionData;
import tools.FLAG;

/**
 * Created by Administrator on 2017-08-12.
 */
public class UploadAssert  extends AppCompatActivity implements View.OnClickListener{
    ImageView imageView ;
    Button takePhotoBtn ;
    Button choseFromGallery;
    EditText assertId;
    EditText assertValue;
    EditText assertContent;
    Button uploadBtn;
    ConnectionData sendData;
    Connection connection;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            ConnectionData receiveData = (ConnectionData)msg.obj;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assert);
        //界面控件初始化
        imageView = (ImageView)findViewById(R.id.assert_photo);
        takePhotoBtn = (Button)findViewById(R.id.btn_take_photo);
        choseFromGallery = (Button)findViewById(R.id.btn_select_gallery);
        assertId = (EditText)findViewById(R.id.assert_id_edt);
        assertValue = (EditText)findViewById(R.id.assert_value_edt);
        assertContent = (EditText)findViewById(R.id.assert_description_content);
        uploadBtn = (Button)findViewById(R.id.btn_assert_upload);
        //传输类数据的初始化
        sendData = new ConnectionData();
        try {
            connection = Connection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FLAG.FLAG_HANDLER = mHandler;
        connection.setHandler();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //拍摄图片
            case R.id.btn_take_photo:{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
                break;
            }
            //相册中选择
            case R.id.btn_select_gallery:{
                break;
            }
            //资产上传
            case R.id.btn_assert_upload:{
                String assertNo = assertId.getText().toString();
                String assertContentString = assertContent.getText().toString();
                String assertValueString = assertValue.getText().toString();
                JSONObject uploadJson = new JSONObject();
                try {
                    uploadJson.put("assertNo",assertNo);
                    uploadJson.put("assertValue",assertValueString);
                    uploadJson.put("assertContent",assertContentString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }
            default:{
                break;
            }
        }
    }
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK)
    }
}
