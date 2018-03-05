package idv.sean.photo_insearch.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;
import idv.sean.photo_insearch.model.MemVO;


public class MemberDetailFragment extends Fragment {
    private RecyclerView rvMember;
    private Button btnRefresh, btnAddPoint;
    private MemVO memVO;
    private List<String[]> memberDetail = new ArrayList<>();
    private MemberAdapter memberAdapter;
    private int point;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences pref = getActivity().getSharedPreferences
                ("preference", Context.MODE_PRIVATE);
        memVO = Utils.gson.fromJson(pref.getString("memVO", ""), MemVO.class);
        point = pref.getInt("point", 0);

        View view = inflater.inflate(R.layout.fragment_member, container, false);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnAddPoint = view.findViewById(R.id.btnAddPoint);
        initButtons();
        initList(memVO, point);
        rvMember = view.findViewById(R.id.recyclerView_member);
        rvMember.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvMember.setLayoutManager(layoutManager);
        memberAdapter = new MemberAdapter(memberDetail);
        rvMember.setAdapter(memberAdapter);

        return view;
    }

    public void initList(MemVO memVO, int point) {
        memberDetail.add(new String[]{"會員帳號:", memVO.getMem_acc()});
        memberDetail.add(new String[]{"姓名:", memVO.getMem_name()});
        memberDetail.add(new String[]{"性別:", memVO.getMem_sex()});
        memberDetail.add(new String[]{"生日:", memVO.getMem_bd().toString()});
        memberDetail.add(new String[]{"MAIL:", memVO.getMem_mail()});
        memberDetail.add(new String[]{"電話:", memVO.getMem_phone()});
        memberDetail.add(new String[]{"會員等級:", memVO.getMem_level().equals("1") ? "一般會員" : "攝影師"});
        memberDetail.add(new String[]{"加入日期:", memVO.getMem_jointime().toString()});
        memberDetail.add(new String[]{"同意搜尋:", memVO.getMem_agree().equals("1") ? "是" : "否"});
        memberDetail.add(new String[]{"地址:", memVO.getMem_addr()});
        memberDetail.add(new String[]{"持有點數:", String.valueOf(point)});
    }

    public void initButtons() {
        //set refresh member data function
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String jsonIn = (String) (new TextTransferTask()
                            .execute(Utils.REFRESH_MEMBER_DATA,
                                    Utils.URL_ANDOROID_CONTROLLER, memVO.getMem_id()).get());
                    JsonObject jsonObject = Utils.gson.fromJson(jsonIn, JsonObject.class);
                    memVO = Utils.gson.fromJson(jsonObject.get("memVO").getAsString(), MemVO.class);
                    point = jsonObject.get("point").getAsInt();
                    memberDetail.clear();
                    initList(memVO, point);
                    memberAdapter.notifyDataSetChanged();

                    //set new member data to SharePreference
                    String memJson = Utils.gson.toJson(memVO);
                    SharedPreferences pref = getActivity()
                            .getSharedPreferences("preference", Context.MODE_PRIVATE);
                    pref.edit().putString("memVO", memJson).apply();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        //set add point dialog and function
        btnAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setTitle("點數加值");
                dialog.setContentView(R.layout.mem_login);

                // 透過myDialog.getWindow()取得這個對話視窗的Window物件
                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);

                WindowManager wm = getActivity().getWindowManager();
                Display d = wm.getDefaultDisplay(); // 取得螢幕寬、高用
                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
                lp.height = (int) (d.getHeight() * 0.8);
                lp.width = (int) (d.getWidth() * 0.8);
                dialogWindow.setAttributes(lp);

                final EditText cardNo, point;
                Button submit, cancel;
                cardNo = dialog.findViewById(R.id.etAcc);
                point = dialog.findViewById(R.id.etPwd);
                submit = dialog.findViewById(R.id.btnLogInSubmit);
                cancel = dialog.findViewById(R.id.btnLogInCancel);

                cardNo.setHint("請輸入信用卡卡號");
                point.setHint("請輸入要加值點數");
                cancel.setText("返回");
                submit.setText("加值");

                point.setInputType(InputType.TYPE_CLASS_NUMBER);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pointAdd = Integer.parseInt(point.getText().toString());
                        new TextTransferTask().execute
                                (Utils.INSERT_POINT, Utils.URL_ANDOROID_CONTROLLER, pointAdd);
                        cardNo.setText(null);
                        point.setText(null);
                        Toast.makeText(getContext(), "加值成功", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
        private List<String[]> memDetail;

        public MemberAdapter(List<String[]> memDetail) {
            this.memDetail = memDetail;
        }

        @Override
        public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.linearlayout_memberdetail, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
            String[] data = memDetail.get(position);
            holder.title.setText(data[0]);
            holder.data.setText(data[1]);
        }

        @Override
        public int getItemCount() {
            return memDetail.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private EditText data;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tv_memkey);
                data = itemView.findViewById(R.id.et_memvalue);
            }
        }
    }
}
