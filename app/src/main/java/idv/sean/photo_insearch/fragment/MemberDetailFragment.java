package idv.sean.photo_insearch.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.Utils;
import idv.sean.photo_insearch.model.MemVO;


public class MemberDetailFragment extends Fragment {
    private RecyclerView rvMember;
    private MemVO memVO;
    private List<String[]> memberDetail;
    private MemberAdapter memberAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences pref = getActivity().getSharedPreferences
                ("preference", Context.MODE_PRIVATE);
        memVO = Utils.gson.fromJson(pref.getString("memVO", ""), MemVO.class);
        memberDetail = new ArrayList<>();
        memberDetail.add(new String[]{"會員帳號:", memVO.getMem_acc()});
        memberDetail.add(new String[]{"姓名:", memVO.getMem_name()});
        memberDetail.add(new String[]{"性別:", memVO.getMem_sex()});
        memberDetail.add(new String[]{"生日:", memVO.getMem_bd().toString()});
        memberDetail.add(new String[]{"MAIL:", memVO.getMem_mail()});
        memberDetail.add(new String[]{"電話:", memVO.getMem_phone()});
        memberDetail.add(new String[]{"會員等級:", memVO.getMem_level().equals("1")? "一般會員" : "攝影師"});
        memberDetail.add(new String[]{"加入日期:", memVO.getMem_jointime().toString()});
        memberDetail.add(new String[]{"同意搜尋:", memVO.getMem_agree().equals("1")? "是" : "否"});
        memberDetail.add(new String[]{"地址:", memVO.getMem_addr()});

        View view = inflater.inflate(R.layout.fragment_member, container, false);

        rvMember = view.findViewById(R.id.recyclerView_member);
        rvMember.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvMember.setLayoutManager(layoutManager);
        memberAdapter = new MemberAdapter(memberDetail);
        rvMember.setAdapter(memberAdapter);

        return view;
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
