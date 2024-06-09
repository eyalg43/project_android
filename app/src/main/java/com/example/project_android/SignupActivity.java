package com.example.project_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;
    private static final int REQUEST_PERMISSION_READ_MEDIA_IMAGES = 2;

    private ImageView imageViewProfile;
    private Uri selectedImageUri;
    private TextView textViewPasswordRequirements;
    private TextView textViewPasswordMatch;
    private TextView textViewImageError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText editTextDisplayName = findViewById(R.id.editTextDisplayName);
        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        Button buttonSignup = findViewById(R.id.buttonSignup);
        textViewPasswordRequirements = findViewById(R.id.textViewPasswordRequirements);
        textViewPasswordMatch = findViewById(R.id.textViewPasswordMatch);
        textViewImageError = findViewById(R.id.textViewImageError);

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignupActivity", "Upload image button clicked");
                checkPermissionAndOpenGallery();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String displayName = editTextDisplayName.getText().toString().trim();

                // Check if password meets requirements
                if (!isPasswordValid(password)) {
                    textViewPasswordRequirements.setVisibility(View.VISIBLE);
                    textViewPasswordRequirements.setText("Password must have at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character.");
                    return;
                }

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    textViewPasswordMatch.setVisibility(View.VISIBLE);
                    textViewPasswordMatch.setText("Passwords do not match.");
                    return;
                }

                // Check if image is selected
                if (selectedImageUri == null) {
                    textViewImageError.setVisibility(View.VISIBLE);
                    textViewImageError.setText("Please choose a profile picture.");
                    return;
                }

                // Add user to in-memory state
                String imageUri = selectedImageUri != null ? selectedImageUri.toString() : "";  // Save image URI or path
                UserState.addUser(username, password, displayName, imageUri);

                // Proceed with signup logic
                Toast.makeText(SignupActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                finish();  // Go back to the previous activity (login screen)
            }
        });
    }

    private boolean isPasswordValid(String password) {
        // Password validation logic
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    private void checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_GET);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_MEDIA_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    imageViewProfile.setVisibility(View.GONE);  // Hide the ImageView

                    TextView textViewImageSuccess = findViewById(R.id.textViewImageSuccess);
                    textViewImageSuccess.setVisibility(View.VISIBLE);  // Show success message

                } catch (IOException e) {
                    Log.e("SignupActivity", "Error loading image: " + e.getMessage());
                }
            }
        }
    }
}
