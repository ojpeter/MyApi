package com.example.myapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapi.R;
import com.example.myapi.api.RetrofitClient;
import com.example.myapi.models.DefaultResponse;
import com.example.myapi.storage.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPassword, editTextName, editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);


        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

            if (SharedPrefManager.getInstance(this).getUser().getUposition() == "Passenger") {

                Intent intent = new Intent(this, PassengerProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            } else if (SharedPrefManager.getInstance(this).getUser().getUposition() == "Conductor") {
                Intent intent = new Intent(this, ConductorProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            } else if (SharedPrefManager.getInstance(this).getUser().getUposition() == "Manager") {
                Intent intent = new Intent(this, ManagerProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            } else {
                Intent intent = new Intent(this, AdminProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }
    }

    private void userSignUp() {
        String uname = editTextName.getText().toString().trim();
        String uphone = editTextPhone.getText().toString().trim();
        String upassword = editTextPassword.getText().toString().trim();
        String uposition = "Passenger";
        //Pick date automatically
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        String date = simpleDateFormat.format(new Date());
        String ucreated = date;

        if(uname.isEmpty()){
            editTextName.setError("Please enter your name");
            editTextName.requestFocus();
            return;
        }

        if(uphone.isEmpty()){
            editTextPhone.setError("Please enter your phone number");
            editTextPhone.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(uphone).matches()){
            editTextPhone.setError("Enter a valid mobile number");
            editTextPhone.requestFocus();
            return;
        }

        if(upassword.isEmpty()){
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        if(upassword.length() < 6){
            editTextPassword.setError("The password should be at least 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(uname, uphone, upassword, uposition, ucreated);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.code() != 0) {
                    if (response.code() == 201) {

                        DefaultResponse dr = response.body();
                        Toast.makeText(SignupActivity.this, dr.getMsg(), Toast.LENGTH_SHORT).show();

                    } else if (response.code() == 422) {
                        Toast.makeText(SignupActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();

                    } else if (response.code() == 423) {
                        Toast.makeText(SignupActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignupActivity.this, "Hold on, there is an Api failure", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSignUp:
                userSignUp();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}