package com.example.apptoyselling.data.api;
import io.reactivex.Observable;

import com.example.apptoyselling.model.DonHangModel;
import com.example.apptoyselling.model.MessageModel;
import com.example.apptoyselling.model.SanPhamModel;
import com.example.apptoyselling.model.UserModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    @GET("getsanpham.php")
    Observable<SanPhamModel> getSanPham();

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSP(
            @Field("tenSP") String tenSP,
            @Field("hinhAnh") String hinhAnh,
            @Field("moTa") String moTa,
            @Field("giaTien") float giaTien,
            @Field("thuongHieu") String thuongHieu,
            @Field("type") String type
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<SanPhamModel> xoaSanPham(
            @Field("id") int id
    );

    @POST("update.php")
    @FormUrlEncoded
    Observable<MessageModel> updateSP(
            @Field("tenSP") String tenSP,
            @Field("hinhAnh") String hinhAnh,
            @Field("moTa") String moTa,
            @Field("giaTien") float giaTien,
            @Field("thuongHieu") String thuongHieu,
            @Field("type") String type,
            @Field("id") int id
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("passWord") String passWord
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("passWord") String passWord
    );

    @POST("suataikhoan.php")
    @FormUrlEncoded
    Observable<UserModel> suataikhoan(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("passWord") String passWord,
            @Field("id") int id
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> postDonHang(
            @Field("idDH") String idDH,
            @Field("nameDH") String nameDH,
            @Field("phoneDH") String phoneDH,
            @Field("diachiDH") String diachiDH,
            @Field("priceDH") float priceDH,
            @Field("statusDH") String statusDH,
            @Field("date") String date,
            @Field("id") int id,
            @Field("payment") String payment
    );

    @POST("getdonhanguser.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonHangUser(
            @Field("id") int id
    );
    @GET("getdonhangadmin.php")
    Observable<DonHangModel> getDonHangAdmin();

    @POST("updatedonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> updateDonHang(
            @Field("idDH") String idDH,
            @Field("statusDH") String statusDH
    );
}

