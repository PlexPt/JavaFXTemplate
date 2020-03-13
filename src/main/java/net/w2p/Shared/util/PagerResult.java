package net.w2p.Shared.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagerResult<T> extends iResult implements Serializable {

    private List<T> data= new ArrayList();

    public int pageIndex=1;
    public int pageSize=20;
    public int total=0;
    public int totalPages=0;

    public Integer resultCode=1;


    public String resultMsg="";

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getBegin_index() {
        return begin_index;
    }

    public void setBegin_index(int begin_index) {
        this.begin_index = begin_index;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private int begin_index=0;
    private int limit=0;

    public PagerResult<T> setError(String msg){
        this.message=msg;
        this.msg=msg;
        this.code=-1;
        this.state=false;
        this.stateCode=-1;
        this.resultCode=0;
        this.resultMsg=msg;
        return this;
    }
    public PagerResult(){

    }
    public PagerResult(boolean trueOrFalse,String message){
        if(!trueOrFalse){
            setError(message);
        }
        else{
            setSuccess(message);
        }
    }

    public void setFrom(boolean trueOrFalse,String message){
        if(trueOrFalse){
            setSuccess(message);
            return;
        }
        setError(message);
    }

    public PagerResult<T> setSuccess(String msg){
        this.message=msg;
        this.msg=msg;
        this.code=0;
        this.state=true;
        this.stateCode=0;
        this.resultCode=1;
        this.resultMsg=msg;
        return this;
    }
    public static PagerResult failure(String errorMsg){

        return new PagerResult(false,errorMsg);
    }
    public static PagerResult success(String msg,List<Object> data){
        PagerResult op1= new PagerResult(true,msg);
        op1.setData(data);

        return op1;
    }
    
    public Map<String,Object> toHashMap(){
        Map<String,Object> map=new HashMap<>(20);
        map.put("message",this.message);
        map.put("msg",this.msg);
        map.put("code",this.code);
        map.put("state",this.state);
        map.put("stateCode",this.stateCode);
        map.put("resultCode",this.resultCode);
        map.put("resultMsg",this.resultMsg);
        map.put("data",this.data);
        map.put("pageIndex",this.pageIndex);
        map.put("pageSize",this.pageSize);
        map.put("total",this.total);
        map.put("totalPages",this.totalPages);

        return map;
    }


    /****
     * 注意,这个是用来兼容视酷原有分页系统以及52web的分页系统的.
     * ***/
    public Map<String,Object> fixedToHashMap(){
        Map<String,Object> countsMap=new HashMap<>(9);
        Map<String,Object> dataMap=new HashMap<>(2);

        countsMap.put("pageIndex",this.pageIndex);
        countsMap.put("pageSize",this.pageSize);
        countsMap.put("total",this.total);
        countsMap.put("totalPages",this.totalPages);

        Map<String,Object> finalRes=toHashMap();

        dataMap.put("counts",countsMap);
        dataMap.put("list",this.getData());

        finalRes.put("data",dataMap);


        return finalRes;
    }


}