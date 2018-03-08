package idv.sean.photo_insearch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.activity.ShowOrderDetailActivity;
import idv.sean.photo_insearch.model.Order_masterVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class ProductOrderHistoryFragment extends Fragment {
    RecyclerView rvProductOrderList;
    List<Order_masterVO> orderList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String jsonIn = (String) new TextTransferTask().execute(Utils.GET_ORDERMASTER,
                    Utils.URL_ANDOROID_CONTROLLER, Utils.getMemVO().getMem_id()).get();
            Type type = new TypeToken<List<Order_masterVO>>(){}.getType();
            orderList = Utils.gson.fromJson(jsonIn, type);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate
                (R.layout.framgment_product_order_list, container, false);

        rvProductOrderList = view.findViewById(R.id.rvProductOrderList);
        rvProductOrderList.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        rvProductOrderList.setLayoutManager(manager);
        rvProductOrderList.setAdapter(new OrderMasterAdapter());

        return view;
    }

    private class OrderMasterAdapter extends RecyclerView.Adapter<OrderMasterAdapter.ViewHolder>{

        @Override
        public OrderMasterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_order_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderMasterAdapter.ViewHolder holder, int position) {
            final Order_masterVO masterVO = orderList.get(position);
            holder.tvId.setText(masterVO.getOrder_id());
            final String money = NumberFormat.getInstance().format(masterVO.getOrder_amt());
            holder.tvMoney.setText("NTD " + money + " 元");
            holder.tvDate.setText(masterVO.getOrder_date().toString());
            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ShowOrderDetailActivity.class);
                    intent.putExtra("orderId", masterVO.getOrder_id());
                    intent.putExtra("money", money);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvId, tvDate, tvMoney;
            Button btnView;

            public ViewHolder(View view) {
                super(view);
                tvId = view.findViewById(R.id.tvOrderNo);
                tvDate = view.findViewById(R.id.tvOrderDate);
                tvMoney = view.findViewById(R.id.tvOrderMoney);
                btnView = view.findViewById(R.id.btnShowOrderDetail);
            }
        }
    }
}
