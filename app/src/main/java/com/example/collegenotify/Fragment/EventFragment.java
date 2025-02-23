package com.example.collegenotify.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.collegenotify.Activity.AddEventActivity;
import com.example.collegenotify.Adapter.EventAdapter;
import com.example.collegenotify.Model.EventModel;
import com.example.collegenotify.R;
import com.example.collegenotify.Services.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EventFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private DatabaseHelper databaseHelper;
    private List<EventModel> eventList;
    private LinearLayout layEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_event, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        layEvent = v.findViewById(R.id.layEvent);

        databaseHelper = new DatabaseHelper(requireActivity());
        loadEvents();  // Load events when the view is created
        setupRecyclerView();

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(v1 -> {
            Intent intent = new Intent(requireActivity(), AddEventActivity.class);
            startActivity(intent);
        });

        return v;
    }

    private void loadEvents() {
        eventList = databaseHelper.getAllEvents();
    }

    private void setupRecyclerView() {
        if (eventList != null && !eventList.isEmpty()) {
            layEvent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            eventAdapter = new EventAdapter(requireActivity(), eventList);
            recyclerView.setAdapter(eventAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            layEvent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents(); // Reload events when fragment resumes
        setupRecyclerView(); // Update the RecyclerView with the new data
    }
}
