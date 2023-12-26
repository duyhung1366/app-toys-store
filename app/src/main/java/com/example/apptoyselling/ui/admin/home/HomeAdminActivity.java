package com.example.apptoyselling.ui.admin.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.apptoyselling.R;
import com.example.apptoyselling.databinding.ActivityHomeAdminBinding;
import com.example.apptoyselling.ui.admin.qldonhang.QuanLyDHActivity;
import com.example.apptoyselling.ui.admin.qlsanpham.QuanLySPActivity;
import com.example.apptoyselling.ui.admin.qlsanpham.ThemSanPhamActivity;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;

public class HomeAdminActivity extends AppCompatActivity {
    private ActivityHomeAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_admin);
        onCLickLogout();
        onCLickQuanLyDonHang();
        onCLickQuanLySanPham();
    }

    private void onCLickQuanLySanPham() {
        binding.layoutQLSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAdminActivity.this, QuanLySPActivity.class));
            }
        });
    }

    private void onCLickQuanLyDonHang() {
        binding.layoutQLDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAdminActivity.this, QuanLyDHActivity.class));
            }
        });
    }

    private void onCLickLogout() {
        binding.logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAdminActivity.this, SigninActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HomeAdminActivity.this, SigninActivity.class));
        super.onBackPressed();
    }
}