package com.bytter.biz.finacial.accbook.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.finacial.accbook.dao.FcAccbookBlnedDao;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.entity.*;
import com.bytter.biz.finacial.accbook.service.base.*;
import com.bytter.biz.finacial.accbook.util.FcAccbookUtils;
import com.bytter.biz.finacial.accbook.vo.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcAccDTO;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcFinacialParamConfigService;
import com.bytter.biz.finacial.api.util.FinacialUtils;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.paramconfig.service.FcBusRaceService;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import com.bytter.system.api.dto.*;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_ACCBOOK
 * @date 2017-05-31 15:02:10
 */
@Service("fcAccbookService")
@Transactional
public class FcAccbookService extends BaseService<FcAccbook, String> {
    //private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private FcAccbookDao fcAccbookDao;
    @Autowired
    private FcAccbookBlnedDao accbookBlnedDao;
    @Autowired
    private FcAccbookDtlService bookDtlservice;
    @Autowired
    private FcAccbookExtService bookExtservice;
    @Autowired
    private FcAccbookBlnedDtlService bookBlnedDtlservice;
    @Autowired
    private FcAccbookBlnedExtService bookBlnedExtservice;
    @Autowired
    private FcAccbookBlnedService bookBlnedservice;
    @Autowired
    private FcAccbookMsgService bookMsgservice;
    @Autowired
    private FcAccbookHisService bookHisservice;
    @Autowired
    private FcAccbookDtlHisService bookDtlHisservice;
    @Autowired
    private FcAccbookExtHisService bookExtHisservice;
    @Autowired
    private FcBusRaceService busRaceService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IAccountGrantService accGrantService;
    @Autowired
    private FcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    private IFcFinacialParamConfigService paramConfigService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @Autowired(required = false)
    IInputBankInfoService iInputBankInfoService;
    @DubboReference(check = false)
    IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IExternalCorpService iExternalCorpService;
    @Autowired(required = false)
    private FcAccbookDtlService fcAccbookDtlService;
    @Autowired
    private FcVoucherCashService fcVoucherCashService;
    @Autowired
    private OverdraftValidator overdraftValidator;
    @Autowired
    private BookBaseValidator bookBaseValidator;
    @Autowired
    private UnionAccbookProcessor unionAccbookProcessor;
    @Autowired
    private ChargeProcessor chargeProcessor;
    @Autowired
    private BankDirectProcessor bankDirectProcessor;

    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;

