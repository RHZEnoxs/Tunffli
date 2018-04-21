package com.enoxs.tunffli;

/**
 * Created by enoxs on 2016/9/21.
 */
public interface Controller {
    public void jump_Fragment(int ctrl);
    public void Back_Page();
    public void connect(String host, String port);
    public void unlink();
    public void publish(String topic, String message);
    public void subscribe(String topic);
    public void init_topic_info();
}
