package br.com.olist.olistly.controller.url;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.olist.olistly.dto.url.UrlDTO;
import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.exception.UniqueFieldException;
import br.com.olist.olistly.model.PageList;
import br.com.olist.olistly.model.QueryParam;
import br.com.olist.olistly.model.url.UrlAddress;
import br.com.olist.olistly.security.service.SecurityService;
import br.com.olist.olistly.service.url.UrlAddressService;
import br.com.olist.olistly.utilitary.HTTPUtils;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@RestController
@RequestMapping("/url")
public class UrlAddressController {

	private UrlAddressService urlAddressService;

	public UrlAddressController(UrlAddressService urlAddressService) {
		this.urlAddressService = urlAddressService;
	}

	@GetMapping("/paged")
	public PageList<UrlAddress> list(QueryParam queryParam) {
		return urlAddressService.findAllByUserIdPaged(SecurityService.getLoggedUser().getId(), queryParam);
	}

	@DeleteMapping("/{id:[0-9]+}/{status:^(?:ENABLE|DISABLE)$}")
	public int changeStatus(@PathVariable("id") Long id, @PathVariable("status") Status status) {
		return urlAddressService.changeStatus(id, status);
	}

	@PostMapping("/shorten")
	public String getShortenUrl(@RequestBody UrlDTO urlDTO) {
		try {
			UrlAddress urlAddress = new UrlAddress();
			urlAddress.setOriginalUrl(completeURLWithoutProcotol(urlDTO.getOriginal()));
			urlAddress.setUser(SecurityService.getLoggedUser());
			urlAddressService.create(urlAddress);

			return urlAddress.getShortenedHash();
		} catch (RequiredFieldException | UniqueFieldException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao encurtar URL, contate o suporte!");
		}
	}

	public static String completeURLWithoutProcotol(String url) {
		if (ValidatorUtils.isNullOrEmpty(url) || ValidatorUtils.isValidUrl(url))
			return url;

		return HTTPUtils.setHttpProtocol(url);
	}
}
