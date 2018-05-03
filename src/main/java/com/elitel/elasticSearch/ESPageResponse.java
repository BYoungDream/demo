package com.elitel.elasticSearch;

import java.io.Serializable;
import java.util.List;

/**
 * created by guoyanfei on 2018/3/20
 */
public class ESPageResponse<T> implements Serializable {
    private static final long serialVersionUID = 768549219476295665L;
    private long total;//总条数
    private List<T> rows;//当前页数据
    private String description;//描述信息

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
