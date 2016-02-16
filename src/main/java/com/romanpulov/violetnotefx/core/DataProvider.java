package com.romanpulov.violetnotefx.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by rpulov on 16.02.2016.
 */
public class DataProvider {
    private Map<Object, Object> data = new HashMap<>();

    public Object getValue(Object key) {
        return data.get(key);
    }

    public void setValue(Object key, Object value) {
        data.put(key, value);
    }
}
