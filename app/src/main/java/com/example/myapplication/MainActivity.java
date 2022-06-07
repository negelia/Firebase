package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;

    EditText email, phone, pass, passCheck;
    Button btn;

    public Pattern patternPass = Pattern.compile("(?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{7,}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         email = findViewById(R.id.email);
         phone = findViewById(R.id.telephone);
         pass = findViewById(R.id.pass);
         passCheck = findViewById(R.id.passCheck);
         btn = findViewById(R.id.next);


        database = FirebaseDatabase
                .getInstance("https://application-e18a5-default-rtdb.firebaseio.com/")
                .getReference().child("table");

         btn.setOnClickListener(view -> {
             String getPhone = phone.getText().toString();
             String getEmail = email.getText().toString();
             String getPass = pass.getText().toString();
             String getPassCheck = passCheck.getText().toString();

             User user = new User (getPhone, getEmail, getPass);

             if (!getPhone.isEmpty() || !getEmail.isEmpty() || !getPass.isEmpty())
             {
                 if (Patterns.EMAIL_ADDRESS.matcher(getEmail).matches())
                 {
                     if (Patterns.PHONE.matcher(getPhone).matches())
                     {
                         if (patternPass.matcher(getPass).matches())
                         {
                             if (getPassCheck.equals(getPass))
                             {
                                 database.child(getPhone).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         phone.getText().clear();
                                         email.getText().clear();
                                         pass.getText().clear();
                                         passCheck.getText().clear();
                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                     }
                                 });
                             }
                             else
                             {Toast.makeText(this, "Пароли не совпадают !", Toast.LENGTH_SHORT).show();}
                         }
                         else
                         {Toast.makeText(this, "Введите пароль правильно !", Toast.LENGTH_SHORT).show();}
                     }
                     else
                     {Toast.makeText(this, "Введите телефон правильно !", Toast.LENGTH_SHORT).show();}
                 }
                 else
                 {Toast.makeText(this, "Введите почту правильно !", Toast.LENGTH_SHORT).show();}
             }
             else
             {Toast.makeText(this, "Введите все поля !", Toast.LENGTH_SHORT).show();}
         });
    }
}