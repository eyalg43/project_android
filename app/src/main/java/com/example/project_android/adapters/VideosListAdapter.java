package com.example.project_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android.R;
import com.example.project_android.entities.VideoData;

import java.util.List;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(VideoData video);
    }

    private final LayoutInflater mInflater;
    private List<VideoData> videoData;
    private Context context;
    private final OnItemClickListener listener;

    public VideosListAdapter(Context context, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView videoTitle;
        private final TextView videoAuthor;
        private final TextView videoViews;
        private final TextView videoUploadTime;
        private final ImageView videoAuthorImage;
        private final ImageView videoImage;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoAuthor = itemView.findViewById(R.id.videoAuthor);
            videoViews = itemView.findViewById(R.id.videoViews);
            videoUploadTime = itemView.findViewById(R.id.videoUploadTime);
            videoAuthorImage = itemView.findViewById(R.id.videoAuthorImage);
            videoImage = itemView.findViewById(R.id.videoImage);
        }

        public void bind(final VideoData video, final OnItemClickListener listener) {
            videoTitle.setText(video.getTitle());
            videoAuthor.setText(video.getAuthor());
            videoViews.setText(video.getViews());
            videoUploadTime.setText(video.getUploadTime());

            int authorImageResId = context.getResources().getIdentifier(video.getAuthorImage(), "drawable", context.getPackageName());
            int imgResId = context.getResources().getIdentifier(video.getImg(), "drawable", context.getPackageName());

            videoAuthorImage.setImageResource(authorImageResId);
            videoImage.setImageResource(imgResId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(video);
                }
            });
        }
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
            holder.bind(current, listener);
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
