package com.project.javapro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.dto.QuizDto;
import com.project.javapro.service.QuizService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddQuizActivity extends AppCompatActivity {
    private TextInputLayout quizNameLayout;
    private TextInputLayout quizDurationLayout;
    private TextInputEditText quizNameEditText;
    private TextInputEditText quizDurationEditText;
    private Button saveQuizButton;
    private QuizService quizService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        quizService = RetrofitClient.getRetrofitInstance().create(QuizService.class);
        quizNameLayout = findViewById(R.id.quizNameLayout);
        quizDurationLayout = findViewById(R.id.quizDurationLayout);
        quizNameEditText = findViewById(R.id.quizNameEditText);
        quizDurationEditText = findViewById(R.id.quizDurationEditText);
        saveQuizButton = findViewById(R.id.saveQuizButton);

        // Set click listener for save quiz button
        saveQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuiz();
            }
        });
    }


    private void saveQuiz() {
        String quizName = quizNameEditText.getText().toString().trim();
        String quizDurationStr = quizDurationEditText.getText().toString().trim();

        if (TextUtils.isEmpty(quizName)) {
            quizNameLayout.setError("Nazwa quizu jest wymagana!");
            return;
        } else {
            quizNameLayout.setError(null);
        }

        if (TextUtils.isEmpty(quizDurationStr)) {
            quizDurationLayout.setError("Czas trwania quizu jest wymagany!");
            return;
        } else {
            quizDurationLayout.setError(null);
        }

        int quizDuration = Integer.parseInt(quizDurationStr);

        QuizDto quizDto = new QuizDto(quizName, quizDuration);

        Call<Void> call = quizService.createQuiz(quizDto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showSuccessDialog("Quiz został dodany!");
                    clearForm();
                } else {
                    showErrorDialog("Błąd, quiz nie został dodany!");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                showErrorDialog(t.toString());
            }
        });
    }
    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuizActivity.this);
        builder.setTitle("Sukces")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuizActivity.this);
        builder.setTitle("Błąd")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void clearForm() {
        quizNameEditText.setText("");
        quizDurationEditText.setText("");
    }
}