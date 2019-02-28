package com.ncirl.jank.spyderdroid;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Author: Jan Kowal
 * MainActivity extending AppCompatActivity
 * App's main activity
 */
public class MainActivity extends AppCompatActivity {
    //private GUI properties
    private Toolbar toolbar;
    private ViewPager pager;
    //private FragmentPagerAdapter
    private FragmentPagerAdapter fragmentAdapter;
    //firebase auth object
    private FirebaseAuth fbAuth;

    @Override
    //on create call
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the custom toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // get pager from
        pager = findViewById(R.id.pager);
        //init fragment adapter
        fragmentAdapter = new FragmentCollectionAdapter(getSupportFragmentManager());
        //set pager's adapter to fragment adapter
        pager.setAdapter(fragmentAdapter);
        //run firebase auth using credentials
        fbAuth = FirebaseAuth.getInstance();
        fbAuth.signInWithEmailAndPassword("***", "***")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Logged in"); // log to the console
                        } else {
                            System.out.println("Problems with authentication");
                        }
                    }
                });

    }
    //inflate the custom top bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when item from top bar custom menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch statement to catch the menu item id and do take an appropriate action ( get fragment )
        * by calling pager.setCurrentItem()
        */
        switch (item.getItemId()){
            case R.id.action_temp:
                pager.setCurrentItem(0, true);
                return true;
            case R.id.action_ranger:
                pager.setCurrentItem(1, true);
                return true;
            case R.id.action_light:
                pager.setCurrentItem(2, true);
                return true;
            case R.id.action_sound:
                pager.setCurrentItem(3, true);
                return true;
            case R.id.action_rgb:
                pager.setCurrentItem(4, true);
                return true;
            case R.id.action_msg:
                pager.setCurrentItem(5,true);
                return true;
            default:
                return true;
        }
    }
}
