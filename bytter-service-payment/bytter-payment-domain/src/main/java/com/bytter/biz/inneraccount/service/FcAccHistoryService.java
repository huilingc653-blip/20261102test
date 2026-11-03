package com.bytter.biz.inneraccount.service;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccHistoryService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: Service
 * @Description: FC_ACC_HISTORY
 * @author onlineGenerator
 * @date 2017-06-07 14:30:19
 * @version 1.0
 *
 */
@Service("fcAccHistoryService")
@Transactional
public class FcAccHistoryService /* extends BaseService<FcAccHistory,String> */{
	@DubboReference(check = false)
	private IFcAccHistoryService fcAccHistoryService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	@DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
	/**
	 * 删除对象
	 * @param history
	 */
	public FcAccHistoryVO mergeObj(FcAccHistoryVO history) {
		return fcAccHistoryService.mergeObj(history);
	}
	
	public FcAccHistoryVO getFcAccHistoryVOById(String id) {
		return fcAccHistoryService.getFcAccHistoryVOById(id);
	}
	public FcAccHistoryVO get(String id) {
		return fcAccHistoryService.getFcAccHistoryById(id);
	}
	public FcAccHistoryVO getFcAccHistoryById(String id) {
		return fcAccHistoryService.getFcAccHistoryById(id);
	}
	public void deleteEntity(String id){
		fcAccHistoryService.delete(id);;
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		fcAccHistoryService.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcAccHistoryVO entity){
		fcAccHistoryService.save(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcAccHistoryVO entity){
		fcAccHistoryService.update(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcAccHistoryVO entity){
		fcAccHistoryService.saveOrUpdate(entity);
	}
	/**
	 * 待提交的可改户申请的数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser userVO) {
//		Map<String,Object> params=new HashMap();
//		String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,userVO.getId()),"t.corp_Id",params);
//		StringBuilder joinSql=new StringBuilder();
//		String loginName = userVO.getLoginName();
//		joinSql.append(" and ").append(corpAuthSql);
//		return fcAccHistoryService.getDataTablesUnion(dataTablesInput,joinSql.toString(),loginName,params);
		return  fcAccHistoryService.getDataTablesUnion(dataTablesInput,userVO);
	}
	/**
	 * 待提交的可改户申请的数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput,ICurrentUser userVO) {
		return fcAccHistoryService.getDataTablesUnionForPool(dataTablesInput,userVO);
	}
	/**
	 * 复核数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getCheckDataTable(DataTablesInput dataTablesInput,ICurrentUser user) {
		return fcAccHistoryService.getCheckDataTable(dataTablesInput,user);
	}
	/**
	 * 复核数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getCheckDataTableForPool(DataTablesInput dataTablesInput,ICurrentUser user) {
		return fcAccHistoryService.getCheckDataTableForPool(dataTablesInput,user);
	}
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FcAccHistoryVO findFcAccHistoryVOById(String id) {
		FcAccHistoryVO fcAcc = fcAccHistoryService.findFcAccHistoryVOById(id);
		return fcAcc;
	}
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FcAccHistoryVO findFcAccHistoryVOByIdForPool(String id) {
		FcAccHistoryVO fcAcc = fcAccHistoryService.findFcAccHistoryVOByIdForPool(id);
		return fcAcc;
	}
	/**
	 * 根据账号查询历史账户对象
	 *
	 * @param blnctrAcc
	 * @return
	 */
	public FcAccHistoryVO findFcAccHistoryVOByBlnctrAcc(String blnctrAcc) {
		FcAccHistoryVO fcAcc = fcAccHistoryService.findFcAccHistoryVOByBlnctrAcc(blnctrAcc);
		return fcAcc;
	}
	/**
	 * 根据账号信息查找计息账号列表
	 * @param vo
	 * @return
	 */
	public List<FcAccHistoryVO> getAccListByExpPay(FcAccHistoryVO vo) {
		List<com.bytter.system.api.vo.FcAccHistoryVO> accList = fcAccHistoryService.getAccListByExpPay(vo);
		return accList;
	}
	/**
	 * 根据账号信息查找计息账号
	 * @param vo
	 * @return
	 */
	public FcAccHistoryVO getFcAccHistoryVO(FcAccHistoryVO vo) {
		FcAccHistoryVO accList = fcAccHistoryService.getFcAccHistoryVO(vo);
		return accList;
	}
	/**
	 * 根据账号删除关联计息账号
	 * @param entity
	 * @param isDel 是否删除
	 */
	public void delInterestAcc(FcAccHistoryVO entity,boolean isDel){
		if(!EmptyUtils.isEmpty(entity) && !EmptyUtils.isEmpty(entity.getBlnctrAcc()) ){
			FcAccHistoryVO vo = new FcAccHistoryVO();
			vo.setBlnctrAcc(entity.getBlnctrAcc());
			List<FcAccHistoryVO> accListByExpPay = fcAccHistoryService.getAccListByExpPay(vo);
			if(!EmptyUtils.isEmptyList(accListByExpPay)){
				for(FcAccHistoryVO accVO : accListByExpPay){
					if(isDel){
						this.delete(accVO.getId());
					}else{
						FcAccHistoryVO historyvo = fcAccHistoryService.getFcAccHistoryVOById(accVO.getId());
						FcAccHistoryVO fcAccHistory = new FcAccHistoryVO();
						BeanUtils.copyProperties(historyvo, fcAccHistory);
						if(!StringUtils.isBlank(entity.getAccType()) && InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(entity.getAccType())){
							FcSubjectDTO subjectDto = fcSubjectService.findSubjectById(entity.getSubjectId());
							if(!EmptyUtils.isEmpty(subjectDto) && !EmptyUtils.isEmpty(subjectDto.getSysSubjectCode())){
								//系统科目：应付利息-企业活期存款（2011）,应付利息-企业定期存款（2012）,应付利息-通知存款（2013）,应付利息-企业协定（2016）,
								// 应收利息-银行活期存款（1032）,应付利息-银行借款（2014）,应收利息-企业放款（1031）,这几个科目放入INTEREST_REC_PAY字段中
								if(SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(subjectDto.getSysSubjectCode())){
									fcAccHistory.setInterestRecPay("");
								}
								//系统科目：利息支出-企业活期存款（5212）,利息支出—企业定期存款（5211）,利息支出-通知存款（5213）,利息支出-企业协定（5216）,
								//利息收入-银行活期存款（5012）,利息支出-银行借款（5214）,利息收入-企业放款（5011）
								if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
										|| SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(subjectDto.getSysSubjectCode())){
									fcAccHistory.setInterestInExp("");
								}
							}
						}
						FcAccHistoryVO history = new FcAccHistoryVO();
						BeanUtils.copyProperties(fcAccHistory, history);
						this.saveOrUpdate(history);
					}
				}
			}
		}
	}
}
