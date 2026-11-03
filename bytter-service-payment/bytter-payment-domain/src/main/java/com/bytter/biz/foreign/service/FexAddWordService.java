package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexAddWordDao;
import com.bytter.biz.foreign.entity.FexAddWord;
import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.biz.foreign.vo.FexAddWordVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Title: Service
 * @Description: 交易附言表
 * @author huangx
 * @date 2022-10-31 15:20:33
 * @version 1.0
 *
 */
@Service("fexAddWordService")
@Transactional
public class FexAddWordService extends BaseService<FexAddWord, String> implements IExcelVerifyHandler<FexAddWordVO> {

    @Autowired
    private FexAddWordDao fexAddWordDao;

    @Autowired
    private FexTranInfoService fexTranInfoService;

    @Override
    public BaseDao<FexAddWord, String> getEntityDao() {
        return fexAddWordDao;
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput){
        return fexAddWordDao.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 根据ID获取交易附言信息
     *
     * @param id
     * @return
     */
    public FexAddWordVO getByIdUnion(String id) {
        return fexAddWordDao.getByIdUnion(id);
    }

    /**
     * 根据交易编码和收支方向获取交易附言
     *
     * @param tranCode
     * @param cdSign
     * @return
     */
    public FexAddWordVO getByTranCodeAndCdSign(String tranCode, String cdSign) {
        return fexAddWordDao.getByTranCodeAndCdSign(tranCode, cdSign);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(FexAddWordVO fexAddWordVO) {
        return null;
    }

    @Override
    public void verifyHandler(List<FexAddWordVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //交易编码集合
            HashMap<String, FexTranInfo> tranInfoMap = new HashMap<>();
            //交易编码+收支方向
            HashMap<String, String> tranCodeMap = new HashMap<>();
            for (FexAddWordVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                FexTranInfo tranInfo = tranInfoMap.get(vo.getTranCode());
                if (tranInfo == null) {
                    tranInfo = this.fexTranInfoService.getByTranCode(vo.getTranCode());
                    tranInfoMap.put(vo.getTranCode(), tranInfo);
                }
                if (EmptyUtils.isEmpty(tranInfo) || EmptyUtils.isBlank(tranInfo.getId())) {
                    msgList.add("foreign.addword.message.traninfo.null");
                } else {
                    vo.setTranName(tranInfo.getTranName());
                }

                if (ForeignConstant.CD_SIGN_0.equals(vo.getCdSignName())) {
                    vo.setCdSign(CommonConstant.NO);
                } else if (ForeignConstant.CD_SIGN_1.equals(vo.getCdSignName())) {
                    vo.setCdSign(CommonConstant.YES);
                } else {
                    msgList.add("foreign.addword.message.cdsign.mismatch");
                }

                if (StringUtils.isNotBlank(vo.getTranCode()) && StringUtils.isNotBlank(vo.getCdSign())) {
                    String key = vo.getTranCode() + CommonConstant.COMMA_SPLIT + vo.getCdSign();
                    if (tranCodeMap.containsKey(key)) {
                        msgList.add("foreign.addword.message.trancode.repeat");
                    } else {
                        FexAddWordVO addWordVO = this.getByTranCodeAndCdSign(vo.getTranCode(), vo.getCdSign());
                        if (addWordVO != null) {
                            msgList.add("foreign.addword.message.trancode.repeat");
                        }
                        tranCodeMap.put(key, key);
                    }
                }
                vo.setValidSign(CommonConstant.YES);
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<FexAddWordVO> list, ICurrentUser user) {
        List<FexAddWord> listAddWord = new ArrayList<>();
        for (FexAddWordVO vo : list) {
            FexAddWord fexAddWord = new FexAddWord();
            BeanUtils.copyNotNullProperties(vo, fexAddWord);
            fexAddWord.setCreateBy(user.getLoginName());
            fexAddWord.setCreateDate(DateUtils.getDate());
            listAddWord.add(fexAddWord);
        }
        this.batchSave(listAddWord);
    }
}
