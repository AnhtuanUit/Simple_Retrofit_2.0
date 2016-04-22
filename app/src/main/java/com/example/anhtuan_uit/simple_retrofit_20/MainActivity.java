package com.example.anhtuan_uit.simple_retrofit_20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anhtuan_uit.simple_retrofit_20.BaseConnection.FileUploadService;
import com.example.anhtuan_uit.simple_retrofit_20.BaseConnection.Webservice;
import com.example.anhtuan_uit.simple_retrofit_20.BaseConnection.ServiceGenerator;
import com.example.anhtuan_uit.simple_retrofit_20.DTO.Files;
import com.example.anhtuan_uit.simple_retrofit_20.DTO.User;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnGetImage = (Button) findViewById(R.id.btn_upload_file);

        btnLogin.setOnClickListener(this);
        btnGetImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginRequest("tuan4", "12345");
                break;
            case R.id.btn_upload_file:
                getImage();
                break;
        }
    }

    public void loginRequest(String username, String password) {
        Webservice userService = ServiceGenerator.createService(Webservice.class);
        Call<User> call = userService.login(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    Toast.makeText(MainActivity.this, "Login success!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Username: "  + user.data.username, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Login fail!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // handle execution failures like no internet connectivity
            }
        });
    }

    private void uploadFile(String picturePath) {
        File file = new File(Uri.parse(picturePath).toString());

        //Add part file with multipart
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);
        String fileExtension
                = MimeTypeMap.getFileExtensionFromUrl(Uri.parse(picturePath).toString());
        String mimeType
                = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        // add another part request string
        String description = "hello, this is description speaking";

        // finally, execute the request
        Call<Files> call = service.upload(description, body);
        call.enqueue(new Callback<Files>() {
            @Override
            public void onResponse(Call<Files> call,
                                   Response<Files> response) {
                Toast.makeText(MainActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Link upload: " + response.body().data.link, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Files> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void getImage(){
        //Check permisson for divice ...
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            uploadFile(picturePath);
        }
    }
}

