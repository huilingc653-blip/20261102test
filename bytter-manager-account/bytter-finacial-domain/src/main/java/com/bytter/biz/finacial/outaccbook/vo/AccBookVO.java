package com.bytter.biz.finacial.outaccbook.vo;
import java.io.Serializable;
import java.util.List;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtl;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
public class AccBookVO implements Serializable {
	private FcOutAccbook book;
	private List<FcOutAccbookDtl> dtls;
	
	public FcOutAccbook getBook() {
		return book;
	}
	public void setBook(FcOutAccbook book) {
		this.book = book;
	}
	public List<FcOutAccbookDtl> getDtls() {
		return dtls;
	}
	public void setDtls(List<FcOutAccbookDtl> dtls) {
		this.dtls = dtls;
	}
	
	
}
