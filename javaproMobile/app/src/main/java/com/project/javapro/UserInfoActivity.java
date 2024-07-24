package com.project.javapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText albumNumberEditText;
    private EditText passwordEditText;
    private Button startQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        albumNumberEditText = findViewById(R.id.albumNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        startQuizButton = findViewById(R.id.startQuizButton);

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();
                String albumNumber = albumNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (password.equals("java")) {
                    Intent intent = new Intent(UserInfoActivity.this, QuizLobbyActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("surname", surname);
                    intent.putExtra("albumNumber", albumNumber);
                    intent.putExtra("quizDto", getIntent().getSerializableExtra("quizDto"));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UserInfoActivity.this, "Błędne hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
