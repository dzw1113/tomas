package io.github.dzw1113.common.model;

import java.util.Date;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 13:53
 **/
public class SocketRequest {
    
    private String status = "成功！";
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    private Date time;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    private String text;
}
