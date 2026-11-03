package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: SYS_ATTACHMENT
 * @date 2017-06-01 16:22:46
 */
@Entity
@Table(name = "SYS_ATTACHMENT", schema = "")
@SuppressWarnings("serial")
public class SysAttachment extends IdEntity implements java.io.Serializable {
    /**
     * 组别ID
     */
    private String groupId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 附件名称
     */
    @Excel(name = "附件名称")
    //@BtCompositeSearch(fieldName = "name")
    private String name;
    /**
     * 附件别名
     */
    @Excel(name = "附件别名")
    private String reName;
    /**
     * 附件类型
     */
    @Excel(name = "附件类型")
    private String type;
    /**
     * 附件后缀
     */
    private String suffix;
    /**
     * 附件大小
     */
    private BigDecimal fileSize;
    /**
     * 附件地址
     */
    private String path;
    /**
     * 附件描述
     */
    private String description;
    /**
     * 附件分类
     */
    private String category;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    public SysAttachment() {
    }
    public SysAttachment(String groupId, String fileName, String newFileName, String type, String suffix, BigDecimal size, String path, String categoryName, ShiroUser user) {
        super();
        this.groupId = groupId;
        this.userId = user.getId();
        this.name = fileName;
        this.reName = newFileName;
        this.type = type;
        this.suffix = suffix;
        this.fileSize = size;
        this.path = path;
        this.category = categoryName;
        this.status = 1;
        this.createBy = user.getUsername();
        this.createDate = DateUtils.getDate();
    }
    @Column(name = "GROUP_ID", length = 32)
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  用户ID
     */
    @Column(name = "USER_ID", nullable = true, length = 32)
    public String getUserId() {
        return this.userId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件名称
     */
    @Column(name = "NAME", nullable = true, length = 100)
    public String getName() {
        return this.name;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件别名
     */
    @Column(name = "RE_NAME", nullable = true, length = 50)
    public String getReName() {
        return reName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件别名
     */
    public void setReName(String reName) {
        this.reName = reName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件类型
     */
    @Column(name = "TYPE", nullable = true, length = 20)
    public String getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件类型
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件后缀
     */
    @Column(name = "SUFFIX", nullable = true, length = 20)
    public String getSuffix() {
        return this.suffix;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件后缀
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  附件大小
     */
    @Column(name = "FILE_SIZE", nullable = true, scale = 2, length = 10)
    public BigDecimal getFileSize() {
        return this.fileSize;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  附件大小
     */
    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件地址
     */
    @Column(name = "PATH", nullable = true, length = 500)
    public String getPath() {
        return this.path;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件地址
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件描述
     */
    @Column(name = "DESCRIPTION", nullable = true, length = 225)
    public String getDescription() {
        return this.description;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件分类
     */
    @Column(name = "CATEGORY", nullable = true, length = 30)
    public String getCategory() {
        return this.category;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件分类
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */
    @Column(name = "STATUS", nullable = true, length = 2)
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人
     */
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public SysAttachment appendOri(MultipartFile oriFile) {
        String originalFilename = oriFile.getOriginalFilename();
        this.setFileSize(BigDecimal.valueOf(oriFile.getSize()));
        this.setName(FileUtils.getFilePrefix(originalFilename));
        this.setSuffix(FileUtils.getExtend(originalFilename));
        this.setType(this.getSuffix());
        return this;
    }
    public SysAttachment appendDest(String path) {
        this.setPath(path);
        int begin = path.lastIndexOf(File.separator);
        int end = path.lastIndexOf(CommonConstant.DOT);
        this.setReName(path.substring(begin + 1, end));
        return this;
    }
    public static SysAttachment create(String userId,String groupId,AttachmentCategoryEnum modeName) {
        SysAttachment attachment = new SysAttachment();
        attachment.setId(StringUtils.getUUId());
        attachment.setGroupId(groupId);
        attachment.setCategory(modeName.getName());
        attachment.setUserId(userId);
        attachment.setCreateBy(userId);
        attachment.setCreateDate(new Date());
        attachment.setStatus(1);
        return attachment;
    }
}
