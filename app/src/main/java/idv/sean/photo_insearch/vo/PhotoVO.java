package idv.sean.photo_insearch.vo;

import java.io.Serializable;
import java.sql.Date;


public class PhotoVO implements Serializable {
    private String photo_id;
    private String mem_id;
    private String type_id;
    private String photo_name;
    private String photo_des;
    private byte[] photo_pic;
    private Integer photo_pric;
    private Date photo_date;
    private String photo_state;
    private String emp_id;


    public PhotoVO() {
        super();
    }


    public PhotoVO(String photo_id, String mem_id, String type_id, String photo_name, String photo_des, byte[] photo_pic,
                   Integer photo_pric, Date photo_date, String photo_state, String emp_id) {
        super();
        this.photo_id = photo_id;
        this.mem_id = mem_id;
        this.type_id = type_id;
        this.photo_name = photo_name;
        this.photo_des = photo_des;
        this.photo_pic = photo_pic;
        this.photo_pric = photo_pric;
        this.photo_date = photo_date;
        this.photo_state = photo_state;
        this.emp_id = emp_id;
    }


    public String getPhoto_id() {
        return photo_id;
    }


    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }


    public String getMem_id() {
        return mem_id;
    }


    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }


    public String getType_id() {
        return type_id;
    }


    public void setType_id(String type_id) {
        this.type_id = type_id;
    }


    public String getPhoto_name() {
        return photo_name;
    }


    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }


    public String getPhoto_des() {
        return photo_des;
    }


    public void setPhoto_des(String photo_des) {
        this.photo_des = photo_des;
    }


    public byte[] getPhoto_pic() {
        return photo_pic;
    }


    public void setPhoto_pic(byte[] bs) {
        this.photo_pic = bs;
    }


    public Integer getPhoto_pric() {
        return photo_pric;
    }


    public void setPhoto_pric(Integer photo_pric) {
        this.photo_pric = photo_pric;
    }


    public Date getPhoto_date() {
        return photo_date;
    }


    public void setPhoto_date(Date photo_date) {
        this.photo_date = photo_date;
    }


    public String getPhoto_state() {
        return photo_state;
    }


    public void setPhoto_state(String photo_state) {
        this.photo_state = photo_state;
    }


    public String getEmp_id() {
        return emp_id;
    }


    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

}
