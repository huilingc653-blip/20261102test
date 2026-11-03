package com.bytter.system.api.dto;
import lombok.Data;
import java.io.Serializable;
/**
 * @author YonJar
 * @date 2023/5/12.
 * @description fc_acc/fc_subject/bt_currency
 */
@Data
public class FcAccSubjectDTO implements Serializable {
    private static final long serialVersionUID = 4706601496557341010L;
    private String blnctrAcc;
    private String accType;
    private String accName;
    private String subjectSort;
    private String subjectId;
    private String subjectName;
    private String netId;
    // 币种名称
    private String curName;
    private String curId;
    // 计量单位
    private String measureUnit;
    private Integer balanceWay;
    private String corpId;
    private String corpName;
    private String subjectPoolName;
}
