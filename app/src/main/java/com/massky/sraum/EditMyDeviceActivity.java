package com.massky.sraum;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import okhttp3.Call;


/**
 * Created by zhu on 2018/1/8.
 */

public class EditMyDeviceActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.input_panel_name_edit)
    ClearEditText input_panel_name_edit;

    @InjectView(R.id.edit_one)
    ClearEditText edit_one;
    @InjectView(R.id.button_one_id)
    Button button_one_id;

    @InjectView(R.id.edit_two)
    ClearEditText edit_two;
    @InjectView(R.id.button_two_id)
    Button button_two_id;


    @InjectView(R.id.edit_three)
    ClearEditText edit_three;
    @InjectView(R.id.button_three_id)
    Button button_three_id;


    @InjectView(R.id.edit_four)
    ClearEditText edit_four;
    @InjectView(R.id.button_four_id)
    Button button_four_id;

    @InjectView(R.id.linear_one)
    LinearLayout linear_one;
    @InjectView(R.id.linear_two)
    LinearLayout linear_two;
    @InjectView(R.id.linear_three)
    LinearLayout linear_three;
    @InjectView(R.id.linear_four)
    LinearLayout linear_four;

    @InjectView(R.id.first_txt)
    TextView first_txt;
    @InjectView(R.id.second_txt)
    TextView second_txt;
    @InjectView(R.id.three_txt)
    TextView three_txt;
    @InjectView(R.id.four_txt)
    TextView four_txt;
    private Map panelItem_map = new HashMap();
    @InjectView(R.id.next_step_txt)
    TextView next_step_txt;
    private String type = "";
    private String edit_one_txt_str = "";
    private String edit_two_txt_str = "";
    private String edit_three_txt_str = "";
    private String edit_four_txt_str = "";
    private String name = "";
    private boolean isPanelAndDeviceSame;
    private String number = "";
    private List<User.panellist> panelList = new ArrayList<>();
    private DialogUtil dialogUtil;
    private List<User.device> deviceList = new ArrayList<>();
    private String panelType;
    private String panelName;
    private String panelMAC;
    private int device_index;
    private String deviceNumber;
    private String input_panel_name_edit_txt_str;


    @Override
    protected int viewId() {
        return R.layout.edit_my_device_act;
    }

    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        dialogUtil = new DialogUtil(this);
        onEvent();
        onData();
    }


    private void onEvent() {
        back.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        next_step_txt.setOnClickListener(this);
        button_one_id.setOnClickListener(this);
        button_two_id.setOnClickListener(this);
        button_three_id.setOnClickListener(this);
        button_four_id.setOnClickListener(this);
    }


    private void onData() {
        panelItem_map = (Map) getIntent().getSerializableExtra("panelItem");
        if (panelItem_map != null) {
//            device_name_txt.setText(panelItem_map.get("name").toString());
//            project_select.setText(panelItem_map.get("name").toString());
//            mac_txt.setText(panelItem_map.get("mac").toString());
            type = panelItem_map.get("type").toString();
            name = panelItem_map.get("name").toString();
            number = panelItem_map.get("id").toString();
            input_panel_name_edit.setText(name);
            setCommon(type);
            getPanel_devices();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step_txt:
                switch (type) {
                    case "A201":
                    case "A202":
                    case "A203":
                    case "A204":
                    case "A301"://
                    case "A302":
                    case "A303":
                    case "A401":
                        save_panel();
                        break;
                    case "A501":
                    case "A511":
                    case "A801":
                    case "A901":
                    case "AB01":
                    case "A902":
                    case "AB04":
                    case "AC01":
                    case "AD01":
                    case "B001":
                        dialogUtil.loadDialog();
                        input_panel_name_edit_txt_str = input_panel_name_edit.getText().toString().trim() == null
                                || input_panel_name_edit.getText().toString().trim() == "" ? "" : input_panel_name_edit.getText().toString().trim();
                        if (input_panel_name_edit_txt_str.equals("")) {
                            ToastUtil.showToast(EditMyDeviceActivity.this, "设备名称为空");
                        } else {
                            sraum_update_panel_name(input_panel_name_edit_txt_str, number, false);//更新面板信息
                        }
                        break;
                }
                break;
            case R.id.back:
                EditMyDeviceActivity.this.finish();
                break;
            case R.id.button_one_id://找设备

                find_common_dev(deviceList.get(0).number);
                break;
            case R.id.button_two_id://找设备
                find_common_dev(deviceList.get(0).number);

                break;
            case R.id.button_three_id://找设备
                find_common_dev(deviceList.get(0).number);

                break;
            case R.id.button_four_id://找设备
                find_common_dev(deviceList.get(1).number);
                break;
        }
    }

    private void find_common_dev(String number2) {
        switch (panelType) {
            case "A201":
            case "A202":
            case "A203":
            case "A204":
            case "A301"://
            case "A302":
            case "A303":
                find_device(deviceList.get(0).number);
                break;
            case "A401":
                find_device(number2);
                break;
        }
    }

