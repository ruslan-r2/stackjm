package com.javamentor.qa.platform.models.entity.question.answer;

import com.javamentor.qa.platform.models.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "votes_on_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteAnswer {

    @Id
    @GeneratedValue(generator = "AnswerVote_seq")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Answer answer;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime persistDateTime;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public VoteAnswer(User user, Answer answer, VoteType voteType) {
        this.user = user;
        this.answer = answer;
        this.voteType = voteType;
    }
}
