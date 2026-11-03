package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.vo.FcAccHistoryVO;
import java.util.List;
/**
 * 内部历史账号查询相关接口
 *
 * @author wangf
 */
public interface IFcAccHistoryService {
	
	public FcAccHistoryVO getFcAccHistoryVOById(String id);
	
	public FcAccHistoryVO getFcAccHistoryById(String id);
	
	public FcAccHistoryVO mergeObj(FcAccHistoryVO object);
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id);
    /**
     * 保存
     *
     * @param vo
     */
    public void save(FcAccHistoryVO vo);
    /**
     * 更新
     *
     * @param vo
     */
    public void update(FcAccHistoryVO vo);
    /**
     * 保存or更新
     *
     * @param vo
     */
    public void saveOrUpdate(FcAccHistoryVO vo);
    /**
     * 待提交的可改户申请的数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO);
    /**
     * 待提交的可改户申请的数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser userVO);
    /**
     * 复核数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getCheckDataTable(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 复核数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getCheckDataTableForPool(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOById(String id);
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByIdForPool(String id);
    /**
     * 根据账号查询历史账户对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByBlnctrAcc(String blnctrAcc);
    /**
     * 根据账号信息查找计息账号列表
     *
     * @param vo
     * @return
     */
    public List<FcAccHistoryVO> getAccListByExpPay(FcAccHistoryVO vo);
    /**
     * 根据账号信息查找计息账号
     * @param vo
     * @return
     */
    public FcAccHistoryVO getFcAccHistoryVO(FcAccHistoryVO vo);
}
