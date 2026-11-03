package com.bytter.biz.finacial.interest.vo;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
public class FcInterestPrintVO implements Serializable {
    /**
     * 直属网点单位
     */
    private CorpDTO netCorp;
    /**
     * 会计科目
     */
    private FcSubjectDTO fcSubjectDTO;
    private ListFcAccInterestVO listFcAccInterestVO;
    private List<ListFcAccInterestVO> listFcAccInterestVOList;
    /**
     * 利息合计
     */
    private BigDecimal interestSum;
    public CorpDTO getNetCorp() {
        return netCorp;
    }
    public void setNetCorp(CorpDTO netCorp) {
        this.netCorp = netCorp;
    }
    public FcSubjectDTO getFcSubjectDTO() {
        return fcSubjectDTO;
    }
    public void setFcSubjectDTO(FcSubjectDTO fcSubjectDTO) {
        this.fcSubjectDTO = fcSubjectDTO;
    }
    public ListFcAccInterestVO getListFcAccInterestVO() {
        return listFcAccInterestVO;
    }
    public void setListFcAccInterestVO(ListFcAccInterestVO listFcAccInterestVO) {
        this.listFcAccInterestVO = listFcAccInterestVO;
    }
    public List<ListFcAccInterestVO> getListFcAccInterestVOList() {
        return listFcAccInterestVOList;
    }
    public void setListFcAccInterestVOList(List<ListFcAccInterestVO> listFcAccInterestVOList) {
        this.listFcAccInterestVOList = listFcAccInterestVOList;
    }
    public BigDecimal getInterestSum() {
        return interestSum;
    }
    public void setInterestSum(BigDecimal interestSum) {
        this.interestSum = interestSum;
    }
}
