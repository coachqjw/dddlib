package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class NotEmptyCriteron implements QueryCriterion {

	private String propName;

	public NotEmptyCriteron(String propName) {
		this.propName = propName;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof NotEmptyCriteron))
			return false;
		NotEmptyCriteron castOther = (NotEmptyCriteron) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName()).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " is not empty";
	}

	public String getPropName() {
		return propName;
	}
	
}
