package com.canzhang.sample.manager.db.sqllite.BRL.db.bean;

/**
 * @Description: 基础上报对象类型
 * @Author: canzhang
 * @CreateDate: 2019/11/25 18:08
 */
public class BaseBRLBean {
    private long id;//数据库自增id
    private int type;//数据类型
    private String data;//数据详情  json串格式
    private boolean isNeedSave = true; //是否需要存储到db（一般数据默认存储，从数据库中取的数据和敏感数据，默认不存储）
    private boolean isRealTimeReport;//是否实时上报

    public BaseBRLBean(long id, int type, String data, boolean isRealTimeReport, boolean isNeedSave) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.isNeedSave = isNeedSave;
        this.isRealTimeReport = isRealTimeReport;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public boolean isNeedSave() {
        return isNeedSave;
    }

    public boolean isRealTimeReport() {
        return isRealTimeReport;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static class BaseCRLBeanBuilder {

        private long id;
        private int type;
        private String data;
        private boolean isNeedSave = true;
        private boolean isRealTimeReport;

        public BaseCRLBeanBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public BaseCRLBeanBuilder setType(int type) {
            this.type = type;
            return this;
        }

        public BaseCRLBeanBuilder setData(String data) {
            this.data = data;
            return this;
        }

        public BaseCRLBeanBuilder setNeedSave(boolean needSave) {
            this.isNeedSave = needSave;
            return this;
        }

        public BaseCRLBeanBuilder setRealTimeReport(boolean realTimeReport) {
            this.isRealTimeReport = realTimeReport;
            return this;
        }

        public BaseBRLBean createCRLBean() {
            return new BaseBRLBean(id, type, data, isRealTimeReport, isNeedSave);
        }
    }

    @Override
    public String toString() {
        return "BaseBRLBean{" +
                "id=" + id +
                ", type=" + type +
                ", data='" + data + '\'' +
                ", isNeedSave=" + isNeedSave +
                ", isRealTimeReport=" + isRealTimeReport +
                '}';
    }
}
