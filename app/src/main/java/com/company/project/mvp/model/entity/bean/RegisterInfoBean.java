package com.company.project.mvp.model.entity.bean;

/**
 * Author：leguang on 2016/10/21 0021 13:54
 * Email：langmanleguang@qq.com
 */
public class RegisterInfoBean {
    private String account;
    private String userPwd;
    private String OSType;
    private String machineCode;
    private String phoneBrand;
    private String phoneSysVersion;
    private String phoneModel;
    private String auth_key;

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getOSType() {
        return OSType;
    }

    public void setOSType(String OSType) {
        this.OSType = OSType;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneSysVersion() {
        return phoneSysVersion;
    }

    public void setPhoneSysVersion(String phoneSysVersion) {
        this.phoneSysVersion = phoneSysVersion;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}
