package br.com.olist.olistly.repository.system;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.olist.olistly.dto.system.SessionListViewDTO;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.model.system.User;

public interface SessionRepository extends JpaRepository<Session, Long> {

	@Modifying
	@Transactional
	void deleteById(Long id);

	@Modifying
	@Transactional
	long deleteByToken(String token);

	@Modifying
	@Transactional
	@Query("DELETE FROM Session s WHERE s.user.id = :#{#user.id}")
	void removeAll(@Param("user") User user);

	@Modifying
	@Transactional
	@Query("DELETE FROM Session s WHERE s.user.id = :userId AND s.expiration < :date")
	void deleteByUserAndExpirationLessThan(@Param("userId") Long userId, @Param("date") LocalDateTime date);

	@Query("SELECT COUNT(s)> 0 FROM Session s WHERE s.id = :id AND s.user.id = :userId")
	boolean existsAndBelongToUser(@Param("id") Long id, @Param("userId") Long userId);

	Session findByToken(String token);

	@Query("SELECT s.id as id, s.deviceName as deviceName, s.remoteAddress as remoteAddress, s.lastAccess as lastAccess, s.expiration as expiration, (CASE WHEN s.id=:currentId THEN true ELSE false END) as current FROM Session s WHERE s.user.id = :userId")
	List<SessionListViewDTO> findAllByUserId(@Param("userId") Long userId, @Param("currentId") Long currentId);
}
