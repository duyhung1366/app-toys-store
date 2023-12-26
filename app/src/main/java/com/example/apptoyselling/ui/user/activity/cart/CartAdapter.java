package com.example.apptoyselling.ui.user.activity.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.apptoyselling.databinding.ItemCartBinding;
import com.example.apptoyselling.model.Cart;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private ICart iCart;
    public CartAdapter(ICart iCart) {
        this.iCart = iCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CartViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Cart cart = iCart.getData(position);
        holder.binding.txtNameCart.setText(cart.getNameCart());
        if (cart.getImgCart().contains("http")){
            Glide.with(iCart.getContext()).load(cart.getImgCart()).into(holder.binding.imgCart);
        }else {
            String hinh = Utils.BASE_URL+cart.getImgCart();
            Glide.with(iCart.getContext()).load(hinh).into(holder.binding.imgCart);
        }
        holder.binding.txtPriceCart.setText(decimalFormat.format(cart.getPriceCart())+"đ");
        holder.binding.txtNumberOderCart.setText(String.valueOf(cart.getNumberOrder()));
        int number = Integer.parseInt(holder.binding.txtNumberOderCart.getText().toString());
        float sumPrice = number * cart.getPriceCart();
        holder.binding.txtSumPriceCart.setText(decimalFormat.format(sumPrice)+"đ");

        holder.binding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOrder = Integer.parseInt(holder.binding.txtNumberOderCart.getText().toString());
                iCart.onCLickMinus(position,cart.getIdCart(),numberOrder,holder.binding.txtNumberOderCart,holder.binding.txtSumPriceCart,holder.binding.btnMinus);
            }
        });
        holder.binding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOrder = Integer.parseInt(holder.binding.txtNumberOderCart.getText().toString());
                iCart.onClickPlus(position,cart.getIdCart(),numberOrder,holder.binding.txtNumberOderCart,holder.binding.txtSumPriceCart);
            }
        });
        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCart.onClickDelete(cart.getIdCart());
            }
        });
    }

    @Override
    public int getItemCount() {
        return iCart.getCount();
    }

    interface ICart{
        int getCount();
        Context getContext();
        Cart getData(int position);
        void onClickDelete(int id);
        void onCLickMinus(int position, int idCart, int numberOder, TextView txtNumberOrderCart, TextView txtSumPriceCart, ImageView btnMinus);
        void onClickPlus(int position, int idCart, int numberOder, TextView txtNumberOrderCart, TextView txtSumPriceCart);
    }
    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ItemCartBinding binding;
        public CartViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
