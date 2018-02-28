package idv.sean.photo_insearch.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class ReportFragment extends Fragment implements View.OnClickListener {
    EditText etMail, etMailContent;
    Button btnSendMail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        etMail = view.findViewById(R.id.etEmail);
        etMailContent = view.findViewById(R.id.etMailContent);
        btnSendMail = view.findViewById(R.id.btnSendMail);
        btnSendMail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        String to = "ba106g1.iii@gmail.com";
        String subject = "來自Android用戶的意見回饋";
        String email = etMail.getText().toString();
        String messageText = etMailContent.getText().toString();
        if (messageText.isEmpty()) {
            Toast.makeText(getActivity(), "請輸入意見", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(getActivity(), "請輸入信箱", Toast.LENGTH_SHORT).show();
            return;
        } else {
            TextTransferTask textTransferTask = new TextTransferTask();
            int result = 0;
            String memberName = Utils.getMemVO().getMem_name();
            String userData = "會員: " + memberName + "\nE-mail: " + email + "\n意見: ";
            try {
                result = (int) textTransferTask.execute
                        (Utils.SEND_MAIL, Utils.URL_ANDOROID_CONTROLLER
                                , to, subject, userData + messageText).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            switch (result) {
                case 0:
                    Toast.makeText(getContext(), "傳送失敗，請稍後重試‧", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getContext(), "傳送成功，感謝您的意見。", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "傳送失敗，請稍後重試‧", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            etMail.setText(null);
            etMailContent.setText(null);
        }
    }
}
