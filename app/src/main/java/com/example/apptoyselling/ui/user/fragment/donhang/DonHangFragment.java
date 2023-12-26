package com.example.apptoyselling.ui.user.fragment.donhang;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.FragmentDonHangBinding;
import com.example.apptoyselling.model.DonHang;
import com.example.apptoyselling.ui.admin.qldonhang.QuanLyDHActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DonHangFragment extends Fragment implements DonHangAdapter.IDonHang{
    private FragmentDonHangBinding binding;
    private ArrayList<DonHang> donHangArrayList;
    private DonHangAdapter adapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_don_hang,container,false);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        donHangArrayList = new ArrayList<>();
        getDataUserFromDb();
        isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recylerDonHang.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecylerView();
        return binding.getRoot();
    }

    private void initRecylerView() {
        adapter = new DonHangAdapter(donHangArrayList,this);
        binding.recylerDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recylerDonHang.setAdapter(adapter);
    }

    private void getDataUserFromDb() {
        isLoading.setValue(true);
        compositeDisposable.add(apiService.getDonHangUser(Utils.idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            isLoading.setValue(false);
                            if (donHangModel.isSuccess()){
                                donHangArrayList = (ArrayList<DonHang>) donHangModel.getResult();
                                initRecylerView();
                            }else {
                                Toast.makeText(getContext(),donHangModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public void onClickXacNhanDH(String idDH) {

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
                                Toast.makeText(getContext(),donHangModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}