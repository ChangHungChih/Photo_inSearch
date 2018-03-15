package idv.sean.photo_insearch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.fragment.NoDataFragment;
import idv.sean.photo_insearch.model.CartVO;
import idv.sean.photo_insearch.model.MemVO;
import idv.sean.photo_insearch.util.ItemTouchHelperAdapter;
import idv.sean.photo_insearch.util.ItemTouchHelperViewHolder;
import idv.sean.photo_insearch.util.TextTransferTask;
import idv.sean.photo_insearch.util.Utils;

public class ShoppingCartActivity extends AppCompatActivity {
    private final String TAG = "ShoppingCartActivity";
    private static List<CartVO> cart;
    private RecyclerView rvCart;
    private TextView tvSum;
    private Button btnCheckout, btnClear;
    private SharedPreferences preferences;
    private NumberFormat nf = NumberFormat.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (cart == null || cart.size() == 0) {
            showNoDataFragment();
            return;
        }
        rvCart = findViewById(R.id.rvCart);
        rvCart.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvCart.setLayoutManager(manager);
        CartAdapter adapter = new CartAdapter();
        rvCart.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallBack(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvCart);

        tvSum = findViewById(R.id.tvSum);
        setSum();
        btnCheckout = findViewById(R.id.btnCheckOut);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.clear();
                rvCart.getAdapter().notifyDataSetChanged();
                setSum();
                showNoDataFragment();
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("preference", MODE_PRIVATE);
                boolean login = preferences.getBoolean("login", false);
                if (login) {
                    String memJson = preferences.getString("memVO", "");
                    MemVO memVO = Utils.gson.fromJson(memJson, MemVO.class);
                    String memId = memVO.getMem_id();
                    String total = tvSum.getText().toString();
                    //從字串中切出數字再將逗號刪除
                    int amount = Integer.valueOf(total.substring(4, total.length() - 2)
                            .replace(",", ""));
                    String cartList = Utils.gson.toJson(cart);
                    try {
                        String in = (String) new TextTransferTask()
                                .execute(Utils.CHECKOUT, Utils.URL_ANDOROID_CONTROLLER,
                                        memId, amount, cartList).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ShoppingCartActivity.this,
                            "結帳完成", Toast.LENGTH_SHORT).show();
                    cart.clear();
                    rvCart.getAdapter().notifyDataSetChanged();
                    finish();
                } else {                                 //not login
                    Intent intent = new Intent
                            (ShoppingCartActivity.this, LoginDialogActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void setSum() {
        int sum = 0;
        for (CartVO vo : cart) {
            sum += vo.getProd_price() * vo.getProd_qty();
        }
        tvSum.setText("NTD " + nf.format(sum) + " 元");
    }

    public void showNoDataFragment() {
        Fragment noDataFragment = new NoDataFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.llCartContainer, noDataFragment, "No Data").commit();
        if (btnCheckout != null)
            btnCheckout.setOnClickListener(null);
    }

    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>
            implements ItemTouchHelperAdapter {

        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate
                    (R.layout.cardview_cartcontent, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
            final CartVO cartVO = cart.get(position);
            holder.name.setText(cartVO.getProd_name());
            holder.price.setText("NTD " + nf.format(cartVO.getProd_price()) + " 元");
            holder.qtn.setText(cartVO.getProd_qty().toString());
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartVO.setProd_qty(cartVO.getProd_qty() + 1);
                    holder.qtn.setText(cartVO.getProd_qty().toString());
                    setSum();
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartVO.setProd_qty(cartVO.getProd_qty() - 1);
                    if (cartVO.getProd_qty() == 0) {
                        cart.remove(position);
                        rvCart.getAdapter().notifyDataSetChanged();
                        if (cart.size() == 0) {
                            showNoDataFragment();
                        }
                    }
                    holder.qtn.setText(cartVO.getProd_qty().toString());
                    setSum();
                }
            });
        }

        @Override
        public int getItemCount() {
            return cart.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(cart, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            cart.remove(position);
            setSum();
            notifyDataSetChanged();
            if (cart.size() == 0) {
                showNoDataFragment();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
            TextView name, price, qtn;
            ImageView plus, minus;
            private float defaultZ;

            public ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.tvProductName);
                name.setSelected(true);
                price = view.findViewById(R.id.tvCartPrice);
                qtn = view.findViewById(R.id.tvQtn);
                plus = view.findViewById(R.id.ivPlus);
                minus = view.findViewById(R.id.ivMinus);
                defaultZ = itemView.getTranslationZ();
            }

            @Override
            public void onItemSelected() {
                itemView.setTranslationZ(15.0f);
            }

            @Override
            public void onItemClear() {
                itemView.setTranslationZ(defaultZ);
            }
        }
    }

    private class SimpleItemTouchHelperCallBack extends ItemTouchHelper.Callback {
        private ItemTouchHelperAdapter adapter;

        public SimpleItemTouchHelperCallBack(ItemTouchHelperAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            // 也可以設ItemTouchHelper.RIGHT(向右滑)，
            // 或是 ItemTouchHelper.START | ItemTouchHelper.END (左右滑都可以)
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX,
                                float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float width = (float) viewHolder.itemView.getWidth();
                float alpha = 1.0f - Math.abs(dX) / width;
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder,
                        dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            // We only want the active item
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemViewHolder =
                            (ItemTouchHelperViewHolder) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemClear();
                viewHolder.itemView.setTranslationX(0);
                viewHolder.itemView.setAlpha(1);
            }
        }
    }

    public static List<CartVO> getCart() {
        return cart;
    }

    public static void setCart(List<CartVO> cart) {
        ShoppingCartActivity.cart = cart;
    }
}
