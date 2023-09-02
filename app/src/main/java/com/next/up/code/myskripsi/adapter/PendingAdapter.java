package com.next.up.code.myskripsi.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.next.up.code.myskripsi.core.data.api.response.item.SkripsiItem;
import com.next.up.code.myskripsi.databinding.ItemPendingBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ChildViewHolder> {
    private List<SkripsiItem> listPengajuan = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<SkripsiItem> newSkripsiItem) {
        if (newSkripsiItem == null) return;
        listPengajuan.clear();
        listPengajuan.addAll(newSkripsiItem);

        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(SkripsiItem skripsiItem);
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPendingBinding binding = ItemPendingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ChildViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        SkripsiItem data = listPengajuan.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return listPengajuan.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private final ItemPendingBinding binding;

        public ChildViewHolder(@NonNull ItemPendingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SkripsiItem skripsiItem) {
            binding.btnDetail.setOnClickListener(view -> {
                if (onItemClickCallback != null) {
                    onItemClickCallback.onItemClicked(skripsiItem);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.tvThesisTitle.setText(skripsiItem.getJudul());
                binding.tvName.setText(skripsiItem.getNamaMahasiswa());
                binding.tvNim.setText(skripsiItem.getNim());
                binding.tvTimeAgo.setText(skripsiItem.getCreatedTimeAgo());
            }
        }
    }
}
