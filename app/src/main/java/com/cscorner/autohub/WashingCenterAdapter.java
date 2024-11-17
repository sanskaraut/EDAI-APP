package com.cscorner.autohub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cscorner.autohub.R;
import com.cscorner.autohub.WashingCenterOwner;

import java.util.List;

public class WashingCenterAdapter extends RecyclerView.Adapter<WashingCenterAdapter.ViewHolder> {

    private List<WashingCenterOwner> ownerList;

    public WashingCenterAdapter(List<WashingCenterOwner> ownerList) {
        this.ownerList = ownerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_washing_centre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WashingCenterOwner owner = ownerList.get(position);
        holder.nameText.setText(owner.getName());
        holder.usernameText.setText(owner.getUsername());
        holder.mobileText.setText(owner.getMobileNo());
    }

    @Override
    public int getItemCount() {
        return ownerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, usernameText, mobileText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name_text);
            usernameText = itemView.findViewById(R.id.username_text);
            mobileText = itemView.findViewById(R.id.mobile_text);
        }
    }
}