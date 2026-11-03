package com.bytter.check.vo;
import java.io.Serializable;
/**
 * <p>用于基础平台数据注解注入功能接口</p>
 * <p>可注入的注解如下：</p>
 * <ul>
 *     <li>国际化：@Lang</li>
 *     <li>字典：@Dicts/@Dict</li>
 *     <li>网点单位：@Corps/@Corp</li>
 *     <li>用户：@User</li>
 *     <li>菜单：@Menu</li>
 *     <li>省市：@Region</li>
 *     <li>银行类型：@BankType</li>
 *     <li>币别：@Currency</li>
 *     <li>联行号：@InputBankInfos/InputBankInfo</li>
 *     <li>资金池：@CorpPool</li>
 * </ul>
 * Created by zhoul on 2017/12/11.
 * update by wangzx on 2019/5/9
 */
public interface IBaseDataInject extends Serializable {
   long serialVersionUID = 1L;
}
