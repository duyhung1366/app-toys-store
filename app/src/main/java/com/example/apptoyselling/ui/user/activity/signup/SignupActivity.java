package com.example.apptoyselling.ui.user.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.ActivitySignupBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        binding.btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });
        binding.btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edtName.getText().toString().trim();
                String email = binding.edtEmailSignup.getText().toString().trim();
                String password = binding.edtPasswordSignup.getText().toString().trim();
                String phone = binding.edtPhone.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Dữ liệu không được để trống !",Toast.LENGTH_SHORT).show();
                }else {
                      progressDialog.show();
                      compositeDisposable.add(apiService.dangky(name,phone,email,password)
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                                            Toast.makeText(SignupActivity.this,userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignupActivity.this,userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                      throwable ->{
                                          progressDialog.dismiss();
                                          Toast.makeText(SignupActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                      }
                              ));
                }
            }
        });
    }
}