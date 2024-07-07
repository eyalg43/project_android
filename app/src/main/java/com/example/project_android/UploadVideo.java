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
import com.example.project_android.VideosState;
import com.example.project_android.entities.VideoData;

import java.io.IOException;

public class UploadVideo extends AppCompatActivity {

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
    private Button buttonCancel;
    private Uri selectedThumbnailUri;
    private Uri selectedVideoUri;
    private Bitmap selectedThumbnailBitmap;


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
        Button buttonCancel = findViewById(R.id.buttonCancel);

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

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    selectedThumbnailBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedThumbnailUri);
                    imageViewThumbnail.setImageBitmap(selectedThumbnailBitmap);
                    imageViewThumbnail.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Log.e("UploadVideo", "Error loading thumbnail: " + e.getMessage());
                }
            } else if (requestCode == REQUEST_VIDEO_GET) {
                selectedVideoUri = uri;
                Toast.makeText(this, "Video File Successfully Loaded.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitVideo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String uploadTime = getElapsedTime(System.currentTimeMillis());
        int newVideoId = VideosState.getInstance().getLatestVideoId() + 1;

        // converts bitmap to base64 string and vice versa
        Converters converter = new Converters();

        if (title.isEmpty() || description.isEmpty() || selectedThumbnailUri == null || selectedVideoUri == null) {
            Toast.makeText(this, "Please fill all fields to upload.", Toast.LENGTH_SHORT).show();
        } else {
            textViewError.setVisibility(View.GONE);
            String author = UserState.getLoggedInUser().getDisplayName();
            String authorImageBase64 = UserState.getLoggedInUser().getProfilePicture();
            String thumbnailBase64 = converter.fromBitmap(selectedThumbnailBitmap);
            String videoUriString = selectedVideoUri.toString();

            // Add new video to the state
            VideoData newVideo = new VideoData(
                    newVideoId, // Generate new ID
                    title,
                    description,
                    author,
                    "1 views",
                    thumbnailBase64,
                    videoUriString,
                    uploadTime,
                    authorImageBase64
            );
            // VideosState.getInstance().addVideo(newVideo);
            // uploadVideoToServer(newVideo);
            Toast.makeText(this, "Video successfully uploaded to Vidtube.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UploadVideo.this, HomePage.class);
            startActivity(intent);
        }
    }

    // ADD THE UPLOAD VIDEO TO SERVER METHOD HERE

    private String getElapsedTime(long uploadTime) {
        long now = System.currentTimeMillis();
        long diff = now - uploadTime;

        if (diff < 60000) {
            return "just now";
        } else if (diff < 3600000) {
            return (diff / 60000) + " minutes ago";
        } else if (diff < 86400000) {
            return (diff / 3600000) + " hours ago";
        } else {
            return (diff / 86400000) + " days ago";
        }
    }
}
