package com.example.presentapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Arrays;

public class ElevAccountConfig extends AppCompatActivity {

    public static boolean[] ClassNameVerify(String classInput) {
        boolean[] rezultat = {false, false};

        for (int i = 0; i < classInput.length(); i++) {

            char caracter = classInput.charAt(i);

            if (Character.isLetter(caracter)) {
                rezultat[0] = true;
            }
            if (Character.isDigit(caracter)) {
                rezultat[1] = true;
            }
        }

        return rezultat;
    }

    private final String FILENAME = "ElevSign.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elev_account_config);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView userName= findViewById(R.id.userName);

        String userId;

        try (FileInputStream fileInputStream = openFileInput(FILENAME);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null)  {
                stringBuilder.append(line).append("\n");
            }

            userId = stringBuilder.toString().trim();

            fileInputStream.close();

            userName.setText(userId);

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {

                outputStreamWriter.write("");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TextView classEnterError = findViewById(R.id.classEnterError);
        TextView classinput = findViewById(R.id.classInput);
        Button Join = findViewById(R.id.Join);

        Join.setOnClickListener(v -> {
            String classInput = classinput.getText().toString().trim();

            ArrayList<String> NrEleviInClasa = new ArrayList<>();

            int NrAdevaratDeEleviInClasa = 0;

            boolean[] rezultat = ClassNameVerify(classInput);

            if (classInput.isEmpty()) {
                classEnterError.setText("Nu ai introdus clasa !");
            }
            else if(!rezultat[0]) {
                classEnterError.setText("Ai introdus clasa dar nu numele ei !");
            }

            else if(!rezultat[1]) {
                classEnterError.setText("Ai introdus numele dar nu clasa ei !");
            }
            else {
                String FILECLASSDATA = "ClasesData.txt";

                File file = new File(getFilesDir(),FILECLASSDATA);
                if(file.exists()){
                    boolean textExists = false;
                    try (FileOutputStream fileOutputStream1 = openFileOutput(FILENAME, MODE_APPEND);
                         OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(fileOutputStream1);
                         BufferedWriter bufferedWriter1 = new BufferedWriter(outputStreamWriter1)) {

                        bufferedWriter1.write(userId);
                        bufferedWriter1.newLine();
                        try (FileInputStream fileInputStream = openFileInput(FILECLASSDATA);
                             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                if (line.contains(classInput)) {
                                    textExists = true;

                                    String[] parts = line.split("\\s+");
                                    if (parts.length > 1) {
                                        NrAdevaratDeEleviInClasa = Integer.parseInt(parts[1]);
                                    }

                                    break;
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        int NrCreatiDeEleviInClasa = 0;

                        try (FileInputStream fileInputStream = openFileInput(classInput + "_EleviFull.txt");
                             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                            String line;


                            while ((line = bufferedReader.readLine()) != null) {
                                if (!line.isEmpty()) {
                                    NrCreatiDeEleviInClasa++;
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Log.w(TAG, String.valueOf(NrCreatiDeEleviInClasa));

                        if (NrCreatiDeEleviInClasa  <  NrAdevaratDeEleviInClasa){
                            if (textExists) {
                                String FILEUSERID = userId + ".txt";
                                File fileuserid = new File(getFilesDir(), FILEUSERID);

                                if (!fileuserid.exists()) {
                                    try (FileOutputStream fileOutputStream2 = openFileOutput(FILEUSERID, MODE_APPEND);
                                         OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(fileOutputStream2);
                                         BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter2)) {

                                        bufferedWriter2.write(classInput);

                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    try (FileOutputStream fileOutputStream2 = openFileOutput(classInput + "_EleviFull.txt", MODE_APPEND);
                                         OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(fileOutputStream2);
                                         BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter2)) {

                                        bufferedWriter2.write(userId);
                                        bufferedWriter2.newLine();

                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Intent intent = new Intent(ElevAccountConfig.this, ElevAccount.class);
                                    startActivity(intent);
                                    Toast.makeText(ElevAccountConfig.this, "Ati reusit sa intrati in clasa " + classInput + " cu contul " + userId, Toast.LENGTH_SHORT).show();
                                } else if (fileuserid.exists()) {
                                    classEnterError.setText("Contul " + userId + " este deja configurat !");
                                }
                            } else {
                                classEnterError.setText("Clasa " + classInput + " nu exista !");
                            }
                        }

                        else {
                            classEnterError.setText("Nu mai sunt locuri pentru alti elevi in clasa "+classInput+" !");
                        }
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    classEnterError.setText("Inca nu au fost create clase de catre profesor !");
                }
            }
        });
    }
}