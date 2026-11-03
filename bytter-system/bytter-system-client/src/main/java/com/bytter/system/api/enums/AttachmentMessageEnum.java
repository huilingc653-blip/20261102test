package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 上传附件异常提醒信息
 * Created by zhouj on 2019/5/31.
 */
public enum AttachmentMessageEnum implements IMessageEnum {
    /**
     * 附件大小限制异常
     **/
    ATTACHMENT_MESSAGE_ERROR_MAX_SIZE("981001"),
    /**
     * 附件格式异常
     **/
    ATTACHMENT_MESSAGE_ERROR_FORMAT("981002"),
    /**
     * 附件上传异常
     **/
    ATTACHMENT_MESSAGE_ERROR_UPLOAD("981003"),
    /**
     * ftp地址有误
     **/
    FTP_UNKNOW_HOST("981004"),
    /**
     * ftp连接超时
     **/
    FTP_CONNECT_TIMEOUT("981005"),
    /**
     * 未连接到FTP，用户名或密码错误.
     */
    FTP_USERNAME_PASSWORD_ERROR("981006"),
    /**
     * ftp连接失败.
     */
    FTP_CONNECT_ERROR("981007"),
    /**
     * Sftp地址有误
     **/
    SFTP_UNKNOW_HOST("981008"),
    /**
     * Sftp连接超时
     **/
    SFTP_CONNECT_TIMEOUT("981009"),
    /**
     * 未连接到SFTP，用户名或密码错误.
     */
    SFTP_USERNAME_PASSWORD_ERROR("981010"),
    /**
     * Sftp连接失败.
     */
    SFTP_CONNECT_ERROR("981007");
    private String code;
    AttachmentMessageEnum(String code) {
        this.code = code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
