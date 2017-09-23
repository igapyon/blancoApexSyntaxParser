/*
 * Copyright 2016 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blanco.apex.syntaxparser;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.junit.Test;

import blanco.apex.parser.BlancoApexParser;
import blanco.apex.parser.BlancoApexParserUtil;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.syntaxparser.token.AbstractBlancoApexSyntaxToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxAnnotationToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxClassToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxFieldToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxForStatementToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxIfStatementToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxMethodToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxModifierToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxParenthesisToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxPropertyToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxSourceToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxStatementToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxTypeToken;

public class BlancoApexSyntaxParserTest {
	static final String file2String(final File file) throws IOException {
		final StringWriter writer = new StringWriter();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		final char[] cbuf = new char[4096];
		for (;;) {
			final int length = reader.read(cbuf);
			if (length <= 0) {
				break;
			}
			writer.write(cbuf, 0, length);
		}
		reader.close();
		writer.close();

		return writer.toString();
	}

	@Test
	public void testReadFukugen() throws Exception {
		final File[] files = new File("./test/data/apex/").listFiles();
		for (File file : files) {
			if (file.isFile() && file.getName().toLowerCase().endsWith(".cls")) {
				final String fileString = file2String(file);

				System.out.println("Parse file [" + file.getName() + "]");

				final List<BlancoApexToken> sourceTokenList = new BlancoApexParser().parse(fileString);

				final List<BlancoApexToken> tokenList = new BlancoApexSyntaxParser().parse(sourceTokenList, file);

				if (false)
					if (fileString.equals(BlancoApexParserUtil.tokenList2String(tokenList)) == false) {
						System.out.println("Different: ");

						// System.out.println(BlancoApexParserUtil.tokenList2String(tokenList));
					}

				assertEquals("eq", fileString, BlancoApexParserUtil.tokenList2String(tokenList));
			}
		}
	}

	@Test
	public void testSingleFile() throws Exception {
		// TODO change apex class filename to adapt your environment.
		final List<BlancoApexToken> sourceTokenList = new BlancoApexParser()
				// .parse(new File("./test/data/apex/MySimpleTest.cls"));
				.parse(new File("./test/data/apex/MySimpleTest4.cls"));

		final List<BlancoApexToken> tokenList = new BlancoApexSyntaxParser().parse(sourceTokenList);

		if (true) {
			for (BlancoApexToken token : tokenList) {
				if (token instanceof AbstractBlancoApexSyntaxToken) {
					displayDebug((AbstractBlancoApexSyntaxToken) token);
				}
			}
		}
		if (true) {
			System.out.println(BlancoApexParserUtil.tokenList2String(tokenList));
		}
	}

	public static void displayDebug(final AbstractBlancoApexSyntaxToken token) throws Exception {
		if (token instanceof BlancoApexSyntaxSourceToken) {
			System.out.println("SOURCE: ");
			for (BlancoApexToken token2 : token.getTokenList()) {
				if (token2 instanceof AbstractBlancoApexSyntaxToken) {
					displayDebug((AbstractBlancoApexSyntaxToken) token2);
				}
			}
		} else if (token instanceof BlancoApexSyntaxClassToken) {
			System.out.println("CLASS: " + ((BlancoApexSyntaxClassToken) token).getDefineString());
			for (BlancoApexToken token2 : token.getTokenList()) {
				// System.out.println(token2.getDisplayString());
				if (token2 instanceof AbstractBlancoApexSyntaxToken) {
					displayDebug((AbstractBlancoApexSyntaxToken) token2);
				}
			}
		} else if (token instanceof BlancoApexSyntaxAnnotationToken) {
			System.out.println("    ANNOTATION: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxBlockToken) {
			System.out.println("    BLOCK: ");
			for (BlancoApexToken token2 : token.getTokenList()) {
				if (token2 instanceof AbstractBlancoApexSyntaxToken) {
					displayDebug((AbstractBlancoApexSyntaxToken) token2);
				}
			}
		} else if (token instanceof BlancoApexSyntaxMethodToken) {
			System.out.println("  METHOD: " + ((BlancoApexSyntaxMethodToken) token).getDefineString() + " : "
					+ ((BlancoApexSyntaxMethodToken) token).getDefineArgsString());
			for (BlancoApexToken token2 : token.getTokenList()) {
				if (token2 instanceof AbstractBlancoApexSyntaxToken) {
					displayDebug((AbstractBlancoApexSyntaxToken) token2);
				}
			}
		} else if (token instanceof BlancoApexSyntaxTypeToken) {
			System.out.println("  TYPE: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxModifierToken) {
			System.out.println("  MODIFIER: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxFieldToken) {
			System.out.println("  FIELD: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxIfStatementToken) {
			System.out.println("    IF_STATEMENT: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxForStatementToken) {
			System.out.println("    FOR_STATEMENT: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxParenthesisToken) {
			// System.out.println("PARENTHESIS: " + token.getValue());
		} else if (token instanceof BlancoApexSyntaxPropertyToken) {
			System.out.println("  PROPERTY: [" + token.getValue() + "]");
		} else if (token instanceof BlancoApexSyntaxStatementToken) {
			System.out.println("    STATEMENT: [" + token.getValue() + "]");
			// System.out.println(token.getDisplayString());
		} else {
			System.out.println("LISTUP: Unexpected: " + token.getDisplayString());
		}
	}
}
