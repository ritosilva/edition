/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AddText.Place;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.DelText.HowDel;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.Rend;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.TextPortion;

/**
 * Produces a TEI representation of the tree representing a fragment text
 * 
 * @author ars
 * 
 */
public class TEIWriter implements TextTreeVisitor {

	private String result = "";

	public String getResult() {
		return result;
	}

	@Override
	public void visit(AppText appText) {
		result = result + "<app>";

		TextPortion firstChild = appText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</app>";

		if (appText.getParentOfLastText() == null) {
			if (appText.getNextText() != null) {
				appText.getNextText().accept(this);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		result = result + "<rdgGrp>";

		TextPortion firstChild = rdgGrpText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</rdgGrp>";

		if (rdgGrpText.getParentOfLastText() == null) {
			rdgGrpText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RdgText rdgText) {
		String wit = "";
		for (FragInter inter : rdgText.getInterps()) {
			wit = wit + "#" + inter.getXmlId() + " ";
		}

		if (!wit.equals("")) {
			result = result + "<rdg wit=\"" + wit.trim() + "\">";
		} else {
			result = result + "<rdg>";
		}

		TextPortion firstChild = rdgText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</rdg>";

		if (rdgText.getParentOfLastText() == null) {
			rdgText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		result = result + "<p>";

		TextPortion firstChild = paragraphText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</p>";

		if (paragraphText.getParentOfLastText() == null) {
			paragraphText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SegText segText) {
		String rendition = "";
		for (Rend rend : segText.getRendSet()) {
			rendition = rendition + "#" + rend.getRend().getDesc() + " ";
		}

		if (!rendition.equals("")) {
			result = result + "<seg rendition=\"" + rendition.trim() + "\">";
		} else {
			result = result + "<seg>";
		}

		TextPortion firstChild = segText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</seg>";

		if (segText.getParentOfLastText() == null) {
			segText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SimpleText simpleText) {
		result = result + simpleText.getValue();

		if (simpleText.getParentOfLastText() == null) {
			simpleText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		String hyphenated = lbText.getHyphenated() ? "type=\"hyphenated\"" : "";
		String breakWord = lbText.getBreakWord() ? "break=\"no\"" : "";
		String ed = "";
		for (FragInter inter : lbText.getInterps()) {
			ed = ed + "#" + inter.getXmlId() + " ";
		}

		ed = ed.equals("") ? "" : "ed=\"" + ed.trim() + "\"";

		result = result + "<lb " + ed + " " + hyphenated + " " + breakWord
				+ "/>";

		if (lbText.getParentOfLastText() == null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {
		result = result + "<pb/>";

		if (pbText.getParentOfLastText() == null) {
			pbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText spaceText) {
		String dim = "dim=\"" + spaceText.getDim().getDesc() + "\"";
		String quantity = "quantity=\"" + spaceText.getQuantity() + "\"";
		String unit = "unit=\"" + spaceText.getUnit().getDesc() + "\"";

		result = result + "<space " + dim + " " + quantity + " " + unit + "/>";

		if (spaceText.getParentOfLastText() == null) {
			spaceText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		String place = addText.getPlace() != Place.UNSPECIFIED ? "place=\""
				+ addText.getPlace().getDesc() + "\"" : "";

		result = result + "<add " + place + ">";

		TextPortion firstChild = addText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</add>";

		if (addText.getParentOfLastText() == null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		String rend = delText.getHow() != HowDel.UNSPECIFIED ? "rend=\""
				+ delText.getHow().getDesc() + "\"" : "";

		result = result + "<del " + rend + ">";

		TextPortion firstChild = delText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</del>";

		if (delText.getParentOfLastText() == null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {
		result = result + "<subst>";

		TextPortion firstChild = substText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</subst>";

		if (substText.getParentOfLastText() == null) {
			substText.getNextText().accept(this);
		}
	}
}
