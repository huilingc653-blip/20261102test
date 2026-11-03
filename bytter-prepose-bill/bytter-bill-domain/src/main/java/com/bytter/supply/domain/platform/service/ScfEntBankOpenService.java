package com.bytter.supply.domain.platform.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.supply.enums.SupplyMessageEnum;
import com.bytter.supply.domain.platform.dao.ScfBankOpenDao;
import com.bytter.supply.domain.platform.dao.ScfEntBankOpenDao;
import com.bytter.supply.domain.platform.dao.ScfPlatformDao;
import com.bytter.supply.domain.platform.entity.ScfEntBankOpen;
import com.bytter.supply.domain.platform.vo.ScfBankOpenVo;
import com.bytter.supply.domain.platform.vo.ScfEntBankOpenVo;
import com.bytter.supply.domain.platform.vo.ScfEntPlatformListVo;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: ScfEntBankOpenService
 * @Description: 平台接口开通配置业务层
 * @version 1.0
 */
@Service("scfEntBankOpenService")
@Transactional
public class ScfEntBankOpenService extends BaseService<ScfEntBankOpen, String> {

    /** 平台直联标记接口类型（仅用于标记平台已保存过直联配置，非真实业务接口） */
    private static final String DIRECT_LINK_MARK_TYPE = "DIRECT_LINK";

    /** 平台直联标记接口编码 */
    private static final String DIRECT_LINK_MARK_API_NAME = "DIRECT_LINK_MARK";

    /** 平台直联标记接口名称 */
    private static final String DIRECT_LINK_MARK_API_NAME_CN = "平台直联标记";

    @Autowired
    private ScfEntBankOpenDao scfEntBankOpenDao;

    @Autowired
    private ScfBankOpenDao scfBankOpenDao;

    @Autowired
    private ScfPlatformDao scfPlatformDao;

    @Override
    public BaseDao<ScfEntBankOpen, String> getEntityDao() {
        return scfEntBankOpenDao;
    }

    /**
     * 根据平台ID查询内管端配置（从 scf_bank_open 查）
     */
    @Transactional(readOnly = true)
    public List<ScfBankOpenVo> getBankOpenByPlatformId(String platformId) {
        return scfBankOpenDao.getListByPlatformId(platformId);
    }

    /**
     * 获取全部接口字典项（从 sys_dict 查询）
     */
    @Transactional(readOnly = true)
    public List<ScfBankOpenVo> getAllDictItems() {
        return scfEntBankOpenDao.getBankOpenConfigList();
    }

    /**
     * 平台列表（数据来自 SCF_PLATFORM）
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput listPlatform(DataTablesInput dataTablesInput) {
        DataTablesOutput output = scfEntBankOpenDao.listPlatform(dataTablesInput);
        List<ScfEntPlatformListVo> rows = output.getData();
        if (rows != null) {
            for (ScfEntPlatformListVo row : rows) {
                // 判断是否具备直连接口（除平台直联标记接口外还有其他接口）
                row.setHasDirect(hasDirectInterface(getConfigByEntPlatformId(row.getId())));
            }
        }
        return output;
    }

    /**
     * 根据ID查询平台
     */
    @Transactional(readOnly = true)
    public ScfPlatformVo getPlatformById(String id) {
        return scfPlatformDao.getPlatformById(id);
    }

    /**
     * 根据分组ID查询已保存的接口开通配置
     */
    @Transactional(readOnly = true)
    public List<ScfEntBankOpenVo> getConfigByEntPlatformId(String entPlatformId) {
        return scfEntBankOpenDao.getListByEntPlatformId(entPlatformId);
    }

    /**
     * 判断企业端已保存配置中是否具备直连接口（除平台直联标记接口外，还需该接口本身标记为具备直连 has_direct=1）
     *
     * @return "1" 具备直连接口；"0" 不具备
     */
    public String hasDirectInterface(List<ScfEntBankOpenVo> configList) {
        if (configList == null || configList.isEmpty()) {
            return "0";
        }
        for (ScfEntBankOpenVo config : configList) {
            if (!DIRECT_LINK_MARK_API_NAME.equals(config.getApiName()) && "1".equals(config.getHasDirect())) {
                return "1";
            }
        }
        return "0";
    }

