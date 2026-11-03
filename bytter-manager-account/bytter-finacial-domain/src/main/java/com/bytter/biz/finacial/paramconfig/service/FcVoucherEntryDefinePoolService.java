package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.paramconfig.dao.FcVoucherEntryDefineDao;
import com.bytter.biz.finacial.paramconfig.dao.FcVoucherEntryDefinePoolDao;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefinePool;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IFcSubjectPoolService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_VOUCHER_ENTRY_DEFINE_POOL
* @author onlineGenerator 
* @date 2017-05-22 16:44:06
* @version 1.0
*
*/
@Service("fcVoucherEntryDefinePoolService")
@Transactional
public class FcVoucherEntryDefinePoolService extends BaseService<FcVoucherEntryDefinePool,String> {
	@Autowired
	private FcVoucherEntryDefinePoolDao fcVoucherEntryDefinePoolDao;
	@DubboReference(check = false)
	private IFcSubjectPoolService iFcSubjectPoolService;
	@DubboReference(check = false)
	private IFcSubjectService iFcSubjectService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcVoucherEntryDefinePool entity){
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
	public void save(FcVoucherEntryDefinePool entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcVoucherEntryDefinePool entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcVoucherEntryDefinePool entity){
		super.saveOrUpdate(entity);
	}
	/**
	 * 功能介绍 ： 根据记账规则获取规则记账分录。<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcVoucherEntryDefine> getEntryDefinesByRuleId(String ruleId) {
		return this.fcVoucherEntryDefinePoolDao.getEntryDefinesByRuleId(ruleId);
	}
	/**
	 * 功能介绍 : 获取手续费记账规则明细。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcVoucherEntryDefine> getChargeDefinesByRuleId(String ruleId) {
		return this.fcVoucherEntryDefinePoolDao.getChargeDefinesByRuleId(ruleId);
	}
	/**
	 * 功能介绍 ： 根据记账规则ID删除规则记账分录。<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void deleteEntryDefinesByRuleId(String ruleId) {
		this.fcVoucherEntryDefinePoolDao.deleteEntryDefinesByRuleId(ruleId);
	}
	public BaseDao<FcVoucherEntryDefinePool, String> getEntityDao() {
		return fcVoucherEntryDefinePoolDao;
	}
	/**
	 * 功能介绍 ： 根据记账规则获取规则记账分录。<br>
	 */
	public List<FcVoucherEntryDefineVo> getVoucherEntryDefinesPoolByRuleId(String ruleId) {
		List<FcVoucherEntryDefineVo> voList = this.fcVoucherEntryDefinePoolDao.getVoucherEntryDefinesPoolByRuleId(ruleId);
		List<String> subjectIds = voList.stream().map(FcVoucherEntryDefineVo::getSubjectId).collect(Collectors.toList());
		List<FcSubjectPoolDTO> fcSubjectPoolDTOS = iFcSubjectPoolService.getFcSubjectPoolByIds(subjectIds);
		Map<String, String> map = fcSubjectPoolDTOS.stream().collect(Collectors.toMap(FcSubjectPoolDTO::getId, FcSubjectPoolDTO::getSubjectName));
		voList.forEach( a -> {
			a.setSubjectName(map.get(a.getSubjectId()));
		});
		return voList;
	}
	/**
	 * 功能介绍 ： 根据记账规id则获取规则记账分录。<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public List<FcVoucherEntryDefineVo> getInitVoucherEntryDefinesByRuleId(String ruleId) {
		List<FcVoucherEntryDefineVo> vos = fcVoucherEntryDefinePoolDao.getInitVoucherEntryDefinesByRuleId(ruleId);
		List<String> subjectIds = vos.stream().map(FcVoucherEntryDefineVo::getSubjectCode).collect(Collectors.toList());
		List<FcSubjectDTO> subjectDTOS = iFcSubjectService.getSubjectByIds(subjectIds);
		Map<String, String> map = subjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, FcSubjectDTO::getSubjectName));
		vos.forEach(a -> {
			a.setSubjectName(map.get(a.getSubjectCode()));
		});
		return vos;
	}
	/**
	 * 功能介绍 : 删除已存在记账规则明细。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void deleteInitEntryRule() {
		fcVoucherEntryDefinePoolDao.deleteInitEntryRule();
	}
	/**
	 * 功能介绍 : 记账规则明细导入。<br>
	 */
	public void importInitEntryRule() {
		fcVoucherEntryDefinePoolDao.importInitEntryRule();
	}
}
