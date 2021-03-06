package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.base.BaseAdapter;
import com.data.User;
import com.massky.sraum.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by masskywcy on 2017-05-16.
 */

public class SelectLinkageAdapter extends android.widget.BaseAdapter {
    private List<User.panellist> list = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private Context context;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected = new HashMap<>();

    public SelectLinkageAdapter(Context context, List<User.panellist> list, List<Integer> listint, List<Integer> listintwo) {
        this.list = list;
        this.listint = listint;
        this.listintwo = listintwo;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
        this.context = context;
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < listint.size(); i++) {
            getIsSelected().put(i, false);
        }
    }


    @Override
    public int getCount() {
        return listint.size();
    }

    @Override
    public Object getItem(int position) {
        return listint.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.select_sensor_item, null);
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


//        int element = (Integer) list.get(position).get("image");
//        viewHolderContentType.img_guan_scene.setImageResource(element);
//        viewHolderContentType.panel_scene_name_txt.setText(list.get(position).get("name").toString());


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, DeviceExcuteOpenActivity.class);
////                context.startActivity(intent);
//
//
//            }
//        });
        viewHolderContentType.panel_scene_name_txt.setText(list.get(position).panelName);
        viewHolderContentType.gateway_name_txt.setText(list.get(position).boxName);
            viewHolderContentType.checkbox.setChecked(getIsSelected().get(position));
            if (getIsSelected().get(position)) {
                viewHolderContentType.img_guan_scene.setImageResource(listintwo.get(position));
            } else {
                viewHolderContentType.img_guan_scene.setImageResource(listint.get(position));
            }
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
      isSelected = isSelected;
    }

    public void setlist(List<User.panellist> list, List<Integer> listint, List<Integer> listintwo) {
        this.list = list;
        initDate();
        this.listint = listint;
        this.listintwo = listintwo;
    }


    class ViewHolderContentType {
        ImageView img_guan_scene;
        TextView panel_scene_name_txt;
        TextView execute_scene_txt;
        CheckBox checkbox;
        public TextView gateway_name_txt;
    }
}
