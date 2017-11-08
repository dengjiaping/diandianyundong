package cn.ingenic.indroidsync.devicemanager;

import cn.ingenic.indroidsync.Column;
/**
 *  
 * */
public enum DeviceColumn implements Column {

    command(Integer.class),       
    data(String.class);
    

    private final Class<?> mClass;

    DeviceColumn(Class<?> c) {
        mClass = c;
    }

    @Override
    public String key() {
        return name();
    }

    @Override
    public Class<?> type() {
        return mClass;
    }

}
