package com.robin.iot.common.kafka.model;

import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产者
 */
@Data
public class Producer {

    /**
     * kafka template。
     */
    private String kafkaTemplate;

    /**
     * 格式为 host[:port]，例如 127.0.0.1:9092，kafka 连接的 broker 地址列表。
     */
    private List<String> bootstrapServers = new ArrayList<String>(){{
        add("127.0.0.1:9092");
    }};

    /**
     * kafka 收到消息的 ack，
     * 0 - 不要答复，爱收到没收到；
     * 1 - 有一个 leader broker 答复即可；
     * all - 所有 broker 都要收到。
     * <p>
     * 默认为 1。
     * <li>
     *     0: Producer 不等待 kafka 服务器的答复，消息立刻发往 socket buffer，
     *     这种方式不能保证 kafka 收到消息，设置成这个值的时候 retries 参数就失效了，
     *     因为 producer 不知道 kafka 收没收到消息，所以所谓的重试就没有意义了，
     *     发送返回值的 offset 全默认是 -1。
     * <li>
     *     1: 等待 leader 记录数据到 broker 本地 log 即可，不等待 leader 同步到其他 followers，
     *     那么假如此时刚好 leader 收到消息并答复后，leader 突然挂了，
     *     其他 follower 还没来得及复制消息呢，那么这条消息就会丢失了。
     * <li>
     *     all: 等待所有 broker 记录消息. 保证消息不会丢失（只要从节点没全挂），
     *     这种方式是最高可用的。
     */
    private Integer acks = 1;

    /**
     * 批处理消息字节数。
     * <p>
     * 发往 broker 的消息会包含多个 batches，每个分区对应一个 batch，
     * batch 小了会减小响吞吐量，batch 为 0 的话就禁用了 batch 发送。
     * <p>
     * 默认为 16384(16kb)
     */
    private Integer batchSize = 16384;

    /**
     * 当消息发送速度大于 kafka 服务器接收的速度，producer 会阻塞 max_block_ms，超时会报异常。
     * <p>
     * buffer_memory 用来保存等待发送的消息。
     * <p>
     * 默认为 33554432 (32MB)
     */
    private Long bufferMemory = 33554432L;

    /**
     * 客户端标识，可用于追查日志。
     * <p>
     * 默认为 kafka-producer-#，其中 # 代表一个唯一编号。
     */
    private String clientId = "kafka-producer";

    /**
     * 发消息时可选择的压缩类型，包括：gzip，snappy，lz4 和 None。
     * <p>
     * 压缩是针对 batches 的，所以 batches 的大小会影响压缩效率，大一点的压缩比例可能好些，
     * 要是太小的话压缩就没有意义了，比如就发个几个字节的数据那压完数据没准更大了。
     * <p>
     * 至于什么时候启用压缩，要看应用场景，启用后 producer 会变慢，但网络传输带宽占用会减少，
     * 带宽紧缺建议开启压缩，带宽充足就不用开了。
     * <p>
     * 默认为 None。
     */
    private String compressionType = "None";

    /**
     * key 序列化器。
     */
    private Class<?> keySerializer = StringSerializer.class;

    /**
     * Value 序列化器。
     */
    private Class<?> valueSerializer = StringSerializer.class;

    /**
     * 重试发送次数，有时候网络出现短暂的问题的时候，会自动重发消息，
     * 前面提到了这个值是需要在 acks 等于 1 或 all 时候才有效。
     * <p>
     * 如果设置了该参数，但是 max.in.flight.requests.per.connection 没有设置为 1 的话，
     * 可能造成消息顺序的改变，因为如果 2 个 batches 发到同一个 partition，但是第一个失败重发了，
     * 那么就会造成第二个 batches 跑到前面去了。
     * <p>
     * 默认为 0。
     */
    private Integer retries = 0;

    /**
     * 生产者每次发送消息的时间间隔，单位：ms。
     * <p>
     * 默认值为 0。
     */
    private Integer lingerMs = 0;

    /**
     * 单条消息的最大字节数。
     * <p>
     * 默认值为 52428800 (50MB)
     */
    private Integer maxRequestSize = 52428800;

    /**
     * 当非空时，为生产者启用事务支持。
     */
    private String transactionIdPrefix;

    /**
     * 用于配置客户端的其他特定于生产者的属性。
     */
    private final Map<String, String> properties = new HashMap<>();

}
