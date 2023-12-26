package com.example.apptoyselling.ui.user.activity.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.ActivityDetailsBinding;
import com.example.apptoyselling.model.Cart;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.user.activity.cart.CartActivity;
import com.example.apptoyselling.ui.user.activity.cart.CartAdapter;
import com.example.apptoyselling.ui.user.activity.home.HomeActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private int idDetails;
    private String nameDetails;
    private String imgDetails;
    private float priceDetails;
    private String descriptionDetails;
    private String originDetails;
    private int numberOrder = 1;
    private SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_details);
        sqLiteHelper = new SQLiteHelper(this,"toy.db",null,1);
        onGetData();
        onClickBack();
        onClickMinus();
        onClickPlus();
        onClickAddToCart();
        onClickBuyNow();
    }

    private void onClickBuyNow() {
        binding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.cartArrayList.size() > 0){
                    int numberOrder = Integer.parseInt(binding.txtNumberOder.getText().toString());
                    boolean exits = false;
                    for(int i =0 ; i< Utils.cartArrayList.size();i++){
                        if (Utils.cartArrayList.get(i).getIdCart() == idDetails){
                            Utils.cartArrayList.get(i).setNumberOrder((Utils.cartArrayList.get(i).getNumberOrder() + numberOrder));
                            Utils.cartArrayList.get(i).setPriceCart((priceDetails * Utils.cartArrayList.get(i).getNumberOrder()));
                            sqLiteHelper.QueryData("UPDATE CARTS SET NumberOrder = '"+Utils.cartArrayList.get(i).getNumberOrder()+"' , SumPrice = '"+ Utils.cartArrayList.get(i).getPriceCart() + "' WHERE IdSP = '"+idDetails+"' AND IdUser = '"+Utils.idUser+"' ");
                            exits = true;
                        }
                    }
                    if (!exits){
                        float sumPrice = numberOrder*priceDetails;
                        Cart cart = new Cart();
                        cart.setIdCart(idDetails);
                        cart.setNameCart(nameDetails);
                        cart.setDesCart(descriptionDetails);
                        cart.setImgCart(imgDetails);
                        cart.setOriginCart(originDetails);
                        cart.setNumberOrder(numberOrder);
                        cart.setSumPrice(sumPrice);
                        cart.setPriceCart(priceDetails);
                        sqLiteHelper.QueryData("INSERT INTO CARTS VALUES(null,'"+ idDetails +"','"+ Utils.idUser +"','"+ imgDetails +"','"+ nameDetails +"','"+ priceDetails +"','"+ descriptionDetails +"','"+ originDetails +"','"+ numberOrder +"','"+ sumPrice +"')");
                    }
                }else {
                    int numberOrder = Integer.parseInt(binding.txtNumberOder.getText().toString());
                    float sumPrice = numberOrder*priceDetails;
                    Cart cart = new Cart();
                    cart.setIdCart(idDetails);
                    cart.setNameCart(nameDetails);
                    cart.setDesCart(descriptionDetails);
                    cart.setImgCart(imgDetails);
                    cart.setOriginCart(originDetails);
                    cart.setNumberOrder(numberOrder);
                    cart.setSumPrice(sumPrice);
                    cart.setPriceCart(priceDetails);
                    sqLiteHelper.QueryData("INSERT INTO CARTS VALUES(null,'"+ idDetails +"','"+ Utils.idUser +"','"+ imgDetails +"','"+ nameDetails +"','"+ priceDetails +"','"+ descriptionDetails +"','"+ originDetails +"','"+ numberOrder +"','"+ sumPrice +"')");
                }
                startActivity(new Intent(DetailsActivity.this, CartActivity.class));
            }
        });
    }

    private void onClickAddToCart() {
        binding.btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.cartArrayList.size() > 0){
                    boolean exits = false;
                    int numberOrder = Integer.parseInt(binding.txtNumberOder.getText().toString());
                    for(int i = 0 ; i< Utils.cartArrayList.size();i++){
                        if (Utils.cartArrayList.get(i).getIdCart() == idDetails){
                            Utils.cartArrayList.get(i).setNumberOrder(Utils.cartArrayList.get(i).getNumberOrder() + numberOrder);
                            Utils.cartArrayList.get(i).setPriceCart(priceDetails * Utils.cartArrayList.get(i).getNumberOrder());
                            exits = true;
                            sqLiteHelper.QueryData("UPDATE CARTS SET NumberOrder = '"+Utils.cartArrayList.get(i).getNumberOrder()+"' , SumPrice = '"+ Utils.cartArrayList.get(i).getPriceCart() + "' WHERE IdSP = '"+idDetails+"' AND IdUser = '"+Utils.idUser+"' ");
                        }
                    }
                    if (!exits){
                        float sumPrice = numberOrder*priceDetails;
                        Cart cart = new Cart();
                        cart.setIdCart(idDetails);
                        cart.setNameCart(nameDetails);
                        cart.setDesCart(descriptionDetails);
                        cart.setImgCart(imgDetails);
                        cart.setOriginCart(originDetails);
                        cart.setNumberOrder(numberOrder);
                        cart.setSumPrice(sumPrice);
                        cart.setPriceCart(priceDetails);
                        sqLiteHelper.QueryData("INSERT INTO CARTS VALUES(null,'"+ idDetails +"','"+ Utils.idUser +"','"+ imgDetails +"','"+ nameDetails +"','"+ priceDetails +"','"+ descriptionDetails +"','"+ originDetails +"','"+ numberOrder +"','"+ sumPrice +"')");
                    }
                }else {
                    int numberOrder = Integer.parseInt(binding.txtNumberOder.getText().toString());
                    float sumPrice = numberOrder*priceDetails;
                    Cart cart = new Cart();
                    cart.setIdCart(idDetails);
                    cart.setNameCart(nameDetails);
                    cart.setDesCart(descriptionDetails);
                    cart.setImgCart(imgDetails);
                    cart.setOriginCart(originDetails);
                    cart.setNumberOrder(numberOrder);
                    cart.setSumPrice(sumPrice);
                    cart.setPriceCart(priceDetails);
                    sqLiteHelper.QueryData("INSERT INTO CARTS VALUES(null,'"+ idDetails +"','"+ Utils.idUser +"','"+ imgDetails +"','"+ nameDetails +"','"+ priceDetails +"','"+ descriptionDetails +"','"+ originDetails +"','"+ numberOrder +"','"+ sumPrice +"')");
                }
                Toast.makeText(getApplicationContext(),"Thêm vào giỏ hàng thành công",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailsActivity.this, HomeActivity.class));
            }
        });
    }

    private void onClickPlus() {
        binding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOrder += 1;
                binding.txtNumberOder.setText(String.valueOf(numberOrder));
            }
        });
    }

    private void onClickMinus() {
        binding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(binding.txtNumberOder.getText().toString());
                if (number <= 1){
                    binding.btnMinus.setEnabled(false);
                }else {
                    numberOrder = number - 1;
                }
                binding.txtNumberOder.setText(String.valueOf(numberOrder));
            }
        });
    }

    private void onClickBack() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void onGetData() {
        Intent intent = getIntent();
        if (intent != null){
            idDetails = intent.getIntExtra("id",0);
            nameDetails = intent.getStringExtra("name");
            imgDetails = intent.getStringExtra("img");
            descriptionDetails = intent.getStringExtra("des");
            priceDetails = intent.getFloatExtra("price",0f);
            originDetails = intent.getStringExtra("origin");
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            if (imgDetails.contains("http")){
                Glide.with(this).load(imgDetails).into(binding.imgDetails);
            }else {
                String hinh = Utils.BASE_URL+imgDetails;
                Glide.with(this).load(hinh).into(binding.imgDetails);
            }
            binding.txtNameDetail.setText(nameDetails);
            binding.txtPriceDetail.setText(decimalFormat.format(priceDetails)+"đ");
            binding.txtXuatSu.setText(originDetails);
            binding.txtDes.setText(descriptionDetails);
        }
    }
}