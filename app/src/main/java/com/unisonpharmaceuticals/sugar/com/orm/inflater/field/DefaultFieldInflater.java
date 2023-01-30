package com.unisonpharmaceuticals.sugar.com.orm.inflater.field;

import android.database.Cursor;
import com.unisonpharmaceuticals.sugar.com.orm.util.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Created by Łukasz Wesołowski on 03.08.2016.
 */
public class DefaultFieldInflater extends FieldInflater {

    public DefaultFieldInflater(Field field, Cursor cursor, Object object, Class<?> fieldType) {
        super(field, cursor, object, fieldType);
    }

    @Override
    public void inflate() {
        ReflectionUtil.setFieldValueFromCursor(cursor, field, object);
    }
}
