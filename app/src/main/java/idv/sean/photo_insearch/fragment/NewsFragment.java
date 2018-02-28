package idv.sean.photo_insearch.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.NewsVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class NewsFragment extends Fragment {
    RecyclerView rvNews;
    List<NewsVO> newsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextTransferTask task = new TextTransferTask();
        String jsonIn = null;
        try {
            jsonIn = (String) task.execute
                    (Utils.NEWS_ALL_DOWNLOAD, Utils.URL_ANDOROID_CONTROLLER).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Type type = new TypeToken<List<NewsVO>>() {
        }.getType();
        newsList = Utils.gson.fromJson(jsonIn, type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        rvNews = view.findViewById(R.id.recyclerView_member);
        //設定每個List是否為固定尺寸
        rvNews.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvNews.setLayoutManager(layoutManager);
        rvNews.setAdapter(new NewsAdapter(newsList));
        return view;
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<NewsVO> newsList;

        public NewsAdapter(List<NewsVO> newsList) {
            this.newsList = newsList;
        }

        @Override
        public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.cardview_news, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            final NewsVO news = newsList.get(position);
            holder.tvTitle.setText(news.getTitle());
            holder.tvDate.setText(news.getNews_date().toString());
            if (news.getNews_pic() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray
                        (news.getNews_pic(), 0, news.getNews_pic().length);
                holder.ivPicture.setImageBitmap(bitmap);
                holder.ivPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), news.getTitle() + "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDate;
            ImageView ivPicture;

            public ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tvNewsTitle);
                tvDate = view.findViewById(R.id.tvNewsDate);
                ivPicture = view.findViewById(R.id.ivNewsPicture);
            }
        }
    }
}
