package idv.sean.photo_insearch.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.ProductVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;
import idv.sean.photo_insearch.model.PhotoVO;


public class ShowPhotoActivity extends AppCompatActivity {
    private static final String TAG = "ShawPhotoActivity";
    private RecyclerView rvPhotos;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showphoto);

        Bundle bundle = getIntent().getExtras();
        String typeId = bundle.getString("typeId");
        String title = bundle.getString("title");
        TextView tvTitle = findViewById(R.id.tvPhotoType);
        tvTitle.setText(title);
        rvPhotos = findViewById(R.id.rvPhotoShow);
        rvPhotos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPhotos.setLayoutManager(layoutManager);
        new PhotoDownloadTask().execute(Utils.URL_ANDOROID_CONTROLLER, typeId);
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
        List<PhotoVO> photoList;

        public PhotoAdapter(List<PhotoVO> photoList) {
            this.photoList = photoList;
        }

        @Override
        public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ShowPhotoActivity.this);
            View view = inflater.inflate(R.layout.gridview_photo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoAdapter.ViewHolder holder, int position) {
            final PhotoVO photo = photoList.get(position);
            Bitmap bitmap = BitmapFactory.decodeByteArray
                    (photo.getPhoto_pic(), 0, photo.getPhoto_pic().length);
            holder.ivPhoto.setImageBitmap(bitmap);

        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPhoto;

            public ViewHolder(View view) {
                super(view);
                ivPhoto = view.findViewById(R.id.ivPhotoShow);
            }
        }
    }

    private class PhotoDownloadTask extends AsyncTask<Object, Void, List<PhotoVO>> {
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ShowPhotoActivity.this);
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Override
        protected List<PhotoVO> doInBackground(Object... objects) {
            String url = (String) objects[0];
            String typeId = (String) objects[1];
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "photos_download");
            jsonObject.addProperty("typeId", typeId);
            jsonObject.addProperty("imageSize", 400);
            List<PhotoVO> photoList = null;
            try {
                String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                Type type = new TypeToken<List<PhotoVO>>() {
                }.getType();
                photoList = Utils.gson.fromJson(jsonIn, type);
                //decode picture by Base64
                for(PhotoVO photo: photoList){
                    byte[] pic = Base64.decode(photo.getPicBase64(), Base64.DEFAULT);
                    photo.setPicBase64(null);
                    photo.setPhoto_pic(pic);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return photoList;
        }

        @Override
        protected void onPostExecute(List<PhotoVO> photoList) {
            rvPhotos.setAdapter(new PhotoAdapter(photoList));
            dialog.cancel();
        }
    }
}