    @DubboReference(check = false)
    private IRegionService regionService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcAccbook entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcAccbook entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcAccbook entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcAccbook entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<FcAccbook, String> getEntityDao() {
        return fcAccbookDao;
    }
    /**
     * 功能介绍 : 获取网点自动记账人。<br>
     * <b>日期：</b>2017年11月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public String getAutoTallerUser(String netId) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "autoAccounter");
        String paramValue = configDTO.getValue();
        if (EmptyUtils.isEmpty(paramValue)) {//自动记账人为空
        	 throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
        paramValue = paramValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netUsers = paramValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和记账人
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());//获取记账人
        if (EmptyUtils.isEmpty(userCode)) {
        	String[] param = {corpDTO.getName() + "(" + corpDTO.getCode() + ")"};
        	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NETNULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER,param);
        }
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO) {
            return userDTO.getId();
        }
        throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_USER_NOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
    }
    /**
     * 功能介绍 : 获取网点自动记账人。<br>
     * <b>日期：</b>2017年11月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public String getAutoCheckUser(String netId, boolean innerCarryFlag) {
        ConfigDTO configDTO = null;
        if (innerCarryFlag) {
            configDTO = configService.getConfig(ConfigConstant.FCL, "carryAutoChecker");
        } else {
            configDTO = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        }
        String paramValue = configDTO.getValue();
        if (EmptyUtils.isEmpty(paramValue)) {//自动记账人为空
            throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
        paramValue = paramValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netUsers = paramValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和记账人
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = null;
//		网点/资金池，当查不到网点数据时则为资金池
        logger.error("异常报错地方。");
        if (null != corpDTO) {
            userCode = userInfo.get(corpDTO.getCode());//获取复核人
            if (EmptyUtils.isEmpty(userCode)) {
            	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NETNULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
            }
        } else {
            FcCorpPoolDTO corpPoolDTO = fcCorpPoolService.getById(netId);//获取资金池主体
            String poolCode = corpPoolDTO.getPoolCode();
            userCode = userInfo.get(poolCode);
            if (EmptyUtils.isEmpty(userCode)) {
            	String[] param  = {corpPoolDTO.getPoolName() + "(" + corpPoolDTO.getPoolCode() + ")"};
            	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_POOL_TALLY_AUTOCHECKER_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER,param);
            }
        }
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO) {
            return userDTO.getId();
        }
        String[] param  = {userCode};
        throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_USER_NOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER,param);
    }
    /**
     * 功能介绍 : 设置自动记账人。<br>
     * <b>日期：</b>2017年11月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setTallerUser(VoucherWrapper voucher) {
        if (voucher.isAutoAccounter()) {//自动记账
            FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
            if (!EmptyUtils.isEmpty(voucherVO)) {
                String netCode = voucherVO.getNetCode();//记账网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setUserCode(getAutoTallerUser(netCode));
                    return;
                }
                FcAccbook accbook = voucherVO.getAccbook();
                netCode = accbook.getNetId();//记账网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setUserCode(getAutoTallerUser(netCode));
                    return;
                }
                List<FcAccbookDtl> accbookDtls = voucherVO.getBookDtls();
                if (!EmptyUtils.isEmptyList(accbookDtls)) {
                    FcAccbookDtl accbookDtl = accbookDtls.get(0);
                    String account = accbookDtl.getAccount();
                    com.bytter.system.api.dto.FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(account);
                    netCode = accDTO.getNetId();//账号网点
                    if (!EmptyUtils.isEmpty(netCode)) {
                        voucher.setUserCode(getAutoTallerUser(netCode));
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 : 设置自动复核人。<br>
     * <b>日期：</b>2017年11月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setCheckUser(VoucherWrapper voucher) {
        boolean innerCarryFlag = EmptyUtils.isEmpty(voucher.getInnerCarryFlag()) ? false : voucher.getInnerCarryFlag();
        if (voucher.isAutoAccounter() || voucher.isAutoCheck()) {//自动记账
            FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
            if (voucher.isHaveCheckUser()) {//指定复核人
                if (!EmptyUtils.isEmpty(voucherVO)) {
                    String generateMode = voucherVO.getAccbook().getGenerateMode();
                    if (FinaBookConstant.GenerateMode.INTEREST.get().equals(generateMode) || FinaBookConstant.GenerateMode.BANK_POOL.get().equals(generateMode)) {
                        if (EmptyUtils.isNotEmpty(voucher.getCheckUser())) {
                            voucher.setUserCode(voucher.getCheckUser());
                        } else {
                            voucher.setUserCode(voucher.getUserCode());
                        }
                        return;
                    }
                }
            }
            String netCode = voucher.getNetId();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                voucher.setUserCode(getAutoCheckUser(netCode,innerCarryFlag));
                return;
            }
            if (!EmptyUtils.isEmpty(voucherVO)) {
                netCode = voucherVO.getNetCode();//记账网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setUserCode(getAutoCheckUser(netCode,innerCarryFlag));
                    return;
                }
                FcAccbook accbook = voucherVO.getAccbook();
                netCode = accbook.getNetId();//记账网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setUserCode(getAutoCheckUser(netCode,innerCarryFlag));
                    return;
                }
                List<FcAccbookDtl> accbookDtls = voucherVO.getBookDtls();
                if (!EmptyUtils.isEmptyList(accbookDtls)) {
                    FcAccbookDtl accbookDtl = accbookDtls.get(0);
                    String account = accbookDtl.getAccount();
                    com.bytter.system.api.dto.FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(account);
                    netCode = accDTO.getNetId();//账号网点
                    if (!EmptyUtils.isEmpty(netCode)) {
                        voucher.setUserCode(getAutoCheckUser(netCode,innerCarryFlag));
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 : 获取当前登陆用户的网点ID。<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public CorpDTO getUserNet(ICurrentUser user) {
        String netId = "";
        UserDTO userDTO = userService.getByLoginName(user.getLoginName());
        CorpDTO corpDto = userDTO.getCorp();
        while (CorpConstant.CORP_TYPE_CODE_CORP.toString().equals(corpDto.getType().toString())) {
            String parentId = corpDto.getParentId();
            if (!StringUtils.isEmpty(parentId)) {
                corpDto = corpService.get(parentId);
            } else {
                break;
            }
        }
        return corpDto;
    }
    /**
     * 功能介绍 : 获取用户的可用业务规则。<br>
     * <b>日期：</b>2017年12月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<FcBusRaceVo> getUserBusRace(ICurrentUser user) {
        CorpDTO corpDto = getUserNet(user);
        String netId = corpDto.getId();//网点ID
        List<FcBusRaceVo> busRaces = busRaceService.findByNetIds(new String[]{netId});
        if (null == busRaces) {
            busRaces = new ArrayList<FcBusRaceVo>();
        }
        return busRaces;
    }
    /**
     * 功能介绍 : 获取用户的可用业务规则。<br>
     * <b>日期：</b>2017年12月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<FcBusRaceVo> getBusRace() {
        List<FcBusRaceVo> busRaces = busRaceService.listAllValid();
        if (null == busRaces) {
            busRaces = new ArrayList<FcBusRaceVo>();
        }
        return busRaces;
    }
    @Transactional(readOnly = true)
    public List<FcAccbookDtl> getFcAccBookDtlByBooKId(String bookId) {
        return bookDtlservice.getAccbookDtlsByBookId(bookId);
    }
    /**
     * 功能介绍 : 获取凭证的所有关联账务。<br>
     * <b>日期：</b>2017年6月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<FcAccbookVoucherVO> getBookVoucherByBusGlobalId(String globalId) {
        List<FcAccbookVoucherVO> list = new ArrayList<FcAccbookVoucherVO>();
        List<FcAccbookDtl> bookDtls = bookDtlservice.getBookDtlsByBusGlobalId(globalId);
        Map<String, List<FcAccbookDtl>> bookDtlMap = new HashMap<String, List<FcAccbookDtl>>();
        for (FcAccbookDtl bookDtl : bookDtls) {
            String bookId = bookDtl.getBookId();
            List<FcAccbookDtl> dtls = bookDtlMap.get(bookId);
            if (dtls == null) {
                dtls = new ArrayList<FcAccbookDtl>();
                bookDtlMap.put(bookId, dtls);
            }
            dtls.add(bookDtl);
        }
        List<FcAccbookExt> bookExts = bookExtservice.getBookExtByBusGlobalId(globalId);
        Map<String, FcAccbookExt> extMap = new HashMap<String, FcAccbookExt>();
        for (FcAccbookExt bookExt : bookExts) {
            extMap.put(bookExt.getBookId(), bookExt);
        }
        List<FcAccbook> books = fcAccbookDao.getBooksByBusGlobalId(globalId);
        for (FcAccbook book : books) {
            FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
            String bookId = book.getId();
            List<FcAccbookDtl> dtls = bookDtlMap.get(bookId);
            if (null == dtls) {
                dtls = getFcAccBookDtlByBooKId(bookId);
            } else {
                Collections.sort(dtls, new Comparator<FcAccbookDtl>() {
                    @Override
                    public int compare(FcAccbookDtl o1, FcAccbookDtl o2) {
                        return o1.getLineNo() - o2.getLineNo();
                    }
                });
            }
            List<FcAccbookDtl> bookDtlVOs = BeanUtils.copyListProperties(dtls, FcAccbookDtl.class);
            FcAccbook accbookVO = new FcAccbook();
            BeanUtils.copyNotNullProperties(book, accbookVO);
            FcAccbookExt bookExt = extMap.get(bookId);
            if (null != bookExt) {
                FcAccbookExt bookExtVO = new FcAccbookExt();
                BeanUtils.copyNotNullProperties(bookExt, bookExtVO);
                voucherVO.setBookExt(bookExtVO);//账务扩展数据
            }
            voucherVO.setBookDtls(bookDtlVOs);//账务明细
            voucherVO.setAccbook(accbookVO);//账务主表数据
            list.add(voucherVO);
        }
        return list;
    }
    /**
     * 获取同一流水号的所有账务信息
     */
    @SuppressWarnings("unchecked")
    public FcAccbookVO getBookVOByOrderNo(String orderNo, String isPool) {
        FcAccbookVO fcVo;
        if (FinacialConstant.BOOLEAN_TRUE.equals(isPool)) {
            fcVo = fcAccbookDao.getPoolAccbookByOrderNo(orderNo);
        } else {
            fcVo = fcAccbookDao.getAccbookByOrderNo(orderNo);
        }
        if(Objects.nonNull(fcVo)){
            setData(fcVo);
        }
        return fcVo;
    }
    private void setData(FcAccbookVO fcVo) {
        BtCurrencyDTO currency = iBtCurrencyService.getObjectById(fcVo.getCurCode());
        if (null != currency) {
            fcVo.setCurCode(currency.getCurName());
        }
        FcVoucherTypeDTO voucherType = iFcVoucherTypeService.getFcVoucherTypeById(fcVo.getVoucherType());
        if (null != voucherType) {
            fcVo.setVoucherName(voucherType.getVoucherName());
        }
        if(null == fcVo.getRaceName() && null != fcVo.getBusRaceName() ){
            fcVo.setRaceName(fcVo.getBusRaceName());
        }

    }
    /**
     * 功能介绍 : 获取同一全局ID的所有已日结的账务信息。<br>
     * <b>日期：</b>2017年11月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<FcAccbook> getBooksByBusGlobalId(String busGlobalId) {
        return fcAccbookDao.getBooksByBusGlobalId(busGlobalId);
    }
    /**
     * 功能介绍 : 获取同一全局ID的所有账务信息。<br>
     * <b>日期：</b>2018年9月11日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public FcAccbook getAccbookByBusGlobalId(String busGlobalId) {
        return fcAccbookDao.getAccbookByBusGlobalId(busGlobalId);
    }
    /**
     * 获取网点主管
     */
    public String getDirectorUser(String netId) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "director");
        String paramValue = configDTO.getValue();
        if (EmptyUtils.isEmpty(paramValue)) {//自动记账人为空
            return "";
        }
        paramValue = paramValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netUsers = paramValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和主管
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());//获取主管
        if (EmptyUtils.isEmpty(userCode)) {
            return "";
        }
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO) {
            return userDTO.getUsername();
        }
        return "";
    }
    /**
     * 功能介绍 : 获取页面显示凭证，附带凭证相关信息。<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public FcAccbookPageVO getFcAccbookPageVOById(String id) {
        FcAccbookPageVO pageVO = new FcAccbookPageVO();
        FcAccbookVO accbookVO = fcAccbookDao.getAccbookVoById(id, false);
        if (EmptyUtils.isEmpty(accbookVO)) {//已日结
            FcAccbookPageVO page = bookBlnedservice.getFcAccbookBlnedPageVOById(id, false);
            page.getAccbook().setManageName(this.getDirectorUser(page.getAccbook().getNetId()));
            for(FcAccbookDtlVO vo:page.getBookDtls()) {
                com.bytter.system.api.dto.FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(vo.getAccount());
                vo.setSubjectId(fcAccDTO.getSubjectId());
                vo.setSubjectName(fcAccDTO.getSubjectName());
            }
            FcAccbookExtVO accbookExt = page.getBookExt();
            RegionDTO regionDTO = regionService.getById(accbookExt.getExternalCity());
            if (Objects.nonNull(regionDTO)) {
                accbookExt.setCity(regionDTO.getName());
                accbookExt.setProv(regionDTO.getProvName());
            }
            return page;
        }
//        FcAccbookVO accbook = fcAccbookDao.getAccbookVoById(id, false);
        if (FinaBookConstant.GenerateMode.DEPOSIT.get().equals(accbookVO.getGenerateMode())
                || FinaBookConstant.GenerateMode.INTEREST.get().equals(accbookVO.getGenerateMode())
                || FinaBookConstant.GenerateMode.CREDIT_BOOK.get().equals(accbookVO.getGenerateMode())) {
            if (accbookVO.getAffixAmount() == null) {
                accbookVO.setAffixAmount(0);
            }
        }
        BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(accbookVO.getCurCode());
        FcVoucherTypeDTO voucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(accbookVO.getVoucherType());
        Map<String, String> map = userService.getUserNameByIds(Lists.newArrayList(accbookVO.getOperatorCode(), accbookVO.getCheckerCode()));

        accbookVO.setVoucherName(voucherTypeDTO.getVoucherName());
        accbookVO.setCurName(currencyDTO.getCurName());
        accbookVO.setOperatorName(map.get(accbookVO.getOperatorCode()));
        accbookVO.setCheckerName(map.get(accbookVO.getCheckerCode()));

        pageVO.setAccbook(accbookVO);
        pageVO.getAccbook().setManageName(this.getDirectorUser(accbookVO.getNetId()));
        List<FcAccbookDtlVO> fcAccbookDtlVOList= fcAccbookDao.getPageDtlsByBookId(id, false);
        pageVO.setBookDtls(fcAccbookDtlVOList);
        //TODO后续加上账务外单位信息
        FcAccbookExt accbookExt = bookExtservice.getBookExtByBookId(id);
        FcAccbookExtVO extVO = new FcAccbookExtVO();
        if (accbookExt != null) {
            RegionDTO regionDTO = regionService.getById(accbookExt.getExternalCity());
            if (Objects.nonNull(regionDTO)) {
                accbookExt.setCity(regionDTO.getName());
                accbookExt.setProv(regionDTO.getProvName());
            }
            BeanUtils.copyProperties(accbookExt, extVO);
        }
        pageVO.setBookExt(extVO);
        return pageVO;
    }
    /**
     * 功能介绍 : 获取页面显示凭证，附带凭证相关信息。<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public FcAccbookPageVO getFcAccbookPageVOByVoucherNo(String voucherNo) {
        FcAccbookPageVO pageVO = new FcAccbookPageVO();
        FcAccbookVO accbookVO = fcAccbookDao.getAccbookVOByVoucherNo(voucherNo);
        if (EmptyUtils.isEmpty(accbookVO)) {//已日结
            return bookBlnedservice.getFcAccbookBlnedPageVOByVoucherNo(voucherNo, false);
        }
        FcAccbookVO accbook = fcAccbookDao.getAccbookVoById(accbookVO.getId(), false);
        if (FinaBookConstant.GenerateMode.DEPOSIT.get().equals(accbook.getGenerateMode())
                || FinaBookConstant.GenerateMode.INTEREST.get().equals(accbook.getGenerateMode())
                || FinaBookConstant.GenerateMode.CREDIT_BOOK.get().equals(accbook.getGenerateMode())) {
            if (accbook.getAffixAmount() == null) {
                accbook.setAffixAmount(0);
            }
        }
        FcVoucherTypeDTO voucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(accbook.getVoucherType());
        accbook.setVoucherName(voucherTypeDTO.getVoucherName());
        BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(accbook.getCurCode());
        accbook.setCurName(btCurrencyDTO.getCurName());
        pageVO.setAccbook(accbook);
        pageVO.setBookDtls(fcAccbookDao.getPageDtlsByBookId(accbookVO.getId(), false));
        //TODO后续加上账务外单位信息
        FcAccbookExt accbookExt = bookExtservice.getBookExtByBookId(accbookVO.getId());
        FcAccbookExtVO extVO = new FcAccbookExtVO();
        if (null != accbookExt) {
            RegionDTO regionDTO = regionService.getById(accbookExt.getExternalCity());
            if (Objects.nonNull(regionDTO)) {
                accbookExt.setCity(regionDTO.getName());
                accbookExt.setProv(regionDTO.getProvName());
            }
            BeanUtils.copyProperties(accbookExt, extVO);
        }
        pageVO.setBookExt(extVO);
        return pageVO;
    }
    /**
     * 功能介绍 ： 获取账务凭证对象，用于数据存储。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public FcAccbookVoucherVO getFcAccbookVoucherVO(String id) {
        FcAccbookVoucherVO voucher = new FcAccbookVoucherVO();
        FcAccbook fcAccbook = fcAccbookDao.getAccbookById(id);
        FcVoucherTypeDTO voucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(fcAccbook.getVoucherType());
        if (!Objects.isNull(voucherTypeDTO)){
            fcAccbook.setVoucherName(voucherTypeDTO.getVoucherName());
        }
        voucher.setAccbook(fcAccbook);//账务主表数据
        voucher.setBookDtls(getFcAccBookDtlByBooKId(id));//账务明细
        FcAccbookExt accbookExt = bookExtservice.getBookExtByBookId(id);
        if (null != accbookExt) {
            RegionDTO regionDTO = regionService.getById(accbookExt.getExternalCity());
            if (Objects.nonNull(regionDTO)) {
                accbookExt.setCity(regionDTO.getName());
                accbookExt.setProv(regionDTO.getProvName());
            }
        }
        voucher.setBookExt(accbookExt);//账务外单位其他信息
        return voucher;
    }
    @Transactional(readOnly = true)
    public void getFcAccbookVoucherVOById(String busGlobalId, FcAccbookVoucherVO voucher) {
        List<FcAccbook> accbooks = fcAccbookDao.getBooksByBusGlobalId(busGlobalId);
        if (EmptyUtils.isEmptyList(accbooks)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
        }
        FcAccbook accbook = accbooks.get(0);//进这里不
        String id = accbook.getId();
        if (accbook.getUpdateTime() == null){
            accbook.setUpdateTime(new Date());
        }
        voucher.setAccbook(accbook);//账务主表数据
        voucher.setBookDtls(getFcAccBookDtlByBooKId(id));//账务明细
        voucher.setBookExt(bookExtservice.getBookExtByBookId(id));//账务外单位其他信息
    }

    @Transactional(readOnly = true)
    public void getCheckFcAccbookVoucherVOById(String busGlobalId, FcAccbookVoucherVO voucher) {
        List<FcAccbook> accbooks = fcAccbookDao.getBooksByBusGlobalId(busGlobalId);
        if (EmptyUtils.isEmptyList(accbooks)) {
            throw new WorkFlowVerifyException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);//状态已改变，刷新页面重试
        }
        FcAccbook accbook = accbooks.get(0);//进这里不
        String id = accbook.getId();
        if (accbook.getUpdateTime() == null){
            accbook.setUpdateTime(new Date());
        }
        voucher.setAccbook(accbook);//账务主表数据
        voucher.setBookDtls(getFcAccBookDtlByBooKId(id));//账务明细
        voucher.setBookExt(bookExtservice.getBookExtByBookId(id));//账务外单位其他信息
    }

    @Transactional(readOnly = true)
    public void checkGetCheckFcAccbookVoucherVOById(String busGlobalId, FcAccbookVoucherVO voucher) {
    	  if(StringUtils.isEmpty(busGlobalId)) {
    		  throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);//状态已改变，刷新页面重试
    	  }
//        List<FcAccbook> accbooks = fcAccbookDao.getBooksByBusGlobalId(busGlobalId);
//        if (EmptyUtils.isEmptyList(accbooks)) {
//            throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);//状态已改变，刷新页面重试
//        }
//        FcAccbook accbook = accbooks.get(0);//进这里不
//        String id = accbook.getId();
//        if (accbook.getUpdateTime() == null){
//            accbook.setUpdateTime(new Date());
//        }
//        voucher.setAccbook(accbook);//账务主表数据
//        voucher.setBookDtls(getFcAccBookDtlByBooKId(id));//账务明细
//        voucher.setBookExt(bookExtservice.getBookExtByBookId(id));//账务外单位其他信息
    }
    /**
     * 功能介绍 ：获取记账凭证业务未完成数据。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getNoCheckBookDtl(DataTablesInput input, FcDayCloseDTO dayClose) {
        // 状态记账打回、暂存、待复核'F','T','C','D','M','W'
        input.addFilter(MatchTypeEnum.IN, "voucherState",
                FinaBookConstant.VoucherStatus.STATUS_BACK.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_TEMP.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get());
        //input.addFilter(MatchTypeEnum.LE, "tallyDate", dayClose.getCloseDate());//记账日期前
        input.addFilter(MatchTypeEnum.LE, "tallyDate", DateUtils.formatDate(dayClose.getCloseDate()), Date.class);//记账日期前
        input.addFilter(MatchTypeEnum.EQ, "netId", dayClose.getNetCode());//网点
        DataTablesOutput dataTablesOutput = fcAccbookDao.queryCanEditAccbooks(input, false);
        List<FcAccbookListVO> data = dataTablesOutput.getData();
        List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
        List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
        Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
        if (CollectionUtils.isNotEmpty(dtos)){
            data.forEach(a -> {
                a.setVoucherName(map.get(a.getVoucherType()));
                a.setVoucherId(a.getVoucherType());
            });
        }
        return dataTablesOutput;
    }
    /**
     * 功能介绍 ：获取记账凭证列表数据。<br>
     * <b>日期：</b>2017年5月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getEditFcAccbookVoList(DataTablesInput input, String resource, String userId) {
        input.addFilter(MatchTypeEnum.EQ, "busOriType", FinaBookConstant.BusOriType.TAILLY.get());//手工凭证
        input.addFilter(MatchTypeEnum.EQ, "redblueSign", FinacialConstant.RB_SIGN_B);//正常凭证
        input.addFilter(MatchTypeEnum.EQ, "operatorCode", userId);//记账人
        // 状态记账打回、暂存、待复核
        input.addFilter(MatchTypeEnum.IN, "voucherState",
                FinaBookConstant.VoucherStatus.STATUS_BACK.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_TEMP.get() + "," +
                        FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get());
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
        DataTablesOutput dataTablesOutput = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
        List<FcAccbookListVO> data = dataTablesOutput.getData();
        List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
        List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
        Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
        if (CollectionUtils.isNotEmpty(dtos)){
            data.forEach(a -> {
                a.setVoucherName(map.get(a.getVoucherType()));
                a.setVoucherId(a.getVoucherType());
            });
        }
        return dataTablesOutput;
    }
    /**
     * 功能介绍 ：获取记账凭证列表数据。<br>
     * <b>日期：</b>2017年5月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getCheckFcAccbookVoList(DataTablesInput input, String resource, String userId) {
        input.addFilter(MatchTypeEnum.EQ, "busOriType", FinaBookConstant.BusOriType.TAILLY.get());//手工凭证
        input.addFilter(MatchTypeEnum.EQ, "redblueSign", FinacialConstant.RB_SIGN_B);//正常凭证
        input.addFilter(MatchTypeEnum.NE, "operatorCode", userId);//FIXME 不能是记账人
        // 状态记账打回、暂存、待复核
        input.addFilter(MatchTypeEnum.EQ, "voucherState", FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get());
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
        DataTablesOutput dataTablesOutput = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
        List<FcAccbookListVO> data = dataTablesOutput.getData();

        List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
        List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
        Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));

        List<String> operatorIds = data.stream().map(FcAccbookListVO::getOperatorCode).collect(Collectors.toList());
        Map<String, String> userMap = userService.getUserNameByIds(operatorIds);
        if (CollectionUtils.isNotEmpty(dtos)){
            data.forEach(a -> {
                a.setVoucherName(map.get(a.getVoucherType()));
                a.setVoucherId(a.getVoucherType());
                a.setOperatorCode(userMap.get(a.getOperatorCode()));
            });
        }
        return dataTablesOutput;
    }
    /**
     * 功能介绍 :消息完成。<br>
     * <b>日期：</b>2017年11月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void completeEditNotificationInfo(VoucherWrapper voucher) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (FinaBookConstant.GenerateMode.TAILLY.get().equals(accbook.getGenerateMode())) {
                notificationService.complete(NotificationConstant.FINACIAL.BOOK_EDIT, accbook.getId(), voucher.getUserCode());
            }
        }
    }
    /**
     * 功能介绍 :消息完成。<br>
     * <b>日期：</b>2017年11月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void completeCheckNotificationInfo(VoucherWrapper voucher) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (FinaBookConstant.GenerateMode.TAILLY.get().equals(accbook.getGenerateMode())) {
                //打回改账处，通知提醒记账人
                if (!EmptyUtils.isEmpty(accbook.getId())) {
                    notificationService.complete(NotificationConstant.FINACIAL.BOOK_CHECK, accbook.getId(), voucher.getUserCode());
                }
            }
        }
    }
    /**
     * 功能介绍 : 发送消息提醒。<br>
     * <b>日期：</b>2017年11月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void sendCheckNotificationInfo(VoucherWrapper voucher) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (FinaBookConstant.GenerateMode.TAILLY.get().equals(accbook.getGenerateMode())) {
                // 发送消息复核提醒
                FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
                notifyVO.setOrderNo(accbook.getOrderNo());
                List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
                BigDecimal money = BigDecimal.ZERO;
                if (!EmptyUtils.isEmptyList(bookDtls)) {
                    for (FcAccbookDtl accbookDtl : bookDtls) {
                        if (null != accbookDtl.getDebitMoney()) {
                            money = money.add(accbookDtl.getDebitMoney());
                        }
                    }
                }
                notifyVO.setMoney(money);
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(),
                        NotificationConstant.FINACIAL.BOOK_CHECK, notifyVO, voucher.getUserCode());
            }
        }
    }
    /**
     * 功能介绍 :消息打回。<br>
     * <b>日期：</b>2017年11月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void sendBackNotificationInfo(VoucherWrapper voucher) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            if (FinaBookConstant.GenerateMode.TAILLY.get().equals(accbook.getGenerateMode())) {
                FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
                notifyVO.setOrderNo(accbook.getOrderNo());
                List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
                BigDecimal money = BigDecimal.ZERO;
                if (!EmptyUtils.isEmptyList(bookDtls)) {
                    for (FcAccbookDtl accbookDtl : bookDtls) {
                        if (null != accbookDtl.getDebitMoney()) {
                            money = money.add(accbookDtl.getDebitMoney());
                        }
                    }
                }
                notifyVO.setMoney(money);
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(),
                        NotificationConstant.FINACIAL.BOOK_EDIT, notifyVO);
            }
        }
    }
    /**
     * 功能介绍 : 凭证数据保存前校验。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void preVoucherSave(VoucherWrapper voucher) {
        if (StringUtils.isEmpty(voucher.getBusGlobalId())) {
            voucher.setOpType(OperateType.ADD);
        } else {
            voucher.setOpType(OperateType.MODIFY);
        }
        //设置需要更新旧凭证记录
        processOriVoucher(voucher);
        String busGlobalId = voucher.getBusGlobalId();
        if (StringUtils.isBlank(busGlobalId)) {
            busGlobalId = FinacialUtils.createGlobalId();//生成全局ID
            voucher.setBusGlobalId(busGlobalId);
        }
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (StringUtils.isEmpty(accbook.getId())) {
                voucherVO.setOpType(OperateType.ADD);//记账暂存
            } else {
                voucherVO.setOpType(OperateType.MODIFY);//记账修改
            }
            //判断是否新增全局ID
            accbook.setBusGlobalId(busGlobalId);//生成全局ID
            initAccbook(accbook, FinaBookConstant.VoucherStatus.STATUS_TEMP.get());//暂存状态
            accbook.setOperatorCode(voucher.getUserCode());// 记账员
            //校验凭证数据有效性
            validateVoucher(voucherVO);
        }
        for (FcAccbookVoucherVO oriAccbook : voucher.getOriTarget()) {
            oriAccbook.setOpType(voucher.getOpType());
        }
    }
    /**
     * 功能介绍 : 设置需要更新旧凭证记录。<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void processOriVoucher(VoucherWrapper voucher) {
        if (StringUtils.isEmpty(voucher.getBookVoucher().getAccbook().getId())) {//如果凭证ID为空，则没有原凭证信息，为新录入的数据
            return;
        }
        //查询原凭证数据
        FcAccbook accbook = voucher.getBookVoucher().getAccbook();
        String busGlobalId = accbook.getBusGlobalId();
        List<FcAccbookVoucherVO> voucherVOs = getBookVoucherByBusGlobalId(busGlobalId);
        if (EmptyUtils.isEmptyList(voucherVOs)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
        }
        voucher.addOriTargets(voucherVOs);
    }

    /**
     * 功能介绍 : 设置需要更新旧凭证记录。<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void checkProcessOriVoucher(VoucherWrapper voucher) {
        if (StringUtils.isEmpty(voucher.getBookVoucher().getAccbook().getId())) {//如果凭证ID为空，则没有原凭证信息，为新录入的数据
            return;
        }
        //查询原凭证数据
        FcAccbook accbook = voucher.getBookVoucher().getAccbook();
        String busGlobalId = accbook.getBusGlobalId();
        List<FcAccbookVoucherVO> voucherVOs = getBookVoucherByBusGlobalId(busGlobalId);
        if (EmptyUtils.isEmptyList(voucherVOs)) {
            throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);//状态已改变，刷新页面重试
        }
        voucher.addOriTargets(voucherVOs);
    }
    /**
     * 功能介绍 : 记账暂存凭证对象。<br>
     * <b>日期：</b>2017年5月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveFcAccbook(VoucherWrapper voucher) {
        //1、保存前数据校验
        preVoucherSave(voucher);
        //2、数据准备保存凭证
        processVoucher(voucher);
        completeEditNotificationInfo(voucher);//改账消息处理
        completeCheckNotificationInfo(voucher);//复核消息处理
    }
    /**
     * 功能介绍 : 内存锁账务凭证号。<br>
     * <b>日期：</b>2020年5月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void lockVoucherNet(VoucherWrapper voucher) {
        String netCode = "";
        String curCode = "";
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (FinaBookConstant.GenerateMode.BANK_POOL.get().equals(accbook.getGenerateMode())) {
                return;
            }
            List<FcAccbookDtl> dtlVOS = voucherVO.getBookDtls();
            if (EmptyUtils.isNotEmpty(dtlVOS)) {
                String account = dtlVOS.get(0).getAccount();
                com.bytter.system.api.dto.FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(account);
                CorpDTO corpDTO = corpService.get(fcAccDTO.getNetId());
                netCode = corpDTO.getCode();
                BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(fcAccDTO.getCurId());
                curCode = currencyDTO.getCurCode();
            }
        }
        if (EmptyUtils.isNotEmpty(netCode) && EmptyUtils.isNotEmpty(curCode)) {
            SequenceSingleton.lockOnly(SequenceEnum.VOUCHER_NO, netCode + curCode, netCode);//锁住流水号规则，不让其他记账业务取流水号
        }
    }
    /**
     * 功能介绍 : 处理凭证。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void processVoucher(VoucherWrapper voucher) {
        //1、获取账务网点ID，加锁
        lockVoucherNet(voucher);
        //		获取初始联机余额信息
        List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
        //2、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
        Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
        //3、数据保存
        processVoucherSave(voucher, ledgerMap, origOnlineLedgerDTOList);
    }

    /**
     * 功能介绍 : 处理凭证。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void checkProcessVoucher(VoucherWrapper voucher) {
        //1、获取账务网点ID，加锁
        //lockVoucherNet(voucher);//没有抛异常不校验
        //		获取初始联机余额信息
        List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
        //2、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
        Map<String, FcOnlineLedgerDTO> ledgerMap = checkProcessAccountBalance(voucher);
        //3、数据保存
        //processVoucherSave(voucher, ledgerMap, origOnlineLedgerDTOList);//没有抛异常不需要保存数据
    }
    /**
     * 功能介绍 : 凭证保存。<br>
     * <b>日期：</b>2017年6月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected synchronized void processVoucherSave(VoucherWrapper voucher,
                                                   Map<String, FcOnlineLedgerDTO> ledgerMap, List<FcOnlineLedgerDTO> origLedgerDTOs) {
        Map<String, FcAccbookVoucherVO> accbookMap = new HashMap<String, FcAccbookVoucherVO>();
        UserDTO userDTO =  userService.getByUserId(voucher.getUserCode());
        for (FcAccbookVoucherVO voucherVO : voucher.getOriTarget()) {
            accbookMap.put(voucherVO.getAccbook().getId(), voucherVO);
        }
        Set<String> summaryContent = new HashSet<String>();
        //填充冗余字段数据
        List<FcAccbookVoucherVO> target = voucher.getTarget();
        ArrayList<String> accounts = new ArrayList<>();
        target.forEach(a -> {
            List<FcAccbookDtl> bookDtls = a.getBookDtls();
            Set<String> collect = bookDtls.stream().map(FcAccbookDtl::getAccount).collect(Collectors.toSet());
            accounts.addAll(collect);
        });
        this.fillData(accounts,target);
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            String curCode = voucherVO.getBookDtls().get(0).getCurCode();//币别
            String account = voucherVO.getBookDtls().get(0).getAccount();//账号
            String origVoucherNo = voucherVO.getBookDtls().get(0).getOrigVoucherNo();//原始凭证号
            FcOnlineLedgerDTO ledgerDTO = ledgerMap.get(account);
            String netCode = ledgerDTO.getNetCode();//网点CODE
            String netId = ledgerDTO.getNetId();//网点ID
            /*
             * 整理要更新的数据
             * 1、更新余额
             * 2、写入账务表
             */
            accbook.setCurCode(curCode);//设置币别
            accbook.setNetId(netId);//设置网点
            if (EmptyUtils.isEmpty(accbook.getOrigVoucherNo())) {
                accbook.setOrigVoucherNo(origVoucherNo);
            }
            voucher.setNetId(netId);//记账网点
            if (voucherVO.getOpType() == OperateType.DELETE) {
                //FIXME 删除该凭证
                deleteBookVoucher(voucherVO, voucher.getUserCode());
                continue;
            }
            accbook.setCreateTime(new Date());
            accbook.setUpdateTime(new Date());
            accbook.setCreateBy(userDTO.getUsername());
            accbook.setUpdateBy(userDTO.getUsername());
            accbook.setCheckerCode(voucher.getCheckUser());
            if (StringUtils.isBlank(accbook.getOperatorCode())){
                accbook.setOperatorCode(voucher.getUserCode());
            }
