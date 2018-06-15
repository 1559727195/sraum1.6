package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.AddsignAdapter;
import com.adapter.SelectSensorAdapter;
import com.base.Basecactivity;
import com.xlistview.XListView;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by zhu on 2018/6/13.
 */

public class SelectSensorActivity extends Basecactivity implements XListView.IXListViewListener
        , AdapterView.OnItemClickListener {

    private static final int REQUEST_SENSOR = 101;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.xListView_scan)
    XListView xListView_scan;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private SelectSensorAdapter selectexcutesceneresultadapter;
    private List<Map> list_hand_scene = new ArrayList<>();
    private Handler mHandler = new Handler();
    String[] again_elements = {"7", "8", "9",
            "10", "11", "12", "13", "14", "15", "16"};
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private List<Boolean> list_bool = new ArrayList<>();

    @Override
    protected int viewId() {
        return R.layout.selection_sensor_lay;
    }


    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        xListView_scan.setOnItemClickListener(this);
        onData();
    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
//    R.drawable.magnetic_door_s,
//    R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
//    R.drawable.emergency_button_s
    private void onData() {
        list_hand_scene = new ArrayList<>();
        for (String type : again_elements) {
            Boolean flag =false;
            Map map = new HashMap();
            map.put("name", type);
            setPicture(type);
//            switch (type) {
//                case "7":
//                    map.put("image",R.drawable.magnetic_door_s);
//                    break;
//                case "8":
//                    map.put("image",R.drawable.human_induction_s);
//                    break;
//                case "9":
//                    map.put("image",R.drawable.water_s);
//                    break;
//                case "10":
//                    map.put("image",R.drawable.pm25_s);
//                    break;
//                case "11":
//                    map.put("image",R.drawable.emergency_button_s);
//                    break;
//                case "12":
//                    map.put("image",R.drawable.icon_rucebjq_40);
//                    break;
//                case "13":
//                    map.put("image",R.drawable.icon_yanwubjq_40);
//                    break;
//                case "14":
//                    map.put("image",R.drawable.icon_ranqibjq_40);
//                    break;
//                case "15":
//                    map.put("image",R.drawable.icon_zhinengmensuo_40);
//                    break;
//                case "16":
//                    map.put("image",R.drawable.ic_launcher);
//                    break;
//            }
            list_hand_scene.add(map);
            list_bool.add(flag);
        }

        selectexcutesceneresultadapter = new SelectSensorAdapter(SelectSensorActivity.this,
                list_hand_scene, listint, listintwo,list_bool);
        xListView_scan.setAdapter(selectexcutesceneresultadapter);
        xListView_scan.setPullLoadEnable(false);
        xListView_scan.setFootViewHide();
        xListView_scan.setXListViewListener(this);
    }

    private void setPicture(String type) {
        switch (type) {
            case "7":
                listint.add(R.drawable.icon_menci_40);
                listintwo.add(R.drawable.icon_menci_40_active);
                break;
            case "8":
                listint.add(R.drawable.icon_rentiganying_40);
                listintwo.add(R.drawable.icon_rentiganying_40_active);
                break;
            case "9":
                listint.add(R.drawable.icon_shuijin_40);
                listintwo.add(R.drawable.icon_shuijin_40_active);
                break;
            case "10":
                listint.add(R.drawable.icon_pm25_40);
                listintwo.add(R.drawable.icon_pm25_40_active);
                break;
            case "11":
                listint.add(R.drawable.icon_jinjianniu_40);
                listintwo.add(R.drawable.icon_jinjianniu_40_active);
                break;
            case "12":
                listint.add(R.drawable.icon_rucebjq_40);
                listintwo.add(R.drawable.icon_rucebjq_40_active);
                break;
            case "13":
                listint.add(R.drawable.icon_yanwubjq_40);
                listintwo.add(R.drawable.icon_yanwubjq_40_active);
                break;
            case "14":
                listint.add(R.drawable.icon_ranqibjq_40);
                listintwo.add(R.drawable.icon_ranqibjq_40_active);
                break;
            case "15":
                listint.add(R.drawable.icon_zhinengmensuo_40);
                listintwo.add(R.drawable.icon_zhinengmensuo_40_active);
                break;
            case "16":
                listint.add(R.drawable.ic_launcher);
                listintwo.add(R.drawable.ic_launcher);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                SelectSensorActivity.this.finish();
                break;
            case R.id.next_step_txt:
//                Intent intent = new Intent(SelectSensorActivity.this,
//                        GuanLianSceneBtnActivity.class);
//                intent.putExtra("excute","auto");//自动的
//                startActivity(intent);
                break;
            case R.id.rel_scene_set:
//                startActivity(new Intent(SelectExecuteSceneResultActivity.this,
//                        ExcuteSomeHandSceneActivity.class));
                break;//执行某些手动场景
        }
    }


    private void onLoad() {
        xListView_scan.stopRefresh();
        xListView_scan.stopLoadMore();
        xListView_scan.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        onLoad();
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 1000);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View v = parent.getChildAt(position - xListView_scan.getFirstVisiblePosition());
       CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
        cb.toggle();
        //设置checkbox现在状态
       SelectSensorAdapter.getIsSelected().put(position, cb.isChecked());
        if (cb.isChecked()) {
            Intent intent = new Intent(SelectSensorActivity.this, DeviceExcuteOpenActivity.class);
            startActivityForResult(intent, REQUEST_SENSOR);
        } else {
            selectexcutesceneresultadapter.notifyDataSetChanged();
        }

    }
}
