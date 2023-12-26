package com.example.apptoyselling.ui.admin.qlsanpham;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptoyselling.databinding.ItemDsSanPhamAdminBinding;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;

public class DanhSachSPAdapter extends RecyclerView.Adapter<DanhSachSPAdapter.DanhSachSPViewHolder>{
    IDanhSachSP iDanhSachSP;

    public DanhSachSPAdapter(IDanhSachSP iDanhSachSP) {
        this.iDanhSachSP = iDanhSachSP;
    }

    @NonNull
    @Override
    public DanhSachSPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDsSanPhamAdminBinding binding = ItemDsSanPhamAdminBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DanhSachSPViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DanhSachSPViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        SanPham sanPham = iDanhSachSP.getData(position);
        if (sanPham.getHinhAnh().contains("http")){
            Glide.with(iDanhSachSP.getContext()).load(sanPham.getHinhAnh()).into(holder.binding.imageSP);
        }else {
            String hinh = Utils.BASE_URL+sanPham.getHinhAnh();
            Glide.with(iDanhSachSP.getContext()).load(hinh).into(holder.binding.imageSP);
        }
        holder.binding.txtName.setText(sanPham.getTenSP());
        holder.binding.txtPrice.setText(decimalFormat.format(sanPham.getGiaTien())+"đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDanhSachSP.onCLickItem(position);
            }
        });
        holder.binding.imgDeleteSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDanhSachSP.onClickDelete(sanPham.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return iDanhSachSP.getCount();
    }

    public interface IDanhSachSP{
        int getCount();
        SanPham getData(int position);
        void onClickDelete(int idSP);
        Context getContext();
        void onCLickItem(int position);
    }
    public class DanhSachSPViewHolder extends RecyclerView.ViewHolder{
        ItemDsSanPhamAdminBinding binding;
        public DanhSachSPViewHolder(@NonNull ItemDsSanPhamAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
