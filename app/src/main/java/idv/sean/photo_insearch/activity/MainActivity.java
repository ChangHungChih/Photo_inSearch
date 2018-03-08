package idv.sean.photo_insearch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.content.LocalBroadcastManager;
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

import java.util.HashMap;
import java.util.Map;

import idv.sean.photo_insearch.model.State;
import idv.sean.photo_insearch.util.EndDrawerToggle;
import idv.sean.photo_insearch.util.MyPagerAdapter;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.Utils;
import idv.sean.photo_insearch.model.MemVO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQ_LOGIN = 1;
    public static final int PAGER_HOME = 1;
    public static final int PAGER_MEM = 2;
    public static final int PAGER_NEWS = 3;
    public static final int PAGER_QA = 4;
    public static final int PAGER_ABOUT_US = 5;
    public static final int PAGER_REPORT = 6;
    public static final int PAGER_ORDER = 7;
    private final String TAG = "MainActivity";
    private TabLayout tabLayout;
    private Toolbar myToolBar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private TextView signIn, signOut, tvUser;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private boolean login;
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

    public void findViews() {
        //toolbar setting
        myToolBar = (Toolbar) findViewById(R.id.toolBar_main);
        myToolBar.setLogo(R.mipmap.logo1);
        myToolBar.setTitle(R.string.title);
        myToolBar.setSubtitle("首頁");
        setSupportActionBar(myToolBar);


        //drawer setting
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this,drawerLayout,myToolBar, R.string.drawer_open, R.string.drawer_close);
//        EndDrawerToggle endDrawerToggle = new EndDrawerToggle
//                (this, drawerLayout, myToolBar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.navView);
        /*let drawer can be touched */
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    public void initBody() {
        myToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                return true;
            }
        });

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
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent loginIntent = new Intent
                        (MainActivity.this, LoginDialogActivity.class);
                startActivityForResult(loginIntent, REQ_LOGIN);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear login data
                login = false;
                sharedPreferences.edit().putBoolean("login", false).apply();
                tvUser.setText("訪客");
                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.INVISIBLE);
                Utils.setMemVO(null);
                drawerLayout.closeDrawer(GravityCompat.START);
                Utils.disConnectWebSocketServer();
                //switch fragment to homepage
                navigationView.getMenu().getItem(0).setChecked(true);
                clearAllFragments();
                myToolBar.setSubtitle("首頁");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_HOME);
                viewPager.setAdapter(myPagerAdapter);
            }
        });
    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        endDrawerToggle.syncState();
