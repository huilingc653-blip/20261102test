package com.bytter.system.entity.id;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.bytter.system.entity.BisAccBal;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
/**
 * @author zhoul
 * @Description 银行帐号余额主键生成策略 适配银行帐号接口
 * @date 2017/11/2.
 */
public class AccountBalanceIdGenerator implements IdentifierGenerator {
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public String toMD5(String str) {
        byte[] digesta = null;
        StringBuilder md5 = new StringBuilder();
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(str.getBytes());
            digesta = alga.digest();
            String[] digit = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
            for (int i = 0; i < digesta.length; i++) {
                md5.append(digit[(digesta[i] & 0XF0) / 16]);
                md5.append(digit[digesta[i] & 0X0F]);
            }
        } catch (Exception e) {
        }
        return md5.toString();
    }
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}
//    @Override
//    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
//        if (object instanceof BisAccBal) {
//            //帐号(bankAcc) + 时间balDate(yyyyMMdd) + 币别Id(curId)
//            BisAccBal accountBalance = (BisAccBal) object;
//            StringBuilder str = new StringBuilder();
//            str.append(accountBalance.getBankAcc()).append(format.format(accountBalance.getBalDate())).append(accountBalance.getCurId());
//            return toMD5(str.toString());
//        } else {
//            throw new HibernateException(this.getClass() + "主键生成策略只适用" + BisAccBal.class);
//        }
//    }
}