//    /**
//     * 保存数据
//     */
//    private void save_data() {
//
//    }

    /**
     * 保存面板
     */
    private void save_panel() {
        //                panelmac.setText(panelList.get(i).mac);
//                paneltype.setText(panelList.get(i).type);
//                panelname.setText(panelList.get(i).name);
//        String panelName = panelname.getText().toString().trim();
        int count_device = deviceList.size();
        //判断面板和设备名称是否相同，相同就不提交
        input_panel_name_edit_txt_str = input_panel_name_edit.getText().toString().trim() == null
                || input_panel_name_edit.getText().toString().trim() == "" ? "" : input_panel_name_edit.getText().toString().trim();
        edit_one_txt_str = edit_one.getText().toString().trim() == null
                || edit_one.getText().toString().trim() == "" ? "" : edit_one.getText().toString().trim();
        edit_two_txt_str = edit_two.getText().toString().trim() == null
                || edit_two.getText().toString().trim() == "" ? "" : edit_two.getText().toString().trim();
        edit_three_txt_str = edit_three.getText().toString().trim() == null
                || edit_three.getText().toString().trim() == "" ? "" : edit_three.getText().toString().trim();
        edit_four_txt_str = edit_four.getText().toString().trim() == null
                || edit_four.getText().toString().trim() == "" ? "" : edit_four.getText().toString().trim();
        List<String> list = new ArrayList<>();
        list.add(input_panel_name_edit_txt_str);
        list.add(edit_one_txt_str);
        list.add(edit_two_txt_str);
        list.add(edit_three_txt_str);
        list.add(edit_four_txt_str);

//                switch (count_device) {
//                    case 1:
//
//                        break;
//                    case 2:
//
//                        break;
//                    case 3:
//
//                        break;
//                    case 4:
//
//                        break;
//                }

        //遍历面板和设备名称有相同的吗
        isPanelAndDeviceSame = false;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if ((list.get(i).equals(list.get(j)) && !list.get(i).equals("")
                        && !list.get(j).equals(""))) {
                    isPanelAndDeviceSame = true;
                    break;
                }
            }
        }

        if (!isPanelAndDeviceSame) {
            for (int i = 0; i < count_device + 1; i++) {
                if (list.get(i).equals("")) {
                    isPanelAndDeviceSame = true;
                }
            }

            if (isPanelAndDeviceSame) {
                ToastUtil.showToast(EditMyDeviceActivity.this, "输入框不能为空");
            } else {
                dialogUtil.loadDialog();
                sraum_update_panel_name(input_panel_name_edit_txt_str, number, false);//更新面板信息
                //更新面板下的设备列表信息
//                int count_device = deviceList.size();
                //updateDeviceInfo();//更新设备信息
            }
        } else {
            ToastUtil.showToast(EditMyDeviceActivity.this, "所输入内容重复");
        }
    }

    /**
     * 找设备
     */
    private void find_device(final String deviceNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(EditMyDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(EditMyDeviceActivity.this));
        map.put("deviceNumber", deviceNumber);
        MyOkHttp.postMapObject(ApiHelper.sraum_findDevice, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取gogglen刷新数据
                find_device(deviceNumber);
            }
        }, EditMyDeviceActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                switch (user.result) {
                    case "100":
                        ToastUtil.showDelToast(EditMyDeviceActivity.this, "操作完成，查看对应设备");
                        break;
                    case "103":
                        ToastUtil.showDelToast(EditMyDeviceActivity.this, "设备编号不存在");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }


    public String replaceBlank(String src) {
        String dest = "";
        if (src != null) {
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(src);
            dest = matcher.replaceAll("");
        }
        return dest;
    }


    /**
     * 添加面板下的设备信息
     */
    private void getPanel_devices() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(EditMyDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(EditMyDeviceActivity.this));
        map.put("panelNumber", number);
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getPanel_devices();
                    }
                }, EditMyDeviceActivity.this, dialogUtil) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        panelList.clear();
                        deviceList.clear();
                        deviceList.addAll(user.deviceList);
                        //面板的详细信息
                        panelType = user.panelType;
                        panelName = user.panelName;
                        panelMAC = user.panelMAC;
                        panel_and_device_information();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    /**
     * 显示面板和设备内容信息
     */
    private void panel_and_device_information() {
        switch (panelType) {
            case "A201"://一灯控
                edit_one.setText(replaceBlank(deviceList.get(0).name));
                break;
            case "A202"://二灯控
                edit_one.setText(replaceBlank(deviceList.get(0).name));
                edit_two.setText(replaceBlank(deviceList.get(1).name));

                break;
            case "A203"://三灯控
                edit_one.setText(replaceBlank(deviceList.get(0).name));
                edit_two.setText(replaceBlank(deviceList.get(1).name));
                edit_three.setText(replaceBlank(deviceList.get(2).name));
                break;
            case "A204"://四灯控
                edit_one.setText(replaceBlank(deviceList.get(0).name));
                edit_two.setText(replaceBlank(deviceList.get(1).name));
                edit_three.setText(replaceBlank(deviceList.get(2).name));
                edit_four.setText(replaceBlank(deviceList.get(3).name));
                break;

            case "A301"://一键调光，3键灯控  设备4个
            case "A302"://两键调光，2键灯控
            case "A303"://三键调光，一键灯控
//            case "A304"://四键调光
                edit_one.setText(replaceBlank(deviceList.get(0).name));
                edit_two.setText(replaceBlank(deviceList.get(1).name));
                edit_three.setText(replaceBlank(deviceList.get(2).name));
                edit_four.setText(replaceBlank(deviceList.get(3).name));
                break;//一键-4键调光

            case "A401"://设备2个
                edit_one.setText(replaceBlank(deviceList.get(0).name));
                edit_two.setText(replaceBlank(deviceList.get(0).name1));
                edit_three.setText(replaceBlank(deviceList.get(0).name2));
                edit_four.setText(replaceBlank(deviceList.get(1).name));
                //窗帘前3个搞定，最后一个按钮为八键灯控名称修改
                break;//窗帘 ，窗帘第八个按钮为八键灯控名称修改

            case "A501"://空调-设备1个
            case "A511":
            case "A601"://新风
            case "A701"://地暖
//                edit_one.setText(deviceList.get(0).name);
                break;
            default:

                break;
            //updateDeviceInfo();
        }
    }


    /**
     * 更新设备信息
     */

    private void updateDeviceInfo() {
        switch (panelType) {
            case "A201"://一灯控
                edit_one_txt_str = edit_one.getText().toString().trim();
                device_index = 0;
                control_device_name_change_one(edit_one_txt_str, 0);
                break;
            case "A202"://二灯控
                edit_one_txt_str = edit_one.getText().toString().trim();
                edit_two_txt_str = edit_two.getText().toString().trim();
                device_index = 1;
                control_device_name_change_one(edit_one_txt_str, 0);//从0 -1 开始
                break;
            case "A203"://三灯控
                edit_one_txt_str = edit_one.getText().toString().trim();
                edit_two_txt_str = edit_two.getText().toString().trim();
                edit_three_txt_str = edit_three.getText().toString().trim();
                device_index = 2;
                control_device_name_change_one(edit_one_txt_str, 0);//从0 - 2开始
                break;
            case "A204"://四灯控
                edit_one_txt_str = edit_one.getText().toString().trim();
                edit_two_txt_str = edit_two.getText().toString().trim();
                edit_three_txt_str = edit_three.getText().toString().trim();
                edit_four_txt_str = edit_four.getText().toString().trim();
                device_index = 3;
                control_device_name_change_one(edit_one_txt_str, 0);//从0-3开始
                break;
            case "A301"://一键调光，3键灯控  设备4个
            case "A302"://两键调光，2键灯控
            case "A303"://三键调光，一键灯控
                edit_one_txt_str = edit_one.getText().toString().trim();
                edit_two_txt_str = edit_two.getText().toString().trim();
                edit_three_txt_str = edit_three.getText().toString().trim();
                edit_four_txt_str = edit_four.getText().toString().trim();
                device_index = 3;
                control_device_name_change_one(edit_one_txt_str, 0);//从0-3开始
//                control_device_name_change(twokey_device_txt_str,1);
//                control_device_name_change(threekey_device_txt_str,2);
//                control_device_name_change(fourkey_device_txt_str,3);

                break;//一键-4键调光

            case "A401"://设备2个

                final String customName = edit_one.getText().toString().trim();
                final String name1 = edit_two.getText().toString().trim();
                final String name2 = edit_three.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String deviceNumber = deviceList.get(0).number;
                        updateDeviceInfo(customName, name1, name2, deviceNumber, "窗帘前3", 0);
                    }
                }).start();


                //窗帘前3个搞定，最后一个按钮为八键灯控名称修改
                break;//窗帘 ，窗帘第八个按钮为八键灯控名称修改

            case "A501":
            case "A511":
            case "A801":
            case "A901":
            case "AB01":
            case "A902":
            case "AB04":
            case "AC01":
            case "AD01":
            case "B001":
                for (int i = 0; i < deviceList.size(); i++) {
                    input_panel_name_edit_txt_str = input_panel_name_edit.getText().toString().trim() == null
                            || input_panel_name_edit.getText().toString().trim() == "" ? "" : input_panel_name_edit.getText().toString().trim();
                    if (input_panel_name_edit_txt_str.equals("")) {
                        ToastUtil.showToast(EditMyDeviceActivity.this, "设备名称为空");
                        continue;
                    } else {
                        sraum_update_others(input_panel_name_edit_txt_str, deviceList.get(i).number, i);
                    }
                }
                break;
            case "A601"://新风
                updateDeviceInfo(edit_one.getText().toString().trim(), "", "",
                        deviceList.get(0).number, "", 0);
                break;
            case "A701"://地暖
                updateDeviceInfo(edit_one.getText().toString().trim(), "", "",
                        deviceList.get(0).number, "", 0);
                break;

            default:
                break;
