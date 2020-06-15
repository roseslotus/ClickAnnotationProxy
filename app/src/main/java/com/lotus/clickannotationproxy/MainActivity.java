package com.lotus.clickannotationproxy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lotus.clickannotationproxy.anno.OnClick;
import com.lotus.clickannotationproxy.helper.InjectHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectHelper.injectEvent(this);
    }

    @OnClick({R.id.tv_hello})
    public void click(View view){
        Toast.makeText(this,"tv_hello",Toast.LENGTH_SHORT).show();
    }
}
