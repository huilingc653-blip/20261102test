package com.bytter.core.sequence;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.sequence.entity.MultiTable;
import com.bytter.core.sequence.enums.SequenceDateEnum;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.sequence.enums.SequenceTypeEnum;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
import com.bytter.core.sequence.interfaces.ISequenceService;
import com.bytter.core.sequence.interfaces.impl.DefaultSequenceHolder;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.MD5Util;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * @author zhoul
 * @date 2017/5/31
 * 唯一流水号生成类<br>
 * <br>
 * 注意事项：
 * 条件允许情况下，先生成业务数据，最后再获取流水，再进行写表操作。<br>
 * 不推荐在大事务（指的是业务处理时间花费较长）中使用获取单个流水，可预先插入数据入库，在调用流水，一次获取多个流水，再对原有业务进行流水回写操作<br>
 */
@Component
public class SequenceSingleton implements InitializingBean {
    @Value("${spring.redis.switch}")
    private String redisSwitch;
    @Value("${spring.redis.mode}")
    private String redisMode;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;
    // 哨兵模式
    @Value("${spring.redis.sentinel.master}")
    private String sentinelMaster;
    @Value("${spring.redis.sentinel.nodes}")
    private String sentinelNodes;
    // 集群模式
    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
    private static SequenceSingleton sequenceSingleton = new SequenceSingleton();
    private Map<String, DefaultSequenceHolder> cacheSequenceHolder = new HashMap<>();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    @Autowired
    private ISequenceService sequenceServiceImpl;
    private static ISequenceService sequenceService;
    private static RedissonClient redissonClient;
    @Override
    public void afterPropertiesSet() {
        sequenceService = this.sequenceServiceImpl;
        System.out.println("redisSwitch = " + redisSwitch);
        System.out.println("redisMode = " + redisMode);
        // 暂不添加
        if (Boolean.parseBoolean(redisSwitch)) {
            Config config = new Config();
            //update by zhoul 2020-04-27 修改适配单机、哨兵、集群模式
            if ("sentinel".equals(redisMode)) {
                // 哨兵模式
                System.out.println("哨兵模式 sentinelNodes = " + sentinelNodes);
                if (StrUtil.isNotBlank(sentinelNodes)){
                    Set<String> hostAndPorts = StringUtils.commaDelimitedListToSet(sentinelNodes);
                    String[] hostAndPortsArray = new String[hostAndPorts.size()];
                    int i = 0;
                    for (String hostAndPort : hostAndPorts) {
                        hostAndPortsArray[i++] = "redis://" + hostAndPort;
                    }
                    config.useSentinelServers()
                            .setMasterName(sentinelMaster)
                            .addSentinelAddress(hostAndPortsArray)
                            .setPassword(password);
                }
            } else if ("cluster".equals(redisMode)) {
                // 集群模式
                System.out.println("集群模式 clusterNodes = " + clusterNodes);
                if (StrUtil.isNotBlank(clusterNodes)){
                    Set<String> hostAndPorts = StringUtils.commaDelimitedListToSet(clusterNodes);
                    String[] hostAndPortsArray = new String[hostAndPorts.size()];
                    int i = 0;
                    for (String hostAndPort : hostAndPorts) {
                        hostAndPortsArray[i++] = "redis://" + hostAndPort;
                    }
                    config.useClusterServers()
                            // 集群状态扫描间隔时间，单位是毫秒
                            .setScanInterval(5000)
                            //添加集群节点
                            .addNodeAddress(hostAndPortsArray)
                            .setPassword(password);
                }
            } else {
                // 单点模式
                System.out.println("单点模式 = " + "redis://" + host + ":" + port);
                SingleServerConfig singleServerConfig = config.useSingleServer();
                singleServerConfig.setAddress("redis://" + host + ":" + port);
                singleServerConfig.setPassword(password);
                config.useSingleServer().setDatabase(database);
            }
            redissonClient = Redisson.create(config);
        }
    }
    public SequenceSingleton() {
    }
    public static SequenceSingleton getInstance() {
        return sequenceSingleton;
    }
    /**
     * 获取一个流水号，使用业务代码作为流水号前缀组成部分
     *
     * @param sequenceEnum 流水号类型
     * @return String       流水号
     */
    private DefaultSequenceHolder getSequenceHolder(ISequenceEnum sequenceEnum) {
        readWriteLock.readLock().lock();
        DefaultSequenceHolder defaultSequenceHolder;
        try {
            // 将接口对象转化为字符串
            String enumMd5JsonString = MD5Util.MD5Encode(JsonUtils.toJSONString(sequenceEnum));
            defaultSequenceHolder = cacheSequenceHolder.get(enumMd5JsonString);
            if (defaultSequenceHolder == null) {
                readWriteLock.readLock().unlock();
                readWriteLock.writeLock().lock();
                defaultSequenceHolder = cacheSequenceHolder.get(enumMd5JsonString);
                try {
                    if (defaultSequenceHolder == null) {
                        defaultSequenceHolder = new DefaultSequenceHolder(sequenceEnum, sequenceService, redissonClient);
                        cacheSequenceHolder.put(enumMd5JsonString, defaultSequenceHolder);
                    }
                } finally {
                    readWriteLock.readLock().lock();
                    readWriteLock.writeLock().unlock();
                }
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
        return defaultSequenceHolder;
    }
    /**
     * 流水枚举对象转化为  流水接口对象
     *
     * @param sequenceEnum 枚举对象
     * @return 流水接口对象
     */
    private static ISequenceEnum sequenceEnum2ISequenceEnum(final SequenceEnum sequenceEnum) {
        if (sequenceEnum == null) {
            return null;
        }
        return new ISequenceEnum() {
            @Override
            public SequenceTypeEnum getSequenceTypeEnum() {
                return sequenceEnum.getSequenceTypeEnum();
            }
            @Override
            public String getEnumName() {
                return sequenceEnum.name();
            }
            @Override
            public String getPrefix() {
                return sequenceEnum.getPrefix();
            }
            @Override
            public Integer getSequenceLength() {
                return sequenceEnum.getSequenceLength();
            }
            @Override
            public String getTableName() {
                return sequenceEnum.getTableName();
            }
            @Override
            public String getFieldName() {
                return sequenceEnum.getFieldName();
            }
            @Override
            public SequenceDateEnum getDateEnum() {
                return sequenceEnum.getDateEnum();
            }
            @Override
            public void setDateEnum(SequenceDateEnum dateEnum) {
                sequenceEnum.setDateEnum(dateEnum);
            }
            @Override
            public MultiTable[] getMultiTables() {
                return sequenceEnum.getMultiTables();
            }
            @Override
            public void setMultiTables(MultiTable[] multiTables) {
                sequenceEnum.setMultiTables(multiTables);
            }
            @Override
            public boolean isContinueSequence() {
                return sequenceEnum.isContinueSequence();
            }
        };
    }
    /**
     * 获取一个流水号
     *
     * @param sequenceEnum 流水接口对象
     * @return String   流水号
     */
    public static String getSequence(SequenceEnum sequenceEnum) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).get();
    }
    /**
     * 获取一个流水号
     *
     * @param sequenceEnum 流水号类型
     * @return String   流水号
     */
    public static String getSequence(ISequenceEnum sequenceEnum) {
        return getInstance().getSequenceHolder(sequenceEnum).get();
    }
    public static String getSequence(SequenceEnum sequenceEnum, String specialCode) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).get(specialCode);
    }
    public static String getSequenceBySpecifyLock(SequenceEnum sequenceEnum, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).get(lockKey);
    }
    public static String getSequenceBySpecifyLockNoCache(SequenceEnum sequenceEnum, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).getNoCache(lockKey);
    }
    public static String getSequenceBySpecifyLock(ISequenceEnum sequenceEnum, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum).get(lockKey);
    }
    public static String getSequenceBySpecifyLock(SequenceEnum sequenceEnum, String specialCode, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).get(specialCode, lockKey);
    }
    public static String getSequenceBySpecifyLock(ISequenceEnum sequenceEnum, String specialCode, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum).get(specialCode, lockKey);
    }
    public static void lockOnly(SequenceEnum sequenceEnum, String specialCode, String lockKey) {
        getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).lockOnly(specialCode, lockKey);
    }
    public static void lockOnly(ISequenceEnum sequenceEnum, String specialCode, String lockKey) {
        getInstance().getSequenceHolder(sequenceEnum).lockOnly(specialCode, lockKey);
    }
    public static boolean isLocked(SequenceEnum sequenceEnum, String specialCode) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).isLocked(specialCode, null);
    }
    public static boolean isLocked(ISequenceEnum sequenceEnum, String specialCode) {
        return getInstance().getSequenceHolder(sequenceEnum).isLocked(specialCode, null);
    }
    public static boolean isLocked(SequenceEnum sequenceEnum, String specialCode, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).isLocked(specialCode, lockKey);
    }
    public static boolean isLocked(ISequenceEnum sequenceEnum, String specialCode, String lockKey) {
        return getInstance().getSequenceHolder(sequenceEnum).isLocked(specialCode, lockKey);
    }
    /**
     * 获取多个流水号
     *
     * @param sequenceEnum 流水号类型
     * @param size         流水号的数量
     * @return List<String> 流水号List集合
     */
    public static List<String> listSequence(SequenceEnum sequenceEnum, Integer size) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).get(size, null, null);
    }
    /**
     * 获取多个流水号
     *
     * @param sequenceEnum 流水号类型
     * @param size         流水号的数量
     * @return List<String> 流水号List集合
     */
    public static List<String> listSequence(ISequenceEnum sequenceEnum, Integer size) {
        return getInstance().getSequenceHolder(sequenceEnum).get(size, null, null);
    }
    /**
     * 获取多个流水号，使用业务代码作为流水号前缀组成部分
     *
     * @param sequenceEnum 流水号类型
     * @param size         流水号的数量
     * @param specialCode  特殊代码
     * @return List<String>
     */
    public static List<String> listSequence(SequenceEnum sequenceEnum, Integer size, String specialCode) {
        return getInstance().getSequenceHolder(sequenceEnum2ISequenceEnum(sequenceEnum)).get(size, specialCode, null);
    }
    /**
     * 获取多个流水号，使用业务代码作为流水号前缀组成部分
     *
     * @param sequenceEnum 流水号类型
     * @param size         流水号的数量
     * @param specialCode  特殊代码
     * @return List<String>
     */
    public static List<String> listSequence(ISequenceEnum sequenceEnum, Integer size, String specialCode) {
        return getInstance().getSequenceHolder(sequenceEnum).get(size, specialCode, null);
    }
}
