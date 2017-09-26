package com.example.administrator.watermarking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import tools.Connection;
import tools.FLAG;
import tools.Picture;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Administrator on 2017-08-11.
 */
// assert load activity
    //支持资产显示，资产修改，资产删除
public class AssetActivity extends AppCompatActivity {
    Connection connection;
    JSONObject sendData;
    JSONObject picJsonList;
    Context context;
    @Bind(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    JSONObject receiveData = (JSONObject) msg.obj;
                    try {
                        if (receiveData.getString("state").equals(FLAG.STATE[1])) {
                            Toast.makeText(AssetActivity.this, receiveData.getString("wrongInfo"), Toast.LENGTH_SHORT).show();
                        } else {
                            picJsonList = new JSONObject(receiveData.getString("picList"));
                            String[] dataSet = new String[picJsonList.length()];
                            for (int i = 0; i < picJsonList.length(); i++) {
                                dataSet[i] = picJsonList.getString(String.valueOf(i));
                            }
                            //TODO 界面中添加新的资产布局
                            viewRender(context, dataSet);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 1 :{
                    String[] dataSet = (String[])msg.obj;
                    PhotoAdapter adapter = new PhotoAdapter(dataSet, context,mHandler);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                            super.getItemOffsets(outRect, view, parent, state);
                            outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                    try {
                        sendData.put("function",FLAG.FUNCTION[4]);
                        Bundle bundle = msg.getData();
                        sendData.put("asset_no",bundle.getString("asset_no"));
                        sendData.put("token",FLAG.TOKEN);
                        sendData.put("user_phone",FLAG.USERACCOUNT);
                        connection.setData(sendData);
                        connection.startSend();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        connection = Connection.getConnection();
        ButterKnife.bind(this);
       // setSupportActionBar(mToolbar);
        sendData = new JSONObject();
        context = getApplicationContext();
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
            sendData.put("function",FLAG.FUNCTION[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void viewRender(Context context,String[] dataSet) throws JSONException {
        PhotoAdapter adapter = new PhotoAdapter(dataSet, context,mHandler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
