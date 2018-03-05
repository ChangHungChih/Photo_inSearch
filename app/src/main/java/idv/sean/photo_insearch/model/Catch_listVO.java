package idv.sean.photo_insearch.model;

import java.io.Serializable;

public class Catch_listVO implements Serializable {
    private String case_id;
    private String mem_id;
    private String mem_id2;

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_id2() {
        return mem_id2;
    }

    public void setMem_id2(String mem_id2) {
        this.mem_id2 = mem_id2;
    }
}
