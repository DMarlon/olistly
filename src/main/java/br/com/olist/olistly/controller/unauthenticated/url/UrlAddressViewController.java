package br.com.olist.olistly.controller.unauthenticated.url;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.model.url.UrlAddress;
import br.com.olist.olistly.model.url.UrlAddressView;
import br.com.olist.olistly.service.url.UrlAddressService;
import br.com.olist.olistly.service.url.UrlAddressViewService;

@RestController("UnauthenticatedUrlAddressViewController")
@RequestMapping("/public/url/view")
public class UrlAddressViewController {

	private UrlAddressService urlAddressService;
	private UrlAddressViewService urlAddressViewService;

	public UrlAddressViewController(UrlAddressViewService urlAddressViewService, UrlAddressService urlAddressService) {
		this.urlAddressService = urlAddressService;
		this.urlAddressViewService = urlAddressViewService;
	}

	@GetMapping("/{hash:[a-zA-Z0-9]+}")
	public String getURL(@PathVariable("hash")String hash, HttpServletRequest request) {
		Optional<UrlAddress> urlAddress = urlAddressService.findByShortenedHashAndStatus(hash, Status.ENABLE);
		if (urlAddress.isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL não encontrada!");

		try {
			UrlAddressView urlAddressView = new UrlAddressView();
			urlAddressView.setUrlAddress(urlAddress.get());
			urlAddressView.setDeviceName(request.getHeader("User-Agent"));
			urlAddressView.setRemoteAddress(getRemoteIP(request));
			urlAddressViewService.registerURLView(urlAddressView);

			return urlAddress.get().getOriginalUrl();
		} catch (RequiredFieldException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao encurtar URL, contate o suporte!");
		}
	}

	private String getRemoteIP(HttpServletRequest request) {
		String remoteIP = request.getHeader("X-FORWARDED-FOR");
		if (remoteIP == null)
			remoteIP = request.getRemoteAddr();

		return remoteIP;
	}
}
