package com.example.project_android.adapters;

import android.content.Context;
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

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoData> videoList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(VideoData videoData);
    }

    public VideoAdapter(List<VideoData> videoList, OnItemClickListener onItemClickListener) {
        this.videoList = videoList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoData videoData = videoList.get(position);

        holder.titleTextView.setText(videoData.getTitle());
        holder.authorViewsUploadTimeTextView.setText(
                context.getString(R.string.video_author_views_uploadtime,
                        videoData.getAuthor(), videoData.getViews(), videoData.getUploadTime())
        );

        // Load video thumbnail
        int imageResource = context.getResources().getIdentifier(videoData.getImg(), "drawable", context.getPackageName());
        holder.thumbnailImageView.setImageResource(imageResource);

        // Load author image
        int authorImageResource = context.getResources().getIdentifier(videoData.getAuthorImage(), "drawable", context.getPackageName());
        holder.authorImageView.setImageResource(authorImageResource);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(videoData));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateVideoList(List<VideoData> newVideoList) {
        this.videoList = newVideoList;
        notifyDataSetChanged();
    }

    public List<VideoData> getVideoList() {
        return videoList;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView authorViewsUploadTimeTextView;
        ImageView authorImageView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.video_thumbnail);
            titleTextView = itemView.findViewById(R.id.video_title);
            authorViewsUploadTimeTextView = itemView.findViewById(R.id.video_author_views_uploadtime);
            authorImageView = itemView.findViewById(R.id.author_image);
        }
    }
}
