
package com.bytter.core.common.hibernate.id;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDGenerationStrategy;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.jboss.logging.Logger;
import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;
/**
 * 使用Hibernate4 推荐的uuid2主键生成策略，原本使用的是36位长度，改造为32位 周亮 2017-06-14
 */
public class UUIDGenerator implements IdentifierGenerator, Configurable {
    public static final String UUID_GEN_STRATEGY = "uuid_gen_strategy";
    public static final String UUID_GEN_STRATEGY_CLASS = "uuid_gen_strategy_class";
    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
            UUIDGenerator.class.getName());
    private UUIDGenerationStrategy strategy;
    private UUIDTypeDescriptor.ValueTransformer valueTransformer;
    public static UUIDGenerator buildSessionFactoryUniqueIdentifierGenerator() {
        final UUIDGenerator generator = new UUIDGenerator();
        generator.strategy = StandardRandomStrategy.INSTANCE;
        generator.valueTransformer = UUIDTypeDescriptor.ToStringTransformer.INSTANCE;
        return generator;
    }
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        if (object != null && object instanceof IdEntity) {
            IdEntity entity = (IdEntity) object;
            if (StringUtils.isNotEmpty(entity.getId())) {
                return entity.getId();
            }
        }
        Serializable uuidSerializable = valueTransformer.transform(strategy.generateUUID(session));
        return uuidSerializable.toString().replace("-", "");
    }
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        // check first for the strategy instance
        strategy = (UUIDGenerationStrategy) properties.get(UUID_GEN_STRATEGY);
        if (strategy == null) {
            // next check for the strategy class
            final String strategyClassName = properties.getProperty(UUID_GEN_STRATEGY_CLASS);
            if (strategyClassName != null) {
                try {
                    final Class strategyClass = ReflectHelper.classForName(strategyClassName);
                    try {
                        strategy = (UUIDGenerationStrategy) strategyClass.newInstance();
                    } catch (Exception ignore) {
                        LOG.unableToInstantiateUuidGenerationStrategy(ignore);
                    }
                } catch (ClassNotFoundException ignore) {
                    LOG.unableToLocateUuidGenerationStrategy(strategyClassName);
                }
            }
        }
        if (strategy == null) {
            // lastly use the standard random generator
            strategy = StandardRandomStrategy.INSTANCE;
        }
        if (UUID.class.isAssignableFrom(type.getReturnedClass())) {
            valueTransformer = UUIDTypeDescriptor.PassThroughTransformer.INSTANCE;
        } else if (String.class.isAssignableFrom(type.getReturnedClass())) {
            valueTransformer = UUIDTypeDescriptor.ToStringTransformer.INSTANCE;
        } else if (byte[].class.isAssignableFrom(type.getReturnedClass())) {
            valueTransformer = UUIDTypeDescriptor.ToBytesTransformer.INSTANCE;
        } else {
            throw new HibernateException(
                    "Unanticipated return type [" + type.getReturnedClass().getName() + "] for UUID conversion");
        }
    }
}
