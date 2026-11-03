package com.bytter.guarantee.bond.service;


import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.guarantee.bond.dao.SearchFcBondDao;
import com.bytter.guarantee.bond.entity.FcBondApply;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.vo.*;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fusb
 * @version 1.0
 * @Title SearchFcBondService
 * @Description 查询保证金service
 * @date 2018年9月17日
 */
@Service("searchFcBondService")
@Transactional
public class SearchFcBondService extends BaseService<FcBondApply, String> {
    @Autowired
    SearchFcBondDao searchFcBondDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IUserService iUserService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IOperationAreaService operationAreaService;

    /**
     * 保证金项目查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<FcBondApplyVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.NETBANK_BOND_FCBONDINFO_APPLY;
        List<String> corpList=corpService.getUserCorpIds(resource, user.getId());
        String corpAuthSql = SqlUtil.getInSql(corpList, "C.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);

        DataTablesOutput<FcBondApplyVO> output = searchFcBondDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<FcBondApplyVO> voList = output.getData();
        if(voList.size()>0) {
            List<String> bankAccIds = new ArrayList<>();
            for (FcBondApplyVO vo : voList) {
                bankAccIds.add(vo.getBankAccId());
            }
            List<BtBankAccDTO> accVOList = iBtBankAccService.getBankAccByBankAccIds(bankAccIds);
            Map<String, BtBankAccDTO> accDTOMap = new HashMap<>();
            for (BtBankAccDTO dto : accVOList) {
                accDTOMap.put(dto.getId(), dto);
            }
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpList);
            Map<String, String> btCurrencyMap = iBtCurrencyService.getCurrencyNameMap();
            for (FcBondApplyVO vo : voList) {
                BtBankAccDTO accDTO = accDTOMap.get(vo.getBankAccId());
                if (null != accDTO) {
                    vo.setBankAcc(accDTO.getBankAcc());
                    vo.setAccName(accDTO.getAccName());
                    vo.setBankName(accDTO.getBankName());
                    vo.setCorpId(accDTO.getCorpId());
                    vo.setCorpName(corpMap.get(accDTO.getCorpId()).getName());
                }
                if (!EmptyUtils.isEmpty(btCurrencyMap)) {
                    if (btCurrencyMap.containsKey(vo.getCurId())) {
                        vo.setCurName(btCurrencyMap.get(vo.getCurId()));
                    }
                }
            }
        }
        return output;
    }

    /**
     * 保证金账户查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<FcBondBackSearchVO> getDataTablesUnion1(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.NETBANK_BOND_FCBONDBACK_APPLY;//应用资源
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "b.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
        } else {
            sql.append(" and 1 <> 1");
        }
        //账号条件
        String likes_bankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
        String likes_bankAcc = (String) dataTablesInput.getFormdata().get("likes_bankAcc");
        String likes_accName = (String) dataTablesInput.getFormdata().get("likes_accName");
        if (!StringUtil.isBlank_new(likes_bankName) ||!StringUtil.isBlank_new(likes_bankAcc)||!StringUtil.isBlank_new(likes_accName)) {
            StringBuilder accSql = new StringBuilder();
            Map<String, Object> params = new HashMap();
            accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE 1=1");
            if (!StringUtil.isBlank_new(likes_bankName))
                accSql.append(" and a.bank_name like '%" + likes_bankName + "%'");
            if (!StringUtil.isBlank_new(likes_bankAcc))
                accSql.append(" and a.bank_acc like '%" + likes_bankAcc + "%'");
            if (!StringUtil.isBlank_new(likes_accName))
                accSql.append(" and a.acc_name like '%" + likes_accName + "%'");
            List<BtBankAccDTO> accDTOList = iBtBankAccService.listBankAccBySql(accSql.toString(), params);
            if (accDTOList.size() > 0) {
                List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
                if (accList != null && accList.size() > 0) {
                    sql.append(" and ").append(SqlUtil.getInSql(accList, " B.BANK_ACC_ID", true));
                }

                dataTablesInput.getFormdata().remove("likes_bankName");
                dataTablesInput.getFormdata().remove("likes_bankAcc");
                dataTablesInput.getFormdata().remove("likes_accName");
            } else {
                return new DataTablesOutput<>();
            }
        }
        int pageSize = dataTablesInput.getPageSize();
        DataTablesOutput<FcBondBackSearchVO> output = searchFcBondDao.getDataTablesUnion1(dataTablesInput, sql.toString());
        List<FcBondBackSearchVO> voList = output.getData();
        if(voList.size()>0) {
            List<String> bankAccIds = new ArrayList<>();
            for (FcBondBackSearchVO vo : voList) {
                bankAccIds.add(vo.getBankAccId());
            }
            List<BtBankAccDTO> accVOList = iBtBankAccService.getBankAccByBankAccIds(bankAccIds);
            Map<String, BtBankAccDTO> accDTOMap = new HashMap<>();
            for (BtBankAccDTO dto : accVOList) {
                accDTOMap.put(dto.getId(), dto);
            }
            Map<String, String> btCurrencyMap = iBtCurrencyService.getCurrencyNameMap();
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpList);
            for (FcBondBackSearchVO vo : voList) {
                BtBankAccDTO accDTO = accDTOMap.get(vo.getBankAccId());
                if (null != accDTO) {
                    vo.setBankAcc(accDTO.getBankAcc());
                    vo.setAccName(accDTO.getAccName());
                    vo.setBankName(accDTO.getBankName());
                    vo.setCorpId(accDTO.getCorpId());
                    vo.setCorpName(corpMap.get(accDTO.getCorpId()).getName());
                    vo.setCurName(btCurrencyMap.get(vo.getCurId()));
                }
            }
        }
    	//倒序
        //return o2.getCorpName().compareTo(o1.getCorpName());
        //正序
        //return o1.getCorpName().compareTo(o2.getCorpName());
        //列表排序
        int orderSize =dataTablesInput.getOrder().size();
        for(int i=0;i<orderSize;i++) {
            Order order = dataTablesInput.getOrder().get(i);
            if (1 == order.getColumn() && "asc".equals(order.getDir())) {
                	Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getCorpName()) && o1!=null && !StringUtils.isEmpty(o1.getCorpName())) {
                            return o1.getCorpName().compareTo(o2.getCorpName());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            } else if (1 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                    Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getCorpName()) && o1!=null && !StringUtils.isEmpty(o1.getCorpName())) {
                            return o2.getCorpName().compareTo(o1.getCorpName());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            }else if (2 == order.getColumn() && "asc".equals(order.getDir())) {
            		Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getBankAcc()) && o1!=null && !StringUtils.isEmpty(o1.getBankAcc())) {
                            return o1.getBankAcc().compareTo(o2.getBankAcc());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            } else if (2 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                	Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getBankAcc()) && o1!=null && !StringUtils.isEmpty(o1.getBankAcc())) {
                            return o2.getBankAcc().compareTo(o1.getBankAcc());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            }else if (3 == order.getColumn() && "asc".equals(order.getDir())) {
                	Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getAccName()) && o1!=null && !StringUtils.isEmpty(o1.getAccName())) {
                            return o1.getAccName().compareTo(o2.getAccName());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            } else if (3 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                	Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getAccName()) && o1!=null && !StringUtils.isEmpty(o1.getAccName())) {
                            return o2.getAccName().compareTo(o1.getAccName());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            }else if (4 == order.getColumn() && "asc".equals(order.getDir())) {
                	Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getBankName()) && o1!=null && !StringUtils.isEmpty(o1.getBankName())) {
                            return o1.getBankName().compareTo(o2.getBankName());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            } else if (4 == order.getColumn() && "desc".equals(order.getDir())) {//倒序
                Collections.sort(voList, new Comparator<FcBondBackSearchVO>() {
                    @Override
                    public int compare(FcBondBackSearchVO o1, FcBondBackSearchVO o2) {
                        if (o2!=null && !StringUtils.isEmpty(o2.getBankName()) && o1!=null && !StringUtils.isEmpty(o1.getBankName())) {
                            return o2.getBankName().compareTo(o1.getBankName());
                        } else {
                            return o2 == null ? 1 : -1;
                        }
                    }
                });
            }
        }
        return output;
    }

    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public FcBondBackVO getByIdFcBondBackVO(String id) {
        FcBondBackVO vo = searchFcBondDao.getByIdFcBondBackVO(id);
        BtBankAccDTO accDTO = iBtBankAccService.getBankAccById(vo.getBankAccId());
        if (null != accDTO) {
            vo.setBankAcc(accDTO.getBankAcc());
            vo.setAccName(accDTO.getAccName());
            vo.setBankName(accDTO.getBankName());
            vo.setCorpId(accDTO.getCorpId());
            vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
            vo.setCurId(accDTO.getCurId());
            vo.setCurName(iBtCurrencyService.getObjectById(accDTO.getCurId()).getCurName());
        }
        return vo;
    }

    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<FcBondApplyVO> getByIdFcBondApply(String id) {
        return searchFcBondDao.getByIdFcBondApplys(id);
    }
    @Transactional(readOnly = true)
    public List<FcBondApplyVO> getByIdFcBondApply(String id,String param) {
        List<FcBondApplyVO> list = searchFcBondDao.getByIdFcBondApplys(id,param);
        for(FcBondApplyVO vo : list){
            //设置营运区
            if(StringUtils.isNotEmpty(vo.getOperationAreaCode())){
                StringBuilder operationAreaNames = new StringBuilder();
                List<String> operationAreaCodes = Arrays.asList(vo.getOperationAreaCode().split(","));
                for(String code:operationAreaCodes){
                    List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByCode(code);
                    if(CollectionUtil.isNotEmpty(operationAreaDTOS)){
                        operationAreaDTOS.stream().forEach(operationAreaDTO -> {
                            operationAreaNames.append(operationAreaDTO.getOperationAreaName()).append(",");
                        });
                    }
                }
                vo.setOperationAreaName(StringUtils.isNotEmpty(operationAreaNames.toString())?operationAreaNames.toString().substring(0,operationAreaNames.toString().length()-1):"");
            }
        }
        return list;
    }

    /**
     * 根据保证金id，得到保证金转回历史记录
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<FcBondBackVO> getByIdFcBondBack(String id) {
        List<FcBondBackVO> voList = searchFcBondDao.getByIdFcBondBacks(id);
        List<String> loginNames = new ArrayList<>();
        for (FcBondBackVO backVO : voList) {
            loginNames.add(backVO.getCreateBy());
        }
        Map<String, String> userMap = iUserService.getUserNameByLoginNames(loginNames);
        for (FcBondBackVO backVO : voList) {
            backVO.setCreateByName(userMap.get(backVO.getCreateBy()));
        }
        return voList;
    }

    /**
     * 根据占用表FC_BOND_APPLY的ID，得到FcBondApplyVO
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public FcBondApplyVO getFcBondApplyVO(String id) {
        FcBondApplyVO vo = searchFcBondDao.getFcBondApplyVOById(id);
        BtBankAccDTO accDTO = iBtBankAccService.getBankAccById(vo.getBankAccId());
        if (null != accDTO) {
            vo.setBankAcc(accDTO.getBankAcc());
            vo.setAccName(accDTO.getAccName());
            vo.setBankName(accDTO.getBankName());
            vo.setCorpId(accDTO.getCorpId());
            vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
            vo.setCurId(accDTO.getCurId());
            vo.setCurName(iBtCurrencyService.getObjectById(accDTO.getCurId()).getCurName());
        }
        return vo;
    }

    /**
     * 根据项目id和项目单号查询项目占用的保证金信息
     *
     * @param itemId
     * @param itemNo
     * @return
     */
    @Transactional(readOnly = true)
    public FcBondApplyInfoVO getFcBondApplyInfo(String itemId, String itemNo) {
        return searchFcBondDao.getFcBondApplyInfoVO(itemId, itemNo);
    }

