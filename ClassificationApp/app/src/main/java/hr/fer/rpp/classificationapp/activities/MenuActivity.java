package hr.fer.rpp.classificationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
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

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.buttonClassify).setOnClickListener(this);
        findViewById(R.id.buttonRecords).setOnClickListener(this);
        findViewById(R.id.buttonLogout).setOnClickListener(this);

        textView = findViewById(R.id.textView);
        User user = SharedPrefManager.getInstance(this).getUser();
        textView.setText("Welcome back " + user.getFirstName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userLogout(){
        User user = SharedPrefManager.getInstance(this).getUser();

        String email = user.getEmail();
        String password = user.getPassword();

        String base = email + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        Call<ResponseBody> logoutCall = RetrofitClient
                .getInstance()
                .getApi()
                .logout(authHeader);

        final LoadingDialog loadingDialog = new LoadingDialog(MenuActivity.this);
        loadingDialog.startLoadingDialog();

        logoutCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.code() == 200){
                        loadingDialog.dismissDialog();
                        Toast.makeText(MenuActivity.this, "Successfully logged out", Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(MenuActivity.this).clear();

                        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(MenuActivity.this, "Logout error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonClassify:
                Intent classifyIntent = new Intent(this, ClassifyActivity.class);
                startActivity(classifyIntent);
                break;
            case R.id.buttonRecords:
                Intent recordsIntent = new Intent(this, RecordsActivity.class);
                startActivity(recordsIntent);
                break;
            case R.id.buttonLogout:
                userLogout();
                break;
        }
    }
}
