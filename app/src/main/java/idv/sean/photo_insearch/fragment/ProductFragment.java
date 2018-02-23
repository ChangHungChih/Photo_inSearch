package idv.sean.photo_insearch.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.Product;


public class ProductFragment extends Fragment{
    private RecyclerView rvProduct;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productList = new ArrayList<>();
        productList.add(new Product(R.drawable.cannon6d_markii,"Cannon 6D MarkII",50000));
        productList.add(new Product(R.drawable.cannon_powershot,"Canno Powershot",6000));
        productList.add(new Product(R.drawable.nikon_d7500,"Nikon D7500",45000));
        productList.add(new Product(R.drawable.nikon_w100,"Nikon W100",35000));
        productList.add(new Product(R.drawable.olympus_e_m10_markiii,"Olympus E-M10 MarkIII",30000));
        productList.add(new Product(R.drawable.tamrom_lens,"Tamurom Lens for Nikon",18000));
        productAdapter = new ProductAdapter(productList);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        rvProduct = (RecyclerView) view.findViewById(R.id.recyclerView_photo);
        rvProduct.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(productAdapter);

        return view;
    }



    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
        private List<Product> productList;

        public ProductAdapter(List<Product> productList) {
            this.productList = productList;
        }

        @Override
        public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate( R.layout.gridview_product,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
            final Product product = productList.get(position);
            holder.ivPicture.setImageResource(product.getPicture());
            holder.tvName.setText(product.getName());
            holder.tvPrice.setText(String.valueOf(product.getPrice()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),product.getName()+"clicked",Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView ivPicture;
            private TextView tvName;
            private TextView tvPrice;

            public ViewHolder(View view) {
                super(view);
                ivPicture = (ImageView)view.findViewById(R.id.ivProduct);
                tvName = (TextView)view.findViewById(R.id.tvProduct);
                tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            }
        }
    }
}
