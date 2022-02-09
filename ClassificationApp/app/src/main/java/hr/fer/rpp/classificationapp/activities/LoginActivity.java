package hr.fer.rpp.classificationapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hr.fer.rpp.classificationapp.R;
import hr.fer.rpp.classificationapp.api.RetrofitClient;
import hr.fer.rpp.classificationapp.dialogs.LoadingDialog;
import hr.fer.rpp.classificationapp.models.User;
import hr.fer.rpp.classificationapp.storage.SharedPrefManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewSignUp).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //validation
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(email, password);

        String base = email + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        final Call<User> callLoggedUser = RetrofitClient
                .getInstance()
                .getApi()
                .getLoggedUser(authHeader);

        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.startLoadingDialog();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.code() == 200){
                        //Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();

                        callLoggedUser.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                try {
                                    if(response.code() == 200){
                                        loadingDialog.dismissDialog();

                                        User user = response.body();
                                        //save user
                                        //open profile
                                        SharedPrefManager.getInstance(LoginActivity.this)
                                                .saveUser(user);

                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);

                                        //Toast.makeText(LoginActivity.this, user.getEmail() + user.getRole(), Toast.LENGTH_LONG).show();
                                    } else {
                                        loadingDialog.dismissDialog();
                                        String s = response.errorBody().string();
                                        Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e){
                                    loadingDialog.dismissDialog();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(LoginActivity.this, "Email or password incorrect", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                Intent signUpIntent = new Intent(this, SignUpActivity.class);
                startActivity(signUpIntent);
                break;
        }
    }
}
