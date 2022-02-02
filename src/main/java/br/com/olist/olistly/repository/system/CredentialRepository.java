package br.com.olist.olistly.repository.system;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.olist.olistly.model.system.Credential;
import br.com.olist.olistly.model.system.User;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

	@Query("SELECT COUNT(c)> 0 FROM Credential c WHERE c.user.id = :userId AND (:id IS NULL OR id <> :id)")
	boolean existsByUserId(@Param("userId") Long userId, @Param("id") Long id);

	@Query("SELECT COUNT(c)> 0 FROM Credential c WHERE c.passwordResetHash = :passwordResetHash")
	boolean existsByPasswordResetHash(@Param("passwordResetHash") String passwordResetHash);

	@Modifying
	@Transactional
	@Query("UPDATE Credential c SET c.password = :password, c.passwordResetHash = null, c.passwordResetExpiration = null WHERE c.id = :id")
	int passwordChange(@Param("id") Long id, @Param("password") String password);

	@Modifying
	@Transactional
	@Query("UPDATE Credential c SET c.passwordResetHash = :passwordResetHash, c.passwordResetExpiration = :passwordResetExpiration WHERE c.id = :id")
	int updatePasswordResetInformations(@Param("passwordResetHash") String passwordResetHash, @Param("passwordResetExpiration") LocalDateTime passwordResetExpiration, @Param("id") Long id);

	Optional<Credential> findByUser(User user);
}
