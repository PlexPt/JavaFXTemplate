package net.w2p.Shared.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/****
 * Warning:由于用到ibatis所以在普通没有引入mybatis的库的项目里面无法使用的。
 * ****/
public class WebTools {
    public static void calculatePages(int pageIndex,int pageSize,int total,PagerResult pager){

        int _totalPages=1;
        pager.setPageIndex(pageIndex);
        pager.setTotal(total);
        if(pager.getTotal()%pager.getPageSize()==0){
            _totalPages=(int)(pager.getTotal()/pager.getPageSize());
        }
        else{
//            _totalPages=pager.getTotal()/pager.getPageSize()+1;
            _totalPages= (int)((pager.getTotal()-(pager.getTotal()%pager.getPageSize()))/pager.getPageSize())+1;
        }
        pager.setTotalPages(_totalPages);
        if(pager.getPageIndex()<=0){
            pager.setPageIndex(1);
        }
        else if(pager.getPageIndex()>pager.getTotalPages()&&pager.getTotalPages()>1){
            pager.setPageIndex(pager.getTotalPages());
        }
        pager.setBegin_index((pager.getPageIndex()-1)*pager.getPageSize());
        pager.setLimit(pager.getPageSize());
    }

    public static Boolean getBoolean(String str_val,Boolean defaultVal){
        Boolean res=null;
        if(str_val==null){
            res= null;
        }
        else if(ValidateUtils.isEmpty(str_val)){
            res= null;
        }
        else if(StringUtils.equalsIgnoreCase("1",str_val.trim())||StringUtils.equalsIgnoreCase("true",str_val.trim())){
            res=true;
        }
        else{
            res=false;
        }
        if(res==null){
            return defaultVal;
        }
        return res;
    }

    public static PagerResult calculatePages(int pageIndex,int pageSize,int total){

        int _totalPages=1;
        PagerResult pager=new PagerResult();

        pager.setPageIndex(pageIndex);
        pager.setPageSize(pageSize);
        pager.setTotal(total);
        if(pager.getTotal()%pager.getPageSize()==0){
            _totalPages=(int)(pager.getTotal()/pager.getPageSize());
        }
        else{
//            _totalPages=pager.getTotal()/pager.getPageSize()+1;
            _totalPages= (int)((pager.getTotal()-(pager.getTotal()%pager.getPageSize()))/pager.getPageSize())+1;
        }
        pager.setTotalPages(_totalPages);
        if(pager.getPageIndex()<=0){
            pager.setPageIndex(1);
        }
        else if(pager.getPageIndex()>pager.getTotalPages()&&pager.getTotalPages()>1){
            pager.setPageIndex(pageIndex);
        }
        pager.setBegin_index((pager.getPageIndex() - 1) * pager.getPageSize());
        pager.setLimit(pager.getPageSize());



        return pager;
    }


    public static HashMap<String,String> getQueryParam(String URLQUERY) throws Exception{
        String _str=URLQUERY;
        HashMap<String,String> res=new HashMap<String, String>();
        if(URLQUERY==null||URLQUERY.trim().length()<1){}
        else{
            _str=URLQUERY.trim();
            _str= URLQUERY.replaceAll("&+","&");

            String[] str_res= _str.split("&+");
            if(str_res!=null&&str_res.length>0){
                for(String tmp_mix:str_res){

                    String[] t_arr_key_value=tmp_mix.split("=",2);
                    if(t_arr_key_value!=null&&t_arr_key_value.length>1){
                        String s1= t_arr_key_value[0];
                        String tKey= URLDecoder.decode(t_arr_key_value[0], "UTF-8");
                        String tValue="";
                        if(t_arr_key_value.length>1){
                            tValue=URLDecoder.decode(t_arr_key_value[1],"UTF-8");
                        }

                        res.put(tKey, tValue);
//                        LoggerHelper.info("处理querystr："+tKey+" value =:"+tValue);
                    }

                }}}

        return res;
    }


    /**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String md5(String str) {
        return MD5Util.getMD5String(str);

    }
    public static String md5(byte[] bytes) {
        //确定计算方法

        return MD5Util.getMD5String(bytes);

    }

    /***检查是不是以。。。为后缀的。**/
    public static boolean checkNameSuffix(String originUrlOrFileName,String[] suffixNames,Boolean ignoreCase){
        if(originUrlOrFileName==null||ValidateUtils.isEmpty(originUrlOrFileName)){
            return false;
        }
        if(suffixNames==null||suffixNames.length<=0){
            return false;
        }
        String nowUrl="";
        if(ignoreCase){
            nowUrl=originUrlOrFileName.toLowerCase();
        }
        else{
            nowUrl=originUrlOrFileName;
        }

        String realTmpSuffix="";
        String lastStr="";
        for(String item:suffixNames){
            if(item==null||ValidateUtils.isEmpty(item)){
                continue;
            }
            if(ignoreCase){
                realTmpSuffix=item.trim().toLowerCase();
            }
            else{
                realTmpSuffix=item.trim();
            }
            if(nowUrl.length()<realTmpSuffix.length()){
                return false;
            }
            lastStr=nowUrl.substring(nowUrl.length()-realTmpSuffix.length());
//            System.out.println(lastStr);
            if(lastStr.equals(realTmpSuffix)){
                return true;
            }
//            tmpRegxStr="[\\.]{1}"+realTmpSuffix+"$";
//            tmpRegxStr="\\.css$";
//            tmpPattern=Pattern.compile(tmpRegxStr);
//            Matcher matcher=tmpPattern.matcher(nowUrl);
//            if(matcher.matches()){
//                return true;
//            }
//            else{
//                continue;
//            }

        }

        return false;

    }

