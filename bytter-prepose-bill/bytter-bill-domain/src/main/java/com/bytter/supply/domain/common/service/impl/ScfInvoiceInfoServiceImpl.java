package com.bytter.supply.domain.common.service.impl;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.supply.domain.common.dao.ScfInvoiceInfoDao;
import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.bytter.supply.domain.common.service.ScfInvoiceInfoService;
import com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfInvoiceInfoServiceImpl
 * @Description: 应付/应收债券发票信息服务实现
 * @since 2026/7/3
 */
@Service("ScfInvoiceInfoService")
@Transactional
public class ScfInvoiceInfoServiceImpl extends BaseService<ScfInvoiceInfo, String> implements ScfInvoiceInfoService {

    @Autowired
    private ScfInvoiceInfoDao scfInvoiceInfoDao;

    @Override
    public BaseDao<ScfInvoiceInfo, String> getEntityDao() {
        return scfInvoiceInfoDao;
    }

    /**
     * 列表查询
     */
    @Override
    public DataTablesOutput<ScfInvoiceInfoVO> dataTable(DataTablesInput input) {
        return scfInvoiceInfoDao.dataTable(input);
    }

    @Override
    public boolean saveInvoiceInfo(ScfInvoiceInfoDTO invoiceInfoDTO) {
        ScfInvoiceInfo invoiceInfo = new ScfInvoiceInfo();
        BeanUtils.copyNotNullProperties(invoiceInfoDTO, invoiceInfo);
        saveEntity(invoiceInfo);
        return true;
    }

    @Override
    public boolean updateInvoiceInfo(ScfInvoiceInfo invoiceInfo) {
        updateEntity(invoiceInfo);
        return true;
    }

    @Override
    public boolean deleteInvoiceInfo(String id) {
        delete(id);
        return true;
    }

    @Override
    public ScfInvoiceInfoVO getInvoiceInfo(String id) {
        ScfInvoiceInfo invoiceInfo = get(id);
        if (invoiceInfo == null) {
            return null;
        }
        ScfInvoiceInfoVO vo = new ScfInvoiceInfoVO();
        BeanUtils.copyNotNullProperties(invoiceInfo, vo);
        return vo;
    }

    @Override
    public ScfInvoiceInfo getInvoiceInfoById(String id) {
        return get(id);
    }

    @Override
    public List<ScfInvoiceInfo> listBy(String propertyName, Object value) {
        return getEntityDao().listBy(propertyName, value);
    }
}
