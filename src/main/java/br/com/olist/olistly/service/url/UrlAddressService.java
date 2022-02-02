package br.com.olist.olistly.service.url;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.exception.RequiredFieldException;
import br.com.olist.olistly.model.PageList;
import br.com.olist.olistly.model.QueryParam;
import br.com.olist.olistly.model.url.UrlAddress;
import br.com.olist.olistly.repository.url.UrlAddressRepository;
import br.com.olist.olistly.utilitary.FormatterUtils;
import br.com.olist.olistly.utilitary.ValidatorUtils;

@Service
public class UrlAddressService {

	private UrlAddressRepository urlAddressRepository;

	public UrlAddressService(UrlAddressRepository urlAddressRepository) {
		this.urlAddressRepository = urlAddressRepository;
	}

	public UrlAddress create(UrlAddress urlAddress) {
		checkRequiredFields(urlAddress);

		urlAddress.setId(null);
		urlAddress.setOriginalUrl(urlAddress.getOriginalUrl());
		urlAddress.setShortenedHash(generateShortenedUrl());
		urlAddress.setDateCreated(LocalDateTime.now());
		urlAddress.setStatus(Status.ENABLE);

		return urlAddressRepository.save(urlAddress);
	}

	private void checkRequiredFields(UrlAddress urlAddress) {
		if (urlAddress == null)
			throw new RequiredFieldException("O endereço de URL não pode ser nulo!");
		if (urlAddress.getUser() == null || ValidatorUtils.isNullOrLessThanOne(urlAddress.getUser().getId()))
			throw new RequiredFieldException("Deve ser informado um usuário!");
		if (ValidatorUtils.isNullOrEmpty(urlAddress.getOriginalUrl()))
			throw new RequiredFieldException("Deve ser informada uma URL de origem!");
		if (!ValidatorUtils.isValidUrl(urlAddress.getOriginalUrl()))
			throw new RequiredFieldException("Deve ser informada uma URL de origem válida!");
	}

	public PageList<UrlAddress> findAllByUserIdPaged(Long userId, QueryParam params) {
		return urlAddressRepository.findAllByUserIdPaged(userId, params);
	}

	public Optional<UrlAddress> findByShortenedHashAndStatus(String shortenedHash, Status status) {
		if (ValidatorUtils.isNullOrEmpty(shortenedHash))
			return Optional.empty();

		return urlAddressRepository.findByShortenedHashAndStatus(shortenedHash.trim(), status == null ? Status.ENABLE : status);
	}

	public int changeStatus(Long id, Status status) {
		if (ValidatorUtils.isNullOrLessThanOne(id))
			return 0;

		return urlAddressRepository.changeStatus(id, status);
	}

	private String generateShortenedUrl() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			final String hash = UUID.randomUUID().toString() + System.currentTimeMillis();
			final byte[] hashbytes = digest.digest(hash.getBytes(StandardCharsets.UTF_8));

			return FormatterUtils.bytesToHex(hashbytes);
		} catch (NoSuchAlgorithmException exception) {
			throw new RequiredFieldException("Erro ao gerar o hash de link curto!");
		}
	}
}
