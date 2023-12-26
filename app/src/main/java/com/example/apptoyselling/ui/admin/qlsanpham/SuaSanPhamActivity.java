package com.example.apptoyselling.ui.admin.qlsanpham;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.ActivitySuaSanPhamBinding;
import com.example.apptoyselling.model.MessageModel;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuaSanPhamActivity extends AppCompatActivity {
    private ActivitySuaSanPhamBinding binding;
    int id;
    String name , img , des , origin,type;
    float price;
    String mediaPath;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sua_san_pham);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        onClickBack();
        onGetData();
        onClickUploadHinhAnhSP();
        onClickSuaSanPham();
    }

    private void onClickSuaSanPham() {
        binding.btnSuaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenSP = binding.edtTenSP.getText().toString().trim();
                String moTa = binding.edtMoTaSP.getText().toString().trim();
                String giaSP = binding.edtGiaTienSP.getText().toString().trim();
                String thuongHieu = binding.edtThuongHieuSP.getText().toString().trim();
                String hinhAnh = binding.hinhAnhSP.getText().toString().trim();
                String type = binding.edtType.getText().toString().trim();
                if(tenSP.isEmpty() || moTa.isEmpty() || giaSP.isEmpty() || thuongHieu.isEmpty() || hinhAnh.isEmpty() || type.isEmpty()){
                    Toast.makeText(SuaSanPhamActivity.this,"Vui lòng nhập đủ thông tin !",Toast.LENGTH_SHORT).show();
                }else {
                    float giaTien = Float.parseFloat(giaSP);
                    compositeDisposable.add(apiService.updateSP(tenSP,hinhAnh,moTa,giaTien,thuongHieu,type,id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        if (messageModel.isSuccess()){
                                            startActivity(new Intent(SuaSanPhamActivity.this,QuanLySPActivity.class));
                                            Toast.makeText(SuaSanPhamActivity.this,messageModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(SuaSanPhamActivity.this,messageModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(SuaSanPhamActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));

                }
            }
        });
    }

    private void onClickUploadHinhAnhSP() {
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SuaSanPhamActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }
    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if (cursor == null){
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }
    private void onGetData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        name = intent.getStringExtra("name");
        img = intent.getStringExtra("img");
        des = intent.getStringExtra("des");
        price = intent.getFloatExtra("price",0f);
        origin = intent.getStringExtra("origin");
        type = intent.getStringExtra("type");
        binding.edtTenSP.setText(Editable.Factory.getInstance().newEditable(name));
        binding.hinhAnhSP.setText(Editable.Factory.getInstance().newEditable(img));
        binding.edtMoTaSP.setText(Editable.Factory.getInstance().newEditable(des));
        binding.edtGiaTienSP.setText(Editable.Factory.getInstance().newEditable(String.valueOf(price)));
        binding.edtThuongHieuSP.setText(Editable.Factory.getInstance().newEditable(origin));
        binding.edtType.setText(Editable.Factory.getInstance().newEditable(type));
    }

    private void onClickBack() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuaSanPhamActivity.this, QuanLySPActivity.class));
            }
        });
    }
    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);
        File file = new File(getPath(uri));
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call<MessageModel> call = apiService.uploadFile(fileToUpload1);
        call.enqueue(new Callback< MessageModel >() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                MessageModel serverRespone = response.body();
                if (serverRespone != null){
                    if (serverRespone.isSuccess()){
                        binding.hinhAnhSP.setText(Editable.Factory.getInstance().newEditable(serverRespone.getName()));
                    }else {
                        Toast.makeText(SuaSanPhamActivity.this,serverRespone.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.v("Respone",serverRespone.toString());
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("failed",t.toString());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuaSanPhamActivity.this,QuanLySPActivity.class));
        super.onBackPressed();
    }
}