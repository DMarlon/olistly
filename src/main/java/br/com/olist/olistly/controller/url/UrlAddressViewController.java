package br.com.olist.olistly.controller.url;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.olist.olistly.model.PageList;
import br.com.olist.olistly.model.QueryParam;
import br.com.olist.olistly.model.url.UrlAddressView;
import br.com.olist.olistly.security.service.SecurityService;
import br.com.olist.olistly.service.url.UrlAddressViewService;

@RestController
@RequestMapping({"/url/view", "/url/{urlAddressId:[0-9]+}/view"})
public class UrlAddressViewController {

	private UrlAddressViewService urlAddressViewService;

	public UrlAddressViewController(UrlAddressViewService urlAddressViewService) {
		this.urlAddressViewService = urlAddressViewService;
	}

	@GetMapping("/paged")
	public PageList<UrlAddressView> list(@PathVariable("urlAddressId") Optional<Long> urlAddressId, QueryParam queryParam) {
		if (urlAddressId.isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser informado o id da URL que deseja visualizar!");

		return urlAddressViewService.findAllByUrlAddressIdPaged(urlAddressId.get(), queryParam);
	}

	@GetMapping("/total")
	public long getTotalByUrlAddress(@PathVariable("urlAddressId") Optional<Long> urlAddressId) {
		return urlAddressId.isEmpty() ? urlAddressViewService.countByUserId(SecurityService.getLoggedUser().getId()) : urlAddressViewService.countByUrlAddressId(urlAddressId.get());
	}
}
