package com.bytter.finance.domain.precative.service.impl;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.utils.BeanUtils;
import com.bytter.finance.api.ICmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanRepayDao;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanSendDao;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;

@Service("cmsPrecativeLoanSendServiceImpl")
@DubboService
@Transactional
public class CmsPrecativeLoanSendServiceImpl implements ICmsPrecativeLoanSendService {
    @Autowired
    private CmsPrecativeLoanSendDao cmsPrecativeLoanSendDao;
    @Autowired
    private CmsPrecativeLoanRepayDao cmsPrecativeLoanRepayDao;
    
    public CmsPrecativeLoanSendDTO getPrecativeLoanSendVoByVo(CmsPrecativeLoanSendDTO vo) {
        return cmsPrecativeLoanSendDao.getPrecativeLoanSendVoByVo(vo);
    }

    @Override
    public CmsPrecativeLoanSendDTO getById(String id) {
        CmsPrecativeLoanSend loanSend = cmsPrecativeLoanSendDao.get(id);
        CmsPrecativeLoanSendDTO dto = new CmsPrecativeLoanSendDTO();
        BeanUtils.copyProperties(loanSend,dto);
        return dto;
    }
    
    @Override
    public List<CmsPrecativeLoanRepayDTO> queryRepayListByBillCodes(List<String> billCodes) {
    	List<CmsPrecativeLoanRepayDTO> listDto = new ArrayList<CmsPrecativeLoanRepayDTO>();
    	List<CmsPrecativeLoanRepayVo> list = cmsPrecativeLoanRepayDao.queryRepayListByBillCodes(billCodes,"");
    	if(!CollectionUtils.isEmpty(list)) {
    		listDto = BeanUtils.copyListProperties(list, CmsPrecativeLoanRepayDTO.class);
    	}
        return listDto;
    }
}
