package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.TokenUtil;
import com.adapter.SelectSensorSingleAdapter;
import com.base.Basecactivity;
import com.data.User;
import com.xlistview.PullToRefreshLayout;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by zhu on 2018/6/13.
 */

public class SelectSensorActivity extends Basecactivity implements
        AdapterView.OnItemClickListener,
        PullToRefreshLayout.OnRefreshListener {

    private static final int REQUEST_SENSOR = 101;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.maclistview_id)
    ListView maclistview_id;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private SelectSensorSingleAdapter selectexcutesceneresultadapter;
    private List<Map> list_hand_scene = new ArrayList<>();
    private Handler mHandler = new Handler();
    //    String[] again_elements = {"7", "8", "9",
//            "10", "11", "12", "13", "14", "15", "16"};
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private List<Boolean> list_bool = new ArrayList<>();
    private int position;
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.selection_sensor_lay;
    }


    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        dialogUtil = new DialogUtil(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        maclistview_id.setOnItemClickListener(this);
        refresh_view.setOnRefreshListener(this);
//        refresh_view.autoRefresh();
        onData();
    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
//    R.drawable.magnetic_door_s,
//    R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
//    R.drawable.emergency_button_s
    private void onData() {

        getOtherDevices("");
        list_hand_scene = new ArrayList<>();
//        for (String type : again_elements) {
//            Boolean flag = false;
//            Map map = new HashMap();
//            map.put("name", type);
//            setPicture(type);
////            switch (type) {
////                case "7":
////                    map.put("image",R.drawable.magnetic_door_s);
////                    break;
////                case "8":
////                    map.put("image",R.drawable.human_induction_s);
////                    break;
////                case "9":
////                    map.put("image",R.drawable.water_s);
////                    break;
////                case "10":
////                    map.put("image",R.drawable.pm25_s);
////                    break;
////                case "11":
////                    map.put("image",R.drawable.emergency_button_s);
////                    break;
////                case "12":
////                    map.put("image",R.drawable.icon_rucebjq_40);
////                    break;
////                case "13":
////                    map.put("image",R.drawable.icon_yanwubjq_40);
////                    break;
////                case "14":
////                    map.put("image",R.drawable.icon_ranqibjq_40);
////                    break;
////                case "15":
////                    map.put("image",R.drawable.icon_zhinengmensuo_40);
////                    break;
////                case "16":
////                    map.put("image",R.drawable.ic_launcher);
////                    break;
////            }
//            list_hand_scene.add(map);
//            list_bool.add(flag);
//        }

        selectexcutesceneresultadapter = new SelectSensorSingleAdapter(SelectSensorActivity.this,
                list_hand_scene, listint, listintwo, new SelectSensorSingleAdapter.SelectSensorListener() {

            @Override
            public void selectsensor(int position) {
                SelectSensorActivity.this.position = position;
            }
        });
        maclistview_id.setAdapter(selectexcutesceneresultadapter);
//        xListView_scan.setPullLoadEnable(false);
//        xListView_scan.setFootViewHide();
//        xListView_scan.setXListViewListener(this);
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

    /**
     * 获取门磁等第三方设备
     *
     * @param doit
     */
    private void getOtherDevices(final String doit) {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(SelectSensorActivity.this));
//        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(SelectSensorActivity.this));
        MyOkHttp.postMapString(ApiHelper.sraum_getLinkSensor, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                getOtherDevices("load");
            }
        }, SelectSensorActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen

            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
            }

            @Override
            public void onSuccess(final User user) {
                list_hand_scene = new ArrayList<>();
                listint.clear();
                listintwo.clear();
                for (int i = 0; i < user.deviceList.size(); i++) {
                    Map<String, String> mapdevice = new HashMap<>();
                    mapdevice.put("name", user.deviceList.get(i).name);
                    mapdevice.put("number", user.deviceList.get(i).number);
                    mapdevice.put("type", user.deviceList.get(i).type);
                    mapdevice.put("boxNumber", user.deviceList.get(i).boxNumber);
                    mapdevice.put("boxName", user.deviceList.get(i).boxName);
                    list_hand_scene.add(mapdevice);
                    setPicture(user.deviceList.get(i).type);
                }


                selectexcutesceneresultadapter.setLists(list_hand_scene, listint, listintwo);
                selectexcutesceneresultadapter.notifyDataSetChanged();
                switch (doit) {
                    case "refresh":

                        break;
                    case "load":
                        break;
                }

//                Observable.create(new ObservableOnSubscribe<User>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
//                        emitter.onNext(user);//耗时动作
//                    }
//                }).timeout(5, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<User>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(User user) {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//                super.onSuccess(user);
////                refresh_view.stopRefresh();
//                list.clear();
//                listtype.clear();
//                list.addAll(user.deviceList);
//                for (User.device ud : list) {
//                    listtype.add(ud.status);
//                }
//                LogUtil.i("这是设备长度2", "" + list.size());
//                macstatus.setVisibility(View.GONE);
//                boxStatus(TokenUtil.getBoxflag(getActivity()), list.size());
////                adapter = new MacFragAdapter(getActivity(), list);
////                macfragritview_id.setAdapter(adapter);
//                adapter.clear();
//                adapter.addAll(list);//不要new adapter
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                SelectSensorActivity.this.finish();
                break;
            case R.id.next_step_txt:
//                Intent intent = null;
//                String deviceType = (String) list_hand_scene.get(position).get("type");
//                String deviceId = (String) list_hand_scene.get(position).get("number");
//                Map map = new HashMap();
//                map.put("deviceType", deviceType);
//                map.put("deviceId", deviceId);
//                if (deviceType == null) return;
//                switch (deviceType) {
//                    case "10":
//                        intent = new Intent(SelectSensorActivity.this, SelectPmDataActivity.class);
//                        intent.putExtra("map_link", (Serializable) map);
//                        startActivity(intent);
//                        break;//pm2.5
//                    default:
//                        intent = new Intent(SelectSensorActivity.this, UnderWaterActivity.class);
//                        intent.putExtra("map_link", (Serializable) map);
//                        startActivityForResult(intent, REQUEST_SENSOR);
//                        break;
//                }

                break;
            case R.id.rel_scene_set:
//                startActivity(new Intent(SelectExecuteSceneResultActivity.this,
//                        ExcuteSomeHandSceneActivity.class));
                break;//执行某些手动场景
        }
    }


