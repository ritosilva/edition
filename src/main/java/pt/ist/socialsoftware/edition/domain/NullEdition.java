package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.NullEdition_Base;

public class NullEdition extends NullEdition_Base {

	@Override
	public Boolean getPub() {
		return true;
	}

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>();
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.AUTHORIAL;
	}

	@Override
	public String getReference() {
		return "";
	}

}
