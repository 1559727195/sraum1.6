package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.SelectDeviceMessageAdapter;
import com.base.Basecfragment;
import com.data.User;
import com.massky.sraum.MessageDetailActivity;
import com.massky.sraum.R;
import com.xlistview.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import lecho.lib.hellocharts.model.Line;
import okhttp3.Call;

import static com.fragment.MessageFragment.MESSAGE_FRAGMENT;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于第一个fragment主界面*/
public class DeviceMessageFragment extends Basecfragment implements
        AdapterView.OnItemClickListener, XListView.IXListViewListener, AdapterView.OnItemLongClickListener {
    private SelectDeviceMessageAdapter selectdevicemessageAdapter;
    private MessageReceiver mMessageReceiver;
    private String content = "";
    private View account_view;
    private DialogUtil dialogUtil;
    private List<Map> messageLists = new ArrayList<>();
    @InjectView(R.id.xListView_scan)
    XListView xListView_scan;
    @InjectView(R.id.linear_popcamera)
    LinearLayout linear_popcamera;
    @InjectView(R.id.all_read_linear)
    LinearLayout all_read_linear;
    @InjectView(R.id.all_select_linear)
    LinearLayout all_select_linear;
    @InjectView(R.id.delete_linear)
    LinearLayout delete_linear;
    private Handler mHandler = new Handler();
    private List<Map> messageLists_local = new ArrayList<>();
    private String str = "全选";

    @Override
    protected int viewId() {
        return R.layout.device_message_fragment;
    }

    @Override
    public void onStart() {//onStart()-这个方法在屏幕唤醒时调用。
        super.onStart();
        get_message(true, "doit");
    }

    /**
     * 退出动画
     */
    private void exitanimation() {
        Animation animation = AnimationUtils.loadAnimation(
                getActivity(), R.anim.dialog_exit);
        linear_popcamera.clearAnimation();
        linear_popcamera.setAnimation(animation);
        linear_popcamera.setVisibility(View.GONE);
        xListView_scan.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 1.0f));
        linear_popcamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0.0f));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 进入动画
     */
    private void enteranimation() {
        xListView_scan.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 1.0f));
        linear_popcamera.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0.0f));
        Animation animation = AnimationUtils.loadAnimation(
                getActivity(), R.anim.dialog_enter);
        linear_popcamera.clearAnimation();
        linear_popcamera.setAnimation(animation);
        linear_popcamera.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void onLoad() {
        xListView_scan.stopRefresh();
        xListView_scan.stopLoadMore();
        xListView_scan.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        onLoad();
        get_message(true, "doit");
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
                get_message(false, "doit");
            }
        }, 500);
    }


