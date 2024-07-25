package com.project.javapro;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
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
import java.net.URLConnection;
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

public class AddQuestionActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;
    public static Uri currentFilePath = null;
    private QuizDto quizDto;
    private TextInputEditText questionTitleEditText;
    private TextInputLayout questionTitleLayout;
    private Button saveQuestionButton;
    private LinearLayout answersContainer;
    private Button addAnswerButton;
    private ImageView imageView;
    private boolean isImageSelected = false;

    private QuestionService questionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionTitleLayout = findViewById(R.id.questionTitleLayout);
        questionTitleEditText = findViewById(R.id.questionTitle);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
        answersContainer = findViewById(R.id.answersContainer);
        addAnswerButton = findViewById(R.id.addAnswerButton);

        imageView = findViewById(R.id.imageView);
        questionService = RetrofitClient.getRetrofitInstance().create(QuestionService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizDto")) {
            quizDto = (QuizDto) intent.getSerializableExtra("quizDto");
            TextView quizTitle = findViewById(R.id.textViewQuizTitle);
            quizTitle.setText("Nazwa quizu: " + quizDto.getName());
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
                addAnswerField();
            }
        });
    }
    private void addAnswerField() {
        View answerView = getLayoutInflater().inflate(R.layout.item_answer, null);

        ImageButton deleteAnswerButton = answerView.findViewById(R.id.deleteAnswerButton);

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
        QuestionDto questionDto = new QuestionDto();
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
            showSuccessDialog("Musisz dodać przynajmniej jedną odpowiedź!");
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
            uploadFile(currentFilePath, randomFileName);
        }
        Call<Void> call = questionService.createQuestion(questionDto);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestionActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestionActivity.this);
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
        isImageSelected = false;
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

    public void selectFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            currentFilePath = filePath;
            if (requestCode == PICK_IMAGE_REQUEST) {
                isImageSelected = true;
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
            } else if (requestCode == PICK_FILE_REQUEST) {
                isImageSelected = false;

                imageView.setImageResource(R.drawable.ic_file);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = 500;
                imageView.setLayoutParams(layoutParams);
                imageView.requestLayout();
            }
        }
    }

    private void uploadFile(Uri filePath, String fileName) {
        String realPath = getRealPathFromURI(filePath);

        if (realPath == null) {
            Log.e("UploadFile", "Unable to get real path for URI: " + filePath);
            return;
        }

        File file = new File(realPath);
        if (!file.exists()) {
            Log.e("UploadFile", "File does not exist at path: " + realPath);
            return;
        }

        // Get MIME type of the file
        String mimeType = getContentResolver().getType(filePath);
        if (mimeType == null) {
            mimeType = URLConnection.guessContentTypeFromName(file.getName());
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.137.119:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        Call<ResponseBody> call;

        if (isImageSelected) {  // Assuming isImageSelected is a boolean determining if the file is an image
            call = service.uploadImage(body);
        } else {
            call = service.uploadCode(body);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("UploadFile", "Upload successful");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UploadFile", "Upload failed", t);
            }
        });
    }



    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                Log.e("GetRealPath", "Error getting path from URI", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (scheme != null && scheme.equals("file")) {
            filePath = uri.getPath();
        }

        return filePath;
    }


}
