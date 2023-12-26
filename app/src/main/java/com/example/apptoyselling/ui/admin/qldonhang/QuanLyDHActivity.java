package com.example.apptoyselling.ui.admin.qldonhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.ActivityQuanLyDhactivityBinding;
import com.example.apptoyselling.model.DonHang;
import com.example.apptoyselling.ui.admin.home.HomeAdminActivity;
import com.example.apptoyselling.ui.admin.qlsanpham.QuanLySPActivity;
import com.example.apptoyselling.ui.admin.qlsanpham.ThemSanPhamActivity;
import com.example.apptoyselling.ui.user.activity.payment.PaymentActivity;
import com.example.apptoyselling.ui.user.fragment.donhang.DonHangAdapter;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class QuanLyDHActivity extends AppCompatActivity implements DonHangAdapter.IDonHang{
    private ActivityQuanLyDhactivityBinding binding;
    private ArrayList<DonHang> donHangArrayList;
    private DonHangAdapter adapter;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_quan_ly_dhactivity);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        donHangArrayList = new ArrayList<>();
        getDataUserFromDb();
        isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recylerDonHang.setVisibility(View.VISIBLE);
                    binding.layoutDoanhThu.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecylerView();
        onClickBack();
    }
    @SuppressLint("SetTextI18n")
    private void tongDoanhThu(){
        float tongTien = 0;
        for (int i=0;i<donHangArrayList.size();i++){
            if (donHangArrayList.get(i).getStatusDH().equals("Đã xác nhận")){
                tongTien += donHangArrayList.get(i).getPriceDH();
            }
        }
        binding.txtTongDoanhThu.setText(decimalFormat.format(tongTien)+"đ");
    }
    private void onClickBack() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyDHActivity.this, HomeAdminActivity.class));
            }
        });
    }

    private void initRecylerView() {
        adapter = new DonHangAdapter(donHangArrayList,this);
        binding.recylerDonHang.setLayoutManager(new LinearLayoutManager(this));
        binding.recylerDonHang.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    private void getDataUserFromDb() {
        isLoading.setValue(true);
        if (donHangArrayList != null){
            donHangArrayList.clear();
        }
        compositeDisposable.add(apiService.getDonHangAdmin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            isLoading.setValue(false);
                            if (donHangModel.isSuccess()){
                                donHangArrayList = (ArrayList<DonHang>) donHangModel.getResult();
                                initRecylerView();
                                tongDoanhThu();
                            }else {
                                Toast.makeText(QuanLyDHActivity.this,donHangModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(QuanLyDHActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public void onClickXacNhanDH(String idDH) {
        String status = "Đã xác nhận";
        compositeDisposable.add(apiService.updateDonHang(idDH,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if (donHangModel.isSuccess()){
                                getDataUserFromDb();
                            }else {
                                Toast.makeText(QuanLyDHActivity.this,donHangModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(QuanLyDHActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    @Override
    public void onClickHuyDH(String idDH) {
        String status = "Đã hủy";
        compositeDisposable.add(apiService.updateDonHang(idDH,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if (donHangModel.isSuccess()){
                                getDataUserFromDb();
                            }else {
                                Toast.makeText(QuanLyDHActivity.this,donHangModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(QuanLyDHActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuanLyDHActivity.this,HomeAdminActivity.class));
        super.onBackPressed();
    }
}