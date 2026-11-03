package com.bytter.biz.finacial.outaccbook.vo;
import java.io.Serializable;
import java.util.List;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtlHis;
import com.bytter.biz.finacial.outaccbook.entity.FcPreOutAccbook;
public class AccBookHisVO implements Serializable {
	
	private FcPreOutAccbook bookHis;
	
	private List<FcOutAccbookDtlHis> dtlsHis;
	public FcPreOutAccbook getBookHis() {
		return bookHis;
	}
	public void setBookHis(FcPreOutAccbook bookHis) {
		this.bookHis = bookHis;
	}
	public List<FcOutAccbookDtlHis> getDtlsHis() {
		return dtlsHis;
	}
	public void setDtlsHis(List<FcOutAccbookDtlHis> dtlsHis) {
		this.dtlsHis = dtlsHis;
	}
	
	
}
