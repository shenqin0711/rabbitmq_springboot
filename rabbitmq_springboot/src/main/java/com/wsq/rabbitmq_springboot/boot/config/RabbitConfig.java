package com.wsq.rabbitmq_springboot.boot.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/***
 * 编写RabbitConfig类，类里面设置很多个EXCHANGE,QUEUE,ROUTINGKEY，是为了接下来的不同使用场景。
 *
 *  Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
     Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
     Queue:消息的载体,每个消息都会被投到一个或多个队列。
     Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
     Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
     vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
     Producer:消息生产者,就是投递消息的程序.
     Consumer:消息消费者,就是接受消息的程序.
     Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Configuration
public class RabbitConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    //定义三个交换机name
    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";
    public static final String EXCHANGE_C = "my-mq-exchange_C";
    //定义三个queueName
    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";
    //定义三个routing key
    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";
    /**
     * 创建rabbitmq的工厂
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host,port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost("/sample");
        cachingConnectionFactory.setPublisherConfirms(true);
        return cachingConnectionFactory;
    }
    /**
     * 创建rabbitmqTemplate
     * SCOPE_PROTOTYPE:这个是说在每次注入的时候回自动创建一个新的bean实例
     *
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return  rabbitTemplate;

    }
    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange(){
        return new DirectExchange(EXCHANGE_A);
    }
    /**
     * * 获取队列A
     * @return
     durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean
    public Queue queueA(){
        return new Queue(QUEUE_A,false,false,false);
    }
    @Bean
    public Queue queueB(){
        return new Queue(QUEUE_B,false,false,false);
    }
    @Bean
    public Queue queueC(){
        return new Queue(QUEUE_C,false,false,false);
    }
    /**
     * 绑定队列和交换机
     *
     */
    public Binding bindingA(){
        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(ROUTINGKEY_A);
    }
    @Bean
    public Binding bindingB(){
        return BindingBuilder.bind(queueB()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_B);
    }

}
