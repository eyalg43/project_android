package com.example.project_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class UploadVideo extends AppCompatActivity {

    private static final int REQUEST_THUMBNAIL_GET = 1;
    private static final int REQUEST_VIDEO_GET = 2;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonUploadThumbnail;
    private TextView textViewVideoDetails;
    private ImageView imageViewThumbnail;
    private Button buttonUploadVideo;
    private Button buttonSubmitVideo;
    private TextView textViewError;

    private Uri selectedThumbnailUri;
    private Uri selectedVideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonUploadThumbnail = findViewById(R.id.buttonUploadThumbnail);
        buttonUploadVideo = findViewById(R.id.buttonUploadVideo);
        buttonSubmitVideo = findViewById(R.id.buttonSubmitVideo);
        textViewError = findViewById(R.id.textViewError);
        imageViewThumbnail = findViewById(R.id.imageViewThumbnail);
        textViewVideoDetails = findViewById(R.id.textViewVideoDetails);

        buttonUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryForThumbnail();
            }
        });

        buttonUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryForVideo();
            }
        });

        buttonSubmitVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVideo();
            }
        });
    }

    private void openGalleryForThumbnail() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_THUMBNAIL_GET);
    }

    private void openGalleryForVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (requestCode == REQUEST_THUMBNAIL_GET) {
                selectedThumbnailUri = uri;
                try {
                    Bitmap thumbnailBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedThumbnailUri);
                    imageViewThumbnail.setImageBitmap(thumbnailBitmap);
                    imageViewThumbnail.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Log.e("UploadVideo", "Error loading thumbnail: " + e.getMessage());
                }
            } else if (requestCode == REQUEST_VIDEO_GET) {
                selectedVideoUri = uri;
                textViewVideoDetails.setText("Video File Successfully Loaded");
                textViewVideoDetails.setVisibility(View.VISIBLE);
            }
        }
    }


    private void submitVideo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || selectedThumbnailUri == null || selectedVideoUri == null) {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText("Please fill all fields and upload thumbnail and video.");
        } else {
            textViewError.setVisibility(View.GONE);
            // Proceed with video submission
            // Implement video submission logic here
        }
    }
}
