package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.paramconfig.dao.FcSetMonthDao;
import com.bytter.biz.finacial.paramconfig.entity.FcSetMonth;
import com.bytter.biz.finacial.paramconfig.vo.FcSetMonthVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 月结区间
* @author onlineGenerator
* @date 2017-05-24 14:18:47
* @version 1.0
*
*/
@Service("fcSetMonthService")
@Transactional
public class FcSetMonthService extends BaseService<FcSetMonth,String> {
	@Autowired
	private FcSetMonthDao fcSetMonthDao;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IUserService userService;
	/**
	 * 查询月结区间，未查询到数据，构建十二个自然月区间
	 * @return
	 */
	public List<FcSetMonthVO> listFcSetMonth(FcSetMonth fsm,ICurrentUser user){
		List<FcSetMonthVO> listFsm = fcSetMonthDao.listFcSetMonth(fsm);
		//未查询到数据，构建十二个自然月区间
		if(EmptyUtils.isEmptyList(listFsm)){
			listFsm = new ArrayList<FcSetMonthVO>();
            int year = Integer.parseInt(fsm.getOverMonth());
            String netCode = fsm.getNetId();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            for( int i = 0 ; i < 12 ; i++ ){
                FcSetMonthVO setMonth = new FcSetMonthVO();
                setMonth.setNetId(netCode);
                setMonth.setOverMonth( i < 9 ? year + "0" + ( i + 1 ) : year + "" + ( i + 1 ) );
                //设置开始日期和结束日期
                cal.set(Calendar.MONTH, i);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                setMonth.setDateStart(cal.getTime());
                cal.set(Calendar.MONTH, i);
                int maxday =cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                cal.set(Calendar.DAY_OF_MONTH, maxday);
                setMonth.setDateEnd(cal.getTime());
                setMonth.setStatus("0");
                //设置网点的值 假如没有设置为当前结算中心
                if(EmptyUtils.isBlank(fsm.getNetId())){
					CorpDTO userNet = this.getUserNet(user);
					//Map m = fcSetMonthDao.getAllCorp("");
					setMonth.setNetId(userNet.getId());
					setMonth.setCorpName(userNet.getName());
                }else{
					//Map m = fcSetMonthDao.getAllCorp(fsm.getNetId());
					CorpDTO corp = iCorpService.get(fsm.getNetId());
					setMonth.setNetId(corp.getId());
					setMonth.setCorpName(corp.getName());
                }
                listFsm.add(setMonth);
            }
		}else{
			List<String> corpIds = listFsm.stream().map(FcSetMonthVO::getNetId).collect(Collectors.toList());
			Map<String, String> corpMap = iCorpService.getNameByTypeAndIds(corpIds, 0);
			//月结区间已设值时，设置状态
            for(FcSetMonthVO  fs : listFsm){
				fs.setStatus("1");
				fs.setCorpName(corpMap.get(fs.getNetId()));
            }
        }
		return listFsm;
	}
	//获取查询条件年份
	public int[] getYears(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int startYear = year-5;
		int endYear   = year+5;
		int cn = 0;
		int[] yearItems = new int[10];
		while(startYear<endYear){
			yearItems[cn++] = startYear++;
		}
		return yearItems;
	}
	/**
	 * 获取当前登陆用户的网点ID
	 */
	public CorpDTO getUserNet(ICurrentUser user){
		//UserDTO userDTO = userService.getByLoginName(user.getLoginName());
		CorpDTO corpDto = iCorpService.get(user.getCorpId());
		while (CorpConstant.CORP_TYPE_CODE_CORP.equals(corpDto.getType().toString())){
			String parentId = corpDto.getParentId();
			if (!StringUtils.isEmpty(parentId)) {
				corpDto = iCorpService.get(parentId);
			} else {
				break;
			}
		}
		return corpDto;
	}
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcSetMonth entity){
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
	public void save(FcSetMonth entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcSetMonth entity){
		super.updateEntity(entity);
	}
	/**
	 * 设置初始值
	 * @param entity
	 */
	public FcSetMonth setInitdata(FcSetMonth fsm, ICurrentUser user){
		int[]years = getYears();
		if(EmptyUtils.isBlank(fsm.getNetId())){
			CorpDTO userNet = this.getUserNet(user);
			fsm.setNetId(userNet.getId());
		}
		if(EmptyUtils.isBlank(fsm.getOverMonth())){
			fsm.setOverMonth(years[5]+"");
		}
		return fsm;
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcSetMonth entity){
		super.saveOrUpdate(entity);
	}
	/**
	 * 保存
	 * @param
	 */
	public void saveSetMonth(FcSetMonthVO vo ){
		List<FcSetMonth> fsm = vo.getFsm();
		List<FcSetMonthVO> list = fcSetMonthDao.listFcSetMonth(fsm.get(0));
		if(EmptyUtils.isEmptyList(list)){
			batchSave(fsm);
		}else{
			for (FcSetMonth fcSetMonth : fsm) {
				FcSetMonth tempSetMonth = new FcSetMonth();
				BeanUtils.copyProperties(fcSetMonth, tempSetMonth);
				update(tempSetMonth);
			}
		}
	}
	public BaseDao<FcSetMonth, String> getEntityDao() {
		return fcSetMonthDao;
	}
	/**
	 * 功能介绍 : 获取查询网点月结月份的月结区间。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public FcSetMonth getNetSetMonth(String netId, String overMonth) {
		return fcSetMonthDao.getNetSetMonth(netId, overMonth);
	}
}
