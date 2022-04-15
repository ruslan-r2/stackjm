package com.javamentor.qa.platform.models.entity.question;

import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes_on_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteQuestion implements Serializable {

    private static final long serialVersionUID = 6479035497338780810L;

    @Id
    @GeneratedValue(generator = "VoteQuestion_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime localDateTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private VoteTypeQ voteTypeQ;

    public VoteQuestion(User user, Question question, VoteTypeQ voteTypeQ) {
        this.user = user;
        this.question = question;
        this.voteTypeQ = voteTypeQ;
    }

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (!voteTypeQ.equals(VoteTypeQ.UP) && !voteTypeQ.equals(VoteTypeQ.DOWN)) {
            throw new ConstrainException("В сущности VoteQuestion допускается передача значения в поле voteTypeQ только UP или DOWN");
        }
    }
}