    /***
     * 检查当前url是否属于几个排除之后的url路径之下。
     * **/
    public static boolean checkBelongPathFolder(String originUrlOrFileName,String[] paths,Boolean ignoreCase){

        if(originUrlOrFileName==null||ValidateUtils.isEmpty(originUrlOrFileName)){
            return false;
        }
        if(paths==null||paths.length<=0){
            return false;
        }
        String nowUrl="";
        if(ignoreCase){
            nowUrl=originUrlOrFileName.toLowerCase();
        }
        else{
            nowUrl=originUrlOrFileName;
        }
        if(!nowUrl.substring(nowUrl.length()-1).equals("/")){
            nowUrl=nowUrl+"/";
        }

        String realTmpPathFolder="";
        String theUriPathPart="";
        for(String item:paths){
            if(item==null||ValidateUtils.isEmpty(item)){
                continue;
            }
            if(ignoreCase){
                realTmpPathFolder=item.trim().toLowerCase();
            }
            else{
                realTmpPathFolder=item.trim();
            }
            if(!realTmpPathFolder.substring(realTmpPathFolder.length()-1).equals("/")){
                realTmpPathFolder=realTmpPathFolder+"/";
            }
            if(nowUrl.length()<realTmpPathFolder.length()){
                continue;
            }
            if(nowUrl.equals(realTmpPathFolder)){
                return true;
            }
            theUriPathPart=nowUrl.substring(0,realTmpPathFolder.length());
//            System.out.println(lastStr);
            if(theUriPathPart.equals(realTmpPathFolder)){
                return true;
            }
//            tmpRegxStr="[\\.]{1}"+realTmpSuffix+"$";
//            tmpRegxStr="\\.css$";
//            tmpPattern=Pattern.compile(tmpRegxStr);
//            Matcher matcher=tmpPattern.matcher(nowUrl);
//            if(matcher.matches()){
//                return true;
//            }
//            else{
//                continue;
//            }

        }

        return false;

    }

    /***
     * 设定某个操作结果为错误。
     * **/
    public static void setOpError(OpResult opRes,String errorMsg){
        opRes.setState(false);

        opRes.setMessage(errorMsg);//.message=errorMsg;
        opRes.setMsg(errorMsg);//.msg=errorMsg;
        opRes.setResultMsg(errorMsg);
        opRes.setCode(-1);//.code=-1;
        opRes.setStateCode(-1);//.stateCode=-1;
        opRes.setResultCode(0);
    }
    /***
     * 设定某个操作结果的操作code。
     * **/
    public static void setOpCode(OpResult opRes,int code){


        opRes.setCode(code);//.code=-1;
        opRes.setStateCode(code);//.stateCode=-1;
    }
    /***
     * 设定某个操作结果为成功。
     * **/
    public static void setOpSuccess(OpResult opRes,String message){
        opRes.setState(true);

        opRes.setMessage(message);//.message=errorMsg;
        opRes.setMsg(message);//.msg=errorMsg;
        opRes.setCode(0);//.code=-1;
        opRes.setStateCode(0);//.stateCode=-1;
        opRes.setResultMsg(message);
    }


    /***
     * 设定某个操作结果为错误。
     * **/
    public static void setResultError(iResult opRes,String errorMsg){
        opRes.setState(false);

        opRes.setMessage(errorMsg);//.message=errorMsg;
        opRes.setMsg(errorMsg);//.msg=errorMsg;
        opRes.setCode(-1);//.code=-1;
        opRes.setStateCode(-1);//.stateCode=-1;
    }
    /***
     * 设定某个操作结果的操作code。
     * **/
    public static void setResultCode(iResult opRes,int code){


        opRes.setCode(code);//.code=-1;
        opRes.setStateCode(code);//.stateCode=-1;
    }
    /***
     * 设定某个操作结果为成功。
     * **/
    public static void setResultSuccess(iResult opRes,String message){
        opRes.setState(true);

        opRes.setMessage(message);//.message=errorMsg;
        opRes.setMsg(message);//.msg=errorMsg;
        opRes.setCode(0);//.code=-1;
        opRes.setStateCode(0);//.stateCode=-1;
    }

    /***
     * 设定某个操作结果为错误。
     * **/
    public static void setPagerError(PagerResult opRes,String errorMsg){
        opRes.setState(false);

        opRes.setMessage(errorMsg);//.message=errorMsg;
        opRes.setMsg(errorMsg);//.msg=errorMsg;
        opRes.setResultMsg(errorMsg);
        opRes.setCode(-1);//.code=-1;
        opRes.setStateCode(-1);//.stateCode=-1;
        opRes.setResultCode(0);
    }
    /***
     * 设定某个操作结果的操作code。
     * **/
    public static void setPagerCode(PagerResult opRes,int code){


        opRes.setCode(code);//.code=-1;
        opRes.setStateCode(code);//.stateCode=-1;
        opRes.setResultCode(code);
    }
    /***
     * 设定某个操作结果为成功。
     * **/
    public static void setPagerSuccess(PagerResult opRes,String message){
        opRes.setState(true);

        opRes.setMessage(message);//.message=errorMsg;
        opRes.setMsg(message);//.msg=errorMsg;
        opRes.setCode(0);//.code=-1;
        opRes.setStateCode(0);//.stateCode=-1;
        opRes.setResultCode(1);
        opRes.setResultMsg(message);

    }

    /***
     * 转换文件大小为文字显示：
     * 譬如，1024*3就是3kb了。
     * **/
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /***
     * 转换成为php的时间戳。
     * **/

    public static Long getPhpTimeStamp(Long time){
        return time/1000;
    }
    public static Long getPhpTimeStampByNow(){
        Long time=new Date().getTime();
        return time/1000;
    }
    public static String toStr(Object obj){
        if(obj==null){
            return "";
        }
        else{
            return obj.toString();
        }
    }
    public static String toStr(Timestamp obj){
        if(obj==null){
            return "";
        }
        else{
            return WebTools.phpTimeStampToDate(WebTools.getPhpTimeStamp(obj.getTime()));
        }
    }

