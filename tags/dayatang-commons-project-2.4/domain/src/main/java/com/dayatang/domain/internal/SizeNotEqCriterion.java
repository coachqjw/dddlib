package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class SizeNotEqCriterion implements QueryCriterion {
	
	private int value;
	private String propName;

	public SizeNotEqCriterion(String propName, int value) {
		this.propName = propName;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof SizeNotEqCriterion))
			return false;
		SizeNotEqCriterion castOther = (SizeNotEqCriterion) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(value, castOther.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName()).append(value).toHashCode();
	}

	@Override
	public String toString() {
		return "size of " + getPropName() + " != " + value;
	}

	public String getPropName() {
		return propName;
	}
}