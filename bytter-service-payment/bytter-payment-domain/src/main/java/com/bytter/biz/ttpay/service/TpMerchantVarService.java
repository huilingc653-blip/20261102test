package com.bytter.biz.ttpay.service;
import com.bytter.biz.ttpay.dao.TpMerchantVarDao;
import com.bytter.biz.ttpay.entity.TpMerchantVar;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.biz.ttpay.vo.TpMerchantVarVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAttachmentService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @Title: Service
 * @Description: 商户信息Service
 * @author 郑刚
 * @date 2019-09-26 15:43:05
 * @version 1.0
 */
@Service("tpMerchantVarService")
@Transactional
public class TpMerchantVarService extends BaseService<TpMerchantVar, String> {
	
	@Autowired
	private TpMerchantVarDao tpMerchantVarDao;
	
	@DubboReference(check = false)
	private IAttachmentService attachmentService;
	
	@Override
	public BaseDao<TpMerchantVar, String> getEntityDao() {
		return tpMerchantVarDao;
	}
	
	/**
	 * 根据客商信息id获取对应参数信息
	 * @param mchId
	 * @return
	 */
	public List<TpMerchantVar> getTpMerchantVarByMchId(String mchId){
		return tpMerchantVarDao.getTpMerchantVarByMchId(mchId);
	}
	
	/**
	 * 根据客商信息id获取对应参数信息
	 * @param mchId
	 * @return
	 */
	public List<TpMerchantVarVO> getTpMerchantVarVOByMchId(String mchId){
		List<TpMerchantVar> list = this.getTpMerchantVarByMchId(mchId);
		if(list==null||list.size()<1){
			return null;
		}
		List<TpMerchantVarVO> voList=BeanUtils.copyListProperties(list, TpMerchantVarVO.class);
		for(TpMerchantVarVO info:voList){
			if(StringUtils.isNotEmpty(info.getFileId())){
				info.setAttachDTO(attachmentService.findAttachmentById(info.getFileId()));
			}
		}
		return voList;
	}
	/**
	 * 根据客商信息id删除对应参数信息
	 * @param mchId
	 * @return
	 */
	public int deleteTpMerchantVarByMchId(String mchId){
		return tpMerchantVarDao.deleteTpMerchantVarByMchId(mchId);
	}
	/**
	 * 对商户参数信息进行新增
	 */
	public void saveMerchantVar(TpMerchantVO infoVO){
		if(infoVO!=null
				&&infoVO.getMerchantVarList()!=null
				&&infoVO.getMerchantVarList().size()>0){
			List<TpMerchantVar> varInfoList=new ArrayList<>();
			double i=0;
			for(TpMerchantVarVO varVO:infoVO.getMerchantVarList()){
				TpMerchantVar varInfo=new TpMerchantVar();
				BeanUtils.copyNotNullProperties(varVO, varInfo);
				varInfo.setMchId(infoVO.getId());
				varInfo.setSort(++i);
				if(StringUtils.isNotEmpty(varInfo.getFileId())
						&&attachmentService!=null){
					byte[] fileData=attachmentService.getAttachmentDataByid(varInfo.getFileId());
					if(fileData!=null
							&&fileData.length>0){
						varInfo.setVarValue(new String(fileData));
					}
				} else {
				    //判断是否为应用ID，应用ID额外存在var_value_str字段
                    if (StringUtils.isNotEmpty(varInfo.getVarCode()) && varVO.getVarCode().contains("appId")) {
                        varInfo.setVarValueStr(varInfo.getVarValue());
                    }
                }
				varInfoList.add(varInfo);
			}
			tpMerchantVarDao.batchJdbcSave(varInfoList);
		}
	}
}
