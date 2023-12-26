package com.example.apptoyselling.ui.user.fragment.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.FragmentProfileBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;
import com.example.apptoyselling.ui.user.activity.signup.SignupActivity;
import com.example.apptoyselling.ui.utils.Utils;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private int passwordNotVisible = 1;
    Dialog dialog;
    Dialog dialogUpdate;
    ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading......");
        getDataUserFromDb();
        onClickLogout();
        onClickChangeInfor();
        onClickVisibilityPassword();

        return binding.getRoot();
    }
    private void onClickVisibilityPassword() {
        binding.imgShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    binding.imgShowPass.setImageResource(R.drawable.ic_visibility_off_pass);
                    binding.txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    binding.imgShowPass.setImageResource(R.drawable.ic_visibliti_pass);
                    binding.txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }
                binding.txtPassword.setSelection(binding.txtPassword.length());
            }
        });
    }
    private void onClickChangeInfor() {
        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });
    }

    private void onClickLogout() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SigninActivity.class));
            }
        });
    }

    private void getDataUserFromDb() {
        binding.txtName.setText(Utils.nameUser);
        binding.txtemail.setText(Utils.emailUser);
        binding.txtPhone.setText(Utils.phoneUser);
        binding.txtPassword.setText(Editable.Factory.getInstance().newEditable(Utils.passWordUser));
    }
    private void OpenDialogUpdateSuccess(){
        dialogUpdate = new Dialog(getContext());
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(R.layout.dialog_sua_thong_tin);
        Window window = dialogUpdate.getWindow();
        if (window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);
        dialogUpdate.setCancelable(true);
        Button dialogDuyTri= dialogUpdate.findViewById(R.id.btnDuyTri);
        Button dialogDangXuat = dialogUpdate.findViewById(R.id.btnDialogDangXuat);
        dialogDuyTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdate.dismiss();
            }
        });
        dialogDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(),SigninActivity.class));
                dialogUpdate.dismiss();
            }
        });
        dialogUpdate.show();
    }
    private void OpenDialog(){
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_profile);
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
        Button dialogOK = dialog.findViewById(R.id.btnOKEdit);
        Button dialogCancel = dialog.findViewById(R.id.btnCancleEidt);
        EditText edtName = dialog.findViewById(R.id.edtNameEdit);
        EditText edtEmail = dialog.findViewById(R.id.edtEmailEdit);
        EditText edtPhone = dialog.findViewById(R.id.edtPhoneEdit);
        EditText edtPass = dialog.findViewById(R.id.edtPassEdit);
        edtName.setText(Editable.Factory.getInstance().newEditable(binding.txtName.getText().toString()));
        edtEmail.setText(Editable.Factory.getInstance().newEditable(binding.txtemail.getText().toString()));
        edtPhone.setText(Editable.Factory.getInstance().newEditable(binding.txtPhone.getText().toString()));
        edtPass.setText(Editable.Factory.getInstance().newEditable(binding.txtPassword.getText().toString()));
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String phone= edtPhone.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                progressDialog.show();
                compositeDisposable.add(apiService.suataikhoan(name,phone,email,pass,Utils.idUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()){
                                        progressDialog.dismiss();
                                        OpenDialogUpdateSuccess();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable ->{
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        ));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}