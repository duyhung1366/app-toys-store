package com.example.apptoyselling.ui.user.fragment.donhang;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptoyselling.R;
import com.example.apptoyselling.databinding.ItemDonHangBinding;
import com.example.apptoyselling.model.DonHang;
import com.example.apptoyselling.ui.utils.Utils;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder> {
    private ArrayList<DonHang> donHangArrayList;
    private IDonHang iDonHang;

    public DonHangAdapter(ArrayList<DonHang> donHangArrayList, IDonHang iDonHang) {
        this.donHangArrayList = donHangArrayList;
        this.iDonHang = iDonHang;
    }

    public interface IDonHang{
        void onClickXacNhanDH(String idDH);
        void onClickHuyDH(String idDH);
    }
    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDonHangBinding binding = ItemDonHangBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DonHangViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        DonHang donHang = donHangArrayList.get(position);
        holder.binding.txtMaDH.setText(donHang.getIdDH());
        holder.binding.txtNameDH.setText(donHang.getNameDH());
        holder.binding.txtPhoneDH.setText(donHang.getPhoneDH());
        holder.binding.txtDiaChiDH.setText(donHang.getDiachiDH());
        holder.binding.txtTongTienDH.setText(decimalFormat.format(donHang.getPriceDH())+"đ");
        holder.binding.txtTrangThaiDH.setText(donHang.getStatusDH());
        holder.binding.txtDate.setText(donHang.getDate());
        holder.binding.txtPayment.setText(donHang.getPayment());
        Log.d("TAG", "Utils.checkDH: " + Utils.checkDH);
        if (Utils.checkDH){
            holder.binding.btnXacNhan.setVisibility(View.VISIBLE);
        }else {
            holder.binding.btnXacNhan.setVisibility(View.GONE);
        }
        Log.d("TAG", "donHang: " + donHang.getIdDH() + " - " + donHang.getStatusDH());
        if (donHang.getStatusDH().equals("Đã xác nhận")){
            holder.binding.btnXacNhan.setEnabled(false);
            holder.binding.btnHuyDH.setEnabled(false);
            holder.binding.txtTrangThaiDH.setTextColor(Color.parseColor("#1E97BD"));
        }else {
            holder.binding.txtTrangThaiDH.setTextColor(Color.parseColor("#F42A2A"));
            holder.binding.btnXacNhan.setEnabled(true);
            holder.binding.btnHuyDH.setEnabled(true);
        }
        holder.binding.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonHang.onClickXacNhanDH(donHang.getIdDH());
            }
        });
        holder.binding.btnHuyDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonHang.onClickHuyDH(donHang.getIdDH());
            }
        });
    }

    @Override
    public int getItemCount() {
        return donHangArrayList.size();
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder{
        ItemDonHangBinding binding;
        public DonHangViewHolder(@NonNull ItemDonHangBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
