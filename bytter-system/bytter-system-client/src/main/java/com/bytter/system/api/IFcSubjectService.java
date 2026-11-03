package com.bytter.system.api;
import com.bytter.system.api.dto.FcSubjectDTO;
import java.util.List;
import java.util.Map;
/**
 * @author YonJar
 * @date 2023/4/29.
 * @description
 */
public interface IFcSubjectService {
    /**
     * @param ids
     * @return
     */
    List<FcSubjectDTO> getSubjectByIds(List<String> ids);
    void update(FcSubjectDTO fcSubjectDTO, String loginName);
    /**
     * 根据科目类型查询id的全部子集包含自己
     *
     * @param id
     */
    public List<String> getSubsetBySubjectSort(String id);
    /**
     * 根据科目类型查询id的全部子集包含自己
     *
     * @param id
     * @param subjectSort 科目类型
     */
    List<String> getSubsetBySubjectSort(String id, String subjectSort);
    /**
     * 根据ID查询会计科目DTO对象
     * @param id 科目id
     * @return FcSubjectDTO对象
     */
    FcSubjectDTO findSubjectById(String id);
    /**
     * 根据Code查询会计科目DTO对象
     * @param subjectCode 科目编号
     * @return FcSubjectDTO对象
     */
    FcSubjectDTO findSubjectByCode(String subjectCode);
    /**
     * 根据系统科目和开户种类查询
     * @param sysSubjcetCode 系统会计科目编号
     * @param accType 账号类型
     */
    FcSubjectDTO findSubjectByAccTypeSubCode(String sysSubjcetCode,String accType);
    /**
     *根据系统科目查询
     * @param sysSubjcetCode 系统科目代码
     * @param valid 有效标识 FinacialConstant.VALID_TRUE or FinacialConstant.VALID_FALSE or null
     * @return FcSubjectDTO对象集合
     */
    List<FcSubjectDTO> findSubjectBySysCode(String sysSubjcetCode,String valid);
    /**
     * 根据开户种类查询
     * @param accType 开户种类代码
     * @param valid 有效标识 FinacialConstant.VALID_TRUE or FinacialConstant.VALID_FALSE or null
     * @return FcSubjectDTO对象集合
     */
    List<FcSubjectDTO> findSubjectByAccType(String accType,String valid);
    /**
     * 根据科目类别查询
     * @param sort 科目类别代码
     * @param valid 有效标识 FinacialConstant.VALID_TRUE or FinacialConstant.VALID_FALSE or null
     * @return FcSubjectDTO对象集合
     */
    List<FcSubjectDTO> findSubjectBySort(String sort,String valid);
    /**查询二级科目*/
    public List<FcSubjectDTO> findSubjectByFatherId();
    /**
     * 根据 subjectsCodes , 查询 fc_subject 表，返回 subjectName; k = subjectCode, v = subjectName
     * @param subjectCodes
     * @return
     */
    Map<String,String> getSubjectNameBySubjectCode(List<String> subjectCodes);
    /**
     * 参考
     * StringBuilder sql = new StringBuilder();
     * 		sql.append("select * from Fc_Subject where valid_Sign='1' ");
     * 		Map<String, String> values = null;
     * 		sql.append(" order by subject_Code");
     * 		return listBySQLAliasToBean(sql.toString(), values, FcSubjectVo.class);
     * @return
     */
    List<FcSubjectDTO> listValidSubject();
    /**
     * 根据 id ，查询 fc_subject ，返回 subjcetName
     * @param id
     * @return
     */
    String getSubjectNameById(String id);

    boolean checkCashSubject(List<String> subIds);
}
