package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.vo.FcAccVO;
import java.util.List;
import java.util.Map;
public interface IFcAccCancelService {
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id);
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO);
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params);
    /**
     * 是否活期
     *
     * @param accTypeCode
     * @return
     */
    public boolean isCurrType(String accTypeCode);
    /**
     * 获取FcAccVO
     *
     * @param account
     * @return
     */
    public FcAccVO getFcAccVOByBlnctrAcc(String account);
    /**
     * 注销转出账号,单位企业活期账号，不包含自己的活期账号
     *
     * @param blnctrAcc
     * @param netId
     * @return
     */
    public List<FcAccVO> transferOutAcc(String blnctrAcc, String netId);
    /**
     * <b>功能介绍：</b>获取借方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public String getDebitAccout(FcAccVO fcAccDTO, boolean interestSign);
    /**
     * <b>功能介绍：</b>获取贷方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public String getCreditAccout(FcAccVO fcAccDTO, boolean interestSign);
}
