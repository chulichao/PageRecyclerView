package com.clc.srv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.clc.srv.databinding.ItemViewBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> mDatas;
    public static ViewAdapter instance;

    public List<String> getDatas() {
        return mDatas;
    }

    public void setDatas(List<String> datas) {
        mDatas = datas;
    }

    public ViewAdapter(Context context) {
        mContext = context;
        instance = this;
        mDatas = new ArrayList<>();
        mDatas.add("0");
        mDatas.add("1");
        mDatas.add("2");
        mDatas.add("3");
        mDatas.add("4");
        mDatas.add("5");
        mDatas.add("6");
        mDatas.add("7");
        mDatas.add("8");
        mDatas.add("9");
        mDatas.add("10");
        mDatas.add("11");
        mDatas.add("12");
        mDatas.add("13");
        mDatas.add("14");
        mDatas.add("15");
        mDatas.add("16");
        mDatas.add("17");
        mDatas.add("18");
        mDatas.add("19");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_view, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (position == 4 || position == 9 || position == 14 || position == 19) {
            itemViewHolder.mTextView.setBackgroundColor(0xFFF44336);
        } else {
            itemViewHolder.mTextView.setBackgroundColor(0xFF2196F3);
        }
        itemViewHolder.mTextView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ItemViewHolder(@NonNull ItemViewBinding binding) {
            super(binding.getRoot());
            mTextView = binding.textView;
        }
    }
}
