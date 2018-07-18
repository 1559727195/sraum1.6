package com.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.massky.sraum.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by masskywcy on 2017-05-16.
 */

public class MyDeviceAdapter extends android.widget.BaseAdapter {
    private List<Map> list = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private int temp = -1;
    private Activity activity;//上下文
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected = new HashMap<>();

    public MyDeviceAdapter(Activity context, List<Map> list, List<Integer> listint, List<Integer> listintwo, SelectSensorListener selectSensorListener) {
        this.list = list;
        this.listint = listint;
        this.listintwo = listintwo;
        this.activity = context;
        this.selectSensorListener = selectSensorListener;
        this.activity = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderContentType viewHolderContentType = null;
        if (null == convertView) {
            viewHolderContentType = new ViewHolderContentType();
            convertView = LayoutInflater.from(activity).inflate(R.layout.my_device_item, null);
            viewHolderContentType.img_guan_scene = (ImageView) convertView.findViewById(R.id.img_guan_scene);
            viewHolderContentType.panel_scene_name_txt = (TextView) convertView.findViewById(R.id.panel_scene_name_txt);
            viewHolderContentType.execute_scene_txt = (TextView) convertView.findViewById(R.id.execute_scene_txt);
            viewHolderContentType.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            //gateway_name_txt
            viewHolderContentType.gateway_name_txt = (TextView) convertView.findViewById(R.id.gateway_name_txt);

            convertView.setTag(viewHolderContentType);
        } else {
            viewHolderContentType = (ViewHolderContentType) convertView.getTag();
        }


        viewHolderContentType.panel_scene_name_txt.setText((String) list.get(position).get("name"));
//        viewHolderContentType.gateway_name_txt.setText((String) list.get(position).get("boxName"));
        if (position < listint.size())
            viewHolderContentType.img_guan_scene.setImageResource(listint.get(position));
        return convertView;
    }

    public void setLists(List<Map> list_hand_scene, List<Integer> listint, List<Integer> listintwo) {
        this.list = list_hand_scene;
        this.listint = listint;
        this.listintwo = listintwo;
    }

//    public static HashMap<Integer, Boolean> getIsSelected() {
//        return isSelected;
//    }
//
//    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//        SelectSensorSingleAdapter.isSelected = isSelected;
//    }


    public static class ViewHolderContentType {
        public ImageView img_guan_scene;
        public TextView panel_scene_name_txt;
        public TextView execute_scene_txt;
        public CheckBox checkbox;
        public TextView gateway_name_txt;
    }

    public SelectSensorListener selectSensorListener;

    public interface SelectSensorListener {
        void selectsensor(int position);
    }
}
