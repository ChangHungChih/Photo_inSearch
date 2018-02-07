package idv.sean.photo_insearch.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.activity.ShowPhotoActivity;
import idv.sean.photo_insearch.util.PhotoDownloadTask;
import idv.sean.photo_insearch.util.Util;
import idv.sean.photo_insearch.vo.Photo;
import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.vo.PhotoVO;

public class PhotoFragment extends Fragment{
    private RecyclerView rvPhoto;


    public PhotoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        rvPhoto = (RecyclerView)view.findViewById(R.id.recyclerView_photo);

        //設定每個List是否為固定尺寸
        rvPhoto.setHasFixedSize(true);
        //產生一個LinearLayoutManger
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //設定LayoutManager
        rvPhoto.setLayoutManager(layoutManager);

        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo(R.drawable.spring,"風景"));
        photoList.add(new Photo(R.drawable.house,"實體商品"));
        photoList.add(new Photo(R.drawable.autumn1,"接案區"));


        rvPhoto.setAdapter(new PhotoAdapter(photoList));

        return view;
    }



    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
        private List<Photo> photoList;

        public PhotoAdapter(List<Photo> photoList) {
            this.photoList = photoList;
        }


        @Override
        public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_photo,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoAdapter.ViewHolder holder, final int position) {
            final Photo photo = photoList.get(position);
            holder.tvTitle.setText(photo.getTitle());
            holder.ivPicture.setImageResource(photo.getPhoto());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),photo.getTitle()+" clicked",Toast.LENGTH_SHORT).show();
//                    if(position == 0){
//                        PhotoDownloadTask photoDownloadTask;
//                        Intent intent = new Intent();
//                        intent.setClass(getContext(), ShowPhotoActivity.class);
//                        Bundle bundle = new Bundle();
//                        photoDownloadTask = new PhotoDownloadTask();
//
//                        PhotoVO photoVO = null;
//
//                        try {
//                            photoVO = photoDownloadTask.execute(Util.URL_ANDOROID_CONTROLLER).get();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                        if(photoVO == null){
//                            Toast.makeText(getContext(),"photo is null",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        bundle.putSerializable("photo",photoVO);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView ivPicture;
            private TextView tvTitle;

            public ViewHolder(View view) {
                super(view);
                ivPicture = (ImageView)view.findViewById(R.id.ivPicture);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            }
        }

    }

}
