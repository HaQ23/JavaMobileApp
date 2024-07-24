package com.project.javapro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.service.LectureService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLectureActivity extends AppCompatActivity {

    private TextInputEditText lectureTitleEditText;
    private TextInputLayout lectureTitleLayout;
    private Button saveLectureButton;
    private LectureService lectureService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        lectureTitleLayout = findViewById(R.id.lectureTitleLayout);
        lectureTitleEditText = findViewById(R.id.lectureTitle);
        saveLectureButton = findViewById(R.id.saveLectureButton);

        lectureService = RetrofitClient.getRetrofitInstance().create(LectureService.class);

        saveLectureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLecture();
            }
        });
    }

    private void saveLecture() {
        String lectureTitle = lectureTitleEditText.getText().toString().trim();

        if (lectureTitle.isEmpty()) {
            lectureTitleLayout.setError("Musisz podać nazwę wykładu!");
            return;
        } else {
            lectureTitleLayout.setError(null);
        }

        Call<Void> call = lectureService.createLecture(lectureTitle);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddLectureActivity.this, "Folder wykładu został utworzony!", Toast.LENGTH_SHORT).show();
                    finish(); // Zakończ aktywność po zapisaniu wykładu
                } else {
                    Toast.makeText(AddLectureActivity.this, "Błąd, folder wykładu nie został utworzony!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddLectureActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
