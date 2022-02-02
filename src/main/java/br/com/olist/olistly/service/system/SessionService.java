package br.com.olist.olistly.service.system;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.olist.olistly.dto.system.SessionListViewDTO;
import br.com.olist.olistly.model.JWTInfo;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.model.system.User;
import br.com.olist.olistly.repository.system.SessionRepository;
import br.com.olist.olistly.security.adapter.JWTAdapter;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@Service
public class SessionService {

	private SessionRepository sessionRepository;
	private JWTInfo jwtInfo;

	public SessionService(SessionRepository sessionRepository, JWTInfo jwtInfo) {
		this.sessionRepository = sessionRepository;
		this.jwtInfo = jwtInfo;
	}

	public boolean existsAndBelongToUser(Long id, Long userId) {
		if (ValidatorUtils.isNullOrLessThanOne(id) || ValidatorUtils.isNullOrLessThanOne(userId))
			return false;

		return sessionRepository.existsAndBelongToUser(id, userId);
	}

	public Session register(User user, String addressIP, String device) {
		LocalDateTime expiration = JWTAdapter.addExpirationTime(LocalDateTime.now());

		Session session = new Session();
		session.setUser(user);
		session.setRemoteAddress(addressIP);
		session.setDeviceName(device);
		session.setToken(JWTAdapter.createToken(user.getName(), expiration, jwtInfo.getSecret()));
		session.setLastAccess(LocalDateTime.now());
		session.setExpiration(expiration);
		sessionRepository.save(session);

		return session;
	}

	public void removeExpiredSessions(User user) {
		sessionRepository.deleteByUserAndExpirationLessThan(user.getId(), LocalDateTime.now());
	}

	public void removeAll(User user) {
		if (user != null && !ValidatorUtils.isNullOrLessThanOne(user.getId()))
			sessionRepository.removeAll(user);
	}

	public Session save(Session session) {
		return sessionRepository.save(session);
	}

	public List<SessionListViewDTO> findAllByUserId(Long userId, Long currentId) {
		if (ValidatorUtils.isNullOrLessThanOne(userId))
			return Collections.emptyList();

		return sessionRepository.findAllByUserId(userId, currentId);
	}

	public Session getByToken(String token) {
		if (ValidatorUtils.isNullOrEmpty(token))
			return null;

		return sessionRepository.findByToken(token.trim());
	}

	public void deleteById(Long id) {
		if (!ValidatorUtils.isNullOrLessThanOne(id))
			sessionRepository.deleteById(id);
	}

	public void logout(String token) {
		if (!ValidatorUtils.isNullOrEmpty(token))
			sessionRepository.deleteByToken(token);
	}
}
