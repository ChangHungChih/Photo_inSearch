package idv.sean.photo_insearch.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.activity.ShowCaseDetailActivity;
import idv.sean.photo_insearch.model.MemVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;
import idv.sean.photo_insearch.model.CasesVO;

public class CaseFragment extends Fragment {
    RecyclerView rvCase;
    List<CasesVO> casesList;
    List<MemVO> memList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize casesList
        TextTransferTask textTransferTask = new TextTransferTask();
        String jsonIn = null;
        String jsonCases = null;
        String jsonMem = null;
        try {
            jsonIn = (String) textTransferTask
                    .execute(Utils.CASES_ALL_DOWNLOAD, Utils.URL_ANDOROID_CONTROLLER).get();
            JsonObject jsonObject = Utils.gson.fromJson(jsonIn, JsonObject.class);
            jsonCases = jsonObject.get("caseList").getAsString();
            jsonMem = jsonObject.get("memList").getAsString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Type typeCases = new TypeToken<List<CasesVO>>() {
        }.getType();
        casesList = Utils.gson.fromJson(jsonCases, typeCases);
        Type typeMem = new TypeToken<List<MemVO>>() {
        }.getType();
        memList = Utils.gson.fromJson(jsonMem, typeMem);
        Utils.setMemList(memList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        rvCase = view.findViewById(R.id.recyclerView_photo);
        rvCase.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvCase.setLayoutManager(layoutManager);
        rvCase.setAdapter(new CasesAdapter(casesList));

        return view;
    }

    private class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.ViewHolder> {
        List<CasesVO> casesList;

        public CasesAdapter(List<CasesVO> caseList) {
            this.casesList = caseList;
        }

        @Override
        public CasesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.gridview_cases, parent, false);
            return new ViewHolder(view);
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
                    (idName, "drawable", getContext().getPackageName());
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
//            holder.ivCasePicture.setImageBitmap(bitmap);
            holder.ivCasePicture.setImageResource(resId);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ShowCaseDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("caseVO", caseVO);
                    bundle.putSerializable("mem1", mem1);
                    bundle.putSerializable("mem2", mem2);
                    bundle.putInt("caseState", MyCaseTypeFragment.POSTED_CASES);
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
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
