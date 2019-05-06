package com.company.project.inter.common;

import android.content.Context;

import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import com.company.project.mvp.model.entity.db.YusheshuizhunxianData;

/**
 * Created by Administrator on 2017/2/23.
 * 使用一个标准的简单工厂来改进策略模式得到不同路线观测类型下测量顺序的不同
 * 使用注解+反射动态决定使用哪种策略模式
 *
 */
public class ObserverFactory {

    static ObserverType observerType;
    private ObserverFactory(){}

//    public static ObserverType createObserverType(YusheshuizhunxianData mYusheshuizhunxianData){
//
//        if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE1)){
//                return new FanceFBBF();
//            }else if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE2)){
//                return new DoubleBFFB();
//            }else if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE3)){
//                return new SingleBFFB();
//            }else if(mYusheshuizhunxianData.getObserveType().equals(Constants.TYPE4)){
//                return new BBFFObserverType();
//        }
//        return observerType;
//    }

    //这里是一个常量，表示我们扫描策略的包
    private static final String OBSERVER_YPYE_PACKAGE = "com.company.project.inter.type";
    //我们加载策略时的类加载器，我们任何类运行时信息必须来自该类加载器
    private ClassLoader classLoader=getClass().getClassLoader();
    //策略列表
    private List<Class<? extends ObserverType>> observerTypeList;

    public ObserverType createObserverType(YusheshuizhunxianData mYusheshuizhunxianData){

        //在策略列表查找策略
        for (Class<? extends ObserverType> clazz : observerTypeList) {
            ObserverRegion observerRegion = handleAnnotation(clazz);//获取该策略的注解
            //判断路线观测类型是哪一种
            if (mYusheshuizhunxianData.getObserveType().equals(observerRegion.ORFINAL())) {
                try {
                    //是的话我们返回一个当前策略的实例
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("策略获得失败");
                }
            }
        }
        throw new RuntimeException("策略获得失败");

    }

    //处理注解，我们传入一个策略类，返回它的注解
    private ObserverRegion handleAnnotation(Class<? extends ObserverType> clazz){
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i] instanceof ObserverRegion) {
                return (ObserverRegion) annotations[i];
            }
        }
        return null;
    }

    //单例
    private ObserverFactory(Context context){
        init(context);
    }

    //在工厂初始化时要初始化策略列表
    private void init(Context context){
        observerTypeList = new ArrayList<Class<? extends ObserverType>>();
        //获取到包下所有的class文件
        List<String> className = getClassName(OBSERVER_YPYE_PACKAGE, context);
        Class<ObserverType> observerTypeClazz = null;
        try {
            observerTypeClazz = (Class<ObserverType>) classLoader.loadClass(ObserverType.class.getName());//使用相同的加载器加载策略接口
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("未找到策略接口");
        }
        for (int i = 0; i < className.size(); i++) {
            try {
                //载入包下的类
                Class<?> clazz = classLoader.loadClass(className.get(i).replace(".class", ""));
                //判断是否是ObserverType的实现类并且不是ObserverType它本身，满足的话加入到策略列表
                if (ObserverType.class.isAssignableFrom(clazz) && clazz != observerTypeClazz) {
                    observerTypeList.add((Class<? extends ObserverType>) clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //获取扫描的包下面所有的class文件
    public List<String > getClassName(String packageName,Context context){
        List<String >classNameList=new ArrayList<String >();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = (String) enumeration.nextElement();

                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  classNameList;
    }



    public static ObserverFactory getInstance(Context context){
//        return ObserVerTypeFactoryInstance.instance;
        return new ObserverFactory(context);
    }

//    private static class ObserVerTypeFactoryInstance{
//
//        private static ObserverFactory instance = new ObserverFactory();
//    }

}
