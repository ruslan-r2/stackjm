package com.javamentor.qa.platform.models.entity.user;

import com.javamentor.qa.platform.models.entity.Badge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_badges")
public class UserBadges implements Serializable {

    private static final long serialVersionUID = 7887575908980210093L;
    @Id
    @GeneratedValue(generator = "UserBadges_seq")
    private Long id;

    @Column
    private Boolean ready;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="badges_id")
    private Badge badge;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        UserBadges that = (UserBadges) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(ready, that.ready);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ready);
    }
}

