package com.bytter.credit.api;

import java.util.List;
import com.bytter.credit.vo.RatUrationCurVO;

public interface IRatBkrationAddDetailService {
	 public List<RatUrationCurVO> queryRationSubAddFreeze(List<String> ratCodes);
}
