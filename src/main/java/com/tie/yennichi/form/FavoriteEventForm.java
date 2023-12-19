package com.tie.yennichi.form;

import lombok.Data;

@Data
public class FavoriteEventForm {

    private Long userId;

    private Long eventId;

    private EventForm event;
}
