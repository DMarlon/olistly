package br.com.olist.olistly.utilitary;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class DataBaseUtils {

	public static Predicate Ilike(CriteriaBuilder cb, Expression<String> field, String search ) {
		return cb.like(cb.lower(field), "%"+FormatterUtils.removeAccentuation(search.toLowerCase())+"%");
	}
}
