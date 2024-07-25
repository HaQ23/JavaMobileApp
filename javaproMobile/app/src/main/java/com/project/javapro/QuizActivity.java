package com.project.javapro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.javapro.dto.AnswerDto;
import com.project.javapro.dto.QuestionDto;
import com.project.javapro.dto.QuizDto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String BASE_URL = "http:// 192.168.137.119:8080/uploads/";
    private static final String TAG = "QuizActivity";

    private QuizDto quizDto;
    private String name;
    private String surname;
    private String albumNumber;
    private int currentQuestionIndex = 0;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private int correctAnswers = 0;
    private List<List<Boolean>> answersState; // Struktura do przechowywania zaznaczonych odpowiedzi

    private TextView questionText;
    private TextView quizTimer;
    private TextView questionNumber;
    private ImageView questionImage;
    private LinearLayout answersLayout;
    private Button submitAnswerButton;
    private Button prevQuestionButton;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean reviewMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        quizTimer = findViewById(R.id.quizTimer);
        questionNumber = findViewById(R.id.questionNumber);
        questionImage = findViewById(R.id.questionImage);
        answersLayout = findViewById(R.id.answersLayout);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);
        prevQuestionButton = findViewById(R.id.prevQuestionButton);

        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            surname = intent.getStringExtra("surname");
            albumNumber = intent.getStringExtra("albumNumber");
            quizDto = (QuizDto) intent.getSerializableExtra("quizDto");
            reviewMode = intent.getBooleanExtra("reviewMode", false);
            timeLeftInMillis = quizDto.getTime() * 60000; // Czas w minutach

            if (intent.hasExtra("answersState")) {
                Gson gson = new Gson();
                String jsonAnswersState = intent.getStringExtra("answersState");
                Type type = new TypeToken<List<List<Boolean>>>() {}.getType();
                answersState = gson.fromJson(jsonAnswersState, type);
                Log.d("QuizActivity", "Restored answersState: " + answersState);
            } else {
                initializeAnswersState();
            }

            if (!reviewMode) {
                startTimer();
            }
            displayQuestion();
        }

        submitAnswerButton.setOnClickListener(v -> {
            if (reviewMode) {
                if (currentQuestionIndex < quizDto.getQuestions().size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                } else {
                    goToQuizResultActivity();
                }
            } else {
                checkAnswer(); // Sprawdzanie odpowiedzi przed zapisaniem stanu
                saveAnswersState();
                if (currentQuestionIndex < quizDto.getQuestions().size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                } else {
                    endQuiz();
                }
            }
        });

        prevQuestionButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                saveAnswersState();
                currentQuestionIndex--;
                displayQuestion();
            }
        });
    }

    private void goToQuizResultActivity() {
        Intent resultIntent = new Intent(QuizActivity.this, QuizResultActivity.class);
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("surname", surname);
        resultIntent.putExtra("albumNumber", albumNumber);
        resultIntent.putExtra("quizId", quizDto.getId());
        resultIntent.putExtra("correctAnswers", correctAnswers);
        resultIntent.putExtra("totalQuestions", quizDto.getQuestions().size());
        resultIntent.putExtra("quizDto", quizDto);

        Gson gson = new Gson();
        String answersJson = gson.toJson(answersState);
        resultIntent.putExtra("answersState", answersJson);

        startActivity(resultIntent);
        finish();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                endQuiz();
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        quizTimer.setText("Czas: " + timeLeftFormatted);
    }

    private void initializeAnswersState() {
        answersState = new ArrayList<>();
        for (int i = 0; i < quizDto.getQuestions().size(); i++) {
            List<Boolean> questionState = new ArrayList<>();
            for (int j = 0; j < quizDto.getQuestions().get(i).getAnswerList().size(); j++) {
                questionState.add(false);
            }
            answersState.add(questionState);
        }
        Log.d("QuizActivity", "Initialized answersState: " + answersState);
    }

    private void displayQuestion() {
        QuestionDto question = quizDto.getQuestions().get(currentQuestionIndex);
        questionText.setText(question.getName());
        questionNumber.setText("Pytanie " + (currentQuestionIndex + 1));

        if (currentQuestionIndex == 0) {
            prevQuestionButton.setVisibility(View.GONE);
        } else {
            prevQuestionButton.setVisibility(View.VISIBLE);
        }

        if (currentQuestionIndex == quizDto.getQuestions().size() - 1) {
            submitAnswerButton.setText(reviewMode ? "Zakończ przegląd" : "Zakończ test");
        } else {
            submitAnswerButton.setText("Następne");
        }

        handleQuestionDisplay(question);
    }

    private void handleQuestionDisplay(QuestionDto question) {
        if (question.getFileUrl() != null && !question.getFileUrl().isEmpty()) {
            Log.e("FILENAME", question.getFileUrl());
            handleCodeOrImageDisplay(question);
        } else {
            questionImage.setVisibility(View.GONE);
        }

        setupAnswers(question);
    }

    private void handleCodeOrImageDisplay(QuestionDto question) {
        TextView codeTextView = findViewById(R.id.codeTextView);
        switch (question.getFileUrl()) {
            case "code":
                questionImage.setVisibility(View.GONE);
                codeTextView.setVisibility(View.VISIBLE);
                codeTextView.setText("public static void main(String args[]) {}");
                break;
            case "code1":
                questionImage.setVisibility(View.GONE);
                codeTextView.setVisibility(View.VISIBLE);
                codeTextView.setText("public void setName(String name) { this.name = name; }");
                break;
            case "code2":
                questionImage.setVisibility(View.GONE);
                codeTextView.setVisibility(View.VISIBLE);
                codeTextView.setText("while(true) { System.out.println('loop');}");
                break;
            case "code3":
                questionImage.setVisibility(View.GONE);
                codeTextView.setVisibility(View.VISIBLE);
                codeTextView.setText("User user = new User();");
                break;
            default:
                codeTextView.setVisibility(View.GONE);
                questionImage.setVisibility(View.VISIBLE);
                String imageUrl = BASE_URL + question.getFileUrl() + ".png";
                Log.d(TAG, "Loading image from URL: " + imageUrl);
                new DownloadImageTask(questionImage).execute(imageUrl);
        }
    }

    private void setupAnswers(QuestionDto question) {
        answersLayout.removeAllViews();
        checkBoxes.clear();

        List<Boolean> questionState = answersState.get(currentQuestionIndex);
        for (int i = 0; i < question.getAnswerList().size(); i++) {
            AnswerDto answer = question.getAnswerList().get(i);
            CheckBox checkBox = new CheckBox(this);
            String answerText = (i + 1) + ". " + answer.getName();
            SpannableString spannableString = new SpannableString(answerText);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            checkBox.setText(spannableString);
            checkBox.setChecked(questionState.get(i));
            checkBox.setTextSize(18);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 16);
            checkBox.setLayoutParams(params);

            if (reviewMode) {
                checkBox.setEnabled(false);
                colorizeCheckBox(checkBox, questionState.get(i), answer.isCorrect());
            }

            answersLayout.addView(checkBox);
            checkBoxes.add(checkBox);
        }
    }

    private void colorizeCheckBox(CheckBox checkBox, boolean isChecked, boolean isCorrect) {
        Log.d("ColorizeCheck", "isChecked: " + isChecked + ", isCorrect: " + isCorrect);
        if (isChecked) {
            if (isCorrect) {
                checkBox.setTextColor(getResources().getColor(android.R.color.holo_green_dark)); // Poprawna i zaznaczona
            } else {
                checkBox.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // Niepoprawna, ale zaznaczona
            }
        } else {
            if (isCorrect) {
                checkBox.setTextColor(getResources().getColor(android.R.color.holo_green_light)); // Poprawna, ale niezaznaczona
            } else {
                checkBox.setTextColor(getResources().getColor(android.R.color.darker_gray)); // Niepoprawna i niezaznaczona
            }
        }
    }

    private void saveAnswersState() {
        List<Boolean> questionState = answersState.get(currentQuestionIndex);
        for (int i = 0; i < checkBoxes.size(); i++) {
            questionState.set(i, checkBoxes.get(i).isChecked());
        }
        Log.d("QuizActivity", "Saved answersState: " + answersState);
    }

    private void checkAnswer() {
        QuestionDto question = quizDto.getQuestions().get(currentQuestionIndex);
        boolean isCorrect = true;

        for (int i = 0; i < checkBoxes.size(); i++) {
            CheckBox checkBox = checkBoxes.get(i);
            boolean answerCorrect = question.getAnswerList().get(i).isCorrect();
            if (checkBox.isChecked() != answerCorrect) {
                isCorrect = false;
                break;
            }
        }

        if (isCorrect) {
            correctAnswers++;
        }
    }

    private void endQuiz() {
        saveAnswersState();  // Upewnij się, że zapisujesz stan przed zamknięciem.
        countDownTimer.cancel();
        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("surname", surname);
        intent.putExtra("albumNumber", albumNumber);
        intent.putExtra("quizId", quizDto.getId());
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("totalQuestions", quizDto.getQuestions().size());
        intent.putExtra("quizDto", quizDto);

        Gson gson = new Gson();
        String answersJson = gson.toJson(answersState);
        intent.putExtra("answersState", answersJson);

        startActivity(intent);
        finish();
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                Log.e(TAG, "Failed to load image");
            }
        }
    }

    private String downloadTextContent(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        inputStream.close();
        urlConnection.disconnect();
        return stringBuilder.toString();
    }
}
