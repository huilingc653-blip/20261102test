package com.bytter.biz.finacial.paramconfig.service;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.paramconfig.dao.BtSummaryDao;
import com.bytter.biz.finacial.paramconfig.entity.BtSummary;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
/**   
 * @Title: Service  
 * @Description: BT_SUMMARY摘要词组维护
 * @author Lixi
 * @date 2017-05-16 15:36:55
 * @version V1.0   
 *
 */
@Service("btSummaryService")
@Transactional
public class BtSummaryService extends BaseService<BtSummary,String> {
	@Autowired
	private BtSummaryDao btSummaryDao;
	/**
	 * 加载数据
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput dataTable(DataTablesInput dataTablesInput){
		return btSummaryDao.dataTable(dataTablesInput);
	}
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BtSummary entity){
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
	* 更新
	* @param entity
	*/
	public void update(BtSummary entity, String userId){
		entity.setUpdateBy(userId);
		entity.setUpdateTime(new Date());
		super.updateEntity(entity);
	}
	
	/**
	* 保存
	* @param entity
	*/
	public void save(BtSummary summary, String userId) {
		// 设置摘要代码
		/*if (StringUtils.isEmpty(summary.getSummaryCode())) {
			String maxSummaryCode = btSummaryDao.getMaxSummaryCode();
			if (StringUtils.isBlank(maxSummaryCode)) {
				maxSummaryCode = "0";
			}
			Long maxOrder = Long.valueOf(maxSummaryCode.trim());
			maxOrder = maxOrder + 1;
			summary.setSummaryCode(StringUtils.leftPad(maxOrder + "", 4, "0"));
		}*/
		if (!EmptyUtils.isEmptyList(listByContent(summary.getSummaryContent()))) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_SUMMARY_EXISTS);//摘要已存在
		}
		summary.setCreateBy(userId);
		summary.setCreateTime(new Date());
		super.saveEntity(summary);
	}
	public BaseDao<BtSummary, String> getEntityDao() {
		return btSummaryDao;
	}
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtSummary> listById(String id){
		return btSummaryDao.listById(id);
		//return list;
	}
	/**
	 * 获取有效的摘要词组
	 * @param
	 */
	@Transactional(readOnly = true)
	public List<BtSummary> getListByValidSign(){
		return btSummaryDao.getListByValidSign();
	}
	
	/**
	 * <b>功能介绍：</b>根据内容查询<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param content
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtSummary> listByContent(String content){
		return btSummaryDao.listByContent(content);
	}
	
	/**
	 * 功能介绍 : 外模块保存摘要信息。<br>
	 * <b>日期：</b>2017年11月7日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void saveSummaryByOuterSys(String userId, String... summaryContents) {
        for (String summaryContent : summaryContents) {
        	if (EmptyUtils.isEmptyList(listByContent(summaryContent))) {
                BtSummary summary = new BtSummary();
                summary.setSummaryContent(summaryContent);
                summary.setValidSign(FinacialConstant.VALID_TRUE);
                save(summary, userId);
            }
        }
    } 
	
}
