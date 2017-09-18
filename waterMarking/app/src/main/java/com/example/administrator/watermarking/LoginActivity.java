package com.example.administrator.watermarking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tools.Connection;
import tools.FLAG;

/**
 * Created by Administrator on 2017-08-10.
 */
public class LoginActivity  extends AppCompatActivity{

    EditText account;
    EditText password;
    Button btn_sign_in;
    Connection connection;
    JSONObject sendData;
    Intent intent;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            JSONObject receiveData = (JSONObject) msg.obj;
            try {
                if(receiveData.has("state")) {
                    if (receiveData.getString("state").equals(FLAG.STATE[0])) {
                        FLAG.TOKEN = receiveData.getString("token");
                        FLAG.USERACCOUNT = sendData.getString("phone");
                        FLAG.USERPASSWORD = sendData.getString("password");
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        FLAG.HISTORY.append("用户: " + FLAG.USERACCOUNT + " 登陆成功");
                    } else {
                        Toast.makeText(LoginActivity.this, "请输入正确的账号密码", Toast.LENGTH_SHORT).show();
                        account.setText("");
                        password.setText("");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account =(EditText)findViewById(R.id.Account);
        password=(EditText)findViewById(R.id.password);
        btn_sign_in= (Button)findViewById(R.id.btn_sign_in);
        sendData = new JSONObject();
        intent = new Intent();

    }
    @Override
    protected void onResume(){
        super.onResume();
        FLAG.FLAG_HANDLER = mHandler;
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection = Connection.getConnection();
                connection.setHandler();
                if(setSendData()){
                    connection.setData(sendData);
                    connection.startSend();
                    connection.start();
                }

            }
        });
    }
    public boolean setSendData(){
        String phoneNumber = account.getText().toString();
        String passWord = password.getText().toString();
        if(phoneNumber.equals("")||passWord.equals("")){
            Toast.makeText(this,"请输入账号密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        char[] phoneArray = phoneNumber.toCharArray();
        if(phoneArray.length > 13){
            Toast.makeText(this,"请输入正确位数的号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i = 0 ; i < phoneArray.length; i ++){
            if(!Character.isDigit(phoneArray[i])){
                return false;
            }
        }
        try {
            sendData.put("function",FLAG.FUNCTION[0]);
            sendData.put("phone",phoneNumber);
            sendData.put("password",passWord);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
