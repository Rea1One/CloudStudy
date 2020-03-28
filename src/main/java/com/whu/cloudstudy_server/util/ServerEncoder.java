package com.whu.cloudstudy_server.util;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import net.sf.json.JSONObject;

/**
 * Author: 郭瑞景
 * Date: 2020-03-20
 */
public class ServerEncoder implements Encoder.Text<Response> {
    @Override
    public String encode(Response response) {
        return JSONObject.fromObject(response).toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig){}

    @Override
    public void destroy(){}
}
