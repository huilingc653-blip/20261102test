package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.vo.BisBankDtlBillVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.dao.BisBankReceiptDtlDao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 银行电子回单
 *
 * @author zuoy 2018-04-09
 */
@Service("bisBankReceiptDtlService")
@Transactional
public class BisBankReceiptDtlService extends BaseService<BisBankReceiptDtl, String> {
    @Autowired
    private BisBankReceiptDtlDao bisBankReceiptDtlDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @Override
    public BaseDao<BisBankReceiptDtl, String> getEntityDao() {
        return bisBankReceiptDtlDao;
    }
    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) {
        //对同一个电子回单关联的流水和付款单的id用“/”连接起来
        DataTablesOutput dataTablesOutput = this.bisBankReceiptDtlDao.getDataTablesUnion(input, commonSql);
        for (int i = 0; i < dataTablesOutput.getData().size(); i++) {
            BisBankReceiptDtlVO bisBankReceiptDtlVO = (BisBankReceiptDtlVO) dataTablesOutput.getData().get(i);
            List<BisBankDtlBillVO> bisBankDtlBillVOS= bisBankReceiptDtlDao.getDtls(bisBankReceiptDtlVO.getId());
            bisBankReceiptDtlVO.setBillFrom("no_one");
            bisBankReceiptDtlVO.setBisBankDtlBillVOList(bisBankDtlBillVOS);
        }
        return dataTablesOutput;
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnion(String id) {
        return this.bisBankReceiptDtlDao.getByIdUnion(id);
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        this.bisBankReceiptDtlDao.updatePrint(id);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnionV2(String id) {
       return this.bisBankReceiptDtlDao.getByIdUnionV2(id);
    }
    /**
     * 自动迁移数据调用方法
     *
     * @param date
     * @return
     */
    public DataTablesOutput getByTime(DataTablesInput input, Date date) {
        return this.bisBankReceiptDtlDao.getByTime(input, date);
    }
    /**
     * 自动迁移数据调用方法 获取需求迁移的总数
     *
     * @param date
     * @return
     */
    public BigDecimal getByTimeCount(Date date) {
        return this.bisBankReceiptDtlDao.getByTimeCount(date);
    }
    public List<BisBankReceiptDtlVO> listByIds(String ids) {
      return bisBankReceiptDtlDao.listByIds(ids);
    }

    /**
     * 根据付款表id得到电子回单表ID集合
     * @param payId
     * @return 回单表ID集合
     */
    public List<String> getIdsByPayId(String payId) {
        return this.bisBankReceiptDtlDao.getIdsByPayId(payId);
    }

}
