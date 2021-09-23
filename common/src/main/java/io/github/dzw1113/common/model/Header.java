package io.github.dzw1113.common.model;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/18 16:50
 **/
public class Header {
    
    /**
     * netty消息校验码三部分
     * 1、0xccdc 固定值，表明消息是系统的协议消息
     * 2、主版本号：1~255 1byte
     * 3、次版本号:1~255 1byte
     * crcCode = oxccdc+主版本号+次版本号
     */
    private int crcCode = 0xccdc0101;
    /**
     * 消息长度，包括消息头和消息体
     */
    private int length;//消息长度
    /**
     * 集群节点全局唯一,由会话ID生成器生成
     */
    private long sessionID;//会话ID
    /**
     * 消息类型
     */
    private byte type;//消息类型
    
    public Header() {
    }
    
    public Header(int crcCode, int length, long sessionID) {
        this.crcCode = crcCode;
        this.length = length;
        this.sessionID = sessionID;
        this.type = type;
    }
    
    public int getCrcCode() {
        return crcCode;
    }
    
    public int getLength() {
        return length;
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    
    public long getSessionID() {
        return sessionID;
    }
    
    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }
    
    public byte getType() {
        return type;
    }
    
    public void setType(byte type) {
        this.type = type;
    }
    
    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }
}
