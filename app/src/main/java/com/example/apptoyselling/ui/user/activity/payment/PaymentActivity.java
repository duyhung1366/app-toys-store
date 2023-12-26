package com.example.apptoyselling.ui.user.activity.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.ActivityPaymentBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.ui.user.activity.cart.CartActivity;
import com.example.apptoyselling.ui.user.activity.home.HomeActivity;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private SQLiteHelper sqLiteHelper;
    Dialog dialog;
    float pricePay;
    long time = System.currentTimeMillis();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    ProgressDialog progressDialog;
    String payment;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        sqLiteHelper = new SQLiteHelper(this,"toy.db",null,1);
        Intent intent = getIntent();
        pricePay = intent.getFloatExtra("pay",0f);
        binding.txtPricePayment.setText(decimalFormat.format(pricePay)+"đ");
        onCLickRadioButton();
        onClickBack();
        getDataUserFromDb();
        onClickPayment();
    }

    private void onClickPayment() {
        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtDiaChi = binding.edtAddressPayment.getText().toString().trim();
                if (edtDiaChi.isEmpty()){
                    Toast.makeText(PaymentActivity.this,"Địa chỉ nhận hàng không được để trống !",Toast.LENGTH_SHORT).show();
                }else if (!binding.radioMoney.isChecked() && !binding.radioCard.isChecked()){
                    Toast.makeText(PaymentActivity.this,"Vui lòng chọn phương thức thanh toán !",Toast.LENGTH_SHORT).show();
                }else {
                    addDataBilltoDB();
                }
            }
        });
    }

    private void onCLickRadioButton() {
        binding.radioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutCard.setVisibility(View.VISIBLE);
            }
        });
        binding.radioMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutCard.setVisibility(View.GONE);
            }
        });
    }

    private void getDataUserFromDb() {
        binding.txtNamePayment.setText(Utils.nameUser);
        binding.txtPhonePayment.setText(Utils.phoneUser);
    }

    private void onClickBack() {
        binding.imgBackPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, CartActivity.class));
            }
        });
    }
    private String date(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
    private void OpenDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment_success);
        Window window = dialog.getWindow();
        if (window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);
        dialog.setCancelable(true);
        Button dialogOK = dialog.findViewById(R.id.btnOkPayment);
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteListCart();
                startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void addDataBilltoDB(){
        payment = "";
        time++;
        String idHD = "HD"+time;
        String nameHD = binding.txtNamePayment.getText().toString().trim();
        String phoneHD = binding.txtPhonePayment.getText().toString().trim();
        String addressHD = binding.edtAddressPayment.getText().toString().trim();
        String status = "Chưa xác nhận";
        if (binding.radioCard.isChecked()){
            payment = "Thẻ ngân hàng";
        }
        if (binding.radioMoney.isChecked()){
            payment = "Tiền mặt";
        }
        String date = date();
        progressDialog.show();
        compositeDisposable.add(apiService.postDonHang(idHD,nameHD,phoneHD,addressHD,pricePay,status,date,Utils.idUser,payment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            if (donHangModel.isSuccess()){
                                OpenDialog();
                                progressDialog.dismiss();
                            }else {
                                Toast.makeText(PaymentActivity.this,donHangModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(PaymentActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void DeleteListCart(){
        sqLiteHelper.QueryData("DELETE FROM CARTS WHERE IdUser = '"+Utils.idUser+"' ");
    }
}