package idv.sean.photo_insearch.model;

import java.io.Serializable;

public class CartVO implements Serializable {
    private String prod_id;
    private String prod_name;
    private Integer prod_price;
    private Integer prod_qty;

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || !(obj instanceof CartVO)){
            return false;
        }
        return this.getProd_id().equals(((CartVO)obj).getProd_id());
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public Integer getProd_price() {
        return prod_price;
    }

    public void setProd_price(Integer prod_price) {
        this.prod_price = prod_price;
    }

    public Integer getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(Integer prod_qty) {
        this.prod_qty = prod_qty;
    }
}
