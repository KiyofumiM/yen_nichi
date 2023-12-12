package com.tie.yennichi.form;

import lombok.Data;

@Data
public class FavoriteLearningForm {

    private Long userId;

    private Long learningId;

    private LearningForm learning;
}
