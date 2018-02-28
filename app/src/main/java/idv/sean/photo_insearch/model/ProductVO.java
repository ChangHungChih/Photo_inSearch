package idv.sean.photo_insearch.model;

import java.io.Serializable;

public class ProductVO implements Serializable {
    private String prod_id;
    private String prod_type_id;
    private String brand_id;
    private String prod_name;
    private Integer prod_price;
    private Integer prod_status;
    private String prod_detil;
    private byte[] prod_pic;

    public ProductVO() {
    }

    public ProductVO(String prod_id, String prod_type_id, String brand_id,
                     String prod_name, Integer prod_price, Integer prod_status,
                     String prod_detil, byte[] prod_pic) {
        this.prod_id = prod_id;
        this.prod_type_id = prod_type_id;
        this.brand_id = brand_id;
        this.prod_name = prod_name;
        this.prod_price = prod_price;
        this.prod_status = prod_status;
        this.prod_detil = prod_detil;
        this.prod_pic = prod_pic;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_type_id() {
        return prod_type_id;
    }

    public void setProd_type_id(String prod_type_id) {
        this.prod_type_id = prod_type_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
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

    public Integer getProd_status() {
        return prod_status;
    }

    public void setProd_status(Integer prod_status) {
        this.prod_status = prod_status;
    }

    public String getProd_detil() {
        return prod_detil;
    }

    public void setProd_detil(String prod_detil) {
        this.prod_detil = prod_detil;
    }

    public byte[] getProd_pic() {
        return prod_pic;
    }

    public void setProd_pic(byte[] prod_pic) {
        this.prod_pic = prod_pic;
    }
}
