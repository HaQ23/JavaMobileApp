package com.project.javapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.service.SlideService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideViewerActivity extends AppCompatActivity {

    private ImageView slideImageView;
    private TextView slideNumberTextView;
    private Spinner slideSpinner;
    private Button nextButton;
    private Button prevButton;
    private FloatingActionButton addSlideFab;

    private List<String> slideNames;
    private int currentSlideIndex = 0;
    private String lectureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_viewer);

        slideImageView = findViewById(R.id.slideImageView);
        slideNumberTextView = findViewById(R.id.slideNumberTextView);
        slideSpinner = findViewById(R.id.slideSpinner);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        addSlideFab = findViewById(R.id.addSlideFab);

        lectureName = getIntent().getStringExtra("LECTURE_NAME");

        loadSlides();

        nextButton.setOnClickListener(v -> showNextSlide());
        prevButton.setOnClickListener(v -> showPreviousSlide());
        addSlideFab.setOnClickListener(v -> {
            Intent intent = new Intent(SlideViewerActivity.this, AddSlideActivity.class);
            intent.putExtra("LECTURE_NAME", lectureName);
            startActivity(intent);
        });
    }

    private void loadSlides() {
        SlideService slideService = RetrofitClient.getRetrofitInstance().create(SlideService.class);
        slideService.getSlides(lectureName).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                slideNames = response.body();
                if (slideNames != null && !slideNames.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SlideViewerActivity.this, android.R.layout.simple_spinner_item, slideNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    slideSpinner.setAdapter(adapter);

                    slideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currentSlideIndex = position;
                            updateSlide();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    updateSlide();
                } else {
                    Toast.makeText(SlideViewerActivity.this, "Nie znaleziono slajdów.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(SlideViewerActivity.this, "Nie udało się załadować slajdów.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSlide() {
        if (slideNames != null && !slideNames.isEmpty() && currentSlideIndex >= 0 && currentSlideIndex < slideNames.size()) {
            String slideUrl = "http://192.168.137.119/:8080/uploads/" + lectureName + "/" + slideNames.get(currentSlideIndex);
            Glide.with(this).load(slideUrl).into(slideImageView);
            slideNumberTextView.setText((currentSlideIndex + 1) + "/" + slideNames.size());
        }
    }

    private void showNextSlide() {
        if (slideNames != null && currentSlideIndex < slideNames.size() - 1) {
            currentSlideIndex++;
            slideSpinner.setSelection(currentSlideIndex);
            updateSlide();
        }
    }

    private void showPreviousSlide() {
        if (slideNames != null && currentSlideIndex > 0) {
            currentSlideIndex--;
            slideSpinner.setSelection(currentSlideIndex);
            updateSlide();
        }
    }
}
