package idv.sean.photo_insearch.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.TextUploadTask;
import idv.sean.photo_insearch.util.Util;
import idv.sean.photo_insearch.vo.PhotoVO;


public class ShowPhotoActivity extends AppCompatActivity {
    private ImageView ivShow;
    private TextView tvShow;
    private EditText etUpload;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showphoto);
        ivShow = findViewById(R.id.ivShow);
        tvShow = findViewById(R.id.tvName);
        etUpload = findViewById(R.id.etContent);
        Bundle bundle = this.getIntent().getExtras();
        PhotoVO photoVO = (PhotoVO) bundle.getSerializable("photo");

        tvShow.setText(photoVO.getPhoto_id());

        byte[] photo = photoVO.getPhoto_pic();
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo,0,photo.length);
        ivShow.setImageBitmap(bitmap);
    }

    public void onSubmitClick(View v){
        String uploadText = etUpload.getText().toString();
        if(Util.networkConnected(this)){
           new TextUploadTask().execute(Util.URL_ANDOROID_CONTROLLER, uploadText);
        }else{
            Toast.makeText(this,"Network is not connected...",Toast.LENGTH_SHORT).show();
        }

    }
}
