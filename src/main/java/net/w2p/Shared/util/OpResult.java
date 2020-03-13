package net.w2p.Shared.util;


import java.io.Serializable;

public class OpResult<T> extends iResult  implements Serializable {

    private T data;

    public static OpResult failure(String errorMsg){
        return new OpResult(false,errorMsg);
    }
    public static OpResult success(String msg,Object data){
        OpResult op1= new OpResult(true,msg);
        op1.setData(data);

        return op1;
    }

    public OpResult(){}
    public OpResult(boolean trueOrFalse,String message){
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


    public T getData() {
        return data;
    }
    public OpResult<T> setData(T data) {
        this.data = data;
        return this;
    }
     public Integer resultCode=1;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String resultMsg="";

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public OpResult<T> setError(String msg){
        this.message=msg;
        this.msg=msg;
        this.code=-1;
        this.state=false;
        this.stateCode=-1;
        this.resultCode=0;
        this.resultMsg=msg;
        return this;
    }
    public OpResult<T> setSuccess(String msg){
        this.message=msg;
        this.msg=msg;
        this.code=0;
        this.state=true;
        this.stateCode=0;
        this.resultCode=1;
        this.resultMsg=msg;
        return this;
    }


}