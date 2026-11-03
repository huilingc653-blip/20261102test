package com.bytter.biz.fundwarn.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 预警策略条件实体
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_WARN_CONDITION", schema = "")
@SuppressWarnings("serial")
public class BtWarnCondition extends IdEntity implements java.io.Serializable {
  /** 预警策略表ID*/
  private String warnId;
  /** 数据类型（corp单位、bankAcc银行账户、fcAcc内部账户、bankAccAttribute银行账号属性、
   * bankAccType银行账号类别、bankAccNature银行账号性质、fcAccNature内部账户性质、
   * accSubject会计科目）*/
  private String dataType;
  /** 数据值，选择为全部时值为null*/
  private String dataValue;
  @Column(name ="WARN_ID",nullable=true,length=32)
  public String getWarnId() {
    return warnId;
  }
  public void setWarnId(String warnId) {
    this.warnId = warnId;
  }
  @Column(name ="DATA_TYPE",nullable=true,length=20)
  public String getDataType() {
    return dataType;
  }
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }
  @Column(name = "DATA_VALUE",nullable = true,length = 32)
  public String getDataValue() {
    return dataValue;
  }
  public void setDataValue(String dataValue) {
    this.dataValue = dataValue;
  }
  @Override
  public String toString() {
    return "BtWarnCondition{" +
            "warnId='" + warnId + '\'' +
            ", dataType='" + dataType + '\'' +
            ", dataValue='" + dataValue + '\'' +
            '}';
  }
}
