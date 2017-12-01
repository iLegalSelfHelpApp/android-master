package com.cs401.ilegal.ilegal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainNavigationActivity extends AppCompatActivity {
    static final String adminemail = "seanyuanuscedu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
        tabLayout.addTab(tabLayout.newTab().setText("Saved"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.userprofile:
                Intent i = new Intent(MainNavigationActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            // action with ID action_settings was selected
            case R.id.reset_password:
                Intent g = new Intent(MainNavigationActivity.this, ResetPasswordActivity.class);
                startActivity(g);
                break;
            case R.id.chat:
                String myemail = UserSingleton.getInstance().getEmail();
                String myaccount = myemail.replace("@","").replace(".","");
                if(myaccount.equals(MainNavigationActivity.adminemail)){
                    Intent p = new Intent(MainNavigationActivity.this, ChatListviewActivity.class);
                    startActivity(p);
                    break;
                }else{
                    Intent p = new Intent(MainNavigationActivity.this, Chat.class);
                    p.putExtra("useremail", "User");
                    startActivity(p);
                    break;
                }
            case R.id.action_logout:
                Intent h = new Intent(MainNavigationActivity.this, MainActivity.class);
                startActivity(h);
                break;
            default:
                break;
        }

        return true;
    }


}
