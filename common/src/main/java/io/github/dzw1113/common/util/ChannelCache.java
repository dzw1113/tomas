package io.github.dzw1113.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

/**
 * @description:s
 * @author: dzw
 * @date: 2021/09/18 18:00
 **/
public class ChannelCache {
    
    
    private final static Map<String, Channel> channelSessionCache = new ConcurrentHashMap<String, Channel>();
    
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public static void addSession(Channel channel) {
        lock.writeLock().lock();
        try {
            ChannelId channelId = channel.id();
            channelSessionCache.put(channelId.asLongText(), channel);
        } catch (Exception e) {
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    
    public static void removeSession(String channelId) {
        lock.writeLock().lock();
        try {
            if (channelSessionCache.containsValue(channelId)) {
                channelSessionCache.remove(channelId);
            }
        } catch (Exception e) {
        } finally {
            lock.writeLock().unlock();
        }
        
    }
    
    public static Channel getSession(String channelId) {
        lock.readLock().lock();
        Channel channel = null;
        try {
            if (channelSessionCache.containsKey(channelId)) {
                channel = channelSessionCache.get(channelId);
            }
        } catch (Exception e) {
        } finally {
            lock.readLock().unlock();
        }
        return channel;
    }
    
    public static void resetSession() {
        lock.writeLock().lock();
        try {
            channelSessionCache.clear();
        } catch (Exception e) {
        } finally {
            lock.writeLock().unlock();
        }
    }
    
}
