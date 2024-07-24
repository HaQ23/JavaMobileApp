package com.project.javapro;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.service.FileService;

import java.io.File;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

public class AddSlideActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText slideTitleEditText;
    private TextInputLayout slideTitleLayout;
    private Button saveSlideButton;
    private ImageView imageView;
    private Uri currentImageUri;
    private String lectureName;
    private FileService fileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slide);

        slideTitleLayout = findViewById(R.id.slideTitleLayout);
        slideTitleEditText = findViewById(R.id.slideTitle);
        saveSlideButton = findViewById(R.id.saveSlideButton);
        imageView = findViewById(R.id.imageView);

        lectureName = getIntent().getStringExtra("LECTURE_NAME");

        TextView lectureTitleTextView = findViewById(R.id.textViewLectureTitle);
        lectureTitleTextView.setText("Nazwa wykładu: " + lectureName);

        fileService = RetrofitClient.getRetrofitInstance().create(FileService.class);

        saveSlideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSlide();
            }
        });
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            currentImageUri = data.getData();
            imageView.setImageURI(currentImageUri);
        }
    }

    private void saveSlide() {
        String slideTitle = slideTitleEditText.getText().toString().trim();
        String randomFileName = UUID.randomUUID().toString();

        if (slideTitle.isEmpty()) {
            slideTitleLayout.setError("Musisz podać nazwę slajdu!");
            return;
        } else {
            slideTitleLayout.setError(null);
        }

        if (currentImageUri == null) {
            Toast.makeText(this, "Musisz wybrać zdjęcie!", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImage(currentImageUri, randomFileName);
    }

    private void uploadImage(Uri fileUri, String fileName) {
        String realPath = getRealPathFromURI(fileUri);

        if (realPath == null) {
            Toast.makeText(this, "Nie udało się uzyskać ścieżki do pliku!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(realPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        Call<ResponseBody> call = fileService.uploadLectureImage(body, lectureName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddSlideActivity.this, "Slajd został zapisany!", Toast.LENGTH_SHORT).show();
                    finish(); // Zakończ aktywność po zapisaniu slajdu
                } else {
                    Toast.makeText(AddSlideActivity.this, "Nie udało się przesłać zdjęcia!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddSlideActivity.this, "Błąd przesyłania zdjęcia: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (scheme != null && scheme.equals("file")) {
            filePath = uri.getPath();
        }

        return filePath;
    }
}
