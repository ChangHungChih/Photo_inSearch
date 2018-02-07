package idv.sean.photo_insearch.vo;

import java.io.Serializable;
import java.sql.Date;

public class MemVO implements Serializable {

    private String mem_id;
    private String mem_name;
    private String mem_acc;
    private String mem_pwd;
    private String mem_sex;
    private Date mem_bd;
    private String mem_mail;
    private String mem_phone;
    private String mem_addr;
    private Date mem_jointime;
    private String mem_level;
    private String mem_state;
    private String mem_agree;

    public MemVO() {
    }

    public MemVO(String mem_id, String mem_name, String mem_acc, String mem_pwd,
                 String mem_sex, Date mem_bd, String mem_mail, String mem_phone,
                 String mem_addr, Date mem_jointime, String mem_level, String mem_state,
                 String mem_agree) {
        this.mem_id = mem_id;
        this.mem_name = mem_name;
        this.mem_acc = mem_acc;
        this.mem_pwd = mem_pwd;
        this.mem_sex = mem_sex;
        this.mem_bd = mem_bd;
        this.mem_mail = mem_mail;
        this.mem_phone = mem_phone;
        this.mem_addr = mem_addr;
        this.mem_jointime = mem_jointime;
        this.mem_level = mem_level;
        this.mem_state = mem_state;
        this.mem_agree = mem_agree;
    }

    public String getMem_id() {
        return mem_id;
    }
    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public String getMem_name() {
        return mem_name;
    }
    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }
    public String getMem_acc() {
        return mem_acc;
    }
    public void setMem_acc(String mem_acc) {
        this.mem_acc = mem_acc;
    }
    public String getMem_pwd() {
        return mem_pwd;
    }
    public void setMem_pwd(String mem_pwd) {
        this.mem_pwd = mem_pwd;
    }
    public String getMem_sex() {
        return mem_sex;
    }
    public void setMem_sex(String mem_sex) {
        this.mem_sex = mem_sex;
    }
    public Date getMem_bd() {
        return mem_bd;
    }
    public void setMem_bd(Date mem_bd) {
        this.mem_bd = mem_bd;
    }
    public String getMem_mail() {
        return mem_mail;
    }
    public void setMem_mail(String mem_mail) {
        this.mem_mail = mem_mail;
    }
    public String getMem_phone() {
        return mem_phone;
    }
    public void setMem_phone(String mem_phone) {
        this.mem_phone = mem_phone;
    }
    public String getMem_addr() {
        return mem_addr;
    }
    public void setMem_addr(String mem_addr) {
        this.mem_addr = mem_addr;
    }
    public Date getMem_jointime() {
        return mem_jointime;
    }
    public void setMem_jointime(Date mem_jointime) {
        this.mem_jointime = mem_jointime;
    }
    public String getMem_level() {
        return mem_level;
    }
    public void setMem_level(String mem_level) {
        this.mem_level = mem_level;
    }
    public String getMem_state() {
        return mem_state;
    }
    public void setMem_state(String mem_state) {
        this.mem_state = mem_state;
    }
    public String getMem_agree() {
        return mem_agree;
    }
    public void setMem_agree(String mem_agree) {
        this.mem_agree = mem_agree;
    }
}
