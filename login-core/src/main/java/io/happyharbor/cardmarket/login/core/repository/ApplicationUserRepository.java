package io.happyharbor.cardmarket.login.core.repository;

import io.happyharbor.cardmarket.login.core.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {

  boolean existsByUsername(final String username);

  Optional<ApplicationUser> findByUsername(String username);

}
