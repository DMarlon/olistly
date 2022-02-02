package br.com.olist.olistly.repository.url;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.olist.olistly.enumerator.Status;
import br.com.olist.olistly.model.PageList;
import br.com.olist.olistly.model.QueryParam;
import br.com.olist.olistly.model.url.UrlAddress;
import br.com.olist.olistly.utilitary.DataBaseUtils;
import br.com.olist.olistly.utilitary.ValidatorUtils;

public interface UrlAddressRepository extends JpaRepository<UrlAddress, Long>, JpaSpecificationExecutor<UrlAddress> {

	@Modifying
	@Transactional
	@Query("UPDATE UrlAddress u SET u.status = :status WHERE u.id = :id")
	int changeStatus(@Param("id") Long id, @Param("status") Status status);

	Optional<UrlAddress> findByShortenedHashAndStatus(String shortenedHash, Status status);

	default PageList<UrlAddress> findAllByUserIdPaged(Long userId, QueryParam params) {
		Specification<UrlAddress> filters = getFilters(userId, params);

		Page<UrlAddress> page = findAll(filters, getPageable(params));
		if (params.getPage() > page.getTotalPages()) {
			params.setPage(page.getTotalPages());
			page = findAll(filters, getPageable(params));
		}

		return new PageList<UrlAddress>()
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

	default Specification<UrlAddress> getFilters(Long userId, QueryParam params) {
		Specification<UrlAddress> filter = Specification.where(userIdEquals(userId));

		if (params != null) {
			if (!ValidatorUtils.isNullOrEmpty(params.getTerm()))
				filter = filter.and(originalUrlContains(params.getTerm()));

			if (params.getStatus() != null)
				filter = filter.and(statusEquals(params.getStatus()));
		}

		return filter;
	}

	static Specification<UrlAddress> userIdEquals(Long userId) {
		if (ValidatorUtils.isNullOrLessThanOne(userId))
			throw new IllegalArgumentException("Deve ser informado o usuário da URL!");

	    return (urlAddress, cq, cb) -> cb.equal(urlAddress.get("user"), userId);
	}

	static Specification<UrlAddress> originalUrlContains(String originalUrl) {
	    return (urlAddress, cq, cb) -> DataBaseUtils.Ilike(cb, urlAddress.get("originalUrl"), originalUrl);
	}

	static Specification<UrlAddress> statusEquals(Status status) {
	    return (urlAddress, cq, cb) -> cb.equal(urlAddress.get("status"), status.getCode());
	}

}
