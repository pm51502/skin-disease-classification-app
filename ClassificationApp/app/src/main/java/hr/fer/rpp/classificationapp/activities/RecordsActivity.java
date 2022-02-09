package hr.fer.rpp.classificationapp.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import hr.fer.rpp.classificationapp.R;
import hr.fer.rpp.classificationapp.adapters.RecordListAdapter;
import hr.fer.rpp.classificationapp.api.RetrofitClient;
import hr.fer.rpp.classificationapp.dialogs.LoadingDialog;
import hr.fer.rpp.classificationapp.models.Record;
import hr.fer.rpp.classificationapp.models.User;
import hr.fer.rpp.classificationapp.storage.SharedPrefManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordsActivity extends AppCompatActivity {

    private Set<Record> records = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        User user = SharedPrefManager.getInstance(this).getUser();
        String email = user.getEmail();
        getUserRecords(email);
    }

    private void getUserRecords(String email){
        Call<Set<Record>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getRecords(email);

        final LoadingDialog loadingDialog = new LoadingDialog(RecordsActivity.this);
        loadingDialog.startLoadingDialog();

        call.enqueue(new Callback<Set<Record>>() {
            @Override
            public void onResponse(Call<Set<Record>> call, Response<Set<Record>> response) {
                loadingDialog.dismissDialog();
                try {
                    if(response.code() == 200){
                        records = response.body();

                        if(records.isEmpty()){
                            Toast.makeText(RecordsActivity.this, "No records yet, go to classify to upload images to server", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(RecordsActivity.this, records.size() + " records loaded", Toast.LENGTH_SHORT).show();

                        ListView mListView = (ListView) findViewById(R.id.listView);
                        ArrayList<Record> recordList = new ArrayList<>();
                        for(Record record : records)
                            recordList.add(record);

                        /*
                        if(recordList.size() > 0) {
                            Record res = recordList.get(0);
                            String message = res.getId() + " " + res.getUsername() + " " + res.getDiagnosis() + " " + res.getDate();
                            Toast.makeText(RecordsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                         */

                        RecordListAdapter adapter = new RecordListAdapter(RecordsActivity.this, R.layout.adapter_view_layout, recordList);
                        mListView.setAdapter(adapter);

                    } else {
                        Toast.makeText(RecordsActivity.this, "Error while getting records", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    Toast.makeText(RecordsActivity.this, "Error while getting records", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Set<Record>> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(RecordsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        //get all user's records from api using retrofit
        User user = SharedPrefManager.getInstance(this).getUser();
        String email = user.getEmail();
        getUserRecords(email);
    }
     */
}
