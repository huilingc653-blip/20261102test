package com.bytter.biz.bdlink.service;
import com.bytter.biz.bdlink.dao.BisQueryCmdDao;
import com.bytter.biz.bdlink.entity.BisQueryCmd;
import com.bytter.biz.bdlink.vo.BisQueryCmdVO;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 查询指令表
* @author onlineGenerator
* @date 2017-12-29 15:57:19
* @version 1.0
*
*/
@Service("bisQueryCmdService")
@Transactional
public class BisQueryCmdService extends BaseService<BisQueryCmd,String> implements ICorpDisableVerifyService {
	@Autowired
	private BisQueryCmdDao bisQueryCmdDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IRegionService regionService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
    RedisUtils redisUtils;
    private final String PROVINCE = "province:";
    private final String CITY = "city:";
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(BisQueryCmd entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(BisQueryCmd entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BisQueryCmd entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(BisQueryCmd entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<BisQueryCmd, String> getEntityDao() {
		return bisQueryCmdDao;
	}
	/**
	 * 指令列表查询
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String resource="";
		String opType = (String)dataTablesInput.getFormdata().get("opType");
		if("listQuerySend".equals(opType)){
			//指令发送
			resource=ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDQUERY;
			sql.append(" and b.cmd_Stat <> '0' ");
		}else{
			resource=ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_QUERY;
		}
		if(!StringUtils.isBlank(resource)){
			String corpAuthSql = SqlUtil.getInSql(
						corpService.getUserCorpIds(resource, user.getId()), "b.corp_Id",
						true);
			sql.append(" and ").append(corpAuthSql);
		}
		DataTablesOutput<BisQueryCmdVO> outputdata = bisQueryCmdDao.getDataTablesUnion(dataTablesInput,sql.toString());
		return this.setJoinField(outputdata, opType);
	}
	/**
	 * 设置付款办理的关联字段
	 * @param output
	 * @param opType
	 * @return
	 */
	public DataTablesOutput setJoinField(DataTablesOutput output, String opType) {
		List<BisQueryCmdVO> listBisQueryCmd = output.getData();
		if (EmptyUtils.isNotEmpty(listBisQueryCmd)) {
			Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
			Map<String, BisBifInitDTO> bisBifInitMap = bisBifInitService.getBisBifInitMap();
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(BdlinkConstant.EXC_VOUCHERSTAT_DICT, BdlinkConstant.EXC_TRANSID_DICT,
					BdlinkConstant.EXC_CMDTYPE_DICT);
			for (BisQueryCmdVO bisQueryCmd : listBisQueryCmd) {
				if (curMap.containsKey(bisQueryCmd.getCurId())) {
					bisQueryCmd.setCurName(curMap.get(bisQueryCmd.getCurId()).getCurName());
				}
				if (bisBifInitMap.containsKey(bisQueryCmd.getBifCode())) {
					bisQueryCmd.setBifName(bisBifInitMap.get(bisQueryCmd.getBifCode()).getName());
				}
				//字典注入
				this.injectValues(bisQueryCmd, listDictMap);
			}
		}
		return output;
	}
	/**
	 * 付款指令信息字典注入
	 * @param vo
	 * @param listDictMap
	 */
	private void injectValues(BisQueryCmdVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
		// 交易状态
		Optional.ofNullable(vo.getCmdStat()).ifPresent(cmdStat -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(cmdStat)))
					.ifPresent(dict -> vo.setCmdStatName(dict.getName()));
		});
		// 交易类型
		Optional.ofNullable(vo.getTransId()).ifPresent(transId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_TRANSID_DICT);
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(transId)))
					.ifPresent(dict -> vo.setTransName(dict.getName()));
		});
		// 指令类型
		Optional.ofNullable(vo.getCmdType()).ifPresent(cmdType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_CMDTYPE_DICT);
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(cmdType)))
					.ifPresent(dict -> vo.setCmdTypeName(dict.getName()));
		});
	}
	/**
	 * 根据ID获取指令
	 * @param id
	 * @return
	 */
	public BisQueryCmdVO getByIdUnion(String id){
		BisQueryCmdVO vo = this.bisQueryCmdDao.getByIdUnion(id);
		if(!Optional.ofNullable(vo).isPresent()) {
			return new BisQueryCmdVO();
		}
		return this.bisQueryCmdDao.getByIdUnion(id);
	}
	/**
	 * 指令重发
	 * @param bisQueryCmd
	 */
	public void resendBisQueryCmd(BisQueryCmd bisQueryCmd, ICurrentUser user) {
		//更新状态为3-准备发送
		bisQueryCmd.setCmdStat(BdlinkConstant.PRE_SEND);
		bisQueryCmd.setSendTime(DateUtils.getDate());
		bisQueryCmd.setUpdateBy(user.getLoginName());
		bisQueryCmd.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(bisQueryCmd);
	}
	/**
	 * 新增查询指令
	 * @param bisQueryCmd
	 */
	public void addBisQueryCmd(BisQueryCmdVO bisQueryCmd, ICurrentUser user) {
		String bankAccIds = bisQueryCmd.getBankAccIds();
		String[] bankAccIdAr = bankAccIds.split(CommonConstant.COMMA_SPLIT);
		if (bankAccIdAr != null) {
			String[] bankAccs = bisQueryCmd.getBankAccs().split(CommonConstant.COMMA_SPLIT);
			List<String> listBankAcc = new ArrayList<>();
			Collections.addAll(listBankAcc, bankAccs);
			Map<String, BtBankAccDTO> bankAccMap = btBankAccService.getBankAccMapByAccsAndCurId(listBankAcc);
			for (int i = 0; i < bankAccIdAr.length; i++) {
				String bankAccCur = bisQueryCmd.getBankAccs().split(CommonConstant.COMMA_SPLIT)[i];
				String curId = bisQueryCmd.getCurIds().split(CommonConstant.COMMA_SPLIT)[i];
				BisQueryCmd entity = new BisQueryCmd();
				BeanUtils.copyNotNullProperties(bisQueryCmd, entity);
				entity.setBankAccId(bankAccIdAr[i]);
				entity.setBankAcc(bankAccCur);
				entity.setCurId(curId);
				entity.setBifCode(bisQueryCmd.getBifCodes().split(CommonConstant.COMMA_SPLIT)[i]);
				entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_QUERY_CMD));
				entity.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
				entity.setSendTime(DateUtils.getDate());
				entity.setCreateBy(user.getLoginName());
				entity.setCreateDate(DateUtils.getDate());

				BtBankAccDTO bankAcc = bankAccMap.get(bankAccCur+curId);
				entity.setAccName(bankAcc.getAccName());
				entity.setBankName(bankAcc.getBankName());
				entity.setCorpId(bankAcc.getCorpId());
				entity.setCorpName(bankAcc.getCorpName());

				this.save(entity);
			}
		}
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的查询指令，不允许禁用！
			List<String> listCorps = new ArrayList<String>();
			listCorps.add(corpId);
			List<BtBankAccDTO> listBankAcc = btBankAccService.getListByCorpIds(listCorps);
			if(CollectionUtils.isEmpty(listBankAcc)) {
				return null;
			}
			List<String> accids = listBankAcc.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
			int num = this.bisQueryCmdDao.getBisQueryCmdCountByBankAccIds(accids);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_QUERY_CMD,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_QUERYCMD_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
}