//    /**
//     * 底部弹出拍照，相册弹出框
//     */
//    private void addViewid() {
//        account_view = LayoutInflater.from(getActivity()).inflate(R.layout.message_function_select, null);
//        all_read_linear = (LinearLayout) account_view.findViewById(R.id.all_read_linear);
//        all_select_linear = (LinearLayout) account_view.findViewById(R.id.all_select_linear);
//        delete_linear = (LinearLayout) account_view.findViewById(R.id.delete_linear);
//
//        all_read_linear.setOnClickListener(this);
//        all_select_linear.setOnClickListener(this);
//        delete_linear.setOnClickListener(this);
//
////        //common_setting_image
////        common_setting_image = (ImageView) account_view.findViewById(R.id.common_setting_image);
//        dialogUtil = new DialogUtil(getActivity(), account_view, 2);
//    }

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_FRAGMENT);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//        switch (content) {
//            case "编辑":
//                dialogUtil.loadViewBottomdialog();
//                break;
//            case "取消":
//                break;
//        }
        return true;
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(MESSAGE_FRAGMENT)) {
                content = (String) intent.getSerializableExtra("action");
                messageLists.clear();
                SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", messageLists);
//                switch (content) {
//                    case "编辑":
//                        dialogUtil.loadViewBottomdialog();
//                        break;
//                    case "取消":
//                        break;
//                }
                get_message(true, "");
            }
        }
    }

    private int page = 1;

    private void get_message(final boolean isRefresh, String doit) {
        if (isRefresh) {
            page = 1;
        }
        get_message_by_page(isRefresh, doit);
    }

    private void get_message_by_page(final boolean isRefresh, final String doit) {
        dialogUtil.loadDialog();
        final Map map = new HashMap();
//        String roomNo = roomNums.get(roomIndex);
        map.put("token", TokenUtil.getToken(getActivity()));
//        map.put("projectCode",projectCode);
        map.put("page", page);
//        map.put("roomNo",roomNo == null ? "" : roomNo);
        MyOkHttp.postMapObject(ApiHelper.sraum_getMessage, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                get_message_by_page(isRefresh, doit);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(getActivity(), "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {
                if (isRefresh) {
                    messageLists.clear();
                } else {
                    str = "全选";
                }
//                messageList.addAll(user.messageList);
                for (int i = 0; i < user.messageList.size(); i++) {
                    Map map = new HashMap();
                    map.put("id", user.messageList.get(i).id);
                    map.put("messageType", user.messageList.get(i).messageType);
                    map.put("messageTitle", user.messageList.get(i).messageTitle);
                    map.put("deviceName", user.messageList.get(i).deviceName);
                    map.put("readStatus", user.messageList.get(i).readStatus);
                    map.put("eventTime", user.messageList.get(i).eventTime);
                    map.put("ischecked", false);
                    messageLists.add(map);
                }
                common_del(doit);
                page++;
//                if( dialogUtil != null){
//                    dialogUtil.removeviewBottomDialog();
//                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    /**
     * 公共处理方法
     */
    private void common_del(String doit) {
        switch (content) {
            case "编辑":
                if (doit.equals("")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dialogUtil.loadViewBottomdialog();
                            enteranimation();
                        }
                    });
                }
                for (int i = 0; i < messageLists.size(); i++) {
                    selectdevicemessageAdapter.getIsCheckBoxVisiable().put(i, true);
                }
                break;
            default:
            case "取消":
                if (doit.equals("")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dialogUtil.loadViewBottomdialog();
                            exitanimation();
                        }
                    });
                }
                for (int i = 0; i < messageLists.size(); i++) {
                    selectdevicemessageAdapter.getIsCheckBoxVisiable().put(i, false);
                    messageLists_local = SharedPreferencesUtil.getInfo_List(getActivity(), "messageLists");
                    if (messageLists_local.size() != 0) {
                        SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", messageLists);
                        for (int j = 0; j < messageLists_local.size(); j++) {
                            if (messageLists_local.get(j).get("id").equals(
                                    messageLists.get(i).get("id")
                            )) {
                                messageLists.get(i).put("ischecked", messageLists_local.get(j).get("ischecked"));
                            }
                        }
                    }
                }
                SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", messageLists);
                break;
        }
        selectdevicemessageAdapter.setList(messageLists);
    }


    @Override

    protected void onView() {
        registerMessageReceiver();
        xListView_scan.setOnItemClickListener(this);
        xListView_scan.setOnItemLongClickListener(this);
        selectdevicemessageAdapter = new SelectDeviceMessageAdapter(getActivity(), messageLists
        );
        xListView_scan.setAdapter(selectdevicemessageAdapter);
        xListView_scan.setPullLoadEnable(true);
        xListView_scan.setXListViewListener(this);
//        String stringBuffer = "1,2,3,";
//        String split = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
//        addViewid();
        all_read_linear.setOnClickListener(this);
        all_select_linear.setOnClickListener(this);
        delete_linear.setOnClickListener(this);
        dialogUtil = new DialogUtil(getActivity());
    }

    @Override
    public void initData() {//刷新数据，选择viewpager时刷新数据
        get_message(false, "doit");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_read_linear:
                //全部已读
                all_read();
                break;

            case R.id.all_select_linear://全选
                all_select(str);
                break;

            case R.id.delete_linear://删除
                delete();
                break;
        }
    }

    /**
     * 删除
     */
    private void delete() {
        String temp = ",";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < messageLists.size(); i++) {
            if ((boolean) messageLists.get(i).get("ischecked")) {
                stringBuffer.append(messageLists.get(i).get("id") + temp);
            }
        }
        String split = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        delete_select(split);
    }

    /**
     * 删除选中的
     *
     * @param split
     */
    private void delete_select(final String split) {
        final Map map = new HashMap();
//        String roomNo = roomNums.get(roomIndex);
        map.put("token", TokenUtil.getToken(getActivity()));
//        map.put("projectCode",projectCode);
        map.put("messageIds", split);
//        map.put("roomNo",roomNo == null ? "" : roomNo);
        MyOkHttp.postMapObject(ApiHelper.sraum_deleteMessage, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                mark_all_read(split);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(getActivity(), "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {
                String[] indexs = split.split(",");
                for (int j = 0; j < indexs.length; j++) {

                    for (int i = 0; i < messageLists.size(); i++) {
                        if (messageLists.get(i).get("id").equals(indexs[j])) {
                            messageLists.remove(i);
                        }
                    }
                }
                SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", messageLists);
                get_message(true, "doit");
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }


    /**
     * 消息全选
     *
     * @param str
     */
    private void all_select(String str) {

        switch (str) {
            case "全选":
                this.str = "全不选";
                for (int i = 0; i < messageLists.size(); i++) {
                    messageLists.get(i).put("ischecked", true);
                }
                break;
            case "全不选":
                this.str = "全选";
                for (int i = 0; i < messageLists.size(); i++) {
                    messageLists.get(i).put("ischecked", false);
                }
                break;
        }

        selectdevicemessageAdapter.setList(messageLists);
        SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", messageLists);
    }

    /**
     * 全部标记已读
     */
    private void all_read() {
        String temp = ",";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < messageLists.size(); i++) {
            if (i == messageLists.size() - 1) {
                stringBuffer.append(messageLists.get(i).get("id"));
            } else {
                stringBuffer.append(messageLists.get(i).get("id") + temp);
            }
        }
        mark_all_read(stringBuffer.toString());
    }


    /**
     * 标记为全部已读
     *
     * @param
     */
    private void mark_all_read(final String messageIds) {
        final Map map = new HashMap();
//        String roomNo = roomNums.get(roomIndex);
        map.put("token", TokenUtil.getToken(getActivity()));
//        map.put("projectCode",projectCode);
        map.put("messageIds", messageIds);
//        map.put("roomNo",roomNo == null ? "" : roomNo);
        MyOkHttp.postMapObject(ApiHelper.sraum_setReadStatus, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                mark_all_read(messageIds);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(getActivity(), "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {
//                for (int i = 0; i < messageList.size(); i++) {
////                    SelectDeviceMessageAdapter.getIsItemRead().put(i, true);
//                    selectdevicemessageAdapter.setList(messageList);
//                }
                get_message(true, "doit");
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (content) {
            case "编辑":
//                View v = parent.getChildAt(position - xListView_scan.getFirstVisiblePosition());
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
                cb.toggle();
                messageLists.get(position - 1).put("ischecked", cb.isChecked());
                SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", messageLists);
                break;
            case "取消":
            default:
                //
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("Message", (Serializable) messageLists.get(position - 1));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("peng", "MacFragment->onResume:name:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mMessageReceiver);
        SharedPreferencesUtil.saveInfo_List(getActivity(), "messageLists", new ArrayList<Map>());
    }
}