//                updateDeviceInfo();
        }
    }

    /**
     * 窗帘第八键设备控制
     *
     * @param chuanglian
     */
    private void select_window_bajian(final String chuanglian) {
        deviceNumber = deviceList.get(1).number; //
        final String customName_window = edit_four.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDeviceInfo(customName_window, "", "", deviceNumber, chuanglian, 0);
            }
        }).start();
    }

    /**
     * 第一个设备控制
     *
     * @param device_name
     * @param index
     */
    private void control_device_name_change_one(final String device_name, final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                    updateDeviceInfo(device_name, "", "",
                            deviceList.get(index).number, "", index);
            }
        }).start();
    }

    /**
     * 第三个设备控制
     *
     * @param device_name
     * @param index
     */
    private void control_device_name_change_three(final String device_name, final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                    updateDeviceInfo(device_name, "", "",
                            deviceList.get(index).number, "", index);
            }
        }).start();
    }

    /**
     * 第二个设备控制
     *
     * @param device_name
     * @param index
     */
    private void control_device_name_change_two(final String device_name, final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                    updateDeviceInfo(device_name, "", "",
                            deviceList.get(index).number, "", index);
            }
        }).start();
    }

    /**
     * 第四个设备控制
     *
     * @param device_name
     * @param index
     */
    private void control_device_name_change_four(final String device_name, final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                    updateDeviceInfo(device_name, "", "",
                            deviceList.get(index).number, "", index);
            }
        }).start();
    }

    private void updateDeviceInfo(String customName, String name1, String name2, String deviceNumber, String chuanglian, int index) {
        sraum_update_s(customName, name1, name2, deviceNumber, chuanglian, index);
    }

    private void sraum_update_s(final String customName, final String name1, final String name2, final String deviceNumber, final String chuanglian, final int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(EditMyDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(EditMyDeviceActivity.this));
        map.put("deviceNumber", deviceNumber);
        map.put("customName", customName);
        if (chuanglian.equals("窗帘前3")) {
            map.put("name1", name1);
            map.put("name2", name2);
        }

        MyOkHttp.postMapObject(ApiHelper.sraum_updateDeviceInfo, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                sraum_update_s(customName, name1, name2, deviceNumber, chuanglian, index);
            }
        }, EditMyDeviceActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                select_device_byway("网关不正确");
            }

            @Override
            public void threeCode() {
                super.threeCode();
//                ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, "设备编号不正确");
                select_device_byway("设备编号不正确");
            }

            @Override
            public void fourCode() {
                super.fourCode();
//                ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, "自定义名称重复");
                select_device_byway("自定义名称重复");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
//                ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, "上传成功");
                select_device_byway("上传成功");
            }

            /**
             * 选设备4-1，依次执行
             * @param content
             */
            private void select_device_byway(String content) {//失败就提示添加失败名称，和失败类型
                if (chuanglian.equals("窗帘前3")) {
                    if (content.equals("上传成功")) {//当前不成功，就不继续加下去了
                        select_window_bajian("窗帘第八键");//控制窗帘第八键
                    } else {
                        ToastUtil.showDelToast(EditMyDeviceActivity.this, customName + ":" + content);
                    }
                    return;
                }

                //窗帘第八键
                if (chuanglian.equals("窗帘第八键")) {
                    if (content.equals("上传成功")) {//当前不成功，就不继续加下去了
                        //
                        ToastUtil.showToast(EditMyDeviceActivity.this, "更新成功");
//                        switch (findpaneltype) {
//                            case "fastedit"://快速编辑
//                                AppManager.getAppManager().finishActivity_current(FastEditPanelActivity.class);
//                                break;
//                            case "wangguan_status":
//                                break;
//                        }
                        EditMyDeviceActivity.this.finish();//修改完毕
                    } else {
                        ToastUtil.showDelToast(EditMyDeviceActivity.this, customName + ":" + content);
                    }
                    return;
                }

                int index_now = index;
                index_now++;
                if (content.equals("上传成功")) {//当前不成功，就不继续加下去了
                    switch (index_now) {
                        case 0://调1
                            control_device_name_change_one(edit_one_txt_str, 0);
                            //control_device_name_change_one(onekey_device_txt_str,3);
                            break;
                        case 1:
                            control_device_name_change_two(edit_two_txt_str, 1);
                            break;
                        case 2:
                            control_device_name_change_three(edit_three_txt_str, 2);
                            break;
                        case 3:
                            control_device_name_change_four(edit_four_txt_str, 3);
                            break;
                    }

                    if (index_now > device_index) {
                        ToastUtil.showToast(EditMyDeviceActivity.this, "更新成功");
//                        switch (findpaneltype) {
//                            case "fastedit"://快速编辑
//                                AppManager.getAppManager().finishActivity_current(FastEditPanelActivity.class);
//                                break;
//                            case "wangguan_status":
//                                break;
//                        }
                        EditMyDeviceActivity.this.finish();//修改完毕
                        return;
                    }
                } else {
                    ToastUtil.showDelToast(EditMyDeviceActivity.this, customName + ":" + content);
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }


    /**
     * 更新其他的设备
     *
     * @param customName
     * @param index
     */
    private void sraum_update_others(final String customName, final String deviceNumber, final int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(EditMyDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(EditMyDeviceActivity.this));
        map.put("deviceNumber", deviceNumber);
        map.put("customName", customName);
        MyOkHttp.postMapObject(ApiHelper.sraum_updateDeviceInfo, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                sraum_update_others(customName, deviceNumber, index);
            }
        }, EditMyDeviceActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
            }

            @Override
            public void threeCode() {
                super.threeCode();
            }

            @Override
            public void fourCode() {
                super.fourCode();
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                if (index == deviceList.size() - 1) {
                    EditMyDeviceActivity.this.finish();//修改完毕
                    ToastUtil.showToast(EditMyDeviceActivity.this, "更新成功");
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void sraum_update_panel_name(final String panelName, final String panelNumber, final boolean istrue) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(EditMyDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(EditMyDeviceActivity.this));
        map.put("panelNumber", panelNumber);
        map.put("panelName", panelName);
        MyOkHttp.postMapObject(ApiHelper.sraum_updatePanelName, map,
                new Mycallback(new AddTogglenInterfacer() {//刷新togglen获取新数据
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_update_panel_name(panelName, panelNumber, istrue);
                    }
                }, EditMyDeviceActivity.this, dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        EditMyDeviceActivity.this.finish();
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
//                        ChangePanelAndDeviceActivity.this.finish();
//                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this, panelName+":"+"面板名字更新成功");
                        if (!istrue) {
                            updateDeviceInfo();//更新设备信息
                        } else {
                            EditMyDeviceActivity.this.finish();
                            ToastUtil.showToast(EditMyDeviceActivity.this, "更新成功");
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void threeCode() {
                        super.threeCode();
                        ToastUtil.showToast(EditMyDeviceActivity.this, panelName + ":" + "面板编号不正确");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showToast(EditMyDeviceActivity.this, panelName + ":" + "面板名字已存在");
                    }
                });
    }

    private void setCommon(String type) {
        switch (type) {
            case "A201":
                linear_one.setVisibility(View.VISIBLE);
                first_txt.setText("第一路灯控名称");
                edit_one.setHint("输入第一路灯控自定义名称");
                break;
            case "A202":
                linear_one.setVisibility(View.VISIBLE);
                linear_two.setVisibility(View.VISIBLE);
                first_txt.setText("第一路灯控名称");
                second_txt.setText("第二路灯控名称");
                edit_one.setHint("输入第一路灯控自定义名称");
                edit_two.setHint("输入第二路灯控自定义名称");
                break;
            case "A203":
                linear_one.setVisibility(View.VISIBLE);
                linear_two.setVisibility(View.VISIBLE);
                linear_three.setVisibility(View.VISIBLE);
                first_txt.setText("第一路灯控名称");
                second_txt.setText("第二路灯控名称");
                three_txt.setText("第三路灯控名称");
                edit_one.setHint("输入第一路灯控自定义名称");
                edit_two.setHint("输入第二路灯控自定义名称");
                edit_three.setHint("输入第三路灯控自定义名称");
                break;
            case "A204":
                common_second("第一路灯控名称", "第二路灯控名称", "第三路灯控名称", "第四路灯控名称",
                        "输入第一路灯控自定义名称", "输入第二路灯控自定义名称", "输入第三路灯控自定义名称",
                        "输入第四路灯控自定义名称");
                break;
            case "A301"://
                common_second("第一路调光名称", "第二路灯控名称", "第三路灯控名称", "第四路灯控名称",
                        "输入第一路调光自定义名称", "输入第二路灯控自定义名称", "输入第三路灯控自定义名称",
                        "输入第四路灯控自定义名称");
                break;
            case "A302":
                common_second("第一路调光名称", "第二路调光名称", "第三路灯控名称", "第四路灯控名称",
                        "输入第一路调光自定义名称", "输入第二路调光自定义名称", "输入第三路灯控自定义名称",
                        "输入第四路灯控自定义名称");
                break;
            case "A303":
                common_second("第一路调光名称", "第二路调光名称", "第三路调光名称", "第四路灯控名称",
                        "输入第一路调光自定义名称", "输入第二路调光自定义名称", "输入第三路调光自定义名称",
                        "输入第四路灯控自定义名称");
                break;
            case "A401":
                common_second("窗帘名称", "第一路窗帘名称", "第二路窗帘名称", "第八路灯控名称",
                        "输入窗帘自定义名称", "输入第一路窗帘自定义名称", "输入第二路窗帘自定义名称",
                        "输入第八路灯控自定义名称");
                break;
            case "A501":
            case "A511":

                break;
            case "A801":

                break;
            case "A901":

                break;
            case "AB01":

                break;
            case "A902":

                break;
            case "AB04":

                break;
            case "AC01":

                break;
            case "AD01":

                break;
            case "B001":

                break;

        }
    }

    private void common_second(String name1, String name2, String name3, String name4,

                               String name5, String name6,

                               String name7, String name8) {
        common_promat(name1, name2, name3, name4);
        edit_one.setHint(name5);
        edit_two.setHint(name6);
        edit_three.setHint(name7);
        edit_four.setHint(name8);
    }

    /**
     * 提示公共方法
     *
     * @param name1
     * @param name2
     * @param name3
     * @param name4
     */
    private void common_promat(String name1, String name2, String name3, String name4) {
        linear_one.setVisibility(View.VISIBLE);//
        linear_two.setVisibility(View.VISIBLE);
        linear_three.setVisibility(View.VISIBLE);
        linear_four.setVisibility(View.VISIBLE);
        first_txt.setText(name1);
        second_txt.setText(name2);
        three_txt.setText(name3);
        four_txt.setText(name4);
    }
}
