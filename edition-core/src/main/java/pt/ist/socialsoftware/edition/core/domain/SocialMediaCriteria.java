package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.shared.exception.LdoDException;

public abstract class SocialMediaCriteria extends SocialMediaCriteria_Base {

	protected void init(VirtualEdition edition, Class<?> clazz) {
		checkUniqueCriteriaType(edition, clazz);
		setVirtualEdition(edition);
	}

	private void checkUniqueCriteriaType(VirtualEdition edition, Class<?> clazz) {
		if (edition.getCriteriaSet().stream().filter(clazz::isInstance).findFirst().isPresent()) {
			throw new LdoDException();
		}
	}

	public void remove() {
		setVirtualEdition(null);

		deleteDomainObject();
	}

}
