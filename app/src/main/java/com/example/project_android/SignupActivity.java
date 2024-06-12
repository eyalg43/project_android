package com.example.project_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_PERMISSION_READ_MEDIA_IMAGES = 3;
    private static final int REQUEST_PERMISSION_CAMERA = 4;

    private ImageView imageViewProfile;
    private Uri selectedImageUri;
    private TextView textViewPasswordRequirements;
    private TextView textViewPasswordMatch;
    private TextView textViewImageError;
    private TextView textViewDisplayNameError;
    private Uri cameraImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText editTextDisplayName = findViewById(R.id.editTextDisplayName);
        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        Button buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        Button buttonSignup = findViewById(R.id.buttonSignup);
        textViewPasswordRequirements = findViewById(R.id.textViewPasswordRequirements);
        textViewPasswordMatch = findViewById(R.id.textViewPasswordMatch);
        textViewImageError = findViewById(R.id.textViewImageError);
        textViewDisplayNameError = findViewById(R.id.textViewDisplayNameError);

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignupActivity", "Upload image button clicked");
                checkPermissionAndOpenGallery();
            }
        });

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignupActivity", "Take photo button clicked");
                checkPermissionAndOpenCamera();
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
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();

                    return;
                }

                // Check if display name is filled
                if (displayName.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Display name is required.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    textViewDisplayNameError.setVisibility(View.GONE);
                }

                // Check if image is selected
                if (selectedImageUri == null) {
                    Toast.makeText(SignupActivity.this, "Please choose a profile picture.", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GET);
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CAMERA);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        try {
            Intent takePictureIntent = new Intent();
            takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(takePictureIntent);
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Log.e("SignupActivity", "Error opening camera: " + e.getMessage());
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
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
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
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
                    imageViewProfile.setImageBitmap(bitmap);
                    textViewImageError.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("SignupActivity", "Error loading image: " + e.getMessage());
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageViewProfile.setImageBitmap(bitmap);
                // Save the captured image URI or path
                selectedImageUri = getImageUriFromBitmap(bitmap);
                textViewImageError.setVisibility(View.GONE);
            }
        }
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        // Save the bitmap to a temporary file and return its URI
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "ProfilePicture", null);
        return Uri.parse(path);
    }
}
