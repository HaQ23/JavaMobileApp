package com.project.javapro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.javapro.dto.QuizDto;

public class QuizLobbyActivity extends AppCompatActivity {

    private QuizDto quizDto;
    public static Long quizId = null;
    private String name;
    private String surname;
    private String albumNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_lobby);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordDialog();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizDto")) {
            quizDto = (QuizDto) intent.getSerializableExtra("quizDto");
            name = intent.getStringExtra("name");
            surname = intent.getStringExtra("surname");
            albumNumber = intent.getStringExtra("albumNumber");

            if (quizDto != null) {
                TextView quizTitle = findViewById(R.id.quizTitle);
                TextView quizTime = findViewById(R.id.quizTime);
                quizTitle.setText("Nazwa: " + quizDto.getName());
                quizTime.setText("Czas trwania: " + quizDto.getTime() + " minut.");
            }
        }

        Button startQuizButton = findViewById(R.id.actionButton);
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartQuizClick(quizDto);
            }
        });
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizLobbyActivity.this);
        builder.setTitle("Podaj hasło");

        final EditText input = new EditText(QuizLobbyActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString().trim();
                if (isValidPassword(password)) {
                    openQuizLobbyPage(quizDto);
                } else {
                    Toast.makeText(QuizLobbyActivity.this, "Błędne hasło", Toast.LENGTH_SHORT).show();
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

    private void openQuizLobbyPage(QuizDto quizDto) {
        Intent intent = new Intent(this, AdminActivity.class);
        intent.putExtra("quizId", quizDto.getId());
        startActivity(intent);
    }

    public void onStartQuizClick(QuizDto quizDto) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("surname", surname);
        intent.putExtra("albumNumber", albumNumber);
        intent.putExtra("quizDto", quizDto);
        startActivity(intent);
    }

    private boolean isValidPassword(String password) {
        return password.equals("java");
    }
}
