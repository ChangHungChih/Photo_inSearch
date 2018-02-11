package idv.sean.photo_insearch.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Util;
import idv.sean.photo_insearch.vo.CasesVO;

public class CaseFragment extends Fragment{
    RecyclerView rvCase;
    List<CasesVO> casesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize casesList
        TextTransferTask textTransferTask =new TextTransferTask();
        String jsonIn = null;
        try {
            jsonIn = (String) textTransferTask
                    .execute(Util.CASES_ALL_DOWNLOAD,Util.URL_ANDOROID_CONTROLLER).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Type type = new TypeToken<List<CasesVO>>(){}.getType();
        casesList = Util.gson.fromJson(jsonIn,type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        rvCase = view.findViewById(R.id.recyclerView_photo);
        rvCase.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        rvCase.setLayoutManager(layoutManager);
        rvCase.setAdapter(new CasesAdapter(casesList));

        return view;
    }

    private class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.ViewHolder>{
        List<CasesVO> casesList;

        public CasesAdapter(List<CasesVO> caseList) {
            this.casesList = caseList;
        }

        @Override
        public CasesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.gridview_cases,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CasesAdapter.ViewHolder holder, int position) {
            final CasesVO caseVO = casesList.get(position);
            holder.tvTitle.setText(caseVO.getCase_title());
            holder.tvDate.setText(caseVO.getCase_create_date().toString());
            holder.tvContent.setText(caseVO.getCase_content());
            holder.tvBuilder.setText(caseVO.getMem_id());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),caseVO.getCase_title(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return casesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvTitle;
            private TextView tvDate;
            private TextView tvContent;
            private TextView tvBuilder;
            private LinearLayout linearLayout;
            public ViewHolder(View view) {
                super(view);

                tvTitle = view.findViewById(R.id.tvCaseTitle);
                tvDate = view.findViewById(R.id.tvCaseDate);
                tvContent = view.findViewById(R.id.tvCaseContent);
                tvBuilder = view.findViewById(R.id.tvCaseBuilder);
                linearLayout = view.findViewById(R.id.linearlayout_case);
            }
        }
    }

}
