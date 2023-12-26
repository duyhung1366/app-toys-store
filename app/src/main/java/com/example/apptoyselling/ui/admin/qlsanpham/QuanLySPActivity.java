package com.example.apptoyselling.ui.admin.qlsanpham;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.ActivityQuanLySpactivityBinding;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.admin.home.HomeAdminActivity;
import com.example.apptoyselling.ui.admin.qldonhang.QuanLyDHActivity;
import com.example.apptoyselling.ui.user.activity.details.DetailsActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class QuanLySPActivity extends AppCompatActivity implements DanhSachSPAdapter.IDanhSachSP{
    private ActivityQuanLySpactivityBinding binding;
    DanhSachSPAdapter danhSachSPAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    ArrayList<SanPham> sanPhamList;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    Dialog dialogDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_quan_ly_spactivity);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        sanPhamList = new ArrayList<>();
        if (isConnectedInternet(getContext())){
            getSanPham();
        }

        isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recylerSP.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecylerView();
        onCLickAdd();
        onClickBack();
    }

    private void initRecylerView() {
        danhSachSPAdapter = new DanhSachSPAdapter(this);
        binding.recylerSP.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recylerSP.setAdapter(danhSachSPAdapter);
    }

    private void getSanPham() {
        isLoading.setValue(true);
        compositeDisposable.add(apiService.getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            isLoading.setValue(false);
                            if (sanPhamModel.isSuccess()){
                                sanPhamList = (ArrayList<SanPham>) sanPhamModel.getResult();
                                Utils.listSPModel = sanPhamModel;
                                initRecylerView();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(),throwable.toString(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void onCLickAdd() {
        binding.btnAddSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLySPActivity.this,ThemSanPhamActivity.class));
            }
        });
    }
    private void onClickBack() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLySPActivity.this, HomeAdminActivity.class));
            }
        });
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public SanPham getData(int position) {
        return sanPhamList.get(position);
    }

    @Override
    public void onClickDelete(int idSP) {
        OpenDialog(idSP);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onCLickItem(int position) {
        Intent intent = new Intent(QuanLySPActivity.this, SuaSanPhamActivity.class);
        SanPham sanPham = sanPhamList.get(position);
        intent.putExtra("id",sanPham.getId());
        intent.putExtra("name",sanPham.getTenSP());
        intent.putExtra("img",sanPham.getHinhAnh());
        intent.putExtra("des",sanPham.getMoTa());
        intent.putExtra("price",sanPham.getGiaTien());
        intent.putExtra("origin",sanPham.getThuongHieu());
        intent.putExtra("type",sanPham.getType());
        startActivity(intent);
    }
    private void OpenDialog(int id){
        dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        Window window = dialogDelete.getWindow();
        if (window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);
        dialogDelete.setCancelable(true);
        Button dialogOK = dialogDelete.findViewById(R.id.btnOKDelete);
        Button dialogCancel = dialogDelete.findViewById(R.id.btnCancleDelete);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compositeDisposable.add(apiService.xoaSanPham(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel -> {
                                    if (messageModel.isSuccess()){
                                        Toast.makeText(getContext(),messageModel.toString(),Toast.LENGTH_SHORT).show();
                                        getSanPham();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getContext(),throwable.toString(),Toast.LENGTH_SHORT).show();
                                }
                        ));
                dialogDelete.dismiss();
            }
        });
        dialogDelete.show();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuanLySPActivity.this, HomeAdminActivity.class));
        super.onBackPressed();
    }
}