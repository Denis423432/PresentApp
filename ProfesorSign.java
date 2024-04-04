package com.example.presentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProfesorSign extends AppCompatActivity {

    TextView userNameError;
    TextView passwordError;
    TextView userName;
    TextView password;
    Button sign;
    private final String FILENAME = "ProfesorData.txt";
    private final String FILENAMESIGN = "ProfesorSign.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor_sign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameError= findViewById(R.id.userNameError);
        passwordError=findViewById(R.id.passwordError);

        userName= findViewById(R.id.userName);
        password= findViewById(R.id.password);

        sign= findViewById(R.id.sign);


        sign.setOnClickListener(v -> {
            String userId = userName.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userPassword.isEmpty() && userId.isEmpty()) {
                passwordError.setText("Nu ai introdus Username-ul si Parola !");
                userNameError.setText("");
            }
            else if (userPassword.isEmpty()) {
                passwordError.setText("Nu ai introdus Parola !");
                userNameError.setText("");
            }
            else if (userId.isEmpty()) {
                userNameError.setText("Nu ai introdus Username-ul !");
                passwordError.setText("");
            }
            else {
                userNameError.setText("");
                passwordError.setText("");
                File file = new File(getFilesDir(),FILENAME);
                if(file.exists()){
                    boolean textExists = false;
                    String searchText = userId + " " + userPassword;

                    try (FileInputStream fileInputStream = openFileInput(FILENAME);
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            if (line.contains(searchText)) {
                                textExists = true;
                                break;
                            }
                        }
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    boolean UserIdExists = false;

                    try (FileInputStream fileInputStream = openFileInput(FILENAME);
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                        String line2;
                        while ((line2 = bufferedReader.readLine()) != null) {
                            String cleanLine = line2.replaceAll("[^a-zA-Z]", "");
                            if (cleanLine.contains(userId)) {
                                UserIdExists = true;
                                break;
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (textExists) {
                        //=======Intra in cont=========
                        File fileSign = new File(getFilesDir(), FILENAMESIGN);
                        if (fileSign.exists()) {
                            try (FileOutputStream fileOutputStream = openFileOutput(FILENAMESIGN, MODE_APPEND);
                                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                                 BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                                bufferedWriter.write(userId);
                                bufferedWriter.newLine();

                                Intent intent = new Intent(ProfesorSign.this, ProfesorAccount.class);
                                startActivity(intent);
                                Toast.makeText(ProfesorSign.this, "V-ati conectat la " + userId+ "!", Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            try {
                                if (fileSign.createNewFile()) {
                                    try (FileOutputStream fileOutputStream = openFileOutput(FILENAMESIGN, MODE_APPEND);
                                         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                                         BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                                        bufferedWriter.write(userId);
                                        bufferedWriter.newLine();

                                        Intent intent = new Intent(ProfesorSign.this, ProfesorAccount.class);
                                        startActivity(intent);
                                        Toast.makeText(ProfesorSign.this, "V-ati conectat la " + userId+ "!", Toast.LENGTH_SHORT).show();

                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    else if(!UserIdExists){
                        passwordError.setText("Scuze dar acest cont " +userId+" cu aceasta parola nu exista ! \n Va rog sa va creati un cont nou pe butonul de Login !");
                    }
                    if (!textExists){
                        passwordError.setText("Ati gresit datele contului");
                    }

                }
                else {
                    passwordError.setText("Scuze dar acest cont " + userId +" cu aceasta parola nu exista ! \n Va rog sa va creati un cont nou pe butonul de Login !");
                }

            }
            // "/data/user/0/.com.example.presapp./files/date.txt"
        });
        Button ElevLogin=findViewById(R.id.login);
        ElevLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfesorSign.this, ProfesorLogin.class);
                startActivity(intent);
            }
        });
    }
}