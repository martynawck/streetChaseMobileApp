package com.example.martyna.sc.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.martyna.sc.Fragments.AllStreetGamesFragment;
import com.example.martyna.sc.Fragments.MyCompletedStreetGamesFragment;
import com.example.martyna.sc.Fragments.MyPlayedStreetGamesFragment;
import com.example.martyna.sc.Fragments.MyStreetGamesFragment;
import com.example.martyna.sc.Interfaces.UpdateFragmentInterface;
import com.example.martyna.sc.Utilities.SessionManager;
import com.example.martyna.sc.R;

import java.util.ArrayList;
import java.util.List;

//import info.androidhive.materialtabs.R;
//import info.androidhive.materialtabs.fragments.OneFragment;
//import info.androidhive.materialtabs.fragments.ThreeFragment;
//import info.androidhive.materialtabs.fragments.TwoFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SessionManager session;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.streetchase);
        menu.setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {
            }

            @Override
            public void onPageSelected(final int i) {
                UpdateFragmentInterface fragment = (UpdateFragmentInterface) adapter.instantiateItem(viewPager, i);
                if (fragment != null) {
                    fragment.updateFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int i) {
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                moveToBackAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void moveToBackAndFinish() {
        moveTaskToBack(true);
        session.destroySession();
        MainActivity.this.finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyStreetGamesFragment(), "MOJE GRY");
        adapter.addFragment(new MyPlayedStreetGamesFragment(), "W CO GRAM");
        adapter.addFragment(new AllStreetGamesFragment(), "POZOSTAłE GRY");
        adapter.addFragment(new MyCompletedStreetGamesFragment(), "UKOŃCZONE GRY");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}