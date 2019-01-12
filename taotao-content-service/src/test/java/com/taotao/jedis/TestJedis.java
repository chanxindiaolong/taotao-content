package com.taotao.jedis;


import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestJedis {
@Test
public void testJedis() throws Exception{
	Jedis jedis = new Jedis("192.168.25.153",6379);
	jedis.set("jedis-key","1234");
	String result = jedis.get("jedis-key");
	System.out.println(result);
	jedis.close();
}
/*
@Test
public void testJedisPool() throws Exception{
	JedisPool jedisPool = new JedisPool("192.168.25.153",6379);
	Jedis jedis =jedisPool.getResource();
	String result = jedis.get("jedis-key");
	System.out.println(result);
	jedis.close();
	jedisPool.close();
	
}

@Test
public void testJedisCluster() throws Exception {
	// 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
	Set<HostAndPort> nodes = new HashSet<>();
	nodes.add(new HostAndPort("192.168.25.153", 7000));
	nodes.add(new HostAndPort("192.168.25.153", 7001));
	nodes.add(new HostAndPort("192.168.25.153", 7002));
	nodes.add(new HostAndPort("192.168.25.153", 7003));
	nodes.add(new HostAndPort("192.168.25.153", 7004));
	nodes.add(new HostAndPort("192.168.25.153", 7005));
	JedisCluster jedisCluster = new JedisCluster(nodes);
	// 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
	jedisCluster.set("hello", "100");
	String result = jedisCluster.get("hello");
	// 第三步：打印结果
	System.out.println(result);
	// 第四步：系统关闭前，关闭JedisCluster对象。
	jedisCluster.close();
}*/
}
