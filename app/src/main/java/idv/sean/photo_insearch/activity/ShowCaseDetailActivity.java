package idv.sean.photo_insearch.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.fragment.MyCaseTypeFragment;
import idv.sean.photo_insearch.model.CasesVO;
import idv.sean.photo_insearch.model.Catch_listVO;
import idv.sean.photo_insearch.model.MemVO;
import idv.sean.photo_insearch.util.Contents;
import idv.sean.photo_insearch.util.QRCodeEncoder;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class ShowCaseDetailActivity extends AppCompatActivity {
    private static final String TAG = "ShowCaseActivity";
    private static final String PACKAGE = "com.google.zxing.client.android";
    private TextView tvTitle, tvDate, tvBuilder, tvApplicant,
            tvContent, tvCommentBuilder, tvCommentApplicant;
    private ImageView ivPicture;
    private Button btnSubmit, btnCancel;
    private LinearLayout llComment;
    private RatingBar rbBuilder, rbApplicant;
    private CasesVO caseVO;
    private MemVO memBuilder, memApplicant, userData;
    private int caseState;
    private boolean idCheck = false;
    private String selectedMemId;
    private double ratePoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcasedetail);

        caseState = getIntent().getExtras().getInt("caseState");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        caseVO = (CasesVO) bundle.getSerializable("caseVO");
        memBuilder = (MemVO) bundle.getSerializable("mem1");
        memApplicant = (MemVO) bundle.getSerializable("mem2");
        findViews();
        setInfo();
    }

    public void findViews() {
        tvTitle = findViewById(R.id.tvCaseTitleShow);
        tvBuilder = findViewById(R.id.tvCaseBuilderShow);
        tvApplicant = findViewById(R.id.tvCaseApplicant);
        tvDate = findViewById(R.id.tvCaseDateShow);
        tvContent = findViewById(R.id.tvCaseContent);
        ivPicture = findViewById(R.id.ivCasePictureShow);
        btnSubmit = findViewById(R.id.btnCaseSubmit);
        btnCancel = findViewById(R.id.btnCaseCancel);
    }

    public void setInfo() {
        tvTitle.setText(caseVO.getCase_title());
        tvBuilder.setText("發案者: " + memBuilder.getMem_name());
        if (memApplicant != null) {
            tvApplicant.setText("接案者: " + memApplicant.getMem_name());
        } else {
            tvApplicant.setVisibility(View.INVISIBLE);
        }
        tvDate.setText(caseVO.getCase_create_date().toString());
        tvContent.setText(caseVO.getCase_content());
        //set image from drawable dynamically
        String casePhoto = Utils.photoType.get(caseVO.getCase_photo_pic());
        String idName = "p" + casePhoto;
        int resId = getResources().getIdentifier(idName, "drawable", this.getPackageName());
        ivPicture.setImageResource(resId);
        initBtn();
    }

    public void initBtn() {
        userData = Utils.getMemVO();
        switch (caseState) {
            case MyCaseTypeFragment.POSTED_CASES:
                btnPostedCases();
                break;
            case MyCaseTypeFragment.PROCEEDING_CASES:
                btnProceedingCases();
                break;
            case MyCaseTypeFragment.FINISHED_CASES:
                btnFinishedCases();
                break;
            case MyCaseTypeFragment.INVITED_CASES:
                btnInvitedCases();
                break;
            case MyCaseTypeFragment.CLOSED_CASES:
            default:
                btnSubmit.setVisibility(View.INVISIBLE);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                break;
        }
    }

    public void btnPostedCases() {
        Spinner spinner = findViewById(R.id.spinner);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if ("CT1".equals(caseVO.getCase_type())) {  //caseState = CT1 (public)
            if (userData == null) {                                 //訪客身分  no member
                btnSubmit.setVisibility(View.INVISIBLE);
            } else if (userData.getMem_id().equals(caseVO.getMem_id())) {  //發案者身分 is case Builder
                try {
                    String jsonApplicants = (String) (new TextTransferTask()
                            .execute(Utils.GET_APPLICANTS, Utils.URL_ANDOROID_CONTROLLER,
                                    caseVO.getCase_id()).get());
                    Type type = new TypeToken<List<Catch_listVO>>() {
                    }.getType();
                    final List<Catch_listVO> catchList = Utils.gson.fromJson(jsonApplicants, type);
                    spinner.setVisibility(View.VISIBLE);
                    String[] applicants = new String[catchList.size() + 1];
                    applicants[0] = "請選擇";
                    final List<MemVO> memList = Utils.getMemList();
                    for (int i = 0; i < applicants.length - 1; i++) {
                        String memId = catchList.get(i).getMem_id2();
                        for (MemVO mem : memList) {
                            if (memId.equals(mem.getMem_id())) {
                                applicants[i + 1] = mem.getMem_name();
                            }
                        }
                    }
                    //set applicants spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (this, android.R.layout.simple_spinner_item, applicants);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    // 在執行setOnItemSelectedListener()之前先呼叫setSelection(position, animate)
                    // 可避免一開始就執行OnItemSelectedListener.onItemSelected()
                    spinner.setSelection(0, true);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            String selectedId = catchList.get(position - 1).getMem_id2();
                            for (MemVO mem : memList) {
                                if (selectedId.equals(mem.getMem_id())) {
                                    selectedMemId = mem.getMem_id();
                                }
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });

                    btnSubmit.setText("確定成案");
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (selectedMemId != null) {
                                new TextTransferTask().execute
                                        (Utils.CHOOSE_MEMBER_TO_CASE, Utils.URL_ANDOROID_CONTROLLER,
                                                caseVO.getCase_id(), selectedMemId);
                                finish();
                            } else {
                                Toast.makeText(ShowCaseDetailActivity.this,
                                        "請選擇攝影師", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else if (userData.getMem_level().equals("2")) {   //非發案者，但為攝影師身分  member level2
                btnSubmit.setText("申請接案");
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String jsonIn = (String) new TextTransferTask().execute
                                    (Utils.APPLY_CASE, Utils.URL_ANDOROID_CONTROLLER,
                                            caseVO.getCase_id(), caseVO.getMem_id(),
                                            userData.getMem_id()).get();
                            if (jsonIn.isEmpty()) {
                                Toast.makeText(ShowCaseDetailActivity.this,
                                        "已經申請接案", Toast.LENGTH_SHORT).show();
                                btnSubmit.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(ShowCaseDetailActivity.this,
                                        "已經申請接案", Toast.LENGTH_SHORT).show();
                                btnSubmit.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {                                          //非發案者，一般會員  member level1
                btnSubmit.setVisibility(View.INVISIBLE);
            }
        } else {                                             //caseState = CT2 (private)
            btnSubmit.setVisibility(View.INVISIBLE);
        }
    }

    public void btnProceedingCases() {
        String caseState = null;
        if (userData.getMem_id().equals(memBuilder.getMem_id())) {
            caseState = "CS6";
        } else {
            caseState = "CS7";
        }
        final String state = caseState;

        //set cancel button
        btnCancel.setText("取消案件");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TextTransferTask().execute(Utils.CANCEL_CASE,
                        Utils.URL_ANDOROID_CONTROLLER, caseVO.getCase_id(), state);
                finish();
            }
        });

        //set submit button
        if (userData.getMem_id().equals(memBuilder.getMem_id())) {
            btnSubmit.setText("身分確認");
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (idCheck) {        //id checked
                        new TextTransferTask().execute(Utils.FINISH_CASE,
                                Utils.URL_ANDOROID_CONTROLLER, caseVO.getCase_id());
                    } else {             //id not checked
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        try {
                            startActivityForResult(intent, 0);
                        }
                        // 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
                        catch (ActivityNotFoundException ex) {
                            showDownloadDialog();
                        }
                    }
                }
            });
        } else {
            btnSubmit.setText("產生行動條碼");
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QRCodeEncoder encoder =
                            new QRCodeEncoder(userData.getMem_id(), null,
                                    Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                                    getDimension());
                    try {
                        Bitmap bitmap = encoder.encodeAsBitmap();
                        showQRCode(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void btnFinishedCases() {
        tvTitle.setText(caseVO.getCase_title() + "(結案)");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llComment = findViewById(R.id.llComments);
        llComment.setVisibility(View.VISIBLE);
        rbBuilder = findViewById(R.id.ratingBarBuilder);
        rbApplicant = findViewById(R.id.ratingBarApplicant);
        tvCommentBuilder = findViewById(R.id.tvCommentBuilder);
        tvCommentApplicant = findViewById(R.id.tvCommentApplicant);
        if (caseVO.getComment1() != null || caseVO.getScore1() > 0) {
            //set comment button invisible
            if (userData.getMem_id().equals(memBuilder.getMem_id())) {
                btnSubmit.setVisibility(View.INVISIBLE);
            }
            rbBuilder.setVisibility(View.VISIBLE);
            rbBuilder.setRating(caseVO.getScore1().floatValue());
            tvCommentBuilder.setText(caseVO.getComment1());
        } else {//enable comment button
            if (userData.getMem_id().equals(memBuilder.getMem_id())) {
                btnSubmit.setText("評價");
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCommentDialog();
                    }
                });
            }
            tvCommentBuilder.setText("尚未評價");
            rbBuilder.setVisibility(View.GONE);
        }

        if (caseVO.getComment2() != null || caseVO.getScore2() != 0) {
            //set comment button invisible
            if (userData.getMem_id().equals(memApplicant.getMem_id())) {
                btnSubmit.setVisibility(View.INVISIBLE);
            }
            rbApplicant.setVisibility(View.VISIBLE);
            rbApplicant.setRating(caseVO.getScore2().floatValue());
            tvCommentApplicant.setText(caseVO.getComment2());
        } else {//enable comment button
            if (userData.getMem_id().equals(memApplicant.getMem_id())) {
                btnSubmit.setText("評價");
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCommentDialog();
                    }
                });
            }
            tvCommentApplicant.setText("尚未評價");
            rbApplicant.setVisibility(View.GONE);
        }
    }

    public void btnInvitedCases() {
        btnSubmit.setText("接受邀請");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TextTransferTask().execute(Utils.ACCEPT_CASE,
                        Utils.URL_ANDOROID_CONTROLLER, caseVO.getCase_id());
                finish();
            }
        });

        btnCancel.setText("拒絕邀請");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TextTransferTask().execute(Utils.REJECT_CASE,
                        Utils.URL_ANDOROID_CONTROLLER, caseVO.getCase_id());
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra("SCAN_RESULT");
                if (content.equals(memApplicant.getMem_id())) {
                    Toast.makeText(this, "身分確認成功", Toast.LENGTH_SHORT).show();
                    btnSubmit.setText("案件完成");
                    idCheck = true;
                } else {
                    Toast.makeText(this, "非接案者", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private int getDimension() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        // API 13列為deprecated，但為了支援舊版手機仍採用
        int width = display.getWidth();
        int height = display.getHeight();

        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;

        // API 13開始支援
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension / 2;
        return smallerDimension;
    }

    private void showQRCode(Bitmap QRCode) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("行動條碼");
        dialog.setContentView(R.layout.dialog_showproduct);
        // 透過myDialog.getWindow()取得這個對話視窗的Window物件
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        lp.height = (int) (d.getWidth() * 0.8);
        lp.width = (int) (d.getWidth() * 0.8);
        dialogWindow.setAttributes(lp);

        TextView name = dialog.findViewById(R.id.tvShowProductTitle);
        name.setText("您的行動條碼");
        TextView price = dialog.findViewById(R.id.tvShowProductPrice);
        price.setVisibility(View.INVISIBLE);
        TextView content = dialog.findViewById(R.id.tvShowProductContent);
        content.setVisibility(View.INVISIBLE);
        ImageView ivProd = dialog.findViewById(R.id.ivShowProduct);
        ivProd.setImageBitmap(QRCode);
        Button btn = dialog.findViewById(R.id.btnCart);
        btn.setText("返回");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle("No Barcode Scanner Found");
        downloadDialog.setMessage("Please download and install Barcode Scanner!");
        downloadDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }

    private void showCommentDialog() {
        final Dialog myDialog = new Dialog(this);
        myDialog.setTitle("評價");
        myDialog.setContentView(R.layout.dialog_rate_and_comment);

        // 透過myDialog.getWindow()取得這個對話視窗的Window物件
        Window dialogWindow = myDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager wm = getWindowManager();
        Display d = wm.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        lp.height = (int) (d.getWidth() * 0.9);
        lp.width = (int) (d.getWidth() * 0.9);
        dialogWindow.setAttributes(lp);

        final TextView tvRating = myDialog.findViewById(R.id.tvRating);
        final RatingBar rbRate = myDialog.findViewById(R.id.rbRate);
        rbRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean b) {
                ratePoint = rate;
                tvRating.setText(rate + "顆星");
            }
        });

        final EditText etComment = myDialog.findViewById(R.id.etComment);
        Button btnCancel = myDialog.findViewById(R.id.btnCommentCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.cancel();
            }
        });
        Button btnSubmit = myDialog.findViewById(R.id.btnCommentSubmit);
        final boolean mem1 = userData.getMem_id().equals(memBuilder.getMem_id());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratePoint == 0) {
                    Toast.makeText(ShowCaseDetailActivity.this,
                            "請給分數", Toast.LENGTH_SHORT).show();
                    return;
                }
                String commentContent = etComment.getText().toString().trim();
                try {
                    String jsonIn = (String) new TextTransferTask().execute(Utils.SET_COMMENT,
                            Utils.URL_ANDOROID_CONTROLLER, caseVO.getCase_id(), ratePoint,
                            commentContent, mem1).get();
                    caseVO = Utils.gson.fromJson(jsonIn, CasesVO.class);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                myDialog.cancel();
                setInfo();
            }
        });

        myDialog.show();
    }

}
