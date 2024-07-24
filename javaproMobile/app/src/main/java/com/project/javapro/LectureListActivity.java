package com.project.javapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.javapro.adapters.LectureAdapter;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.service.LectureService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LectureListActivity extends AppCompatActivity {

    private RecyclerView lectureRecyclerView;
    private LectureAdapter lectureAdapter;
    private FloatingActionButton addLectureFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        lectureRecyclerView = findViewById(R.id.lectureRecyclerView);
        lectureRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addLectureFab = findViewById(R.id.addLectureFab);

        addLectureFab.setOnClickListener(v -> {
            // Przejście do widoku dodawania nowego wykładu
            Intent intent = new Intent(LectureListActivity.this, AddLectureActivity.class);
            startActivity(intent);
        });

        LectureService lectureService = RetrofitClient.getRetrofitInstance().create(LectureService.class);
        lectureService.getLectures().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lectureAdapter = new LectureAdapter(response.body(), lecture -> {
                        Intent intent = new Intent(LectureListActivity.this, SlideViewerActivity.class);
                        intent.putExtra("LECTURE_NAME", lecture);
                        startActivity(intent);
                    });
                    lectureRecyclerView.setAdapter(lectureAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(LectureListActivity.this, "Nie udało się załadować wykładów", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
