package com.canzhang.sample.manager.thread.demo.fqlreport.bean;


import org.json.JSONArray;

/**
 * 上报数据的包装类
 * Created by liveeili on 2017/7/26.
 * http://wiki.fenqile.com/pages/viewpage.action?pageId=26396346
 */
public class ReportWrapper {
    /**
     * 每条数据的唯一标识，自动生成
     */
    private long id;
    /**
     * 当前数据类型，例如：页面访问记录、事件等
     * "data_type":1
     */
    private int dataType;
    /**
     * 当前数据类型对应的记录列表，格式为:
     * 'record_list': [{...},{...},{...}]
     */
    private JSONArray recordList;






    public ReportWrapper(int dataType, JSONArray recordList) {
        this.dataType = dataType;
        this.recordList = recordList;

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getDataType() {
        return dataType;
    }

    public JSONArray getRecordList() {
        return recordList;
    }



    @Override
    public String toString() {
        return "ReportWrapper{" +
                "id=" + id +
                ", dataType=" + dataType +
                ", recordList=" + recordList +
                '}';
    }
}
