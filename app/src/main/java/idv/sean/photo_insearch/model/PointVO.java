package idv.sean.photo_insearch.model;

import java.io.Serializable;

public class PointVO implements Serializable {
    private String pt_id;
    private String mem_id;
    private String pt_time;
    private String pt_type;
    private Integer pt_dtl;

    public String getPt_id() {
        return pt_id;
    }

    public void setPt_id(String pt_id) {
        this.pt_id = pt_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getPt_time() {
        return pt_time;
    }

    public void setPt_time(String pt_time) {
        this.pt_time = pt_time;
    }

    public String getPt_type() {
        return pt_type;
    }

    public void setPt_type(String pt_type) {
        this.pt_type = pt_type;
    }

    public Integer getPt_dtl() {
        return pt_dtl;
    }

    public void setPt_dtl(Integer pt_dtl) {
        this.pt_dtl = pt_dtl;
    }
}
