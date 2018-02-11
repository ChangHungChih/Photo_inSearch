package idv.sean.photo_insearch.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import idv.sean.photo_insearch.util.EndDrawerToggle;
import idv.sean.photo_insearch.util.MyPagerAdapter;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.Util;
import idv.sean.photo_insearch.vo.MemVO;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQ_LOGIN = 1;
    private static final int PAGER_HOME = 1;
    private static final int PAGER_MEM = 2;
    private TabLayout tabLayout;
    private Toolbar myToolBar;
    private ViewPager viewPager;
    private EndDrawerToggle endDrawerToggle;
    private DrawerLayout drawerLayout;
    private TextView signIn, signOut, tvUser;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private boolean login;
    private MemVO memVO;
    private MyPagerAdapter myPagerAdapter;
    private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

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
            if (memJson.length() > 0) { //if memVO not null
                memVO = Util.gson.fromJson(memJson, MemVO.class);
                signIn.setVisibility(View.INVISIBLE);
                signOut.setVisibility(View.VISIBLE);
                tvUser.setText(memVO.getMem_name());

            } else {//if memVO = null
                login = false;
                sharedPreferences.edit().putBoolean("login", false).apply();
                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.INVISIBLE);
            }

        } else {//login = false
            memVO = null;
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
        myToolBar.setSubtitle("首頁");
        setSupportActionBar(myToolBar);

        //drawer setting
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        endDrawerToggle = new EndDrawerToggle
                (this,drawerLayout,myToolBar,
                        R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(endDrawerToggle);

        navigationView = findViewById(R.id.navView);
        /**************神!!*************/
        navigationView.bringToFront();
        /**************神!!************/
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        endDrawerToggle.syncState();
    }

    public void initBody() {
        //viewpager setting
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_HOME);
        viewPager = (ViewPager) findViewById(R.id.viewPager_main);
        viewPager.setAdapter(myPagerAdapter);

        //tab setting
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_main);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void initTextViewButton() {
        //set drawer header text button
        View header = navigationView.getHeaderView(0);
        tvUser = (TextView) header.findViewById(R.id.tvUser);
        signIn = (TextView) header.findViewById(R.id.tvSignIn);
        signOut = (TextView) header.findViewById(R.id.tvSignOut);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.END);

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
                drawerLayout.closeDrawer(GravityCompat.END);
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
    public boolean onNavigationItemSelected(MenuItem item) {//抽屜選單選擇動作
        switch (item.getItemId()) {
            case R.id.home:
                clearAllFragments();
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),PAGER_HOME);
                viewPager.setAdapter(myPagerAdapter);
                myToolBar.setSubtitle("首頁");
                break;

            case R.id.mem:
                clearAllFragments();
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),PAGER_MEM);
                viewPager.setAdapter(myPagerAdapter);
                myToolBar.setSubtitle("會員專區");
                break;

            case R.id.news:
                myToolBar.setSubtitle("最新消息");

                break;

            case R.id.qapage:
                myToolBar.setSubtitle("Q & A");

                break;

            case R.id.aboutUs:
                myToolBar.setSubtitle("關於我們");

                break;

            case R.id.report:
                myToolBar.setSubtitle("意見回饋");
                clearAllFragments();
                viewPager.setAdapter(null);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    public void clearAllFragments(){//clear fragments in viewPager
        for(int i = 0; i < myPagerAdapter.getCount(); i++) {
            fragmentManager.beginTransaction().remove(myPagerAdapter.getItem(i)).commit();
        }
        myPagerAdapter.clearAll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//點擊後收合抽屜處理 handle drawer
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {//返回鍵動作
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

}
