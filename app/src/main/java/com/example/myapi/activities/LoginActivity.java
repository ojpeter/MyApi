package com.example.myapi.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapi.R;
import com.example.myapi.api.RetrofitClient;
import com.example.myapi.models.LoginResponse;
import com.example.myapi.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPhone, editTextPassword;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewforgotPassword).setOnClickListener(this);
        findViewById(R.id.textViewSignUp).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()){

            if(SharedPrefManager.getInstance(this).getUser().getUposition() == "Passenger"){

                Intent intent = new Intent(this, PassengerProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            } else if(SharedPrefManager.getInstance(this).getUser().getUposition() == "Conductor"){
                Intent intent = new Intent(this, ConductorProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            } else if(SharedPrefManager.getInstance(this).getUser().getUposition() == "Manager"){
                Intent intent = new Intent(this, ManagerProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            }else {
                Intent intent = new Intent(this, AdminProfileActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }
    }

    private void userLogin() {
        String uphone = editTextPhone.getText().toString().trim();
        String upassword = editTextPassword.getText().toString().trim();

        if(uphone.isEmpty()){
            editTextPhone.setError("Enter your phone number");
            editTextPhone.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(uphone).matches()){
            editTextPhone.setError("Enter a valid mobile number");
            editTextPhone.requestFocus();
            return;
        }

        if(upassword.isEmpty()){
            editTextPassword.setError("The password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(upassword.length() < 6){
            editTextPassword.setError("The password should be at least 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().userLogin(uphone, upassword);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                SharedPrefManager.getInstance(LoginActivity.this)
                        .saveUser(loginResponse.getUser());

                if(loginResponse != null) {

                    if (response.code() == 201) {

                        Intent intent = new Intent(LoginActivity.this, AdminProfileActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    } else if (response.code() == 202) {

                        Intent intent = new Intent(LoginActivity.this, PassengerProfileActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    } else if (response.code() == 203) {

                        Intent intent = new Intent(LoginActivity.this, ManagerProfileActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    } else if (response.code() == 204) {

                        Intent intent = new Intent(LoginActivity.this, ConductorProfileActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    } else if (response.code() == 404) {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else if (response.code() == 405) {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Hold on, there is an Api failure", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:
                userLogin();
                break;

            case R.id.textViewSignUp:
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }

}