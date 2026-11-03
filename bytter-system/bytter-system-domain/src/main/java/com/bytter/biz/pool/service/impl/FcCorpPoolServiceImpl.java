package com.bytter.biz.pool.service.impl;

import com.bytter.biz.pool.entity.FcCorpPool;
import com.bytter.biz.pool.service.FcCorpPoolService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcCorpPoolVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author wangzx
 * @version 1.0
 * @Title: ServiceImpl
 * @Description: 资金池接口实现类
 * @date 2019-04-30 16:44:38
 */
@Service("fcCorpPoolServiceI")
@Transactional
@DubboService
public class FcCorpPoolServiceImpl implements IFcCorpPoolService {
    @Autowired
    private FcCorpPoolService fcCorpPoolService;
    @Autowired
	private ICorpService corpServiceI;
    @Autowired
	private Ii18nService i18nServiceI;
    /**
     * 功能介绍 : 根据ID获取资金池信息<br>
     * <b>日期：</b>2019年5月6日<br>
     *
     * @version 1.0
     * @author wangzx
     */
    @Override
    public FcCorpPoolDTO getById(String id) {
        if(EmptyUtils.isEmpty(id)){
            return null;
        }
        FcCorpPool fcCorpPool = fcCorpPoolService.findById(id);//之前的GET方法，如果ID对应的记录为空会抛javax.persistence.EntityNotFoundException异常
        if (ObjectUtils.isEmpty(fcCorpPool) || StringUtils.isBlank(fcCorpPool.getId())) {
            return null;
        }
        FcCorpPoolDTO fcCorpPoolDTO = new FcCorpPoolDTO();
        BeanUtils.copyNotNullProperties(fcCorpPool, fcCorpPoolDTO);
        return fcCorpPoolDTO;
    }
    /**
     * 功能介绍 : 根据资金池编码获取资金池信息<br>
     * <b>日期：</b>2019年5月6日<br>
     *
     * @version 1.0
     * @author wangzx
     */
    @Override
    public FcCorpPoolDTO getByPoolCode(String poolCode) {
        FcCorpPool fcCorpPool = fcCorpPoolService.getFcCorpPoolByPoolCode(poolCode).get(0);
        if (EmptyUtils.isEmpty(fcCorpPool)) {
            return null;
        }
        FcCorpPoolDTO fcCorpPoolDTO = new FcCorpPoolDTO();
        BeanUtils.copyNotNullProperties(fcCorpPool, fcCorpPoolDTO);
        return fcCorpPoolDTO;
    }
    /**
     * 功能介绍 : 根据单位id查询是否是资金池主体单位<br>
     * <b>日期：</b>2019年5月7日<br>
     *
     * @version 1.0
     * @author wangzx
     */
    @Override
    public boolean isCorpPool(String corpId) {
        List<FcCorpPool> fcCorpPoolList = fcCorpPoolService.getFcCorpPoolByCorpId(corpId, PoolContant.POOL_SIGN_1);
        if (EmptyUtils.isEmptyList(fcCorpPoolList)) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 功能介绍 : 根据子单位id查询资金池<br>
     * <b>日期：</b>2019年6月15日<br>
     *
     * @version 1.0
     * @author wangzx
     */
    @Override
    public List<FcCorpPoolDTO> getFcCorpPoolByCorpId(String corpId) {
        List<FcCorpPool> fcCorpPoolList = fcCorpPoolService.getFcCorpPoolByCorpId(corpId, PoolContant.POOL_SIGN_0);
        return BeanUtils.copyListProperties(fcCorpPoolList, FcCorpPoolDTO.class);
    }
    /**
     * 获取资源权限的资金池
     *
     * @param user
     * @param resource
     * @return
     */
    @Override
    public List<FcCorpPoolDTO> getUserCorpPools(ICurrentUser user, String resource) {
        List<FcCorpPoolVO> fcCorpPoolList = fcCorpPoolService.getUserCorpPools(user, resource);
        //获取有效的网点、单位
        List<CorpDTO> corpList = corpServiceI.listEffectiveCorp();
        List<FcCorpPoolDTO> fcCorpPoolDTOList = new ArrayList<>();
        for (FcCorpPoolVO fcCorpPoolVO : fcCorpPoolList) {
            FcCorpPoolDTO fcCorpPoolDTO = new FcCorpPoolDTO();
            fcCorpPoolDTO.setShowName("(" + fcCorpPoolVO.getPoolCode() + ")" + fcCorpPoolVO.getPoolName());
            BeanUtils.copyNotNullProperties(fcCorpPoolVO, fcCorpPoolDTO);
            fcCorpPoolDTOList.add(fcCorpPoolDTO);
            for(CorpDTO corpDTO : corpList){
                if(fcCorpPoolDTO.getCorpId() == corpDTO.getId()){
                    fcCorpPoolDTO.setCorpName(corpDTO.getName());
                    fcCorpPoolDTO.setCorpCode(corpDTO.getCode());
                }
            }
        }
        return fcCorpPoolDTOList;
    }
    /**
     * 获取资源权限的资金池的子单位
     *
     * @param user
     * @param resource
     * @param poolId
     * @return
     */
    @Override
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(ICurrentUser user, String resource, String poolId) {
        List<FcCorpPoolDTO> fcCorpPoolDTOList = new ArrayList<>();
        if (!StringUtils.isBlank(poolId)) {
            List<FcCorpPoolVO> fcCorpPoolList = fcCorpPoolService.getUserCorpPoolChilds(user, resource, poolId);
            for (FcCorpPoolVO fcCorpPoolVO : fcCorpPoolList) {
                FcCorpPoolDTO fcCorpPoolDTO = new FcCorpPoolDTO();
                fcCorpPoolDTO.setShowName("(" + fcCorpPoolVO.getCorpCode() + ")" + fcCorpPoolVO.getCorpName());
                BeanUtils.copyNotNullProperties(fcCorpPoolVO, fcCorpPoolDTO);
                fcCorpPoolDTOList.add(fcCorpPoolDTO);
            }
        }
        return fcCorpPoolDTOList;
    }
    /**
     * 获取资源权限的资金池的子单位(包含主体单位本身)
     *
     * @param user
     * @param resource
     * @param poolId
     * @return
     */
    @Override
    public List<FcCorpPoolDTO> getAllUserCorpPoolChilds(ICurrentUser user, String resource, String poolId) {
        List<FcCorpPoolDTO> fcCorpPoolDTOList = new ArrayList<>();
        if (!StringUtils.isBlank(poolId)) {
            //获取资金池
            FcCorpPoolDTO fcCorpPoolDTO = this.getById(poolId);
            CorpDTO corpDTO = corpServiceI.get(fcCorpPoolDTO.getCorpId());//获取主体单位信息
            fcCorpPoolDTO.setCorpName(corpDTO.getName());
            fcCorpPoolDTO.setShowName("(" + corpDTO.getCode() + ")" + corpDTO.getName());
            fcCorpPoolDTOList.add(fcCorpPoolDTO);
            fcCorpPoolDTOList.addAll(this.getUserCorpPoolChilds(user, resource, poolId));
        }
        return fcCorpPoolDTOList;
    }
    /**
     * 根据资金池ID和子单位Id获取子单位信息
     *
     * @param poolId
     * @param corpId
     * @return
     */
    @Override
    public FcCorpPoolDTO getChildCorpByPoolId(String poolId, String corpId) {
        FcCorpPoolVO fcCorpPoolVO = this.fcCorpPoolService.getChildCorpByPoolId(poolId, corpId);
        if (EmptyUtils.isEmpty(fcCorpPoolVO)) {
            return null;
        }
        FcCorpPoolDTO fcCorpPoolDTO = new FcCorpPoolDTO();
        BeanUtils.copyNotNullProperties(fcCorpPoolVO, fcCorpPoolDTO);
        return fcCorpPoolDTO;
    }
    /**
     * 获取当前用户对应资源权限的资金池单位id
     *
     * @param resource 资源
     * @param user     用户
     * @return
     */
    @Override
    public List<String> getUserCorpPoolIds(String resource, ICurrentUser user) {
        List<String> poolList = new ArrayList<>();
        List<FcCorpPoolVO> fcCorpPoolList = fcCorpPoolService.getUserCorpPools(user, resource);
        if (!EmptyUtils.isEmptyList(fcCorpPoolList)) {
            for (FcCorpPoolVO vo : fcCorpPoolList) {
                //资金池的单位corp_id
                poolList.add(vo.getCorpId());
            }
        }
        return poolList;
    }
    /**
     * 获取当前用户对应资源权限的资金池id
     *
     * @param resource 资源
     * @param user     用户
     * @return
     */
    @Override
    public List<String> getUserPoolIds(String resource, ICurrentUser user) {
        List<String> poolList = new ArrayList<>();
        List<FcCorpPoolVO> fcCorpPoolList = fcCorpPoolService.getUserCorpPools(user, resource);
        if (!EmptyUtils.isEmptyList(fcCorpPoolList)) {
            for (FcCorpPoolVO vo : fcCorpPoolList) {
                //资金池的id
                poolList.add(vo.getId());
            }
        }
        return poolList;
    }
    /**
     * 获取资源权限的资金池子单位id
     *
     * @param user
     * @param resource
     * @param poolId
     * @return
     */
    @Override
    public List<String> getUserCorpPoolChildIds(ICurrentUser user, String resource, String poolId) {
        List<FcCorpPoolVO> fcCorpPoolList = fcCorpPoolService.getUserCorpPoolChilds(user, resource, poolId);
        List<String> poolChileList = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(fcCorpPoolList)) {
            for (FcCorpPoolVO vo : fcCorpPoolList) {
                poolChileList.add(vo.getCorpId());
            }
        }
        return poolChileList;
    }
    /**
     * 根据父单位id和单位id查找资金池信息
     *
     * @param parentId
     * @param corpId
     * @return
     */
    @Override
    public FcCorpPoolDTO getInfoByPoolIdCorpId(String parentId, String corpId) {
        FcCorpPool fcCorpPool = fcCorpPoolService.getPoolInfo(parentId, corpId);
        if (EmptyUtils.isEmpty(fcCorpPool)) {
            return null;
        }
        FcCorpPoolDTO fcCorpPoolDTO = new FcCorpPoolDTO();
        BeanUtils.copyNotNullProperties(fcCorpPool, fcCorpPoolDTO);
        return fcCorpPoolDTO;
    }
    /**
     * 查询所有资金池信息
     *
     * @return
     */
    @Override
    public List<FcCorpPoolDTO> getAllCorpPool(ICurrentUser user, String resource) {
        List<FcCorpPoolVO> poolList = this.fcCorpPoolService.getAllCorpPool();
        List<FcCorpPoolDTO> tempList = new ArrayList<FcCorpPoolDTO>();
        if (null != tempList) {
            tempList = BeanUtils.copyListProperties(poolList, FcCorpPoolDTO.class);
        }
        return tempList;
    }
	@Override
	public com.bytter.system.api.vo.FcCorpPool getByIdCache(String id) {
		com.bytter.biz.pool.entity.FcCorpPool pool = fcCorpPoolService.getByIdCache(id);
		com.bytter.system.api.vo.FcCorpPool fcPool = new com.bytter.system.api.vo.FcCorpPool();
		BeanUtils.copyProperties(pool, fcPool);
		return fcPool;
	}
	@Override
	public List<com.bytter.system.api.vo.FcCorpPool> getFcCorpPoolByPoolCode(String poolCode) {
		List<com.bytter.biz.pool.entity.FcCorpPool> list = fcCorpPoolService.getFcCorpPoolByPoolCode(poolCode);
		List<com.bytter.system.api.vo.FcCorpPool> listvo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.FcCorpPool.class);
		return listvo;
	}
	@Override
	public List<com.bytter.system.api.vo.FcCorpPool> getFcCorpPoolByPoolName(String poolName) {
		List<com.bytter.biz.pool.entity.FcCorpPool> list = fcCorpPoolService.getFcCorpPoolByPoolName(poolName);
		List<com.bytter.system.api.vo.FcCorpPool> listvo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.FcCorpPool.class);
		return listvo;
	}
	@Override
	public FcCorpPoolVO getFcCorpPoolById(String id) {
		com.bytter.system.api.vo.FcCorpPoolVO fcPool = fcCorpPoolService.getFcCorpPoolById(id);
		return fcPool;
	}
	@Override
	public List<com.bytter.system.api.vo.FcCorpPool> getFcCorpPoolByCorpId(String corpId, String poolSign) {
		List<com.bytter.biz.pool.entity.FcCorpPool> list = fcCorpPoolService.getFcCorpPoolByCorpId(corpId,poolSign);
		List<com.bytter.system.api.vo.FcCorpPool> listvo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.FcCorpPool.class);
		return listvo;
	}
	@Override
	public void saveCorpPool(com.bytter.system.api.vo.FcCorpPool fcCorpPool, ICurrentUser user) {
		com.bytter.biz.pool.entity.FcCorpPool pool = new com.bytter.biz.pool.entity.FcCorpPool();
		BeanUtils.copyProperties(fcCorpPool, pool);
		fcCorpPoolService.saveCorpPool(pool, user);
	}
	@Override
	public void modifyCorpPool(com.bytter.system.api.vo.FcCorpPool fcCorpPool, ICurrentUser user) {
		com.bytter.biz.pool.entity.FcCorpPool pool = new com.bytter.biz.pool.entity.FcCorpPool();
		BeanUtils.copyProperties(fcCorpPool, pool);
		fcCorpPoolService.modifyCorpPool(pool, user);
	}
	@Override
	public List<FcCorpPoolVO> getTreeTable(String poolCode, String poolName, String status) {
		return fcCorpPoolService.getTreeTable(poolCode, poolName, status);
	}
	@Override
	public List<com.bytter.system.api.vo.FcCorpPool> listByPoolId(String poolId) {
		List<com.bytter.biz.pool.entity.FcCorpPool> list = fcCorpPoolService.listByPoolId(poolId);
		List<com.bytter.system.api.vo.FcCorpPool> listvo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.FcCorpPool.class);
		return listvo;
	}
    @Override
    public List<com.bytter.system.api.vo.FcCorpPool> corpIdByPoolId(String poolId) {
        List<com.bytter.biz.pool.entity.FcCorpPool> list = fcCorpPoolService.corpIdByPoolId(poolId);
        List<com.bytter.system.api.vo.FcCorpPool> listvo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.FcCorpPool.class);
        return listvo;
    }
	@Override
	public List<FcCorpPoolVO> getUserCorpPoolsVO(ICurrentUser user, String resource) {
		return fcCorpPoolService.getUserCorpPools(user,resource);
	}
	@Override
	public List<FcCorpPoolVO> getUserCorpPoolChildsVO(ICurrentUser user, String resource, String poolId) {
		return fcCorpPoolService.getUserCorpPoolChilds(user, resource, poolId);
	}
	@Override
	public FcCorpPoolVO getChildCorpByPoolIdVO(String poolId, String corpId) {
		return fcCorpPoolService.getChildCorpByPoolId(poolId, corpId);
	}
	@Override
	public com.bytter.system.api.vo.FcCorpPool getPoolInfo(String parentId, String corpId) {
		com.bytter.biz.pool.entity.FcCorpPool pool = fcCorpPoolService.getPoolInfo(parentId, corpId);
		com.bytter.system.api.vo.FcCorpPool fcPool = new com.bytter.system.api.vo.FcCorpPool();
		BeanUtils.copyProperties(pool, fcPool);
		return fcPool;
	}
	@Override
	public List<FcCorpPoolVO> getAllCorpPool() {
		return fcCorpPoolService.getAllCorpPool();
	}
	@Override
	public boolean vaildAcc(String corpId, ICurrentUser user) {
		return fcCorpPoolService.vaildAcc(corpId, user);
	}
	@Override
	public boolean fcAccUse(String poolId, String corpId, boolean isFilter) {
		return fcCorpPoolService.fcAccUse(poolId, corpId, isFilter);
	}
	@Override
	public boolean relevance(String poolId, String corpId) {
		return fcCorpPoolService.relevance(poolId, corpId);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		return fcCorpPoolService.verifyCorpDelete(corpId);
	}
	@Override
	public com.bytter.system.api.vo.FcCorpPool getFcCorpPool(String id) {
		com.bytter.system.api.vo.FcCorpPool fcCorpPool = new com.bytter.system.api.vo.FcCorpPool();
		com.bytter.biz.pool.entity.FcCorpPool pool = fcCorpPoolService.get(id);
		BeanUtils.copyProperties(pool, fcCorpPool);
		return fcCorpPool;
	}
	@Override
	public void deleteFcCorpPool(String id) {
		fcCorpPoolService.delete(id);
	}
    @Override
    public List<FcCorpPoolDTO> getFcCorpPoolByIds(List<String> ids){
        return fcCorpPoolService.getFcCorpPoolByIds(ids);
    }
    @Override
    public List<FcCorpPoolDTO> getFcCorpPoolByPoolIds(List<String> ids){
        return fcCorpPoolService.getFcCorpPoolByPoolIds(ids);
    }
    
    @Override
    public Map<String,String> getPoolNameByIds(List<String> ids){
        return fcCorpPoolService.getPoolNameByIds(ids);
    }
    @Override
    public void saveEntity(com.bytter.system.api.vo.FcCorpPool fcCorpPool) {
        com.bytter.biz.pool.entity.FcCorpPool pool = new com.bytter.biz.pool.entity.FcCorpPool();
        BeanUtils.copyProperties(fcCorpPool, pool);
        fcCorpPoolService.saveEntity(pool);
    }
}
