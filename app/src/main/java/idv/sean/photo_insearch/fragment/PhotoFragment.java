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

import idv.sean.photo_insearch.activity.ShowPhotoActivity;
import idv.sean.photo_insearch.model.Photo;
import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.Utils;

public class PhotoFragment extends Fragment{
    private RecyclerView rvPhoto;
    private List<Photo> photoList;
    private PhotoAdapter photoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoList = new ArrayList<>();
        photoList.add(new Photo(R.drawable.p01,"風景"));
        photoList.add(new Photo(R.drawable.p02,"人像"));
        photoList.add(new Photo(R.drawable.p03,"紀實"));
        photoList.add(new Photo(R.drawable.p04,"商品"));
        photoList.add(new Photo(R.drawable.p05,"生態"));
        photoList.add(new Photo(R.drawable.p06,"概念"));
        photoList.add(new Photo(R.drawable.p07,"藝術"));
        photoList.add(new Photo(R.drawable.p08,"新聞"));
        photoList.add(new Photo(R.drawable.p09,"婚紗"));
        photoList.add(new Photo(R.drawable.p10,"時裝"));
        photoList.add(new Photo(R.drawable.p11,"美食"));
        photoList.add(new Photo(R.drawable.p12,"飛機"));
        photoList.add(new Photo(R.drawable.p13,"鐵道"));
        photoList.add(new Photo(R.drawable.p14,"建築"));
        photoList.add(new Photo(R.drawable.p15,"廢墟"));
        photoList.add(new Photo(R.drawable.p16,"黑白"));
        photoList.add(new Photo(R.drawable.p17,"微距"));
        photoList.add(new Photo(R.drawable.p18,"家庭"));
        photoList.add(new Photo(R.drawable.p19,"天文"));
        photoList.add(new Photo(R.drawable.p20,"空中"));
        photoList.add(new Photo(R.drawable.p21,"水中"));
        photoList.add(new Photo(R.drawable.p22,"動物"));
        photoList.add(new Photo(R.drawable.p23,"其他"));

        photoAdapter = new PhotoAdapter(photoList);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        rvPhoto = (RecyclerView)view.findViewById(R.id.recyclerView_photo);

        //設定每個List是否為固定尺寸
        rvPhoto.setHasFixedSize(true);
        //產生一個LinearLayoutManger
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //設定LayoutManager
        rvPhoto.setLayoutManager(layoutManager);
        rvPhoto.setAdapter(photoAdapter);

        return view;
    }



    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
        private List<Photo> photoList;

        public PhotoAdapter(List<Photo> photoList) {
            this.photoList = photoList;
        }


        @Override
        public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.cardview_photo,parent,false);
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
                    Intent intent = new Intent(getContext(), ShowPhotoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("typeId", "PTT0" + Utils.photoType.get(photo.getTitle()));
                    bundle.putString("title", photo.getTitle());
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
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
