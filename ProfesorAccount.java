package com.example.presentapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class ProfesorAccount extends AppCompatActivity {
    TextView userName;

    private final String FILENAME = "ProfesorSign.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName= findViewById(R.id.userName);

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

        Button ClassLook=findViewById(R.id.Classes);
        ClassLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_APPEND);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                     BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                    bufferedWriter.write(userID);
                    bufferedWriter.newLine();

                    Intent intent = new Intent(ProfesorAccount.this, ProfesorClassesLook.class);
                    startActivity(intent);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Button ClassCreate=findViewById(R.id.ClassCreate);
        ClassCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_APPEND);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                     BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                    bufferedWriter.write(userID);
                    bufferedWriter.newLine();

                    Intent intent = new Intent(ProfesorAccount.this, ProfesorCreateClass.class);
                    startActivity(intent);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}