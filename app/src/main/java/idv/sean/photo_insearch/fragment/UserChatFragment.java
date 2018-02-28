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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.activity.ChatActivity;
import idv.sean.photo_insearch.activity.MainActivity;
import idv.sean.photo_insearch.model.State;
import idv.sean.photo_insearch.util.Utils;

public class UserChatFragment extends Fragment {
    private static final String TAG = "UserChatFragment";
    private RecyclerView rvUsers;
    private LocalBroadcastManager broadcastManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        registerUserStateReceiver();

        // 初始化聊天清單
        rvUsers = view.findViewById(R.id.recyclerView_member);
        rvUsers.setLayoutManager(new LinearLayoutManager(activity));
        rvUsers.setAdapter(new UserAdapter(activity));

        return view;
    }

    // 攔截user連線或斷線的broadcast，更新RecyclerView
    private class UserStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            rvUsers.getAdapter().notifyDataSetChanged();
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
            final String userId = Utils.getUserIdsList().get(position);
            final String userName = Utils.getUserNamesMap().get(userId);

            holder.tvUser.setText(userName);
            // 點選聊天清單上的user即開啟聊天頁面
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return Utils.getUserIdsList().size();
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
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        UserStateReceiver userStateReceiver = new UserStateReceiver();
        broadcastManager.registerReceiver(userStateReceiver, openFilter);
        broadcastManager.registerReceiver(userStateReceiver, closeFilter);
    }
}
