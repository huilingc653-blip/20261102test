package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcCorpPool;
import com.bytter.system.api.vo.FcCorpPoolVO;
import java.util.List;
import java.util.Map;
/**
 * 资金池查询相关接口
 * @author wangzx
 * @since 2019-08-26 18:49:44
 */
public interface IFcCorpPoolService{
	/**
	 * 根据ID获取资金池信息
	 * @param id 资金池
	 * @return 资金池DTO
	 */
	FcCorpPoolDTO getById(String id);
	/**
	 * 根据资金池编码获取资金池信息
	 * @param poolCode 资金池编码
	 * @return 资金池DTO
	 */
	FcCorpPoolDTO getByPoolCode(String poolCode);
	/**
	 * 根据单位id查询是否是资金池主体单位
	 * @param corpId 单位
	 * @return 是否资金池
	 */
	boolean isCorpPool(String corpId);
	/**
	 * 获取资源权限的资金池
	 * @param user 操作用户
	 * @param resource 资源
	 * @return 资金池集合
	 */
	List<FcCorpPoolDTO> getUserCorpPools(ICurrentUser user, String resource);
	/**
	 * 获取资源权限的资金池子单位
	 * @param user 操作用户
	 * @param resource 资源
	 * @param poolId 主体单位id
	 * @return 资金池集合
	 */
	List<FcCorpPoolDTO> getUserCorpPoolChilds(ICurrentUser user, String resource, String poolId);
	/**
	 * 获取资源权限的资金池的子单位(包含主体单位本身)
	 * @param user 操作用户
	 * @param resource 资源
	 * @param poolId 主体单位id
	 * @return 资金池集合
	 */
	List<FcCorpPoolDTO> getAllUserCorpPoolChilds(ICurrentUser user, String resource, String poolId);
	/**
	 * 根据资金池ID和子单位Id获取子单位信息
	 * @param poolId 主体单位id
	 * @param corpId 单位id
	 * @return 资金池DTO
	 */
	FcCorpPoolDTO getChildCorpByPoolId(String poolId, String corpId);
	/**
	 * 获取当前用户对应资源权限的资金池单位id
	 * @param resource 资源
	 * @param user 操作用户
	 * @return 用户有操作权限的资金池单位id集合
	 */
	List<String> getUserCorpPoolIds(String resource, ICurrentUser user);
	/**
	 * 获取当前用户对应资源权限的资金池id
	 * @param resource 资源
	 * @param user 操作用户
	 * @return 用户有操作权限的资金池id集合
	 */
	List<String> getUserPoolIds(String resource, ICurrentUser user);
	/**
	 * 获取资源权限的资金池子单位的单位id
	 * @param user 操作用户
	 * @param resource 资源
	 * @param poolId 主体单位
	 * @return 用户有操作权限的资金池子单位单位id集合
	 */
	List<String> getUserCorpPoolChildIds(ICurrentUser user, String resource, String poolId);
	/**
	 * 根据父单位id和单位id查找资金池信息
	 * @param parentId 主体单位
	 * @param corpId 子单位
	 * @return 资金池DTO
	 */
	FcCorpPoolDTO getInfoByPoolIdCorpId(String parentId,String corpId);
	/**
	 * 查询所有资金池信息
	 * @param user 操作用户
	 * @param resource 资源
	 * @return 资金池DTO集合
	 */
	List<FcCorpPoolDTO> getAllCorpPool(ICurrentUser user, String resource);
	/**
	 * 根据子单位id查询资金池
	 * @param corpId 单位
	 * @return 资金池DTO集合
	 */
	List<FcCorpPoolDTO> getFcCorpPoolByCorpId(String corpId);
	
	public FcCorpPool getByIdCache(String id);
    /**
     * 根据资金池代码查询资金池信息
     * @param poolCode
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByPoolCode(String poolCode);
    /**
     * 根据资金池名称查询资金池信息
     * @param poolName
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByPoolName(String poolName);
    /**
     * 根据id查询资金池信息
     * @param id
     * @return
     */
    public FcCorpPoolVO getFcCorpPoolById(String id);
    /**
     * 根据单位id查询资金池单位
     * @param corpId
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByCorpId(String corpId,String poolSign);
    /**
     * 保存资金池信息
     * @param fcCorpPool
     * @param user
     */
    public void saveCorpPool(FcCorpPool fcCorpPool,ICurrentUser user);
    /**
     * 修改资金池
     * @param fcCorpPool
     * @param user
     */
    public void modifyCorpPool(FcCorpPool fcCorpPool,ICurrentUser user);
    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public List<FcCorpPoolVO> getTreeTable(String poolCode,String poolName, String status);
    /**
     * 根据id查询资金池下面的子单位个数
     * @param poolId
     * @return
     */
    public List<FcCorpPool> listByPoolId(String poolId);
	/**
	 * 根据主体单位查询资金池单位下面的子单位
	 * @param poolId
	 * @return
	 */
	public List<FcCorpPool> corpIdByPoolId(String poolId);
    /**
     * 获取资源权限的资金池
     * @param resource
     * @return
     */
    public List<FcCorpPoolVO> getUserCorpPoolsVO(ICurrentUser user, String resource);
    /**
     * 获取资源权限的资金池
     * @param user
     * @param resource
     * @return
     */
    public List<FcCorpPoolVO> getUserCorpPoolChildsVO(ICurrentUser user, String resource, String poolId);
    /**
     * 根据资金池ID和子单位Id获取子单位信息
     * @param poolId
     * @param corpId
     * @return
     */
    public FcCorpPoolVO getChildCorpByPoolIdVO(String poolId, String corpId);
    /**
     * 根据父单位id和单位id查找资金池信息
     * @param parentId
     * @param corpId
     * @return
     */
    public FcCorpPool getPoolInfo(String parentId,String corpId);
    /**
     * 查询所有资金池信息
     * @return
     */
    public List<FcCorpPoolVO> getAllCorpPool();
    /**
     * 判断资金池内部账户余额必须为0，且不能存在未结清的利息
     * @param corpId
     * @param user
     * @return
     */
    public boolean vaildAcc(String corpId,ICurrentUser user);
    /**
     * 根据单位查询状态为(开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回)账号
     * @param poolId
     * @param corpId
     * @param isFilter 是否过滤状态 true过滤，flase 不过滤
     * @return
     */
    public boolean fcAccUse(String poolId, String corpId, boolean isFilter);
    /**
     * 根据单位查询账号是否有关联关系
     * @param corpId
     * @param corpId
     * @return
     */
    public boolean relevance(String poolId, String corpId);
    
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId);
    
    public FcCorpPool getFcCorpPool(String id);
    
    public void deleteFcCorpPool(String id);
	List<FcCorpPoolDTO> getFcCorpPoolByIds(List<String> ids);
	
	List<FcCorpPoolDTO> getFcCorpPoolByPoolIds(List<String> ids);
	/**
	 * 根据 id，查询 返回 poolName。k = id, v = poolName
	 * @param ids
	 * @return
	 */
	Map<String,String> getPoolNameByIds(List<String> ids);
	/**
	 * 保存对象
	 * @param fcCorpPool
	 */
	public void saveEntity(FcCorpPool fcCorpPool);
}
