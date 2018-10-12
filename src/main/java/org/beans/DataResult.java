package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 13:59
 */
import java.util.List;
public class DataResult<T> {
    private List<? extends T> data;
    private int count;
    private boolean isSuccess = true;
    private String errMsg;

    public DataResult() {
    }

    public List<? extends T> getData() {
        return this.data;
    }

    public void setData(List<? extends T> data) {
        this.data = data;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
