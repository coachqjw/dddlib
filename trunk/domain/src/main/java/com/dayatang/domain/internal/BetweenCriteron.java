package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class BetweenCriteron<T> implements QueryCriterion {
	
	private Comparable<T> from;
	
	private Comparable<T> to;

	private String propName;

	public BetweenCriteron(String propName, Comparable<T> from, Comparable<T> to) {
		this.propName = propName;
		this.from = from;
		this.to = to;
	}

	public Comparable<T> getFrom() {
		return from;
	}

	public Comparable<T> getTo() {
		return to;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof BetweenCriteron<?>))
			return false;
		BetweenCriteron<T> castOther = (BetweenCriteron<T>) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(from, castOther.from)
			.append(this.to, castOther.to)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName())
		.append(from).append(to).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " between " + from + " and " + to;
	}

	public String getPropName() {
		return propName;
	}

	
}
