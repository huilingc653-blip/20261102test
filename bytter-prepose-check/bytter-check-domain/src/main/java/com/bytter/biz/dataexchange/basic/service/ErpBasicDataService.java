package com.bytter.biz.dataexchange.basic.service;
import com.bytter.biz.dataexchange.basic.dao.ErpBasicDataDao;
import com.bytter.biz.dataexchange.basic.entity.ErpBasicData;
import com.bytter.biz.dataexchange.basic.entity.ErpDataType;
import com.bytter.biz.dataexchange.basic.vo.ErpBasicDataListVO;
import com.bytter.biz.dataexchange.basic.vo.ErpBasicDataVO;
import com.bytter.biz.dataexchange.basic.vo.ErpTypeVO;
import com.bytter.check.enums.DataExchangeMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.ErpBasicDataVOVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.bytter.core.utils.DateUtils.getDate;
/**
* @Title: Service
* @Description: 基础数据映射
* @author onlineGenerator
* @date 2017-12-14 11:24:27
* @version 1.0
*
*/
@Service("erpBasicDataService")
@Transactional
public class ErpBasicDataService extends BaseService<ErpBasicData,String> implements IExcelVerifyHandler<ErpBasicDataVO> {
	@Autowired
	private ErpBasicDataDao erpBasicDataDao;
	@Autowired
	private ErpTypeService erpTypeService;
	@Autowired
	private ErpDataTypeService erpDataTypeService;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private ISystemForeignService systemForeignService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(ErpBasicData entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(ErpBasicData entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(ErpBasicData entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(ErpBasicData entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<ErpBasicData, String> getEntityDao() {
		return erpBasicDataDao;
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		String typeCode = dataTablesInput.getFormdata().get("typeCode")+"";
		String dataTypeCode = dataTablesInput.getFormdata().get("dataTypeCode")+"";
		if(StringUtils.isEmpty(typeCode) || StringUtils.isEmpty(dataTypeCode)) {
			List<ErpBasicDataVO> listVO = new ArrayList<>();
			return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
		}
		ErpTypeVO erpType = erpTypeService.getErpTypeByCode(typeCode);
		ErpDataType erpDataType = erpDataTypeService.getErpDataTypeByCode(dataTypeCode);
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append(" ").append(erpDataType.getTableName()).append(".").append(erpDataType.getKeyField()).append(" as bytterCode,");
		sql.append(" ").append(erpDataType.getTableName()).append(".").append(erpDataType.getValueField()).append(" as bytterName");
		sql.append(" from ").append(erpDataType.getTableName()).append(" ").append(erpDataType.getTableName()).append(" ");
		if(StringUtils.isNotEmpty(erpDataType.getQueryCondition())){
			sql.append(" where ").append(erpDataType.getQueryCondition());
		}else{
			sql.append(" where 1=1 ");
		}
		Object bytterCode = dataTablesInput.getFormdata().get("bytterCode");
		if(StringUtils.isNotEmpty(bytterCode)){
			sql.append(" and ").append(erpDataType.getTableName()).append(".").append(erpDataType.getKeyField()).append(" like '%").append(bytterCode).append("%'");
		}
		sql.append(" order by ").append(erpDataType.getKeyField());
		List<ErpBasicDataVOVO>  vovoList= systemForeignService.listErpBasicDataVOVO(sql.toString());
		for(ErpBasicDataVOVO vo:vovoList){
			ErpBasicData erpBasicData = erpBasicDataDao.getBean(vo.getBytterCode(), dataTypeCode, typeCode);
			if(null != erpBasicData) {
				vo.setErpCode(erpBasicData.getErpCode());
				vo.setErpName(erpBasicData.getErpName());
				vo.setErpReverse(erpBasicData.getErpReverse());
				vo.setId(erpBasicData.getId());
			}
		}
		DataTablesOutput output = new DataTablesOutput();
		output.setData(vovoList);
		return output;
	}
	/**
	 * 保存
	 * @param erpBasicDataList
	 */
	public void saveErpBasicData(ErpBasicDataListVO erpBasicDataList, ICurrentUser user){
		List<ErpBasicDataVO> erpBasicDataVOS = erpBasicDataList.getErpBasicDataVOS();
		List<ErpBasicData> list=new ArrayList<>();
		ErpBasicData erpBasicData;
		java.util.Date initTime= getDate();
		for(ErpBasicDataVO vo : erpBasicDataVOS){
			erpBasicData=new ErpBasicData();
			BeanUtils.copyNotNullProperties(vo, erpBasicData);
			erpBasicData.setDataTypeCode(erpBasicDataList.getDataTypeCode());
			erpBasicData.setTypeCode(erpBasicDataList.getTypeCode());
			erpBasicData.setCreateBy(user.getLoginName());
			erpBasicData.setUpdateBy(user.getLoginName());
			erpBasicData.setCreateDate(initTime);
			erpBasicData.setUpdateDate(initTime);
			list.add(erpBasicData);
		}
//		erpBasicDataDao.batchSaveOrUpdate(list);
		for (int i = 0; i < list.size(); i++) {
			ErpBasicData data = list.get(i);
			if (StringUtils.isNotEmpty(data.getId())) {
				erpBasicDataDao.merge(data);
			} else {
				ErpBasicData erpBasicData1= erpBasicDataDao.getBean(data.getBytterCode(),data.getDataTypeCode(),data.getTypeCode());
				if(erpBasicData1 != null){
					BeanUtils.copyNotNullProperties(data, erpBasicData1);
					erpBasicDataDao.update(erpBasicData1);
				}else {
					erpBasicDataDao.save(data);
				}
			}
			if (i % 100 == 0) {
				erpBasicDataDao.flush();
			}
		}
	}
	public DataTablesOutput getErpList(DataTablesInput dataTablesInput){
        DataTablesOutput output = erpBasicDataDao.getErpList(dataTablesInput);
        List<ErpBasicDataVO> dataVOS = output.getData();
        if (EmptyUtils.isNotEmpty(dataVOS)) {
            List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
            Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<String, BtCurrencyDTO>();
            for(BtCurrencyDTO currencyDTO : currencyDTOList) {
                currencyDTOMap.put(currencyDTO.getId(), currencyDTO);
            }
            for(ErpBasicDataVO dataVO : dataVOS) {
                String curId = dataVO.getErpReverse();
                BtCurrencyDTO currencyDTO = currencyDTOMap.get(curId);
                if (!EmptyUtils.isEmpty(currencyDTO)) {
                    dataVO.setCurName(currencyDTO.getCurName());
                }
            }
        }
		return output;
	}
	
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<ErpBasicDataVO> list, ICurrentUser user) {
	    List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
	    Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<String, BtCurrencyDTO>();
	    for(BtCurrencyDTO currencyDTO : currencyDTOList) {
	        currencyDTOMap.put(currencyDTO.getCurName(), currencyDTO);
        }
		List<ErpBasicData> entityList = new ArrayList<ErpBasicData>();
		for (ErpBasicDataVO vo : list) {
			ErpBasicData erpBasic = new ErpBasicData();
			BeanUtils.copyNotNullProperties(vo, erpBasic);
			erpBasic.setBytterCode(vo.getBytterCode());//银行账号
            String curName = vo.getErpReverse();
            BtCurrencyDTO currencyDTO = currencyDTOMap.get(curName);
            if (EmptyUtils.isEmpty(currencyDTO)) {
                throw new ParameterException(DataExchangeMessageEnum.DATAEXCHANGE_MESSAGE_CURRENT_NOTEXISTS, curName);
            }
            erpBasic.setErpReverse(currencyDTO.getId());//币别
			erpBasic.setErpCode(vo.getErpCode());//erpId
			erpBasic.setErpName(vo.getErpName());//erp名称
			erpBasic.setDataTypeCode("BT_BANK_ACC");
			erpBasic.setTypeCode("rsm");
			
			// 判断银行账号是否在系统中存在
			if (!this.isBankAcc(vo.getBytterCode(), vo.getErpReverse())) {
				continue;
			}
			entityList.add(erpBasic);
		}
		this.batchSave(entityList);
		
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(ErpBasicDataVO obj) {
		return null;
	}
	@Override
	public void verifyHandler(List<ErpBasicDataVO> list) {
        List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
        Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<String, BtCurrencyDTO>();
        for(BtCurrencyDTO currencyDTO : currencyDTOList) {
            currencyDTOMap.put(currencyDTO.getCurName(), currencyDTO);
        }
		List<String> bankAccList = new ArrayList<>();
		for (ErpBasicDataVO vo : list) {
			List<String> msgList = new ArrayList<>();
			// 银行账号不为空
			if (EmptyUtils.isEmpty(vo.getBytterCode())) {
				msgList.add("nis.netbank.message.downacc.null");
            } else {
                if (EmptyUtils.isEmpty(vo.getErpReverse())) {
                    msgList.add("nis.netbank.message.curname.null");
                } else {
                    String curName = vo.getErpReverse();
                    BtCurrencyDTO currencyDTO = currencyDTOMap.get(curName);
                    if (EmptyUtils.isEmpty(currencyDTO)) {
                        msgList.add("dataexchange.message.current.notexists");
                    }
                    vo.setCurId(currencyDTO.getId());//币别
                    // 判断银行账号是否在系统中存在
                    if (this.isBankAcc(vo.getBytterCode(), vo.getCurId())) {
                        // 导入文件中存在多个相同账户
                        if (bankAccList.contains(vo.getBytterCode() + "_" + vo.getErpReverse())) {
                            msgList.add("nis.netbank.message.bankacc.multiple");
                        } else {
                            bankAccList.add(vo.getBytterCode() + "_" + vo.getErpReverse());
                        }
                    } else {
                        msgList.add("nis.netbank.message.bankaccinfo.notnull");
                    }
                }
			}
			// ERP科目不为空
			if (EmptyUtils.isEmpty(vo.getErpCode())) {
				msgList.add("erp.basic.erpcode.is.null");
			}
			// ERP名称不为空
			if (EmptyUtils.isEmpty(vo.getErpName())) {
				msgList.add("fin.paramconfig.fcsubject.subjectname.null");
			}
			vo.addErrorMsg(msgList);
		}
	}
	/**
	 * 判断是否有相同的银行账号
	 *
	 * @param bytterCode
	 * @return
	 */
	public boolean isBankAcc(String bytterCode) {
		return erpBasicDataDao.isBankAcc(bytterCode);
	}
    /**
     * 判断是否有相同的银行账号
     *
     * @param bytterCode
     * @return
     */
    public boolean isBankAcc(String bytterCode, String erpReverse) {
        return erpBasicDataDao.isBankAcc(bytterCode, erpReverse);
    }
}
