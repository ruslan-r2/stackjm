package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.VoteTypeQ;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ответ")
public class QuestionDto {
    @Schema(description = "id вопроса")
    private Long id;
    @Schema(description = "заголовок вопроса")
    private String title;
    @Schema(description = "id автора")
    private Long authorId;
    @Schema(description = "имя автора")
    private String authorName;
    @Schema(description = "ссылка на изображение автора")
    private String authorImage;
    @Schema(description = "описание вопроса")
    private String description;
    @Schema(description = "количество просмотров")
    private Long viewCount;
    @Schema(description = "репутация автора")
    private Long authorReputation;
    @Schema(description = "количество ответов на вопрос")
    private Long countAnswer;
    @Schema(description = "рейтинг вопроса")
    private Long countValuable;
    @Schema(description = "дата создания вопроса")
    private LocalDateTime persistDateTime;
    @Schema(description = "дата последнего обновления")
    private LocalDateTime lastUpdateDateTime;
    @Schema(description = "кол-во голосов за вопрос")
    private Long countVote;
    @Schema(description = "голос авторизованного пользователя за вопрос")
    private VoteTypeQ voteType;
    @Schema(description = "список тэгов")
    private List<TagDto> listTagDto;

}