//    }

    //檢查是否已經登入 check if login or not
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("preference", MODE_PRIVATE);
        login = sharedPreferences.getBoolean("login", false);
        if (login) {//login = true
            String memJson = sharedPreferences.getString("memVO", "");
            if (memJson.length() > 0) { //if memVO not null
                MemVO memVO = Utils.gson.fromJson(memJson, MemVO.class);
                Utils.setMemVO(memVO);
                signIn.setVisibility(View.INVISIBLE);
                signOut.setVisibility(View.VISIBLE);
                tvUser.setText(memVO.getMem_name());
                //start webSocket
                if (Utils.chatWebSocketClient == null) {
                    registerUserStateMapReceiver();
                    Utils.connectWebSocketServer(memVO.getMem_id(), this);
                }
            } else {//if memVO = null
                sharedPreferences.edit().putBoolean("login", false).apply();
                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.INVISIBLE);
            }
        } else {//login = false
            Utils.setMemVO(null);
            signIn.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.INVISIBLE);
            tvUser.setText("訪客");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //登入成功 跳轉頁面   switch page after succeeded login
        if (requestCode == REQ_LOGIN) {
            if (resultCode == RESULT_OK) {
                //switch to member page
                navigationView.getMenu().getItem(1).setChecked(true);
                myToolBar.setSubtitle("會員專區");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_MEM);
                viewPager.setAdapter(myPagerAdapter);
            }
        }
    }

    @Override                                               //switch fragment by drawer items
    public boolean onNavigationItemSelected(MenuItem item) {//抽屜選單選擇動作
        switch (item.getItemId()) {
            case R.id.home:
                clearAllFragments();
                myToolBar.setSubtitle("首頁");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_HOME);
                viewPager.setAdapter(myPagerAdapter);
                break;

            case R.id.mem:
                if(login == false){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent loginIntent = new Intent
                            (MainActivity.this, LoginDialogActivity.class);
                    startActivityForResult(loginIntent, REQ_LOGIN);
                    return false;
                }
                clearAllFragments();
                myToolBar.setSubtitle("會員專區");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_MEM);
                viewPager.setAdapter(myPagerAdapter);
                break;

            case R.id.order:
                if(login == false){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent loginIntent = new Intent
                            (MainActivity.this, LoginDialogActivity.class);
                    startActivityForResult(loginIntent, REQ_LOGIN);
                    return false;
                }
                clearAllFragments();
                myToolBar.setSubtitle("交易紀錄");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_ORDER);
                viewPager.setAdapter(myPagerAdapter);
                break;

            case R.id.news:
                clearAllFragments();
                myToolBar.setSubtitle("最新消息");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_NEWS);
                viewPager.setAdapter(myPagerAdapter);
                tabLayout.removeAllTabs();
                break;

            case R.id.qapage:
                clearAllFragments();
                myToolBar.setSubtitle("Q & A");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_QA);
                viewPager.setAdapter(myPagerAdapter);
                tabLayout.removeAllTabs();
                break;

            case R.id.aboutUs:
                clearAllFragments();
                myToolBar.setSubtitle("關於我們");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_ABOUT_US);
                viewPager.setAdapter(myPagerAdapter);
                tabLayout.removeAllTabs();
                break;

            case R.id.report:
                clearAllFragments();
                myToolBar.setSubtitle("意見回饋");
                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), PAGER_REPORT);
                viewPager.setAdapter(myPagerAdapter);
                tabLayout.removeAllTabs();
                break;
            default:
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearAllFragments() {//clear fragments in viewPager
        for (int i = 0; i < myPagerAdapter.getCount(); i++) {
            fragmentManager.beginTransaction().remove(myPagerAdapter.getItem(i)).commit();
        }
        myPagerAdapter.clearAll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//點擊後收合抽屜處理 handle drawer
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

    @Override
    public void onBackPressed() {//返回鍵動作
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void registerUserStateMapReceiver() {
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        UserStateMapReceiver mapReceiver = new UserStateMapReceiver(this);
        broadcastManager.registerReceiver(mapReceiver, openFilter);
        broadcastManager.registerReceiver(mapReceiver, closeFilter);
    }

    // 攔截user WebSocket 連線或斷線的broadcast，並在更新在線名單
    private class UserStateMapReceiver extends BroadcastReceiver {
        private MainActivity activity;

        public UserStateMapReceiver(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            State stateMessage = Utils.gson.fromJson(message, State.class);
            String type = stateMessage.getType();
            String userId = stateMessage.getUserId();
            String userName;

            switch (type) {
                case "open":    //when user connected
                    //get all online users
                    Map<String, String> userNamesMap = new HashMap<>(stateMessage.getUsersMap());
                    //remove self from list
                    // 將自己從聊天清單中移除，否則會看到自己在聊天清單上
                    userNamesMap.remove(Utils.getMemVO().getMem_id());
                    Utils.setUserNamesMap(userNamesMap);
                    if (!userId.equals(Utils.getMemVO().getMem_id())) {//通知除了自己之外的人
                        userName = stateMessage.getUsersMap().get(userId);
                        Toast.makeText(activity, userName + " 上線了", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case "close":   //when user disconnected remove from list
                    userName = Utils.getUserNamesMap().get(userId);
                    Utils.getUserNamesMap().remove(userId);
                    Utils.getUserIdsList().remove(userId);

                    Toast.makeText(activity, userName + " 離線了", Toast.LENGTH_SHORT).show();
                    break;
            }
            Log.d(TAG, "message: " + message);
            Log.d(TAG, "usersNamesMap: " + Utils.getUserNamesMap());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}
