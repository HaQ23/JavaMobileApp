package com.project.javapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void onQuizButtonClick(View view) {
        Intent intent = new Intent(this, QuizListActivity.class);
        startActivity(intent);
    }

    public void onSliderButtonClick(View view) {
        Intent intent = new Intent(this, LectureListActivity.class);
        startActivity(intent);
    }
    public void openChatActivity(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}
