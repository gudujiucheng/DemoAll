package com.canzhang.sample.manager.sqllite.fql;

/**
 * 上报数据备份数据库bean
 * Created by liveeili on 2017/7/27.
 */
public class CommonReportInfo {

    private Long id;
    private String reportId;

    public int type;

    public String data;

    public CommonReportInfo() {

    }

    public CommonReportInfo(Long id, int type, String data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonReportInfo{" +
                "id=" + id +
                ", reportId='" + reportId + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}
