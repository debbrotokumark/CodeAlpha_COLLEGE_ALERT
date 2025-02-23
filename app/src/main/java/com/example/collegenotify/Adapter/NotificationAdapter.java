package com.example.collegenotify.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.collegenotify.Activity.WebViewActivity;
import com.example.collegenotify.Model.NotificationModel;
import com.example.collegenotify.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final Context context;
    private final List<NotificationModel> notificationList;

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.body.setText(notification.getBody());
        holder.timestamp.setText(notification.getTimestamp());

        // Handle link click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", notification.getLink());
            intent.putExtra("title", notification.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, body, timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            body = itemView.findViewById(R.id.notification_body);
            timestamp = itemView.findViewById(R.id.notification_time);
        }
    }
}
