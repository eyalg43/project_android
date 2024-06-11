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
    }

    private final LayoutInflater mInflater;
    private List<VideoData> videoData;
    private Context context;

    public VideosListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
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

            int authorImageResId = context.getResources().getIdentifier(current.getAuthorImage(), "drawable", context.getPackageName());
            int imgResId = context.getResources().getIdentifier(current.getImg(), "drawable", context.getPackageName());

            holder.videoAuthorImage.setImageResource(authorImageResId);
            holder.videoImage.setImageResource(imgResId);
        } else {
            holder.videoTitle.setText("No VideoData");
        }
    }

    public void setVideos(List<VideoData> videoData){
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
