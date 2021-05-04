package io.happyharbor.cardmarket.login.core.entity;

import io.happyharbor.cardmarket.login.api.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ApplicationUser {

  @Id
  @GeneratedValue
  private UUID id;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createTs;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime modifyTs;

  @NaturalId
  private String username;

  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  Set<Role> roles;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ApplicationUser)) return false;
    ApplicationUser user = (ApplicationUser) o;
    return Objects.equals(username, user.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }
}

