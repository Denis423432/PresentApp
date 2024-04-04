package com.example.presentapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClassView extends AppCompatActivity {
    TextView userName;
    Button eleviInscrisi;
    Button classReset;
    TextView StudentsCounter;

    TextView ClassName;
    LinearLayout StudentsPresent;
    LinearLayout StudentsAbsent;

    public void StudentsPresent(int numaruDeStudenti, String[] numeleStudentilor, LinearLayout StudentsPresent) {
        Context context = StudentsPresent.getContext();
        StudentsPresent.removeAllViews();

        for (int i = 0; i < numaruDeStudenti; i++) {
            TextView studentTextView = new TextView(context);
            studentTextView.setText(numeleStudentilor[i]);

            studentTextView.setGravity(Gravity.CENTER);
            float textSize = 24;
            studentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            StudentsPresent.addView(studentTextView);
        }
    }
    public void StudentsAbsenti(int numaruDeStudenti, String[] numeleStudentilor, LinearLayout StudentsPresent) {
        Context context = StudentsPresent.getContext();
        StudentsPresent.removeAllViews();

        for (int i = 0; i < numaruDeStudenti; i++) {
            TextView studentTextView = new TextView(context);
            studentTextView.setText(numeleStudentilor[i]);

            studentTextView.setGravity(Gravity.CENTER);
            float textSize = 24;
            studentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            StudentsPresent.addView(studentTextView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName= findViewById(R.id.userName);

        String userID;

        String FILENAME = "ProfesorSign.txt";
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

        ClassName = findViewById(R.id.textView);

        StudentsCounter = findViewById(R.id.StudentsCounter);

        StudentsPresent = findViewById(R.id.StudentsPresent);

        StudentsAbsent = findViewById(R.id.StudentsAbsent);

        eleviInscrisi = findViewById(R.id.eleviInscrisi);

        classReset = findViewById(R.id.classReset);

        String FILENAMECLASS = "ClassSign.txt";

        String ClassData, className, studentsFullNumber;
        try (FileInputStream fileInputStream = openFileInput(FILENAMECLASS);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            ClassData = stringBuilder.toString();

            String[] parts = ClassData.split("\\s+");

            className = parts[0];
            studentsFullNumber = parts[1];

            fileInputStream.close();

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAMECLASS, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {

                outputStreamWriter.write("");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClassName.setText("Class name\n"+className);

        String fileNameClassStudents = className + ".txt";
        ArrayList<String> studentList0 = new ArrayList<>();
        ArrayList<String> studentList1 = new ArrayList<>();

        try (FileInputStream fileInputStream = openFileInput(fileNameClassStudents);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // Separăm linia în două părți la primul spațiu
                String[] parts = line.split(" ", 2);
                if (parts.length >= 2) {
                    if (parts[1].startsWith("0")) {
                        studentList0.add(parts[0]);
                    } else if (parts[1].startsWith("1")) {
                        studentList1.add(parts[0]);
                    }
                }
            }

            fileInputStream.close();

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAMECLASS, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {
                outputStreamWriter.write("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] students0 = studentList0.toArray(new String[0]);
        String[] students1 = studentList1.toArray(new String[0]);

        Log.w(TAG, "Students with '0':");
        for (String student : students0) {
            Log.w(TAG, student);
        }

        Log.w(TAG, "Students with '1':");
        for (String student : students1) {
            Log.w(TAG, student);
        }

        StudentsCounter.setText(students1.length+"/"+studentsFullNumber);

        StudentsPresent(students1.length, students1, StudentsPresent);

        StudentsAbsenti(students0.length,students0 ,StudentsAbsent);

        eleviInscrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_APPEND);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                     BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                    bufferedWriter.write(userID);
                    bufferedWriter.newLine();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try (FileOutputStream fileOutputStream = openFileOutput(FILENAMECLASS, MODE_APPEND);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                     BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                    bufferedWriter.write(className +" "+ studentsFullNumber);
                    bufferedWriter.newLine();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(ClassView.this, ClassFullStudentsSign.class);
                startActivity(intent);
                Log.w(TAG,"AI APASAT BUTONUL");
            }
        });
        classReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = className+".txt";

                try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {
                    outputStreamWriter.write("");
                    Intent intent = new Intent(ClassView.this, ProfesorClassesLook.class);
                    startActivity(intent);
                    Toast.makeText(ClassView.this, "Ati inceput o noua ora !", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}