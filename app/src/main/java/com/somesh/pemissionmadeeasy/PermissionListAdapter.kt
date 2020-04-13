package com.somesh.pemissionmadeeasy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.somesh.pemissionmadeeasy.PermissionListAdapter.PermissionHolder
import com.somesh.pemissionmadeeasy.databinding.ItemPermissionNameBinding
import java.util.*

/**
 * If this code works, it was written by Somesh Kumar on 24, December 2017. If not, I don’t know who wrote it.
 */
class PermissionListAdapter(private val permissionList: ArrayList<String>) : RecyclerView.Adapter<PermissionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionHolder {
        val itemBinding = ItemPermissionNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PermissionHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PermissionHolder, position: Int) = holder.bind(permissionList[position])

    override fun getItemCount(): Int = permissionList.size

    class PermissionHolder(private val itemBinding: ItemPermissionNameBinding) : ViewHolder(itemBinding.root) {
        fun bind(permission: String) = with(itemBinding) {
            tvName.text = permission
        }
    }
}