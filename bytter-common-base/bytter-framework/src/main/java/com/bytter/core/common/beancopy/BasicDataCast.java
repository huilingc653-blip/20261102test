package com.bytter.core.common.beancopy;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
/**
 * 基础数据类型转换，在BeanMapEmitter使用cglib生成的代码中使用该类
 *
 * @author zhoul 2019-06-20
 * @author zhoul update 2020-07-28 新增blob类型转换
 */
public abstract class BasicDataCast {
    public static Object cast(Object source, Class target) {
        if (source == null) {
            return null;
        }
        // 如果类型一致，则直接返回
        if (source.getClass() == target) {
            return source;
        }
        if (source instanceof Character) {
            Character character = (Character) source;
            if (target == String.class) {
                return character.toString();
            }
        } else if (source instanceof CharSequence) {
            CharSequence character = (CharSequence) source;
            if (target == String.class) {
                return character.toString();
            }
        } else if (source instanceof Number) {
            Number number = (Number) source;
            if (target == BigDecimal.class) {
                return BigDecimal.valueOf(number.doubleValue());
            } else if (target == BigInteger.class) {
                return BigInteger.valueOf(number.longValue());
            } else if (target == Long.class) {
                return number.longValue();
            } else if (target == Integer.class) {
                return number.intValue();
            } else if (target == Double.class) {
                return number.doubleValue();
            } else if (target == Float.class) {
                return number.floatValue();
            } else if (target == Short.class) {
                return number.shortValue();
            } else if (target == Byte.class) {
                return number.byteValue();
            } else if (target == String.class) {
                return number.toString();
            }
        } else if (source instanceof Blob) {
            Blob blob = (Blob) source;
            if (target == byte[].class) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
                        InputStream in = blob.getBinaryStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                    return out.toByteArray();
                } catch (IOException | SQLException e) {
                    return null;
                }
            }
        }
        return source;
    }
}
