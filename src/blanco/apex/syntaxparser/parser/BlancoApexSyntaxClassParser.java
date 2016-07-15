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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxClassToken;

public class BlancoApexSyntaxClassParser extends AbstractBlancoApexSyntaxSyntaxParser {
	public static final boolean ISDEBUG = false;

	protected final BlancoApexSyntaxClassToken classToken = new BlancoApexSyntaxClassToken();

	public BlancoApexSyntaxClassParser(final BlancoApexSyntaxParserInput input) {
		super(input);
	}

	public BlancoApexSyntaxClassToken parse() {
		if (ISDEBUG)
			System.out.println("class parser: begin: " + input.getIndex());

		boolean isDefineArea = true;

		try {
			for (input.markRead(); input.availableToken(); input.markRead()) {
				final BlancoApexToken sourceToken = input.readToken();
				if (sourceToken instanceof BlancoApexSpecialCharToken) {
					final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) sourceToken;
					if (specialCharToken.getValue().equals("{")) {
						input.resetRead();
						isDefineArea = false;

						parseClassBlock();

						// prevent process.
						return classToken;
					} else {
						classToken.getTokenList().add(sourceToken);

						if (isDefineArea) {
							// save define area.
							classToken.getDefineList().add(sourceToken);
						}
					}
				} else {
					// will be class name or something.
					classToken.getTokenList().add(sourceToken);

					if (isDefineArea) {
						// save define area.
						classToken.getDefineList().add(sourceToken);
					}
				}
			}
		} finally {
			if (ISDEBUG)
				System.out.println("class parser: end: " + input.getIndex());

			// update mark
			input.markRead();
		}

		return classToken;
	}

	protected void parseClassBlock() {

		// consume {
		classToken.getTokenList().add(input.readToken());

		for (; input.availableToken(); input.markRead()) {
			if (ISDEBUG)
				System.out.println("class parser: process(" + input.getIndex() + "): "
						+ input.getTokenAt(input.getIndex()).getDisplayString());

			final BlancoApexToken inputToken = input.readToken();

			if (inputToken instanceof BlancoApexWordToken) {
				final BlancoApexWordToken wordToken = (BlancoApexWordToken) inputToken;
				final BlancoApexSpecialCharToken nextSpecial = BlancoApexSyntaxUtil
						.getFirstSpecialCharTokenWithPreviousOne(input, new String[] { ";", "=", "(", "{" });
				if (nextSpecial == null) {
					classToken.getTokenList().add(inputToken);
					System.out.println("class parser: Unexpected (L88): " + input.getIndex());
				} else {
					if (nextSpecial.getValue().equals(";")) {
						// field
						input.resetRead();
						classToken.getTokenList().add(new BlancoApexSyntaxFieldParser(input).parse());
					} else if (nextSpecial.getValue().equals("=")) {
						// field with load value
						input.resetRead();
						classToken.getTokenList().add(new BlancoApexSyntaxFieldParser(input).parse());
					} else if (nextSpecial.getValue().equals("(")) {
						// method
						input.resetRead();
						classToken.getTokenList().add(new BlancoApexSyntaxMethodParser(input).parse());
					} else if (nextSpecial.getValue().equals("{")) {
						// will be class or property.
						final BlancoApexToken checkNextToken = BlancoApexSyntaxUtil.getFirstTokenByValue(input,
								new String[] { "class", "interface", "{" });
						if (checkNextToken == null) {
							System.out.println("class parser: unexpected (L101)state: " + input.getIndex());
							classToken.getTokenList().add(inputToken);
						} else if (checkNextToken.getValue().equals("{")) {
							// property;
							input.resetRead();
							classToken.getTokenList().add(new BlancoApexSyntaxPropertyParser(input).parse());
						} else {
							input.resetRead();
							classToken.getTokenList().add(new BlancoApexSyntaxClassParser(input).parse());
						}
					}
				}
			} else if (inputToken instanceof BlancoApexSpecialCharToken) {
				final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
				if (specialCharToken.getValue().equals("}")) {
					// end of class def.
					classToken.getTokenList().add(inputToken);
					return;
				} else if (specialCharToken.getValue().equals("@")) {
					input.resetRead();
					classToken.getTokenList().add(new BlancoApexSyntaxAnnotationParser(input).parse());
				} else if (specialCharToken.getValue().equals("{")) {
					// I'm not sure, but Apex has non-named method? Is it true?
					input.resetRead();
					classToken.getTokenList().add(new BlancoApexSyntaxBlockParser(input, BlockType.UNDEFINED).parse());
				} else {
					System.out.println("class parser: unexpected state(L123): " + specialCharToken.getDisplayString());
					classToken.getTokenList().add(inputToken);
				}
			} else {
				classToken.getTokenList().add(inputToken);
			}
		}

		return;
	}
}
