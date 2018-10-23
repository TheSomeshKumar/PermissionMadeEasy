package com.somesh.pemissionmadeeasy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * If this code works, it was written by Somesh Kumar on 24, December 2017. If not, I don’t know who wrote it.
 */
public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.PermissionHolder> {
    private ArrayList<String> permissionList;
    private TextView tvName;

    public PermissionListAdapter(ArrayList<String> permissionList) {
        this.permissionList = permissionList;
    }

    @NonNull
    @Override
    public PermissionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permission_name, parent, false);
        return new PermissionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionHolder holder, int position) {
        holder.tvName.setText(permissionList.get(position));
    }


    @Override
    public int getItemCount() {
        return permissionList.size();
    }

    static class PermissionHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        PermissionHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
