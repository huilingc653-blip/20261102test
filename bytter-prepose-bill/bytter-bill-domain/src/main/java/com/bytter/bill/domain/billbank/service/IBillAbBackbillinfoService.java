package com.bytter.bill.domain.billbank.service;
import com.bytter.bill.domain.billbank.vo.BillAbBackbillinfoVo;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据背面信息服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-09-02 <br>
 */
public interface IBillAbBackbillinfoService {
	
	/**
	 * <b>方法描述：</b>批量保存背面信息<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	void batchSave(List<BillAbBackbillinfoVo> billAbBackbillinfoList);
}
