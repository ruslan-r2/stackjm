package com.javamentor.qa.platform.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "badges")
public class Badge implements Serializable {

    private static final long serialVersionUID = 3714949012456624550L;
    @Id
    @GeneratedValue(generator = "Badge_seq")
    private Long id;

    @Column(name = "badge_name")
    private String badgeName;

    @Column(name = "reputations_for_merit")
    private Integer reputationForMerit;

    @Column
    private String description;
}
