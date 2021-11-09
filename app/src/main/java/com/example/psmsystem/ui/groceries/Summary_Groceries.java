package com.example.psmsystem.ui.groceries;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.psmsystem.Ingredients;
import com.example.psmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class Summary_Groceries extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Ingredients> data, pending;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<String> key;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ValueEventListener a;
    public static Summary_Groceries summary_groceries;
    Button add;

    private ArrayList<String> checked_key;
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

    public  void addKey(String key_string){
        String[] keys=key_string.split(",");
        checked_key.addAll(Arrays.asList(keys));

    }

    public  void removeKey(String key_string){
        String[] keys=key_string.split(",");
        checked_key.removeAll(Arrays.asList(keys));

    }





    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary__groceries, container, false);
        summary_groceries=this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<Ingredients>();
        pending = new ArrayList<Ingredients>();
        key= new ArrayList<String>();
        checked_key= new ArrayList<String>();



        getActivity().registerReceiver(smsBroadcastReceiver, filter);
//        data.add(new Plan("s","s","s","s"));


//            i++;
        adapter = new SummaryGroceriesAdapter(getActivity(),data ,3,key);


        recyclerView.setAdapter(adapter);



        add = (Button)view.findViewById(R.id.groceries_submit);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                for(String key:checked_key){
                    ref.child(user.getUid()).child("Groceries_List").child(key).child("status").setValue("Pending");
                }

            }
        });

        //populate the adapter
        ref.child(user.getUid()).child("Groceries_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ingredients key_Detail = snapshot.getValue(Ingredients.class);

                        if(key_Detail.getStatus().equals("Checked")) {
                            int i = getIndexByProperty(key_Detail.getIngredient_name());
                            if (i >= 0) {
                                key.set(i, key.get(i) + "," + snapshot.getKey());
                                data.get(i).setQuantity(String.valueOf(Integer.valueOf(data.get(i).getQuantity()) + Integer.valueOf(key_Detail.getQuantity())));
                            } else {
                                data.add(key_Detail);
                                key.add(snapshot.getKey());
                            }

                        }
                    }

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

    private int getIndexByProperty(String food) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) != null && data.get(i).getIngredient_name().equals(food)) {
                return i;
            }
        }
        return -1;// not there is list
    }


}