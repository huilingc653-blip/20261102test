package com.bytter.biz.bdlink.api.service;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import java.util.List;
public interface IBisBankReceiptDtlHisService {
    void saveEntity(BisBankReceiptDtlVO vo);
    DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user);

    DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input,String commonSqlStr, ICurrentUser user);

    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    BisBankReceiptDtlHisVO getByIdUnion(String id);
    /**
     * 更新打印次数
     *
     * @param id
     */
    void updatePrint(String id);
    List<BisBankReceiptDtlHisVO> listByIds(String ids);
}
