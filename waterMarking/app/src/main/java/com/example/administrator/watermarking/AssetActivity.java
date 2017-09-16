package com.example.administrator.watermarking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;

import tools.Connection;
import tools.ConnectionData;
import tools.FLAG;
import tools.Picture;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearSmoothScroller;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-11.
 */
// assert load activity
    //支持资产显示，资产修改，资产删除
public class AssetActivity extends AppCompatActivity implements View.OnClickListener {
    Connection connection;
    ConnectionData sendData;
    List<Picture> pictureList;
    int[][] picViewIdArray;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            ConnectionData receiveData= (ConnectionData)msg.obj;
            if(receiveData.getState().equals(FLAG.STATE[1])){
                Toast.makeText(AssetActivity.this, receiveData.getWrong_info(), Toast.LENGTH_SHORT);
            }else {
                pictureList = receiveData.getPicture_list();
                picViewIdArray = new int[pictureList.size()][5];
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            connection = Connection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendData = new ConnectionData();
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
        connection.start();
    }
    private void getAllAssert(){
        sendData.setToken(FLAG.TOKEN);
        sendData.setFunction(FLAG.FUNCTION[4]);

    }
    private void viewRender(Context context){
        ScrollView scrollView = (ScrollView)findViewById(R.id.assert_scrollView);
        for(Picture pic : pictureList){
            //最外层的布局
            LinearLayout outerMost = new LinearLayout(context);
            LinearLayout.LayoutParams outerMostParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            outerMostParams.setLayoutDirection(LinearLayout.VERTICAL);
            outerMost.setLayoutParams(outerMostParams);
            outerMost.setId(pic.getPicture_no());

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
            assertNameText.setText(pic.getPic_name());
            assertNameText.setId(pic.getPicture_no()*10);
            picViewIdArray[pic.getPicture_no()][0] = pic.getPicture_no()*10;

            //资产的描述
            EditText assertInfoText = new EditText(context);
            LinearLayout.LayoutParams assertInfoParams = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            assertInfoParams.setMargins(0,5,0,0);
            assertInfoText.setLayoutParams(assertInfoParams);
            assertInfoText.setTextColor(Color.parseColor("#454545"));
            //设置资产信息
            assertInfoText.setText(pic.getPicture_info());
            assertInfoText.setId(pic.getPicture_no()*10 +1);

            picViewIdArray[pic.getPicture_no()][1] = pic.getPicture_no()*10+1;

            upLeftLayout.addView(assertNameText);
            upLeftLayout.addView(assertInfoText);
            //图片显示部分
            ImageView upRightLayout = new ImageView(context);
            LinearLayout.LayoutParams imaggParams = new LinearLayout.LayoutParams(0,60,(float)0.5 );
            upRightLayout.setLayoutParams(imaggParams);
            upRightLayout.setImageBitmap(BitmapFactory.decodeByteArray(pic.getPic_data(),0,pic.getPic_data().length));
            upRightLayout.setId(pic.getPicture_no()*10 + 2);
            picViewIdArray[pic.getPicture_no()][2] = pic.getPicture_no()*10+2;

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
            downBtnModify.setId(pic.getPicture_no()*10+3);
            picViewIdArray[pic.getPicture_no()][3] = pic.getPicture_no()*10 + 3;

            Button downBtnDelete = new Button(context);
            downBtnDelete.setBackground(null);
            downBtnDelete.setTextColor(Color.parseColor("#454545"));
            downBtnDelete.setText("删除");
            downBtnDelete.setId(pic.getPicture_no()*10 + 4);

            picViewIdArray[pic.getPicture_no()][4] = pic.getPicture_no()*10 + 4;
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
        for(int i = 0 ; i < pictureList.size() ; i++){
            for(int j = 0 ; j < 3; j++){
                if(clickViewId == picViewIdArray[i][j]){
                    sendData.setToken(FLAG.TOKEN);
                    sendData.setAssertId(i);
                    if(j == 3) {
                        sendData.setFunction(FLAG.FUNCTION[5]);
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
                        sendData.setAssertInfo(modifyInfo);
                    }
                    if(j == 4){
                        sendData.setFunction(FLAG.FUNCTION[6]);
                    }
                    connection.setData(sendData);
                    connection.start();
                }
            }
        }
    }
}
