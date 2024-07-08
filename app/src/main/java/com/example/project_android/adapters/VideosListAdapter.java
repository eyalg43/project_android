package com.example.project_android.adapters;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_android.R;
import com.example.project_android.UserState;
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

        private void loadImage(String data, ImageView imageView) {
            Log.d(TAG, "Loading image: " + data);
            if (data.startsWith("data:image/")) {
                // Base64 encoded image
                try {
                    String base64Image = data.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Glide.with(imageView.getContext())
                            .asBitmap()
                            .load(decodedString)
                            .into(imageView);
                    Log.d(TAG, "Loaded base64 image");
                } catch (Exception e) {
                    Log.e(TAG, "Error loading base64 image: " + e.getMessage());
                }
            } else if (data.startsWith("http://") || data.startsWith("https://")) {
                // URL
                Glide.with(imageView.getContext())
                        .load(data)
                        .into(imageView);
                Log.d(TAG, "Loaded image from URL: " + data);
            } else {
                try {
                    // Check if the path is a drawable resource
                    int resId = imageView.getContext().getResources().getIdentifier(data, "drawable", imageView.getContext().getPackageName());
                    if (resId != 0) {
                        imageView.setImageResource(resId);
                        Log.d(TAG, "Loaded drawable resource: " + data);
                    } else {
                        // Load from local file path
                        Glide.with(imageView.getContext())
                                .load(data)
                                .into(imageView);
                        Log.d(TAG, "Loaded image from local file path: " + data);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error loading image: " + e.getMessage());
                }
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
