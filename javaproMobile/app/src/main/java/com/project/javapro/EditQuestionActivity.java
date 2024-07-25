package com.project.javapro;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.javapro.client.RetrofitClient;
import com.project.javapro.dto.AnswerDto;
import com.project.javapro.dto.QuestionDto;
import com.project.javapro.dto.QuizDto;
import com.project.javapro.service.FileService;
import com.project.javapro.service.QuestionService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditQuestionActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static Uri currentFilePath = null;
    private QuizDto quizDto;
    private QuestionDto questionDto;
    private TextInputEditText questionTitleEditText;
    private TextInputLayout questionTitleLayout;
    private Button saveQuestionButton;
    private LinearLayout answersContainer;
    private Button addAnswerButton;
    private ImageView imageView;

    private QuestionService questionService;
    private Long questionId;
    private Long quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        questionTitleLayout = findViewById(R.id.questionTitleLayout);
        questionTitleEditText = findViewById(R.id.questionTitle);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
        answersContainer = findViewById(R.id.answersContainer);
        addAnswerButton = findViewById(R.id.addAnswerButton);

        imageView = findViewById(R.id.imageView);
        questionService = RetrofitClient.getRetrofitInstance().create(QuestionService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizDto") && intent.hasExtra("questionDto")) {
            quizDto = (QuizDto) intent.getSerializableExtra("quizDto");
            questionDto = (QuestionDto) intent.getSerializableExtra("questionDto");
            questionId = questionDto.getId();
            quizId = quizDto.getId();
            TextView quizTitle = findViewById(R.id.textViewQuizTitle);
            quizTitle.setText("Nazwa quizu: " + quizDto.getName());

            loadQuestionData();
        }

        saveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuestion();
            }
        });
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnswerField(null, false);
            }
        });
    }

    private void loadQuestionData() {
        questionTitleEditText.setText(questionDto.getName());
        if (questionDto.getFileUrl() != null && !questionDto.getFileUrl().isEmpty()) {
            Uri imageUri = Uri.parse(questionDto.getFileUrl());
            imageView.setImageURI(imageUri);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = 500;
            imageView.setLayoutParams(layoutParams);
            imageView.requestLayout();
        }

        for (AnswerDto answer : questionDto.getAnswerList()) {
            addAnswerField(answer.getName(), answer.isCorrect());
        }
    }

    private void addAnswerField(String answerText, boolean isCorrect) {
        View answerView = getLayoutInflater().inflate(R.layout.item_answer, null);

        TextInputLayout answerLayout = answerView.findViewById(R.id.answerLayout);
        CheckBox checkBox = answerView.findViewById(R.id.checkboxIsCorrect);
        TextInputEditText answerEditText = (TextInputEditText) answerLayout.getEditText();
        ImageButton deleteAnswerButton = answerView.findViewById(R.id.deleteAnswerButton);

        if (answerText != null) {
            answerEditText.setText(answerText);
            checkBox.setChecked(isCorrect);
        }

        deleteAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answersContainer.removeView(answerView);
            }
        });

        answersContainer.addView(answerView);
    }

    private void saveQuestion() {
        String questionTitle = questionTitleEditText.getText().toString().trim();
        String randomFileName = UUID.randomUUID().toString();
        questionDto.setName(questionTitle);
        if(currentFilePath != null) {
            questionDto.setFileUrl(randomFileName);
        }
        questionDto.setQuizId(quizDto.getId());

        if (questionTitle.isEmpty()) {
            questionTitleLayout.setError("Musisz podać tytuł pytania!");
            return;
        } else {
            questionTitleLayout.setError(null);
        }

        if (answersContainer.getChildCount() == 0) {
            showSuccessDialog("Musisz dodać przynajmiej jedną odpowiedź!");
            return;
        }

        boolean atLeastOneCorrectAnswer = false;
        List<AnswerDto> answerList = new ArrayList<>();

        for (int i = 0; i < answersContainer.getChildCount(); i++) {
            View answerView = answersContainer.getChildAt(i);
            if (answerView instanceof LinearLayout) {
                TextInputLayout answerLayout = answerView.findViewById(R.id.answerLayout);
                CheckBox checkBox = answerView.findViewById(R.id.checkboxIsCorrect);
                TextInputEditText answerEditText = (TextInputEditText) answerLayout.getEditText();

                if (answerEditText != null) {
                    String answerText = answerEditText.getText().toString().trim();
                    boolean isCorrect = checkBox.isChecked();

                    if (answerText.isEmpty()) {
                        answerLayout.setError("Odpowiedź nie może być pusta!");
                        return;
                    } else {
                        answerLayout.setError(null);
                    }

                    answerList.add(new AnswerDto(null, answerText, isCorrect));
                    if (isCorrect) {
                        atLeastOneCorrectAnswer = true;
                    }
                }
            }
        }

        if (!atLeastOneCorrectAnswer) {
            showSuccessDialog("Żadna odpowiedź nie jest poprawna!");
            return;
        }

        questionDto.setAnswerList(answerList);

        if(currentFilePath != null) {
            uploadImage(currentFilePath, randomFileName);
        }

        Call<Void> call = questionService.updateQuestion(questionId, questionDto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showSuccessDialog("Pytanie zostało zapisane!");
                    clearForm();
                } else {
                    showErrorDialog("Błąd, pytanie nie zostało zapisane!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showErrorDialog(t.toString());
            }
        });
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionActivity.this);
        builder.setTitle("Sukces")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EditQuestionActivity.this, AdminActivity.class);
                        intent.putExtra("quizId", quizDto.getId());
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionActivity.this);
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
        questionTitleEditText.setText("");
        currentFilePath = null;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = 0;
        imageView.setLayoutParams(layoutParams);
        imageView.requestLayout();
        imageView.setImageURI(null);
        answersContainer.removeAllViews();
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            currentFilePath = filePath;
            try {
                imageView.setImageURI(filePath);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = 500;
                imageView.setLayoutParams(layoutParams);
                imageView.requestLayout();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Błąd wczytywania obrazu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri filePath, String fileName) {
        File file = new File(getRealPathFromURI(filePath));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(filePath)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.137.119:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        Call<ResponseBody> call = service.uploadImage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}