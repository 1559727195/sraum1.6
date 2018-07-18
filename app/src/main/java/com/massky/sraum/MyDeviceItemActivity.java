package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.support.percent.PercentRelativeLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;
import com.example.swipemenuview.SwipeMenuLayout;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by zhu on 2018/1/16.
 */

public class MyDeviceItemActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private Map panelItem_map = new HashMap();
    @InjectView(R.id.device_name_txt)
    TextView device_name_txt;
    @InjectView(R.id.mac_txt)
    TextView mac_txt;
    @InjectView(R.id.status_txt)
    TextView status_txt;
    @InjectView(R.id.project_select)
    TextView project_select;
    @InjectView(R.id.wangguan_set_rel)
    RelativeLayout wangguan_set_rel;
    @InjectView(R.id.scene_list_rel)
    RelativeLayout scene_list_rel;
    @InjectView(R.id.list_gujian_rel)
    PercentRelativeLayout list_gujian_rel;
    @InjectView(R.id.banben_txt)
    TextView banben_txt;
    @InjectView(R.id.panid_txt)
    TextView panid_txt;
    @InjectView(R.id.xindao_txt)
    TextView xindao_txt;
    @InjectView(R.id.gateway_id_txt)
    TextView gateway_id_txt;
    @InjectView(R.id.other_jiantou)
    ImageView other_jiantou;
    @InjectView(R.id.delete_device_rel)
    RelativeLayout delete_device_rel;
    private DialogUtil dialogUtil;
    private String panelNumber = "";
    @InjectView(R.id.next_step_id)
    Button next_step_id;

    @Override
    protected int viewId() {
        return R.layout.my_device_item_act;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        dialogUtil = new DialogUtil(this);
        back.setOnClickListener(this);
        wangguan_set_rel.setOnClickListener(this);
        scene_list_rel.setOnClickListener(this);
        next_step_id.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        panelItem_map = (Map) getIntent().getSerializableExtra("panelItem");
        if (panelItem_map != null) {
            device_name_txt.setText(panelItem_map.get("name").toString());
            project_select.setText(panelItem_map.get("name").toString());
            mac_txt.setText(panelItem_map.get("mac").toString());
            banben_txt.setText(panelItem_map.get("firmware").toString());
            panid_txt.setText(panelItem_map.get("hardware").toString());
            gateway_id_txt.setText(panelItem_map.get("gatewayid").toString());
            panelNumber = panelItem_map.get("id").toString();
            switch (panelItem_map.get("status").toString()) {
                case "0":
                    status_txt.setText("离线");
                    break;
                default:
                    status_txt.setText("在线");
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                MyDeviceItemActivity.this.finish();
                break;
            case R.id.wangguan_set_rel:
                if (panelItem_map != null) {
                    Intent intent = new Intent(MyDeviceItemActivity.this, EditMyDeviceActivity.class);
                    intent.putExtra("panelItem", (Serializable) panelItem_map);
                    startActivity(intent);
                    MyDeviceItemActivity.this.finish();
                }
                break;
            case R.id.scene_list_rel:
                if (list_gujian_rel.getVisibility() == View.VISIBLE) {
                    list_gujian_rel.setVisibility(View.GONE);
                    other_jiantou.setImageResource(R.drawable.bg_you);
                } else {
                    list_gujian_rel.setVisibility(View.VISIBLE);
                    other_jiantou.setImageResource(R.drawable.bg_xia);
                }
                break;
            case R.id.next_step_id://删除设备
                sraum_deletepanel(panelNumber);
                break;
        }
    }

    /**
     * 删除设备
     * @param
     */
    private void sraum_deletepanel(final String panelNumber) {
        dialogUtil.loadDialog();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", TokenUtil.getToken(MyDeviceItemActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(MyDeviceItemActivity.this));
        map.put("panelNumber", panelNumber);
        MyOkHttp.postMapObject(ApiHelper.sraum_deletePanel, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_deletepanel(panelNumber);
                    }
                },MyDeviceItemActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
