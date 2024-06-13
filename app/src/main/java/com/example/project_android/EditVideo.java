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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_android.entities.VideoData;

import java.io.IOException;

public class EditVideo extends AppCompatActivity {

    private static final int REQUEST_THUMBNAIL_GET = 1;
    private static final int REQUEST_VIDEO_GET = 2;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewVideoDetails;
    private ImageView imageViewThumbnail;
    private Button buttonUploadThumbnail;
    private Button buttonUploadVideo;
    private Button buttonSubmitVideo;
    private TextView textViewError;

    private Uri selectedThumbnailUri;
    private Uri selectedVideoUri;
    private VideoData videoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonUploadThumbnail = findViewById(R.id.buttonUploadThumbnail);
        buttonUploadVideo = findViewById(R.id.buttonUploadVideo);
        buttonSubmitVideo = findViewById(R.id.buttonSubmitVideo);
        textViewError = findViewById(R.id.textViewError);
        imageViewThumbnail = findViewById(R.id.imageViewThumbnail);
        textViewVideoDetails = findViewById(R.id.textViewVideoDetails);

        Intent intent = getIntent();
        int videoId = intent.getIntExtra("video_id", -1);
        if (videoId != -1) {
            videoData = VideosState.getInstance().getVideoById(videoId);
            if (videoData != null) {
                editTextTitle.setText(videoData.getTitle());
                editTextDescription.setText(videoData.getDescription());
                selectedThumbnailUri = Uri.parse(videoData.getImg());
                selectedVideoUri = Uri.parse(videoData.getVideo());
                try {
                    Bitmap thumbnailBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedThumbnailUri);
                    imageViewThumbnail.setImageBitmap(thumbnailBitmap);
                } catch (IOException e) {
                    Log.e("EditVideoActivity", "Error loading thumbnail: " + e.getMessage());
                }
                textViewVideoDetails.setText("Video File Successfully Loaded");
            }
        }

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
                updateVideo();
            }
        });

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomePage
                Intent intent = new Intent(EditVideo.this, HomePage.class);
                startActivity(intent);
            }
        });

    }

    private void openGalleryForThumbnail() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_THUMBNAIL_GET);
    }

    private void openGalleryForVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_GET);
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
                    Log.e("EditVideoActivity", "Error loading thumbnail: " + e.getMessage());
                }
            } else if (requestCode == REQUEST_VIDEO_GET) {
                selectedVideoUri = uri;
                textViewVideoDetails.setText("Video File Successfully Loaded");
                textViewVideoDetails.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateVideo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || selectedThumbnailUri == null || selectedVideoUri == null) {
            textViewError.setText("Please fill all fields to upload.");
            Log.d("EditVideoActivity", "Error: Please fill all fields to upload.");
            textViewError.setVisibility(View.VISIBLE);
        } else {
            textViewError.setVisibility(View.GONE);
            videoData.setTitle(title);
            videoData.setDescription(description);
            videoData.setImg(selectedThumbnailUri.toString());
            videoData.setVideo(selectedVideoUri.toString());

            VideosState.getInstance().updateVideo(videoData);
            Toast.makeText(this, "Video successfully updated.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditVideo.this, HomePage.class);
            startActivity(intent);
        }
    }
}
