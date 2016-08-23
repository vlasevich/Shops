package com.home.vlas.shops;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.home.vlas.shops.adapter.TabsPagerFragmentAdapter;
import com.home.vlas.shops.utils.ConnectivityReceiver;
import com.home.vlas.shops.utils.ShopApplication;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final int LAYOUT = R.layout.main_activity;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        setTheme(R.style.AppDefault);
        initToolbar();
        initNavigationView();
        initTabs();

        checkConnection();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getApplicationContext(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tablayout = (TabLayout) findViewById(R.id.tabLayout);
        tablayout.setupWithViewPager(viewPager);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.actionNotificationItem: {
                        showNotificationTab();
                        break;
                    }
                    case R.id.actionNotificationItem2: {
                        showNotificationTab2();
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            System.out.println("CONNECTED");
        } else {
            System.out.println("NOT CONNECTED");
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            System.out.println("CONNECTED");
        } else {
            System.out.println("NOT CONNECTED");
        }
        //showSnack(isConnected);
    }

    @Override
    public void onResume() {
        super.onResume();
        ShopApplication.getInstance().setConnectivityListener(this);
    }

    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_ONE);
    }

    private void showNotificationTab2() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }
}
