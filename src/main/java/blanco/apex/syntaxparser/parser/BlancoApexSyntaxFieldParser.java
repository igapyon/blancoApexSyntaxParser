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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxFieldToken;

public class BlancoApexSyntaxFieldParser extends AbstractBlancoApexSyntaxSyntaxParser {
	public static final boolean ISDEBUG = false;

	final BlancoApexSyntaxFieldToken fieldToken = new BlancoApexSyntaxFieldToken();

	public BlancoApexSyntaxFieldParser(final BlancoApexSyntaxParserInput input) {
		super(input);
	}

	public BlancoApexSyntaxFieldToken parse() {
		if (ISDEBUG)
			System.out.println("field parser: begin: " + input.getIndex() + ": "
					+ input.getTokenAt(input.getIndex()).getDisplayString());

		fieldToken.getTokenList().add(input.readToken());

		try {
			for (input.markRead(); input.availableToken(); input.markRead()) {
				final BlancoApexToken inputToken = input.readToken();

				if (ISDEBUG)
					System.out.println("field parser: process(" + input.getIndex() + "): "
							+ input.getTokenAt(input.getIndex()).getDisplayString());

				if (inputToken instanceof BlancoApexSpecialCharToken) {
					final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
					if (specialCharToken.getValue().equals(";")) {
						// end of statement.
						fieldToken.getTokenList().add(inputToken);
						return fieldToken;
					} else {
						fieldToken.getTokenList().add(inputToken);
					}
				} else {
					fieldToken.getTokenList().add(inputToken);
				}
			}
		} finally {
			if (ISDEBUG)
				System.out.println("field parser: end: " + input.getIndex());
		}

		return fieldToken;
	}
}