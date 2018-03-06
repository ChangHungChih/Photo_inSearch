package idv.sean.photo_insearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.fragment.MyCaseTypeFragment;
import idv.sean.photo_insearch.fragment.NoDataFragment;
import idv.sean.photo_insearch.model.CasesVO;
import idv.sean.photo_insearch.model.MemVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class ShowCaseActivity extends AppCompatActivity {
    private RecyclerView rvCases;
    private List<CasesVO> casesList;
    private List<MemVO> memList;
    private TextView tvCaseType;
    private int caseState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_only);

        rvCases = findViewById(R.id.onlyRecyclerView);
        rvCases.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCases.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initList();
        rvCases.setAdapter(new CasesAdapter(casesList));
    }

    public void initList() {
        //initialize casesList
        caseState = getIntent().getIntExtra("case", 0);
        tvCaseType = findViewById(R.id.tvTitle_recyclerView_only);
        switch (caseState) {
            case MyCaseTypeFragment.POSTED_CASES:
                getCases(Utils.GET_POSTED_CASES);
                tvCaseType.setText(R.string.casePosted);
                break;
            case MyCaseTypeFragment.PROCEEDING_CASES:
                getCases(Utils.GET_PROCEEDING_CASES);
                tvCaseType.setText(R.string.caseProceeding);
                break;
            case MyCaseTypeFragment.FINISHED_CASES:
                getCases(Utils.GET_FINISHED_CASES);
                tvCaseType.setText(R.string.caseFinished);
                break;
            case MyCaseTypeFragment.INVITED_CASES:
                getCases(Utils.GET_INVITED_CASES);
                tvCaseType.setText(R.string.caseInvited);
                break;
            case MyCaseTypeFragment.CLOSED_CASES:
                getCases(Utils.GET_CLOSED_CASES);
                tvCaseType.setText(R.string.caseClosed);
            default:
                break;
        }
    }

    public void getCases(int action) {
        TextTransferTask textTransferTask = new TextTransferTask();
        String jsonIn = null;
        try {
            jsonIn = (String) textTransferTask
                    .execute(action, Utils.URL_ANDOROID_CONTROLLER).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Type typeCases = new TypeToken<List<CasesVO>>() {
        }.getType();
        casesList = Utils.gson.fromJson(jsonIn, typeCases);
        memList = Utils.getMemList();

        if(casesList.isEmpty()){
            Fragment noDataFragment = new NoDataFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.linearLayout_onlyRecyclerView, noDataFragment).commit();
        }
    }

    private class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.ViewHolder> {
        List<CasesVO> casesList;

        public CasesAdapter(List<CasesVO> caseList) {
            this.casesList = caseList;
        }

        @Override
        public CasesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ShowCaseActivity.this)
                    .inflate(R.layout.gridview_cases, parent, false);
            return new CasesAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CasesAdapter.ViewHolder holder, int position) {
            final CasesVO caseVO = casesList.get(position);
            MemVO memVO1 = null;
            MemVO memVO2 = null;
            for (MemVO mem : memList) {//get case builder and solver
                if (caseVO.getMem_id().equals(mem.getMem_id()))
                    memVO1 = mem;
                if (mem.getMem_id().equals(caseVO.getMem_id2()))
                    memVO2 = mem;
            }
            final MemVO mem1 = memVO1;
            final MemVO mem2 = memVO2;
            holder.tvTitle.setText(caseVO.getCase_title());
            holder.tvDate.setText(caseVO.getCase_create_date().toString());
            holder.tvBuilder.setText("發案者: " + memVO1.getMem_name());
            //set image from drawable dynamically
            String casePhoto = Utils.photoType.get(caseVO.getCase_photo_pic());
            String idName = "p" + casePhoto;
            int resId = getResources().getIdentifier
                    (idName, "drawable", getPackageName());
            holder.ivCasePicture.setImageResource(resId);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent
                            (ShowCaseActivity.this, ShowCaseDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("caseVO", caseVO);
                    bundle.putSerializable("mem1", mem1);
                    bundle.putSerializable("mem2", mem2);
                    bundle.putInt("caseState", caseState);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return casesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvDate, tvBuilder;
            private ImageView ivCasePicture;

            public ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tvCaseTitle);
                tvDate = view.findViewById(R.id.tvCaseDate);
                tvBuilder = view.findViewById(R.id.tvCaseBuilder);
                ivCasePicture = view.findViewById(R.id.ivCasePicture);
            }
        }
    }
}
