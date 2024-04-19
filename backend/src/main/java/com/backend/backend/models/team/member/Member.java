package com.backend.backend.models.team.member;

import com.backend.backend.models.team.Team;
import com.backend.backend.models.users.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "membros")
@Entity(name = "membros")
@EqualsAndHashCode(of = "id")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", unique = false, nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "teamId", unique = false, nullable = false, updatable = false)
    private Team team;

    public MemberResponseDTO toDTO() {
        return new MemberResponseDTO(this);
    }
}
