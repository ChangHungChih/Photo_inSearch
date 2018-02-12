package idv.sean.photo_insearch.vo;


import java.io.*;
import java.sql.Date;

public class CasesVO implements Serializable {

    private String case_id;
    private String mem_id;
    private String mem_id2;
    private String case_type;
    private String case_title;
    private String case_content;
    private String case_state;
    private String comment1;
    private Double score1;
    private String comment2;
    private Double score2;
    private String emp_id;
    private Date case_create_date;

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

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getCase_title() {
        return case_title;
    }

    public void setCase_title(String case_title) {
        this.case_title = case_title;
    }

    public String getCase_content() {
        return case_content;
    }

    public void setCase_content(String case_content) {
        this.case_content = case_content;
    }

    public String getCase_state() {
        return case_state;
    }

    public void setCase_state(String case_state) {
        this.case_state = case_state;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public Double getScore1() {
        return score1;
    }

    public void setScore1(Double score1) {
        this.score1 = score1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public Double getScore2() {
        return score2;
    }

    public void setScore2(Double score2) {
        this.score2 = score2;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public Date getCase_create_date() {
        return case_create_date;
    }

    public void setCase_create_date(Date case_create_date) {
        this.case_create_date = case_create_date;
    }

}

