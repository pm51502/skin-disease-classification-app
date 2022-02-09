package hr.fer.rpp.classificationapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hr.fer.rpp.classificationapp.R;
import hr.fer.rpp.classificationapp.activities.LoginActivity;
import hr.fer.rpp.classificationapp.api.RetrofitClient;
import hr.fer.rpp.classificationapp.models.Image;
import hr.fer.rpp.classificationapp.models.Record;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordListAdapter extends ArrayAdapter<Record> {

    private Context mContext;
    private int mResource;

    public RecordListAdapter(Context context, int resource, ArrayList<Record> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        //get record information
        Long id = getItem(position).getId();
        String username = getItem(position).getUsername();
        String diagnosis = getItem(position).getDiagnosis();
        String date = getItem(position).getDate();

        //create record object with the information
        Record record = new Record(id, username, diagnosis, date);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDiagnosis = (TextView) convertView.findViewById(R.id.textViewDiagnosis);
        TextView tvDate = (TextView) convertView.findViewById(R.id.textViewDate);

        tvDiagnosis.setText(diagnosis);
        tvDate.setText(date);

        return convertView;
    }
}
