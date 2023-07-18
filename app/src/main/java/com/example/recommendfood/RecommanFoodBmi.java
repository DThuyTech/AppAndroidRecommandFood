package com.example.recommendfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recommendfood.DataBase.AppDatabase;
import com.example.recommendfood.Model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecommanFoodBmi extends AppCompatActivity {
    TextView txtTenmonsang,txtTenmontrua,txtTenmmontoi,txtCalosang,txtCalotrua,txtCalotoi;
    ImageView img1,img2,img3;

    String type;
    int caloriesneed;
    int Calosangmax,Calosangmin;
    int Calotruamax,Calotruamin;
    int Calotoimax,Calotoimin;
    int[] listIdfoodS,listIdfoodT,listIdfoodTr;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendfoodbmr);

        //get intent
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        type = intent.getStringExtra("type");
        caloriesneed = bundle.getInt("user");
        Log.d("test", type);
        if(type.compareTo("Giam can")==0){
            Giamcan();
        }
        else if(type.compareTo("Tang can")==0){
            Tangcan();

        }else if(type.compareTo("Giu dang")==0){
            Giudang();
        }
        Init();
       Initcaloris();
       List<Food> mlistFoodRc = new ArrayList<Food>();
       mlistFoodRc = Xuly();



        Show(mlistFoodRc);



    }

    private void Test(){
        txtTenmonsang.setText(""+Calosangmin);
        txtCalosang.setText(""+Calosangmax);
        txtCalotoi.setText(""+Calotoimax);
        txtTenmmontoi.setText(""+Calotoimin);

    }
    private List<Food> Xuly(){

        int random  = (int) Math.floor(Math.random() * (listIdfoodS.length-1 - 0 + 1) + 0);
        Food foods = AppDatabase.getInstance(this).foodDao().getFoodbyId(listIdfoodS[random]);


        random  = (int) Math.floor(Math.random() * (listIdfoodTr.length-1 - 0 + 1) + 0);
        Food foodtr = AppDatabase.getInstance(this).foodDao().getFoodbyId(listIdfoodTr[random]);

        random  = (int) Math.floor(Math.random() * (listIdfoodT.length-1 - 0 + 1) + 0);
        Food foodt = AppDatabase.getInstance(this).foodDao().getFoodbyId(listIdfoodT[random]);

        List<Food> mlistFoodRc = new ArrayList<Food>();
        mlistFoodRc.add(foods);
        mlistFoodRc.add(foodtr);
        mlistFoodRc.add(foodt);
        return mlistFoodRc;

    }
    private List<Food> Sesion(String sesion,List<Food> foodList){
        List<Food> foods = new ArrayList<Food>();
        for(Food food : foodList){
            if(food.getSession().compareTo(sesion)==0){
                foods.add(food);
            }
        }
        return  foods;
    }

    private void Giudang() {


    }

    private void Tangcan() {
        caloriesneed = caloriesneed*127/100;
    }

    private void Giamcan() {
        caloriesneed = caloriesneed*85/100;
    }

    public void Init(){
        txtTenmonsang =findViewById(R.id.txt_tenmonbuoisang);
        txtTenmontrua = findViewById(R.id.txt_tenmonbuoitrua);
        txtTenmmontoi = findViewById(R.id.txt_tenmonbuoitoi);
        txtCalosang = findViewById(R.id.txt_calomonbuoisang);
        txtCalotrua = findViewById(R.id.txt_calomonbuoitrua);
        txtCalotoi = findViewById(R.id.txt_calomonbuoitoi);
        img1 = findViewById(R.id.img1);
        img2= findViewById(R.id.img2);
        img3=findViewById(R.id.img3);
        Calosangmax = caloriesneed/100*33;
        Calosangmin = caloriesneed/100*28;
        Calotruamax = caloriesneed/100*43;
        Calotruamin = caloriesneed/100*33;
        Calotoimin = caloriesneed/100*33;
        Calotoimax = caloriesneed/100*40;


    }

    public void Initcaloris(){

        listIdfoodS = AppDatabase.getInstance(this).foodDao().getIdFoodMaxMinSe(Calosangmin,Calosangmax);
        Log.d("test", listIdfoodS.length+"");
        listIdfoodTr = AppDatabase.getInstance(this).foodDao().getIdFoodMaxMinSe(Calotruamin,Calotruamax);
        listIdfoodT = AppDatabase.getInstance(this).foodDao().getIdFoodMaxMinSe(Calotoimin,Calotoimax);
    }

    public void Show(List<Food> mlistFood){
        Food food = mlistFood.get(0);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    img1.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmonsang.setText(food.getName());
        txtCalosang.setText(food.getCalo());

        food = mlistFood.get(1);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    img2.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmontrua.setText(food.getName());
        txtCalotrua.setText(food.getCalo());

        food = mlistFood.get(2);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+food.getId());


        try {
            File file = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    img3.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTenmmontoi.setText(food.getName());
        txtCalotoi.setText(food.getCalo());


    }
}
