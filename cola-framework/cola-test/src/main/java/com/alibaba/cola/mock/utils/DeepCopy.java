package com.alibaba.cola.mock.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author shawnzhan.zxy
 * @since 2018/11/12
 */
public class DeepCopy {

    /**
     * 深度复制
     *  主要是避免引用引起的数据串改问题
     * @param o
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static<T> T from(T o) throws IOException, ClassNotFoundException {
        //未序列化,则返回原值
        if(!(o instanceof Serializable)){
            return o;
        }
        try {
            //		//先序列化，写入到流里
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(o);
            //然后反序列化，从流里读取出来，即完成复制
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            return (T)oi.readObject();
        }catch (Exception e){
            return o;
        }
    }

    public static <T> T[] from(T[] os) throws IOException, ClassNotFoundException {
        if(os == null){
            return null;
        }
        if(os.length == 0){
            return os;
        }
        T[] cloneT = (T[])new Object[os.length];
        for(int i = 0; i < os.length; i++){
            cloneT[i] = from(os[i]);
        }
        return cloneT;
    }
}
