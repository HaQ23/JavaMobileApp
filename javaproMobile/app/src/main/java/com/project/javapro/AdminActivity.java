package com.project.javapro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.javapro.client.RetrofitClient;
import com.project.javapro.dto.QuizDto;
import com.project.javapro.service.QuizService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private QuizDto quizDto;
    private Long quizIdMain;
    private QuizService quizService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        quizService = RetrofitClient.getRetrofitInstance().create(QuizService.class);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizId")) {
            quizIdMain = intent.getLongExtra("quizId", -1);
            if (quizIdMain != -1) {
                getQuizById(quizIdMain);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (quizIdMain != null && quizIdMain != -1) {
            getQuizById(quizIdMain);
        }
    }

    private void getQuizById(Long quizId) {
        Call<QuizDto> call = quizService.getQuizById(quizId);
        call.enqueue(new Callback<QuizDto>() {
            @Override
            public void onResponse(Call<QuizDto> call, Response<QuizDto> response) {
                if (response.isSuccessful()) {
                    quizDto = response.body();
                    if (quizDto != null) {
                        TextView quizId = findViewById(R.id.textViewQuizIdLabel);
                        TextView quizTitle = findViewById(R.id.textViewQuizNameLabel);
                        quizTitle.setText("Nazwa quizu: " + quizDto.getName());
                        quizId.setText("ID quizu: " + quizDto.getId());
                    }
                } else {
                    showErrorDialog("Błąd podczas pobierania danych quizu!");
                }
            }

            @Override
            public void onFailure(Call<QuizDto> call, Throwable t) {
                showErrorDialog("Błąd: " + t.toString());
            }
        });
    }

    public void onAddQuestionActivityClick(View view) {
        Intent intent = new Intent(AdminActivity.this, AddQuestionActivity.class);
        intent.putExtra("quizDto", quizDto);
        startActivity(intent);
    }

    public void onQuestionListActivityClick(View view) {
        Intent intent = new Intent(AdminActivity.this, QuizQuestionListActivity.class);
        intent.putExtra("quizDto", quizDto);
        startActivity(intent);
    }

    public void onEditQuizActivityClick(View view) {
        Intent intent = new Intent(AdminActivity.this, EditQuizActivity.class);
        intent.putExtra("quizDto", quizDto);
        startActivity(intent);
    }

    public void onDeleteQuizButtonClick(View view) {
        showConfirmDialog("Czy na pewno chcesz usunąć ten quiz?");
    }

    private void deleteQuiz(Long quizId) {
        Call<Void> call = quizService.deleteQuiz(quizId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showSuccessDialog("Quiz został usunięty!");
                } else {
                    showErrorDialog("Błąd, quiz nie został usunięty!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showErrorDialog("Błąd: " + t.toString());
            }
        });
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setTitle("Sukces")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(AdminActivity.this, QuizListActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void showConfirmDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setTitle("Potwierdzenie")
                .setMessage(message)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteQuiz(quizIdMain);
                    }
                })
                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dodatkowe działania po zamknięciu dialogu
                    }
                })
                .show();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setTitle("Błąd")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dodatkowe działania po zamknięciu dialogu
                    }
                })
                .show();
    }
}
