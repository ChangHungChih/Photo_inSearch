package idv.sean.photo_insearch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.CasesVO;
import idv.sean.photo_insearch.model.MemVO;
import idv.sean.photo_insearch.util.Utils;

public class ShowCaseActivity extends AppCompatActivity {
    TextView tvTitle, tvDate, tvBuilder, tvContent;
    ImageView ivPicture;
    Button btnSubmit, btnCancel;
    CasesVO caseVO;
    MemVO memBuilder, memSolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);

        findViews();
        setInfo();
        initBtn();
    }

    public void findViews() {
        tvTitle = findViewById(R.id.tvCaseTitleShow);
        tvBuilder = findViewById(R.id.tvCaseBuilderShow);
        tvDate = findViewById(R.id.tvCaseDateShow);
        tvContent = findViewById(R.id.tvCaseContent);
        ivPicture = findViewById(R.id.ivCasePictureShow);
        btnSubmit = findViewById(R.id.btnCaseSubmit);
        btnCancel = findViewById(R.id.btnCaseCancel);
    }

    public void setInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caseVO = (CasesVO) bundle.getSerializable("caseVO");
        memBuilder = (MemVO) bundle.getSerializable("mem1");
        memSolver = (MemVO) bundle.getSerializable("mem2");
        tvTitle.setText(caseVO.getCase_title());
        tvBuilder.setText("發案者: " + memBuilder.getMem_name());
        tvDate.setText(caseVO.getCase_create_date().toString());
        tvContent.setText(caseVO.getCase_content());
        //set image from drawable dynamically
        String casePhoto = Utils.photoType.get(caseVO.getCase_photo_pic());
        String idName = "p" + casePhoto;
        int resId = getResources().getIdentifier(idName, "drawable", this.getPackageName());
        ivPicture.setImageResource(resId);
    }

    public void initBtn() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
