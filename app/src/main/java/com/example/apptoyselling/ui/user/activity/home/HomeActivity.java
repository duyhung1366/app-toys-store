package com.example.apptoyselling.ui.user.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.apptoyselling.R;
import com.example.apptoyselling.databinding.ActivityHomeBinding;
import com.example.apptoyselling.ui.user.activity.cart.CartActivity;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;
import com.example.apptoyselling.ui.user.fragment.donhang.DonHangFragment;
import com.example.apptoyselling.ui.user.fragment.home.HomeFragment;
import com.example.apptoyselling.ui.user.fragment.profile.ProfileFragment;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        getFragment(new HomeFragment());
        customBottomNavigation();
    }

    private void customBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    getFragment(new HomeFragment());
                    binding.bottomNavigation.getMenu().findItem(R.id.bottom_home).setChecked(true);
                    break;
                case R.id.bottom_cart:
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    binding.bottomNavigation.getMenu().findItem(R.id.bottom_cart).setChecked(true);
                    break;
                case R.id.bottom_bill:
                    getFragment(new DonHangFragment());
                    binding.bottomNavigation.getMenu().findItem(R.id.bottom_bill).setChecked(true);
                    break;
                case R.id.bottom_profile:
                    getFragment(new ProfileFragment());
                    binding.bottomNavigation.getMenu().findItem(R.id.bottom_profile).setChecked(true);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFragment(new HomeFragment());
        binding.bottomNavigation.getMenu().findItem(R.id.bottom_home).setChecked(true);
    }

    private void getFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,fragment,Fragment.class.getName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HomeActivity.this, SigninActivity.class));
        super.onBackPressed();
    }
}