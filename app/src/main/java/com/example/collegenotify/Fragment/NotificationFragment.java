package com.example.collegenotify.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.collegenotify.Adapter.NotificationAdapter;
import com.example.collegenotify.Model.NotificationModel;
import com.example.collegenotify.R;
import com.example.collegenotify.Services.DatabaseHelper;
import java.util.List;


public class NotificationFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_notification, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayout layNoNoti = v.findViewById(R.id.layNoNoti);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        DatabaseHelper dbHelper = new DatabaseHelper(requireActivity());
        List<NotificationModel> notifications = dbHelper.getAllNotifications();

        if (notifications.size()<=0 || notifications.isEmpty()){
            layNoNoti.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            layNoNoti.setVisibility(View.GONE);
            NotificationAdapter adapter = new NotificationAdapter(requireActivity(), notifications);
            recyclerView.setAdapter(adapter);
        }

        return v;
    }
}