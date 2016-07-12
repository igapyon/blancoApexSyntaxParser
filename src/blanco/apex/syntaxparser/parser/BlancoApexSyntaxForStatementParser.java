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
package blanco.apex.syntaxparser.parser;

import blanco.apex.parser.token.BlancoApexSpecialCharToken;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxForStatementToken;

public class BlancoApexSyntaxForStatementParser extends AbstractBlancoApexSyntaxSyntaxParser {
	public static final boolean ISDEBUG = false;

	protected final BlancoApexSyntaxForStatementToken forStatementToken = new BlancoApexSyntaxForStatementToken();

	public BlancoApexSyntaxForStatementParser(final BlancoApexSyntaxParserInput input) {
		super(input);
	}

	public BlancoApexSyntaxForStatementToken parse() {
		if (ISDEBUG)
			System.out.println("for_statement parser: begin: " + input.getIndex() + ": "
					+ input.getTokenAt(input.getIndex()).getDisplayString());

		// for () {}
		// for () aaa;

		try {
			for (input.markRead(); input.availableToken(); input.markRead()) {
				final BlancoApexToken inputToken = input.readToken();

				if (ISDEBUG)
					System.out.println("for_statement parser: process(" + input.getIndex() + "): "
							+ input.getTokenAt(input.getIndex()).getDisplayString());

				if (inputToken instanceof BlancoApexSpecialCharToken) {
					final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
					if (specialCharToken.getValue().equals("(")) {
						input.resetRead();
						forStatementToken.getTokenList().add(new BlancoApexSyntaxParenthesisParser(input).parse());
						// () processed.
						break;
					} else {
						forStatementToken.getTokenList().add(inputToken);
					}
				} else {
					forStatementToken.getTokenList().add(inputToken);
				}
			}

			processForStatementBody();

		} finally {
			if (ISDEBUG)
				System.out.println("for_statement parser: end: " + input.getIndex());
		}

		return forStatementToken;
	}

	void processForStatementBody() {
		for (input.markRead(); input.availableToken(); input.markRead()) {
			final BlancoApexToken inputToken = input.readToken();

			if (ISDEBUG)
				System.out.println("for_statement parser: process body (" + input.getIndex() + "): "
						+ input.getTokenAt(input.getIndex()).getDisplayString());

			if (inputToken instanceof BlancoApexSpecialCharToken) {
				final BlancoApexSpecialCharToken nextSpecial = BlancoApexSyntaxUtil
						.getFirstSpecialCharTokenWithPreviousOne(input,
								new String[] { ";"/* simple statement */, "{"/* block */ });
				if (nextSpecial == null) {
					forStatementToken.getTokenList().add(inputToken);
					System.out.println("block parser: Unexpected: " + input.getIndex());
				} else if (nextSpecial.getValue().equals(";")) {
					// System.out.println("FOR TRACE: single line.");
					input.resetRead();
					forStatementToken.getTokenList().add(new BlancoApexSyntaxStatementParser(input).parse());
					break;
				} else if (nextSpecial.getValue().equals("{")) {
					// System.out.println("FOR TRACE: block.");
					input.resetRead();
					forStatementToken.getTokenList().add(new BlancoApexSyntaxBlockParser(input).parse());
					break;
				} else {
					System.out.println("for_statement parser: Unexpected one: " + inputToken.getDisplayString());
				}
			} else {
				forStatementToken.getTokenList().add(inputToken);
			}
		}
	}
}
