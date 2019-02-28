package com.ncirl.jank.spyderdroid;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Author: Jan Kowal
 * MessageFragment extending Fragment
 * This class will handle all Messages to display on the LCD screen
 */
public class MessageFragment extends Fragment {
    //private GUI properties
    private TextView message;
    private EditText userInput;
    private Button sendMsgBtn;
    //database ref
    private DatabaseReference msgRef;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout to view
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        //init properties
        msgRef = FirebaseDatabase.getInstance().getReference("display/message");
        message = view.findViewById(R.id.msg);
        userInput = view.findViewById(R.id.new_msg);
        sendMsgBtn = view.findViewById(R.id.send_btn);

        //send msg button event listener
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = userInput.getText().toString();
                //sync with firebase
                msgRef.setValue(temp);
                userInput.setText("");
            }
        });
        //event listener for firebase ref
        msgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //when message changes in DB
                message.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("couldn't connect to DB");
            }
        });
        //return views
        return view;

    }

}
