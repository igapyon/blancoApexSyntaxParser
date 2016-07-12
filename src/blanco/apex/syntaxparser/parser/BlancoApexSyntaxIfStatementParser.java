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
import blanco.apex.parser.token.BlancoApexWordToken;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxIfStatementToken;

public class BlancoApexSyntaxIfStatementParser extends AbstractBlancoApexSyntaxSyntaxParser {
	public static final boolean ISDEBUG = false;

	protected final BlancoApexSyntaxIfStatementToken ifStatementToken = new BlancoApexSyntaxIfStatementToken();

	public BlancoApexSyntaxIfStatementParser(final BlancoApexSyntaxParserInput input) {
		super(input);
	}

	public BlancoApexSyntaxIfStatementToken parse() {
		if (ISDEBUG)
			System.out.println("if_statement parser: begin: " + input.getIndex() + ": "
					+ input.getTokenAt(input.getIndex()).getDisplayString());

		// if () {}
		// if () {} aaa;
		// if () {} else {}
		// if () aaa; else bbb;

		try {
			for (input.markRead(); input.availableToken(); input.markRead()) {
				final BlancoApexToken inputToken = input.readToken();

				if (ISDEBUG)
					System.out.println("if_statement parser: process(" + input.getIndex() + "): "
							+ input.getTokenAt(input.getIndex()).getDisplayString());

				if (inputToken instanceof BlancoApexSpecialCharToken) {
					final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
					if (specialCharToken.getValue().equals("(")) {
						input.resetRead();
						ifStatementToken.getTokenList().add(new BlancoApexSyntaxParenthesisParser(input).parse());
						// () processed.
						break;
					} else {
						ifStatementToken.getTokenList().add(inputToken);
					}
				} else {
					ifStatementToken.getTokenList().add(inputToken);
				}
			}

			processIfStatementBody();

			input.markRead();
			// check else exist?
			for (; input.availableToken();) {
				final BlancoApexToken inputToken = input.readToken();
				if (inputToken instanceof BlancoApexSpecialCharToken) {
					input.resetRead();
					break;
				} else if (inputToken instanceof BlancoApexWordToken) {
					if (inputToken.getValue().equalsIgnoreCase("else")) {
						// else found!!!
						input.resetRead();
						processIfStatementBody();
						input.markRead();
					} else {
						input.resetRead();
						break;
					}
				}
			}
			input.resetRead();

		} finally {
			if (ISDEBUG)
				System.out.println("if_statement parser: end: " + input.getIndex());
		}

		return ifStatementToken;
	}

	void processIfStatementBody() {
		for (input.markRead(); input.availableToken(); input.markRead()) {
			final BlancoApexToken inputToken = input.readToken();

			if (ISDEBUG)
				System.out.println("if_statement parser: process(" + input.getIndex() + "): "
						+ input.getTokenAt(input.getIndex()).getDisplayString());

			if (inputToken instanceof BlancoApexSpecialCharToken) {
				final BlancoApexSpecialCharToken nextSpecial = BlancoApexSyntaxUtil
						.getFirstSpecialCharTokenWithPreviousOne(input,
								new String[] { ";"/* simple statement */, "{"/* block */ });
				if (nextSpecial == null) {
					ifStatementToken.getTokenList().add(inputToken);
					System.out.println("block parser: Unexpected: " + input.getIndex());
				} else if (nextSpecial.getValue().equals(";")) {
					input.resetRead();
					ifStatementToken.getTokenList().add(new BlancoApexSyntaxStatementParser(input).parse());
					break;
				} else if (nextSpecial.getValue().equals("{")) {
					input.resetRead();
					ifStatementToken.getTokenList().add(new BlancoApexSyntaxBlockParser(input).parse());
					break;
				} else {
					System.out.println("if_statement parser: Unexpected one: ");
				}
			} else {
				ifStatementToken.getTokenList().add(inputToken);
			}
		}
	}
}
