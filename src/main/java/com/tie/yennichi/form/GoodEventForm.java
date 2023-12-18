package com.tie.yennichi.form;

import lombok.Data;

@Data
public class GoodEventForm {

    private Long userId;

    private Long EventId;
    
    private EventForm event;
}
