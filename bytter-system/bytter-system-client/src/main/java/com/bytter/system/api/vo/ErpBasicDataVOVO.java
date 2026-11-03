package com.bytter.system.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;
import javax.persistence.Column;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 基础数据映射
 * @author onlineGenerator
 * @date 2017-12-14 11:24:27
 * @version 1.0
 *
 */
@Data
public class ErpBasicDataVOVO implements java.io.Serializable {
	private String id;
	/**内部系统代码*/
	private String bytterCode;
	/**内部系统*/
	private String bytterName;
	/**外部系统代码*/
	private String erpCode;
	/**外部系统名称*/
	private String erpName;
	/**外部系统备用*/
	private String erpReverse;
	/**对应数据类型*/
	private String dataTypeCode;
	/**对应接口类型*/
	private String typeCode;
	/**createDate*/
	private Date createDate;
	/**createBy*/
	private String createBy;
	/**updateDate*/
	private Date updateDate;
	/**updateBy*/
	private String updateBy;
	/**币别*/
	private String curId;
	private String curName;
}
