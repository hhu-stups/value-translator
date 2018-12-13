package de.hhu.stups.prob.translator;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BAtomTest{

	BAtom atom;

	@Before
	public void setUp() throws Exception {
		this.atom = Translator.translate("atom");
	}

	@Test
	public void testToString() {
		assertEquals("atom", this.atom.toString());
	}

	@Test
	public void testEquals() throws BCompoundException {
	    assertEquals(Translator.translate("atom"), this.atom);
	    assertNotEquals(Translator.translate("other"), this.atom);
	}

	@Test
	public void testStringValue() {
		assertEquals("atom", this.atom.stringValue());
	}
}