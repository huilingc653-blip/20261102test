package com.bytter.biz.budget.service.impl;
import java.util.List;
import java.util.Set;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.service.FbsItemService;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author zhangxb
 * @date 2017/5/22 0022.
 */
@Service("fbsItemServiceImpl")
@Transactional
@DubboService
public class FbsItemServiceImpl implements IFbsItemService {
    @Autowired
    private FbsItemService fbsItemService;
    /**
     * 有效预算列表
     * @return
     */
    @Override
	public List<FbsItemDTO> listFbsItemInfoValid(){
        return listFbsItemInfo("1");
    }
    /**
     * 根据预算列表查询账号列表
     * @param validSign 可空
     * @return
     */
    @Override
	public List<FbsItemDTO> listFbsItemInfo(String validSign){
        List<FbsItemDTO> list = listFbsItemInfoBySign(validSign,0);
        return list;
    }
    /**
     * 根据资金流向1， 资金流入-1 ，资金流出   预算列表查询账号列表
     *
     * @param validSign 可空
     * @param sign 不可空
     * @return
     */
    @Override
	public List<FbsItemDTO> listFbsItemInfoBySign(String validSign, Integer sign){
        FbsItemVO fbsItem = new FbsItemVO();
        fbsItem.setSign(sign);
        List<FbsItemVO> fbsItemVO = fbsItemService.listTreeData(fbsItem,validSign);
        List<FbsItemDTO> list = BeanUtils.copyListProperties(fbsItemVO,FbsItemDTO.class);
        return list;
    }
    /**
     * 根据科目代码查找科目信息
     * @param itemCode
     * @return
     */
    @Override
    public FbsItemDTO getFbsItemByCode(String itemCode) {
        FbsItem fbsItem = fbsItemService.getFbsItemByCode(itemCode);
        if(EmptyUtils.isEmpty(fbsItem)){
            return null;
        }
        FbsItemDTO fbsItemDTO = BeanUtils.copyNewInstanceProperties(fbsItem, FbsItemDTO.class);
        return fbsItemDTO;
    }
    /**
     * 根据资金流向获取可执行的计划科目集合
     * @param sign 资金流向 -1 流出 1 流入
     * @return
     */
    @Override
    public List<FbsItemDTO> listFbsItemForProcess(Integer sign) {
        List<FbsItemVO> fbsItemVO = fbsItemService.listFbsItemForProcess(sign);
        List<FbsItemDTO> list = BeanUtils.copyListProperties(fbsItemVO,FbsItemDTO.class);
        return list;
    }
    /**
     * 获取计划执行科目
     * @return
     */
    @Override
    public String getFbsItemForProcess(Integer sign) {
        return fbsItemService.getFbsItemForProcess(sign);
    }
    
    @Override
    public List<com.bytter.biz.budget.spi.vo.FbsItemVO> getFbsItemVOS(Set<String> itemCodes) {
    	return fbsItemService.getFbsItemVOS(itemCodes);
    }
}
