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
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.activity.ShoppingCartActivity;
import idv.sean.photo_insearch.model.CartVO;
import idv.sean.photo_insearch.model.ProductVO;
import idv.sean.photo_insearch.util.Utils;


public class ProductFragment extends Fragment {
    private RecyclerView rvProduct;
    private ProgressDialog progressDialog;
    private NumberFormat nf = NumberFormat.getInstance();

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
            holder.tvPrice.setText("NTD: " + nf.format(product.getProd_price()) + "元");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(product, bitmap);
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public void showDialog(final ProductVO product, Bitmap bitmap) {
            //show product detail by dialog
            final Dialog myDialog = new Dialog(getContext());
            myDialog.setTitle("商品詳情");
            myDialog.setContentView(R.layout.dialog_showproduct);
            // 透過myDialog.getWindow()取得這個對話視窗的Window物件
            Window dialogWindow = myDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);

            WindowManager wm = getActivity().getWindowManager();
            Display d = wm.getDefaultDisplay(); // 取得螢幕寬、高用
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
            lp.height = (int) (d.getWidth() * 0.9);
            lp.width = (int) (d.getWidth() * 0.9);
            dialogWindow.setAttributes(lp);

            TextView prodName = myDialog.findViewById(R.id.tvShowProductTitle);
            prodName.setText(product.getProd_name());
            TextView prodPrice = myDialog.findViewById(R.id.tvShowProductPrice);
            prodPrice.setText("NTD: " + nf.format(product.getProd_price()) + "元");
            myDialog.findViewById(R.id.tvQtn).setVisibility(View.VISIBLE);

            TextView prodContent = myDialog.findViewById(R.id.tvShowProductContent);
            prodContent.setText(product.getProd_detil());
            ImageView ivProd = myDialog.findViewById(R.id.ivShowProduct);
            ivProd.setImageBitmap(bitmap);

            final EditText etQtn = myDialog.findViewById(R.id.etQtn);
            etQtn.setVisibility(View.VISIBLE);
            Button btnCart = myDialog.findViewById(R.id.btnCart);
            btnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etQtn.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "請輸入數量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addToCart(product, Integer.valueOf(etQtn.getText().toString()));
                    myDialog.cancel();
                }
            });

            myDialog.show();
        }

        public void addToCart(ProductVO product, int qtn) {
            List<CartVO> cart = ShoppingCartActivity.getCart();
            if (cart == null) {
                cart = new ArrayList<>();
            }
            CartVO vo = new CartVO();
            vo.setProd_id(product.getProd_id());
            vo.setProd_name(product.getProd_name());
            vo.setProd_price(product.getProd_price());
            vo.setProd_qty(qtn);
            // 要比對欲加入商品與購物車內商品的id是否相同(會呼叫CartVO.equals())，
            // 相同則將該商品取出後，修改數量；不同則將該商品放入購物車內
            int index = cart.indexOf(vo);
            if (index == -1) {
                cart.add(vo);
            } else {
                CartVO cartVO = cart.get(index);
                cartVO.setProd_qty(cartVO.getProd_qty() + qtn);
            }
            ShoppingCartActivity.setCart(cart);
            Toast.makeText(getContext(), "已加入購物車", Toast.LENGTH_SHORT).show();
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

    private class ProductsDownloadTask extends AsyncTask<Object, Void, List<ProductVO>> {
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
                Type type = new TypeToken<List<ProductVO>>() {
                }.getType();
                productsList = Utils.gson.fromJson(jsonIn, type);
                for (ProductVO product : productsList) {
                    byte[] pic = Base64.decode(product.getPicBase64(), Base64.DEFAULT);
                    product.setPicBase64(null);
                    product.setProd_pic(pic);
                }
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