//    private void onLoad() {
//        xListView_scan.stopRefresh();
//        xListView_scan.stopLoadMore();
//        xListView_scan.setRefreshTime("刚刚");
//    }
//
//    @Override
//    public void onRefresh() {
//        onLoad();
//    }

//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onLoad();
//            }
//        }, 1000);
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        View v = parent.getChildAt(position - maclistview_id.getFirstVisiblePosition());
//        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
//        ImageView img_guan_scene = (ImageView) v.findViewById(R.id.img_guan_scene);
//        TextView panel_scene_name_txt = (TextView) v.findViewById(R.id.panel_scene_name_txt);
//        cb.toggle();
        //设置checkbox现在状态
//        SelectSensorAdapter.getIsSelected().put(position, cb.isChecked());
//        if (cb.isChecked()) {
//            Intent intent = new Intent(SelectSensorActivity.this,  UnderWaterActivity.class);
//            intent.putExtra("type",(Serializable) again_elements[position]);
//            startActivityForResult(intent, REQUEST_SENSOR);
//            img_guan_scene.setImageResource(listintwo.get(position));
//            panel_scene_name_txt.setTextColor(getResources().getColor(R.color.gold_color));
//
//        } else {
//            img_guan_scene.setImageResource(listint.get(position));
//            panel_scene_name_txt.setTextColor(getResources().getColor(R.color.black_color));
//        }

//
//        SelectSensorSingleAdapter.ViewHolderContentType viewHolder = (SelectSensorSingleAdapter.ViewHolderContentType) view.getTag();
//        viewHolder.checkbox.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选
        Intent intent = null;
        String deviceType = (String) list_hand_scene.get(position).get("type");
        String deviceId = (String) list_hand_scene.get(position).get("number");
        String name = (String) list_hand_scene.get(position).get("name");
        Map map = new HashMap();
        map.put("deviceType", deviceType);
        map.put("deviceId", deviceId);
        map.put("name", name);
        if (deviceType == null) return;
        switch (deviceType) {
            case "10":
                intent = new Intent(SelectSensorActivity.this, SelectPmDataActivity.class);
                intent.putExtra("map_link", (Serializable) map);
                startActivity(intent);
                break;//pm2.5
            default:
                intent = new Intent(SelectSensorActivity.this, UnderWaterActivity.class);
                intent.putExtra("map_link", (Serializable) map);
                startActivityForResult(intent, REQUEST_SENSOR);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getOtherDevices("refresh");
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
