package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.dao.ArticleDao;
import com.bytter.system.dao.UserDao;
import com.bytter.system.entity.Article;
import com.bytter.system.entity.User;
import com.bytter.system.vo.ArticleVO;
import com.bytter.system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 政策法规、重要公告 Service
 *
 * @author zhoul
 * @since 2020-05-14
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ArticleService extends BaseService<Article, String> {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public ArticleVO getArticleVO(String id) {
        Article article = get(id, true);
        ArticleVO articleVO = BeanUtils.copyNewInstanceProperties(article, ArticleVO.class);
        User user = userDao.get(articleVO.getCreateBy(), true);
        articleVO.setCreateByUserName(user.getUsername());
        return articleVO;
    }

    public void save(Article article, List<String> roleList) {
        article.setCreateDate(new Date());
        //如果不是暂存的数据，则标记为“待复核”数据
        article.setStatus(article.getStatus() == null || article.getStatus() != 0 ? 2 : article.getStatus());
        saveEntity(article);
        articleDao.saveArticleRole(article.getId(), roleList);
    }

    public void update(Article article, List<String> roleList) {
        Article dbArticle = get(article.getId(), true);
        BeanUtils.copyNotNullProperties(article, dbArticle);
        articleDao.deleteArticleRole(article.getId());
        articleDao.saveArticleRole(article.getId(), roleList);
        updateEntity(dbArticle);
    }

    public void cancelTop(String id, String opUser) {
        articleDao.cancelTop(id, opUser);
    }

    public void updateStatus(List<String> ids, Integer status, String opUser) {
        for (String id : ids) {
            articleDao.updateStatus(id, status, opUser);
        }
    }

    public void updateStatus(String id, Integer status, String opUser) {
        articleDao.updateStatus(id, status, opUser);

        //发布才生成用户推送信息
        if (1 == status) {
            //推送指定用户才能看到公告消息
            Article article = get(id, true);
            Integer receiverType = article.getReceiverType();
            List<String> userList = new ArrayList<>();
            if (new Integer("1").equals(receiverType)) {//所有角色
                //获取正常状态的用户
                List<UserVO> userDTOList = userService.listAllUserByUnDelete();
                for (UserVO userVO : userDTOList) {
                    userList.add(userVO.getId());
                }
            } else {//指定角色
                List<String> roleList = articleDao.getArticleRoles(id);
                userList = userService.listUserIdsByRoleId(roleList);
            }
            //用户去重
            List<String> userIds = new ArrayList<>();
            for (String userId : userList) {
                if (!userIds.contains(userId)) {
                    userIds.add(userId);
                }
            }

            if (EmptyUtils.isNotEmpty(userList)) {
                articleDao.saveArticleUser(id, userIds);
            }
        }
    }

    @Override
    public void delete(String articleId) {
        articleDao.deleteArticleRole(articleId);
        articleDao.delete(articleId);
    }


    @Override
    public BaseDao<Article, String> getEntityDao() {
        return articleDao;
    }

    /**
     * 获取公告信息的统计数量
     *
     * @param userId
     * @return
     */
    public Integer getArticleCountByUserId(String userId) {
        return articleDao.getArticleCountByUserId(userId);
    }

    /**
     * 处理公告信息的已读
     *
     * @param articleId
     * @param opUser
     * @return
     */
    public void readArticle(String articleId, String opUser) {
        List<String> idList = articleDao.getArticleUsers(articleId, opUser);
        if (EmptyUtils.isNotEmpty(idList)) {
            List<String> userList = new ArrayList<>();
            userList.add(opUser);

            int count = articleDao.deleteArticleUser(articleId, opUser);
            if (count > 0) {
                articleDao.moveArticleUser(articleId, userList);
            }
        }
    }

    public List<ArticleVO> getArticleByType(Integer type, List<String> roleIds, String userId) {
        List<ArticleVO> artList = articleDao.getArticleByType(type, roleIds);
        List<String> artIdList = articleDao.getArticleByUserType(type, userId);
        Map<String, String> artMap = new HashMap<>();
        for (String artId : artIdList) {
            artMap.put(artId, artId);
        }
        for (ArticleVO articleVO : artList) {
            String id = articleVO.getId();
            String artId = artMap.get(id);
            if (EmptyUtils.isNotEmpty(artId)) {
                articleVO.setReadSign("0");
            } else {
                articleVO.setReadSign("1");
            }
        }
        return artList;
    }
}
