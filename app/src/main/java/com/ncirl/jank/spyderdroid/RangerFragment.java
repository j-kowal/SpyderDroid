
package com.ncirl.jank.spyderdroid;

//imports
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Author: Jan Kowal
 * RangerFragment extending Fragment
 * This fragment will be handling all Ranger sensor activities.
 */
public class RangerFragment extends Fragment {

    //private values from GUI
    private Switch rangerSwitch;
    private SeekBar rangerThreshold;
    private TextView rangerThresholdTxt;
    private TextView rangerDistance;
    private ImageView rangerIntruder;
    private TextView rangerAlarmTxt;
    private ConstraintLayout rangerLayout;
    private TextView rangerDistanceTxt;
    //indicator if Ranger need to reset's it's data to the values from firebase
    private boolean needToGet = false;
    private DatabaseReference rangerRef;
    //android vibrator object
    private Vibrator vib;

    public RangerFragment() {
        // Required empty public constructor
    }
    //on create view...
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranger, container, false);

        //init objects
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        rangerLayout = view.findViewById(R.id.bg_ranger);
        rangerSwitch = view.findViewById(R.id.ranger_switch);
        rangerThreshold = view.findViewById(R.id.ranger_seekbar);
        rangerThresholdTxt = view.findViewById(R.id.ranger_threshold_txt);
        rangerDistance = view.findViewById(R.id.ranger_distance);
        rangerIntruder = view.findViewById(R.id.ranger_intruder);
        rangerDistanceTxt = view.findViewById(R.id.ranger_distance_txt);
        rangerAlarmTxt = view.findViewById(R.id.ranger_alarm_txt);
        rangerThresholdTxt.setText(String.valueOf(rangerThreshold.getProgress()) + " cm");
        rangerRef = FirebaseDatabase.getInstance().getReference("sensors/ultra_range");

        //add event listener to the DB reference
        rangerRef.addValueEventListener(new ValueEventListener() {
            // this method is called every time something changes in DB
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check if range sensor is on
                if ((int) (long) dataSnapshot.child("is_on").getValue() == 1){
                    //set the switch to on
                    rangerSwitch.setChecked(true);
                    //set distance to the value and add 'cm'
                    rangerDistance.setText(String.valueOf(dataSnapshot.child("value").getValue()) + " cm");

                    //if the app just started it needs to pull threshold value and set it to seekbar
                    if (needToGet) {
                        rangerThreshold.setProgress((int) (long) dataSnapshot.child("alarm_threshold_distance").getValue());
                        needToGet = false;
                    }

                    //if alarm is on
                    if ((int) (long) dataSnapshot.child("alarm_is_on").getValue() == 1) {
                        //show intruder icon, set background to "pinkish" and vibrate for 2 s
                        rangerIntruder.setVisibility(View.VISIBLE);
                        rangerLayout.setBackgroundColor(Color.parseColor("#ffebee"));
                        vib.vibrate(2000);
                    }
                    else {
                        // hide the intruder icon and set the background back to white
                        rangerIntruder.setVisibility(View.GONE);
                        rangerLayout.setBackgroundColor(Color.WHITE);
                    }
                }
                else {
                    //if range sensor is off set switch to off.
                    rangerSwitch.setChecked(false);
                }
            }
            //if error in DB
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Problems with DB");
            }
        });

        //listener for the seekbar
        rangerThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //on sliding the seekbar set threshold distance txt to value of seekbar and sync value with Firebase
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rangerThresholdTxt.setText(String.valueOf(seekBar.getProgress()) + " cm");
                rangerRef.child("alarm_threshold_distance").setValue(progress);
            }
            //empty
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            //empty
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //set listener to switch on/off button
        rangerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rangerRef.child("is_on").setValue(isChecked ? 1 : 0); //sync with firebase
                //if switch is on show all the rest of GUI
                if(isChecked){
                    needToGet = true; // this will download threshold seekbar value from FB and set it once
                    rangerThreshold.setVisibility(View.VISIBLE);
                    rangerThresholdTxt.setVisibility(View.VISIBLE);
                    rangerDistance.setVisibility(View.VISIBLE);
                    rangerDistanceTxt.setVisibility(View.VISIBLE);
                    rangerAlarmTxt.setVisibility(View.VISIBLE);
                } else {
                    //hiding all the GUI elements and only switch stays
                    rangerThreshold.setVisibility(View.GONE);
                    rangerThresholdTxt.setVisibility(View.GONE);
                    rangerDistance.setVisibility(View.GONE);
                    rangerIntruder.setVisibility(View.GONE);
                    rangerDistanceTxt.setVisibility(View.GONE);
                    rangerAlarmTxt.setVisibility(View.GONE);
                }

            }
        });
        //return view
        return view;
    }

}
