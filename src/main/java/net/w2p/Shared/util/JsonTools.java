package net.w2p.Shared.util;


import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class JsonTools {
public interface ParseHandler{
    public void handle(JSONObject jsonObject, String currentKey, Object currentVal);
}

/***
 *
 * 对json object的相关字段进行变更。
 *
 * **/
public static JSONObject parse(Object originObject, ParseHandler... handlers){
    JSONObject originJson=new JSONObject();
    if(handlers==null){
//        return originJson;
        return originJson;
    }

    originJson=JSONObject.parseObject(JSONObject.toJSONString(originObject));

    //然后用Iterator迭代器遍历取值，建议用反射机制解析到封装好的对象中

    Set<String> iterator = originJson.keySet();
    for(String key : iterator){
        Object valObj=originJson.get(key);//这里可以根据实际类型去获取
        for(ParseHandler handler:handlers){
            handler.handle(originJson,key,valObj);
        }
    }


    return originJson;


}



}
