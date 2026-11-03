package com.bytter.credit.api;

import java.util.List;
import com.bytter.credit.vo.RatUrationCurVO;

public interface IRatBkrationReliefDetailService {
	public List<RatUrationCurVO> queryRationReliefFreeze(List<String> ratCodes);
}
