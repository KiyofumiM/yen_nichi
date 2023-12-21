package com.tie.yennichi.form;

import lombok.Data;

@Data
public class GoodBoardForm {

    private Long userId;

    private Long boardId;
    
    private BoardForm board;
}
