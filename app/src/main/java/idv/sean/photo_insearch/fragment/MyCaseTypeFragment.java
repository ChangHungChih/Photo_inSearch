package idv.sean.photo_insearch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.activity.ShowCaseActivity;

public class MyCaseTypeFragment extends Fragment implements View.OnClickListener {
    public static final int POSTED_CASES = 1;
    public static final int PROCEEDING_CASES = 2;
    public static final int FINISHED_CASES = 3;
    public static final int INVITED_CASES = 4;
    public static final int CLOSED_CASES = 5;
    Button btnPosted, btnProceeding, btnFinished, btnInvited, btnClosed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate
                (R.layout.fragment_cases_type_choose, container, false);
        btnPosted = view.findViewById(R.id.btnCasePosted);
        btnProceeding = view.findViewById(R.id.btnCaseProceeding);
        btnFinished = view.findViewById(R.id.btnCaseFinished);
        btnInvited = view.findViewById(R.id.btnCaseInvited);
        btnClosed = view.findViewById(R.id.btnCasePast);
        btnPosted.setOnClickListener(this);
        btnProceeding.setOnClickListener(this);
        btnFinished.setOnClickListener(this);
        btnInvited.setOnClickListener(this);
        btnClosed.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent(getContext(), ShowCaseActivity.class);
        switch (id) {
            case R.id.btnCasePosted:
                intent.putExtra("case", POSTED_CASES);
                startActivity(intent);
                break;

            case R.id.btnCaseProceeding:
                intent.putExtra("case", PROCEEDING_CASES);
                startActivity(intent);
                break;

            case R.id.btnCaseFinished:
                intent.putExtra("case", FINISHED_CASES);
                startActivity(intent);
                break;

            case R.id.btnCaseInvited:
                intent.putExtra("case", INVITED_CASES);
                startActivity(intent);
                break;

            case R.id.btnCasePast:
                intent.putExtra("case", CLOSED_CASES);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
