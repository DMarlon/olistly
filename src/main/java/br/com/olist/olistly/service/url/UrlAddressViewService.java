package br.com.olist.olistly.service.url;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.model.PageList;
import br.com.olist.olistly.model.QueryParam;
import br.com.olist.olistly.model.url.UrlAddressView;
import br.com.olist.olistly.repository.url.UrlAddressViewRepository;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@Service
public class UrlAddressViewService {

	private UrlAddressViewRepository urlAddressViewRepository;

	public UrlAddressViewService(UrlAddressViewRepository urlAddressViewRepository, UrlAddressService urlAddressService) {
		this.urlAddressViewRepository = urlAddressViewRepository;
	}

	public UrlAddressView registerURLView(UrlAddressView urlAddressView) {
		checkRequiredFields(urlAddressView);

		urlAddressView.setId(null);
		urlAddressView.setDateAccess(LocalDateTime.now());

		return urlAddressViewRepository.save(urlAddressView);
	}

	private void checkRequiredFields(UrlAddressView urlAddressView) {
		if (urlAddressView == null)
			throw new RequiredFieldException("A visualização do endereço de URL não pode ser nulo!");
		if (urlAddressView.getUrlAddress() == null || ValidatorUtils.isNullOrLessThanOne(urlAddressView.getUrlAddress().getId()))
			throw new RequiredFieldException("Deve ser informado um endereço!");
	}

	public PageList<UrlAddressView> findAllByUrlAddressIdPaged(Long urlAddressId, QueryParam params) {
		return urlAddressViewRepository.findAllByUrlAddressIdPaged(urlAddressId, params);
	}

	public long countByUrlAddressId(Long urlAddressId) {
		if (ValidatorUtils.isNullOrLessThanOne(urlAddressId))
			return Long.valueOf(0);

		return urlAddressViewRepository.countByUrlAddressId(urlAddressId);
	}

	public long countByUserId(Long userId) {
		if (ValidatorUtils.isNullOrLessThanOne(userId))
			return Long.valueOf(0);

		return urlAddressViewRepository.countByUserId(userId);
	}
}
