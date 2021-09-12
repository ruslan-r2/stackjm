package com.javamentor.qa.platform.service.impl.dto;

import java.time.LocalDateTime;

public class QuestionDto {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String authorImage;
    private String description;
    private int viewCount;
    private int countAnswer;
    private int countValuable;
    private LocalDateTime persistDateTime;
    private LocalDateTime lastUpdateDateTime;
    private List<TagDto> listTagDto;
}
