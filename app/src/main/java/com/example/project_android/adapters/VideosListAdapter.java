package com.example.project_android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.UserState;
import com.example.project_android.entities.VideoData;

import java.io.InputStream;
import java.util.List;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    private static final String TAG = "VideosListAdapter";

    public interface OnItemClickListener {
        void onItemClick(VideoData video);
        void onEditClick(VideoData video);
        void onDeleteClick(VideoData video);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView videoTitle;
        private final TextView videoAuthor;
        private final TextView videoViews;
        private final TextView videoUploadTime;
        private final ImageView videoAuthorImage;
        private final ImageView videoImage;
        private final Button editButton;
        private final Button deleteButton;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoAuthor = itemView.findViewById(R.id.videoAuthor);
            videoViews = itemView.findViewById(R.id.videoViews);
            videoUploadTime = itemView.findViewById(R.id.videoUploadTime);
            videoAuthorImage = itemView.findViewById(R.id.videoAuthorImage);
            videoImage = itemView.findViewById(R.id.videoImage);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        // Load image from local path, URI, or drawable resource
        private void loadImage(String path, ImageView imageView) {
            try {
                // Check if the path is a drawable resource
                int resId = imageView.getContext().getResources().getIdentifier(path, "drawable", imageView.getContext().getPackageName());
                if (resId != 0) {
                    imageView.setImageResource(resId);
                } else if (path.startsWith("content://") || path.startsWith("file://")) {
                    // Load from URI
                    Uri uri = Uri.parse(path);
                    InputStream inputStream = imageView.getContext().getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                Log.d(TAG, "Loaded image from path: " + path);
            } catch (Exception e) {
                Log.e(TAG, "Error loading image: " + e.getMessage());
            }
        }
    }

    private final LayoutInflater mInflater;
    private List<VideoData> videoData;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public VideosListAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        if (videoData != null) {
            VideoData current = videoData.get(position);
            holder.videoTitle.setText(current.getTitle());
            holder.videoAuthor.setText(current.getAuthor());
            holder.videoViews.setText(current.getViews());
            holder.videoUploadTime.setText(current.getUploadTime());

            // Load author image
            holder.loadImage(current.getAuthorImage(), holder.videoAuthorImage);

            // Load video thumbnail
            holder.loadImage(current.getImg(), holder.videoImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(current);
                    }
                }
            });

            if (UserState.isLoggedIn()) {
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);
            } else {
                holder.editButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
            }

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onEditClick(current);
                    }
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onDeleteClick(current);
                    }
                }
            });
        } else {
            holder.videoTitle.setText("No VideoData");
        }
    }

    public void setVideos(List<VideoData> videoData) {
        this.videoData = videoData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (videoData != null)
            return videoData.size();
        else return 0;
    }

    public List<VideoData> getVideos() {
        return videoData;
    }
}
