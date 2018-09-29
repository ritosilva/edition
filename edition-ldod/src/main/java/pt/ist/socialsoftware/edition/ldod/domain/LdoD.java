package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;

public class LdoD extends LdoD_Base {
	private static Logger log = LoggerFactory.getLogger(LdoD.class);

	public static LdoD getInstance() {
		return FenixFramework.getDomainRoot().getLdoD();
	}

	public LdoD() {
		FenixFramework.getDomainRoot().setLdoD(this);
		setNullEdition(new NullEdition());
		setLastTwitterID(new LastTwitterID()); // check if this is supposed to be here
	}

	public List<Heteronym> getSortedHeteronyms() {
		return getHeteronymsSet().stream().sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
				.collect(Collectors.toList());
	}

	public List<ExpertEdition> getSortedExpertEdition() {
		return getExpertEditionsSet().stream().sorted().collect(Collectors.toList());
	}

	public Edition getEdition(String acronym) {
		for (Edition edition : getExpertEditionsSet()) {
			if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
				return edition;
			}
		}

		return getVirtualEdition(acronym);
	}

	public VirtualEdition getVirtualEdition(String acronym) {
		for (VirtualEdition edition : getVirtualEditionsSet()) {
			if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
				return edition;
			}
		}

		return null;
	}

	public LdoDUser getUser(String username) {
		for (LdoDUser user : getUsersSet()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public Fragment getFragmentByXmlId(String target) {
		for (Fragment fragment : getFragmentsSet()) {
			if (fragment.getXmlId().equals(target)) {
				return fragment;
			}
		}
		return null;
	}

	public List<VirtualEdition> getVirtualEditions4User(LdoDUser user, LdoDSession session) {
		List<VirtualEdition> manageVE = new ArrayList<>();
		List<VirtualEdition> selectedVE = new ArrayList<>();
		List<VirtualEdition> mineVE = new ArrayList<>();
		List<VirtualEdition> publicVE = new ArrayList<>();

		session.synchronizeSession(user);

		if (user == null) {
			selectedVE.addAll(session.materializeVirtualEditions());
		}

		for (VirtualEdition virtualEdition : getVirtualEditionsSet()) {
			if (user != null && virtualEdition.getSelectedBySet().contains(user)) {
				selectedVE.add(virtualEdition);
			} else if (virtualEdition.getParticipantSet().contains(user)) {
				mineVE.add(virtualEdition);
			} else if (virtualEdition.getPub() && !selectedVE.contains(virtualEdition)) {
				publicVE.add(virtualEdition);
			}
		}

		manageVE.addAll(selectedVE);
		manageVE.addAll(mineVE);
		manageVE.addAll(publicVE);

		return manageVE;
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEdition createVirtualEdition(LdoDUser user, String acronym, String title, LocalDate date, boolean pub,
			Edition usedEdition) {
		log.debug("createVirtualEdition user:{}, acronym:{}, title:{}", user.getUsername(), acronym, title);
		return new VirtualEdition(this, user, acronym, title, date, pub, usedEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public RecommendationWeights createRecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		return new RecommendationWeights(user, virtualEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public void switchAdmin() {
		setAdmin(!getAdmin());
	}

	@Atomic(mode = TxMode.WRITE)
	public LdoDUser createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName,
			String lastName, String email, SocialMediaService socialMediaService, String socialMediaId) {

		removeOutdatedUnconfirmedUsers();

		if (getUser(username) == null) {
			LdoDUser user = new LdoDUser(this, username, passwordEncoder.encode(password), firstName, lastName, email);
			user.setSocialMediaService(socialMediaService);
			user.setSocialMediaId(socialMediaId);

			Role userRole = Role.getRole(RoleType.ROLE_USER);
			user.addRoles(userRole);

			return user;
		} else {
			throw new LdoDDuplicateUsernameException(username);
		}
	}

	public UserConnection getUserConnection(String userId, String providerId, String providerUserId) {
		return getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)
				&& uc.getProviderId().equals(providerId) && uc.getProviderUserId().equals(providerUserId)).findFirst()
				.orElse(null);
	}

	@Atomic(mode = TxMode.WRITE)
	public void createUserConnection(String userId, String providerId, String providerUserId, int rank,
			String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
			String refreshToken, Long expireTime) {

		new UserConnection(this, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
				accessToken, secret, refreshToken, expireTime);
	}

	public void removeOutdatedUnconfirmedUsers() {
		DateTime now = DateTime.now();
		getTokenSet().stream().filter(t -> t.getExpireTimeDateTime().isBefore(now)).map(t -> t.getUser())
				.forEach(u -> u.remove());
	}

	public RegistrationToken getTokenSet(String token) {
		return getTokenSet().stream().filter(t -> t.getToken().equals(token)).findFirst().orElse(null);
	}

	public Set<SourceInter> getFragmentRepresentatives() {
		return getFragmentsSet().stream().map(f -> f.getRepresentativeSourceInter()).collect(Collectors.toSet());
	}

	public VirtualEdition getArchiveEdition() {
		return getVirtualEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getJPCEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.COELHO_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getTSCEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.CUNHA_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getRZEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ZENITH_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getJPEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.PIZARRO_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public VirtualEdition getVirtualEditionByXmlId(String xmlId) {
		return getVirtualEditionsSet().stream().filter(ve -> ve.getXmlId().equals(xmlId)).findFirst().orElse(null);
	}

	@Atomic(mode = TxMode.WRITE)
	public void createTestUsers(PasswordEncoder passwordEncoder) {
		LdoD ldod = LdoD.getInstance();

		Role userRole = Role.getRole(RoleType.ROLE_USER);
		Role admin = Role.getRole(RoleType.ROLE_ADMIN);

		// the bcrypt generator
		// https://www.dailycred.com/blog/12/bcrypt-calculator
		for (int i = 0; i < 6; i++) {
			String username = "zuser" + Integer.toString(i + 1);
			if (LdoD.getInstance().getUser(username) == null) {
				LdoDUser user = new LdoDUser(ldod, username, passwordEncoder.encode(username), "zuser", "zuser",
						"zuser" + Integer.toString(i + 1) + "@teste.pt");

				user.setEnabled(true);
				user.addRoles(userRole);
			}
		}

	}

	public Set<TwitterCitation> getAllTwitterCitation() {
		// allTwitterCitations -> all twitter citations in the archive
		Set<TwitterCitation> allTwitterCitations = getCitationSet().stream().filter(TwitterCitation.class::isInstance)
				.map(TwitterCitation.class::cast).collect(Collectors.toSet());
		return allTwitterCitations;
	}

	public TwitterCitation getTwitterCitationByTweetID(long id) {
		TwitterCitation result = null;
		Set<TwitterCitation> allTwiiterCitations = getAllTwitterCitation();
		for (TwitterCitation tc : allTwiiterCitations) {
			if (tc.getTweetID() == id) {
				result = tc;
			}
		}
		return result;
	}

	public Tweet getTweetByTweetID(long id) {
		Tweet result = null;
		Set<Tweet> allTweets = getTweetSet();
		for (Tweet t : allTweets) {
			if (t.getTweetID() == id) {
				result = t;
			}
		}
		return result;
	}

	public boolean checkIfTweetExists(long id) {
		Set<Tweet> allTweets = getTweetSet();
		for (Tweet t : allTweets) {
			if (t.getTweetID() == id) {
				return true;
			}
		}
		return false;
	}

	public Set<Citation> getCitationSet() {
		return getFragmentsSet().stream().flatMap(f -> f.getCitationSet().stream()).collect(Collectors.toSet());
	}

	public Citation getCitationById(long id) {
		return getCitationSet().stream().filter(citation -> citation.getId() == id).findFirst().orElse(null);
	}

	public List<VirtualEdition> getVirtualEditions4User(String username) {
		LdoDUser user = getUser(username);
		return LdoD.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getParticipantList().contains(user)).collect(Collectors.toList());

	}
	
	public Set<ClassificationGame> getPublicClassificationGames() {
		return getVirtualEditionsSet().stream().flatMap(virtualEdition ->
				virtualEdition.getClassificationGameSet().stream().filter(c -> c.getOpenAnnotation() && c.isActive()))
				.collect(Collectors.toSet());
	}

	public Set<ClassificationGame> getActiveGames4User(String username) {
		Set<VirtualEdition> virtualEditions4User = new HashSet<VirtualEdition>(getVirtualEditions4User(username));
		Set<ClassificationGame> classificationGamesOfUser = virtualEditions4User.stream().flatMap(virtualEdition -> virtualEdition
				.getClassificationGameSet().stream().filter(ClassificationGame::isActive)).collect(Collectors.toSet());

		Set<ClassificationGame> allClassificationGames4User = new HashSet<>(getPublicClassificationGames());
		allClassificationGames4User.addAll(classificationGamesOfUser);
		return allClassificationGames4User;

	}

	public Map<String, Double> getOverallLeaderboard() {
		/*Map<Set<String>, Collection<Double>> collect = getVirtualEditionsSet().stream().
				flatMap(v -> v.getClassificationGameSet().stream().
				map(ClassificationGame::getLeaderboard)).
				collect(Collectors.toMap(Map::keySet, Map::values));
		
		getVirtualEditionsSet().stream().flatMap(v -> v.getClassificationGameSet().stream().
				map(g -> g.getLeaderboard().entrySet().stream().map(Map.Entry::getKey)
						.collect(Collectors.toList())));
		collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		Stream<Map<String, Double>> mapStream = getVirtualEditionsSet().stream().flatMap(v -> v
				.getClassificationGameSet().stream().
				map(g -> g.getLeaderboard().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map
						.Entry::getValue))));
		getVirtualEditionsSet().stream().
				flatMap(v -> v.getClassificationGameSet().stream().
						map(g -> g.getLeaderboard().entrySet().stream().map(Map.Entry::getKey).
		mapStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		//HashMap result = new LinkedHashMap();

		//result.forEach((k, v) -> collect.merge(k, v, (v1, v2) -> v1 + v2));*/
		List<Map<String, Double>> collect = LdoD.getInstance().getVirtualEditionsSet().stream().flatMap(v -> v
				.getClassificationGameSet().stream().map(g -> g.getLeaderboard())).collect(Collectors.toList());
		Map<String, Double> result = new LinkedHashMap<>();
		for (Map<String,Double> m : collect) {
			for (Map.Entry<String, Double> e : m.entrySet()) {
				String key = e.getKey();
				Double value = result.get(key);
				result.put(key, value == null ? e.getValue() : e.getValue() + value);
			}
		}
		return result;
	}

}
