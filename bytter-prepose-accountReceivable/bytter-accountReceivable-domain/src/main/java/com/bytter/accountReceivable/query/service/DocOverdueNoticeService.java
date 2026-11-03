package com.bytter.accountReceivable.query.service;

import com.bytter.accountReceivable.constant.ResourceConstant;
import com.bytter.accountReceivable.docManage.dao.DocManageDao;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.query.dao.DocOverdueNoticeDao;
import com.bytter.accountReceivable.query.entity.DocOverdueNotice;
import com.bytter.accountReceivable.query.vo.DocOverdueNoticeDtlVo;
import com.bytter.accountReceivable.query.vo.DocOverdueNoticeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 逾期催收登记表 service
 */
@Service("docOverdueNoticeService")
@Transactional
public class DocOverdueNoticeService extends BaseService<DocOverdueNotice, String> {

    @Autowired
    private DocOverdueNoticeDao docOverdueNoticeDao;
    @Autowired
    private DocManageDao docManageDao;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public BaseDao<DocOverdueNotice, String> getEntityDao() {
        return docOverdueNoticeDao;
    }

    /**
     * 获取逾期催收登记表查询 列表数据
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user){
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_DOCOVERDUENOTICE_QUERY, user.getId());
        String corpIdInSql = SqlUtil.getInSql(corpIds, "dm.corp_id", true);
        DataTablesOutput out = docManageDao.listQueryDocManage(input, corpIdInSql);
        List<DocManageVo> docManageVoList = out.getData();
        if(EmptyUtils.isNotEmpty(docManageVoList)){
            List<DocOverdueNoticeVo> overdueNoticeVoList = BeanUtils.copyListProperties(docManageVoList, DocOverdueNoticeVo.class);
            for (DocOverdueNoticeVo vo : overdueNoticeVoList) {
                vo.setWriteOffMoney(vo.getReceivableMoney().subtract(vo.getWriteOffBal()));
                if(DateUtils.dayDiffs(vo.getEndDate(),DateUtils.getDateYMD()) >= 0){
                    //未到期
                    //vo.setWriteOffBal(vo.getReceivableMoney());
                    vo.setUnexpiredMoney(vo.getWriteOffBal());
                    //vo.setWriteOffMoney(BigDecimal.ZERO);
                    vo.setOverdueMoney(BigDecimal.ZERO);
                }else {
                    //已到期
                    vo.setUnexpiredMoney(BigDecimal.ZERO);
                    vo.setOverdueMoney(vo.getWriteOffBal());
                }
                vo.setOverdueRate(vo.getOverdueMoney().divide(vo.getReceivableMoney(), 6, BigDecimal.ROUND_HALF_UP));
            }
            out.setData(overdueNoticeVoList);
        }
        return out;
    }

    /**
     * 根据应收单id获取 逾期催收登记数据
     * @param docId
     * @param user
     * @return
     */
    public DataTablesOutput getOverDueNoticeByDocId(String docId, ICurrentUser user){
        DataTablesOutput out = new DataTablesOutput();
        List<DocOverdueNotice> overDueNoticeByDocId = new ArrayList<>();
        if (StringUtils.isNotEmpty(docId)){
            overDueNoticeByDocId = docOverdueNoticeDao.getOverDueNoticeByDocId(docId,user);
        }
        out.setRecordsTotal(overDueNoticeByDocId.size());
        out.setDraw(overDueNoticeByDocId.size());
        out.setData(overDueNoticeByDocId);

        return out;
    }

    /**
     * 保存逾期催收登记数据
     * @param noticeVo
     * @param user
     */
    @Transactional
    public void saveNoticeList(DocOverdueNoticeVo noticeVo, ICurrentUser user){
        //页面不能修改历史登记数据
        DocOverdueNotice[] noticeArr = noticeVo.getNotices();
        String docId = noticeVo.getId();
        List<DocOverdueNoticeDtlVo> docOverdueNotices = docOverdueNoticeDao.queryOverDueNoticeByDocId(docId);
        List<String> oldIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(docOverdueNotices)){
            //过滤出当前用户创建的
            oldIds = docOverdueNotices.stream()
                    .filter(item -> item != null && Objects.equals(item.getCreateBy(), user.getId()))
                    .map(DocOverdueNoticeDtlVo::getId).collect(Collectors.toList());
        }
        List<DocOverdueNotice> noticeList = new ArrayList<>();
        List<DocOverdueNotice> updateList = new ArrayList<>();
        if(noticeArr != null && noticeArr.length > 0){
            for (DocOverdueNotice item : noticeArr) {
                //没有id的就是新增的
                if(StringUtils.isEmpty(item.getId())){
                    item.setDocId(docId);
                    item.setCreateBy(user.getId());
                    item.setCreateDate(DateUtils.getDate());
                    item.setTenantId(user.getTenantId());
                    noticeList.add(item);
                }else{
                    //有id的且createBy是当前用户的才更新
                    if(item.getCreateBy().equals(user.getId())){
                        updateList.add(item);
                    }
                }
            }
        }

        List<String> needSaveIds = updateList.stream().map(DocOverdueNotice::getId).collect(Collectors.toList());
        // 自己创建的记录未从前端传回，说明已在编辑页面删除；删除最后一条时 noticeArr 为空也需要处理
        List<String> needDelIds = oldIds.stream().filter(item -> !needSaveIds.contains(item)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(needDelIds)) {
            docOverdueNoticeDao.delete(needDelIds);
        }

        if (!CollectionUtils.isEmpty(noticeList)) {
            this.batchSave(noticeList);
        }
        if(updateList.size() > 0){
            StringBuilder sql = new StringBuilder();
            sql.append("update DOC_OVERDUE_NOTICE set OVERDUE_REASON= :overdueReason, COLLECTION_BY= :allocationBy, COLLECTION_DATE=:allocationDate");
            sql.append(", COLLECTIO_RESULT=:allocationResult");
            sql.append(" where id=:id");
            updateList.forEach(item ->{
                Map<String,Object> param = new HashMap<>();
                param.put("overdueReason",item.getOverdueReason());
                param.put("allocationBy",item.getAllocationBy());
                param.put("allocationDate",item.getAllocationDate());
                param.put("allocationResult",item.getAllocationResult());
                param.put("id",item.getId());
                docOverdueNoticeDao.batchSQLExecute(sql.toString(),param);
            });
        }
    }

    /**
     * 根据应收单id获取 逾期催收登记数据
     * @param docId
     * @param user
     * @return
     */
    public DataTablesOutput queryOverDueNoticeByDocId(String docId, ICurrentUser user){
        DataTablesOutput out = new DataTablesOutput();
        if (StringUtils.isNotEmpty(docId)){
            List<DocOverdueNoticeDtlVo> result = docOverdueNoticeDao.queryOverDueNoticeByDocId(docId);
            result.forEach(item -> {
                if(item.getCreateBy().equals(user.getId())){
                    item.setEditFlag("1");
                }else{
                    item.setEditFlag("0");
                }
            });
            out.setRecordsTotal(result.size());
            out.setDraw(result.size());
            out.setData(result);
        }
        return out;
    }

}
