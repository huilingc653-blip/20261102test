package com.bytter.basic.service;
import com.bytter.basic.dao.BisBankBranchDao;
import com.bytter.basic.entity.BisBankBranch;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 办理机构表
 * @date 2022-12-12 19:06:25
 */
@Service("bisBankBranchServiceBase")
@Transactional
public class BisBankBranchService extends BaseService<BisBankBranch, String> {
    @Autowired
    private BisBankBranchDao bisBankBranchDao;
    @Override
    public BaseDao<BisBankBranch, String> getEntityDao() {
        return bisBankBranchDao;
    }
    /**
     * 获取有效的办理机构信息
     *
     * @return
     */
    public List<BisBankBranch> listValid() {
        return bisBankBranchDao.listBy("status", CommonConstant.YES);
    }
}
