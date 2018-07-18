package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MacdeviceAdapter;
import com.adapter.MyDeviceAdapter;
import com.adapter.SelectSensorSingleAdapter;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.EditMyDeviceActivity;
import com.massky.sraum.MacdetailActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.MyDeviceItemActivity;
import com.massky.sraum.R;
import com.massky.sraum.SelectSensorActivity;
import com.xlistview.PullToRefreshLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by masskywcy on 2016-09-23.
 */
//智能设备界面
public class MyDeviceFragment extends Basecfragment implements AdapterView.OnItemClickListener,
        PullToRefreshLayout.OnRefreshListener {
    @InjectView(R.id.maclistview_id)
    ListView maclistview_id;
    @InjectView(R.id.addcircle_id)
    ImageView addcircle_id;
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.adderela_id)
    RelativeLayout adderela_id;
    @InjectView(R.id.addebtn_id)
    Button addebtn_id;
    @InjectView(R.id.sideslip_id)
    RelativeLayout sideslip_id;
    @InjectView(R.id.addrelative_id)
    RelativeLayout addrelative_id;
    @InjectView(R.id.addimage_id)
    ImageView addimage_id;
    @InjectView(R.id.cenimage_id)
    ImageView cenimage_id;
    @InjectView(R.id.centext_id)
    TextView centext_id;
    @InjectView(R.id.macdevice_rel)
    RelativeLayout macdevice_rel;
    @InjectView(R.id.addtxt_text_id)
    TextView addtxt_text_id;
    private List<User.device> panelList = new ArrayList<>();
    private String boxnumber;
    //进行判断是否进行创建刷新
    private boolean isFirstIn = true;
    private DialogUtil dialogUtil;
    private MainfragmentActivity mainfragmentActivity;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private static SlidingMenu mySlidingMenu;
    private String accountType;
    private MyDeviceAdapter mydeviceadapter;
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private List<Map> list_hand_scene = new ArrayList<>();

    public static MyDeviceFragment newInstance(SlidingMenu mySlidingMenu1) {
        MyDeviceFragment newFragment = new MyDeviceFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private void chageSlideMenu() {
        if (mySlidingMenu != null) {
            if (mySlidingMenu.isMenuShowing()) {
                mySlidingMenu.showContent();
            } else {
                mySlidingMenu.showMenu();
            }
        }
    }

    @Override
    protected int viewId() {
        return R.layout.mydevicefragment;
    }

    @Override
    protected void onView() {

        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("我的设备");
//        addtxt_text_id.setVisibility(View.VISIBLE);
        addtxt_text_id.setText("设备呼唤");
        addtxt_text_id.setOnClickListener(this);
//
//        registerMessageReceiver();
        mainfragmentActivity = (MainfragmentActivity) getActivity();
        dialogUtil = new DialogUtil(getActivity());
        boxnumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
        maclistview_id.setDividerHeight(0);
        maclistview_id.setOnItemClickListener(this);
        addcircle_id.setOnClickListener(this);
        refresh_view.setOnRefreshListener(this);
        addebtn_id.setOnClickListener(this);
        sideslip_id.setOnClickListener(this);

        accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType", "");
        //panel_bottom_add_rel
        //异步发送数据
        switch (accountType) {
            case "1":
                macdevice_rel.setVisibility(View.VISIBLE);
                break;//    break;//主机，业主-写死
            case "2":
                macdevice_rel.setVisibility(View.GONE);
                break;//从机，成员
        }

        onData();
    }

    @Override
    public void onStart() {
        super.onStart();
//        refresh_view.autoRefresh();
        LogUtil.i("查看onstart方法");
        onData();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
            case R.id.addtxt_text_id://跳转到设备呼唤界面

                break;
        }
    }


    //7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，12-久坐报警器，13-烟雾报警器，14-天然气报警器，15-智能门锁，16-直流电阀机械手
