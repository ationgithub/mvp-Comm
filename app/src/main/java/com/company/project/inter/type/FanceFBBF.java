package com.company.project.inter.type;

import com.company.project.common.Constants;
import com.company.project.inter.common.ObserverRegion;
import com.company.project.inter.common.ObserverType;
import com.company.project.mvp.model.entity.db.CezhanData;

import java.util.HashMap;
import java.util.Map;

import com.company.project.inter.common.ObserverRegion;
import com.company.project.mvp.model.entity.db.CezhanData;

/**
 * Created by Administrator on 2017/2/22.
 * 路线观测类型为往测aBFFB,返测aFBBF
 * 往测时：BFFB,FBBF...奇数站和偶数站交替
 * 返测时：FBBF,BFFB...奇数站和偶数站交替
 *
 */

@ObserverRegion(ORFINAL=Constants.TYPE1)
public class FanceFBBF implements ObserverType {

    private int[] arrayBFFB = {0, Constants.b1, Constants.f1, Constants.f2, Constants.b2};
    private int[] arrayFBBF = {0, Constants.f1, Constants.b1, Constants.b2, Constants.f2};
    private int[] arrayAudioFBBF = {0, Constants.AUDIO_NEXTB, Constants.AUDIO_NEXTB, Constants.AUDIO_NEXTF, 0};
    private int[] arrayAudioBFFB = {0, Constants.AUDIO_NEXTF, Constants.AUDIO_NEXTF, Constants.AUDIO_NEXTB, 0};//对于这个0是否会报错，后面再看
    int[] arraySequence = {0};
    int[] arrayAudio = {0};

    @Override
    public Map<ArrayType,Object> setObserVerOrder(CezhanData mCezhanData) {
        Map<ArrayType,Object> retMap = new HashMap<ArrayType, Object>();
        if (mCezhanData.getObserveType().equals(Constants.BFFB)) {
            //偶数站
            arraySequence = arrayBFFB;
            arrayAudio = arrayAudioBFFB;
        } else {
            //奇数站
            arraySequence = arrayFBBF;
            arrayAudio = arrayAudioFBBF;
        }

        retMap.put(ArrayType.ARRAYAUDIO,arrayAudio);
        retMap.put(ArrayType.ARRAYSEQUENCE,arraySequence);
        return retMap;
    }
}
