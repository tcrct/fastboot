package org.fastboot.redis.test;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 一致性hash工具类
 *https://juejin.im/post/5cfdf4e5f265da1bd260e04c
 * https://juejin.im/post/5b8f93576fb9a05d11175b8d
 *
 * @author Laotang
 * @since 1.0
 */
public class UniformityHashKit {

    private ConcurrentSkipListMap<Integer, String> skipListMap;
    private static final String SEPARATE = "_virtual_";
    // 虚拟节点数量
    private int virtualSize;
    private String nodeKey;
    // 真实节点集合
    private List<String> nodeHostList= new ArrayList<String>() {{
        add("192.168.0.1:8001");
        add("192.168.0.2:8001");
        add("192.168.0.3:8001");
    }};

    private static class UniformityHashKitHolder {
        private static final UniformityHashKit INSTANCE = new UniformityHashKit();
    }

    public static final UniformityHashKit duang() {
        return UniformityHashKitHolder.INSTANCE;
    }

    private UniformityHashKit() {
        skipListMap = new ConcurrentSkipListMap();
//        List<String> nodeHostList= PropKit.getList("node.hosts");
        if (null ==nodeHostList) {
            throw new NullPointerException("服务器地址没有设置[server.host]，请先在配置文件里设置");
        }
        virtualSize = getVirtualSize(nodeHostList.size());
        for (String serverHost : nodeHostList) {
            put(serverHost);
        }
        System.out.println(skipListMap);
    }

    /**
     * 计算虚拟节点
     * 这里用实际节点数乘以100
     *
     * @param size
     * @return
     */
    private int getVirtualSize(int size) {
//        CRC16(Key) % 16384 //redis
        return 16384;
//        return size * 100;
    }

    private void put(String nodeHost) {
        if (null== nodeHost) {
            return;
        }
        for (int i=0; i<virtualSize; i++) {
            String virtualKey = nodeHost + SEPARATE + i;
            skipListMap.put(getHash(virtualKey), virtualKey);
        }
    }

    public UniformityHashKit nodeKey(String key) {
        nodeKey = key;
        return this;
    }

    /**
     * 根据参数取节点
     * @return 节点名称
     */
    public String getNode() {
        if (null == nodeKey) {
            throw new NullPointerException("节点不能为空");
        }
        int keyHash = getHash(nodeKey);
        ConcurrentNavigableMap<Integer, String> tailMap = skipListMap.tailMap(keyHash);
        // 如果tailMap为空，即说明key对应的节点正好在环型节点的尾部，按环型节点的顺时针方向取第一个节点作为返回值
        String nodeHost = (null == tailMap || tailMap.isEmpty()) ? skipListMap.firstEntry().getValue() : tailMap.firstEntry().getValue();
        // 注意,由于使用了虚拟节点,所以这里要做 虚拟节点 -> 真实节点的映射
        return nodeHost.substring(0, nodeHost.indexOf(SEPARATE));
    }

    /**
     * 删除节眯
     */
    public void deleteNode() {
        if (null == nodeKey) {
            return;
        }
        for (int i=0; i<virtualSize; i++) {
            String virtualKey = nodeKey + SEPARATE + i;
            skipListMap.remove(getHash(virtualKey), virtualKey);
        }
    }

    /**
     * 取字符串的hash值
     * @param key
     * @return
     */
    private int getHash(String key) {
        // 直接使用String对象的hashCode
//        int hash = key.hashCode();
//        return (hash<0) ? Math.abs(hash) : hash;

        // 使用FNV1_32_HASH算法
         return getHash2(key);
    }

    /**
     * 计算Hash值, 使用FNV1_32_HASH算法
     * @param str
     * @return
     */
    private int getHash2(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash =( hash ^ str.charAt(i) ) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        return (hash < 0) ?Math.abs(hash) : hash;
    }


    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        for (int i=0; i<1000; i++) {
            String key = UUID.randomUUID().toString();
            String nodeName = UniformityHashKit.duang().nodeKey(key).getNode();
            Integer count = map.get(nodeName);
            if (null == count) {
                map.put(nodeName,1);
            } else {
                map.put(nodeName, count+1);
            }
        }
        System.out.println(map);
    }

}
