package idv.sean.photo_insearch.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.activity.ChatActivity;
import idv.sean.photo_insearch.activity.MainActivity;
import idv.sean.photo_insearch.model.State;
import idv.sean.photo_insearch.util.Utils;

public class UserChatFragment extends Fragment {
    private static final String TAG = "UserChatFragment";
    private RecyclerView rvUsers;
    private LocalBroadcastManager broadcastManager;
    private List<String> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
           @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_member, container, false);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        registerUserStateReceiver();

        rvUsers = view.findViewById(R.id.recyclerView_member);
        rvUsers.setLayoutManager(new LinearLayoutManager(activity));
        rvUsers.setAdapter(new UserAdapter(activity));

        return view;
    }


    // 攔截user連線或斷線的broadcast，並在RecyclerView呈現
    private class UserStateReceiver extends BroadcastReceiver {
        private MainActivity activity;

        public UserStateReceiver(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            State stateMessage = new Gson().fromJson(message, State.class);
            String type = stateMessage.getType();
            String user = stateMessage.getUser();
            String member = activity.getMemberName();

            switch (type) {
                case "open":    //when user connected
                    if (user.equals(member)) { //self connected
                        //get all online users
                        usersList = new ArrayList<>(stateMessage.getUsers());
                        Utils.setUsersList(usersList);
                        //remove self from list
                        // 將自己從聊天清單中移除，否則會看到自己在聊天清單上
                        usersList.remove(member);
                    } else {
                        if (!Utils.getUsersList().contains(user)) {
                            Utils.getUsersList().add(user);
                        }
                        Toast.makeText(activity, user + " 上線了", Toast.LENGTH_SHORT).show();
                    }
                    //refresh usersList
                    rvUsers.getAdapter().notifyDataSetChanged();
                    break;

                case "close":   //when user disconnected remove from list
                    Utils.getUsersList().remove(user);
                    //refresh usersList
                    rvUsers.getAdapter().notifyDataSetChanged();
                    Toast.makeText(activity, user + " 離線了", Toast.LENGTH_SHORT).show();
                    break;
            }
            Log.d(TAG, "message: " + message);
            Log.d(TAG, "usersList: " + Utils.getUsersList());
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        private MainActivity activity;

        public UserAdapter(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View v = inflater.inflate(R.layout.cardview_user, parent, false);

            return new UserViewHolder(v);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            final String user = Utils.getUsersList().get(position);
            holder.tvUser.setText(user);
            // 點選聊天清單上的user即開啟聊天頁面
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user",user);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return Utils.getUsersList().size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            private TextView tvUser;

            public UserViewHolder(View itemView) {
                super(itemView);
                tvUser = itemView.findViewById(R.id.tvUser);
            }
        }

    }

    private void registerUserStateReceiver() {
        MainActivity activity = (MainActivity) getActivity();
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        UserStateReceiver userStateReceiver = new UserStateReceiver(activity);
        broadcastManager.registerReceiver(userStateReceiver, openFilter);
        broadcastManager.registerReceiver(userStateReceiver, closeFilter);
    }
}
