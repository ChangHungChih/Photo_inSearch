package idv.sean.photo_insearch.activity;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import idv.sean.photo_insearch.util.MyPagerAdapter;

import idv.sean.photo_insearch.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final int REQ_LOGIN = 1;
    private TabLayout tabLayout;
    private Toolbar myToolBar;
    private ActionBarDrawerToggle myToggle;
    private DrawerLayout drawerLayout;
    private TextView signIn, signOut;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    public void findViews(){
        //toolbar setting
        myToolBar = (Toolbar)findViewById(R.id.toolBar_main);
        myToolBar.setLogo(R.mipmap.logo1);
        myToolBar.setTitle(R.string.title);
        setSupportActionBar(myToolBar);




        //drawer setting
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        myToggle = new ActionBarDrawerToggle
                (this,drawerLayout, myToolBar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();

        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);




        //viewpager setting
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager_main);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        //tab setting
        tabLayout = (TabLayout)findViewById(R.id.tabLayout_main);
        tabLayout.setupWithViewPager(viewPager);


        //////////////////////////////////////////////////////////////////
        View header = navigationView.getHeaderView(0);
        signIn = (TextView) header.findViewById(R.id.tvSignIn);
        signOut = (TextView) header.findViewById(R.id.tvSignOut);
        signIn.setClickable(true);
        signIn.setFocusable(true);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("???","......................");
                Toast.makeText(MainActivity.this,"Sign In",Toast.LENGTH_SHORT).show();
                TextView v = (TextView)view;
                v.setText("?????");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_drawer, menu);

        //right menu click event
        myToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String msg = "";
                switch (item.getItemId()){
                    case R.id.aboutUs:
                        msg += "Clicked AboutUs!";
                        break;
                    case R.id.mem:
                        msg += "Clicked Member!";
                        Intent loginIntent = new Intent
                                (MainActivity.this,LoginDialogActivity.class);
                        startActivityForResult(loginIntent,REQ_LOGIN);
                        break;
                    case R.id.report:
                        msg += "Clicked Report!";
                        break;
                }

                if(!msg.equals("")){
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        Log.wtf("????????","......................");
        Toast.makeText(this,"00000",Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.aboutUs:
                Log.wtf("aboutUs","......................");
                break;
            case R.id.mem:
                Log.wtf("mem","......................");
                break;
            case R.id.report:
                Log.wtf("report","......................");

                break;
        }


        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//            } else {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }



}
