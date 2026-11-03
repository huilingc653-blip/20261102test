package com.bytter.biz.wage.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.payment.api.constant.NisWageReleaseConfigConstant;
import com.bytter.biz.wage.dao.NisWageReleaseConfigDao;
import com.bytter.biz.wage.entity.NisWageReleaseConfig;
import com.bytter.biz.wage.vo.NisWageReleaseBatchUpdateVO;
import com.bytter.biz.wage.vo.NisWageReleaseConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代发工资下拨金额配置服务
 *
 * @author loveCamille
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NisWageReleaseConfigService extends BaseService<NisWageReleaseConfig, String> implements IExcelVerifyHandler<NisWageReleaseConfigVO> {

    private final NisWageReleaseConfigDao nisWageReleaseConfigDao;

    @DubboReference
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @Override
    public BaseDao<NisWageReleaseConfig, String> getEntityDao() {
        return nisWageReleaseConfigDao;
    }

    /**
     * 表格查询
     * @author loveCamille
     * @param input 查询条件
     * @param user 当前用户
     * @return {@link DataTablesOutput<NisWageReleaseConfigVO>} 查询结果
    */
    public DataTablesOutput<NisWageReleaseConfigVO> dataTableQuery(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuthList = Collections.emptyList();
        if (!EmptyUtils.isEmpty(input.getFormdata().get("ins_corpId"))) {
            corpAuthList = corpService.getUserCorpIds(NisWageReleaseConfigConstant.WAGE_RELEASE_CONFIG_QUERY, user.getId());
        }
        if (!EmptyUtils.isEmpty(input.getFormdata().get("bankAcc"))) {
            input.getFormdata().put("eqs_bankAcc", input.getFormdata().get("bankAcc"));
        }
        return nisWageReleaseConfigDao.dataTableQuery(input, corpAuthList);
    }

    public void batchConfirm(List<String> idList, Integer status, ICurrentUser user) {
        List<NisWageReleaseConfig> list = nisWageReleaseConfigDao.list(idList).stream().filter(k -> !k.getStatus().equals(status)).collect(Collectors.toList());
        if (CollUtil.isEmpty(list)) return;
        Date date = new Date();
        list.forEach(k -> {
            if (status == 1) {
                k.setConfirmId(user.getId());
                k.setConfirmName(user.getUsername());
                k.setConfirmTime(date);
            } else {
                k.setConfirmId(null);
                k.setConfirmName(null);
                k.setConfirmTime(null);
            }
            k.setStatus(status);
            k.setUpdateBy(user.getId());
            k.setUpdateTime(date);
        });
        nisWageReleaseConfigDao.batchUpdate(list);
    }

    public List<BtBankAccDTO> getBankAccQuery(List<String> corpIdList, ICurrentUser user) {
        if (CollUtil.isEmpty(corpIdList)) {
            corpIdList = corpService.getUserCorpIds(NisWageReleaseConfigConstant.WAGE_RELEASE_CONFIG_QUERY, user.getId());
        }
        return nisWageReleaseConfigDao.getBankAccQuery(corpIdList);
    }

    public void batchDelete(List<String> idList) {
        if (CollUtil.isEmpty(idList)) return;
        nisWageReleaseConfigDao.batchDelete(idList);
    }

    @Transactional
    public void saveImport(List<NisWageReleaseConfigVO> list, ICurrentUser user) {
        if (CollUtil.isEmpty(list)) return;
        List<NisWageReleaseConfig> entityList = new ArrayList<>(list.size());
        Date date = new Date();
        for (NisWageReleaseConfigVO nisWageReleaseConfigVO : list) {
            NisWageReleaseConfig entity = BeanUtil.copyProperties(nisWageReleaseConfigVO, NisWageReleaseConfig.class);
            entity.setLastMonthReleaseAmount(BigDecimal.ZERO);
            entity.setTotalReleaseMoney(BigDecimal.ZERO);
            entity.setConfirmId(user.getId());
            entity.setConfirmName(user.getUsername());
            entity.setConfirmTime(date);
            entity.setReleaseDate(DateUtil.parse(nisWageReleaseConfigVO.getReleaseDate(), DatePattern.SIMPLE_MONTH_PATTERN));
            entity.setCreateBy(user.getId());
            entity.setCreateTime(date);
            entity.setUpdateBy(user.getId());
            entity.setUpdateTime(date);
            entityList.add(entity);
        }
        batchSave(entityList);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisWageReleaseConfigVO obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<NisWageReleaseConfigVO> list) {
        if (CollUtil.isEmpty(list)) return;
        Map<String, CorpDTO> corpAllMap = corpService.getCorpListByNamesAll();
        Set<String> added = new HashSet<>();
        for (NisWageReleaseConfigVO vo : list) {
            String corpName = vo.getCorpName();
            CorpDTO corpDTO;
            if (StrUtil.isBlank(corpName) || (corpDTO = corpAllMap.get(corpName)) == null) {
                vo.addErrorMsg(Collections.singletonList("无效的单位名"));
                continue;
            }
            vo.setCorpId(corpDTO.getId());
            vo.setCorpCode(corpDTO.getCorpCode());
            String bankAcc = vo.getBankAcc();
            BtBankAccDTO bankAccDTO;
            if (StrUtil.isBlank(bankAcc) || (bankAccDTO = btBankAccService.getByBankAcc(bankAcc)) == null) {
                vo.addErrorMsg(Collections.singletonList("无效的银行账号"));
                continue;
            }
            if (!corpDTO.getId().equals(bankAccDTO.getCorpId())) {
                vo.addErrorMsg(Collections.singletonList("银行账号不属于此单位"));
                continue;
            }
            vo.setBankAccName(bankAccDTO.getAccName());
            vo.setBankCode(bankAccDTO.getBankCode());
            vo.setBankName(bankAccDTO.getBankName());


            String monthDate;

            try {
                monthDate = DateUtil.format(DateUtil.parse(vo.getReleaseDate(), DatePattern.SIMPLE_MONTH_PATTERN), DatePattern.NORM_MONTH_FORMAT) + "-01";
            } catch (DateException e) {
                vo.addErrorMsg(Collections.singletonList("月份格式错误，请修改为yyyyMM格式"));
                continue;
            }

            String key = corpDTO.getId() + "." + bankAcc + "." + monthDate;
            if (added.contains(key) || nisWageReleaseConfigDao.getOne(corpDTO.getId(), bankAcc, monthDate) != null) {
                vo.addErrorMsg(Collections.singletonList(String.format("【%s】下【%s】账户已存在【%s】月份数据", corpName, bankAcc, vo.getReleaseDate())));
                continue;
            }
            // 设置为已确认
            vo.setStatus(1);
            added.add(key);
        }
    }

    public void batchUpdateAmount(NisWageReleaseBatchUpdateVO updateVO) {
        nisWageReleaseConfigDao.batchUpdateAmount(updateVO);
    }

    @Transactional
    public void update(String id, BigDecimal releaseAmount, ICurrentUser user) {
        NisWageReleaseConfig nisWageReleaseConfig = nisWageReleaseConfigDao.get(id);
        nisWageReleaseConfig.setReleaseAmount(releaseAmount);
        nisWageReleaseConfig.setUpdateBy(user.getId());
        nisWageReleaseConfig.setUpdateTime(new Date());
        updateEntity(nisWageReleaseConfig);
    }
}
