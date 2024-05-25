package com.example.firsttry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class UploadPhoto extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;
    private Interpreter tfliteInterpreter;
    private TextView predictionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        Button uploadButton = findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadPhoto.this, MainActivity.class));
                finish();
            }
        });


        try {
            tfliteInterpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        predictionTextView = findViewById(R.id.prediction_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {

                Bitmap imageBitmap = preprocessImage(imageUri);

                ImageView imageView = findViewById(R.id.image_view);
                imageView.setImageBitmap(imageBitmap);

                runInference(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Imaginea nu a fost putut fii incarcata", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ByteBuffer loadModelFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(getAssets().openFd("fish_model.tflite").getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = getAssets().openFd("fish_model.tflite").getStartOffset();
        long declaredLength = getAssets().openFd("fish_model.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private Bitmap preprocessImage(Uri imageUri) throws IOException {

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));


        int inputWidth = 224;
        int inputHeight = 224;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true);

        return resizedBitmap;
    }

    private void runInference(Bitmap imageBitmap) {
        if (tfliteInterpreter != null) {

            int inputChannels = 3;
            int inputWidth = 224;
            int inputHeight = 224;
            int inputDataTypeSizeBytes = 4;

            ByteBuffer inputBuffer = ByteBuffer.allocateDirect(inputChannels * inputWidth * inputHeight * inputDataTypeSizeBytes);
            inputBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[inputWidth * inputHeight];
            imageBitmap.getPixels(intValues, 0, imageBitmap.getWidth(), 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < inputWidth; ++i) {
                for (int j = 0; j < inputHeight; ++j) {
                    final int val = intValues[pixel++];
                    inputBuffer.putFloat((val >> 16) & 0xFF);
                    inputBuffer.putFloat((val >> 8) & 0xFF);
                    inputBuffer.putFloat(val & 0xFF);
                }
            }

            float[][] outputArray = new float[1][5];
            tfliteInterpreter.run(inputBuffer, outputArray);

            float[] outputScores = outputArray[0];
            int predictedClassIndex = argmax(outputScores);


            String[] classNames = {"amur", "biban", "caras", "crap", "somn"};
            String predictedClassName = classNames[predictedClassIndex];


            displayResult(predictedClassName);
        }
    }

    private int argmax(float[] array) {
        int maxIndex = 0;
        float maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    private void displayResult(String predictedClass) {
        predictionTextView.setText("Specia recunoscuta: " + predictedClass);
    }
}
