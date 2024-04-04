package com.example.presentapp;

import android.content.Context;
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

public class ProfesorCreateClass extends AppCompatActivity {

    TextView NameError;
    TextView NumberStudentError;
    TextView className;
    TextView StudentsNumber;
    Button ClassCreateNew;

    TextView userName;

    private final String FILENAME = "ProfesorSign.txt";

    private final String FILENAMECLASSDATA = "ClasesData.txt";

    public static boolean[] ClassNameVerify(String className1) {
        boolean[] rezultat = {false, false};

        for (int i = 0; i < className1.length(); i++) {

            char caracter = className1.charAt(i);

            if (Character.isLetter(caracter)) {
                rezultat[0] = true;
            }
            if (Character.isDigit(caracter)) {
                rezultat[1] = true;
            }
        }

        return rezultat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor_create_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NameError= findViewById(R.id.userNameError);
        NumberStudentError=findViewById(R.id.passwordError);

        className= findViewById(R.id.userName);
        StudentsNumber= findViewById(R.id.password);

        ClassCreateNew= findViewById(R.id.login);

        userName= findViewById(R.id.userNameId);

        String userID;

        try (FileInputStream fileInputStream = openFileInput(FILENAME);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            userID = stringBuilder.toString();

            fileInputStream.close();

            userName.setText(userID);

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {

                outputStreamWriter.write("");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ClassCreateNew.setOnClickListener(v -> {
            String className1 = className.getText().toString().trim();
            String StudentsNumber1 = StudentsNumber.getText().toString().trim();

            boolean[] rezultat = ClassNameVerify(className1);

            int NumberStudents = Integer.parseInt(StudentsNumber1);

            if (StudentsNumber1.isEmpty() && className1.isEmpty()) {
                NumberStudentError.setText("Nu ai introdus numele clasei si \n nici numaru de elevi !");
                NameError.setText("");
            }
            else if (StudentsNumber1.isEmpty()) {
                NumberStudentError.setText("Nu ai introdus numaru de elevi !");
                NameError.setText("");
            }
            else if (className1.isEmpty()) {
                NameError.setText("Nu ai introdus numele clasei !");
                NumberStudentError.setText("");
            }
            else if(NumberStudents < 8) {
                NumberStudentError.setText("Nu pot sa fie mai putini de 8 elevi in clasa !");
                NameError.setText("");
            }
            else if(NumberStudents > 35) {
                NumberStudentError.setText("Nu pot sa fie mai multi de 35 de elevi in clasa !");
                NameError.setText("");
            }
            else if(!rezultat[0]) {
                NameError.setText("Ai introdus clasa dar nu numele ei !");
                NumberStudentError.setText("");
            }

            else if(!rezultat[1]) {
                NameError.setText("Ai introdus numele dar nu clasa ei !");
                NumberStudentError.setText("");
            }
            else {
                NumberStudentError.setText("");
                NameError.setText("");

                boolean fileIsEmpty = false;

                try (FileInputStream fileInputStream = openFileInput(FILENAMECLASSDATA);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains(className1)) {
                            fileIsEmpty = true;
                            break;
                        }
                    }
                } catch (IOException ignored) {
                }

                if (fileIsEmpty) {
                    NameError.setText("Scuze dar această clasă " + className1 + " există deja!");
                }
                else {
                    try (FileOutputStream fileOutputStream1 = openFileOutput(FILENAMECLASSDATA, MODE_APPEND);
                         OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(fileOutputStream1);
                         BufferedWriter bufferedWriter1 = new BufferedWriter(outputStreamWriter1)) {

                        String classID = className1 + " " + NumberStudents;
                        bufferedWriter1.write(classID);
                        bufferedWriter1.newLine();

                        try(FileOutputStream fileOutputStream2 = openFileOutput(className1 + ".txt", MODE_APPEND);
                            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(fileOutputStream2);
                            BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter2)) {
                            bufferedWriter2.write("");
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try(FileOutputStream fileOutputStream2 = openFileOutput(className1 + "_EleviFull.txt", MODE_APPEND);
                            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(fileOutputStream2);
                            BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter2)) {
                            bufferedWriter2.write("");
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Toast.makeText(ProfesorCreateClass.this, "Clasa dumneavoastră " + className1 + " a fost creată!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ProfesorCreateClass.this, ProfesorClassesLook.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });
    }
}