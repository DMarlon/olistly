package br.com.olist.olistly.repository.system;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.olist.olistly.model.system.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLogin(String login);

	@Query("SELECT COUNT(u)> 0 FROM User u WHERE u.login = :login AND (:id IS NULL OR id <> :id)")
	boolean existsByLogin(@Param("login") String login, @Param("id") Long id);
}