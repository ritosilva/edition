package pt.ist.socialsoftware.edition.visitors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AltText;
import pt.ist.socialsoftware.edition.domain.AnnexNote;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.GapText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.NoteText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.RefText;
import pt.ist.socialsoftware.edition.domain.Rend;
import pt.ist.socialsoftware.edition.domain.Rend.Rendition;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.UnclearText;

public class JDomHtmlWriter4OneInter implements TextPortionVisitor {
	private Element rootElement;
	private FragInter fragInter = null;

	protected Boolean highlightDiff = false;
	protected Boolean displayDel = false;
	protected Boolean highlightIns = true;
	protected Boolean highlightSubst = false;
	protected Boolean showNotes = true;

	private boolean generate = true;
	private PbText startPbText = null;
	private PbText stopPbText = null;

	public String getTranscription() {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(rootElement));
		return xml.outputString(rootElement);
	}

	private final Map<FragInter, Integer> interpsChar = new HashMap<FragInter, Integer>();
	private int totalChar = 0;

	public Integer getInterPercentage(FragInter inter) {
		return (interpsChar.get(inter) * 100) / totalChar;
	}

	public JDomHtmlWriter4OneInter(FragInter fragInter) {
		this.fragInter = fragInter;

		for (FragInter inter : fragInter.getFragment().getFragmentInterSet()) {
			interpsChar.put(inter, 0);
		}
	}

	public void write(Boolean highlightDiff) {
		rootElement = new Element("div");

		this.highlightDiff = highlightDiff;
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}
		visit((AppText) fragInter.getFragment().getTextPortion());
	}

	public void write(Boolean highlightDiff, Boolean displayDel, Boolean highlightIns, Boolean highlightSubst,
			Boolean showNotes, Boolean showFacs, PbText pbText) {
		rootElement = new Element("div");

		this.highlightDiff = highlightDiff;
		this.displayDel = displayDel;
		this.highlightIns = highlightIns;
		this.highlightSubst = highlightSubst;
		this.showNotes = showNotes;
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}

		if (showFacs) {

			startPbText = pbText;
			if (startPbText != null) {
				generate = false;
			}

			stopPbText = ((SourceInter) fragInter).getNextPbText(startPbText);

		}

		visit((AppText) fragInter.getFragment().getTextPortion());
	}

	@Override
	public void visit(AppText appText) {
		propagate2FirstChild(appText);

		propagate2NextSibling(appText);
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		if (rdgGrpText.getInterps().contains(this.fragInter)) {
			propagate2FirstChild(rdgGrpText);
		}

		propagate2NextSibling(rdgGrpText);
	}

	@Override
	public void visit(RdgText rdgText) {
		if (rdgText.getInterps().contains(this.fragInter)) {

			boolean color = false;
			if (highlightDiff) {
				int size = fragInter.getFragment().getFragmentInterSet().size();
				if (rdgText.getInterps().size() < size) {
					color = true;
					int colorValue = 255 - (255 / size) * (size - rdgText.getInterps().size() - 1);

					Element span = new Element("span");
					Attribute style = new Attribute("style", "background-color: rgb(0," + colorValue + ",255);");
					span.setAttribute(style);

					rootElement.addContent(span);
					rootElement = span;
				}
			}

			propagate2FirstChild(rdgText);

			if (color)
				rootElement = rootElement.getParentElement();

		}

		propagate2NextSibling(rdgText);
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		Element paragraph = new Element("p");
		paragraph.setAttribute("align", "justify");

		rootElement.addContent(paragraph);
		rootElement = paragraph;

		propagate2FirstChild(paragraphText);

		rootElement = rootElement.getParentElement();

		propagate2NextSibling(paragraphText);
	}

	@Override
	public void visit(SegText segText) {
		Element seg = new Element("seg");

		rootElement.addContent(seg);
		rootElement = seg;

		generateRendition(segText.getRendSet());

		if (segText.getAltTextWeight() != null) {
			Element span = new Element("span");
			span.setAttribute("class", "text-warning");
			rootElement.addContent(span);

			Element abbr = new Element("abbr");
			abbr.setAttribute("title", segText.getAltTextWeight().getAltText().getMode().getDesc() + " "
					+ segText.getAltTextWeight().getWeight());
			rootElement.addContent(abbr);
		}

		propagate2FirstChild(segText);

		rootElement = rootElement.getParentElement();

		propagate2NextSibling(segText);
	}

	@Override
	public void visit(AltText altText) {
		// do nothing, the segTextOne and segTextTwo will do
		propagate2NextSibling(altText);
	}

	@Override
	public void visit(SimpleText simpleText) {
		String value = simpleText.getValue();

		totalChar = totalChar + value.length();
		for (FragInter inter : simpleText.getInterps()) {
			Integer number = interpsChar.get(inter);
			number = number + value.length();
			interpsChar.put(inter, number);
		}

		Element span = new Element("span");
		span.setText(value);

		rootElement.addContent(span);

		propagate2NextSibling(simpleText);
	}

	@Override
	public void visit(LbText lbText) {
		if (lbText.getInterps().contains(fragInter)) {
			String hyphen = "";
			if (lbText.getHyphenated()) {
				hyphen = "-";
			}

			rootElement.addContent(hyphen);

			rootElement.addContent(new Element("br"));

		}

		propagate2NextSibling(lbText);
	}

	@Override
	public void visit(PbText pbText) {
		if (pbText.getInterps().contains(fragInter)) {
			if ((startPbText != pbText) && (stopPbText != pbText)) {
				Element hr = new Element("hr");
				hr.setAttribute("size", "8");
				hr.setAttribute("color", "black");
				rootElement.addContent(hr);
			}
		}

		if (startPbText == pbText) {
			generate = true;
		}

		if (stopPbText == pbText) {
			generate = false;
		}

		propagate2NextSibling(pbText);
	}

	@Override
	public void visit(SpaceText spaceText) {
		String separator = "";
		if (spaceText.getDim() == SpaceDim.VERTICAL) {
			// the initial line break is for a new line
			rootElement.addContent(new Element("br"));
			for (int i = 0; i < spaceText.getQuantity(); i++) {
				rootElement.addContent(new Element("br"));
			}
		} else if (spaceText.getDim() == SpaceDim.HORIZONTAL) {
			separator = "&nbsp; ";
			String value = "";
			for (int i = 0; i < spaceText.getQuantity(); i++) {
				value += separator;
			}
			rootElement.addContent(value);
		}

		propagate2NextSibling(spaceText);
	}

	@Override
	public void visit(AddText addText) {
		Element oldRoot = rootElement;
		if (highlightIns) {
			generateRendition(addText.getRendSet());

			switch (addText.getPlace()) {
			case INLINE:
			case INSPACE:
			case OVERLEAF:
			case SUPERIMPOSED:
			case MARGIN:
			case OPPOSITE:
			case BOTTOM:
			case END:
			case UNSPECIFIED:
				Element small = new Element("small");
				rootElement.addContent(small);
				rootElement = small;
				break;
			case ABOVE:
			case TOP:
				Element span = new Element("span");
				span.setAttribute("style", "position:relative; top:-3px;");
				rootElement.addContent(span);
				rootElement = span;
				break;
			case BELOW:
				span = new Element("span");
				span.setAttribute("style", "position:relative; top:3px;");
				rootElement.addContent(span);
				rootElement = span;
				break;
			}

			Element span = new Element("span");
			span.setAttribute("style", "color: rgb(128,128,128);");
			Element small = new Element("small");
			small.setText("&and;");
			span.addContent(small);
			if (showNotes) {
				Element abbr = new Element("abbr");
				abbr.setAttribute("title", addText.getNote());
				abbr.addContent(span);
				rootElement.addContent(abbr);
			} else {
				rootElement.addContent(span);
			}

			oldRoot = rootElement;
		}

		propagate2FirstChild(addText);

		if (highlightIns) {
			rootElement = oldRoot;
		}

		propagate2NextSibling(addText);
	}

	@Override
	public void visit(DelText delText) {
		if (displayDel) {
			Element del = new Element("del");
			rootElement.addContent(del);
			Element span = new Element("span");
			span.setAttribute("style", "color: rgb(128,128,128);");
			del.addContent(span);

			if (showNotes) {
				Element abbr = new Element("abbr");
				abbr.setAttribute("title", delText.getNote());
				span.addContent(abbr);
				rootElement = abbr;
			}

			propagate2FirstChild(delText);

			if (showNotes) {
				rootElement = rootElement.getParentElement();
			}

			rootElement = rootElement.getParentElement().getParentElement();
		}

		propagate2NextSibling(delText);
	}

	@Override
	public void visit(SubstText substText) {
		if (displayDel && highlightSubst) {
			Element span = new Element("span");
			span.setAttribute("style", "color: rgb(0,0,255);");
			rootElement.addContent(span);
		}

		propagate2FirstChild(substText);

		if (displayDel && highlightSubst) {
			Element span = new Element("span");
			span.setAttribute("style", "color: rgb(0,0,255);");
			Element sub = new Element("sub");
			sub.addContent("subst");
			span.addContent(sub);
			rootElement.addContent(span);
		}

		propagate2NextSibling(substText);
	}

	@Override
	public void visit(GapText gapText) {
		String gapValue = gapText.getGapValue();

		totalChar = totalChar + gapValue.length();
		for (FragInter inter : gapText.getInterps()) {
			Integer number = interpsChar.get(inter);
			number = number + gapValue.length();
			interpsChar.put(inter, number);
		}

		Element abbr = new Element("abbr");
		abbr.setAttribute("tilte",
				gapText.getReason().getDesc() + ", " + gapText.getExtent() + " " + gapText.getUnit());
		abbr.addContent(gapValue);
		rootElement.addContent(abbr);

		propagate2NextSibling(gapText);
	}

	@Override
	public void visit(UnclearText unclearText) {
		Element span = new Element("span");
		span.setAttribute("style", "text-shadow: black 0.0em 0.0em 0.1em; -webkit-filter: blur(0.005em);");
		rootElement.addContent(span);

		Element abbr = new Element("abbr");
		abbr.setAttribute("title", "unclearText.getReason().getDesc()");
		span.addContent(abbr);

		propagate2FirstChild(unclearText);

		rootElement = rootElement.getParentElement().getParentElement();

		propagate2NextSibling(unclearText);
	}

	@Override
	public void visit(NoteText noteText) {
		int number = 0;
		for (AnnexNote annexNote : noteText.getAnnexNoteSet()) {
			if (annexNote.getFragInter() == fragInter) {
				number = annexNote.getNumber();
			}
		}

		Element abbr = new Element("abbr");
		abbr.setAttribute("title", "");
		rootElement.addContent(abbr);

		rootElement = abbr;

		propagate2FirstChild(noteText);

		abbr.addContent("(" + number + ")");

		rootElement = rootElement.getParentElement();

		propagate2NextSibling(noteText);
	}

	@Override
	public void visit(RefText refText) {
		propagate2FirstChild(refText);
		propagate2NextSibling(refText);
	}

	private void generateRendition(Set<Rend> renditions) {
		Element elem = null;
		for (Rend rend : renditions) {
			if (rend.getRend() == Rendition.RIGHT) {
				elem = new Element("div");
				elem.setAttribute("class", "text-right");
			} else if (rend.getRend() == Rendition.LEFT) {
				elem = new Element("div");
				elem.setAttribute("class", "text-left");
			} else if (rend.getRend() == Rendition.CENTER) {
				elem = new Element("div");
				elem.setAttribute("class", "text-center");
			} else if (rend.getRend() == Rendition.BOLD) {
				elem = new Element("strong");
			} else if (rend.getRend() == Rendition.ITALIC) {
				elem = new Element("em");
			} else if (rend.getRend() == Rendition.RED) {
				elem = new Element("span");
				elem.setAttribute("style", "color: rgb(255,0,0);");
			} else if (rend.getRend() == Rendition.GREEN) {
				elem = new Element("span");
				elem.setAttribute("style", "color: rgb(0,255,0);");
			} else if (rend.getRend() == Rendition.UNDERLINED) {
				elem = new Element("u");
			} else if (rend.getRend() == Rendition.SUPERSCRIPT) {
				elem = new Element("sup");
			} else if (rend.getRend() == Rendition.SUBSCRIPT) {
				elem = new Element("sub");
			}
			rootElement.addContent(elem);
			rootElement = elem;
		}
	}

}
