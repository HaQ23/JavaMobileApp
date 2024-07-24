package com.project.javapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.javapro.client.RetrofitClient;
import com.project.javapro.dto.QuestionDto;
import com.project.javapro.dto.QuizDto;
import com.project.javapro.service.QuestionService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizQuestionListActivity extends AppCompatActivity {

    private QuizDto quizDto;
    private QuestionService questionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question_list);

        questionService = RetrofitClient.getRetrofitInstance().create(QuestionService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizDto")) {
            quizDto = (QuizDto) intent.getSerializableExtra("quizDto");
            if (quizDto != null) {
                TextView quizTitle = findViewById(R.id.quizHeader);
                quizTitle.setText("Quiz: " + quizDto.getName());

                List<QuestionDto> questions = quizDto.getQuestions();
                if(questions != null && !questions.isEmpty()) {
                    renderQuestions(questions);
                }
            }
        }
    }

    private void renderQuestions(List<QuestionDto> questions) {
        LinearLayout questionListLayout = findViewById(R.id.questionListLayout);
        questionListLayout.removeAllViews(); // clear previous views

        for (QuestionDto question : questions) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16); // Margines między pytaniami

            LinearLayout questionLayout = new LinearLayout(this);
            questionLayout.setLayoutParams(params);
            questionLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView questionText = new TextView(this);
            questionText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            questionText.setText(question.getName());
            questionLayout.addView(questionText);

            ImageView editQuestionIcon = new ImageView(this);
            editQuestionIcon.setImageResource(R.drawable.ic_edit);
            editQuestionIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            editQuestionIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editIntent = new Intent(QuizQuestionListActivity.this, EditQuestionActivity.class);
                    editIntent.putExtra("quizDto", quizDto);
                    editIntent.putExtra("questionDto", question);
                    startActivity(editIntent);
                }
            });
            questionLayout.addView(editQuestionIcon);

            ImageView deleteQuestionIcon = new ImageView(this);
            deleteQuestionIcon.setImageResource(R.drawable.ic_delete);
            deleteQuestionIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            deleteQuestionIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteQuestion(question);
                }
            });
            questionLayout.addView(deleteQuestionIcon);

            questionListLayout.addView(questionLayout);
        }
    }

    private void deleteQuestion(QuestionDto question) {
        Call<Void> call = questionService.deleteQuestion(question.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    quizDto.getQuestions().remove(question);
                    renderQuestions(quizDto.getQuestions());
                    Toast.makeText(QuizQuestionListActivity.this, "Pytanie zostało usunięte.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuizQuestionListActivity.this, "Błąd podczas usuwania pytania.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(QuizQuestionListActivity.this, "Błąd podczas usuwania pytania.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
