package com.example.project_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project_android.api.ApiService;
import com.example.project_android.api.RetrofitClient;
import com.example.project_android.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserDetails extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private EditText editTextUsername;
    private EditText editTextDisplayName;
    private EditText editTextPassword;
    private Button buttonUploadImage;
    private Button buttonTakePhoto;
    private Button buttonSaveChanges;
    private Button buttonDeleteUser;

    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;
    private String base64ProfilePicture;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto2);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonDeleteUser = findViewById(R.id.button2);

        apiService = RetrofitClient.getApiService();

        // Load current user details
        loadUserDetails();

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDetails();
            }
        });

        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    private void loadUserDetails() {
        User currentUser = UserState.getLoggedInUser();
        if (currentUser != null) {
            editTextUsername.setText(currentUser.getUsername());
            editTextDisplayName.setText(currentUser.getDisplayName());
            // Don't load password for security reasons
        }
    }

    private void saveUserDetails() {
        String username = editTextUsername.getText().toString().trim();
        String displayName = editTextDisplayName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate inputs
        if (displayName.isEmpty()) {
            editTextDisplayName.setError("Display Name is required.");
            return;
        }

        // If username or password is empty, retain existing values from UserState
        User loggedInUser = UserState.getLoggedInUser();
        if (username.isEmpty()) {
            username = loggedInUser.getUsername();
        }
        if (password.isEmpty()) {
            password = loggedInUser.getPassword(); // If not editable, consider security implications
        }

        // Convert image to base64 if a new image is selected
        if (selectedImageUri != null) {
            base64ProfilePicture = convertImageToBase64(selectedImageUri);
            if (base64ProfilePicture == null) {
                Toast.makeText(this, "Failed to upload profile picture.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create updated user object
        User updatedUser = new User(username, password, displayName, base64ProfilePicture);

        TokenManager tokenManager = TokenManager.getInstance();

        // Update user details via API
        apiService.updateUser("Bearer " + tokenManager.getToken() , loggedInUser.getUsername(), updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User updatedUser = response.body();
                    UserState.setLoggedInUser(updatedUser); // Update UserState with updated user
                    Toast.makeText(EditUserDetails.this, "User details updated successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditUserDetails.this, "Failed to update user details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("EditUserDetails", "Error updating user details: " + t.getMessage());
                Toast.makeText(EditUserDetails.this, "Error updating user details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GET);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GET && data != null) {
                selectedImageUri = data.getData();
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                selectedImageBitmap = (Bitmap) extras.get("data");
                selectedImageUri = getImageUriFromBitmap(selectedImageBitmap);
            }
        }
    }

    private void deleteUser() {
        User loggedInUser = UserState.getLoggedInUser();
        if (loggedInUser != null) {
            String userId = loggedInUser.getUsername();

            // Call the API to delete the user
            TokenManager tokenManager = TokenManager.getInstance();
            apiService.deleteUser("Bearer " + tokenManager.getToken(), userId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Handle successful deletion
                        Toast.makeText(EditUserDetails.this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                        // Optionally navigate to a different activity upon deletion
                        Intent intent = new Intent(EditUserDetails.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close current activity
                    } else {
                        // Handle deletion failure
                        Toast.makeText(EditUserDetails.this, "Failed to delete user.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle deletion failure
                    Log.e("EditUserDetails", "Error deleting user: " + t.getMessage());
                    Toast.makeText(EditUserDetails.this, "Error deleting user.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private String convertImageToBase64(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (IOException e) {
            Log.e("EditUserDetails", "Error converting image to base64: " + e.getMessage());
            return null;
        }
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "ProfilePicture", null);
        return Uri.parse(path);
    }
}
