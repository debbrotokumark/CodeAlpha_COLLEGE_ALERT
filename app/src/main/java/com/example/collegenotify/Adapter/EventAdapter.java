package com.example.collegenotify.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegenotify.Activity.AddEventActivity;
import com.example.collegenotify.Model.EventModel;
import com.example.collegenotify.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private List<EventModel> eventList;

    public EventAdapter(Context context, List<EventModel> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel event = eventList.get(position);
        holder.eventName.setText(event.getName());
        holder.eventDescription.setText(event.getDescription());
        holder.eventTime.setText(event.getEventTime());
        holder.layEvent.setOnClickListener(v ->
                {
                    Intent intent = new Intent(context, AddEventActivity.class);
                    intent.putExtra("isupdate",true);
                    intent.putExtra("eventId",event.getId());
                    intent.putExtra("eventName",event.getName());
                    intent.putExtra("eventDescription",event.getName());
                    intent.putExtra("eventTime",event.getEventTime());
                    context.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDescription, eventTime;
        LinearLayout layEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventTime = itemView.findViewById(R.id.eventTime);
            layEvent = itemView.findViewById(R.id.layEvent);
        }
    }
}
