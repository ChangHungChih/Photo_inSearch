package idv.sean.photo_insearch.model;

import java.io.Serializable;
import java.sql.Date;

public class Order_masterVO implements Serializable {
    private String order_id;
    private String mem_id;
    private Date order_date;
    private Integer order_amt;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public Integer getOrder_amt() {
        return order_amt;
    }

    public void setOrder_amt(Integer order_amt) {
        this.order_amt = order_amt;
    }
}
