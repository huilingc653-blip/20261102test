package com.bytter.basic.service;
import com.bytter.basic.dao.BtSwiftInfoDao;
import com.bytter.basic.entity.BtSwiftInfo;
import com.bytter.basic.vo.BtSwiftInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.api.enums.SwiftInfoMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * 境外银行行号信息业务层
 *
 * @author qinl
 * @date 2019-07-25
 */
@Service("btSwiftInfoServiceBase")
@Transactional
public class BtSwiftInfoService extends BaseService<BtSwiftInfo, String> implements IExcelVerifyHandler<BtSwiftInfoVO> {
    /**
     * 国家信息业务层
     */
    @Autowired
    private ISysCountryCustService sysCountryCustService;
    /**
     * 境外银行信息数据层
     */
    @Autowired
    private BtSwiftInfoDao btSwiftInfoDao;
    /**
     * 银行类型服务接口
     */
    @Autowired
    private IBankTypeService bankTypeService;
    @Override
    public BaseDao<BtSwiftInfo, String> getEntityDao() {
        return btSwiftInfoDao;
    }
    /**
     * <p>
     * 获取列表数据
     * </p>
     *
     * @return DataTablesOutput（Entity）
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return btSwiftInfoDao.selectDataTable(dataTablesInput);
    }
    /**
     * <p>
     * 根据 <b>ID</b> 获取一条境外银行信息
     * </p>
     *
     * @param id [id]
     * @return (VO)
     */
    public BtSwiftInfoVO getVOById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return btSwiftInfoDao.selectVOById(id);
    }
    /**
     * <p>
     * 根据 <b>ID</b> 获取一条境外银行信息
     * </p>
     *
     * @param id [id]
     * @return (Entity)
     */
    public BtSwiftInfo getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return btSwiftInfoDao.selectById(id);
    }
    /**
     * <p>
     * 根据 <b>swiftCode</b> 获取一条境外银行信息
     * </p>
     *
     * @param swiftCode [swiftCode]
     * @return (VO)
     */
    public BtSwiftInfoVO getByCode(String swiftCode) {
        if (StringUtils.isEmpty(swiftCode)) {
            return null;
        }
        BtSwiftInfoVO btSwiftInfoVO = new BtSwiftInfoVO();
        btSwiftInfoVO.setSwiftCode(swiftCode);
        return btSwiftInfoDao.selectOne(btSwiftInfoVO);
    }
    /**
     * <p>
     * 根据 <b>机构名称</b> 获取一条境外银行信息
     * </p>
     *
     * @param bankName [机构名称]
     * @return (VO)
     */
    public BtSwiftInfoVO getByBankName(String bankName) {
        if (StringUtils.isEmpty(bankName)) {
            return null;
        }
        BtSwiftInfoVO btSwiftInfoVO = new BtSwiftInfoVO();
        btSwiftInfoVO.setBankName(bankName);
        return btSwiftInfoDao.selectOne(btSwiftInfoVO);
    }
    //增删改 begin-------------------------------------------------------------
    /**
     * <p>
     * 保存一条银行信息
     * </p>
     *
     * @param btSwiftInfoVO [待保存的数据]
     * @param userName      [操作人]
     * @return boolean
     */
    public boolean save(BtSwiftInfoVO btSwiftInfoVO, String userName) {
        if (btSwiftInfoVO == null) {
            return false;
        }
        //检查swiftCode是否存在
        if (StringUtils.isNotEmpty(btSwiftInfoVO.getSwiftCode()) && this.getByCode(btSwiftInfoVO.getSwiftCode()) != null) {
            throw new ParameterException(SwiftInfoMessageEnum.TABLE_BTSWIFTINFO_SWIFTCODE_EXIST, btSwiftInfoVO.getSwiftCode());
        }
        //检查机构名称是否存在
        if (StringUtils.isNotEmpty(btSwiftInfoVO.getBankName()) && this.getByBankName(btSwiftInfoVO.getBankName()) != null) {
            throw new ParameterException(SwiftInfoMessageEnum.TABLE_BTSWIFTINFO_BANKNAME_EXIST);
        }
        BtSwiftInfo btSwiftInfo = new BtSwiftInfo();
        BeanUtils.copyNotNullProperties(btSwiftInfoVO, btSwiftInfo);
        this.setCreateOrUpdate(btSwiftInfo, userName, false);
        super.saveEntity(btSwiftInfo);
        return true;
    }
    /**
     * <p>
     * 更新一条银行信息
     * </p>
     *
     * @param btSwiftInfoVO [待更新的数据]
     * @param userName      [操作人]
     * @return boolean
     */
    public boolean update(BtSwiftInfoVO btSwiftInfoVO, String userName) {
        if (btSwiftInfoVO == null) {
            return false;
        }
        if (StringUtils.isEmpty(btSwiftInfoVO.getId())) {
            return false;
        }
        //检查swiftCode是否已存在
        BtSwiftInfoVO checkSwiftInfo = this.getByCode(btSwiftInfoVO.getSwiftCode());
        if (checkSwiftInfo != null) {
            if (!checkSwiftInfo.getId().equals(btSwiftInfoVO.getId())) {
                throw new ParameterException(SwiftInfoMessageEnum.TABLE_BTSWIFTINFO_SWIFTCODE_EXIST, btSwiftInfoVO.getSwiftCode());
            }
        }
        //检查机构名称是否存在
        BtSwiftInfoVO checkBankName = this.getByBankName(btSwiftInfoVO.getBankName());
        if (checkBankName != null) {
            if (!checkBankName.getId().equals(btSwiftInfoVO.getId())) {
                throw new ParameterException(SwiftInfoMessageEnum.TABLE_BTSWIFTINFO_BANKNAME_EXIST);
            }
        }
        BtSwiftInfo btSwiftInfo = this.getById(btSwiftInfoVO.getId());
        BeanUtils.copyNotNullProperties(btSwiftInfoVO, btSwiftInfo);
        this.setCreateOrUpdate(btSwiftInfo, userName, true);
        super.saveOrUpdate(btSwiftInfo);
        return true;
    }
    /**
     * <p>
     * 删除一条银行信息
     * </P>
     *
     * @param id [id]
     */
    public void deleteById(String id) {
        super.delete(id);
    }
    /**
     * <p>
     * 批量删除银行信息
     * </P>
     *
     * @param ids [多条id字符串,格式：1,2,3]
     */
    public void deleteByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return;
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            this.deleteById(id);
        }
    }
    //增删改 end-------------------------------------------------------------
    //导入功能 begin-------------------------------------------------------------
    /**
     * <p>
     * 校验成功后，保存操作
     * </p>
     *
     * @param list        [待保存的excel]
     * @param currentUser [操作用户]
     */
    public void saveImport(List<BtSwiftInfoVO> list, ICurrentUser currentUser) {
        //swiftCode去重
        List<String> checkSwiftCodeList = new ArrayList<>();
        List<String> checkBankName = new ArrayList<>();
        for (BtSwiftInfoVO swiftInfoVO : list) {
            if (!checkSwiftCodeList.contains(swiftInfoVO.getSwiftCode()) && !checkBankName.contains(swiftInfoVO.getBankName())) {
                checkSwiftCodeList.add(swiftInfoVO.getSwiftCode());
                checkBankName.add(swiftInfoVO.getBankName());
                this.save(swiftInfoVO, currentUser.getLoginName());
            }
        }
    }
    /**
     * 导入数据校验
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtSwiftInfoVO btSwiftInfoVO) {
        List<String> msgList = new ArrayList<>();
        //境外银行编号检查
        BtSwiftInfoVO checkSwiftInfoVO = this.getByCode(btSwiftInfoVO.getSwiftCode());
        if (checkSwiftInfoVO != null) {
            msgList.add("basic.business.message.btswiftinfo.swiftcode.isexist");
        }else {
            if (btSwiftInfoVO.getSwiftCode().length()>30){
//                msgList.add("basic.business.message.btswiftinfo.swiftcode.max");
                msgList.add("SWIFTCODE导入长度不允许超过30");

            }
        }
        //地址校验
        if(btSwiftInfoVO.getBankAdd() !=null && btSwiftInfoVO.getBankAdd().length() > 300){
            msgList.add("地址导入长度不允许超过300");

        }
        //机构名称检查
        BtSwiftInfoVO checkBankName = this.getByBankName(btSwiftInfoVO.getBankName());
        if (checkBankName != null) {
            msgList.add("table.btswiftinfo.bankname.exist");
        }
        //银行类型校验
        if (StringUtils.isNotBlank(btSwiftInfoVO.getBankTypeName())) {
            BankTypeDTO bankType = bankTypeService.getByTypeName(btSwiftInfoVO.getBankTypeName());
            if (bankType != null) {
                btSwiftInfoVO.setBankTypeId(bankType.getId());
            } else {
                msgList.add("table.btswiftinfo.bankType.notExist");
            }
        }
        //所在国编号检查
        if (StringUtils.isNotEmpty(btSwiftInfoVO.getNameZh())) {
            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZh(btSwiftInfoVO.getNameZh());
            if (sysCountryCustDTO == null || StringUtils.isBlank(sysCountryCustDTO.getNameZh()) || CommonConstant.YES.equals(sysCountryCustDTO.getCountryLevel())) {
                msgList.add("sys.externalcorp.import.provnamefex.mismatch");
            } else {
                btSwiftInfoVO.setCountryCode(sysCountryCustDTO.getId());
            }
        }
        //所在城市
        if (StringUtils.isNotEmpty(btSwiftInfoVO.getCityNameZh())) {
            if (StringUtils.isNotEmpty(btSwiftInfoVO.getCountryCode())) {
                SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZhAndSysId(btSwiftInfoVO.getCityNameZh(), btSwiftInfoVO.getCountryCode());
                if (sysCountryCustDTO == null || StringUtils.isBlank(sysCountryCustDTO.getNameZh())) {
                    msgList.add("basic.business.message.btswiftinfo.city.isexist");
                } else {
                    btSwiftInfoVO.setCityName(sysCountryCustDTO.getId());
                }
            }
        }
        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }
    @Override
    public void verifyHandler(List<BtSwiftInfoVO> list) {
    }
    //导入功能 end-------------------------------------------------------------
    /**
     * 通用属性值设置(创建或更新)
     *
     * @param btSwiftInfo [实体]
     * @param userName    [操作人]
     * @param updateFlag  [是否更新]
     */
    private void setCreateOrUpdate(BtSwiftInfo btSwiftInfo, String userName, boolean updateFlag) {
        if (updateFlag) {
            btSwiftInfo.setUpdateBy(userName);
            btSwiftInfo.setUpdateDate(DateUtils.getDate());
        } else {
            btSwiftInfo.setCreateBy(userName);
            btSwiftInfo.setCreateDate(DateUtils.getDate());
        }
    }

    public List<BtSwiftInfoVO> getByCountryCode(String countryCode) {
        return btSwiftInfoDao.selectByCountryCode(countryCode);
    }
}
