package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:26
 */
public class GogoExample {
    public static final String _YES = "y";
    public static final String _NO = "n";
    protected String orderByClause;
    protected boolean distinct;
    protected Page page;
    protected String fullOrgPath;
    protected String owner;

    public GogoExample() {
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return this.page;
    }

    public void setFullOrgPath(String fullOrgPath) {
        this.fullOrgPath = fullOrgPath;
    }

    public String getFullOrgPath() {
        return this.fullOrgPath;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
    }
}
