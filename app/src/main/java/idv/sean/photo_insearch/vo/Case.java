package idv.sean.photo_insearch.vo;

public class Case {
    private int caseNo;
    private int memNo;
    private String title;
    private String content;
    private int memPoint;

    public Case() {
    }

    public Case(int caseNo, int memNo, String title, String content, int memPoint) {
        this.caseNo = caseNo;
        this.memNo = memNo;
        this.title = title;
        this.content = content;
        this.memPoint = memPoint;
    }

    public int getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(int caseNo) {
        this.caseNo = caseNo;
    }

    public int getMemNo() {
        return memNo;
    }

    public void setMemNo(int memNo) {
        this.memNo = memNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMemPoint() {
        return memPoint;
    }

    public void setMemPoint(int memPoint) {
        this.memPoint = memPoint;
    }
}
