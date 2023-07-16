package com.dreamtech.tldental.models;

public class DataPageObject {
    private int total;
    private String page;
    private String pageSize;
    private Object data;

    public DataPageObject(int total, String page, String pageSize, Object data) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
