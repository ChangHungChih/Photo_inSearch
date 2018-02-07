package idv.sean.photo_insearch.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.vo.Case;

public class CaseFragment extends Fragment{
    RecyclerView rvCase;
    List<Case> caseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);


        return view;
    }

    private class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ViewHolder>{
        List<Case> caseList;

        public CaseAdapter(List<Case> caseList) {
            this.caseList = caseList;
        }

        @Override
        public CaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(CaseAdapter.ViewHolder holder, int position) {



        }

        @Override
        public int getItemCount() {
            return caseList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View view) {
                super(view);
            }
        }
    }

}
