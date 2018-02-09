package idv.sean.photo_insearch.activity;


import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import idv.sean.photo_insearch.util.MyPagerAdapter;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.vo.MemVO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQ_LOGIN = 1;
    private TabLayout tabLayout;
    private Toolbar myToolBar;
    private ActionBarDrawerToggle myToggle;
    private DrawerLayout drawerLayout;
    private TextView signIn, signOut, tvUser;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private boolean login;
    private MemVO memVO;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initBody();
        initTextViewButton();
    }

    //檢查是否已經登入 check if login or not
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("preference", MODE_PRIVATE);
        login = sharedPreferences.getBoolean("login", false);
        if (login) {//login = true
            String memJson = sharedPreferences.getString("memVO", "");
            if (memJson.length() > 0) { //memVO not null
                memVO = gson.fromJson(memJson, MemVO.class);
                signIn.setVisibility(View.INVISIBLE);
                signOut.setVisibility(View.VISIBLE);
                tvUser.setText(memVO.getMem_name());

            } else {//memVO = null
                login = false;
                sharedPreferences.edit().putBoolean("login", false).apply();
                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.INVISIBLE);
            }

        } else {//login = false
            signIn.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.INVISIBLE);
            tvUser.setText("訪客");
        }
    }

    public void findViews() {
        //toolbar setting
        myToolBar = (Toolbar) findViewById(R.id.toolBar_main);
        myToolBar.setLogo(R.mipmap.logo1);
        myToolBar.setTitle(R.string.title);
        setSupportActionBar(myToolBar);


        //drawer setting
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        myToggle = new ActionBarDrawerToggle
                (this, drawerLayout, myToolBar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();

        navigationView = findViewById(R.id.navView);
        /**************神!!*************/
        navigationView.bringToFront();
        /**************神!!************/
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

    }

    public void initBody() {
        //viewpager setting
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_main);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        //tab setting
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_main);
        tabLayout.setupWithViewPager(viewPager);


        //set drawer header text button
        View header = navigationView.getHeaderView(0);
        tvUser = (TextView) header.findViewById(R.id.tvUser);
        signIn = (TextView) header.findViewById(R.id.tvSignIn);
        signOut = (TextView) header.findViewById(R.id.tvSignOut);

    }

    public void initTextViewButton() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);

                Intent loginIntent = new Intent
                        (MainActivity.this, LoginDialogActivity.class);
                startActivityForResult(loginIntent, REQ_LOGIN);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = false;
                sharedPreferences.edit().putBoolean("login", false).apply();
                tvUser.setText("訪客");
                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.INVISIBLE);
                memVO = null;
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //登入成功 設定會員資料   set member information after succeeded login
        if (requestCode == REQ_LOGIN) {
            if (requestCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                memVO = (MemVO) bundle.getSerializable("memVO");
                signIn.setVisibility(View.INVISIBLE);
                signOut.setVisibility(View.VISIBLE);
                tvUser.setText(memVO.getMem_name());
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.wtf("????????", "......................");
        Toast.makeText(this, "00000", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.mem:
                Log.wtf("mem", "......................");
                break;
            case R.id.aboutUs:
                Log.wtf("aboutUs", "......................");
                break;

            case R.id.report:
                Log.wtf("report", "......................");

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
