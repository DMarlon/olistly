package br.com.olist.olistly.repository.url;

import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.olist.olistly.model.PageList;
import br.com.olist.olistly.model.QueryParam;
import br.com.olist.olistly.model.url.UrlAddressView;
import br.com.olist.olistly.utilitary.DataBaseUtils;
import br.com.olist.olistly.utilitary.ValidatorUtils;

public interface UrlAddressViewRepository extends JpaRepository<UrlAddressView, Long>, JpaSpecificationExecutor<UrlAddressView> {

	long countByUrlAddressId(Long urlAddressId);

	@Query("SELECT COUNT(uv.id) FROM UrlAddressView uv JOIN UrlAddress ua ON ua = uv.urlAddress WHERE ua.user.id = :userId")
	long countByUserId(@Param("userId") Long userId);

	default PageList<UrlAddressView> findAllByUrlAddressIdPaged(Long urlAddressId, QueryParam params) {
		Specification<UrlAddressView> filters = getFilters(urlAddressId, params);

		Page<UrlAddressView> page = findAll(filters, getPageable(params));
		if (params.getPage() > page.getTotalPages()) {
			params.setPage(page.getTotalPages());
			page = findAll(filters, getPageable(params));
		}

		return new PageList<UrlAddressView>()
				.inPage(page.getNumber() + 1)
				.withContent(page.getContent())
				.withTotalPages(page.getTotalPages())
				.withTotalRecords(page.getTotalElements());
	}

	default Pageable getPageable(QueryParam params) {
		if (params == null)
			return PageRequest.of(0, 20);

		return PageRequest.of(params.getPage()-1, params.getLimit(), Sort.by(Sort.Direction.fromString(params.getDirection()), getOrder(params.getOrder())));
	}

	default String getOrder(String order) {
		return Arrays.asList("originalUrl", "shortenedUrl").contains(order) ? order : "id";
	}

	default Specification<UrlAddressView> getFilters(Long urlAddressId, QueryParam params) {
		Specification<UrlAddressView> filter = Specification.where(urlAddressIdEquals(urlAddressId));

		if (params != null && !ValidatorUtils.isNullOrEmpty(params.getTerm()))
			filter = filter.and(deviceNameContains(params.getTerm()).or(remoteAddressContains(params.getTerm())));

		return filter;
	}

	static Specification<UrlAddressView> urlAddressIdEquals(Long urlAddressId) {
		if (ValidatorUtils.isNullOrLessThanOne(urlAddressId))
			throw new IllegalArgumentException("Deve ser informado a URL!");

	    return (urlAddress, cq, cb) -> cb.equal(urlAddress.get("urlAddress"), urlAddressId);
	}

	static Specification<UrlAddressView> deviceNameContains(String deviceName) {
	    return (urlAddress, cq, cb) -> DataBaseUtils.Ilike(cb, urlAddress.get("deviceName"), deviceName);
	}

	static Specification<UrlAddressView> remoteAddressContains(String remoteAddress) {
	    return (urlAddress, cq, cb) -> DataBaseUtils.Ilike(cb, urlAddress.get("remoteAddress"), remoteAddress);
	}
}
