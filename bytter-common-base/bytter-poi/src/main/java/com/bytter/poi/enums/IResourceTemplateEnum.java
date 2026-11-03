package com.bytter.poi.enums;
/**
 * <br>
 * <b>功能：</b> 资源模板枚举接口。
 * <br><br> 当需要实现自定义资源枚举时，注意要调用资源模板的添加方法，即ResourceTemplateEnum.addResourceTemplateEnum()。
 * <br><br> 可在业务Service类的【构造代码块】（类实例化时会执行）调用ResourceTemplateEnum.addResourceTemplateEnum()。
 * {@see ResourceTemplateEnum#addResourceTemplateEnum()}
 * <br>
 * <b>作者：</b> chenlw <br>
 * <b>日期：</b> 2021-01-18  <br>
 */
public interface IResourceTemplateEnum {
    /**
     * 获取 资源代码
     *
     * @return 资源代码
     */
    String getCode();
    /**
     * 资源文件路径
     *
     * @return 资源文件路径
     */
    String getPath();
    /**
     * 获取 反射类
     *
     * @return 反射类
     */
    String getReflex();
    /**
     * 获取请求路径
     * @return
     */
    String getUrl();
    
    String getChineseName();
}
