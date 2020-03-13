package net.w2p.Shared.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DGDL-08 on 2017/3/15.
 */
public class ValidateUtils {
    private static boolean preg_match(Object strObject,String regEx){
        String tmpVal=strObject.toString();

        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(tmpVal);
        return matcher.matches();
    }

    // Check var is a valid email or not
    public static boolean isEmail(Object strObject) {
        if(isEmpty(strObject)){
            return false;
        }

        String tmpVal=strObject.toString();
        String regEx="^[_.0-9a-z-]+@([0-9a-z][0-9a-z-]+.)+[a-z]{2,4}$";
        regEx="^([a-z0-9A-Z]+[-|_|\\.]{0,})+[a-z0-9A-Z]?@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(tmpVal);
        return matcher.matches();
    }


    // Check var is a valid Chinese mobile or not
    public static   boolean isMobile(Object strObject) {
        if(isEmpty(strObject)){
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][2,3,4,5,6,7,8,9][0-9]{9}$");
        m = p.matcher(strObject.toString());
        b = m.matches();
        return b;
//        return b;
//        String regEx="^(((d{2,3}))|(d{3}-))?1(3|5|7|8|9)d{9}$";
//        return preg_match(strObject,regEx);
    }

    // Check var is a valid postal code or not
    public static boolean  isPostalCode(Object strObject) {
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="^[1-9]\\d{5}$";
        return preg_match(strObject,regEx);
    }

    // Check var is a valid IP Address or not
    public static boolean  isIPAddress(Object strObject) {
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
        return preg_match(strObject,regEx);
    }

    // Check var is a valid ID card or not
    public static boolean  isIDCard(Object strObject) {
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="(^([\\d]{15}|[\\d]{18}|[\\d]{17}x)$)";
        return preg_match(strObject,regEx);
    }

    /**
     * 检查中文
     * @param string $str 标签字符串
     */
    public static boolean  isCn(Object strObject){
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="[x{4e00}-x{9fa5}]+";
        return preg_match(strObject,regEx);
    }

    /**
     * 检查数字
     * @param string $str 标签字符串
     */
    public static boolean  isNumber(Object strObject){
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="^\\d*$";
        boolean res_1st= preg_match(strObject,regEx);
        if(res_1st){
            return true;
        }
        String regEx2="^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$";
        boolean res_2nd=preg_match(strObject,regEx2);
        if(res_2nd){
            return true;
        }
        String regEx3="^-?[1-9]?\\d*$";
        boolean res_3rd=preg_match(strObject,regEx3);

        if(res_3rd){
            return true;
        }
        return false;
    }
//public static function  isNumber($str){
//	if(preg_match('/^\d+$/', $str)) {
//		return true;
//	}
//	return false;
//}

    /**
     * 检查是否每位相同
     * @param string $str 标签字符串
     */
    public static boolean  isNumSame(String strObject){
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="^(\\w)1+$";
        return preg_match(strObject,regEx);
    }

    /**
     * 检查是否为空
     * @param string $str 标签字符串
     */
    public static boolean  isEmpty(Object str){
        //$str = trim($str);
        if(str==null){
            return true;
        }
        String tmpVal=str.toString();
        if(tmpVal.length()<=0){
            return true;
        }
        return false;
    }




    // 检测一组字符是否有可能组成手机号码
    public static boolean  willMobile(Object strObject) {
        String regEx="^(((\\d{2,3}))|(\\d{3}-))?1(3|5|8|9)\\d{0,9}$";
        boolean res_1st= preg_match(strObject,regEx);
        if(res_1st){
            return true;
        }
        return false;
    }

    public static boolean isPhoneNumber(Object strObject) {
        String regEx="^((0d{3}[-])?\\d{7}|(0\\d{2}[-])?\\d{8})?$";
        boolean res_1st= preg_match(strObject,regEx);
        if(res_1st){
            return true;
        }
        return false;
    }

    public static boolean  isAreaCode(Object strObject){
        String regEx="^(0d{3})|(0\\d{2})$";
        boolean res_1st= preg_match(strObject,regEx);
        if(res_1st){
            return true;
        }
        return false;
    }

    public static boolean isDateTime(Object strObject) {
        if(isEmpty(strObject)){
            return false;
        }
        String regEx="^[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}$";
        boolean res_1st= preg_match(strObject,regEx);
        if(res_1st){
            return true;
        }
        String regEx2="^[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}[\\s]+[0-9]{2}:[0-9]{2}:[0-9]{2}$";
        boolean res_2nd=preg_match(strObject,regEx2);
        if(res_2nd){
            return true;
        }
        return false;

    }

    private static boolean isMatch(String regex, String orginal){
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        boolean mathces=isNum.matches();

        return mathces;
    }

    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^+{0,1}[1-9]\\d*$", orginal);
    }

    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*$", orginal);
    }

    /***
     * 是否整数
     * **/
    public static boolean isInteger(String orginal) {
        return isMatch("^[+-]{0,1}0$", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    public static boolean isPositiveDecimal(String orginal){
        return isMatch("+{0,1}[0].[1-9]*|+{0,1}[1-9]\\d*.\\d*", orginal);
    }

    public static boolean isNegativeDecimal(String orginal){
        return isMatch("^-[0].[1-9]*|^-[1-9]\\d*.\\d*", orginal);
    }

    /**
     * 是否小数
     * **/
    public static boolean isDecimal(String orginal){
        return isMatch("[-+]{0,1}\\d+.\\d*|[-+]{0,1}\\d*.\\d+", orginal);
    }

    /**
     * 是否数字。
     * */
    public static boolean isRealNumber(String orginal){
        return isInteger(orginal) || isDecimal(orginal);
    }

    // 判断电话
    public static boolean isTelephone(String phonenumber) {
        String phone = "0\\d{2,3}-\\d{7,8}";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

//    // 判断手机号
//    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(16[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
//    }



    // 判断日期格式:yyyy-mm-dd

    public static boolean isValidDate(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }
    //验证金额
    public static boolean isNumber(String str)
    {
        return isRealNumber(str);
    }

    /**
     * 是否金额
     * **/
    public static boolean isMoneyAmount(String str)
    {
        Pattern pattern= Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 是否用户帐号--只允许下划线，英文，数字
     * **/
    public static boolean isAccountName(String str)
    {
        Pattern pattern= Pattern.compile("^[a-zA-Z0-9_\\-]?[\\-a-zA-Z0-9_\\s]+$"); // 判断小数点后一位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 是否用户昵称--只允许下划线，英文，数字,中文
     * **/
    public static boolean isNickname(String str)
    {
        Pattern pattern= Pattern.compile("^[\\u4E00-\\u9FA5_A-Za-z0-9_\\s]*$"); // 判断小数点后一位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
