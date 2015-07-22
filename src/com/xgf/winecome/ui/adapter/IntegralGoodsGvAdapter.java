
package com.xgf.winecome.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgf.winecome.R;
import com.xgf.winecome.entity.Goods;

public class IntegralGoodsGvAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<Goods> mDatas;

    private LayoutInflater mInflater;




    public IntegralGoodsGvAdapter(Context context, ArrayList<Goods> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.integral_gv_item_layout, null);

            holder = new ViewHolder();
            holder.mGoodsIcon = (ImageView) convertView
                    .findViewById(R.id.integral_gv_item_icon_iv);
            holder.mGoodsName = (TextView) convertView
                    .findViewById(R.id.integral_gv_item_name_tv);
            holder.mGoodsCategory = (TextView) convertView
                    .findViewById(R.id.integral_gv_item_score_tv);
           

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
    
        holder.mGoodsName.setText(mDatas.get(position).getName());
        return convertView;
    }

   
    static class ViewHolder {

        public ImageView mGoodsIcon;

        public TextView mGoodsName;

        public TextView mGoodsCategory;

    }

}