//            if(accbook.getBusOriType()!=FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.get()){
//
//            }
            if (StringUtils.isEmpty(accbook.getId())) {//如果凭证ID为空，则没有原凭证信息，为新录入的数据
                accbook.setOrderNo(getAccbookOrderNo(netCode));//FIXME 网点CODE待确认

                fcAccbookDao.save(accbook);
            } else {
                //保存修改记录 写入历史记录表，获取原改账的记录
                FcAccbookVoucherVO oriVoucherVO = accbookMap.get(accbook.getId());
                backupFcAccbook(oriVoucherVO, FinaBookConstant.VoucherOpType.OpType_M.get());
                //TODO 是否返回的对象还是指向原来的对象，需验证
                voucherVO = copyNewAccbook2OriAccbook(oriVoucherVO, voucherVO);
                fcAccbookDao.merge(accbook);
            }
            String accbookId = accbook.getId();
            fcAccbookDao.flush();//刷新session提交
            List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
            int lineNo = 1;//判断凭证分录是否和原凭证分录数量一致
            for (FcAccbookDtl accbookDtl : bookDtls) {
                FcOnlineLedgerDTO ledger = ledgerMap.get(accbookDtl.getAccount());//逐笔计算凭证余额

                accbookDtl.setBookId(accbookId);
                accbookDtl.setLineNo(lineNo++);
                //处理明细余额，明细上的余额不显示，复核时再写入
//				accbookDtl.setBalance(ledger.getOnlineBalance());
                //FIXME 账户余额更新待处理
                bookDtlservice.merge(accbookDtl);
                summaryContent.add(accbookDtl.getSummaryContent());//摘要信息
                //后面会查询 先flush
                bookDtlservice.getEntityDao().flush();
            }
            FcAccbookExt bookExt = voucherVO.getBookExt();
            if (null != bookExt) {

                bookExt.setBookId(accbook.getId());
                bookExtservice.saveOrUpdate(bookExt);
            }
            if (!FinaBookConstant.GenerateMode.INTEREST.get().equals(accbook.getGenerateMode())) {//非利息
                bookMsgservice.saveBookMsg(accbook.getId(), getMsgType(), voucherVO.getOpType(), null, voucher.getUserCode());
            }
        }
