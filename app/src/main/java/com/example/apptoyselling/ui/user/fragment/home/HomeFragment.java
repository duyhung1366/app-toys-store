package com.example.apptoyselling.ui.user.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.FragmentHomeBinding;
import com.example.apptoyselling.model.Banner;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.user.activity.category.CategoryActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment{
    private FragmentHomeBinding binding;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    ArrayList<SanPham> sanPhamList;
    ArrayList<SanPham> mListUserOld;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    PopupMenu popupMenu;
    int current = 0;
    Runnable runnable;
    Handler handler;
    String type = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        mListUserOld = new ArrayList<>();
        khoitao();
        if (isConnectedInternet(getContext())){
            if (Utils.listSPModel != null){
                sanPhamList = (ArrayList<SanPham>) Utils.listSPModel.getResult();
                mListUserOld = sanPhamList;
                isLoading.setValue(false);
            }else {
                getSanPham();
            }
        }
        isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recylerSanPham.setVisibility(View.VISIBLE);
                }
            }
        });
        customViewpager();
        initRecylerView();
        onClickSearch();
        onClickFilter();
        onClickCategory();
        return binding.getRoot();
    }

    private void onClickCategory() {
        binding.layoutBupbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "bupbe";
               Intent intent = new Intent(getContext(), CategoryActivity.class);
               intent.putExtra("type",type);
               startActivity(intent);
            }
        });
        binding.layoutOto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "oto";
                Intent intent = new Intent(getContext(), CategoryActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
        binding.layoutRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "robot";
                Intent intent = new Intent(getContext(), CategoryActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
    }

    private void customViewpager() {
        ArrayList<Banner> bannerArrayList = new ArrayList<>();
        bannerArrayList.add(new Banner(R.drawable.banner1));
        bannerArrayList.add(new Banner(R.drawable.banner2));
        bannerArrayList.add(new Banner(R.drawable.banner3));
        bannerArrayList.add(new Banner(R.drawable.banner4));
        bannerArrayList.add(new Banner(R.drawable.banner5));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(bannerArrayList);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.circleIndicator.setViewPager(binding.viewPager);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                current = binding.viewPager.getCurrentItem();
                current++;
                if (current >= binding.viewPager.getAdapter().getCount()){
                    current = 0;
                }
                binding.viewPager.setCurrentItem(current,true);
                handler.postDelayed(runnable,3000);
            }
        };
        handler.postDelayed(runnable,3000);
    }

    private void onClickFilter() {
        binding.imgPopupFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(getContext(),v, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.popup_filter,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.itemTQ:
                                filter("Trung Quốc");
                                break;
                            case R.id.itemDuc:
                                filter("Đức");
                                break;
                            case R.id.itemMy:
                                filter("Mỹ");
                                break;
                            case R.id.itemVN:
                                filter("Việt Nam");
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }
    private void filter(String origin){
        ArrayList<SanPham> listUser = new ArrayList<>();
        for (SanPham sanPham : mListUserOld){
            if (sanPham.getThuongHieu().toLowerCase().contains(origin.toLowerCase())){
                listUser.add(sanPham);
            }
        }
        sanPhamList = listUser;
        initRecylerView();
    }
    private void onClickSearch() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sanPhamAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void khoitao() {
        sanPhamList = new ArrayList<>();
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
                                mListUserOld = sanPhamList;
                                initRecylerView();
                            }
                        },
                        throwable -> {
                           Toast.makeText(getContext(),throwable.toString(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initRecylerView() {
        sanPhamAdapter = new SanPhamAdapter(getContext(),sanPhamList);
        binding.recylerSanPham.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recylerSanPham.setAdapter(sanPhamAdapter);
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