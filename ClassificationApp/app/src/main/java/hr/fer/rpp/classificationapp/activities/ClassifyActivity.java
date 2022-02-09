package hr.fer.rpp.classificationapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import hr.fer.rpp.classificationapp.R;
import hr.fer.rpp.classificationapp.api.RetrofitClient;
import hr.fer.rpp.classificationapp.dialogs.LoadingDialog;
import hr.fer.rpp.classificationapp.models.Record;
import hr.fer.rpp.classificationapp.models.User;
import hr.fer.rpp.classificationapp.storage.SharedPrefManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassifyActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    File f = null;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        imageView = findViewById(R.id.viewImage);
        imageView.setImageResource(R.drawable.camera);

        findViewById(R.id.buttonTakePhoto).setOnClickListener(this);
        findViewById(R.id.buttonChooseFromGallery).setOnClickListener(this);
        findViewById(R.id.buttonUpload).setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bitmapToFile(Bitmap photo){
        try {
            f = new File(getApplicationContext().getCacheDir(), "f.png");
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmapToFile(photo);
            imageView.setImageBitmap(photo);

        } else if(requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                bitmapToFile(selectedImage);
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ClassifyActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ClassifyActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadPhoto(){
        User user = SharedPrefManager.getInstance(this).getUser();
        String email = user.getEmail();
        String password = user.getPassword();

        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), email);

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),f
        );

        MultipartBody.Part body = MultipartBody.Part.createFormData(
                "image", "f.png", requestFile
        );

        Call<Record> call = RetrofitClient
                .getInstance()
                .getApi()
                .uploadImage(username, body);

        final LoadingDialog loadingDialog = new LoadingDialog(ClassifyActivity.this);
        loadingDialog.startLoadingDialog();

        call.enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {

                try {
                    if(response.code() == 200){
                        loadingDialog.dismissDialog();
                        //Toast.makeText(ClassifyActivity.this,response.body().getImage().length(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ClassifyActivity.this, "Image uploaded successfully, go to your records to see results", Toast.LENGTH_SHORT).show();

                        //Intent loginIntent = new Intent(ClassifyActivity.this, RecordsActivity.class);
                        //startActivity(loginIntent);
                    } else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(ClassifyActivity.this, "Error while uploading the image", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e){
                    loadingDialog.dismissDialog();
                    Toast.makeText(ClassifyActivity.this, "Error while uploading the image", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(ClassifyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadPhotoFromGallery(){
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void takePhoto(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonTakePhoto:
                takePhoto();
                break;
            case R.id.buttonChooseFromGallery:
                loadPhotoFromGallery();
                break;
            case R.id.buttonUpload:
                //upload image to server using retrofit
                if(f != null)
                    uploadPhoto();
                else
                    Toast.makeText(ClassifyActivity.this, "No image was selected for upload", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
