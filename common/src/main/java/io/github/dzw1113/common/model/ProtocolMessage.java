package io.github.dzw1113.common.model;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/18 16:51
 **/
public class ProtocolMessage<T> {
    private Header header;//消息头
    private T body;//消息体
    
    public ProtocolMessage() {
    }
    
    public ProtocolMessage(Header header, T body) {
        this.header = header;
        this.body = body;
    }
    
    public Header getHeader() {
        return header;
    }
    
    public void setHeader(Header header) {
        this.header = header;
    }
    
    public T getBody() {
        return body;
    }
    
    public void setBody(T body) {
        this.body = body;
    }
    
}
    