    /**
     * 根据占用表FC_BOND_ADD中的bond_id，得到保证金追历史明细
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<FcBondAddDTO> getFcBondAddsByBondId(String bondId) {
        List<FcBondAddDTO> voList = searchFcBondDao.getFcBondAddsByBondId(bondId);
        List<String> loginNames = new ArrayList<>();
        for (FcBondAddDTO vo : voList) {
            loginNames.add(vo.getCreateBy());
        }
        Map<String, String> userMap = iUserService.getUserNameByLoginNames(loginNames);
        for (FcBondAddDTO vo : voList) {
            vo.setCreateByName(userMap.get(vo.getCreateBy()));
        }
        return voList;
    }

    /**
     * 查询保证金占用相关银行
     *
     * @return
     */
    public List<FcBondVO> queryBondBank() {
        List<FcBondVO> voList = searchFcBondDao.queryBondBank();
        List<String> bankAccIds = new ArrayList<>();
        for (FcBondVO vo : voList) {
            bankAccIds.add(vo.getBankAccId());
            vo.setId(vo.getBankAccId());//给使用的地方转换用
        }
        List<BtBankAccDTO> accVOList = iBtBankAccService.getBankAccByBankAccIds(bankAccIds);
        Map<String, BtBankAccDTO> accDTOMap = new HashMap<>();
        for (BtBankAccDTO dto : accVOList) {
            accDTOMap.put(dto.getId(), dto);
        }
        for (FcBondVO vo : voList) {
            BtBankAccDTO acc = accDTOMap.get(vo.getBankAccId());
            if (null != acc) {
                vo.setBankCode(acc.getBankCode());
                vo.setBankName(acc.getBankName());
            }
        }
        return voList;
    }

