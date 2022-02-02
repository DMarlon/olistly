package br.com.olist.olistly.controller.system;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.olist.olistly.dto.system.UserInfoDTO;
import br.com.olist.olistly.model.system.Session;
import br.com.olist.olistly.security.service.SecurityService;
import br.com.olist.olistly.service.system.SessionService;

@RestController
@RequestMapping("/user")
public class UserController {

	private SessionService sessionService;
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/info")
    public UserInfoDTO getUser() {
    	Session session = SecurityService.getCurrentSession();
    	session.setLastAccess(LocalDateTime.now());

    	try {
    		sessionService.save(session);
		} catch (Exception exception) {
			logger.error("Erro ao atualizar data do último acesso do usuário: "+exception.getMessage());
		}

    	return UserInfoDTO.valueOf(session.getUser());
    }
}
