package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.dao.ArticleDao;
import com.bytter.system.dao.RoleDao;
import com.bytter.system.entity.Article;
import com.bytter.system.entity.Role;
import com.bytter.system.entity.SysAttachment;
import com.bytter.system.service.ArticleService;
import com.bytter.system.service.RoleService;
import com.bytter.system.service.SysAttachmentService;
import com.bytter.system.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import java.util.*;
/**
 * 政策法规、重要信息控制器
 *
 * @author zhoul
 * @since 2020-04-28
 */
@Controller
@RequestMapping("/sys/article")
public class ArticleController extends BaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @GetMapping("/list")
    public void list() {
    }
    @GetMapping("/review")
    public String review(Model model) {
        model.addAttribute("review", 1);
        return "sys/article/list";
    }
    @PostMapping("/list")
    @ResponseBody
    public DataTablesOutput list(@RequestBody DataTablesInput dataTablesInput) {
        Object home = dataTablesInput.getFormdata().get("home");
        if (home != null) {
            ICurrentUser currentUser = getCurrentUser();
            List<String> roleIdsByUserId = roleDao.getRoleIdsByUserId(currentUser.getId());
            dataTablesInput.getFormdata().put("roleIds", roleIdsByUserId);
        }
        return articleDao.getDataTables(dataTablesInput);
    }
    /**
     * 复核
     * 通过：状态由2待复核变更为3未发布
     * 打回：状态有2待复核变更为-1打回
     *
     * @param list
     * @return
     */
    @PostMapping("/confirm")
    @ResponseBody
    public Object confirm(@RequestParam("ids[]") List<String> list, Integer status) {
        if (status != -1 && status != 3) {
            throw new ParameterException(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        }
        for (String id : list) {
            articleService.updateStatus(id, status, getCurrentUser().getId());
        }
        return success();
    }
    @GetMapping("/cancelTop")
    @ResponseBody
    public Object cancelTop(String id) {
        articleService.cancelTop(id, getCurrentUser().getId());
        return success();
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public Object batchSubmit(@RequestParam("ids[]") List<String> ids) {
        articleService.updateStatus(ids, 2, getCurrentUser().getId());
        return success();
    }
    /**
     * 发布
     * 状态由3未发布变更为1已发布
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/deploy")
    @ResponseBody
    public Object deploy(String id) {
        ICurrentUser currentUser = getCurrentUser();
        articleService.updateStatus(id, 1, currentUser.getId());
        return success();
    }
    @GetMapping("/homeData")
    @ResponseBody
    public Object homeData() {
        ICurrentUser currentUser = getCurrentUser();
        List<String> roleIdsByUserId = roleDao.getRoleIdsByUserId(currentUser.getId());
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        List<ArticleVO> notice = articleService.getArticleByType(1, roleIdsByUserId, currentUser.getId());
        List<ArticleVO> law = articleService.getArticleByType(2, roleIdsByUserId, currentUser.getId());
        //界面无数据时显示空盒子
        if (EmptyUtils.isNotEmpty(notice)) {
            objectObjectHashMap.put("notice", notice);
        } else {
            objectObjectHashMap.put("notice", null);
        }
        if (EmptyUtils.isNotEmpty(law)) {
            objectObjectHashMap.put("law", law);
        } else {
            objectObjectHashMap.put("law", null);
        }
        return objectObjectHashMap;
    }
    @GetMapping("/view")
    public void view(String id, String flag, Model model) {
        ArticleVO article = articleService.getArticleVO(id);
        if (EmptyUtils.isNotEmpty(flag)) {
            articleService.readArticle(id, getUserId());//处理消息已读
        }
        article.setContext(HtmlUtils.htmlUnescape(article.getContext()));
        List<SysAttachment> attachments = sysAttachmentService.getAttachmentByGroupId(article.getAttachmentId());
        List<Map<String, Object>> maps = new ArrayList<>();
        for (SysAttachment attachment : attachments) {
            Map<String, Object> map = new HashMap<>(3);
            map.put("id", attachment.getId());
            map.put("name", attachment.getName());
            map.put("fileSize", (attachment.getFileSize().doubleValue() / 1024));
            maps.add(map);
        }
        model.addAttribute("article", article);
        model.addAttribute("attachments", maps);
    }
    @GetMapping("/add")
    public String add(Model model) {
        List<Role> roles = roleService.listAll();
        model.addAttribute("roles", roles);
        return "sys/article/form";
    }
    @PostMapping("/save")
    @ResponseBody
    public Object save(Article article, @RequestParam(value = "roleList[]", required = false) List<String> roleList) {
        ICurrentUser currentUser = getCurrentUser();
        String userId = currentUser.getId();
        article.setCreateBy(userId);
        articleService.save(article, roleList);
        return success();
    }
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        ArticleVO article = articleService.getArticleVO(id);
        article.setContext(HtmlUtils.htmlUnescape(article.getContext()));
        model.addAttribute("article", article);
        List<Role> roles = roleService.listAll();
        model.addAttribute("roles", roles);
        List<String> articleRoles = articleDao.getArticleRoles(id);
        model.addAttribute("checkRoles", articleRoles);
        model.addAttribute("editView", false);
        return "sys/article/form";
    }
    @GetMapping("/editView")
    public String editView(String id, Model model) {
        ArticleVO article = articleService.getArticleVO(id);
        article.setContext(HtmlUtils.htmlUnescape(article.getContext()));
        model.addAttribute("article", article);
        List<Role> roles = roleService.listAll();
        model.addAttribute("roles", roles);
        List<String> articleRoles = articleDao.getArticleRoles(id);
        model.addAttribute("checkRoles", articleRoles);
        model.addAttribute("editView", true);
        return "sys/article/form";
    }
    @PostMapping("/update")
    @ResponseBody
    public Object update(Article article, @RequestParam(value = "roleList[]", required = false) List<String> roleList) {
        article.setCreateBy(getCurrentUser().getId());
        article.setCreateDate(new Date());
        articleService.update(article, roleList);
        return success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam("ids[]") List<String> list) {
        for (String id : list) {
            articleService.delete(id);
        }
        return success();
    }
    /**
     * 获取公告信息的统计数量
     *
     * @return
     */
    @GetMapping("/getArticleCount")
    @ResponseBody
    public Object getArticleCount() {
        Integer count = articleService.getArticleCountByUserId(this.getCurrentUser().getId());
        return count.toString();
    }
}
