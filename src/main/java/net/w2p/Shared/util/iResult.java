package net.w2p.Shared.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by DGDL-08 on 2017/4/3.
 */
public abstract class iResult implements Serializable{

    public Long currentTime=System.currentTimeMillis()/1000;

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    protected boolean state=true;
    protected int stateCode=0;
    protected HashMap<String,String> errors=new HashMap<String, String>();
    protected String message="";
    protected int code=0;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    protected String msg="";

//    public void setError(String msg){
//        this.message=msg;
//        this.msg=msg;
//        this.code=-1;
//        this.state=false;
//        this.stateCode=-1;
//    }
//    public void setSuccess(String msg){
//        this.message=msg;
//        this.msg=msg;
//        this.code=0;
//        this.state=true;
//        this.stateCode=0;
//    }
}
