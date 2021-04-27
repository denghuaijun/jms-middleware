package com.jms.kafka.config;

import com.jms.kafka.entity.Company;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 自定义对象序列化器
 */
public class MySerializer implements Serializer<Company> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Company data) {
        try {
            if (data == null){
                return new byte[0];
            }
            else{
                byte[] name,address;
                if (data.getName() !=null){
                    name=data.getName().getBytes(encoding);
                }else {
                    name=new byte[0];
                }
                if (data.getAddress() !=null){
                    address=data.getAddress().getBytes(encoding);
                }else {
                    address=new byte[0];
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(4+4+name.length+address.length);
                byteBuffer.putInt(name.length);
                byteBuffer.put(name);
                byteBuffer.putInt(address.length);
                byteBuffer.put(address);
                return byteBuffer.array();
            }
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + encoding);
        }
    }

    @Override
    public void close() {

    }
}
