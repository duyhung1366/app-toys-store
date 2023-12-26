package com.example.apptoyselling.ui.user.activity.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.ActivityCategoryBinding;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.user.activity.home.HomeActivity;
import com.example.apptoyselling.ui.user.fragment.home.SanPhamAdapter;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryActivity extends AppCompatActivity {
    private ActivityCategoryBinding binding;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    ArrayList<SanPham> sanPhamList;
    String dataType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_category);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        sanPhamList = new ArrayList<>();
        getDataType();
        onClickBack();
        if (isConnectedInternet(this)){
            getDataCategory();
        }
        isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recylerCategory.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecylerView();
    }
    private void initRecylerView() {
        sanPhamAdapter = new SanPhamAdapter(this,sanPhamList);
        binding.recylerCategory.setLayoutManager(new GridLayoutManager(this,2));
        binding.recylerCategory.setAdapter(sanPhamAdapter);
    }
    private void getDataCategory() {
        isLoading.setValue(true);
        compositeDisposable.add(apiService.getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            isLoading.setValue(false);
                            if (sanPhamModel.isSuccess()){
                                ArrayList<SanPham> listSP = new ArrayList<>();
                                for (SanPham data :sanPhamModel.getResult()){
                                    if (data.getType().equals(dataType)){
                                        listSP.add(data);
                                    }
                                }
                                sanPhamList = listSP;
                                initRecylerView();
                            }
                        },
                        throwable -> {
                            Toast.makeText(CategoryActivity.this,throwable.toString(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void onClickBack() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, HomeActivity.class));
            }
        });
    }

    private void getDataType() {
        Intent intent = getIntent();
        dataType = intent.getStringExtra("type");
        if (dataType.equals("bupbe")){
            binding.txtTitle.setText("Mô hình búp bê");
        }else if (dataType.equals("oto")){
            binding.txtTitle.setText("Mô hình Ô tô");
        }else if (dataType.equals("robot")){
            binding.txtTitle.setText("Mô hình Robot");
        }
    }
    //Kiem tra ket noi internet
    private boolean isConnectedInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkWifi!=null && networkWifi.isConnected() || networkMobile!=null && networkMobile.isConnected()){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}