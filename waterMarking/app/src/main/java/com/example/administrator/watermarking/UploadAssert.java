package com.example.administrator.watermarking;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.rey.material.widget.ProgressView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import tools.Connection;
import tools.EmbedInfo;
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
    Button button1;
    ProgressView circularBar;

    EditText assertId;
    EditText assertValue;
    EditText assertContent;
    JSONObject sendData;
    Connection connection;

    BdPosition bdPosition;

    //存储照片的位置
    File uploadFile = null;
    //记录本地照片的序号
    String picNo = null;
    Bundle bundle;
    String type;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            JSONObject receiveData = (JSONObject) msg.obj;
            if(receiveData.has("state")){
                try {
                    if(receiveData.getString("state").equals("successful")){
                        circularBar.stop();
                        circularBar.setVisibility(View.INVISIBLE);
                        button1.setText("完成");
                        button1.setVisibility(View.VISIBLE);
                        Toast.makeText(UploadAssert.this,"上传成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(UploadAssert.this,MainActivity.class);
                        startActivity(intent);
                    }else{
                        circularBar.stop();
                        circularBar.setVisibility(View.INVISIBLE);
                        button1.setText("失败");
                        button1.setVisibility(View.VISIBLE);
                        String wrongInfo = receiveData.getString("wrongInfo");
                        Toast.makeText(UploadAssert.this,"上传失败"+ wrongInfo,Toast.LENGTH_SHORT).show();
                        Toast.makeText(UploadAssert.this,"请重新上传",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    circularBar.stop();
                    circularBar.setVisibility(View.INVISIBLE);
                    button1.setText("失败");
                    button1.setVisibility(View.VISIBLE);
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
        Intent intent = getIntent();
        bundle = intent.getExtras();
        type = bundle.getString("type");
        //界面控件初始化
        imageView = (ImageView)findViewById(R.id.assert_photo);
        takePhotoBtn = (Button)findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        assertId = (EditText)findViewById(R.id.assert_id_edt);
        assertValue = (EditText)findViewById(R.id.assert_value_edt);
        assertContent = (EditText)findViewById(R.id.assert_description_content);
        button1 = (Button)findViewById(R.id.btn_assert_upload1);
        button1.setOnClickListener(this);
        circularBar = (ProgressView)findViewById(R.id.progress_pv_circular);
        //资产编号要么是数据库获得，要么是activity传递
        assertId.setText(bundle.getString("asset_no"));
        assertId.setInputType(InputType.TYPE_NULL);
        //如果是修改的资产，传进来的数据不能修改
        if(type.equals("add")){
            assertContent.setText(bundle.getString("asset_desc"));
            assertContent.setInputType(InputType.TYPE_NULL);
            assertValue.setText(bundle.getString("asset_money"));
            assertValue.setInputType(InputType.TYPE_NULL);
        }
        //传输类数据的初始化
        sendData = new JSONObject();
        connection = Connection.getConnection();
        FLAG.FLAG_HANDLER = mHandler;
        connection.setHandler();

        picFileDir  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"waterMarking");
        //FIXME 关于位置的获取使用百度地图还是有些问题！！！
        Context context = getApplicationContext();
       try{
           bdPosition = new BdPosition(context);
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
                uploadFile = getOutputFile();
                Uri u = Uri.fromFile(uploadFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,u);
                startActivityForResult(intent,GET_IMAGE_CAMERA);
                break;
            }
            //资产上传
            case R.id.btn_assert_upload1:{
                button1.setVisibility(View.INVISIBLE);
                circularBar.setVisibility(View.VISIBLE);
                circularBar.start();
                uploadDetial();
                break;
            }

            default:{
                break;
            }
        }
    }
    private void uploadDetial(){
        String assertNo = assertId.getText().toString();
        String assertDesc = assertContent.getText().toString();
        String assertValueMount = assertValue.getText().toString();
        String position = String.valueOf(FLAG.LATITUDE) + "," + String.valueOf(FLAG.LONGTITUDE);
        JSONObject picInfoJson = new JSONObject();
        try {
            picInfoJson.put("assertNo",assertNo);
            picInfoJson.put("assertDesc",assertDesc);
            picInfoJson.put("assertValue",assertValueMount);
            picInfoJson.put("position",position);
            picInfoJson.put("picNo",picNo);
            picInfoJson.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"get info is "+picInfoJson.toString());
        //TODO 将图片信息进行加密
        Encrypt encrypt = null;
        String picInfo = "";
        try {
            encrypt = new Encrypt(FLAG.TOKEN,"utf-8");
            picInfo = encrypt.encode(picInfoJson.toString());
            Log.v(TAG,picInfo);
            String newPicInfo = encrypt.decode(picInfo);
            Log.v(TAG,FLAG.TOKEN);
            Log.v(TAG,newPicInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(picMap == null){
            Toast.makeText(this,"图片为空",Toast.LENGTH_SHORT).show();
            Log.v(TAG,"save file is null");
        }else {
            //水印嵌入算法
            picMap = EmbedInfo.writeInfoInBiM(EmbedInfo.Info2Binary(picInfo),picMap);
            //图片转换字节并且进行base64编码
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            picMap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] picAinfo = bos.toByteArray();
            if(picAinfo.length > 0 ) {
                String picData = Base64.encodeToString(picAinfo,Base64.DEFAULT);

                //json传输的封装
                JSONObject uploadJson = new JSONObject();
                try {
                    uploadJson.put("function", FLAG.FUNCTION[1]);
                    uploadJson.put("token", FLAG.TOKEN);
                    uploadJson.put("pic", picData);
                    connection.setData(uploadJson);
                    connection.startSend();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                Log.v(TAG,"read data is null");
            }
        }
    }

    //获取相机返回的图片数据，并且保存在文件中
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(requestCode == GET_IMAGE_CAMERA) {
                try {
                    //测试程序测试bitmap前后的变化
                    //test();
                    FileInputStream fis = new FileInputStream(uploadFile.getPath());
                    picMap = BitmapFactory.decodeStream(fis).copy(Bitmap.Config.ARGB_8888,true);
                    Bitmap zoomMap = zoomBitmap(picMap,picMap.getWidth()/2,picMap.getHeight()/2);
                    imageView.setImageBitmap(zoomMap);
                    findViewById(R.id.take_photo_layout).setVisibility(View.INVISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    //确定保存的文件
    File getOutputFile(){
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            picNo = timeStamp;
            picFile = new File(picFileDir.getPath() + File.separator + "IMG_" + timeStamp + ".PNG");
            Log.v(TAG,picFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return picFile;
    }
    //图片进行压缩，保证在imageView中可以显示
    public  Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
    public void test(){
        try {
            FileInputStream fis = new FileInputStream(uploadFile.getPath());
            picMap = BitmapFactory.decodeStream(fis).copy(Bitmap.Config.ARGB_8888,true);
            EmbedInfo.test(picMap);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            picMap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] picData = bos.toByteArray();
            Bitmap newBitmap = BitmapFactory.decodeByteArray(picData,0,picData.length);
            EmbedInfo.test(newBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
