package idv.sean.photo_insearch.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import idv.sean.photo_insearch.R;

public class QAFragment extends Fragment {
    TextView tvQA1, tvQA2, tvQA3, tvQA4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qanda, container, false);
        tvQA1 = view.findViewById(R.id.tvQA1);
        tvQA2 = view.findViewById(R.id.tvQA2);
        tvQA3 = view.findViewById(R.id.tvQA3);
        tvQA4 = view.findViewById(R.id.tvQA4);
        setText();

        return view;
    }

    public void setText() {
        tvQA1.setText("1.會員Ｑ＆Ａ\n" +
                "\n\t\tＱ：如何加入會員？\n" +
                "\t\tＡ：透過網站的“註冊會員”，填入相關資料，\n" +
                "\t\t\t\t    並通過驗證，即可成為會員。\n" +
                "\n\t\tＱ：加入會員須要費用嗎？\n" +
                "\t\tＡ：加入成為會員無需費用，另外可透過加值，\n" +
                "\t\t\t\t    可購買本網站的商品。\n" +
                "\n\t\tＱ：升級申請是什麼？\n" +
                "\t\tＡ：只要透過網站申請成為攝影師資格，\n" +
                "\t\t\t\t    就可以在本網站販售自己的數位作品，\n" +
                "\t\t\t\t    並且可以進行攝影案件的接案。\n");

        tvQA2.setText("2.點數功能Ｑ＆Ａ\n" +
                "\n\t\tＱ：如何儲值？\n" +
                "\t\tＡ：透過本網站的加值專區，\n" +
                "\t\t\t\t    並綁定信用卡即可加值點數。\n" +
                "\n\t\tＱ：點數的功用是什麼？\n" +
                "\t\tＡ：加值的點數可購買本網站的數位照片\n" +
                "\t\t\t\t    及各類攝影相關的器材商品。\n");

        tvQA3.setText("3.購物Ｑ＆Ａ\n" +
                "\n\t\tＱ：如何購買本網站商品？\n" +
                "\t\tＡ：可透過搜尋找到自己喜愛的商品，\n" +
                "\t\t\t\t    加入購物車之後，即可進行付款。\n" +
                "\n\t\tＱ：客服可代為訂購嗎？\n" +
                "\t\tＡ：客服恕無法代為訂購，需自行於網路上完成交易，\n" +
                "\t\t\t\t    亦無法接受以電話、e-mail 或傳真等\n" +
                "\t\t\t\t    其他方式下訂單。\n");

        tvQA4.setText("4.作品專區Ｑ＆Ａ\n" +
                "\n\t\tＱ：如何有上傳作品的資格？\n" +
                "\t\tＡ：必需加入成為網站會員，\n" +
                "\t\t\t\t    並且申請通過成為攝影師資格，\n" +
                "\t\t\t\t    即擁有上傳並販售作品的資格。\n" +
                "\n\t\tＱ：如何購買作品？\n" +
                "\t\tＡ：需要事先儲值點數，於喜愛的作品點選購買，\n" +
                "\t\t\t\t    即扣除點數完成購買。");
    }
}
