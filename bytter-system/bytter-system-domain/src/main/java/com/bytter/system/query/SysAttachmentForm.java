package com.bytter.system.query;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.StringUtils;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@Data
public class SysAttachmentForm {
    private String id;
    private String pid;
    private String name;
    private String type;
    private Integer size;
    /**
     * 分片相关操作 以前存在留着兼容
     */
    private Integer chunks = 0;
    private Integer chunk = 0;
    /**
     * 文件列表
     */
    private List<MultipartFile> file;
    public String getPid() {
        return StringUtils.isBlank(this.pid) ? StringUtils.getUUId() : this.pid;
    }
    /**
     * 获取文件后缀
     */
    public String getSuffix() {
        return FileUtils.getExtend(this.name);
    }
    /**
     * 获取文件名
     */
    public String getFileName() {
        return FileUtils.getFilePrefix(this.name);
    }
}
