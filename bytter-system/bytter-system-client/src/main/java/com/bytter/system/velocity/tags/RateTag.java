package com.bytter.system.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.velocity.tools.config.DefaultKey;
import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * 汇率标签
 * Created by zhoul on 2017/6/14.
 */
@DefaultKey("rateTag")
public class RateTag {
	static final String RATE = "rate";
	private final  IConfigService configService;
	public RateTag() {
		this.configService = ApplicationContextUtil.getContext().getBean("configServiceI", IConfigService.class);;
	}
	public String show(Object number) {
		return "";
	}
	public String show(BigDecimal number) {
		if (number == null) {
			return "";
		}
		return show(number.doubleValue());
	}
	/**
	 * 将字符串转为小写，主要提供Velocity使用
	 *
	 * @param number
	 * @return
	 */
	public String show(Double number) {
		if (number == null) {
			return "";
		}
		Integer rate = configService.getConfig(ConfigConstant.SYS, RATE).toInteger();
		String formatNumber;
		if (rate.equals(1)) {
			DecimalFormat df = new DecimalFormat("0.000");
			formatNumber = df.format(number * 1000) + "‰";
		} else {
			DecimalFormat df = new DecimalFormat("0.0000");
			formatNumber = df.format(number * 100) + "%";
		}
		return formatNumber;
	}
	public String show(Object number, Object decimal) {
		return "";
	}
	public String show(BigDecimal number, Integer decimal) {
		if (number == null) {
			return "";
		}
		return show(number.doubleValue(), decimal);
	}
	/**
	 * 将字符串转为小写，主要提供Velocity使用
	 *
	 * @param number
	 * @param decimal 小数位数， 默认为2， 不足补0
	 * @return
	 * @author zuoy 2017-10-24
	 */
	public String show(Double number, Integer decimal) {
		if (number == null) {
			return "";
		}
		StringBuilder pattern = new StringBuilder("0.");
		for (int i = 0; i < decimal; i++) {
			pattern.append("0");
		}
		Integer rate = configService.getConfig(ConfigConstant.SYS, RATE).toInteger();
		DecimalFormat df = new DecimalFormat(pattern.toString());
		if (rate.equals(1)) {
			return df.format(number * 1000) + "‰";
		}
		return df.format(number * 100) + "%";
	}
}
