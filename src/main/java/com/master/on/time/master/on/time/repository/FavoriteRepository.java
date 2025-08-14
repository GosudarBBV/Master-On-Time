package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.Favorite;
import com.master.on.time.master.on.time.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndTarget(User user, User target);

    void deleteByUserAndTarget(User user, User target);
}
