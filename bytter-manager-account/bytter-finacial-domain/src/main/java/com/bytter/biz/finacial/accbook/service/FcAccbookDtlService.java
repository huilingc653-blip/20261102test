package com.bytter.biz.finacial.accbook.service;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDtlDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: FC_ACCBOOK_DTL
* @author onlineGenerator
* @date 2017-05-31 15:02:54
* @version 1.0
*
*/
@Service("fcAccbookDtlService")
@Transactional
public class FcAccbookDtlService extends BaseService<FcAccbookDtl,String> implements ICorpDisableVerifyService {
	@Autowired
	private FcAccbookDtlDao fcAccbookDtlDao;
	@Autowired
	private FcAccbookDao fcAccbookDao;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcAccbookDtl entity){
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
	public void save(FcAccbookDtl entity){
		this.fillData(entity);
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcAccbookDtl entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcAccbookDtl entity){
		this.fillData(entity);
		super.saveOrUpdate(entity);
	}
	private void fillData(FcAccbookDtl entity){
		//acc_name、subject_id、subject_name、corp_id、balance_way、cur_id、cur_name、net_id
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setAccName(fcAccDTO.getAccName());
		entity.setSubjectId(fcAccDTO.getSubjectId());
		entity.setSubjectName(fcAccDTO.getSubjectName());
		entity.setCorpId(fcAccDTO.getCorpId());
		entity.setBalanceWay(fcAccDTO.getBalanceWay());
		entity.setCurId(fcAccDTO.getCurId());
		entity.setCurName(fcAccDTO.getCurName());
		entity.setNetId(fcAccDTO.getNetId());
	}
	public BaseDao<FcAccbookDtl, String> getEntityDao() {
		return fcAccbookDtlDao;
	}
	/**
	 * 功能介绍 : 根据账务流水ID获取流水明细。<br>
	 * <b>日期：</b>2017年6月7日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookDtl> getAccbookDtlsByBookId(String bookId){
		return fcAccbookDtlDao.getAccbookDtlsByBookId(bookId);
	}
	/**
	 * 功能介绍 : 获取账务明细信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookDtl> getBookDtlsByBusGlobalId(String busGlobalId) {
		return fcAccbookDtlDao.getBookDtlsByBusGlobalId(busGlobalId);
	}
	/**
	 * 功能介绍 : 根据账务流水ID获取流水详细明细。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<FcAccbookDtlVO> getPageDtlsByBookId(String bookId) {
		return fcAccbookDtlDao.getPageDtlsByBookId(bookId);
	}
	/**
     * <br>
     * <b>功能：</b>未复核的账务<br>
     * <b>作者：</b>yangl<br>
     * <b>日期：</b> 2017-09-28 <br>
     * @param account
     * @return
     */
	@Transactional(readOnly = true)
    public boolean existUnCheckAccbook(String account, String busGlobalId){
        return fcAccbookDao.existUnCheckAccbook(account, busGlobalId);
    }
	public List<FcAccbookDtlDTO> getPoolPageDtlsByBookId(String bookId){
		return fcAccbookDtlDao.getPoolPageDtlsByBookId(bookId);
	}
	/**
	 *单位禁用校验
	 *@Param corpId
	 *@return
	 */
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
		//网络记账
		if (fcAccbookDao.verifyCorpDisableByCorpIdTally(corpId) > 0) {
			CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
			corpVerifyErrorDTO.setCorpId(corpId);
			corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
			corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
			corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_END_VALID_DISABLE_ERROR));
			corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
		}
		//手工记账
		if (fcAccbookDao.verifyCorpDisableByCorpIdH(corpId) > 0) {
			CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
			corpVerifyErrorDTO.setCorpId(corpId);
			corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
			corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_HAND_TALLY);
			corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_HAND_TALLY_END_VALID_DISABLE_ERROR));
			corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
		}
		return corpVerifyErrorDTOS;
	}
	/**
	 * 通过id获取
	 * @param id
	 * @return
	 */
	public FcAccbookDtl getFcAccbookDtlById(String id){
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return fcAccbookDtlDao.getFcAccbookDtlById(params);
	}
	/**
	 *更新现金流项目
	 * @param id
	 * @param cashflux
	 */
	public void updateCashFlux(String id, String cashflux) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("cashflux", cashflux);
		fcAccbookDtlDao.updateCashFlux(params);
	}

	public List<FcAccbookDtl> getFcAccbookDtlByNetId(String netId) {
		return fcAccbookDtlDao.getFcAccbookDtlByNetId(netId);
	}
}
