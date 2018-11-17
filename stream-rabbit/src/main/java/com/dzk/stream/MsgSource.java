package com.dzk.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by dzk on 2018/11/17.
 */
public interface MsgSource {

    String OUTPUT = "msg-output";

    @Output(OUTPUT)
    MessageChannel output();
}
