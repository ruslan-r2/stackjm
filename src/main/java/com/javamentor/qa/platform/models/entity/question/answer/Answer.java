package com.javamentor.qa.platform.models.entity.question.answer;import com.javamentor.qa.platform.models.entity.question.Question;import com.javamentor.qa.platform.models.entity.user.User;import lombok.AllArgsConstructor;import lombok.Getter;import lombok.NoArgsConstructor;import lombok.Setter;import org.hibernate.annotations.CreationTimestamp;import org.hibernate.annotations.Type;import org.hibernate.annotations.UpdateTimestamp;import javax.persistence.CascadeType;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.EntityNotFoundException;import javax.persistence.FetchType;import javax.persistence.GeneratedValue;import javax.persistence.Id;import javax.persistence.JoinColumn;import javax.persistence.Lob;import javax.persistence.ManyToOne;import javax.persistence.OneToMany;import javax.persistence.PrePersist;import javax.persistence.PreUpdate;import javax.persistence.Table;import javax.validation.constraints.NotNull;import java.io.Serializable;import java.time.LocalDateTime;import java.util.ArrayList;import java.util.List;import java.util.Objects;@Entity@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Table(name = "answer")public class Answer implements Serializable {    private static final long serialVersionUID = 8978480742174798932L;    @Id    @GeneratedValue(generator = "Answer_seq")    private Long id;    @CreationTimestamp    @Column(name = "persist_date", updatable = false)    @Type(type = "org.hibernate.type.LocalDateTimeType")    private LocalDateTime persistDateTime;    @Column(name = "update_date", nullable = false)    @Type(type = "org.hibernate.type.LocalDateTimeType")    @UpdateTimestamp    private LocalDateTime updateDateTime;    @ManyToOne(fetch = FetchType.LAZY, optional = false)    @JoinColumn(name = "question_id")    private Question question;    @ManyToOne(fetch = FetchType.LAZY, optional = false)    @JoinColumn(name = "user_id")    private User user;    @Lob    @NotNull    @Column    @Type(type = "org.hibernate.type.TextType")    private String htmlBody;    @NotNull    @Column(name = "is_helpful")    private Boolean isHelpful;    @NotNull    @Column(name = "is_deleted")    private Boolean isDeleted;    @NotNull    @Column(name = "is_deleted_by_moderator")    private Boolean isDeletedByModerator;    @Column(name = "date_accept_time")    @Type(type = "org.hibernate.type.LocalDateTimeType")    private LocalDateTime dateAcceptTime;    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "answer", orphanRemoval = true)    private List<CommentAnswer> commentAnswers;    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "answer", orphanRemoval = true)    private List<VoteAnswer> voteAnswers = new ArrayList<>();    public Answer (Question question, User user, String htmlBody, Boolean isHelpful, Boolean isDeleted, Boolean isDeletedByModerator)  {        this.question = question;        this.user = user;        this.htmlBody = htmlBody;        this.isHelpful = isHelpful;        this.isDeleted = isDeleted;        this.isDeletedByModerator = isDeletedByModerator;    }    @PrePersist    private void prePersistFunction() {        checkConstraints();    }    @PreUpdate    private void preUpdateFunction() {        checkConstraints();    }    private void checkConstraints() {        try {            if (this.user.getId() <= 0) {                throw new EntityNotFoundException("User id must be > 0 on create or update.");            }        } catch (NullPointerException e) {            throw new EntityNotFoundException("User id must be not null on create.");        }    }    @Override    public boolean equals(Object o) {        if (this == o) return true;        if (o == null || getClass() != o.getClass()) return false;        Answer answer = (Answer) o;        return Objects.equals(id, answer.id) &&                Objects.equals(persistDateTime, answer.persistDateTime) &&                Objects.equals(updateDateTime, answer.updateDateTime) &&                Objects.equals(question, answer.question) &&                Objects.equals(user, answer.user) &&                Objects.equals(htmlBody, answer.htmlBody) &&                Objects.equals(isHelpful, answer.isHelpful) &&                Objects.equals(isDeleted, answer.isDeleted) &&                Objects.equals(isDeletedByModerator, answer.isDeletedByModerator) &&                Objects.equals(dateAcceptTime, answer.dateAcceptTime);    }    @Override    public int hashCode() {        return Objects.hash(id, persistDateTime, updateDateTime, question, user, htmlBody, isHelpful, isDeleted, isDeletedByModerator, dateAcceptTime);    }}