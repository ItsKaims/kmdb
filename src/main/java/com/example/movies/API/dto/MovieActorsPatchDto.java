// MovieActorsPatchDto.java (for patching just the actors list)
package com.example.movies.API.dto;

import java.util.Set;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MovieActorsPatchDto {
  @NotNull(message = "Actor IDs cannot be null")
  @NotEmpty(message = "Please select at least one actor")
  private Set<Long> actorIds;

  public MovieActorsPatchDto() { }

  public Set<Long> getActorIds() {
    return actorIds;
  }
  public void setActorIds(Set<Long> actorIds) {
    this.actorIds = actorIds;
  }
}
