package com.bytter.olap.util;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加密和解密工具类
 * Created by chenlw on 2019/06/18
 */
public class AESUtils {

    /**
     * 编码格式
     */
    private static final String CHARSET_UTF8 = "UTF-8";

    /**
     * AES加密算法
     */
    private static final String KEY_AES = "AES";

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1PRNG";


    public static void main(String[] args) throws Exception {
        test1();
        //testDecrypt();
    }

    public static void test1() throws Exception{
        // Key为加密和解密的钥匙
        //final String KEY = "Bytter";
        final String KEY = "1234567812345678";
        String content = "Bytter";
        System.out.println("加密前：" + content);
        System.out.println("加密密钥和解密密钥：" + KEY);
        String encrypt = encrypt(content, KEY);
        System.out.println("加密后：" + encrypt);
        String decrypt = decrypt(encrypt, KEY);
        System.out.println("解密后：" + decrypt);
    }


    public static void testDecrypt() throws Exception{
        final String key = "Bytter";
        //加密前的内容为Bytter
        String content = "F217884172ECDE99FC3F24F184963D7A";
        //String content = "B28C4B0493144589D8D5D348CCF579BBAE3E90973F19F55583AD73FFEC301188B874F1F41B3EC3244C43928D62B7E2B938827A97CC0E1CB5F312A4671DE5203C36B67E905E26DB51FE9225FC601261FEE23DA9D956D93F0A221FF2D34AB99FB078BEEF18E12187CCDF0D98EA342280D8A8ABBCA9B7ACA0BA921667744DE5630131A14C03E068D1402956C533E2ACAE6A399AD38EE42A8D29E39FEB2E7844CCD62BD9E36B2ECAA16F796F1C6E139BD823E2D9DF39E26DAA9C5551B80212281D5BAF1DB557D7FD4E38AD51540BC32293370D1AA4F98127121E26185B47D57DF3F58243DCFF31E866AB3996FDC36243B2C137B01264716219AFD84846D4082D7E2B6ADEAE8A454B35EDCC05FA7C5BD827FD21842C4A2A83F34DF19FBD4E89ADFF94C6C719C2C1B592FD15B7A2A10D499C00AA2C7478915A6B9CA4B4AD13C41C4CC849364282F331F0596D3EB1B38B7452226ADEAE8A454B35EDCC05FA7C5BD827FD90FDEB651862D68DBB6F616D61182160BC1E87E093BE05564D90670142EAADEFC8A5BA9872D6195B739739317551094C87FDF80B8CEA40023A659A66FF9478866ADEAE8A454B35EDCC05FA7C5BD827FD8A05015414131850EACD3DFB2FA7FAC038B111F1017248F347F79C84D46F917564976C1DEE96BC9D03CF18C41EE77D46B4463FAE104726B1890416E5E89D2A11FFFE0EC19CE1CE38738F6158F4DBF981FAF95877C47E182114C257BCDB53E7153E863CCA6FA5920C94C17F962E61FFDC6C28A823F509D3FD388ECAD1AE67D84B7789A9135484CBC7738745FF5F28F58AAA9BD1ABF92325B9B09439C1127FDB7FE34D7EC54FB662C74C903D9D4C8B5C21A0B85A87DCE0B2306A1754B8993C099A89DDDEEFEBCA4AE7A788CD7FA4965326F776F21426D642BDC9518669F98C0B9580CCB1090ADB1070609F400049D43A481C0DEF9F893CDFEED9EAEE72D396F28BF04415B9C93EE43AA9D2B300DC9B6F5FCF903EF0ACE7EE6DC985BC92037A45FDE08627B9A0FAE8AE533919BAAB84D5B853D3B8A76B356203BF89DC06D6BE76701C0DEF9F893CDFEED9EAEE72D396F28BA0CD2E186C04E23593C8AD3FB49A42D5BF158341B8F700E574FA0444AE3210C432725128333B6E32DD0D2A5739AF6D2929C28D8E3F680E5282272AB45B7291E81C0DEF9F893CDFEED9EAEE72D396F28B22C2414838E0AAB46345DA0EAB337CB03C01C289A657971C28D2FC86E183113B4F27235B267FAF9881FAA4BB46F0227E9BDD9026C7FF1AD643CA2940D69B1B72478ECEDC3B1C2EF63D58D6D53A4062835FBD2678C498E835DE71622B9D877D1B00E02EF24AAD5DFCF5751C37279A690F65636DA7B2B932D4EFDA6C0E5DBC6CED98BCD297281F67DA96A1EF457D7A3F533E8CE923EBD690E77EAF6ED498F1B2825E66D27BDE4F45743CA1FC096EAF55B09A6691836129C719D86B7021E4FF8A35189C555540C326DA6AA6DBCCEB520C8AF7DC2535E7257A4DE0003AF71200911B36BC60C489BE8D7DA4F7C6915E04A06B4FAAA3A00C95E3E0A43FDCAF17ED09F8B404DB3FA5CF46439F2513A11435A86161979B895B32030A4554879B300039899868AC1859A3A1970E1D098A6B3C0ED8271B7ADA83DBFB94B48B19BEFF0649298760DD829C3F0ADB09C797B3A6B192095711EFB7E69C62233AF793F818378439EC01CE4D83FEA82C372176ED774D4EA4AD69400AB3DC0DD50CE8512C25C0886FB89A415E549BDFF869CF0A498CB95FA1418FA358C0EF66A441AACA03B6B153CA669D85C0E3D127829CFF7D9999EEDA48A4F40BCA3E8A4D0399CA03A4705A46CE060129BF8DC1E8F413EB8C9F04CD73721B1D935118DB177266E381B3E1BE054C345686953C1B929C4E91148EA04B6C0C4DBC8ECD7EA6001D42B560B7FD81A57F379766C1A3C90B2A9B8376A9210539C5684C002E7BBBC1A75C904D771B988CEC4D5EBB556C19221A9928272B94D27ABD07FF094B9489198CCB3B9C115467D214D4316C7AEB894B8D943B7E7DD5EA2445A7991A9EF504E94FABC3E1A3C28BEA05E742DEC33A709810DFC73549E232A2250BF30C637058380676247FD562E70631A16FE2A3DF28E923B2D4D9FDDF17E592E072618773C3C6072E8C1E4D3CDCCC4A25375066865CB576B749FD7FB2A2E1539DE70B975ECFAF41815CA0E4E4A0784304A7C61FC98C81EAD921BF7F64F670104B07A05239016C21A86F055CE011214B550B4A959D067CAE6598993A245BF91D3BA205080785508E15E5852F82FA9EFF5BC1AA34AC75B900156F4A2057B64F9C68AF46AC8AAC062D70E2F9DDC59072AE6E9644C1444AE960EBC44D7A1CA81B7A5A8537AE99886AA6F62C71C6BC06EAB32ADDE4B08D242BF6287E9AE1338BD9EDF3BEB852413E5149D165A6D292D00135DACDAA7D7A76BE8C9983887D36FE26048486DE8310676EDA51C4B7B0D345E3450F588B6498B00F6AF4E1007527B0967C1E296EA87131D887270ACAEE96BA35E2140A1577EDE3E9A3810A6A81C272FEB936EB53662B01D48BD5358C92626ED238EB28918ADCCF090883B70E17A7AB7DF3236A0686E9C32359B21C3F503BEFB32CC686349995C3F93948A51DDD2479B8C76CCD253A047F980AED69CF341D9D795941F9E19936772B67DB922AE4A55D65E618A2C1B55F42937F8033191D147672AB5634CB6DB0B1C49E3491E0B74428BC75";
        String decrypt = decrypt(content, key);
        System.out.println("解密后：" + decrypt);
    }

