package com.company.project.inter.common;

import com.company.project.mvp.model.entity.db.CezhanData;

import java.util.Map;

import com.company.project.mvp.model.entity.db.CezhanData;

/**
 * Created by Administrator on 2017/2/21.
 * 这个是策略模式的接口，所有的策略均要实现此接口
 */
public interface ObserverType {

    enum ArrayType {
        ARRAYSEQUENCE,ARRAYAUDIO
    }
    /**
     * 策略方法
     */

    public Map<ArrayType, Object> setObserVerOrder(CezhanData mCezhanData);

}
