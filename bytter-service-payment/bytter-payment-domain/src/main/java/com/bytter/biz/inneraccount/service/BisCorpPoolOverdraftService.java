package com.bytter.biz.inneraccount.service;

import com.bytter.biz.inneraccount.dao.BisCorpPoolOverdraftDao;
import com.bytter.biz.inneraccount.entity.BisCorpPoolOverdraft;
import com.bytter.biz.inneraccount.vo.BisCorpPoolOverdraftVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Title: Service
 * @Description: 多级单位余额核算-单位归集透支金额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Service("bisCorpPoolOverdraftService")
@Transactional
public class BisCorpPoolOverdraftService extends BaseService<BisCorpPoolOverdraft, String> implements IExcelVerifyHandler<BisCorpPoolOverdraftVO> {
    @Autowired
    private BisCorpPoolOverdraftDao bisCorpPoolOverdraftDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<BisCorpPoolOverdraft, String> getEntityDao() {
        return bisCorpPoolOverdraftDao;
    }

    /**
     * 查询 单位归集透支金额表 列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder joinSql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.BANKACC_BISCORPPOOLOVERDRAFT_QUERY, user.getId()), "b.corp_id",
                true);
        joinSql.append(" and ").append(corpAuthSql);
        return bisCorpPoolOverdraftDao.getDataTablesUnion(dataTablesInput, joinSql.toString());
    }

    /**
     * 根据ID获取单位归集透支金额表
     * @param id
     * @return
     */
    public BisCorpPoolOverdraftVO getByIdUnion(String id) {
        return bisCorpPoolOverdraftDao.getByIdUnion(id);
    }

    /**
     * 保存 单位归集透支金额数据
     * @param bisCorpPoolOverdraft
     * @param user
     */
    public void saveBisCorpPoolOverdraft(BisCorpPoolOverdraftVO bisCorpPoolOverdraft, ICurrentUser user) {
        BisCorpPoolOverdraft entity = new BisCorpPoolOverdraft();
        BeanUtils.copyNotNullProperties(bisCorpPoolOverdraft, entity);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateTime(DateUtils.getDate());
        this.saveEntity(entity);
    }

    /**
     * 编辑保存 单位归集透支金额数据
     * @param bisCorpPoolOverdraft
     * @param user
     */
    public void updateBisCorpPoolOverdraft(BisCorpPoolOverdraftVO bisCorpPoolOverdraft, ICurrentUser user) {
        BisCorpPoolOverdraft entity = new BisCorpPoolOverdraft();
        BeanUtils.copyNotNullProperties(bisCorpPoolOverdraft, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateTime(DateUtils.getDate());
        this.updateEntity(entity);
    }

    /**
     * 导入保存 单位归集透支金额数据
     * @param list
     * @param user
     */
    public void saveImport(List<BisCorpPoolOverdraftVO> list, ICurrentUser user) {
        List<BisCorpPoolOverdraft> bals = new ArrayList<>();
        for (BisCorpPoolOverdraftVO vo:list) {
            if (!this.validRepeat(vo)) {
                throw new ParameterException(NetbankMessageEnum.NETBANK_MESSAGE_BISCORPPOOLOVERDRAFT_REPEAT);
            }
            BisCorpPoolOverdraft entity = new BisCorpPoolOverdraft();
            BeanUtils.copyNotNullProperties(vo, entity);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateTime(DateUtils.getDate());
            bals.add(entity);
        }
        this.batchSave(bals);
    }

    /**
     * 删除 单位归集透支金额数据
     * @param ids
     */
    public void delBisCorpPoolOverdraft(String ids) {
        bisCorpPoolOverdraftDao.delBisCorpPoolOverdraft(ids);
    }

    /**
     * 存储校验
     * @param vo
     * @return
     */
    public boolean validRepeat(BisCorpPoolOverdraftVO vo) {
        List<BisCorpPoolOverdraftVO> list = bisCorpPoolOverdraftDao.listValidByCorpId(vo);
        if (EmptyUtils.isNotEmpty(list)) {
            return false;
        }
        return true;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BisCorpPoolOverdraftVO vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<BisCorpPoolOverdraftVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            //币种集合
            HashMap<String, String> curMap = new HashMap<>();
            //重复数据集合
            HashMap<String, String> repeatDataMap = new HashMap<>();
            for (BisCorpPoolOverdraftVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp = corpMap.get(vo.getCorpName());
                if (corp == null) {
                    corp = this.corpService.getCorpIdByName(vo.getCorpName());
                    corpMap.put(vo.getCorpName(), corp);
                }
                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                    msgList.add("nis.netbank.message.corp.null");
                } else {
                    vo.setCorpId(corp.getId());
                }

                //校验币别
                String curId = curMap.get(vo.getCurName());
                if (StringUtils.isBlank(curId)) {
                    curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                    curMap.put(vo.getCurName(), curId);
                }
                if (EmptyUtils.isEmpty(curId)) {
                    msgList.add("cms.guacontract.cur.error");
                } else {
                    vo.setCurId(curId);
                }

                //余额不能为空且必须必须要大于0
                if (EmptyUtils.isEmpty(vo.getOverdraftAmt()) || vo.getOverdraftAmt().compareTo(BigDecimal.ZERO) <= 0) {
                    msgList.add("nis.netbank.message.money.mismatch");
                }

                // 重复校验
                String key = vo.getCorpId() + vo.getCurId();
                if (repeatDataMap.containsKey(key)) {
                    msgList.add("netbank.message.biscorppooloverdraft.repeat");
                } else {
                    if (!validRepeat(vo)) {
                        msgList.add("netbank.message.biscorppooloverdraft.repeat");
                    }
                }
                repeatDataMap.put(key, key);
                vo.addErrorMsg(msgList);
            }
        }
    }
}
