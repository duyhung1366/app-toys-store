package com.example.apptoyselling.ui.user.activity.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.ActivitySigninBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.ui.admin.home.HomeAdminActivity;
import com.example.apptoyselling.ui.user.activity.home.HomeActivity;
import com.example.apptoyselling.ui.user.activity.signup.SignupActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SigninActivity extends AppCompatActivity {
    private ActivitySigninBinding binding;
    SharedPreferences sharedPreferences;
    private SQLiteHelper sqLiteHelper;
    private ArrayList<User> userArrayList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signin);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        sqLiteHelper = new SQLiteHelper(this,"toy.db",null,1);
        onCreateDatabase();
        sharedPreferences= getSharedPreferences("dataLogin",MODE_PRIVATE);
        //lấy giá trị
        binding.edtEmail.setText(sharedPreferences.getString("user",""));
        binding.edtPassword.setText(sharedPreferences.getString("pass",""));
        binding.checkbox.setChecked(sharedPreferences.getBoolean("checked",false));
        userArrayList = new ArrayList<>();
        onClickSignup();
        onClickSignin();
    }

    private void onClickSignin() {
        binding.btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtEmail = binding.edtEmail.getText().toString().trim();
                String edtPass = binding.edtPassword.getText().toString().trim();
                if (edtEmail.isEmpty() || edtPass.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Tài khoản mật khẩu không được để trống !!", Toast.LENGTH_SHORT).show();
                }else if (edtEmail.equals("admin@gmail.com") && edtPass.equals("admin")){
                    onCheckSaveInfo(edtEmail, edtPass);
                    Utils.checkDH = true;
                    Toast.makeText(SigninActivity.this, "Đăng nhập thành công !!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SigninActivity.this, HomeAdminActivity.class));
                }else {
                    progressDialog.show();
                      compositeDisposable.add(apiService.dangnhap(edtEmail,edtPass)
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                      userModel -> {
                                          if (userModel.isSuccess()){
                                              onCheckSaveInfo(edtEmail, edtPass);
                                              userArrayList = (ArrayList<User>) userModel.getResult();
                                              for (int i=0;i<userArrayList.size();i++){
                                                  Utils.idUser = userArrayList.get(i).getId();
                                                  Utils.nameUser = userArrayList.get(i).getName();
                                                  Utils.emailUser = userArrayList.get(i).getEmail();
                                                  Utils.phoneUser = userArrayList.get(i).getPhone();
                                                  Utils.passWordUser = userArrayList.get(i).getPassWord();
                                              }
                                              Utils.checkDH = false;
                                              Toast.makeText(SigninActivity.this, "Đăng nhập thành công !!", Toast.LENGTH_SHORT).show();
                                              startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                                              progressDialog.dismiss();
                                          }else {
                                              progressDialog.dismiss();
                                              Toast.makeText(SigninActivity.this,"Tài khoản hoặc mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
                                          }
                                      },
                                      throwable -> {
                                          Toast.makeText(SigninActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                      }
                              ));
                }
            }
        });
    }

    private void onCheckSaveInfo(String edtEmail, String edtPass) {
        if (binding.checkbox.isChecked()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user",edtEmail);
            editor.putString("pass",edtPass);
            editor.putBoolean("checked",true);
            editor.apply();
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user");
            editor.remove("pass");
            editor.remove("checked");
            editor.apply();
        }
    }

    private void onClickSignup() {
        binding.btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }
    private void onCreateDatabase() {
        sqLiteHelper.QueryData("CREATE TABLE IF NOT EXISTS CARTS(Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "IdSP INTEGER,"+
                "IdUser INTEGER,"+
                "Image NVARCHAR(100),"+
                "NameSP NVARCHAR(100),"+
                "Price FLOAT,"+
                "Description NVARCHAR(300),"+
                "Origin NVARCHAR(100),"+
                "NumberOrder INTEGER,"+
                "SumPrice FLOAT)");
        sqLiteHelper.QueryData("CREATE TABLE IF NOT EXISTS BILLS(Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "IdUser INTEGER,"+
                "IdDH NVARCHAR(100),"+
                "NameDH VARCHAR(100),"+
                "PhoneDH VARCHAR(100),"+
                "DiaChiDH VARCHAR(100),"+
                "PriceDH FLOAT,"+
                "StatusDH VARCHAR(100),"+
                "DateDH VARCHAR(100))");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}