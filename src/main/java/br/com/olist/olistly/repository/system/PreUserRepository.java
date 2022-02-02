package br.com.olist.olistly.repository.system;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.olist.olistly.model.system.PreUser;

public interface PreUserRepository extends JpaRepository<PreUser, Long> {

	@Query("SELECT COUNT(p)> 0 FROM PreUser p WHERE p.login = :login AND (:id IS NULL OR id <> :id)")
	boolean existsByLogin(@Param("login") String login, @Param("id") Long id);

	Optional<PreUser> findByActivationHash(String hash);
	Optional<PreUser> findByLogin(String login);

	void deleteById(Long id);
}
