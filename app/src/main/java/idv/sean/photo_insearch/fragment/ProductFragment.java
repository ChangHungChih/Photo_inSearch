package idv.sean.photo_insearch.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.ProductVO;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;


public class ProductFragment extends Fragment {
    private RecyclerView rvProduct;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        rvProduct = (RecyclerView) view.findViewById(R.id.recyclerView_photo);
        rvProduct.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvProduct.setLayoutManager(layoutManager);
        new ProductsDownloadTask().execute(Utils.URL_ANDOROID_CONTROLLER);

        RecyclerView rv = view.findViewById(R.id.recyclerView_photo);
        rv.setBackgroundColor(getResources().getColor(R.color.productBackground));
        return view;
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        private List<ProductVO> productList;

        public ProductAdapter(List<ProductVO> productList) {
            this.productList = productList;
        }

        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gridview_product, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
            final ProductVO product = productList.get(position);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(product.getProd_pic(), 0, product.getProd_pic().length);
            holder.ivPicture.setImageBitmap(bitmap);
            holder.tvName.setText(product.getProd_name());
            holder.tvPrice.setText(String.valueOf(product.getProd_price()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getContext(), product.getProd_name() + "clicked", Toast.LENGTH_SHORT).show();
                    Dialog myDialog = new Dialog(getContext());
                    myDialog.setTitle("商品詳情");
                    myDialog.setContentView(R.layout.activity_showproduct);
                    // 透過myDialog.getWindow()取得這個對話視窗的Window物件
                    Window dialogWindow = myDialog.getWindow();
                    dialogWindow.setGravity(Gravity.CENTER);

                    WindowManager wm = getActivity().getWindowManager();
                    Display d = wm.getDefaultDisplay(); // 取得螢幕寬、高用
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
                    lp.height = (int) (d.getHeight() * 0.7);
                    lp.width = (int)(d.getWidth() * 0.85);
                    dialogWindow.setAttributes(lp);

                    TextView prodName = myDialog.findViewById(R.id.tvShowProductTitle);
                    prodName.setText(product.getProd_name());
                    TextView prodPrice = myDialog.findViewById(R.id.tvShowProductPrice);
                    prodPrice.setText(String.valueOf(product.getProd_price()));
                    TextView prodContent = myDialog.findViewById(R.id.tvShowProductContent);
                    prodContent.setText(product.getProd_detil());
                    ImageView ivProd = myDialog.findViewById(R.id.ivShowProduct);
                    ivProd.setImageBitmap(bitmap);

                    myDialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivPicture;
            private TextView tvName;
            private TextView tvPrice;

            public ViewHolder(View view) {
                super(view);
                ivPicture = (ImageView) view.findViewById(R.id.ivProduct);
                tvName = (TextView) view.findViewById(R.id.tvProduct);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            }
        }
    }

    private class ProductsDownloadTask extends AsyncTask<Object, Void, List<ProductVO>>{
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected List<ProductVO> doInBackground(Object... objects) {
            String url = (String) objects[0];
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "products_all_download");
            List<ProductVO> productsList = null;
            try {
                String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                Type type = new TypeToken<List<ProductVO>>() {}.getType();
                 productsList = Utils.gson.fromJson(jsonIn, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return productsList;
        }

        @Override
        protected void onPostExecute(List<ProductVO> list) {
            ProductAdapter adapter = new ProductAdapter(list);
            rvProduct.setAdapter(adapter);
            progressDialog.cancel();
        }
    }
}
