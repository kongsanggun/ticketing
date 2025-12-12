package app.ticket.ticketing.user;

import app.ticket.ticketing.db.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    User findByUserIdAndIsDelete(String userId, Boolean isDelete);
    @Modifying
    @Transactional
    @Query(value = """
         UPDATE user u SET u.point = u.point - :point
         WHERE u.user_id = :userId AND u.point >= :point;""", nativeQuery = true)
    int usePoint(@Param("userId") String userId, @Param("point") int point);
}
