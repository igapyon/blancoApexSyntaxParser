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
import blanco.apex.syntaxparser.BlancoApexSyntaxConstants;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxMethodToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxParenthesisToken;

public class BlancoApexSyntaxMethodParser extends AbstractBlancoApexSyntaxSyntaxParser {
	public static final boolean ISDEBUG = true;

	final BlancoApexSyntaxMethodToken methodToken = new BlancoApexSyntaxMethodToken();

	public BlancoApexSyntaxMethodParser(final BlancoApexSyntaxParserInput input) {
		super(input);
	}

	public BlancoApexSyntaxMethodToken parse() {
		if (ISDEBUG)
			System.out.println("method parser: begin: " + input.getIndex());

		boolean isDefineArea = true;

		try {
			for (input.markRead(); input.availableToken(); input.markRead()) {
				final BlancoApexToken sourceToken = input.readToken();
				if (sourceToken instanceof BlancoApexSpecialCharToken) {
					final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) sourceToken;

					// processing () needed.

					if (specialCharToken.getValue().equals("(")) {
						input.resetRead();

						isDefineArea = false;

						// start method def.
						final BlancoApexSyntaxParenthesisToken parenthesisToken = new BlancoApexSyntaxParenthesisParser(
								input).parse();
						methodToken.getTokenList().add(parenthesisToken);
						methodToken.getDefineArgsList().add(parenthesisToken);
					} else if (specialCharToken.getValue().equals("{")) {
						isDefineArea = false;

						input.resetRead();
						// start class def.
						methodToken.getTokenList()
								.add(new BlancoApexSyntaxBlockParser(input, BlockType.METHOD_DEF).parse());

						// exit process.
						return methodToken;
					} else if (specialCharToken.getValue().equals(";")) {
						isDefineArea = false;

						// seems non body method.
						methodToken.getTokenList().add(sourceToken);
						// exit process.
						return methodToken;
					} else {
						methodToken.getTokenList().add(sourceToken);

						if (isDefineArea) {
							methodToken.getDefineList().add(sourceToken);
						}
					}
				} else {
					if (isDefineArea) {
						if (methodToken.getReturn() == null) {
							// before method name.
							if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(sourceToken.getValue(),
									BlancoApexSyntaxConstants.MODIFIER_KEYWORDS)) {
								input.resetRead();
								methodToken.setModifiers(new BlancoApexSyntaxModifierParser(input).parse());
								methodToken.getTokenList().add(methodToken.getModifiers());
							} else {
								input.resetRead();
								methodToken.setReturn(new BlancoApexSyntaxTypeParser(input).parse());
								methodToken.getTokenList().add(methodToken.getReturn());
							}
							methodToken.getDefineList().add(sourceToken);
						} else {
							methodToken.getDefineList().add(sourceToken);
						}
					}
					methodToken.getTokenList().add(sourceToken);
				}
			}
		} finally {
			if (ISDEBUG)
				System.out.println(
						"method parser: end: " + methodToken.getDefineString() + methodToken.getDefineArgsString());

			// update mark
			input.markRead();
		}

		return methodToken;
	}
}
