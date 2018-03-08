package idv.sean.photo_insearch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.Order_detailVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class ShowOrderDetailActivity extends AppCompatActivity {
    List<Order_detailVO> orderList;
    Map<String, String> productName;
    RecyclerView rvDetail;
    TextView tvTitle, tvTotal;
    Button btnBack, btnNoUse;
    String orderId, money;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        orderId = getIntent().getStringExtra("orderId");
        money = getIntent().getStringExtra("money");
        findViews();
        initData();
    }

    public void findViews() {
        rvDetail = findViewById(R.id.rvCart);
        tvTitle = findViewById(R.id.tvCartTitle);
        tvTitle.setText("訂單明細");
        //set price sum
        tvTotal = findViewById(R.id.tvSum);
        tvTotal.setText("NTD " + money + " 元");

        btnNoUse = findViewById(R.id.btnClear);
        btnNoUse.setVisibility(View.GONE);
        btnBack = findViewById(R.id.btnCheckOut);
        btnBack.setText("返回");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initData() {
        try {
            String jsonIn = (String) new TextTransferTask().execute
                    (Utils.GET_ORDER_DETAIL, Utils.URL_ANDOROID_CONTROLLER, orderId).get();
            JsonObject jsonObject = Utils.gson.fromJson(jsonIn, JsonObject.class);
            //return to list
            Type listType = new TypeToken<List<Order_detailVO>>(){}.getType();
            orderList = Utils.gson.fromJson(jsonObject.get("list").getAsString(), listType);
            //return to map
            Type mapType = new TypeToken<Map<String, String>>(){}.getType();
            productName = Utils.gson.fromJson(jsonObject.get("map").getAsString(), mapType);

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            rvDetail.setLayoutManager(manager);
            rvDetail.setAdapter(new OrderDetailAdapter());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

        @Override
        public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ShowOrderDetailActivity.this)
                    .inflate(R.layout.cardview_cartcontent, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderDetailAdapter.ViewHolder holder, int position) {
            Order_detailVO vo = orderList.get(position);
            holder.tvName.setText(productName.get(vo.getProd_id()));
            String price = NumberFormat.getInstance().format(vo.getProd_price());
            holder.tvPrice.setText("NTD " + price + " 元");
            holder.tvQtn.setText(vo.getProd_qty().toString());
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvName, tvPrice, tvQtn;
            ImageView ivPlus, ivMinus;

            public ViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvProductName);
                tvName.setSelected(true);
                tvPrice = view.findViewById(R.id.tvCartPrice);
                tvQtn = view.findViewById(R.id.tvQtn);
                ivPlus = view.findViewById(R.id.ivPlus);
                ivPlus.setVisibility(View.INVISIBLE);
                ivMinus = view.findViewById(R.id.ivMinus);
                ivMinus.setVisibility(View.INVISIBLE);
            }
        }
    }
}
