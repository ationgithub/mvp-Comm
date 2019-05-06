package com.company.project.mvp.model.entity.db;

import org.litepal.crud.DataSupport;

/**
 * Author：leguang on 2016/11/22 0022 09:58
 * Email：langmanleguang@qq.com
 */
public class OriginData extends DataSupport {

    private String shuizhunxianluid;
    private String qianhoushibiaojifu;        //B1,F1,B2,F2
    private String qianhoushidianmingcheng;  //后视距，前视距
    private String qianhoushijuli;
    private String qianhoushichidushu;
    private String guancetime;
    private String shunxu;
    private String chengguobiaoid;

    public String getChengguobiaoid() {
        return chengguobiaoid;
    }

    public void setChengguobiaoid(String chengguobiaoid) {
        this.chengguobiaoid = chengguobiaoid;
    }

    public String getShuizhunxianluid() {
        return shuizhunxianluid;
    }

    public void setShuizhunxianluid(String shuizhunxianluid) {
        this.shuizhunxianluid = shuizhunxianluid;
    }

    public String getQianhoushibiaojifu() {
        return qianhoushibiaojifu;
    }

    public void setQianhoushibiaojifu(String qianhoushibiaojifu) {
        this.qianhoushibiaojifu = qianhoushibiaojifu;
    }

    public String getQianhoushidianmingcheng() {
        return qianhoushidianmingcheng;
    }

    public void setQianhoushidianmingcheng(String qianhoushidianmingcheng) {
        this.qianhoushidianmingcheng = qianhoushidianmingcheng;
    }

    public String getQianhoushijuli() {
        return qianhoushijuli;
    }

    public void setQianhoushijuli(String qianhoushijuli) {
        this.qianhoushijuli = qianhoushijuli;
    }

    public String getQianhoushichidushu() {
        return qianhoushichidushu;
    }

    public void setQianhoushichidushu(String qianhoushichidushu) {
        this.qianhoushichidushu = qianhoushichidushu;
    }

    public String getGuancetime() {
        return guancetime;
    }

    public void setGuancetime(String guancetime) {
        this.guancetime = guancetime;
    }

    public String getShunxu() {
        return shunxu;
    }

    public void setShunxu(String shunxu) {
        this.shunxu = shunxu;
    }
}
