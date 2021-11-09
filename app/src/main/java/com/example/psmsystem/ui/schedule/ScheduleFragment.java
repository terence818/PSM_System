package com.example.psmsystem.ui.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psmsystem.R;
import com.example.psmsystem.Recipes;
import com.example.psmsystem.ui.home.HomeAdapter;
import com.example.psmsystem.ui.home.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Schedule> data, pending;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<String> key;
    private TextView total_cal;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ValueEventListener a;

    IntentFilter filter = new IntentFilter("com.codinginflow.EXAMPLE_ACTION");
    private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            Log.e("smsBroadcastReceiver", "onReceive");
        }
    };

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(smsBroadcastReceiver);
        super.onDestroyView();
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        total_cal = (TextView) view.findViewById(R.id.textView3);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<Schedule>();
        pending = new ArrayList<Schedule>();
        key = new ArrayList<String>();


        getActivity().registerReceiver(smsBroadcastReceiver, filter);
//        data.add(new Plan("s","s","s","s"));


//            i++;
        adapter = new ScheduleAdapter(getActivity(), data, 3, key);


        recyclerView.setAdapter(adapter);

        //populate the adapter
        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                if (dataSnapshot.exists()) {

                    int total_calories = 0;
                    for (DataSnapshot snapshot : dataSnapshot.child("Schedule_List").getChildren()) {
                        Schedule key_Detail = snapshot.getValue(Schedule.class);
                        if (key_Detail != null) {
                            total_calories += Integer.parseInt(key_Detail.getCalories());
                        }
//                        total_cal+=Integer.valueOf(key_Detail.getCalories());
                        key.add(snapshot.getKey());
                        data.add(key_Detail);
                    }
                    total_cal.setText("Total Calories: " +total_calories + "kCal" );
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //populate the adapter
        return view;
    }

}