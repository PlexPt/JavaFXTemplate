package net.w2p.Shared.util;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * <code>Language</code>类用于设定语言和地区。
 * 
 * @author fengyouchao
 *
 */
public class Language {
	
	private static String languagePakageName = null;
	private static Locale locale  = null;
	private static ResourceBundle bundle = null;
	
	/**
	 * 带有一个参数的构造方法。
	 * 
	 * @param languagePakageName 国际资源文件名称。
	 */
	public Language(String languagePakageName){
		setLanguagePakageName(languagePakageName);
	}
	
	/**
	 * 设定系统默认语言和地区。
	 */
	public void setDefaultLanguage(){
		locale = Locale.getDefault();
		bundleInitialize();
	}
	
	
	/**
	 * 通过国际字符串获得设定地区的字符串。
	 * 
	 * @param key 国际字符串
	 * @return 该地区的字符串
	 */
//	public static String getString(String key){
//		return bundle.getString(key);
//	}
//
//
	/**
	 * 设置地区。
	 * 
	 * @param locale 地区。
	 */
	public void setLocale(Locale locale ){
		Language.locale = locale;
		bundleInitialize();
	}

	
	/**
	 * 初始化Bundle。
	 */
	private void bundleInitialize(){
		bundle = ResourceBundle.getBundle(languagePakageName,locale);
	}
	
	/**
	 * 获取国际资源包的名字。
	 * 
	 * @return 国际资源包的名字。
	 */
	public String getLanguagePakageName() {
		return languagePakageName;
	}

	
	/**
	 * 设置国际资源包的名字。
	 * 
	 * @param languagePakageName 国际资源包的名字。
	 */
	public void setLanguagePakageName(String languagePakageName) {
		Language.languagePakageName = languagePakageName;
	}
	
}
