package com.bytter.bill.domain.bill.service;
// import com.bytter.bill.domain.bill.dao.AbBillAttachmentDao;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillAttachmentDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillAttachment;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.v1.AbEmitBillAddVo;
import com.bytter.bill.domain.bill.vo.v1.AbReceiveBillViewPageVO;
import com.bytter.bill.domain.bill.vo.v1.ApproveVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: Service
* @Description: 票据附件操作
* @author onlineGenerator
* @date 2017-10-24 14:33:51
* @version 1.0
*
*/
@Service("abBillAttachmentService")
@Transactional
public class AbBillAttachmentService extends BaseService<AbBillAttachment,String> {
	@Autowired
	private AbBillAttachmentDao abBillAttachmentDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(AbBillAttachment entity){
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
 	public void save(AbBillAttachment entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbBillAttachment entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(AbBillAttachment entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<AbBillAttachment, String> getEntityDao() {
		return abBillAttachmentDao;
	}
	
	/**
	 * 附件业务操作
	 * @param acVoInfo 流水对象VO
	 * @param accInfo 流水对象
	 */
	public void opAttachmentInfo(AbAccrecordsVO acVoInfo,AbAccrecords accInfo,ICurrentUser userVo){
		acVoInfo.setId(accInfo.getId());
		acVoInfo.setVid(accInfo.getVid());
		acVoInfo.setBriefId(accInfo.getBriefId());
		opAttachmentInfo(acVoInfo,userVo);
	}
	
	/**
	 * 预览查看
	 * @param accId
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = true)
	public void viewAttachment(String accId, Model model){
		AbBillAttachmentVO attVo=viewAttachment(accId);
		model.addAttribute("attachmentInfo", attVo);
		if(StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())){
			AbBillAttachmentVO attachmentInfoDiscount = viewAttachmentByDiscount(accId);
			model.addAttribute("attachmentInfoDiscount", attachmentInfoDiscount);
		}
		boolean isAttachmentShow=isAttachmentShow(attVo.getBriefId());
		model.addAttribute("isAttachmentShow", isAttachmentShow);
		model.addAttribute("bankTypeName", StringUtils.isNotEmpty(attVo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()):null);
	}


	public void viewAttachment(String accId, AbReceiveBillViewPageVO pageVO){
		AbBillAttachmentVO attVo=viewAttachment(accId);
		pageVO.setAttachmentInfo(attVo);
		if(StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())){
			AbBillAttachmentVO attachmentInfoDiscount = viewAttachmentByDiscount(accId);
			pageVO.setAttachmentInfoDiscount(attachmentInfoDiscount);
		}
		boolean isAttachmentShow=isAttachmentShow(attVo.getBriefId());
		pageVO.setIsAttachmentShow(isAttachmentShow);
		pageVO.setBankTypeName(StringUtils.isNotEmpty(attVo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()):null);
	}

    @Transactional(readOnly = true)
    public void viewAttachmentV1(String accId, ApproveVO model) {
        AbBillAttachmentVO attVo = viewAttachment(accId);
        model.setAttachmentInfo(attVo);
        if (StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())) {
            AbBillAttachmentVO attachmentInfoDiscount = viewAttachmentByDiscount(accId);
            model.setAttachmentInfoDiscount(attachmentInfoDiscount);
        }
        boolean isAttachmentShow = isAttachmentShow(attVo.getBriefId());
        model.setIsAttachmentShow(isAttachmentShow);
        model.setBankTypeName(StringUtils.isNotEmpty(attVo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()) : null);
    }

    @Transactional(readOnly = true)
    public void viewAttachmentV1(String accId, AbEmitBillAddVo model) {
        AbBillAttachmentVO attVo = viewAttachment(accId);
        model.setAttachmentInfo(attVo);
        if (StringUtils.isNotEmpty(attVo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(attVo.getBriefId())) {
            AbBillAttachmentVO attachmentInfoDiscount = viewAttachmentByDiscount(accId);
            model.setAttachmentInfoDiscount(attachmentInfoDiscount);
        }
        boolean isAttachmentShow = isAttachmentShow(attVo.getBriefId());
        model.setIsAttachmentShow(isAttachmentShow);
        model.setBankTypeName(StringUtils.isNotEmpty(attVo.getVvid()) ? abVoucherinfoViceService.findBankTypeNameWith(attVo.getVvid()) : null);
    }

	
	/**
	 * 预览查看
	 * @param accId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAttachmentVO viewAttachment(String accId){
		AbAccrecordsVO acVoInfo=new AbAccrecordsVO();
		acVoInfo.setId(accId);
		acVoInfo.setIsNotOp("1");
		AbBillAttachmentVO attVo = getAbBillAttachmentVO(acVoInfo);
		attVo.setIsExpand("true");
		return attVo;
	}
	/**
	 * 贴现办理预览查看
	 * @param accId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAttachmentVO viewAttachmentByDiscount(String accId){
		AbAccrecordsVO acVoInfo=new AbAccrecordsVO();
		acVoInfo.setId(accId);
		acVoInfo.setIsNotOp("1");
		AbBillAttachmentVO attVo = getAbBillAttachmentVOByDiscount(acVoInfo);
		attVo.setIsExpand("true");
		return attVo;
	}
	/**
	 * 编辑修改
	 * @param accId
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = true)
	public void editAttachment(String accId, Model model){
		AbBillAttachmentVO attVo=editAttachment(accId);
		model.addAttribute("attachmentInfo", attVo);
	}
	
	/**
	 * 编辑修改
	 * @param accId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAttachmentVO editAttachment(String accId){
		AbAccrecordsVO acVoInfo=new AbAccrecordsVO();
		acVoInfo.setIsNotOp("0");
		acVoInfo.setId(accId);
		AbBillAttachmentVO attVo = getAbBillAttachmentVO(acVoInfo);
		attVo.setIsExpand("false");
		return attVo;
	}
	
	/**
	 * 附件业务操作
	 * @param acVoInfo 流水对象VO
	 */
	public void opAttachmentInfo(AbAccrecordsVO acVoInfo,ICurrentUser userVo){
		if(acVoInfo!=null&&StringUtils.isNotEmpty(acVoInfo.getAttachmentId())){
			int attachmentNum=acVoInfo.getAttachmentNum()==null?0:acVoInfo.getAttachmentNum();
			Map<String, Object> params=new HashMap<>();
			params.put("attachmentId", acVoInfo.getAttachmentId());
			AbBillAttachment attInfo=abBillAttachmentDao.getAbBillAttachmentByParams(params);
			if(attInfo==null){
				attInfo=new AbBillAttachment();
				attInfo.setCreateDate(new Date());
				if(userVo!=null){
					attInfo.setCreateBy(userVo.getLoginName());
					attInfo.setCreateName(userVo.getUsername());
				}
				attInfo.setAttachmentId(acVoInfo.getAttachmentId());
				attInfo.setAcid(acVoInfo.getId());
				if(StringUtils.isNotEmpty(acVoInfo.getId())
						&&StringUtils.isEmpty(acVoInfo.getVvid())){
					AbAccrecords accInfo= abAccrecordsService.get(acVoInfo.getId(), true);
					attInfo.setAcid(accInfo.getId());
					attInfo.setVvid(accInfo.getVvid());
					attInfo.setBriefId(accInfo.getBriefId());
				}else{
					attInfo.setAcid(acVoInfo.getId());
					attInfo.setVvid(acVoInfo.getVvid());
					attInfo.setBriefId(acVoInfo.getBriefId());
				}
				attInfo.setNum(attachmentNum);
				this.save(attInfo);
			}else{
				attInfo.setUpdateDate(new Date());
				if(userVo!=null){
					attInfo.setUpdateBy(userVo.getLoginName());
					attInfo.setUpdateName(userVo.getUsername());
				}
				attInfo.setNum(attachmentNum);
				this.update(attInfo);
			}
		}
	}
	
	/**
	 * 跳转附件单独预览页面加载附件信息
	 * @param acVo 票据业务流水vo
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAttachmentVO getAbBillAttachmentVO(AbAccrecordsVO acVo){
		AbBillAttachmentVO attVo=new AbBillAttachmentVO();
		Map<String, Object> params=new HashMap<>();
		if(StringUtils.isNotEmpty(acVo.getAttachmentId())){
			params.put("attachmentId", acVo.getAttachmentId());
		}else if(StringUtils.isNotEmpty(acVo.getId())){
			AbAccrecords accInfo= abAccrecordsService.get(acVo.getId(), true);
			if(accInfo!=null){
				String briefId=acVo.getBriefId();
				if(StringUtils.isEmpty(briefId)){
					briefId=accInfo.getBriefId();
				}
				String[] briefIds=new String[]{briefId};
				if(AccebillConstant.BRIEF_STATUS_001.equals(accInfo.getBriefId())
						||AccebillConstant.BRIEF_STATUS_003.equals(accInfo.getBriefId())
						||AccebillConstant.BRIEF_STATUS_6101.equals(accInfo.getBriefId())){
					briefIds=new String[]{AccebillConstant.BRIEF_STATUS_001,AccebillConstant.BRIEF_STATUS_6101};
					params.put("vvid", accInfo.getVvid());
				}else{
					String[] acids=new String[]{accInfo.getId()};
					if(StringUtils.isNotEmpty(accInfo.getLastid())){
						acids=new String[]{accInfo.getId(),accInfo.getLastid()};
					}
					params.put("acids", acids);
				}
				attVo.setId(accInfo.getId());
				attVo.setVvid(accInfo.getVvid());
				attVo.setBriefId(briefId);
				params.put("briefIds", briefIds);
			}
		}
		if(!params.isEmpty()){
			AbBillAttachment attInfo=abBillAttachmentDao.getAbBillAttachmentByParams(params);
			if(attInfo!=null){
				attVo.setAttachmentId(attInfo.getAttachmentId());
			}
		}
		if("1".equals(acVo.getIsNotOp())){
			attVo.setHasUpload("false");
			attVo.setHasDelete("false");
		}else{
			attVo.setHasUpload("true");
			attVo.setHasDelete("true");
		}
		attVo.setIsExpand("true");
		return attVo;
	}
	/**
	 * 贴现办理专用：根据流水id查询贴现申请流水附件信息
	 * @param acVo 票据业务流水vo
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAttachmentVO getAbBillAttachmentVOByDiscount(AbAccrecordsVO acVo){
		AbBillAttachmentVO attVo=new AbBillAttachmentVO();
		Map<String, Object> params=new HashMap<>();
		if(StringUtils.isNotEmpty(acVo.getId())){
			AbAccrecords accInfo= abAccrecordsService.get(acVo.getId(), true);
			if(accInfo!=null){
				//当流水为贴现办理时
				if(AccebillConstant.BRIEF_STATUS_011.equals(accInfo.getBriefId()) && StringUtils.isNotEmpty(accInfo.getLastid())){
					AbAccrecords lastAccInfo= abAccrecordsService.get(accInfo.getLastid(), true);
					if(lastAccInfo!=null && StringUtils.isNotEmpty(lastAccInfo.getLastid())){
						String[] acids=new String[]{lastAccInfo.getLastid()};
						params.put("acids", acids);
					}
				}else if(AccebillConstant.BRIEF_STATUS_6011.equals(accInfo.getBriefId()) && StringUtils.isNotEmpty(accInfo.getLastid())){
					String[] acids=new String[]{accInfo.getLastid()};
					params.put("acids", acids);
				}
				String briefId=AccebillConstant.BRIEF_STATUS_6011;
				String[] briefIds=new String[]{briefId};
				attVo.setId(accInfo.getId());
				attVo.setVvid(accInfo.getVvid());
				attVo.setBriefId(briefId);
				params.put("briefIds", briefIds);
			}
		}
		if(!params.isEmpty()){
			AbBillAttachment attInfo=abBillAttachmentDao.getAbBillAttachmentByParams(params);
			if(attInfo!=null){
				attVo.setAttachmentId(attInfo.getAttachmentId());
			}
		}
		if("1".equals(acVo.getIsNotOp())){
			attVo.setHasUpload("false");
			attVo.setHasDelete("false");
		}else{
			attVo.setHasUpload("true");
			attVo.setHasDelete("true");
		}
		attVo.setIsExpand("true");
		return attVo;
	}
	/**
	 * 根据业务类似判断是否需要显示附件
	 * @param briefId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isAttachmentShow(String briefId){
		if(AccebillConstant.BRIEF_STATUS_001.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6101.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6001.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_010.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_011.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_018.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6011.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_020.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_021.equals(briefId)){
			return true;
		}
		return false;
	}
}