//    R.drawable.magnetic_door_s,
//    R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
//    R.drawable.emergency_button_s
    private void onData() {
        list_hand_scene = new ArrayList<>();
        mydeviceadapter = new MyDeviceAdapter(getActivity(),
                list_hand_scene, listint, listintwo, null);
        maclistview_id.setAdapter(mydeviceadapter);
        getOtherDevices("");
    }

    /**
     * 获取门磁等第三方设备
     *
     * @param doit
     */
    private void getOtherDevices(final String doit) {
        dialogUtil.loadDialog();
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(getActivity()));
        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        MyOkHttp.postMapString(ApiHelper.sraum_getAllPanelNew, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                getOtherDevices("load");
            }
        }, getActivity(), dialogUtil) {
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
                for (int i = 0; i < user.panelList.size(); i++) {
                    Map<String, String> mapdevice = new HashMap<>();
                    mapdevice.put("id", user.panelList.get(i).id);
                    mapdevice.put("mac", user.panelList.get(i).mac);
                    mapdevice.put("name", user.panelList.get(i).name);
                    mapdevice.put("type", user.panelList.get(i).type);
                    mapdevice.put("status", user.panelList.get(i).status);
                    mapdevice.put("buttonStatus", user.panelList.get(i).buttonStatus);
                    mapdevice.put("button5Name", user.panelList.get(i).button5Name);
                    mapdevice.put("button5Type", user.panelList.get(i).button5Type);
                    mapdevice.put("button6Name", user.panelList.get(i).button6Name);
                    mapdevice.put("button6Type", user.panelList.get(i).button6Type);
                    mapdevice.put("button7Name", user.panelList.get(i).button7Name);
                    mapdevice.put("button7Type", user.panelList.get(i).button7Type);
                    mapdevice.put("button8Name", user.panelList.get(i).button8Name);
                    mapdevice.put("button8Type", user.panelList.get(i).button8Type);
                    mapdevice.put("firmware", user.panelList.get(i).firmware);
                    mapdevice.put("hardware", user.panelList.get(i).hardware);
                    mapdevice.put("gatewayid", user.panelList.get(i).gatewayid);

                    list_hand_scene.add(mapdevice);
                    setPicture(user.panelList.get(i).type);
                }

                mydeviceadapter.setLists(list_hand_scene, listint, listintwo);
                mydeviceadapter.notifyDataSetChanged();
                switch (doit) {
                    case "refresh":

                        break;
                    case "load":
                        break;
                }
            }
        });
    }

    private void setPicture(String type) {
        switch (type) {
            case "A201":
            case "A202":
            case "A203":
                listint.add(R.drawable.marklamph);
                listintwo.add(R.drawable.marklamph);
                break;
            case "A301":
            case "A302":
            case "A303":
                listint.add(R.drawable.dimminglights);
                listintwo.add(R.drawable.dimminglights);
                break;
            case "A401":
                listint.add(R.drawable.home_curtain);
                listintwo.add(R.drawable.home_curtain);
                break;
            case "A501":
            case "A511":
                listint.add(R.drawable.home_26);
                listintwo.add(R.drawable.home_26);
                break;
            case "A801":
                listint.add(R.drawable.icon_menci_40);
                listintwo.add(R.drawable.icon_menci_40_active);
                break;
            case "A901":
                listint.add(R.drawable.icon_rentiganying_40);
                listintwo.add(R.drawable.icon_rentiganying_40);
                break;
            case "AB01":
                listint.add(R.drawable.icon_yanwubjq_40);
                listintwo.add(R.drawable.icon_yanwubjq_40);
                break;
            case "A902":
                listint.add(R.drawable.icon_rucebjq_40);
                listintwo.add(R.drawable.icon_rucebjq_40);
                break;
            case "AB04":
                listint.add(R.drawable.icon_ranqibjq_40);
                listintwo.add(R.drawable.icon_ranqibjq_40);
                break;
            case "AC01":
                listint.add(R.drawable.icon_shuijin_40);
                listintwo.add(R.drawable.icon_shuijin_40);
                break;
            case "AD01":
                listint.add(R.drawable.icon_pm25_40);
                listintwo.add(R.drawable.icon_pm25_40);
                break;
            case "B001":
                listint.add(R.drawable.icon_jinjianniu_40);
                listintwo.add(R.drawable.icon_jinjianniu_40);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), MyDeviceItemActivity.class);
        intent.putExtra("panelItem", (Serializable) list_hand_scene.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
        onData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.macdeviceceiver";

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                if (messflag == 1 || messflag == 3 || messflag == 4 || messflag == 5) {

                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            refresh_view.autoRefresh();
            onData();
        }
        LogUtil.eLength("查看显示方法", hidden + "");
    }


    public void setBackToMainTitleListener(BackToMainTitleListener backToMainTitleListener) {
        this.backToMainTitleListener = backToMainTitleListener;
    }

    private BackToMainTitleListener backToMainTitleListener;

    public interface BackToMainTitleListener {
        void backToMainTitleLength(int length);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.e("peng", "MacdeviceFragment->onResume:name:");
    }
}
