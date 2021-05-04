package io.happyharbor.cardmarket.login.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
  USER("user", "ROLE_USER"),
  ADMIN("admin", "ROLE_ADMIN");

  @JsonValue
  @SuppressWarnings("unused")
  private final String name;

  private final String grantedAuthorityName;

  @Override
  public String getAuthority() {
    return grantedAuthorityName;
  }
}