//		paramConfigService.saveSummary(voucher.getUserCode(), summaryContent.toArray(new String[summaryContent.size()]));//保存不存在的摘要信息
        //更新账户联机余额
        fcOnlineLedgerService.updateOnlineBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()), origLedgerDTOs);
    }

    /**
     * 填充冗余字段数据
     */
    private void fillData(List<String> accounts, List<FcAccbookVoucherVO> target){
        //acc_name、subject_id、subject_name、corp_id、balance_way、cur_id、cur_name、net_id

        List<com.bytter.system.api.dto.FcAccDTO> fcAccDTOList = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, String> accountAndSubjectIdMap = fcAccDTOList.stream().collect(Collectors.toMap(com.bytter.system.api.dto.FcAccDTO::getBlnctrAcc, com.bytter.system.api.dto.FcAccDTO::getSubjectId,(a,b)->a));
        List<String> subjectIds = accountAndSubjectIdMap.values().stream().collect(Collectors.toList());
        List<FcSubjectDTO> subjectLists = iFcSubjectService.getSubjectByIds(subjectIds);
        Map<String, String> subjectNameMap = subjectLists.stream().collect(Collectors.toMap(FcSubjectDTO::getId, FcSubjectDTO::getSubjectName));

        for (FcAccbookVoucherVO vo: target) {
            List<FcAccbookDtl> bookDtls = vo.getBookDtls();
            bookDtls.forEach(a ->{
                String account = a.getAccount();
                String subjectId = accountAndSubjectIdMap.get(account);
                a.setSubjectId(subjectId);
                a.setSubjectName(subjectNameMap.get(subjectId));
            });
        }
        /*entity.setAccName(fcAccDTO.getAccName());
        entity.setSubjectId(fcAccDTO.getSubjectId());
        entity.setSubjectName(fcAccDTO.getSubjectName());
        entity.setCorpId(fcAccDTO.getCorpId());
        entity.setBalanceWay(fcAccDTO.getBalanceWay());
        entity.setCurId(fcAccDTO.getCurId());
        entity.setCurName(fcAccDTO.getCurName());
        entity.setNetId(fcAccDTO.getNetId());*/
    }

    /**
     * 功能介绍 : 返回日志操作类型。<br>
     * <b>日期：</b>2017年10月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public String getMsgType() {
        return FinacialConstant.FINACIAL_MSGTYPE_N;
    }
    /**
     * 功能介绍 : 更新凭证时复制新录入的数据到原凭证对象中，避免session不一致。<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private FcAccbookVoucherVO copyNewAccbook2OriAccbook(FcAccbookVoucherVO oriVoucher, FcAccbookVoucherVO voucher) {
        FcAccbook oriAccbook = oriVoucher.getAccbook();
        FcAccbook accbook = voucher.getAccbook();
        FcAccbookExt oriBookExt = oriVoucher.getBookExt();
        FcAccbookExt bookExt = voucher.getBookExt();
        oriAccbook.setBusRaceId(accbook.getBusRaceId());
        oriAccbook.setRmk(accbook.getRmk());
        oriAccbook.setInterestStartDate(accbook.getInterestStartDate());
        oriAccbook.setTallyDate(accbook.getTallyDate());
        oriAccbook.setAffixAmount(accbook.getAffixAmount());
        oriAccbook.setEmergencySign(accbook.getEmergencySign());
        oriAccbook.setVoucherState(accbook.getVoucherState());
        oriAccbook.setVoucherType(accbook.getVoucherType());
        oriAccbook.setChargedSign(accbook.getChargedSign());
        oriAccbook.setCurCode(accbook.getCurCode());
        oriAccbook.setOrigVoucherNo(accbook.getOrigVoucherNo());
        oriVoucher.setBookDtls(copyNewDtl2OriDtl(oriVoucher, voucher));
        if (null != bookExt) {
            oriBookExt.setExternalAcc(bookExt.getExternalAcc());
            oriBookExt.setExternalCorp(bookExt.getExternalCorp());
            oriBookExt.setExternalBank(bookExt.getExternalBank());
            oriBookExt.setExternalType(bookExt.getExternalType());
            oriBookExt.setTypeSign(bookExt.getTypeSign());
        }
        return oriVoucher;
    }
    /**
     * 功能介绍 : 更新时，删除原纪录中多余的分录。<br>
     * <b>日期：</b>2017年6月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private List<FcAccbookDtl> copyNewDtl2OriDtl(FcAccbookVoucherVO oriVoucher, FcAccbookVoucherVO voucher) {
        FcAccbook fcAccbook = voucher.getAccbook();
        List<FcAccbookDtl> dtls = new ArrayList<FcAccbookDtl>();
        List<FcAccbookDtl> delDtls = new ArrayList<FcAccbookDtl>();
        List<FcAccbookDtl> oriDtls = oriVoucher.getBookDtls();//原分录的明细
        List<FcAccbookDtl> bookDtls = voucher.getBookDtls();//最新明细
        int oriIndex = oriDtls.size();//
        int index = bookDtls.size();//
        for (int i = 0; i < index; i++) {
            FcAccbookDtl dtl = bookDtls.get(i);
            dtl.setLineNo(i + 1);//行号
            dtl.setBookId(fcAccbook.getId());//流水ID
            if (i < oriIndex) {//原明细直接覆盖
                FcAccbookDtl oriDtl = oriDtls.get(i);
                try {
                    BeanUtils.copyProperties(dtl, oriDtl, "id", "tenantId");//原分录的ID保留，避免ID丢失新增，租户也保留
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dtls.add(oriDtl);
            } else {//新增的明细
                dtls.add(dtl);
            }
        }
        if (index < oriIndex) {//删除原凭证中多余的分录
            for (int i = index; i < oriIndex; i++) {
                delDtls.add(oriDtls.get(i));
                //bookDtlservice.deleteEntity(oriDtls.get(i));
                bookDtlservice.delete(oriDtls.get(i).getId());
            }
        }
        return dtls;
    }
    /**
     * 功能介绍 : 账户联机余额检查。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected Map<String, FcOnlineLedgerDTO> processAccountBalance(VoucherWrapper voucher) {
        //OverdraftValidator overdraftValidator = new OverdraftValidator();
        return overdraftValidator.processVoucherBalance(voucher);
    }

    /**
     * 功能介绍 : 账户联机余额检查。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected Map<String, FcOnlineLedgerDTO> checkProcessAccountBalance(VoucherWrapper voucher) {
        //OverdraftValidator overdraftValidator = new OverdraftValidator();
        return overdraftValidator.checkProcessVoucherBalance(voucher);
    }
    /**
     * 获取联机余额
     *
     * @param voucher
     * @return
     */
    protected List<FcOnlineLedgerDTO> listFcOnlineLedger(VoucherWrapper voucher) {
        //OverdraftValidator overdraftValidator = new OverdraftValidator();
        return overdraftValidator.listFcOnlineLedger(voucher);
    }
    /**
     * 功能介绍 : 凭证数据校验。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void validateVoucher(FcAccbookVoucherVO voucher) {
        //BookBaseValidator validate = new BookBaseValidator();
        bookBaseValidator.baseValidate(voucher);
    }

    /**
     * 功能介绍 : 凭证数据校验。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void checkValidateVoucher(FcAccbookVoucherVO voucher) {
        //BookBaseValidator validate = new BookBaseValidator();
        bookBaseValidator.checkBaseValidate(voucher);
    }
    /**
     * 功能介绍 ： 获取流水号。<br>
     * <b>日期：</b>2017年6月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected String getAccbookOrderNo(String netCode) {
        return SequenceSingleton.getSequence(SequenceEnum.ORDER_NO, netCode);
    }

    /**
     * 功能介绍 ： 获取凭证号。<br>
     * <b>日期：</b>2017年6月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected String getAccbookVouvherNo(String netCode, Date tallyDate, String curCode) {
        //默认规则为网点+币别+6位系统年月
        //根据参数返回生成凭证号规则
        String prefix = netCode + curCode;
        SequenceEnum sequenceEnum = SequenceEnum.VOUCHER_NO;//按照系统日期
        boolean voucherNoFlag = false;//参数默认为按照系统日期生成
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "voucherNoRule");//取记账凭证号生成规则
        if (null != configDTO) {
            String voucherNoRule = configDTO.getValue();
            if (EmptyUtils.isNotEmpty(voucherNoRule) && "0".equals(voucherNoRule)) {//生成规则为按照记账日期
                voucherNoFlag = true;
            }
        }
        if (voucherNoFlag) {
            //生成规则为网点+币别+6位记账年月
            sequenceEnum = SequenceEnum.VOUCHER_NO_TALLY;//按照记账日期
            String tallyDate1 = DateUtils.formatDate(tallyDate, "yyyyMM");
            prefix = netCode + curCode + tallyDate1;//凭证号生成规则
        }
        String voucherNo = SequenceSingleton.getSequenceBySpecifyLockNoCache(sequenceEnum, prefix);
        logger.info("------------凭证号为:" + voucherNo);
        return voucherNo;
    }


    public String getSysBatchInstWay() {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "voucherNoRule");
        return configDTO.getValue();
    }
    /**
     * 作废前校验凭证状态
     *
     * @param pageType
     * @param id
     */
    public void validVoucher(String pageType, String id) {
        if (EmptyUtils.isEmpty(id)) {
            return;
        }
        FcAccbook accbook = fcAccbookDao.getAccbookById(id);
        if (EmptyUtils.isEmpty(accbook)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
        }
        String voucherStatus = accbook.getVoucherState();
        //检查凭证状态，非暂存、打回、待复核状态不允许修改保存或提交
        if (FinacialConstant.PAGE_EDIT.equals(pageType)) {//作废修改
            FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_BACK.get(),
                    FinaBookConstant.VoucherStatus.STATUS_TEMP.get(), FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get()});
        } else if (FinacialConstant.PAGE_CHECK.equals(pageType)) {//作废复核
            FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get()});
        }
    }
    /**
     * 功能介绍 : 初始化账务数据。<br>
     * <b>日期：</b>2017年6月5日<br>
     *
     * @param accbook
     * @param voucherStatus
     * @version 1.0
     * @author liyh
     */
    protected void initAccbook(FcAccbook accbook, String voucherStatus) {
        accbook.setEnrolTime(DateUtils.getDate());// 记账时间
        accbook.setRushSign(FinacialConstant.RUSH_SIGN_NO);// 未冲账
        accbook.setRedblueSign(FinacialConstant.RB_SIGN_B);// 蓝
        //设置手工记账类型
        accbook.setGenerateMode(FinaBookConstant.getGenerateModeByOriType(accbook.getBusOriType()));
        accbook.setVoucherState(voucherStatus);//凭证状态
        accbook.setChargedSign(FinacialConstant.CHARGED_SIGN_NO);
        accbook.setCheckTime(null);
        accbook.setCheckerCode(null);
    }
    /**
     * 功能介绍 : 凭证数据保存前校验。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private VoucherWrapper preVoucherSubmit(VoucherWrapper voucher) {
        voucher.setOpType(OperateType.SUBMIT);//记账提交
        String busGlobalId = voucher.getBusGlobalId();
        if (StringUtils.isBlank(busGlobalId)) {
            busGlobalId = FinacialUtils.createGlobalId();//生成全局ID
            voucher.setBusGlobalId(busGlobalId);
        }
        //设置需要更新旧凭证记录
        processOriVoucher(voucher);
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            voucherVO.setOpType(OperateType.SUBMIT);//记账提交
            //FIXME 判断是否新增全局ID
            accbook.setBusGlobalId(voucher.getBusGlobalId());//生成全局ID
            initAccbook(accbook, FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get());//待复核状态
            if(StringUtils.isNotBlank(voucher.getUserCode())){
                if(accbook.getBusOriType()!=FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.get()){
                    accbook.setOperatorCode(voucher.getUserCode());// 记账员
                }
            }
            //校验凭证数据有效性
            validateVoucher(voucherVO);
        }
        for (FcAccbookVoucherVO oriAccbook : voucher.getOriTarget()) {
            oriAccbook.setOpType(voucher.getOpType());
        }
        return voucher;
    }

    /**
     * 功能介绍 : 凭证数据保存前校验。<br>
     * <b>日期：</b>2017年6月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private VoucherWrapper checkPreVoucherSubmit(VoucherWrapper voucher) {
        voucher.setOpType(OperateType.SUBMIT);//记账提交
        String busGlobalId = voucher.getBusGlobalId();
        if (StringUtils.isBlank(busGlobalId)) {
            busGlobalId = FinacialUtils.createGlobalId();//生成全局ID
            voucher.setBusGlobalId(busGlobalId);
        }
        //设置需要更新旧凭证记录
        checkProcessOriVoucher(voucher);
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            voucherVO.setOpType(OperateType.SUBMIT);//记账提交
            //FIXME 判断是否新增全局ID
            accbook.setBusGlobalId(voucher.getBusGlobalId());//生成全局ID
            initAccbook(accbook, FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get());//待复核状态
            if(StringUtils.isNotBlank(voucher.getUserCode())){
                if(accbook.getBusOriType()!=FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.get()){
                    accbook.setOperatorCode(voucher.getUserCode());// 记账员
                }
            }
            //校验凭证数据有效性
            checkValidateVoucher(voucherVO);
        }
        for (FcAccbookVoucherVO oriAccbook : voucher.getOriTarget()) {
            oriAccbook.setOpType(voucher.getOpType());
        }
        return voucher;
    }
    /**
     * 功能介绍 : 记账提交凭证对象。<br>
     * <b>日期：</b>2017年5月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void submitFcAccbook(VoucherWrapper voucher) {
        //1、提交前数据校验
        preVoucherSubmit(voucher);
        //2、数据准备提交数据
        processVoucher(voucher); //凭证保存 & 更新账户联机余额
        completeEditNotificationInfo(voucher);//改账消息处理
        completeCheckNotificationInfo(voucher);//复核消息处理

        //如果自动复核，则复核凭证通过
        if (voucher.isAutoCheck()) {
            //批量计息-手工复核时，凭证是自动复核，但是无需额外设置复核人员，默认复核人员就是计息手工复核的人员
            if ((StringUtils.isBlank(voucher.getCheckUser()) || StringUtils.equals(voucher.getCheckUser(),voucher.getUserCode()))&&isBatchInstAutoCheck()) {
                //获取自动复核员用户
                if (StringUtils.isBlank(voucher.getCheckUser())){
                    setCheckUser(voucher);
                }
                if (StringUtils.isBlank(voucher.getUserCode())){
                    setAutoChecker(voucher.getTarget().get(0).getAccbook(), voucher);
                }

            }
            if (StringUtils.isBlank(voucher.getCheckUser()) || StringUtils.equals(voucher.getCheckUser(),voucher.getUserCode())) {
                //获取自动复核员用户
                setCheckUser(voucher);
//                setAutoChecker(voucher.getTarget().get(0).getAccbook(), voucher);
            }
            checkFcAccbook(voucher); //里面设置 voucherNo，还有指令操作
        } else {
            sendCheckNotificationInfo(voucher); //发送消息
        }
    }

    /**
     * 功能介绍 : 记账提交凭证对象。<br>
     * <b>日期：</b>2017年5月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkSubmitFcAccbook(VoucherWrapper voucher) {
        //1、提交前数据校验
    	checkPreVoucherSubmit(voucher);
        //2、数据准备提交数据
    	checkProcessVoucher(voucher); //凭证保存 & 更新账户联机余额
        //如果自动复核，则复核凭证通过
        if (voucher.isAutoCheck()) {
            //批量计息-手工复核时，凭证是自动复核，但是无需额外设置复核人员，默认复核人员就是计息手工复核的人员
            if ((StringUtils.isBlank(voucher.getCheckUser()) || StringUtils.equals(voucher.getCheckUser(),voucher.getUserCode()))&&isBatchInstAutoCheck()) {
                //获取自动复核员用户
                if (StringUtils.isBlank(voucher.getCheckUser())){
                    setCheckUser(voucher);
                }
                if (StringUtils.isBlank(voucher.getUserCode())){
                    setAutoChecker(voucher.getTarget().get(0).getAccbook(), voucher);
                }

            }
            if (StringUtils.isBlank(voucher.getCheckUser()) || StringUtils.equals(voucher.getCheckUser(),voucher.getUserCode())) {
                //获取自动复核员用户
                setCheckUser(voucher);
            }
            checkCheckFcAccbook(voucher); //里面设置 voucherNo，还有指令操作
        }
    }

    private boolean isBatchInstAutoCheck() {
        ConfigDTO config = configService.getConfig("ist", "batchInstAutoCheck");
        return config.toInteger().intValue() == 1;
    }
    /**
     * 设置自动复核柜员
     * @param accbook
     * @param voucher
     */
    private void setAutoChecker(FcAccbook accbook, VoucherWrapper voucher){
        if (EmptyUtils.isEmpty(accbook)){
            return;
        }
        accbook.setCheckerCode(voucher.getUserCode());
        if (StringUtils.equals(accbook.getCheckerCode(), voucher.getUserCode())) {
            boolean innerCarryFlag = EmptyUtils.isEmpty(voucher.getInnerCarryFlag()) ? false : voucher.getInnerCarryFlag();
            accbook.setCheckerCode(getAutoCheckUser(voucher.getNetId(), innerCarryFlag));
        }
    }
    /**
     * 现金流项目设置
     *
     * @param voucher
     */
    public void setCashFlow(VoucherWrapper voucher) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
