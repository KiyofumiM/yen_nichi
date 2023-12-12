package com.tie.yennichi.form;

import lombok.Data;

@Data
public class GoodLearningForm {

    private Long userId;

    private Long learningId;

    private LearningForm learning;
}
