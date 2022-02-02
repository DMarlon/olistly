package br.com.olist.olistly.controller.system;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.olist.olistly.dto.system.SessionListViewDTO;
import br.com.olist.olistly.security.service.SecurityService;
import br.com.olist.olistly.service.system.SessionService;

@RestController
@RequestMapping("/user/session")
public class SessionController {

	private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<SessionListViewDTO> getSessions() {
    	return sessionService.findAllByUserId(SecurityService.getLoggedUser().getId(), SecurityService.getCurrentSession().getId());
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void deleteSession(@PathVariable("id") Long id) {
    	if (sessionService.existsAndBelongToUser(id, SecurityService.getLoggedUser().getId())) {
    		sessionService.deleteById(id);
    	}
    }
}
