package com.project.javapro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.dto.QuizDto;
import com.project.javapro.service.QuizService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizListActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private QuizService quizApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        linearLayout = findViewById(R.id.linearLayout);
        quizApiService = RetrofitClient.getRetrofitInstance().create(QuizService.class);

        fetchQuizzes();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchQuizzes();
    }

    private void fetchQuizzes() {
        Call<List<QuizDto>> call = quizApiService.getQuizzes();
        call.enqueue(new Callback<List<QuizDto>>() {
            @Override
            public void onResponse(Call<List<QuizDto>> call, Response<List<QuizDto>> response) {
                if (response.isSuccessful()) {
                    List<QuizDto> quizzes = response.body();
                    if (quizzes != null) {
                        linearLayout.removeAllViews(); // Clear the layout before adding new buttons
                        for (QuizDto quizDto : quizzes) {
                            addButton(quizDto);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<QuizDto>> call, Throwable t) {
                Toast.makeText(QuizListActivity.this, "Błąd podczas pobierania quizów", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addButton(QuizDto quizDto) {
        Button button = new Button(this);
        button.setText(quizDto.getName());
        button.setOnClickListener(v -> onQuizButtonClick(quizDto));
        linearLayout.addView(button);
    }

    private void onQuizButtonClick(QuizDto quizDto) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("quizDto", quizDto);
        startActivity(intent);
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizListActivity.this);
        builder.setTitle("Podaj hasło");

        final EditText input = new EditText(QuizListActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString().trim();
                if (isValidPassword(password)) {
                    Intent intent = new Intent(QuizListActivity.this, AddQuizActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(QuizListActivity.this, "Błędne hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();
    }

    private boolean isValidPassword(String password) {
        return password.equals("java");
    }
}
