package com.zxn.xm.fireworks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.wym.fireworks.widget.FireworkDrawable;
import com.zxn.wym.fireworks.widget.FireworkView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected FireworkView fv;
    protected TextView tv;
    protected TextView tvOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

        fv.showFirework();
    }

    private void initView() {
        fv = (FireworkView) findViewById(R.id.fv);
        fv.setOnDismissListener(new FireworkDrawable.OnDismissListener() {
            @Override
            public void onDismiss(FireworkDrawable fireworkDrawable) {
                Toast.makeText(MainActivity.this, "烟花散尽了啊!!!!!", Toast.LENGTH_SHORT).show();
            }
        });
//        fv.setImageDrawable();
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(MainActivity.this);
        tvOff = (TextView) findViewById(R.id.tv_off);
        tvOff.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv) {
            fv.showFirework();
        } else if (view.getId() == R.id.tv_off) {
            //fv.showFirework();
        }
    }
}
