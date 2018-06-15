package com.massky.sraum;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import butterknife.InjectView;

/**
 * Created by zhu on 2018/1/12.
 */

public class DeviceExcuteOpenActivity extends Basecactivity{
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;

    @Override
    protected int viewId() {
        return R.layout.device_execute_openact;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);

    }

//    @Override
//    protected void onView() {
//        StatusUtils.setFullToStatusBar(this);  // StatusBar.
//    }
//
//    @Override
//    protected void onEvent() {
//        back.setOnClickListener(this);
//        next_step_txt.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                DeviceExcuteOpenActivity.this.finish();
                break;
            case R.id.next_step_txt://场景设置
//                Intent intent = new Intent(DeviceExcuteOpenActivity.this,
//                        GuanLianSceneBtnActivity.class);
//                intent.putExtra("excute","auto");//自动的
//                startActivity(intent);
                break;
        }
    }
}
