package app.ticket.ticketing.user;

import app.ticket.ticketing.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    User findByUserIdAndIsDelete(String userId, Boolean isDelete);
}