    /**
     * 保存接口开通配置（新增，校验平台是否已存在直联配置，避免重复提交）
     */
    public void saveConfig(String entPlatformId, List<ScfEntBankOpen> configList, ICurrentUser currentUser) {
        if (existsConfig(entPlatformId)) {
            throw new ParameterException(SupplyMessageEnum.SCF_ENT_PLATFORM_CONFIG_EXIST);
        }
        doSaveConfig(entPlatformId, configList, currentUser);
    }

    /**
     * 修改接口开通配置（允许覆盖已存在配置）
     */
    public void updateConfig(String entPlatformId, List<ScfEntBankOpen> configList, ICurrentUser currentUser) {
        doSaveConfig(entPlatformId, configList, currentUser);
    }

    /**
     * 判断平台是否已存在直联配置
     */
    @Transactional(readOnly = true)
    public boolean existsConfig(String entPlatformId) {
        return scfEntBankOpenDao.existsByEntPlatformId(entPlatformId);
    }

    /**
     * 保存接口开通配置（先删后插）
     */
    private void doSaveConfig(String entPlatformId, List<ScfEntBankOpen> configList, ICurrentUser currentUser) {
        // 先删除旧的配置
        scfEntBankOpenDao.deleteByEntPlatformId(entPlatformId);
        // 无论前端是否勾选接口，都固定保存一条标记记录，仅用于标记该平台已做过直联配置
        super.saveEntity(buildDirectLinkMark(entPlatformId, currentUser));
        // 再插入前端勾选的接口配置
        if (configList != null) {
            for (ScfEntBankOpen config : configList) {
                // 跳过标记记录，避免重复插入
                if (DIRECT_LINK_MARK_API_NAME.equals(config.getApiName())) {
                    continue;
                }
                config.setId(null);
                config.setEntPlatformId(entPlatformId);
                config.setCreateBy(currentUser.getLoginName());
                config.setCreateDate(DateUtils.getDate());
                // 前端传布尔 true/false，DB 列 HAS_DIRECT 按 0/1 存储，入库前归一化
                config.setHasDirect(normalizeHasDirect(config.getHasDirect()));
                super.saveEntity(config);
            }
        }
    }

    /**
     * 归一化直连接口标记：true/"1" → "1"，其余(false/null/"0") → "0"（null 原样返回）
     */
    private String normalizeHasDirect(String hasDirect) {
        if (hasDirect == null) {
            return null;
        }
        return ("true".equalsIgnoreCase(hasDirect) || "1".equals(hasDirect)) ? "1" : "0";
    }

    /**
     * 构建平台直联标记记录（不参与接口开通判断，仅用于标记平台已保存过直联配置）
     */
    private ScfEntBankOpen buildDirectLinkMark(String entPlatformId, ICurrentUser currentUser) {
        ScfEntBankOpen mark = new ScfEntBankOpen();
        mark.setEntPlatformId(entPlatformId);
        mark.setType(DIRECT_LINK_MARK_TYPE);
        mark.setApiNameCn(DIRECT_LINK_MARK_API_NAME_CN);
        mark.setApiName(DIRECT_LINK_MARK_API_NAME);
        mark.setCreateBy(currentUser.getLoginName());
        mark.setCreateDate(DateUtils.getDate());
        return mark;
    }

    /**
     * 根据分组ID删除接口配置
     */
    public void deleteConfigByEntPlatformId(String entPlatformId) {
        scfEntBankOpenDao.deleteByEntPlatformId(entPlatformId);
    }

    /**
     * 判断平台下 接口是否开通
     * @param platformId
     * @param apiCode
     * @return
     */
    public boolean judgeInstructionOpen(String platformId,String apiCode) {
        return scfEntBankOpenDao.judgeInstructionOpen(platformId, apiCode);
    }


    /**
     * 判断该租户是否存在的开通接口（所有平台）
     * @param apiCode
     * @return
     */
    public boolean judgeInstructionOpenAllPlatform(String apiCode) {
        return scfEntBankOpenDao.judgeInstructionOpenAllPlatform(apiCode);
    }
}
