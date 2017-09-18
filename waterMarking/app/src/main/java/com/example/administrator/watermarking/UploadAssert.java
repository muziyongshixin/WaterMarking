package com.example.administrator.watermarking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tools.Connection;
import tools.Encrypt;
import tools.FLAG;

/**
 * Created by Administrator on 2017-08-12.
 */
public class UploadAssert  extends AppCompatActivity implements View.OnClickListener{
    String TAG = UploadAssert.class.getName();
    int GET_IMAGE_CAMERA = 1;
    ImageView imageView ;
    Button takePhotoBtn ;
    Button choseFromGallery;
    EditText assertId;
    EditText assertValue;
    EditText assertContent;
    Button uploadBtn;
    JSONObject sendData;
    Connection connection;

    BdPosition bdPosition;
    File uploadFile = null;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            JSONObject receiveData = (JSONObject) msg.obj;
            if(receiveData.has("state")){
                try {
                    if(receiveData.getString("state").equals("successful")){
                        Toast.makeText(UploadAssert.this,"上传成功",Toast.LENGTH_SHORT).show();
                    }else{
                        String wrongInfo = receiveData.getString("wrongInfo");
                        Toast.makeText(UploadAssert.this,"上传失败"+ wrongInfo,Toast.LENGTH_SHORT).show();
                        Toast.makeText(UploadAssert.this,"请重新上传",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UploadAssert.this,"上传出现异常"+e.toString() ,Toast.LENGTH_SHORT).show();
                }
            }

         }
    };
    File picFileDir;
    File picFile;

    Bitmap picMap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assert);
        //界面控件初始化
        imageView = (ImageView)findViewById(R.id.assert_photo);
        takePhotoBtn = (Button)findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        assertId = (EditText)findViewById(R.id.assert_id_edt);
        assertValue = (EditText)findViewById(R.id.assert_value_edt);
        assertContent = (EditText)findViewById(R.id.assert_description_content);
        uploadBtn = (Button)findViewById(R.id.btn_assert_upload);
        uploadBtn.setOnClickListener(this);
        //传输类数据的初始化
        sendData = new JSONObject();
        connection = Connection.getConnection();
        FLAG.FLAG_HANDLER = mHandler;
        connection.setHandler();

        picFileDir  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"waterMarking");
        //FIXME 关于位置的获取使用百度地图还是有些问题！！！
       try{
           bdPosition = new BdPosition(this);
       }catch (Exception e){
           e.printStackTrace();
           Log.v("Location","happend exception" + e.toString());
       }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //拍摄图片
            case R.id.btn_take_photo:{

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //TODO 写入到文件中的做法是否正确还需要商榷
                uploadFile = getOutputFile();
                Uri u = Uri.fromFile(uploadFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,u);
                startActivityForResult(intent,GET_IMAGE_CAMERA);
               /* picMap = BitmapFactory.decodeFile(uploadFile.getPath());
                imageView.setImageBitmap(picMap);
                findViewById(R.id.take_photo_layout).setVisibility(View.INVISIBLE);*/
                break;
            }
            //资产上传
            case R.id.btn_assert_upload:{

                //TODO 这里资产不应该有资产的编号，应该是新的资产由服务器分配,这里仅作测试用
                String assertNo = assertId.getText().toString();
                String assertContentString = assertContent.getText().toString();
                String assertValueString = assertValue.getText().toString();
                String position = String.valueOf(FLAG.LATITUDE) + "," + String.valueOf(FLAG.LONGTITUDE);
                String picInfo = assertNo+","+ assertContentString + "," + assertValueString + "," + position;
                Log.v(TAG,"get info is "+picInfo);
                //TODO 将图片信息进行加密
                //String encrptInfo = Encrypt.makeEncrypt(FLAG.TOKEN,picInfo);
                    //将加密的信息嵌入到图片中去
                   // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //picMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                if(picMap == null){
                    Toast.makeText(this,"图片为空",Toast.LENGTH_SHORT).show();
                    Log.v(TAG,"save file is null");
                }else {
                    byte[] picAinfo = null;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    picMap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    picAinfo = baos.toByteArray();
                    if(picAinfo != null) {
                        //TODO 这里需要使用嵌入算法将信息嵌入进去，下面知识一个测试通信代码
                        //byte[] picAinfo = Encrypt.EmbedInfo(baos.toByteArray(),encrptInfo);
                        //byte[] picAinfo = baos.toByteArray();
                        //通过base64来将图片信息转换成字符串信息
                        String picData = Base64.encodeToString(picAinfo,Base64.DEFAULT);
                        //开始上传图片

                        JSONObject uploadJson = new JSONObject();
                        try {
                            uploadJson.put("function", FLAG.FUNCTION[1]);
                            uploadJson.put("token", FLAG.TOKEN);
                            uploadJson.put("pic", picData);
                            /*uploadJson.put("assertNo",assertNo);
                            uploadJson.put("assertValue",assertValueString);
                            uploadJson.put("assertContent",assertContentString);*/
                            connection.setData(uploadJson);
                            connection.startSend();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.v(TAG,"read data is null");
                    }
                }
                break;
            }
            default:{
                break;
            }
        }
    }
    File getOutputFile(){
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            picFile = new File(picFileDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            Log.v(TAG,picFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return picFile;
    }
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
                //Uri u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getCon))
            //picMap = BitmapFactory.decodeFile(uploadFile.getPath(),null);
            if(requestCode == GET_IMAGE_CAMERA) {
                try {
                    FileInputStream fis = new FileInputStream(uploadFile.getPath());
                    picMap = BitmapFactory.decodeStream(fis);
                    imageView.setImageBitmap(picMap);
                    findViewById(R.id.take_photo_layout).setVisibility(View.INVISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
