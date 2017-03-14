package com.iqvis.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by IQVIS on 10/24/2016.

 Copyright 2016 IQVIS. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 "http://www.apache.org/licenses/LICENSE-2.0"

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. */
public class DeviceListAdapter extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public DeviceListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.device_list_view_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemName = (TextView)view.findViewById(R.id.tv_deviceName);
        TextView listItemStatus = (TextView)view.findViewById(R.id.tv_pair_unpair);

        listItemName.setText(list.get(position));
        listItemStatus.setText(R.string.text_pair);

        //Handle buttons and add onClickListeners
//        listItemStatus.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                list.get(position); //or some other task
//                notifyDataSetChanged();
//            }
//        });
        return view;
    }

}

