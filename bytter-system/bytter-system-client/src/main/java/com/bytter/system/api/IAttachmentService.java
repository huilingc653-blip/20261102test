package com.bytter.system.api;

import com.bytter.system.api.dto.AttachmentDTO;

import java.util.List;
/**
 * 上次附件接口类
 * <b>周健 </b>
 * Created by zhouj on 2017/5/31.
 */
public interface IAttachmentService {

    AttachmentDTO previewAttachmentFromPath(String id,String realPath,StringBuffer requestURL);

    List<AttachmentDTO> findAttachmentByIds(List<String> fileIds);

    /**
     * 根据组id查询附件列表
     *
     * @param groudId
     * @return
     */
    List<AttachmentDTO> findAttachmentByGroupId(String groudId);
    /**
     * 合并附件 组合一个
     *
     * @param ids
     * @return
     */
    String mergeGroupIds(List<String> ids);
    /**
     * 通过附件ID获取附件二进制数据
     *
     * @param id
     * @return
     */
    byte[] getAttachmentDataByid(String id);
    /**
     * 根据组fileId查询文件信息
     *
     * @param fileId
     * @return
     */
    AttachmentDTO findAttachmentById(String fileId);
}
