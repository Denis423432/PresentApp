package com.example.presentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProfesorLogin extends AppCompatActivity {

    TextView userNameError;
    TextView passwordError;
    TextView userName;
    TextView password;
    Button login;
    private final String FILENAME = "ProfesorData.txt";
    private boolean containsOnlyLettersAndSpaces(String line) {
        for (char c : line.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        userNameError= findViewById(R.id.userNameError);
        passwordError=findViewById(R.id.passwordError);

        userName= findViewById(R.id.userName);
        password= findViewById(R.id.password);

        login= findViewById(R.id.login);


        login.setOnClickListener(v -> {
            String userId = userName.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userPassword.isEmpty() && userId.isEmpty()) {
                passwordError.setText("Nu ai introdus Username-ul si Parola!");
                userNameError.setText("");
            }
            else if (userPassword.isEmpty()) {
                passwordError.setText("Nu ai introdus Parola!");
                userNameError.setText("");
            }
            else if (userId.isEmpty()) {
                userNameError.setText("Nu ai introdus Username-ul!");
                passwordError.setText("");
            }
            else if (!containsOnlyLettersAndSpaces(userId)) {
                userNameError.setText("Username-ul "+userId+" nu trebuie sa contina numere !");
                passwordError.setText("");
            }
            else {
                userNameError.setText("");
                passwordError.setText("");
                boolean textExists = false;
                String searchText = userId;

                try (FileInputStream fileInputStream = openFileInput(FILENAME);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String cleanLine = line.replaceAll("[^a-zA-Z]", "");
                        if (cleanLine.contains(searchText)) {
                            textExists = true;
                            break;
                        }
                    }
                }
                catch (IOException e) {
                }

                if (textExists) {
                    userNameError.setText("Scuze dar acest cont " + userId+ " există!");
                }
                else {
                    try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_APPEND);
                         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                         BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                        String newData = userId + " " + userPassword;
                        bufferedWriter.write(newData);
                        bufferedWriter.newLine();

                        Toast.makeText(ProfesorLogin.this, "Contul dumneavoastră "+userId+" a fost înregistrat cu succes!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ProfesorLogin.this, ProfesorSign.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }



            }
            // "/data/user/0/.com.example.presapp./files/date.txt"
        });
    }
}