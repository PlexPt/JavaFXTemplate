package net.w2p.Shared.common;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DGDL-08 on 2017/3/29.
 */
public class EnumItemValuePair implements Serializable {
    public EnumItemValuePair(){}
    public EnumItemValuePair(int key,String value){
        this.key=key;
        this.value=value;
    }
    private Integer key;



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
