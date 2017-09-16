package com.example.administrator.watermarking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import tools.Connection;
import tools.ConnectionData;
import tools.FLAG;

/**
 * Created by Administrator on 2017-08-10.
 */
public class LoginActivity  extends AppCompatActivity{

    EditText account;
    EditText password;
    Button btn_sign_in;
    Connection connection;
    ConnectionData sendData;
    Intent intent;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            ConnectionData receiveData = (ConnectionData) msg.obj;
            if(receiveData.getState().equals(FLAG.STATE[0])){
                FLAG.TOKEN = receiveData.getToken();
                FLAG.USERACCOUNT = receiveData.getPhone();
                FLAG.USERPASSWORD = receiveData.getPassword();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                FLAG.HISTORY.append("用户: "+receiveData.getPhone() + " 登陆成功");
            }else{
                Toast.makeText(LoginActivity.this,"请输入正确的账号密码",Toast.LENGTH_SHORT).show();
                account.setText("");
                password.setText("");
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
        try {
            connection = Connection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendData = new ConnectionData();
        intent = new Intent();

    }
    @Override
    protected void onResume(){
        super.onResume();
        FLAG.FLAG_HANDLER = mHandler;
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.setHandler();
                if(setSendData()){
                    connection.setData(sendData);
                    connection.start();
                }
                try {
                    connection.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        sendData.setFunction(FLAG.FUNCTION[0]);
        sendData.setPhone(phoneNumber);
        sendData.setPassword(passWord);
        return true;
    }

}
