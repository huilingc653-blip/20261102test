package com.bytter.biz.finacial.paramconfig.service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.paramconfig.dao.FcVoucherEntryDefineDao;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_VOUCHER_ENTRY_DEFINE
* @author onlineGenerator
* @date 2017-05-22 16:44:06
* @version 1.0
*
*/
@Service("fcVoucherEntryDefineService")
@Transactional
public class FcVoucherEntryDefineService extends BaseService<FcVoucherEntryDefine,String> {
	@Autowired
	private FcVoucherEntryDefineDao fcVoucherEntryDefineDao;
	@DubboReference(check = false)
	private IFcSubjectService iFcSubjectService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcVoucherEntryDefine entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcVoucherEntryDefine entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcVoucherEntryDefine entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcVoucherEntryDefine entity){
		super.saveOrUpdate(entity);
	}
	/**
	 * 功能介绍 ： 根据记账规则获取规则记账分录。<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcVoucherEntryDefine> getEntryDefinesByRuleId(String ruleId) {
		return this.fcVoucherEntryDefineDao.getEntryDefinesByRuleId(ruleId);
	}
	/**
	 * 功能介绍 : 获取手续费记账规则明细。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcVoucherEntryDefine> getChargeDefinesByRuleId(String ruleId) {
		return this.fcVoucherEntryDefineDao.getChargeDefinesByRuleId(ruleId);
	}
	/**
	 * 功能介绍 ： 根据记账规则ID删除规则记账分录。<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void deleteEntryDefinesByRuleId(String ruleId) {
		this.fcVoucherEntryDefineDao.deleteEntryDefinesByRuleId(ruleId);
	}
	public BaseDao<FcVoucherEntryDefine, String> getEntityDao() {
		return fcVoucherEntryDefineDao;
	}
	/**
	 * 功能介绍 ： 根据记账规则获取规则记账分录。<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public List<FcVoucherEntryDefineVo> getVoucherEntryDefinesByRuleId(String ruleId) {
		List<FcVoucherEntryDefineVo> lists = this.fcVoucherEntryDefineDao.getVoucherEntryDefinesByRuleId(ruleId);
		List<String> subjectIds = lists.stream().map(FcVoucherEntryDefineVo::getSubjectId).collect(Collectors.toList());
		List<FcSubjectDTO> subjectDTOS = iFcSubjectService.getSubjectByIds(subjectIds);
		Map<String, String> map = subjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, FcSubjectDTO::getSubjectName));
		lists.forEach( a -> {
			a.setSubjectName(map.get(a.getSubjectId()));
		});
		return lists;
	}
	/**
	 * 功能介绍 ： 根据记账规id则获取规则记账分录。<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public List<FcVoucherEntryDefineVo> getInitVoucherEntryDefinesByRuleId(String ruleId) {
		List<FcVoucherEntryDefineVo> vos = fcVoucherEntryDefineDao.getInitVoucherEntryDefinesByRuleId(ruleId);
		List<String> subjectIds = vos.stream().map(FcVoucherEntryDefineVo::getSubjectCode).collect(Collectors.toList());
		List<FcSubjectDTO> subjectDTOS = iFcSubjectService.getSubjectByIds(subjectIds);
		Map<String, String> map = subjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, FcSubjectDTO::getSubjectName));
		vos.forEach( a -> {
			a.setSubjectName(map.get(a.getSubjectCode()));
		});
		return vos;
	}
}