    /**
     * AES加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static String encrypt(String data, String key) throws Exception {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * AES解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     * @return
     */
    public static String decrypt(String data, String key) throws Exception {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data 待处理数据
     * @param key  密钥
     * @param mode 加解密mode
     * @return
     */
    private static String doAES(String data, String key, int mode) throws Exception {
        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }
            //判断是加密还是解密
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            // true 加密内容 false 解密内容
            if (encrypt) {
                content = data.getBytes(CHARSET_UTF8);
            } else {
                content = parseHexStr2Byte(data);
            }
            // 1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
            // 2.根据ecnodeRules规则初始化密钥生成器

            // 生成一个128位的随机源,根据传入的字节数组
            //kgen.init(128, new SecureRandom(key.getBytes()));
            //生成一个128位的随机源,根据传入的字节数组,防止linux下 随机生成key
            SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
            random.setSeed(key.getBytes(CHARSET_UTF8));
            kgen.init(128, random);


            // 3.产生原始对称密钥
            SecretKey secretKey = kgen.generateKey();
            // 4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            // 5.根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            // 6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(KEY_AES);// 创建密码器
            // 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, keySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                // 将二进制转换成16进制
                return parseByte2HexStr(result);
            } else {
                return new String(result, CHARSET_UTF8);
            }
        } catch (Exception e) {
            throw new Exception("AES 密文处理异常:"+e.getMessage());
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


}