    public static Date toDate(String originStr, String format)
    {
/* 486 */     Date mydate = new Date();
        try {
/* 488 */       SimpleDateFormat sdf = new SimpleDateFormat(format);
/* 489 */       String dstr = originStr;
/* 490 */       return sdf.parse(dstr);
        }
        catch (Exception e)
        {
/* 494 */       System.out.println("警告：StringUtil，错误原因：无法将【" + originStr + "】按照格式【“" + format + "”】转换成为日期！");
        }
/* 496 */     return mydate;
    }
    public static String toStr(ArrayList<Object> objs){
        if(objs==null){
            return "";
        }
        else{
            return StringUtils.join(objs, ",");
        }
    }

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<String> getStringArray(String str,String sep){
        String str_temp=str;
        ArrayList<String> res=new ArrayList<String>();
        if(str_temp==null){
            return res;
        }
        String[] arr_tmp=str_temp.split(sep);

        for(int i=0;i< arr_tmp.length;i++){
            if(ValidateUtils.isEmpty(arr_tmp[i])==false){
                res.add(arr_tmp[i]);
            }
        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<String> getStringArray(String str){
        return getStringArray(str, ",");
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Long> getLongArray(String str,String sep){
        ArrayList<String> strArr=getStringArray(str,sep);
        ArrayList<Long> res=new ArrayList<Long>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){
            if(ValidateUtils.isInteger(strArr.get(i))){
                res.add(Long.parseLong(strArr.get(i)));
            }

        }

        return res;
    }


    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Boolean> getBooleanArray(String str,String sep){
        ArrayList<String> strArr=getStringArray(str,sep);
        ArrayList<Boolean> res=new ArrayList<Boolean>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        String tmpStr=null;
        for(int i=0;i< strArr.size();i++){
            if(ValidateUtils.isEmpty(strArr.get(i))){
                res.add(false);
                continue;
            }
            tmpStr=strArr.get(i).toLowerCase().trim();
            if(tmpStr.equals("1")
                    ||tmpStr.equals("true")
                    ||tmpStr.equals("t")
                    ){
                res.add(true);
            }
            else{
                res.add(false);
            }

        }

        return res;
    }

    /***
     * 通过分隔符获取数组
     * ***/
    public static Boolean[] getBooleanArr(String str){

        ArrayList<Boolean> res=getBooleanArray(str, ",");
        Boolean[] res_arr=new Boolean[res.size()];
        for(int i=0;i< res.size();i++){
            res_arr[i]=res.get(i);

        }

        return res_arr;
    }

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Object> getObjectArray(String str,String sep){
        ArrayList<String> strArr=getStringArray(str, sep);
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){
            if(!ValidateUtils.isEmpty(strArr.get(i))){
                res.add(strArr.get(i));
            }

        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Long> getLongArray(String str){
        return getLongArray(str,",");
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Double> getDoubleArray(String str,String sep){
        ArrayList<String> strArr=getStringArray(str,sep);
        ArrayList<Double> res=new ArrayList<Double>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){
            if(ValidateUtils.isRealNumber(strArr.get(i))){
                res.add(Double.parseDouble(strArr.get(i)));
            }

        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Double> getDoubleArray(String str){
        return getDoubleArray(str, ",");
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Long> getLongArray(ArrayList<Object> ids){
        ArrayList<Object> strArr=ids;
        ArrayList<Long> res=new ArrayList<Long>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){
            if(ValidateUtils.isEmpty(strArr.get(i))||!ValidateUtils.isInteger(strArr.get(i).toString())){
                continue;
            }
            res.add(Long.parseLong(strArr.get(i).toString()));
        }

        return res;
    }

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Object> getObjectListByLongArr(ArrayList<Long> ids){
        ArrayList<Long> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }

    public static ArrayList<Object> getObjectListByIntegerArr(ArrayList<Integer> ids){
        ArrayList<Integer> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }

    public static ArrayList<Object> getObjectListByLongArr(Long[] ids){
        Long[] strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.length);
        if(strArr.length<=0){
            return res;
        }
        for(int i=0;i< strArr.length;i++){

            res.add(strArr[i]);
        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Object> getObjectListByBooleanArr(ArrayList<Boolean> ids){
        ArrayList<Boolean> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }

    public static ArrayList<Object> getObjectListByBooleanArr(List<Boolean> ids){
        List<Boolean> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Object> getObjectListByStringArr(ArrayList<String> ids){
        ArrayList<String> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }

    public static ArrayList<Object> getObjectListByStringArr(List<String> ids){
        List<String> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Object> getObjectListByLongArr(List<Long> ids){
        List<Long> strArr=ids;
        ArrayList<Object> res=new ArrayList<Object>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){

            res.add(strArr.get(i));
        }

        return res;
    }


    /***short 类型 begin****/

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Short> getShortArray(String str,String sep){
        ArrayList<String> strArr=getStringArray(str, sep);
        ArrayList<Short> res=new ArrayList<Short>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        for(int i=0;i< strArr.size();i++){
            res.add(Short.parseShort(strArr.get(i)));
        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Short> getShortArray(String str){
        return getShortArray(str,",");
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static Short[] getShortArr(String str){
        ArrayList<Short> nowlist=getShortArray(str, ",");
        Short[] tmp=new Short[nowlist.size()];
        nowlist.toArray(tmp);
        return tmp;
    }
    /***short 类型 end****/

    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Integer> getIntegerArray(String str,String sep){
        ArrayList<String> strArr=getStringArray(str, sep);
        ArrayList<Integer> res=new ArrayList<Integer>(strArr.size());
        if(strArr.size()<=0){
            return res;
        }
        String tmpRes="";
        for(int i=0;i< strArr.size();i++){
            tmpRes=strArr.get(i);
//            tmpRes.replaceAll("\"","");
            res.add(Integer.parseInt(tmpRes));
        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static ArrayList<Integer> getIntegerArray(String str){
        return getIntegerArray(str,",");
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static Integer[] getIntegerArr(String str){
        ArrayList<Integer> nowlist=getIntegerArray(str, ",");
        Integer[] tmp=new Integer[nowlist.size()];
        nowlist.toArray(tmp);
        return tmp;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static Long[] getLongArr(String str){
        ArrayList<Long> nowlist=getLongArray(str, ",");
        Long[] tmp=new Long[nowlist.size()];
        nowlist.toArray(tmp);
        return tmp;
    }
    /**
     * 获取没有前后括弧的字符串。
     * **/
    public static String getStrWithoutBrackets(String str){
        String res=str;
        if(res==null){
            return res;
        }
        res=res.trim();
        if(res.indexOf('{')==0){
            res=res.substring(1);
        }
        if(res.lastIndexOf('}')==res.length()-1){
            res=res.substring(0,res.length()-1);
        }

        return res;
    }
    /***
     * 通过分隔符获取数组
     * ***/
    public static Double[] getDoubleArr(String str){
        ArrayList<Double> nowlist=getDoubleArray(str, ",");
        Double[] tmp=new Double[nowlist.size()];
        nowlist.toArray(tmp);
        return tmp;
    }

    /***
     * 通过字符串变换成为json 数组
     * ***/
    public static JSONObject[] getJsonArr(String str){
        JSONObject[] res=new JSONObject[]{};
        if(ValidateUtils.isEmpty(str)){
            return res;
        }

        JSONArray arr= JSON.parseArray(str);
        JSONObject tmpObj=null;
        res=new JSONObject[arr.size()];
        for(int i=0;i<arr.size();i++){
            tmpObj=arr.getJSONObject(i);
            res[i]=tmpObj;
        }
        return res;
    }


    /***
     * 通过分隔符获取数组
     * ***/
    public static String[] getStringArr(String str){
        ArrayList<String> nowlist=getStringArray(str, ",");
        String[] tmp=new String[nowlist.size()];
        nowlist.toArray(tmp);
        return tmp;
    }    

    public static String phpTimeStampToDate(Long phpTimeStamp){
        Long javaTimeStamp=phpTimeStamp*1000;
        Date thisDate=new Date(javaTimeStamp);
        String res= DateUtils.dateToString(thisDate);
        return res;
    }
    public static String phpTimeStampToYYMMDDDate(Long phpTimeStamp){
        Long javaTimeStamp=phpTimeStamp*1000;
        Date thisDate=new Date(javaTimeStamp);
        String res= DateUtils.dateToString(thisDate);
        res=res.substring(0, 10);
        return res;
    }

    public static String toYYMMDDDate(Date date){

        Date thisDate=date;
        String res= DateUtils.dateToString(thisDate);
        res=res.substring(0, 10);
        return res;
    }

    public static String phpTimeStampToYYMMDate(Long phpTimeStamp){
        Long javaTimeStamp=phpTimeStamp*1000;
        Date thisDate=new Date(javaTimeStamp);
        String res= DateUtils.dateToString(thisDate);
        res=res.substring(0, 7);
        return res;
    }
    /**
     * 中文的年月日日期。
     * **/
    public static String phpTimeStampToYYMMDDDateByCn(Long phpTimeStamp){
        Long javaTimeStamp=phpTimeStamp*1000;
        Date thisDate=new Date(javaTimeStamp);
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy年M月d日");
        String ctime = formatter.format(thisDate);

        return ctime;
    }
    public static String phpTimeStampToYYMDHmDateByCn(Long phpTimeStamp){
        Long javaTimeStamp=phpTimeStamp*1000;
        Date thisDate=new Date(javaTimeStamp);
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy年M月d日H时m分");
        String ctime = formatter.format(thisDate);

        return ctime;
    }
    public static String Lpad(String str,int num,String pad){
        StringBuilder n_str=new StringBuilder();
        if(str==null)
            n_str= new StringBuilder("  ");
        for(int i=str.length();i <num;i++){
            n_str.append(pad);
        }
        //

//        n_str=pad+n_str;
        n_str.append(str);
        return n_str.toString();
    }


    public static Long toLong(Object obj){
        if(obj==null){
            return null;
        }
        if(ValidateUtils.isInteger(obj.toString())){
            Long tmp=Long.parseLong(obj.toString());
            return tmp;
        }
        return null;
    }
    public static Integer toInteger(Object obj){
        if(obj==null){
            return null;
        }
        if(ValidateUtils.isInteger(obj.toString())){
            Integer tmp=Integer.parseInt(obj.toString());
            return tmp;
        }
        return null;
    }
    public static Integer toInteger(Object obj,Integer defaultVal){
        if(obj==null){
            return defaultVal;
        }
        if(ValidateUtils.isInteger(obj.toString())){
            Integer tmp=Integer.parseInt(obj.toString());
            return tmp;
        }
        return null;
    }
    public static Short toShort(Object obj){
        if(obj==null){
            return null;
        }
        if(ValidateUtils.isInteger(obj.toString())){
            Short tmp=Short.parseShort(obj.toString());
            return tmp;
        }
        return null;
    }
    public static Timestamp toTimeStamp(Object obj){
        if(obj==null){
            return null;
        }

        if(ValidateUtils.isDateTime(obj.toString())){
            Date theDate=DateUtils.stringToDate(obj.toString().trim());
            Timestamp theRes=new Timestamp(theDate.getTime());
            return theRes;
        }
        else if(ValidateUtils.isInteger(obj.toString())){
            Timestamp theRes=new Timestamp(WebTools.toLong(obj.toString()));
            return theRes;
        }
        return null;
    }


    public static Boolean toBoolean(Object obj){
        if(obj==null){
            return Boolean.FALSE;
        }

        String str=obj.toString().trim().toLowerCase();

        if(str.equals("1")||str.equals("true")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Byte[] toBlob(Object obj){
        if(obj==null){
            return new Byte[]{};
        }

        return new Byte[]{};

    }
    public static Long toLong(Object obj,Long defaultVal){
        if(obj==null){
            return defaultVal;
        }
        if(ValidateUtils.isInteger(obj.toString())){
            Long tmp=Long.parseLong(obj.toString());
            return tmp;
        }
        return defaultVal;
    }

    public static Double toDouble(Object obj,Double defaultVal){
        if(obj==null){
            return defaultVal;
        }
        if(ValidateUtils.isRealNumber(obj.toString())){
            Double tmp=Double.parseDouble(obj.toString());
            return tmp;
        }
        return defaultVal;
    }

    public static Double toDouble(Object obj){
        if(obj==null){
            return null;
        }
        if(ValidateUtils.isRealNumber(obj.toString())){
            Double tmp=Double.parseDouble(obj.toString());
            return tmp;
        }
        return null;
    }


    public static JSONObject toJson(Object obj,JSONObject defaultVal){
        if(obj==null){
            return defaultVal;
        }
        if(!ValidateUtils.isEmpty(obj.toString())){
            JSONObject now=JSONObject.parseObject(obj.toString());
            return now;
        }
        return defaultVal;
    }
    public static JSONObject toJson(Object obj){

        return toJson(obj,new JSONObject());
    }


    /**
     * 注意，这个方法用于将textarea这些普通字符串转换成为html进行显示。
     * **/
    public static String TextToHtml(String sourcestr)
    {
        int strlen;
        String restring="", destr = "";
        String realSource="";
        if(ValidateUtils.isEmpty(sourcestr)){
            return realSource;
        }
//        System.out.println("first:"+sourcestr);
        realSource=sourcestr.replaceAll("\\\\n","\n").replace("\\n","\n");
//        System.out.println("middle:"+realSource);
        strlen = realSource.length();
        for     (int i=0; i<strlen;     i++)
        {
            char ch=realSource.charAt(i);
            switch (ch)
            {
                case '<':
                    destr = "&lt;";
                    break;
                case '>':
                    destr = "&gt;";
                    break;
                case '\"':
                    destr = "&quot;";
                    break;
                case '&':
                    destr = "&amp;";
                    break;
                case '\r':
                    destr = "<br/>";
                    break;
                case '\n':
                    destr="<br/>";
                    break;
                case 32:
                    destr = "&nbsp;";
                    break;
                default :
                    destr = "" + ch;
                    break;
            }
            restring = restring + destr;
        }
        restring=restring.replaceAll("<br/><br/>","<br/>");
//        System.out.println("result:"+restring);

        return "" + restring;
    }

    /**
     * 比较两个整数。
     * **/
    public static Boolean compareTwoInteger(Object long1,Object long2){
        if(long1==null||long2==null){
            return false;
        }
        if(!ValidateUtils.isInteger(long1+"")||!ValidateUtils.isInteger(long2+"")){
            return false;
        }
        Long int1=WebTools.toLong(long1);
        Long int2=WebTools.toLong(long2);

        return int1.equals(int2);

    }

    public static Boolean compareTwoString(Object str1,Object str2){
        if(str1==null||str2==null){
            return false;
        }

        String s1=str1.toString();
        //YC_888_184+	YC_888_239+	YC_888_294+	YC_888_349+	YC_888_404+YC_888_459+	YC_888_514+YC_888_569+YC_888_624+YC_888_679+YC_888_734+YC_888_789+YC_888_844+YC_888_899+YC_888_954+YC_888_1308+	YC_888_1309+	YC_888_1310+	YC_888_1311+	YC_888_1312+	YC_888_1313+	YC_888_1314+	YC_888_1315+	YC_888_1316+	YC_888_1317+	YC_888_1318+	YC_888_1319+	YC_888_1320+	YC_888_1321+	YC_888_1322
        String s2=str2.toString(); String t2="YC_888_184,YC_888_239,YC_888_294,YC_888_349,YC_888_404,YC_888_459,YC_888_514,YC_888_569,YC_888_624,YC_888_679,YC_888_734,YC_888_789,YC_888_844,YC_888_899,YC_888_954,YC_888_1308,YC_888_1309,YC_888_1310,YC_888_1311,YC_888_1312,YC_888_1313,YC_888_1314,YC_888_1315,YC_888_1316,YC_888_1317,YC_888_1318,YC_888_1319,YC_888_1320,YC_888_1321,YC_888_1322";

        String t3="YC_888_184," +
                "YC_888_239," +
                "YC_888_294," +
                "YC_888_349," +
                "YC_888_404," +
                "YC_888_459," +
                "YC_888_514," +
                "YC_888_569," +
                "YC_888_624," +
                "YC_888_679," +
                "YC_888_734," +
                "YC_888_789," +
                "YC_888_844," +
                "YC_888_899," +
                "YC_888_954," +
                "YC_888_1308," +
                "YC_888_1309," +
                "YC_888_1310," +
                "YC_888_1311," +
                "YC_888_1312," +
                "YC_888_1313," +
                "YC_888_1314," +
                "YC_888_1315," +
                "YC_888_1316," +
                "YC_888_1317," +
                "YC_888_1318," +
                "YC_888_1319," +
                "YC_888_1320," +
                "YC_888_1321," +
                "YC_888_1322";

        return s1.equals(s2);

    }

    public static Double toFormatDouble(Double dst,String format){
        String fmt = String.format("%.2f", dst);
        return (Double.parseDouble(fmt));

    }



    /**
     * 获取来访者的主机名称
     * @param ip
     * @return
     */
    public static String getHostName(String ip){
        InetAddress inet;
        try {
            inet = InetAddress.getByName(ip);
            return inet.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 命令获取mac地址
     * @param cmd
     * @return
     */
    private static String callCmd(String[] cmd) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);
            while ((line = br.readLine ()) != null) {
                result += line;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     *
     *
     *
     * @param cmd
     *            第一个命令
     *
     * @param another
     *            第二个命令
     *
     * @return 第二个命令的执行结果
     *
     */

    private static String callCmd(String[] cmd,String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); // 已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);
            while ((line = br.readLine ()) != null) {
                result += line;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     *
     *
     * @param ip
     *            目标ip,一般在局域网内
     *
     * @param sourceString
     *            命令处理的结果字符串
     *
     * @param macSeparator
     *            mac分隔符号
     *
     * @return mac地址，用上面的分隔符号表示
     *
     */

    private static String filterMacAddress(final String ip, final String sourceString,final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while(matcher.find()){
            result = matcher.group(1);
            if(sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; // 如果有多个IP,只匹配本IP对应的Mac.
            }
        }
        return result;
    }

    /**
     * @param ip
     *            目标ip
     * @return Mac Address
     *
     */

    private static String getMacInWindows(final String ip){
        String result = "";
        String[] cmd = {"cmd","/c","ping " + ip};
        String[] another = {"cmd","/c","arp -a"};
        String cmdResult = callCmd(cmd, another);
        result = filterMacAddress(ip,cmdResult,"-");
        return result;
    }
    /**
     *
     * @param ip
     *            目标ip
     * @return Mac Address
     *
     */
    private static String getMacInLinux(final String ip){
        String result = "";
        String[] cmd = {"/bin/sh","-c","ping " +  ip + " -c 2 && arp -a" };
        String cmdResult = callCmd(cmd);
        result = filterMacAddress(ip,cmdResult,":");
        return result;
    }

    /**
     * 获取MAC地址
     *
     * @return 返回MAC地址
     */
    public static String getMacAddress(String ip){
        String macAddress = "";
        macAddress = getMacInWindows(ip).trim();
        if(macAddress==null||"".equals(macAddress)){
            macAddress = getMacInLinux(ip).trim();
        }
        return macAddress;
    }

    public static String cutStr(String originStr,Integer length,String suffix){
        if(ValidateUtils.isEmpty(originStr)){
            return "";
        }
        if(originStr.length()>length){

            return originStr.substring(0,length-1)+suffix;
        }

        return originStr;
    }

    public static byte[] fetchRemoteImage(String imgUrl) throws Exception{

        //new一个URL对象
        URL url = new URL(imgUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        // conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream=null;
        ByteArrayOutputStream outStream = null;
        byte[] data=null;
        try{
            inStream = conn.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while( (len=inStream.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inStream.close();
            outStream.close();

            //把outStream里的数据写入内存
            data=outStream.toByteArray();
        }
        catch (Exception ed){
            ed.printStackTrace();
        }
        finally {
            if(inStream!=null){inStream.close();}
            if(outStream!=null){outStream.close();}
        }

        //得到图片的二进制数据，以二进制封装得到数据，具有通用性

        return data;
    }


    /***
     * 采用php的时间间隔，将其翻译成为字符串【中文】
     * **/
    public static String translateTimeSpanToCn(Long phpTimeSepStamp){
        String res="";
        if(phpTimeSepStamp==null){
            return "0秒";
        }
        if(WebTools.compareTwoInteger(phpTimeSepStamp,0)){
            return "0秒";
        }

        Long minute_secs=60L;
        Long hour_secs=60*minute_secs;
        Long day_secs=24*hour_secs;
        Long mod_day= getModTimes (phpTimeSepStamp,day_secs);//phpTimeSepStamp-phpTimeSepStamp % day_secs)/day_secs;
        Long mod_hour= getModTimes (phpTimeSepStamp-day_secs*mod_day,hour_secs);
        Long mod_minute=getModTimes(phpTimeSepStamp-day_secs*mod_day-mod_hour*hour_secs,minute_secs);
        Long mod_sec=(phpTimeSepStamp-day_secs*mod_day-mod_hour*hour_secs-mod_minute*minute_secs);

        ArrayList<String> strs=new ArrayList<String>();

        if(mod_day>0L){strs.add(mod_day+"天");}
        if(mod_hour>0L){strs.add(mod_hour+"小时");}
        if(mod_minute>0L){strs.add(mod_minute+"分钟");}
        if(mod_sec>0L){strs.add(mod_sec+"秒钟");}

        res=StringUtils.join(strs,"");
        return res;
    }

    private static Long getModTimes(Long origin,Long number2){
        Long mod_day=(origin-origin % number2)/number2;
        return mod_day;
    }

    public static String formatNumber(Double number){

        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00");

        String res=df.format(number);

        return res;
    }

    /****\
     * 将日期转换为php时间戳
     *
     */
    public static String getDateToPhpTime(String res)throws Exception{
        String time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(res);
        long ts = date.getTime();
        time = String.valueOf(ts);

        return time;

    }
    /****\
     * 将日期转换为php时间戳
     *
     */
    public static String getDateToPhpTimes(String res)throws Exception{
        String time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = simpleDateFormat.parse(res);
        long ts = date.getTime();
        time = String.valueOf(ts);

        return time;

    }
    /**
     * 获取当日开始以及结束时间的时间戳(当前时间毫秒值)
     */
    public static HashMap<String,Long> getTimeStamp(Long current){
        HashMap<String, Long> stamp = new HashMap<String, Long>();
        //获取当天的起始时间戳
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();

        //当日凌晨毫秒值
        long twelve=zero+24*60*60*1000-1;
//        Long begin = (Long)new Timestamp(zero);
//        Timestamp end = new Timestamp(twelve);
        //将其装换为php时间戳
        stamp.put("begin",zero);
        stamp.put("end",twelve);


        return stamp;

    }
    /***
     * 获取指定日期的开始时间和结束时间(毫秒值)
     *
     */
    public static HashMap<String,Long> getBeginAndEndTime(String time) throws Exception{
        HashMap<String, Long> map = new HashMap<String,Long>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date start = format.parse(time + " 00:00:00");
        Date end = format.parse(time + " 23:59:59");
        long begin = start.getTime();
        long endTime = end.getTime();
        map.put("begin",begin);
        map.put("end",endTime);
        return map;
    }

    /***
     * 根据当天时间获取前段时间的日期
     * @return
     */
    public static String getStatetime(Integer t){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -t);
        Date befor_date = c.getTime();
        String pre_date = sdf.format(befor_date);
        return pre_date;
    }

    /***
     * 将时间戳转化为日期
     * @return
     */
    public static String transformDate(Long time){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(time);
        return date;

    }
    /***
     * 将Date日期转换为毫秒值
     */
    public static  Long transformMillionSeconds(Date date) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format.format(date);
        long time = format.parse(s).getTime();
        return time;
    }


    /***
     * 翻译相关模板。
     * **/
    public static String translateTpl(String templateStr,HashMap<String,Object> obj){
        String res="";
        if(ValidateUtils.isEmpty(templateStr)){
            return "";
        }


        Iterator iter = obj.entrySet().iterator();
        String tempTpl=templateStr;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            String _key=key.toString();
            String _val=(val==null?"_NULL_":val.toString());
            //System.out.println(key+" : "+val);
            tempTpl=tempTpl.replaceAll("\\{"+key+"\\}",_val);
        }
        return tempTpl;
    }

    public static String formatToSimpleColor(String colorStr){
        if(ValidateUtils.isEmpty(colorStr)){
            return "";
        }
        String rStr=colorStr.trim();
        rStr=rStr.replace('#',' ');
        return "#"+rStr.trim();
    }
    public static String toPlainText(String originStr){
        if(ValidateUtils.isEmpty(originStr)){
            return "";
        }
        String res = originStr.replaceAll("(\r\n|\r|\n|\n\r|\t|\\s*)", "");
        res= StringUtils.trim(res);

        return res;
    }



    /***
     * 获得已经消失了的整数。
     * @param originNumbers 原本的数组。
     * @param currentNumbers 新来的数组。
     * ***/
    public static ArrayList<Long> findDisappearNumbers(ArrayList<Long> originNumbers,ArrayList<Long> currentNumbers){
        ArrayList<Long> needDel=new ArrayList<>();
        HashMap<Long,Boolean> map_currentIds=new HashMap<>();
        if(originNumbers.size()>0){

            for(Long tmpLong :currentNumbers){
                if(tmpLong!=null){
                    map_currentIds.put(tmpLong, true);
                }
            }
        }

        for(Long tModel:originNumbers){
            if(!map_currentIds.containsKey(tModel)){
                if(tModel!=null){
                    needDel.add(tModel);
                }
                else{
                    String t2="";
                }

            }
        }
        return needDel;
    }

    public static ArrayList<Integer> findDisappearIntListNumbers(ArrayList<Integer> originNumbers,ArrayList<Integer> currentNumbers){
        ArrayList<Integer> needDel=new ArrayList<>();
        HashMap<Integer,Boolean> map_currentIds=new HashMap<>();
        if(originNumbers.size()>0){

            for(Integer tmpInteger :currentNumbers){
                if(tmpInteger!=null){
                    map_currentIds.put(tmpInteger, true);
                }
            }
        }

        for(Integer tModel:originNumbers){
            if(!map_currentIds.containsKey(tModel)){
                if(tModel!=null){
                    needDel.add(tModel);
                }
                else{
                    String t2="";
                }

            }
        }
        return needDel;
    }

    public static String toJsonString(Object obj){
        return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String fetchRemotePage(String str_url){
//        try {
//            //TODO 这里的ip 地址一定不能使localhost 一定要是电脑的或者是正式ip地址.
//            //如果写成了localhost，那么就会报错java.net.ConnectException: localhost/127.0.0.1:8080 - Connection refused
////            URL url = new URL("http://localhost:8080/tomcat.png");
//            URL url = new URL("http://192.168.1.106:8080/tomcat.png");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setConnectTimeout(5000);
//            //Sets the flag indicating whether this URLConnection allows input. It cannot be set after the connection is established.
//            httpURLConnection.setDoInput(true);
//
//            InputStream in = null;
//            FileOutputStream fos = null;
//            if (httpURLConnection.getResponseCode() == 200) {
//                in = httpURLConnection.getInputStream();
//                //一定不能直接在FileOutputStream里面写文件名，需要添加路径
//                //错误的写法：fos = new FileOutputStream("a.bmp");
//                //下面存储到内部存储的私有的cache目录里面，注意了生成的文件名是cachea.bmp
//                fos = new FileOutputStream(getCacheDir().getPath()+"a.bmp");
//                byte[] arr = new byte[1024];
//                int len = 0;
//                while ((len = in.read(arr)) != -1) {
//                    fos.write(arr, 0, len);
//                }
//                in.close();
//                fos.close();
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            //建立连接
            URL url = new URL(str_url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setReadTimeout(50000);
            httpUrlConn.setConnectTimeout(5000);
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //获取输入流
            InputStream input = null;
            httpUrlConn.connect();
            int respCode=httpUrlConn.getResponseCode();
            if (
                    httpUrlConn.getResponseCode()==HttpURLConnection.HTTP_ACCEPTED
                    ||httpUrlConn.getResponseCode()==HttpURLConnection.HTTP_CREATED
                    ||httpUrlConn.getResponseCode()==HttpURLConnection.HTTP_OK

                    ) {
                //获取输入流
                input = httpUrlConn.getInputStream();
            }
            else{
                input = httpUrlConn.getErrorStream();
            }

            //将字节输入流转换为字符输入流
            InputStreamReader read = new InputStreamReader(input, "utf-8");
            //为字符输入流添加缓冲
            BufferedReader br = new BufferedReader(read);
            // 读取返回结果
            String data = br.readLine();
            while(data!=null)  {
                System.out.println(data);
                data=br.readLine();
            }
            // 释放资源
            br.close();
            read.close();
            input.close();
            httpUrlConn.disconnect();

            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }



    /**
     * 根据生日计算当前周岁数
     */
    public static int getCurrentAge(Date birthday) {
        // 当前时间
        Calendar curr = Calendar.getInstance();
        // 生日
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);
        // 年龄 = 当前年 - 出生年
        int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        if (age <= 0) {
            return 0;
        }
        // 如果当前月份小于出生月份: age-1
        // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
        int currMonth = curr.get(Calendar.MONTH);
        int currDay = curr.get(Calendar.DAY_OF_MONTH);
        int bornMonth = born.get(Calendar.MONTH);
        int bornDay = born.get(Calendar.DAY_OF_MONTH);
        if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
            age--;
        }
        return age < 0 ? 0 : age;
    }

    /***
     *
     *
     * 规范化url地址。
     * 将烦人的重复斜杆干掉。
     *
     * ***/
    public static String normalizeUrl(String url){
        String res=url;
        boolean containHttps=false;
        boolean containHttpHeader=false;
        if(res.indexOf("https://")==0){
            containHttps=true;
            res=res.replace("https://","");
        }
        else if(res.indexOf("http://")==0){
            containHttpHeader=true;
            res=res.replace("http://","");
        }
        res= res.replaceAll("\\.+",".").replaceAll("\\/+","/");
        if(containHttps){
            return "https://"+res;
        }
        else if(containHttpHeader){
            return "http://"+res;
        }
        else{
            return res;
        }
    }

    /***
     *
     * 规范化后缀名，前面带点。
     *
     * **/
    public static String normalizeFileExt(String fileExt){
        if(ValidateUtils.isEmpty(fileExt)){
            return "";
        }
        String realExt=fileExt.trim();

        realExt=realExt.replaceAll("\\.+",".");
        if(realExt.indexOf('.')==0){
            return realExt;
        }
        else{
            return String.format(".%s",realExt);
        }
    }



    /****
     *
     * 获取随机中文名称
     *
     * ****/
    public static String getRndCnName() {
        Random random = new Random();
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
                "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
                "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
                "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
                "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
        String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
        String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个随机的姓氏
        int i = random.nextInt(3);//可以根据这个数设置产生的男女比例
        if(i==2){
            int j = random.nextInt(girl.length()-2);
            if (j % 2 == 0) {
                name = "女-" + name + girl.substring(j, j + 2);
            } else {
                name = "女-" + name + girl.substring(j, j + 1);
            }

        }
        else{
            int j = random.nextInt(girl.length()-2);
            if (j % 2 == 0) {
                name = "男-" + name + boy.substring(j, j + 2);
            } else {
                name = "男-" + name + boy.substring(j, j + 1);
            }

        }

        return name;
    }

    public static String getString(String defaultVal,String... args){
        if(args==null||args.length<=0){
            return defaultVal;
        }

        String realStr=null;

        for(String item:args){
            if(!ValidateUtils.isEmpty(item)){
                realStr=item;
                break;

            }
        }

        if(ValidateUtils.isEmpty(realStr)){
            return defaultVal;
        }

        return realStr;
    }

    public static String trim(String s){
        return trim(s,null);
    }
    public static String trim(String s,String defaultVal) {
        if(s==null){
            return defaultVal;
        }
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray())
            if (' ' != ch)
                sb.append(ch);
        s = sb.toString();

        return s.replaceAll("&nbsp;", "").replaceAll(" ", "").replaceAll("　", "").replaceAll("\t", "").replaceAll("\n", "");
    }

    public static String FindPhoneNumber(String text) {
        if (text == null)
            return FindQqOrWxNumber("");
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[35689]\\d{9})|(?:861[35689]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(text);
        //StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            text = text.replace(matcher.group(), matcher.group().substring(0, 3) + "********");
        }
        return FindQqOrWxNumber(text);
    }

    /**
     *
     * 过滤qq号微信号
     */
    public static String FindQqOrWxNumber(String text) {
        if (text == null)
            return "";
        Pattern pattern = Pattern.compile("(微信|QQ|qq|weixin|vx|v信|微xin|1[0-9]{10}|[a-zA-Z0-9\\-\\_]{6,16}|[0-9]\n" +
                "{6,11})+");
        StringBuilder finderStr=new StringBuilder("");
        for(char c :text.toLowerCase().toCharArray()){
            //--去除中间的空格。
            if(c==' '||c=='\n'||c=='\r'){
                continue;
            }
            finderStr.append(c);
        }
        Matcher matcher = pattern.matcher(finderStr.toString());
        //StringBuffer bf = new StringBuffer(64);
        String str2=finderStr.toString();
        while (matcher.find()) {
            String str=matcher.group();
            str2 = str2.replaceAll(str, "******");
        }
        if(str2.equals(finderStr.toString())){

            return text;
        }
        else{
            text=str2;
        }
        return text;

    }
}



