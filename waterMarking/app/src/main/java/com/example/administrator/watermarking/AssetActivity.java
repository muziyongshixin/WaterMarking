package com.example.administrator.watermarking;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import tools.Connection;
import tools.FLAG;
import tools.Picture;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-08-11.
 */
// assert load activity
    //支持资产显示，资产修改，资产删除
public class AssetActivity extends AppCompatActivity implements View.OnClickListener {
    Connection connection;
    JSONObject sendData;
    JSONObject picJsonList;
    int[][] picViewIdArray;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            JSONObject receiveData= (JSONObject) msg.obj;
            try {
                if(receiveData.getString("state").equals(FLAG.STATE[1])){
                    Toast.makeText(AssetActivity.this, receiveData.getString("wrongInfo"), Toast.LENGTH_SHORT).show();
                }else {
                    picJsonList = new JSONObject(receiveData.getString("picList"));
                    picViewIdArray = new int[picJsonList.length()][5];
                    //TODO 界面中添加心得资产布局
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connection = Connection.getConnection();
        sendData = new JSONObject();
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshAssert();
    }
    private void refreshAssert(){
        FLAG.FLAG_HANDLER = mHandler;
        connection.setHandler();
        getAllAssert();
        connection.setData(sendData);
        connection.startSend();
    }
    private void getAllAssert(){
        try {
            sendData.put("token",FLAG.TOKEN);
            sendData.put("function",FLAG.FUNCTION[4]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //FIXME 这部分的代码还有很大的问题渲染起来很麻烦
    private void viewRender(Context context) throws JSONException {
        ScrollView scrollView = (ScrollView)findViewById(R.id.assert_scrollView);
        for(int i = 0 ; i < picJsonList.length() ; i ++){
            JSONObject pic = null;
            pic = new JSONObject(picJsonList.getString(String.valueOf(i)));
            if(pic == null){
                continue;
            }
            //最外层的布局
            LinearLayout outerMost = new LinearLayout(context);
            LinearLayout.LayoutParams outerMostParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            outerMostParams.setLayoutDirection(LinearLayout.VERTICAL);
            outerMost.setLayoutParams(outerMostParams);
            //TODO 不知道这里行不行，需要给每个资产的提供特定的id，从而修改的时候可以通过id
            //如果可以通过图片id来展示就蛮好的，数据库需要更改
            outerMost.setId(i);

            //上面布局的形式
            LinearLayout upLayout = new LinearLayout(context);
            LinearLayout.LayoutParams upLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,60);
            upLayoutParams.setLayoutDirection(LinearLayout.HORIZONTAL);
            upLayout.setLayoutParams(upLayoutParams);
            upLayout.setBackgroundColor(Color.parseColor("#ffff"));
            upLayout.setWeightSum((float)1.0);


            //上面左边的布局，表示的是资产名称以及资产描述
            LinearLayout upLeftLayout = new LinearLayout(context);
            LinearLayout.LayoutParams upLeftParams = new LinearLayout.LayoutParams(0,50,(float)0.5);
            upLeftParams.setLayoutDirection(LinearLayout.VERTICAL);
            upLeftParams.setMargins(5,5,0,0);
            upLayout.setLayoutParams(upLayoutParams);
            //资产名称
            EditText assertNameText = new EditText(context);
            LinearLayout.LayoutParams assertNameParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            assertNameText.setLayoutParams(assertNameParams);
            assertNameText.setTextSize(9);
            assertNameText.setTextColor(Color.parseColor("#454545"));
            //设置资产名称
            assertNameText.setText(pic.getString("picName"));
            assertNameText.setId(i*10);
            //TODO 同上
            picViewIdArray[i][0] = i *10;

            //资产的描述
            EditText assertInfoText = new EditText(context);
            LinearLayout.LayoutParams assertInfoParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            assertInfoParams.setMargins(0,5,0,0);
            assertInfoText.setLayoutParams(assertInfoParams);
            assertInfoText.setTextColor(Color.parseColor("#454545"));
            //设置资产信息
            assertInfoText.setText(pic.getString("picInfo"));
            //TODO
            assertInfoText.setId(i*10 +1);

            //TODO 同上
            picViewIdArray[i][1] = i*10+1;

            upLeftLayout.addView(assertNameText);
            upLeftLayout.addView(assertInfoText);
            //图片显示部分
            ImageView upRightLayout = new ImageView(context);
            LinearLayout.LayoutParams imaggParams = new LinearLayout.LayoutParams(0,60,(float)0.5 );
            upRightLayout.setLayoutParams(imaggParams);
            byte[] picData = Base64.decode(pic.getString("picData"),Base64.DEFAULT);
            upRightLayout.setImageBitmap(BitmapFactory.decodeByteArray(picData,0,picData.length));
            //TODO
            upRightLayout.setId(i*10 + 2);
            picViewIdArray[i][2] = i*10+2;

            //布局的添加
            upLayout.addView(upLeftLayout);
            upLayout.addView(upRightLayout);

            //下面布局的设置
            LinearLayout downLayout = new LinearLayout(context);
            LinearLayout.LayoutParams downLayoutParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,20);
            downLayoutParams.setLayoutDirection(LinearLayout.HORIZONTAL);
            downLayout.setLayoutParams(downLayoutParams);
            downLayout.setWeightSum((float)1.0);
            downLayout.setBackgroundColor(Color.parseColor("#ffff"));
            Button downBtnModify = new Button(context);
            downBtnModify.setBackground(null);
            downBtnModify.setTextColor(Color.parseColor("#454545"));
            downBtnModify.setText("修改");
            // TODO
            downBtnModify.setId(i*10+3);
            picViewIdArray[i][3] = i*10 + 3;

            Button downBtnDelete = new Button(context);
            downBtnDelete.setBackground(null);
            downBtnDelete.setTextColor(Color.parseColor("#454545"));
            downBtnDelete.setText("删除");
            // TODO
            downBtnDelete.setId(i*10 + 4);

            picViewIdArray[i][4] = i*10 + 4;
            TextView downTextTime= new TextView(context);
            LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            a.setMarginStart(30);
            downTextTime.setLayoutParams(a);
            downTextTime.setTextSize(8);

            downLayout.addView(downBtnModify);
            downLayout.addView(downBtnDelete);
            downLayout.addView(downTextTime);


            outerMost.addView(upLayout);
            outerMost.addView(downLayout);
            scrollView.addView(outerMost);
        }

    }

    @Override
    public void onClick(View v) {
        int clickViewId = v.getId();
        for(int i = 0 ; i < picJsonList.length() ; i++){
            for(int j = 0 ; j < 3; j++){
                if(clickViewId == picViewIdArray[i][j]){
                    try {
                        sendData.put("token",FLAG.TOKEN);
                        sendData.put("assertId",i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(j == 3) {
                        try {
                            sendData.put("function",FLAG.FUNCTION[5]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject modifyInfo = new JSONObject();
                        EditText assertName = (EditText)findViewById(picViewIdArray[i][0]);
                        String modifyName = assertName.getText().toString();
                        EditText assertInfo = (EditText)findViewById(picViewIdArray[i][1]);
                        String modifyInfoContent = assertInfo.getText().toString();
                        try {
                            modifyInfo.put("modifyName",modifyName);
                            modifyInfo.put("modifyContent",modifyInfoContent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /*sendData.setAssertInfo(modifyInfo);*/
                    }
                    if(j == 4){
                        try {
                            sendData.put("function",FLAG.FUNCTION[6]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    connection.setData(sendData);
                    connection.start();
                }
            }
        }
    }
}
