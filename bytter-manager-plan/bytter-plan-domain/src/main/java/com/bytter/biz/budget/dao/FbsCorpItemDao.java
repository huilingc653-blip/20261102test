package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsCorpItem;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.spi.vo.SimpleFbsItemVo;
import com.bytter.biz.budget.vo.FbsCorpItemVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.commons.collections4.ListUtils;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.core.tenant.SmartThreadPoolManager;
import java.util.concurrent.*;

/**
* @Title: Dao
* @Description: FBS_CORP_ITEM
* @author onlineGenerator
* @date 2017-07-11 10:46:42
* @version 1.0
*
*/
@Repository
public class FbsCorpItemDao extends BaseDao<FbsCorpItem,String> implements DisposableBean {


    //资金计划查询多线程
    private static ExecutorService FBS_CORP_DTO_EXECUTIR = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(8));
    @Override
    public void destroy() throws Exception {
        FBS_CORP_DTO_EXECUTIR.shutdown();
    }

    /**
	 * 查找单位科目
	 * @param fbsCorpItem
	 * @return
	 */
	public List<FbsCorpItemVO> listFbsItem(FbsCorpItem fbsCorpItem) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fi.id as id, fi.item_code as itemCode,fb.item_name as itemName from fbs_corp_item fi");
        sql.append(" left join Fbs_Item fb on fi.item_code = fb.item_code where fi.valid_sign = '1'");
        if(fbsCorpItem != null && StringUtils.isNotEmpty(fbsCorpItem.getCorpId())){
        	sql.append(" and fi.corp_id ='");
        	sql.append(fbsCorpItem.getCorpId());
        	sql.append("'");
        }
        return listBySQLAliasToBean(sql.toString(), null, FbsCorpItemVO.class);
	}
	
	/**
	 * 根据id查找单位科目
	 * @param corpId
	 * @return
	 */
	public List<FbsCorpItemVO> getTree(String corpId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select fi.id as id, fi.item_code as itemCode,fb.item_name as itemName from fbs_corp_item fi");
		sql.append(" left join Fbs_Item fb on fi.item_code = fb.item_code where fi.valid_sign = '1'");
		if(StringUtils.isNotEmpty(corpId)){
			sql.append(" and fi.corp_id ='");
			sql.append(corpId);
			sql.append("'");
		}
		return listBySQLAliasToBean(sql.toString(), null, FbsCorpItemVO.class);
	}
	/**
	 * 根据单位id 找 所属单位科目列表
	 * @param corpId
	 * @return
	 */
	public List<FbsItemVO> getAllItemByCorpId(String corpId) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.allot_item_code as allotItemCode,");
		sql.append(" c.is_Rigid as isRigid,c.is_Begin_Period as isBeginPeriod,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,c.item_Relation as itemRelation,c.is_Write_Detail as isWriteDetail");
		sql.append(" from fbs_corp_item fi");
		sql.append(" left join Fbs_Item c on fi.item_code = c.item_code where fi.corp_id ='");
		sql.append(corpId);
		sql.append("'");
		sql.append(" and c.valid_sign ='1' order by fi.item_Code");
		return listBySQLAliasToBean(sql.toString(), null, FbsItemVO.class);
	}

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public List<SimpleFbsItemVo> getAllItemByCorpId(List<String> corpIds) {
        StringBuilder sql = new StringBuilder("select fi.corp_id as corpId,fi.id as id, fi.item_Code as itemCode ");
        sql.append(" from fbs_corp_item fi where fi.corp_id in(:corpIds)");
        Map<String, Object> params = new HashMap<>();
        params.put("corpIds", corpIds);
        return createSQLQuery(sql.toString(), params)
                .addScalar("id", StandardBasicTypes.STRING)
                .addScalar("itemCode", StandardBasicTypes.STRING)
                .addScalar("corpId", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(SimpleFbsItemVo.class)).list();
    }
	/**
	 * 根据单位id 找 所属单位科目列表
	 * @param corpId
	 * @return
	 */
	public List<FbsItem> getAllItemByCorpIdNotVO(String corpId) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.allot_item_code as allotItemCode,");
		sql.append(" c.is_Rigid as isRigid,c.is_Begin_Period as isBeginPeriod,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,c.item_Relation as itemRelation,c.is_Write_Detail as isWriteDetail");
		sql.append(" from fbs_corp_item fi");
		sql.append(" left join Fbs_Item c on fi.item_code = c.item_code where fi.corp_id ='");
		sql.append(corpId);
		sql.append("'");
		sql.append(" and c.valid_sign ='1' order by fi.item_Code");
		return listBySQLAliasToBean(sql.toString(), null, FbsItem.class);
	}
	
	 /**
     * 根据code搜索查询
     * @param codes
     * @return
     */
    public List<FbsCorpItemVO> listFbsItemByCodes(List<String> codes) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fi.id as id, fi.item_code as itemCode,fb.item_name as itemName from fbs_corp_item fi");
        sql.append(" left join Fbs_Item fb on fi.item_code = fb.item_code where fi.valid_sign = '1' and ");
        String where = getSQLIn(codes, 900, "fi.item_code");
        return listBySQLAliasToBean(sql.append(where).toString(), null, FbsCorpItemVO.class);
    }
    
    /**
	 * 根据单位ID删除分配的单位科目
	 * @param corpId
	 */
	public void deleteByCorpId(String corpId) {
		createSQLQuery("delete from FBS_CORP_ITEM where CORP_ID=?", corpId).executeUpdate();
	}

    /**
     * 根据单位ID删除分配的单位科目
     *
     * @param corpIds
     */
    public void deleteByCorpId(List<String> corpIds) {
        List<String> ids=new ArrayList<>();
        for (String id:corpIds) {
            ids.add(id);
            if (ids.size()>50){
                // 以500个为单位进行分割list 直接全部删除
                Map<String, Object> params = new HashMap<>();
                params.put("corpIds", ids);
                createSQLQuery("delete from FBS_CORP_ITEM where CORP_ID  in (:corpIds)", params).executeUpdate();
                ids.clear();
            }

        }
        if (EmptyUtils.isNotEmpty(ids)) {
            Map<String, Object> params = new HashMap<>();
            params.put("corpIds", ids);
            createSQLQuery("delete from FBS_CORP_ITEM where CORP_ID  in (:corpIds)", params).executeUpdate();
        }

    }
	/**
	 * 根据单位id得到单位科目信息
	 * @param corpId
	 * @return
	 */
    public List<FbsCorpItem> getItemCode(String corpId){
		Map<String,Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select fi.id as id,fi.item_code as itemCode,fi.corp_id as corpId,fi.valid_sign as validSign,");
		sql.append("fi.create_by as createBy,fi.create_date as createDate,fi.update_by as updateBy,fi.update_date as updateDate from fbs_corp_item fi");
		sql.append(" left join Fbs_Item fb on fi.item_code = fb.item_code where fb.valid_sign = '1'");
		if(StringUtils.isNotEmpty(corpId)){
			sql.append(" and fi.corp_id =:corpId");
		}
		params.put("corpId",corpId);
    	return listBySQLAliasToBean(sql.toString(), params,FbsCorpItem.class);
    }

    /**
     * 根据单位id得到单位科目信息
     *
     * @param corpIds
     * @return
     */
    public List<FbsCorpItem> getItemCode(List<String> corpIds) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select fi.id as id,fi.item_code as itemCode,fi.corp_id as corpId,fi.valid_sign as validSign,");
        sql.append("fi.create_by as createBy,fi.create_date as createDate,fi.update_by as updateBy,fi.update_date as updateDate from fbs_corp_item fi");
        sql.append(" left join Fbs_Item fb on fi.item_code = fb.item_code where fb.valid_sign = '1'");
        if (!CollectionUtils.isEmpty(corpIds)){
            sql.append(" and" +getSQLIn(corpIds, 900, "fi.corp_id"));
        }
        params.put("corpIds", corpIds);
        return listBySQLAliasToBean(sql.toString(), params, FbsCorpItem.class);
    }
	/**
	* 根据item_code,corp_id得到数据，主要是id
	* @param corpId
	* @return
	*/
  	public List<FbsCorpItemVO> getAlreadyCorpItem(String corpId){
  		HashMap params = new HashMap();
  		StringBuilder sql = new StringBuilder();
  		sql.append("select id as id,item_code as itemCode,corp_id as corpId,valid_sign as validSign from fbs_corp_item where corp_id =:corpId");
  		params.put("corpId", corpId);
		return listBySQLAliasToBean(sql.toString(),params,FbsCorpItemVO.class);
  	}
    /**
     * 根据item_code,corp_id得到数据，主要是id
     *
     * @param corpIds
     * @return
     */
    public List<FbsCorpItemVO> getAlreadyCorpItem(List<String> corpIds) {
        HashMap params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("select item_code as itemCode from fbs_corp_item where corp_id in (:corpIds)  GROUP BY item_code ");
        params.put("corpIds", corpIds);
        return listBySQLAliasToBean(sql.toString(), params, FbsCorpItemVO.class);
    }
  	
  	/**
  	 * 查找已分配科目的单位
  	 * @return
  	 */
  	public List<FbsCorpItem> isItem(){
  		return createSQLQuery("select distinct corp_id from fbs_corp_item where item_code is not null").list();
  	}
	/**
	 * 根据code判断是否存在使用记录
	 * @param itemCodes
	 * @return
	 */
	public Long getCountByCodes(List<String> itemCodes) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from fbs_corp_item fi where 1=1 and ");
		String where = getSQLIn(itemCodes, 900, "fi.item_code");
		BigDecimal count = (BigDecimal) createSQLQuery(sql.append(where).toString()).uniqueResult();
		return count.longValue();
	}
	/**
	 * 根据单位统计设置的计划科目
	 * @param corpId
	 * @return
	 */
	public int getCorpItemCountByCorpId(String corpId){
		String sql="select count(*) from fbs_corp_item where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}

    /**
     * 根据单位id 找 所属单位科目列表
     *
     * @param corpIds
     * @return
     */
    public List<SimpleFbsItemVo> getAllItemByCorpIdExecutir(List<String> corpIds) {
        List<SimpleFbsItemVo> fbsItemVOS = new ArrayList<>();
        List<Future<List<SimpleFbsItemVo>>> futures = new ArrayList<>();
        // 一次999减少io
        List<List<String>> splitedList = ListUtils.partition(corpIds, 999);
        final CountDownLatch latch = new CountDownLatch(splitedList.size());
        for (List<String> list : splitedList) {
            final List<String> stringList=list;
            Future<List<SimpleFbsItemVo>> submit = FBS_CORP_DTO_EXECUTIR.submit(new Callable<List<SimpleFbsItemVo>>() {
                @Override
                public List<SimpleFbsItemVo> call() {
                    try {
                        List<SimpleFbsItemVo> vos = getAllItemByCorpId(stringList);
                        return vos;
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        //线程监听器统计已经完成处理
                        latch.countDown(); //每次调用CountDown()，计数减1
                    }
                }
            });
            futures.add(submit);
        }

        try {
            //等待所有线程执行完毕
            latch.await(); //主程序执行到await()函数会阻塞等待线程的执行，直到计数为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        for (List<String> stringList : splitedList) {
//            long time = System.currentTimeMillis();
//            Map<String, Object> params = new HashMap<>();
//            params.put("corpIds", stringList);
//            List<FbsItemVO> vos = listBySQLAliasToBean(sql.toString(), params, FbsItemVO.class);
//            fbsItemVOS.addAll(vos);
//            logger.info("执行一次500的查询耗时：{}", System.currentTimeMillis() - time);
//        }

        for (Future<List<SimpleFbsItemVo>> future : futures) {
            try {
                List<SimpleFbsItemVo> vos=future.get();
                fbsItemVOS.addAll(vos);
            } catch (InterruptedException e) {
                logger.error("获取数据被中断 ", e);
            } catch (ExecutionException e) {
                logger.error("执行异常 ", e);
            }
        }
        return fbsItemVOS;
    }

}
