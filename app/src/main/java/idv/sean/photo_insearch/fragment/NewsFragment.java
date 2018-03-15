package idv.sean.photo_insearch.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.NewsVO;
import idv.sean.photo_insearch.util.Utils;

public class NewsFragment extends Fragment {
    RecyclerView rvNews;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_only, container, false);
        TextView tv = view.findViewById(R.id.tvTitle_recyclerView_only);
        tv.setHeight(0);
        rvNews = view.findViewById(R.id.onlyRecyclerView);
        //設定每個List是否為固定尺寸
        rvNews.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvNews.setLayoutManager(layoutManager);
        new NewsDownloadTask().execute(Utils.URL_ANDOROID_CONTROLLER);

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
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog myDialog = new Dialog(getContext());
                    myDialog.setTitle(news.getNews_type().equals("INN") ? "站內消息" : "攝影新聞");
                    myDialog.setContentView(R.layout.dialog_showproduct);

                    // 透過myDialog.getWindow()取得這個對話視窗的Window物件
                    Window dialogWindow = myDialog.getWindow();
                    dialogWindow.setGravity(Gravity.CENTER);

                    WindowManager wm = getActivity().getWindowManager();
                    Display d = wm.getDefaultDisplay(); // 取得螢幕寬、高用
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
                    lp.height = (int) (d.getHeight() * 0.8);
                    lp.width = (int) (d.getWidth() * 0.9);
                    dialogWindow.setAttributes(lp);

                    TextView newsTitle = myDialog.findViewById(R.id.tvShowProductTitle);
                    newsTitle.setText(news.getTitle());
                    newsTitle.setTextSize(20);
                    TextView newsDate = myDialog.findViewById(R.id.tvShowProductPrice);
                    newsDate.setText(news.getNews_date().toString());
                    TextView newsContent = myDialog.findViewById(R.id.tvShowProductContent);
                    newsContent.setText(news.getArticle());
                    ImageView newsPicture = myDialog.findViewById(R.id.ivShowProduct);
                    newsPicture.setImageResource(R.drawable.p08);
                    if (news.getNews_pic() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray
                                (news.getNews_pic(), 0, news.getNews_pic().length);
                        newsPicture.setImageBitmap(bitmap);
                    }

                    Button btn = myDialog.findViewById(R.id.btnCart);
                    btn.setText("返回");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.cancel();
                        }
                    });

                    myDialog.show();
                }
            });
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

    private class NewsDownloadTask extends AsyncTask<String, Void, List<NewsVO>> {
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected List<NewsVO> doInBackground(String... strings) {
            String url = strings[0];
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "news_all_download");
            List<NewsVO> newsList = null;
            try {
                String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                Type type = new TypeToken<List<NewsVO>>() {
                }.getType();
                newsList = Utils.gson.fromJson(jsonIn, type);
                for (NewsVO news : newsList) {              //decode picture by Base64
                    if (news.getPicBase64() != null) {      //if picture exist
                        byte[] pic = Base64.decode(news.getPicBase64(), Base64.DEFAULT);
                        news.setPicBase64(null);
                        news.setNews_pic(pic);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsList;
        }

        @Override
        protected void onPostExecute(List<NewsVO> newsList) {
            rvNews.setAdapter(new NewsAdapter(newsList));
            dialog.cancel();
        }
    }
}