    /**
     * 查询保证金占用相关单位
     *
     * @return
     */
    public List<FcBondApplyDTO> queryBondCorp(String resource, ICurrentUser user) {
        //当前用户查询单位权限
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        String sql = "";
        if (corpAuth != null && corpAuth.size() > 0) {
            sql = SqlUtil.getInSql(corpAuth, "bond.corp_id", true);
        } else {
            sql = " 1 <> 1 ";
        }
        List<FcBondApplyDTO> list = searchFcBondDao.queryBondCorp(sql);
        Map<String, String> corpMap = this.corpService.getNameByIds(corpAuth);
        for (FcBondApplyDTO vo : list) {
            vo.setId(vo.getCorpId());//给使用的地方转换用
            vo.setCorpName(corpMap.get(vo.getId()));
        }
        return list;
    }

    public List<FcBondUseVO> getBondUseList(FcBondApplyVO applyVO, String resource, ICurrentUser user) {
        //当前用户查询单位权限
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        String sql = "";
        if (corpAuth != null && corpAuth.size() > 0) {
            sql = SqlUtil.getInSql(corpAuth, "dtl.corpID", true);
        } else {
            sql = " 1 <> 1 ";
        }
        //账号条件
        if (StringUtils.isNotEmpty(applyVO.getBankId())) {//界面选择了银行信息
            StringBuilder accSql=new StringBuilder();
            Map<String, Object> params=new HashMap();
            accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE a.bank_code =:bankCode ");
            params.put("bankCode",applyVO.getBankId());
            List<BtBankAccDTO> accDTOList= iBtBankAccService.listBankAccBySql(accSql.toString(),params);
            List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
            if (accList != null && accList.size() > 0) {
                sql += " and "+SqlUtil.getInSql(accList, "dtl.bankAccId", true);
            }
        }
        List<FcBondUseDtlVO> dtlVOS = searchFcBondDao.getFcBondApplySum(applyVO, sql);
        List<FcBondUseVO> useVOS = new ArrayList<>();
        if (dtlVOS.size() > 0) {
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpAuth);
            List<String> bankAccIds = new ArrayList<>();
            for (FcBondUseDtlVO vo : dtlVOS) {
                bankAccIds.add(vo.getBankAccId());
            }
            List<BtBankAccDTO> accVOList = iBtBankAccService.getBankAccByBankAccIds(bankAccIds);
            Map<String, BtBankAccDTO> accDTOMap = new HashMap<>();
            for (BtBankAccDTO dto : accVOList) {
                accDTOMap.put(dto.getId(), dto);
            }
            List<FcBondUseDtlVO> dtlVOS1 = new ArrayList<FcBondUseDtlVO>();
            for (FcBondUseDtlVO vo : dtlVOS) {
                BtBankAccDTO acc = accDTOMap.get(vo.getBankAccId());
                if (null != acc) {
                    CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                    vo.setCorpCode(corpDTO.getCode());
                    vo.setCorpName(corpDTO.getName());
                    vo.setBankCode(acc.getBankCode());
                    vo.setBankName(acc.getBankName());
                    vo.setCurName(iBtCurrencyService.getObjectById(vo.getCurId()).getCurName());

                    dtlVOS1.add(vo);
                }
            }
            dtlVOS = dtlVOS1;

            //同银行同单位同币别分组
            Map<String, FcBondUseVO> dtlMap = new HashMap<String, FcBondUseVO>();
            for (FcBondUseDtlVO fcBondUseDtlVO : dtlVOS) {
                List<FcBondUseDtlVO> useDtls = new ArrayList<>();
                BigDecimal sumApplyMoney = BigDecimal.ZERO;
                BigDecimal bondSumMoney = BigDecimal.ZERO;
                String curCode = fcBondUseDtlVO.getCurId();
                String bankCode = fcBondUseDtlVO.getBankCode();
                String corpId = fcBondUseDtlVO.getCorpId();
                String key = corpId + "_" + bankCode + "_" + curCode;
                FcBondUseVO bondVO = dtlMap.get(key);
                if (bondVO == null) {
                    FcBondUseVO bondUseVO = new FcBondUseVO();
                    BeanUtils.copyProperties(fcBondUseDtlVO, bondUseVO);
                    dtlMap.put(key, bondUseVO);
                    for (FcBondUseDtlVO useDtlVO : dtlVOS) {
                        if (useDtlVO.getCorpId().equals(corpId) && useDtlVO.getBankCode().equals(bankCode) && useDtlVO.getCurId().equals(curCode)) {
                            useDtls.add(useDtlVO);
                            sumApplyMoney = sumApplyMoney.add(useDtlVO.getApplyMoney());
                            bondSumMoney = bondSumMoney.add(useDtlVO.getBondSumMoney());
                        }
                    }
                    bondUseVO.setApplyDtls(useDtls);
                    bondUseVO.setDtlSize(useDtls.size());
                    bondUseVO.setApplySumMoney(sumApplyMoney);
                    bondUseVO.setBondSumMoney(bondSumMoney);
                    bondUseVO.setCanUseSumMoney(bondUseVO.getBondSumMoney().subtract(sumApplyMoney));
                    useVOS.add(bondUseVO);
                }
            }
        }
        return useVOS;
    }

    public List<FcBondUseDtlVO> getBondTransDtl(String id) {
        List<FcBondUseDtlVO> voList = searchFcBondDao.getBondTransDtl(id);
        return voList;
    }

    /**
     * 根据单位，银行，币别查询保证金占用明细
     *
     * @return
     */
    public List<FcBondUseDtlVO> queryBondUseDtl(FcBondApplyVO applyVO) {
        //通过银行信息查询出账号
       String sql="";
        if (StringUtils.isNotEmpty(applyVO.getBankName())) {//界面选择了银行信息
            StringBuilder accSql=new StringBuilder();
            Map<String, Object> params=new HashMap();
            accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE a.bank_name like :bankName ");
            params.put("bankName","%" + applyVO.getBankName() + "%");
            List<BtBankAccDTO> accDTOList= iBtBankAccService.listBankAccBySql(accSql.toString(),params);
            List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
            if (accList != null && accList.size() > 0) {
                sql = " and "+SqlUtil.getInSql(accList, "a.bank_acc_id", true);
            }
        }
        if (StringUtils.isNotEmpty(applyVO.getBankAcc())) {//界面选择了银行信息
            StringBuilder accSql=new StringBuilder();
            Map<String, Object> params=new HashMap();
            accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE a.bank_acc like :bankAcc ");
            params.put("bankAcc",applyVO.getBankAcc()+"%");
            List<BtBankAccDTO> accDTOList= iBtBankAccService.listBankAccBySql(accSql.toString(),params);
            List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
            if (accList != null && accList.size() > 0) {
                sql += " and "+SqlUtil.getInSql(accList, "a.bank_acc_id", true);
            }
        }
        List<FcBondUseDtlVO> voList = searchFcBondDao.queryBondUseDtl(applyVO,sql);
        List<String> bankAccIds = new ArrayList<>();
        for (FcBondUseDtlVO vo : voList) {
            bankAccIds.add(vo.getBankAccId());
        }
        List<BtBankAccDTO> accVOList = iBtBankAccService.getBankAccByBankAccIds(bankAccIds);
        Map<String, BtBankAccDTO> accDTOMap = new HashMap<>();
        for (BtBankAccDTO dto : accVOList) {
            accDTOMap.put(dto.getId(), dto);
        }
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        for (FcBondUseDtlVO vo : voList) {
            BtBankAccDTO accDTO = accDTOMap.get(vo.getBankAccId());
            if (null != accDTO) {
                vo.setBankAcc(accDTO.getBankAcc());
                vo.setAccName(accDTO.getAccName());
                vo.setBankName(accDTO.getBankName());
                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpCode(corpDTO.getCode());
                vo.setCorpName(corpDTO.getName());
                vo.setCurName(iBtCurrencyService.getObjectById(vo.getCurId()).getCurName());
            }
        }
        return voList;
    }

    @Override
    public BaseDao<FcBondApply, String> getEntityDao() {
        return searchFcBondDao;
    }
}