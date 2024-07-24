package com.project.javapro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.dto.QuizResultDto;
import com.project.javapro.service.QuizService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizResultActivity extends AppCompatActivity {

    private static final String TAG = "QuizResultActivity";

    private TextView resultText;
    private TextView gradeText;
    private Button returnButton;
    private Button viewQuestionsButton;

    private QuizService quizService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        resultText = findViewById(R.id.resultTex);
        gradeText = findViewById(R.id.gradeText);
        returnButton = findViewById(R.id.returnButton);
        viewQuestionsButton = findViewById(R.id.viewQuestionsButton);

        quizService = RetrofitClient.getRetrofitInstance().create(QuizService.class);

        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String albumNumber = getIntent().getStringExtra("albumNumber");
        long quizId = getIntent().getLongExtra("quizId", 0);

        double scorePercentage = ((double) correctAnswers / totalQuestions) * 100;
        String grade = calculateGrade(scorePercentage);

        resultText.setText("Ilość punktów: " + correctAnswers + "/" + totalQuestions);
        gradeText.setText("Ocena: " + grade);

        QuizResultDto quizResultDto = new QuizResultDto();
        quizResultDto.setName(name);
        quizResultDto.setSurname(surname);
        quizResultDto.setAlbumNumber(albumNumber);
        quizResultDto.setQuizId(quizId);
        quizResultDto.setCorrectAnswers(correctAnswers);
        quizResultDto.setTotalQuestions(totalQuestions);

        sendQuizResult(quizResultDto);

        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(QuizResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        viewQuestionsButton.setOnClickListener(v -> {
            Intent intent = new Intent(QuizResultActivity.this, QuizActivity.class);
            intent.putExtra("quizDto", getIntent().getSerializableExtra("quizDto"));
            intent.putExtra("name", name);
            intent.putExtra("surname", surname);
            intent.putExtra("albumNumber", albumNumber);
            intent.putExtra("reviewMode", true); // Dodajemy nowy extra do intentu

            Gson gson = new Gson();
            String answersJson = getIntent().getStringExtra("answersState");
            intent.putExtra("answersState", answersJson);

            startActivity(intent);
            finish();
        });
    }

    private void sendQuizResult(QuizResultDto quizResultDto) {
        Call<Void> call = quizService.sendQuizResult(quizResultDto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuizResultActivity.this, "Wynik został wysłany pomyślnie", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Wynik został wysłany pomyślnie");
                } else {
                    Toast.makeText(QuizResultActivity.this, "Błąd podczas wysyłania wyniku", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Błąd podczas wysyłania wyniku: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(QuizResultActivity.this, "Błąd sieci: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Błąd sieci: ", t);
            }
        });
    }

    private String calculateGrade(double scorePercentage) {
        if (scorePercentage < 60) {
            return "2.0";
        } else if (scorePercentage < 74) {
            return "3.0";
        } else if (scorePercentage < 80) {
            return "3.5";
        } else if (scorePercentage < 86) {
            return "4.0";
        } else if (scorePercentage < 93) {
            return "4.5";
        } else {
            return "5.0";
        }
    }
}
