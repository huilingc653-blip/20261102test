package com.bytter.system.api.dto;
import java.math.BigDecimal;
/**
 * Created by zhouj on 2017/5/31.
 */
public class AttachmentDTO implements java.io.Serializable {
    /**
     * 附件id
     **/
    private String id;
    /**
     * 组别id
     **/
    private String groupId;
    /**
     * 附件名称
     */
    private String name;
    /**
     * 附件别名
     */
    private String reName;
    /**
     * 附件类型
     */
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
     * 附件地址(非绝对路径)
     */
    private String filePath;


    /**
     * 文件预览地址
     */
    private String previewPath;

    /** 预览附件临时路径*/
    private java.lang.String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getReName() {
        return reName;
    }
    public void setReName(String reName) {
        this.reName = reName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public BigDecimal getFileSize() {
        return fileSize;
    }
    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }
}
