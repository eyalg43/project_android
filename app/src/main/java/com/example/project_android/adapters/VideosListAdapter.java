package com.example.project_android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entities.VideoData;

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
        private final View editButton;
        private final View deleteButton;

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

        private void loadImage(String base64Str, ImageView imageView) {
            try {
                if (base64Str != null && !base64Str.isEmpty()) {
                    byte[] decodedString = Base64.decode(base64Str.split(",")[1], Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(decodedByte);
                } else {

                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading image: " + e.getMessage());
            }
        }
    }

    private final LayoutInflater mInflater;
    private List<VideoData> videoData;
    private OnItemClickListener onItemClickListener;

    public VideosListAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
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
        return videoData != null ? videoData.size() : 0;
    }
}
