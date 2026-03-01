package com.example.qwerty;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.app.*;


public class MainActivity extends Activity {
TextView textView;
EditText playerId;
EditText lastLoginTime;
EditText now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
textView = findViewById(R.id.textView);
playerId = findViewById(R.id.playerId);
lastLoginTime = findViewById(R.id.lastLoginTime);
now = findViewById(R.id.now);
    }
    protected void onCreate(String h) {
textView.setText(textView.getText()+"\n"+h);
    }
    public void onCreate(View h) {
try{
textView.setText("j");
Rewardstep1.main(this);
textView.setText(textView.getText().toString().split("\n",2)[1]);
}catch(Exception e){
textView.setText(e.toString());
}
    }
}