//			非资金池记账
            if (!FinaBookConstant.GenerateMode.BANK_POOL.get().equals(accbook.getGenerateMode())) {
                List<FcAccbookDtl> fcAccbookDtls = voucherVO.getBookDtls();
                for (FcAccbookDtl fcAccbookDtl : fcAccbookDtls) {//遍历记账明细
//					获取账号
                    com.bytter.system.api.dto.FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(fcAccbookDtl.getAccount());
                    if (null != fcAccDTO && StringUtils.isNotEmpty(fcAccDTO.getSubjectId())) {
                        FcVoucherCashVo fcVoucherCashVo;
                        if (null != fcAccbookDtl.getDebitMoney() && fcAccbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
                            //						通过账号科目查询现金流项目
                            fcVoucherCashVo = fcVoucherCashService.getItemBySubject(fcAccDTO.getSubjectId(), null);
                            if (null != fcVoucherCashVo && StringUtils.isNotEmpty(fcVoucherCashVo.getDebitItem())) {
                                fcAccbookDtl.setCashFlux(fcVoucherCashVo.getDebitItem());
                            }
                        } else if (null != fcAccbookDtl.getCreditMoney() && fcAccbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                            //						通过账号科目查询现金流项目
                            fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, fcAccDTO.getSubjectId());
                            if (null != fcVoucherCashVo && StringUtils.isNotEmpty(fcVoucherCashVo.getCreditItem())) {
                                fcAccbookDtl.setCashFlux(fcVoucherCashVo.getCreditItem());
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 : 复核前查询凭证数据。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void preCheckVoucher(VoucherWrapper voucher) {
        voucher.setOpType(OperateType.CHECK);//记账复核
        if (EmptyUtils.isEmptyList(voucher.getTarget())) {
            voucher.addTarget(new FcAccbookVoucherVO());
        }
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            getFcAccbookVoucherVOById(voucher.getBusGlobalId(), voucherVO);
            voucherVO.setOpType(OperateType.CHECK);//记账复核通过
            initCheckAccbook(voucherVO.getAccbook(), FinaBookConstant.VoucherStatus.STATUS_CHECK.get());//复核状态
            voucherVO.getAccbook().setCheckerCode(voucher.getUserCode());// 复核员
        }
    }

    /**
     * 功能介绍 : 复核前查询凭证数据。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void checkPreCheckVoucher(VoucherWrapper voucher) {
        voucher.setOpType(OperateType.CHECK);//记账复核
        if (EmptyUtils.isEmptyList(voucher.getTarget())) {
            voucher.addTarget(new FcAccbookVoucherVO());
        }
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
        	checkGetCheckFcAccbookVoucherVOById(voucher.getBusGlobalId(), voucherVO);
            voucherVO.setOpType(OperateType.CHECK);//记账复核通过
            initCheckAccbook(voucherVO.getAccbook(), FinaBookConstant.VoucherStatus.STATUS_CHECK.get());//复核状态
            voucherVO.getAccbook().setCheckerCode(voucher.getUserCode());// 复核员
        }
    }
    /**
     * 功能介绍 : 初始化复核账务数据。<br>
     * <b>日期：</b>2017年6月5日<br>
     *
     * @param accbook
     * @param voucherStatus
     * @version 1.0
     * @author liyh
     */
    protected void initCheckAccbook(FcAccbook accbook, String voucherStatus) {
        accbook.setCheckTime(DateUtils.getDate());// 复核时间
        accbook.setVoucherState(voucherStatus);//凭证状态
    }
    /**
     * 功能介绍 : 凭证记账复核。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkFcAccbook(VoucherWrapper voucher) {
        //1、复核前查询数据
        preCheckVoucher(voucher);
        //2、提交前数据校验 并保存相关数据
        processVoucherCheck(voucher); //里面有生成voucherNo逻辑，也有指令操作
        completeCheckNotificationInfo(voucher); //通知提醒
    }

    /**
     * 功能介绍 : 凭证记账复核。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkCheckFcAccbook(VoucherWrapper voucher) {
        //1、复核前查询数据
    	checkPreCheckVoucher(voucher);//TODO
        //2、提交前数据校验 并保存相关数据
    	checkProcessVoucherCheck(voucher); //里面有生成voucherNo逻辑，也有指令操作
    }
    /**
     * 功能介绍 : 复核过程处理。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void processVoucherCheck(VoucherWrapper voucher) {
        //1、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
        Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
        //2、数据保存
        // FIXME 拆分出了获取更新凭证号方法（单独做了事务提交，每获取一个就会更新到数据库）
        checkVoucher(voucher, ledgerMap); //里面生成voucherNo
        //处理联行往来信息
        //指令
        unionAccbookProcessor.processUnionAccbook(voucher);
        /* new BankDirectLinkProcessor(null).processBankDirectLink(voucher);
         */
        /**
         * 手续费中有个同行和异地的判断来源于指令处理，如果不产生指令，则手续费中的同行异地不处理
         * 如果存在手续费，先处理手续费
         */
        chargeProcessor.processResult(voucher, ledgerMap); //有调用核算进行数据保存(费用累计)
        bankDirectProcessor.processResult(voucher); //根据付款账号和外单位信息产生指令
    }

    /**
     * 功能介绍 : 复核过程处理。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void checkProcessVoucherCheck(VoucherWrapper voucher) {
        //1、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
        Map<String, FcOnlineLedgerDTO> ledgerMap = checkProcessAccountBalance(voucher);
        //2、数据保存
        // FIXME 拆分出了获取更新凭证号方法（单独做了事务提交，每获取一个就会更新到数据库）
        //没有抛异常,不校验
        //checkVoucher(voucher, ledgerMap); //里面生成voucherNo
        //处理联行往来信息
        //指令
        //没有抛异常,不校验
//        unionAccbookProcessor.processUnionAccbook(voucher);
        /**
	              * 手续费中有个同行和异地的判断来源于指令处理，如果不产生指令，则手续费中的同行异地不处理
	           * 如果存在手续费，先处理手续费
         */
        //前置校验不保存数据
        //chargeProcessor.processResult(voucher, ledgerMap); //有调用核算进行数据保存(费用累计)
        //bankDirectProcessor.processResult(voucher); //根据付款账号和外单位信息产生指令chargeProcessor.processResult(voucher, ledgerMap); //有调用核算进行数据保存(费用累计)
        bankDirectProcessor.checkProcessResult(voucher); //根据付款账号和外单位信息产生指令
    }
    /**
     * 功能介绍 : 处理凭证复核通过账务和余额更新。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void checkVoucher(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            if (voucher.getProcessType() == ProcessType.PROCESS_RUSH) {// 冲账更新冲账标识R
                accbook.setRushSign(FinacialConstant.RB_SIGN_R);
            }

            //在结算外围新增了生成 voucherNo 逻辑
            if (StringUtils.isBlank(accbook.getVoucherNo())){
                /*
                 * 整理要更新的数据 1、更新余额 2、更新账务表
                 */
                String account = voucherVO.getBookDtls().get(0).getAccount();//账号
                FcOnlineLedgerDTO ledgerDTO = ledgerMap.get(account);
                String netCode = ledgerDTO.getNetCode();//网点CODE
                String curCode = ledgerDTO.getCurCode();//币别CODE
                //String tallyDate1 = DateUtils.formatDate(accbook.getTallyDate(), "yyyyMM");
                //String voucherPrefix = netCode + curCode + tallyDate1;//凭证号生成规则
                logger.info("-------凭证类型(" + accbook.getGenerateMode() + ")-----凭证流水号为" + accbook.getOrderNo() +"-----凭证流状态为" + accbook.getVoucherState()+ "获取凭证号：------------");
                accbook.setVoucherNo(getAccbookVouvherNo(netCode, accbook.getTallyDate(), curCode));// FIXME 网点CODE + 币别  待确认
//            if (StringUtils.isNotBlank(voucher.getUserCode())  && StringUtils.isBlank(accbook.getOperatorCode()) ){
//                accbook.setOperatorCode(voucher.getUserCode());
//            }
            }

            if (StringUtils.isNotBlank(voucher.getCheckUser())){
                accbook.setCheckerCode(voucher.getCheckUser());
            }
            accbook.setUpdateTime( new Date());
            fcAccbookDao.merge(accbook);
            List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
            int lineNo = 1;// 判断凭证分录是否和原凭证分录数量一致
            for (FcAccbookDtl accbookDtl : bookDtls) {
                FcOnlineLedgerDTO ledger = ledgerMap.get(accbookDtl.getAccount());
                accbookDtl.setBookId(accbook.getId());
                //accbookDtl.setLineNo(lineNo++);
                // 处理明细余额，明细上的余额不显示，复核时再写入
//				accbookDtl.setBalance(ledger.getCheckBalance());
                // FIXME 账户余额更新待处理
                bookDtlservice.merge(accbookDtl);
            }
            //记录凭证操作日志表
            bookMsgservice.saveBookMsg(accbook.getId(), getMsgType(), voucherVO.getOpType(), null, voucher.getUserCode());
        }
        // 更新账户联机余额 在核算模块
        fcOnlineLedgerService.updateCheckBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()));
        getSession().flush();
    }
    /**
     * 功能介绍 : 复核前查询凭证数据。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void preBackVoucher(VoucherWrapper voucher) {
        voucher.setOpType(OperateType.BACK);//记账打回
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbookExt bookExt;
            getFcAccbookVoucherVOById(voucher.getBusGlobalId(), voucherVO);
            if (null == voucherVO.getBookExt()) {
                bookExt = new FcAccbookExt();
                voucherVO.setBookExt(bookExt);
                bookExt.setBookId(voucherVO.getAccbook().getId());
            } else {
                bookExt = voucherVO.getBookExt();
                voucherVO.getBookExt().setBackReason(bookExt.getBackReason());
            }
            voucherVO.setOpType(OperateType.BACK);//记账打回
            voucherVO.getAccbook().setVoucherState(FinaBookConstant.VoucherStatus.STATUS_BACK.get());//打回状态
        }
    }
    /**
     * 功能介绍 : 凭证复核打回。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void backFcAccbook(VoucherWrapper voucher) {
        //1、打回前查询数据
        preBackVoucher(voucher);
        //2、提交前数据校验 并 更新数据
        processVoucherBack(voucher);
        completeCheckNotificationInfo(voucher);
        sendBackNotificationInfo(voucher);//消息打回提醒
    }
    /**
     * 功能介绍 : 打回过程处理。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void processVoucherBack(VoucherWrapper voucher) {
        //		获取初始联机余额信息
        List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
        //1、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
        Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
        //2、数据更新
        backVoucher(voucher, ledgerMap, origOnlineLedgerDTOList);
    }
    /**
     * 功能介绍 : 处理凭证复核打回账务和余额更新。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private synchronized void backVoucher(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap, List<FcOnlineLedgerDTO> origOnlineLedgerDTOList) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            FcAccbook accbook = voucherVO.getAccbook();
            /*
             * 整理要更新的数据 1、更新余额 2、更新账务表
             */
            //fcAccbookDao.flush();//刷新session提交
            fcAccbookDao.merge(accbook);
            List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
            int lineNo = 1;// 判断凭证分录是否和原凭证分录数量一致
            for (FcAccbookDtl accbookDtl : bookDtls) {
                accbookDtl.setBookId(accbook.getId());
                //accbookDtl.setLineNo(lineNo++);
                // 处理明细余额，明细上的余额不显示，复核时再写入
                // FIXME 账户余额更新待处理
                bookDtlservice.merge(accbookDtl);
            }
            FcAccbookExt bookExt = voucherVO.getBookExt();
            String backReason = "";
            if (null != bookExt) {
                bookExt.setBookId(accbook.getId());
                //bookExtservice.saveOrUpdate(bookExt);
                bookExtservice.merge(bookExt);
                backReason = bookExt.getBackReason();
            }
            //打回信息写入凭证日志表
            bookMsgservice.saveBookMsg(accbook.getId(), getMsgType(), voucherVO.getOpType(), backReason, voucher.getUserCode());
        }
        // 更新账户联机余额
        fcOnlineLedgerService.updateOnlineBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()), origOnlineLedgerDTOList);
    }
    /**
     * 功能介绍 : 删除前查询凭证数据。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void preDeleteVoucher(VoucherWrapper voucher) {
        voucher.setOpType(OperateType.DELETE);
        if (EmptyUtils.isEmptyList(voucher.getTarget())) {
            FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
            voucher.addTarget(voucherVO);
        }
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            getFcAccbookVoucherVOById(voucher.getBusGlobalId(), voucherVO);
            voucherVO.setOpType(OperateType.DELETE);//记账抹账
        }
    }
    /**
     * 功能介绍 : 凭证抹账。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteFcAccbook(VoucherWrapper voucher) {
        //1、抹账前查询数据
        preDeleteVoucher(voucher);
        //2、提交前数据校验
        processVoucherDelete(voucher);
        completeEditNotificationInfo(voucher);
        completeCheckNotificationInfo(voucher);
    }
    /**
     * 功能介绍 : 删除过程处理。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected void processVoucherDelete(VoucherWrapper voucher) {
        //		获取初始联机余额信息
        List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
        //1、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
        Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
        //2、数据更新
        deleteVoucher(voucher, ledgerMap, origOnlineLedgerDTOList);
    }
    /**
     * 功能介绍 : 处理凭证删除账务和余额还原。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private synchronized void deleteVoucher(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap, List<FcOnlineLedgerDTO> origOnlineLedgerDTOList) {
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
            deleteBookVoucher(voucherVO, voucher.getUserCode());
        }
        // 更新账户联机余额
        fcOnlineLedgerService.updateOnlineBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()), origOnlineLedgerDTOList);
    }
    /**
     * 功能介绍 : 删除账务和账务明细。<br>
     * <b>日期：</b>2017年6月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void deleteBookVoucher(FcAccbookVoucherVO voucherVO, String userId) {
        FcAccbook accbook = voucherVO.getAccbook();
        /*
         * 整理要更新的数据 1、 账务历史备份 2、更新账务表  3、更新余额
         */
        //保存抹账记录 写入历史记录表
        backupFcAccbook(voucherVO, FinaBookConstant.VoucherOpType.OpType_D.get());
        delete(accbook.getId());
        List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
        int lineNo = 1;// 判断凭证分录是否和原凭证分录数量一致
        for (FcAccbookDtl accbookDtl : bookDtls) {
            accbookDtl.setBookId(accbook.getId());
            accbookDtl.setLineNo(lineNo++);
            // 处理明细余额，明细上的余额不显示，复核时再写入
            bookDtlservice.delete(accbookDtl.getId());
        }
        //是否删除账务扩展信息
        FcAccbookExt bookExt = voucherVO.getBookExt();
        if (null != bookExt && !StringUtils.isEmpty(bookExt.getId())) {
            bookExtservice.delete(bookExt.getId());//删除账务扩展信息
        }
        //账务删除日志
        bookMsgservice.saveBookMsg(accbook.getId(), getMsgType(), voucherVO.getOpType(), null, userId);
        getEntityDao().flush();
    }
    /**
     * 功能介绍 : 账务数据备份。<br>
     * <b>日期：</b>2017年6月15日<br>
     *
     * @param operateType 作废B：  改账：M  抹账：D
     * @version 1.0
     * @author liyh
     */
    protected void backupFcAccbook(FcAccbookVoucherVO voucherVO, String operateType) {
        FcAccbook book = voucherVO.getAccbook();
        List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
        FcAccbookExt bookExt = voucherVO.getBookExt();
        FcAccbookHis accbookHis = new FcAccbookHis();
        BeanUtils.copyProperties(book, accbookHis);
        accbookHis.setId(null);
        accbookHis.setBookId(book.getId());
        accbookHis.setOperateType(operateType);
        bookHisservice.save(accbookHis);
        List<FcAccbookDtlHis> accbookDtls = new ArrayList<FcAccbookDtlHis>();
        for (FcAccbookDtl bookDtl : bookDtls) {
            FcAccbookDtlHis accbookDtlHis = new FcAccbookDtlHis();
            BeanUtils.copyProperties(bookDtl, accbookDtlHis);
            accbookDtlHis.setId(null);
            accbookDtlHis.setBookId(book.getId());
            accbookDtls.add(accbookDtlHis);
        }
        bookDtlHisservice.batchSave(accbookDtls);
        if (null != bookExt) {
            FcAccbookExt accbookExt = bookExt;
            if (StringUtils.isNotEmpty(accbookExt.getId())) {
                FcAccbookExtHis accbookExtHis = new FcAccbookExtHis();
                BeanUtils.copyProperties(accbookExt, accbookExtHis);
                accbookExtHis.setId(null);
                bookExtHisservice.save(accbookExtHis);
            }
        }
    }
    /**
     * 功能介绍 : 根据账务全局ID获取账务数据。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public VoucherWrapperDTO getVoucherByBusGlobalId(String busGlobalId) {
        VoucherWrapperDTO voucherDTO = new VoucherWrapperDTO();
        FcAccbookVoucherDTO voucher = new FcAccbookVoucherDTO();
        List<FcAccbook> accbooks = fcAccbookDao.getBooksByBusGlobalId(busGlobalId);
        //判断是否日结了
        if (null == accbooks || accbooks.isEmpty()) {
            //已日结表查询
            return getBlnedVoucher(busGlobalId);
        }
        FcAccbook accbook = accbooks.get(0);
        FcAccbookDTO accbookDTO = new FcAccbookDTO();
        BeanUtils.copyProperties(accbook, accbookDTO);
        FcVoucherTypeDTO voucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(accbook.getVoucherType());
        accbookDTO.setVoucherName(voucherTypeDTO.getVoucherName());//凭证种类名称
        voucher.setAccbook(accbookDTO);//账务主表数据
        String id = accbook.getId();
        List<FcAccbookDtl> accbookDtls = getFcAccBookDtlByBooKId(id);
        voucher.setBookDtls(BeanUtils.copyListProperties(accbookDtls, FcAccbookDtlDTO.class));//账务明细
        FcAccbookExt accbookExt = bookExtservice.getBookExtByBookId(id);
        FcAccbookExtDTO accbookExtDTO = new FcAccbookExtDTO();
        if (!EmptyUtils.isEmpty(accbookExt)) {
            RegionDTO regionDTO = regionService.getById(accbookExt.getExternalCity());
            if (Objects.nonNull(regionDTO)) {
                accbookExt.setCity(regionDTO.getName());
                accbookExt.setProv(regionDTO.getProvName());
            }
            BeanUtils.copyProperties(accbookExt, accbookExtDTO);
            voucher.setBookExt(accbookExtDTO);//账务外单位其他信息
        }
        voucherDTO.setBusGlobalId(busGlobalId);//设置全局ID
        voucherDTO.addTarget(voucher);
        return voucherDTO;
    }
    /**
     * 功能介绍 : 根据账务全局ID获取已日结账务数据。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public VoucherWrapperDTO getBlnedVoucher(String busGlobalId) {
        VoucherWrapperDTO voucherDTO = new VoucherWrapperDTO();
        FcAccbookVoucherDTO voucher = new FcAccbookVoucherDTO();
        List<FcAccbookBlned> accbooks = accbookBlnedDao.getBooksByBusGlobalId(busGlobalId);
        //判断是否日结了
        if (null == accbooks || accbooks.isEmpty()) {
            //已日结表查询
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_NOTEXIST);
        }
        FcAccbookBlned accbook = accbooks.get(0);
        FcAccbookDTO accbookDTO = new FcAccbookDTO();
        BeanUtils.copyProperties(accbook, accbookDTO);
        voucher.setAccbook(accbookDTO);//账务主表数据
        String id = accbook.getId();
        List<FcAccbookBlnedDtl> accbookDtls = bookBlnedDtlservice.getAccbookDtlsByBookId(id);
        voucher.setBookDtls(BeanUtils.copyListProperties(accbookDtls, FcAccbookDtlDTO.class));//账务明细
        FcAccbookBlnedExt accbookExt = bookBlnedExtservice.getBookBlnedExtByBookId(id);
        FcAccbookExtDTO accbookExtDTO = new FcAccbookExtDTO();
        BeanUtils.copyProperties(accbookExt, accbookExtDTO);
        voucher.setBookExt(accbookExtDTO);//账务外单位其他信息
        voucherDTO.addTarget(voucher);
        voucherDTO.setFlag(FinacialConstant.OVER_SIGN_YES);//已日结标识
        return voucherDTO;
    }
    /**
     * 功能介绍 : 转换指令来源。<br>
     * <b>日期：</b>2017年11月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public String trans2VoucherFrom(String sysRaceCode) {
        String voucherFrom = "";
        if (FinaBookConstant.SysRaceCode.NB_UNIT_COMMON_PAYMENT_OUT.get().equals(sysRaceCode)) {//单位一般付款
            voucherFrom = BdlinkConstant.FROM_NIS_PAY;
        } else if (FinaBookConstant.SysRaceCode.NB_CNT_DOWN_PAYMENT.get().equals(sysRaceCode)) {//下拨
            voucherFrom = BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN;
        } else if (FinaBookConstant.SysRaceCode.NB_UNIT_ASK_PAYMENT.get().equals(sysRaceCode)) {//请款
            voucherFrom = BdlinkConstant.FROM_ALLOCATION_TYPE_ASK;
        } else if (FinaBookConstant.SysRaceCode.NB_CNT_POS_PAYMENT.get().equals(sysRaceCode)) {//头寸
            voucherFrom = BdlinkConstant.FROM_ALLOCATION_TYPE_POS;
        }
        return voucherFrom;
    }
    /**
     * 功能介绍 : 获取有权限的表内账号。<br>
     * <b>日期：</b>2017年11月4日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccPlusDTO> getTallyAccount(String userId, String permission, String resource) {
        List<String> accs = accGrantService.listByMultiVal(userId, resource, null,
                AccountGrantConstant.INNER_BANK_TYPE, permission);
        if (EmptyUtils.isEmptyList(accs)) {
            return new ArrayList<FcAccPlusDTO>();
        }
        List<FcAccPlusDTO> accDTOs = iFcAccService.getTallyAccTypeAccsDto(accs);//可记账内部账号
        return accDTOs;
    }
    /**
     * 功能介绍 : 作废或冲销复核通过还原原单据信息。<br>
     * <b>日期：</b>2018年12月4日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateBusinessData(VoucherWrapper voucher) {
        List<FcAccbookVoucherVO> accbooks = voucher.getTarget();
        if (EmptyUtils.isEmptyList(accbooks)) {
            return;
        }
        FcAccbookVoucherVO accbookVoucherVO = accbooks.get(0);
        FcAccbook accbook = accbookVoucherVO.getAccbook();
        if (null == accbook) {
            return;
        }
        Integer busOriType = accbook.getBusOriType();//记账业务类型
        String tableName = FinaBookConstant.getBusTableByOriType(busOriType);//获取业务记账表
        if (EmptyUtils.isEmpty(tableName)) {
            return;
        }
        String serId = "";
        List<FcAccbookDtl> bookDtls = accbookVoucherVO.getBookDtls();
        if (EmptyUtils.isEmptyList(bookDtls)) {
            return;
        }
        if (FinaBookConstant.BusOriType.NB_BANKDIRLINK_DEPOSIT.get().equals(busOriType)
                || FinaBookConstant.BusOriType.BIS_ACC_DTL_OTHER_RECEIVE.get().equals(busOriType)
                || FinaBookConstant.BusOriType.BIS_ACC_DTL_OTHER_PAY.get().equals(busOriType)) {
            serId = accbook.getId();
            int count = fcAccbookDao.updateChalkBusinessData(tableName, serId);
            if (count < 1) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
        } else if (FinaBookConstant.BusOriType.BIS_ACC_DTL_DOWN.get().equals(busOriType)) {
            serId = accbook.getId();
            int count = fcAccbookDao.updateBisAccDtlData(tableName, serId);
            if (count < 1) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
        } else {
            FcAccbookDtl bookDtl = bookDtls.get(0);
            serId = bookDtl.getOrigVoucherId();//记账业务ID
            if (EmptyUtils.isEmpty(serId)) {
                return;
            }
            fcAccbookDao.updateBusinessData(tableName, serId);//还原记账标识
        }
    }
    /**
     * 功能介绍 : 获取页面显示凭证，附带凭证相关信息。<br>
     */
    @Transactional(readOnly = true)
    public FcAccbookPageVO getPoolFcAccbookPageVOById(String id) {
        FcAccbookPageVO pageVO = new FcAccbookPageVO();
        FcAccbookVO accbookVO = fcAccbookDao.getAccbookVoById(id, true);
        if (EmptyUtils.isEmpty(accbookVO)) {//已日结
            return bookBlnedservice.getFcAccbookBlnedPageVOById(id, true);
        }
        pageVO.setAccbook(fcAccbookDao.getAccbookVoById(id, true));
        pageVO.setBookDtls(fcAccbookDao.getPageDtlsByBookId(id, true));
        //TODO后续加上账务外单位信息
        FcAccbookExt accbookExt = bookExtservice.getBookExtByBookId(id);
        FcAccbookExtVO extVO = new FcAccbookExtVO();
        if (null != accbookExt) {
            RegionDTO regionDTO = regionService.getById(accbookExt.getExternalCity());
            if (Objects.nonNull(regionDTO)) {
                accbookExt.setCity(regionDTO.getName());
                accbookExt.setProv(regionDTO.getProvName());
            }
            BeanUtils.copyProperties(accbookExt, extVO);
        }
        pageVO.setBookExt(extVO);
        return pageVO;
    }
    /**
     * 按照计息参数获取相关利息账号
     */
    public List<FcAccDTO> getInterestAcc(String curCode, String netId, String corpId, String subjectId, String busType, String bankCode, String user, String msg) {
        List<com.bytter.system.api.dto.FcAccDTO> dtos = null;
        Set<String> netIds = new HashSet<>();
        netIds.add(netId);
        String msgCorp = "";
        String bankName = "";
        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
        if (entrySign == 0) {//结算中心
            dtos = iFcAccService.getFcAccBySysSubjectIdFilterBankCode(subjectId, null, netId, netIds, curCode);
            msgCorp = corpService.get(netId).getName();
        } else {
            if ("provide".equals(busType) || "ab".equals(busType)) {
                dtos = innerAccountService.listAccountByBankParams(curCode, netId, netId, subjectId, bankCode, user);
                bankName = iInputBankInfoService.getByBankCode(bankCode).getBankTypeName();
            } else {
                dtos = innerAccountService.listAccountByParams(curCode, netId, corpId, subjectId);
            }
            msgCorp = corpService.get(corpId).getName();
        }
        if (dtos.size() < 1) {
            if (("provide".equals(busType) || "ab".equals(busType)) && entrySign == 1) {//单位或银行
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ACCOUNT_BY_BANKTYPE_ISNULL, msgCorp, msg, bankName);
            } else {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ACCOUNT_ISNULL, msgCorp, msg);
            }
        }
        // todo:数据转换
        return BeanUtils.copyListProperties(dtos, FcAccDTO.class);
    }
    /**
     * 反写客商信息
     */
    public void saveExternal(FcAccbookVoucherVO fcAccbook, String bankCode) {
        String externalSign = configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue();
        if ("0".equals(externalSign)) {//白名单：否
            if ("1".equals(fcAccbook.getBookExt().getExternalType())) {
                if (StringUtils.isNotEmpty(fcAccbook.getBookExt().getExternalBank())) {
                    ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
                    externalCorpDTO.setName(fcAccbook.getBookExt().getExternalCorp());//客商名称
                    externalCorpDTO.setCorpId(fcAccbook.getNetCode());
                    externalCorpDTO.setSourceSystem("FIN");//数据来源
                    externalCorpDTO.setStatus(1);
                    externalCorpDTO.setIsNative(Integer.valueOf(fcAccbook.getBookExt().getTypeSign()));
                    //客商账号
                    List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
                    ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
                    externalCorpBankAccDTO.setExternalAcc(fcAccbook.getBookExt().getExternalAcc());//客商银行账号
                    externalCorpBankAccDTO.setBank(fcAccbook.getBookExt().getExternalBank());//开户行名称
                    externalCorpBankAccDTO.setBankType(fcAccbook.getBookExt().getExternalBank());
                    externalCorpBankAccDTO.setBankSourceCode(bankCode);
                    externalCorpBankAccDTO.setIsDefaultAcc("0");//首选银行：1是 0否
                    externalCorpBankAccDTO.setAccProvince(fcAccbook.getBookExt().getExternalProv());
                    externalCorpBankAccDTO.setAccCity(fcAccbook.getBookExt().getExternalCity());
                    externalCorpBankAccList.add(externalCorpBankAccDTO);
                    externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
                    iExternalCorpService.saveIfNotExist(externalCorpDTO);
                }
            }
        }
    }
    /**
     * 根据凭证号得到账务信息
     *
     * @param voucherNo
     * @return
     */
    public FcAccbookVO getAccbookByVoucherNo(String voucherNo) {
        return fcAccbookDao.getAccbookByVoucherNo(voucherNo);
    }
    /**
     * 更新现金流项目
     *
     * @param fcAccbookVoucherVO
     * @param user
     */
    public void updateVoucherBookItem(FcAccbookVoucherVO fcAccbookVoucherVO, ICurrentUser user) {
        List<FcAccbookDtl> fcAccbookDtls = fcAccbookVoucherVO.getBookDtls();
        for (FcAccbookDtl fcAccbookDtl : fcAccbookDtls) {
            FcAccbookDtl fcAccbookDtl11 = fcAccbookDtlService.getFcAccbookDtlById(fcAccbookDtl.getId());
//			已日结
            if (null == fcAccbookDtl11) {
                bookBlnedDtlservice.updateCashFlux(fcAccbookDtl.getId(), fcAccbookDtl.getCashFlux());
                bookBlnedservice.updateChangeCashflux(fcAccbookDtl.getBookId());
            } else {
                fcAccbookDtlService.updateCashFlux(fcAccbookDtl.getId(), fcAccbookDtl.getCashFlux());
                fcAccbookDao.updateChangeCashflux(fcAccbookDtl11.getBookId());
            }
        }
    }
    /**
     * 导出分录明细
     *
     * @param request
     * @param response
     * @param list
     * @param title
     */
    public void export(HttpServletRequest request, HttpServletResponse response, List<FcAccbookPageVO> list, String title) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, title) + ".xls");
        try (OutputStream os = response.getOutputStream()) {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 无边框靠左对齐样式
            HSSFCellStyle leftNoBorderCellStyle = workbook.createCellStyle();
            leftNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            leftNoBorderCellStyle.setAlignment(HorizontalAlignment.LEFT);
            // 无边框居中对齐样式
            HSSFCellStyle centerNoBorderCellStyle = workbook.createCellStyle();
            centerNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerNoBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(title);
            // 标题
            setExportTitle(sheet, title, centerNoBorderCellStyle);
            //表头
            HSSFRow row1 = sheet.createRow(1);
            HSSFCell row1cell0 = row1.createCell(0);
            row1cell0.setCellStyle(centerCellStyle);
            row1cell0.setCellValue("记账日期");
            HSSFCell row1cell1 = row1.createCell(1);
            row1cell1.setCellStyle(centerCellStyle);
            row1cell1.setCellValue("起息日期");
            HSSFCell row1cell2 = row1.createCell(2);
            row1cell2.setCellStyle(centerCellStyle);
            row1cell2.setCellValue("摘要");
            HSSFCell row1cell3 = row1.createCell(3);
            row1cell3.setCellStyle(centerCellStyle);
            row1cell3.setCellValue("序号");
            HSSFCell row1cell4 = row1.createCell(4);
            row1cell4.setCellStyle(centerCellStyle);
            row1cell4.setCellValue("内部账号");
            HSSFCell row1cell5 = row1.createCell(5);
            row1cell5.setCellStyle(centerCellStyle);
            row1cell5.setCellValue("借方金额");
            HSSFCell row1cell6 = row1.createCell(6);
            row1cell6.setCellStyle(centerCellStyle);
            row1cell6.setCellValue("贷方金额");
            HSSFCell row1cell7 = row1.createCell(7);
            row1cell7.setCellStyle(centerCellStyle);
            row1cell7.setCellValue("业务种类");
            HSSFCell row1cell8 = row1.createCell(8);
            row1cell8.setCellStyle(centerCellStyle);
            row1cell8.setCellValue("凭证种类");
            HSSFCell row1cell9 = row1.createCell(9);
            row1cell9.setCellStyle(centerCellStyle);
            row1cell9.setCellValue("备注");
            HSSFCell row1cell10 = row1.createCell(10);
            row1cell10.setCellStyle(centerCellStyle);
            row1cell10.setCellValue("附件张数");
            HSSFCell row1cell11 = row1.createCell(11);
            row1cell11.setCellStyle(centerCellStyle);
            row1cell11.setCellValue("对方名称");
            HSSFCell row1cell12 = row1.createCell(12);
            row1cell12.setCellStyle(centerCellStyle);
            row1cell12.setCellValue("对方账号");
            HSSFCell row1cell13 = row1.createCell(13);
            row1cell13.setCellStyle(centerCellStyle);
            row1cell13.setCellValue("对方账号开户行");
            int i = 1;
            for (FcAccbookPageVO voucher : list) {
                for (int j = 0; j < voucher.getBookDtls().size(); j++) {
                    i++;
                    HSSFRow row = sheet.createRow(i);
                    //记账日期
                    HSSFCell rowCell0 = row.createCell(0);
                    rowCell0.setCellStyle(centerCellStyle);
                    rowCell0.setCellValue(DateUtils.format(voucher.getAccbook().getTallyDate(), "yyyy-MM-dd"));
                    //起息日期
                    HSSFCell rowCell1 = row.createCell(1);
                    rowCell1.setCellStyle(centerCellStyle);
                    rowCell1.setCellValue(DateUtils.format(voucher.getAccbook().getInterestStartDate(), "yyyy-MM-dd"));
                    //摘要
                    HSSFCell rowCell2 = row.createCell(2);
                    rowCell2.setCellStyle(centerCellStyle);
                    rowCell2.setCellValue(voucher.getBookDtls().get(j).getSummaryContent());
                    //序号
                    HSSFCell rowCell3 = row.createCell(3);
                    rowCell3.setCellStyle(centerCellStyle);
                    rowCell3.setCellValue(j + 1);
                    //内部账号
                    HSSFCell rowCell4 = row.createCell(4);
                    rowCell4.setCellStyle(centerCellStyle);
                    rowCell4.setCellValue(voucher.getBookDtls().get(j).getAccount());
                    //借方金额
                    HSSFCell rowCell5 = row.createCell(5);
                    rowCell5.setCellStyle(rightCellStyle);
                    rowCell5.setCellValue(NumberUtil.formatNumeric(voucher.getBookDtls().get(j).getDebitMoney()));
                    //贷方金额
                    HSSFCell rowCell6 = row.createCell(6);
                    rowCell6.setCellStyle(rightCellStyle);
                    rowCell6.setCellValue(NumberUtil.formatNumeric(voucher.getBookDtls().get(j).getCreditMoney()));
                    //业务种类
                    HSSFCell rowCell7 = row.createCell(7);
                    rowCell7.setCellStyle(centerCellStyle);
                    rowCell7.setCellValue(voucher.getAccbook().getRaceName());
                    //凭证种类
                    HSSFCell rowCell8 = row.createCell(8);
                    rowCell8.setCellStyle(centerCellStyle);
                    rowCell8.setCellValue(voucher.getAccbook().getVoucherName());
                    //备注
                    HSSFCell rowCell9 = row.createCell(9);
                    rowCell9.setCellStyle(centerCellStyle);
                    rowCell9.setCellValue(voucher.getAccbook().getRmk());
                    //附件张数
                    HSSFCell rowCell10 = row.createCell(10);
                    rowCell10.setCellStyle(centerCellStyle);
                    int count = 0;
                    if (!EmptyUtils.isEmpty(voucher.getAccbook().getAffixAmount())) {
                        count = voucher.getAccbook().getAffixAmount();
                    }
                    rowCell10.setCellValue(count);
                    //对方名称
                    HSSFCell rowCell11 = row.createCell(11);
                    rowCell11.setCellStyle(centerCellStyle);
                    rowCell11.setCellValue(voucher.getBookExt().getExternalCorp());
                    //对方账号
                    HSSFCell rowCell12 = row.createCell(12);
                    rowCell12.setCellStyle(centerCellStyle);
                    rowCell12.setCellValue(voucher.getBookExt().getExternalAcc());
                    //对方账号开户行
                    HSSFCell rowCell13 = row.createCell(13);
                    rowCell13.setCellStyle(centerCellStyle);
                    rowCell13.setCellValue(voucher.getBookExt().getExternalBank());
                }
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置标题
     *
     * @param sheet
     * @param title
     * @param centerCellStyle
     */
    public void setExportTitle(HSSFSheet sheet, String title, HSSFCellStyle centerCellStyle) {
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth(15);
        // (实际列宽+列宽基数：0.72) * 256
//            int width = new BigDecimal((80 / 3.5 + 0.72) * 256).intValue();
        sheet.setColumnWidth(0, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(1, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(2, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(3, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(4, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(5, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(6, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(7, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(8, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(9, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(10, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(11, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(12, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(13, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        // 主标题
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(centerCellStyle);
        titleCell.setCellValue(title);
        for (int i = 1; i <= 13; i++) {
            titleRow.createCell(i);
        }
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //合并列
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 13);
        ExportUtils.setRegionStyle(sheet, region, centerCellStyle);
        sheet.addMergedRegion(region);
    }
    /**
     * 获取已有记账的bill_id
     *
     * @param billId
     * @return
     */
    public List<String> getTallyRecordListId(List<String> billId) {
        return fcAccbookDao.getTallyRecordListId(billId);
    }

    public List<String> getVoucherNoByNetIdAndVoucherState(String netId, String voucherState) {
        return fcAccbookDao.getVoucherNoByNetIdAndVoucherState(netId, voucherState);
    }

    /**
     * 功能介绍 : 初始化手工记账凭证。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public FcAccbookVoucherVO setAccBookVoucher(FcAccbookVoucherVO voucher) {
        voucher.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        List<FcAccbookDtl> bookDtls = new ArrayList<FcAccbookDtl>();
        if (EmptyUtils.isEmptyList(voucher.getBookDtls())) {//分录为空
            voucher.setBookDtls(new ArrayList<FcAccbookDtl>());
        }
        // 初始化数据
        for (FcAccbookDtl bookDtl : voucher.getBookDtls()) {
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount())
                    && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
                continue;
            }
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            //
            ;
            Integer balanceWay = iFcAccService.getFcAccVOByBlnctrAcc(bookDtl.getAccount()).getBalanceWay();
            bookDtl.setBalanceWay(balanceWay);
            // 如果明细数据为空，则不保存非空明细
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount())) {
            } else {
                bookDtls.add(bookDtl);
            }
        }
        if (EmptyUtils.isEmptyList(bookDtls)) {//分录为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        // 明细进行先借后贷的排序
        Collections.sort(bookDtls, new Comparator<FcAccbookDtl>() {
            public int compare(FcAccbookDtl o1, FcAccbookDtl o2) {
                return o1.getCreditMoney().abs().compareTo(BigDecimal.ZERO);
            }
        });
        voucher.setBookDtls(bookDtls);
        return voucher;
    }

    public VoucherWrapper setVoucherWrapper(FcAccbookVoucherVO fcAccbook, String useId, ICurrentUser currentUser) {
        VoucherWrapper voucher = new VoucherWrapper();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(useId);//设置用户
        voucher.setUser(currentUser);
        return voucher;
    }
}
