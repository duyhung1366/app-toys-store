package com.example.apptoyselling.ui.user.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.apptoyselling.databinding.ItemSanPhamBinding;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.user.activity.details.DetailsActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> implements Filterable {
    private Context context;
    private ArrayList<SanPham> sanPhamArrayList;
    private ArrayList<SanPham> mListUserOld;

    public SanPhamAdapter(Context context, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
        this.mListUserOld = sanPhamArrayList;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSanPhamBinding binding = ItemSanPhamBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SanPhamViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        SanPham sanPham = sanPhamArrayList.get(position);
        if (sanPham.getHinhAnh().contains("http")){
            Glide.with(context).load(sanPham.getHinhAnh()).into(holder.binding.imageSP);
        }else {
            String hinh = Utils.BASE_URL+sanPham.getHinhAnh();
            Glide.with(context).load(hinh).into(holder.binding.imageSP);
        }
        holder.binding.txtName.setText(sanPham.getTenSP());
        holder.binding.txtPrice.setText(decimalFormat.format(sanPham.getGiaTien())+"đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("id",sanPham.getId());
                intent.putExtra("name",sanPham.getTenSP());
                intent.putExtra("img",sanPham.getHinhAnh());
                intent.putExtra("des",sanPham.getMoTa());
                intent.putExtra("price",sanPham.getGiaTien());
                intent.putExtra("origin",sanPham.getThuongHieu());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty()){
                    sanPhamArrayList = mListUserOld;
                }else {
                    ArrayList<SanPham> listUser = new ArrayList<>();
                    for (SanPham sanPham : mListUserOld){
                        if (sanPham.getTenSP().toLowerCase().contains(search.toLowerCase())){
                            listUser.add(sanPham);
                        }
                    }
                    sanPhamArrayList = listUser;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sanPhamArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sanPhamArrayList = (ArrayList<SanPham>) results.values;
                notifyDataSetChanged();
            }
        };

    }


    public class SanPhamViewHolder extends RecyclerView.ViewHolder{
        ItemSanPhamBinding binding;
        public SanPhamViewHolder(@NonNull ItemSanPhamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
