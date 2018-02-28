package idv.sean.photo_insearch.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import idv.sean.photo_insearch.R;

public class AboutUsFragment extends Fragment {
    private final String TAG = "AboutUsFragment";
    private ImageView ivPicture;
    private TextView tvAboutUs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutus,container,false);
        ivPicture = view.findViewById(R.id.ivAboutUs);
        tvAboutUs = view.findViewById(R.id.tvAboutUs);
        tvAboutUs.setText("\n  我們是一個簡單又安全的友善交易及合作平台，" +
                "鑑於目前網路上圖片相片的智慧財產相關法律知識日趨普及，" +
                " \t卻礙於中文的交易平台相對稀少。\n\n  為了提倡使用者付費及保障攝影的創作資產，" +
                "\t我們想透過這個平台讓攝影者與圖片使用者有更友善的交易環境。" +
                "\t\n\n  您可透過我們的網站購買專業圖片、上傳您的專業作品為自己賺取費用、購買所需要的攝影器材、" +
                "\t及透過發案及接案合作專區，找到您符合需求的攝影師及找到您想進行接案的案件。");

        return view;
    }
}
