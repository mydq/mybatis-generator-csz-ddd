package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:05
 */
public class PrivilegeInfo {
    private String userId; //此字段与表中的CREATOR 和 MODIFIER 有关
    private String openAccount;
    private String type;
    private String status;
    private String token;
    private String aesKey;

    public PrivilegeInfo() {
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenAccount() {
        return this.openAccount;
    }

    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public static PrivilegeInfo getDummyPrivilegeInfo() {
        PrivilegeInfo pvgInfo = new PrivilegeInfo();
        pvgInfo.setUserId((String)null);
        return pvgInfo;
    }
}
