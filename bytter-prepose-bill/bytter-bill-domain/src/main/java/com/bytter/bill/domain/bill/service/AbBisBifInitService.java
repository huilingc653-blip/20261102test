package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.dto.BisBifInitDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 接口代码服务类
 * @author zhenghj
 *
 */
@Service("abBisBifInitService")
@Transactional
public class AbBisBifInitService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public BisBifInitDTO getObjectById(String id){
		return bisBifInitService.getObjectById(id);
	}
	/**
	 * 根据有效标志过滤
	 * @param validSign 可空
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BisBifInitDTO> listByValidSign(String validSign){
		return bisBifInitService.listByValidSign(validSign);
	}
	/**
	 * 根据有效标志，银行类别过滤
	 * @param validSign 可空
	 * @param bankTypeId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BisBifInitDTO> listBySignOrBankType(String validSign,String bankTypeId){
		return bisBifInitService.listBySignOrBankType(validSign, bankTypeId);
	}
	/**
	 * 根据接口代码查找
	 * @param bifCode 接口代码
	 * @return
	 */
	@Transactional(readOnly = true)
	public BisBifInitDTO getByBifCode(String bifCode){
		return bisBifInitService.getByBifCode(bifCode);
	}
	/**
	 * 根据接口代码名称查找
	 * @param name	接口代码名称
	 * @return
	 */
	@Transactional(readOnly = true)
	public BisBifInitDTO getByName(String name){
		return bisBifInitService.getByName(name);
	}
}
