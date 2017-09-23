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

import java.io.File;

import blanco.apex.parser.token.BlancoApexCommentToken;
import blanco.apex.parser.token.BlancoApexNewlineToken;
import blanco.apex.parser.token.BlancoApexSpecialCharToken;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.parser.token.BlancoApexWhitespaceToken;
import blanco.apex.parser.token.BlancoApexWordToken;
import blanco.apex.syntaxparser.BlancoApexSyntaxConstants;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxSourceToken;

/**
 * Source file parser
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxSourceParser extends AbstractBlancoApexSyntaxSyntaxParser {
	public static final boolean ISDEBUG = false;

	protected final BlancoApexSyntaxSourceToken sourceToken = new BlancoApexSyntaxSourceToken();

	public BlancoApexSyntaxSourceParser(final BlancoApexSyntaxParserInput input) {
		super(input);
	}

	public BlancoApexSyntaxSourceParser(final BlancoApexSyntaxParserInput input, final File sourceFile) {
		super(input);

		sourceToken.setSourceFile(sourceFile);
	}

	public BlancoApexSyntaxSourceToken parse() {

		if (ISDEBUG)
			System.out.println("source parser: begin: " + input.getIndex());

		try {
			for (input.markRead(); input.availableToken(); input.markRead()) {
				final BlancoApexToken inputToken = input.readToken();

				if (inputToken instanceof BlancoApexWhitespaceToken || inputToken instanceof BlancoApexNewlineToken
						|| inputToken instanceof BlancoApexCommentToken) {
					// skipping whitespace and comments simply.

					// simple copy.
					sourceToken.getTokenList().add(inputToken);
				} else if (inputToken instanceof BlancoApexWordToken) {
					final BlancoApexWordToken wordToken = (BlancoApexWordToken) inputToken;

					// reserved word checking.
					if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(wordToken.getValue(),
							BlancoApexSyntaxConstants.RESERVED_KEYWORDS)) {
						input.resetRead();
						parseSourceReservedKeyword();
					} else {
						// maybe name
						System.out.println(
								"SOURCE(L77): Unexpected token: simply copy. It should be keyword or annotations...: "
										+ wordToken.getDisplayString());
						sourceToken.getTokenList().add(inputToken);
					}
				} else if (inputToken instanceof BlancoApexSpecialCharToken) {
					final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
					if (specialCharToken.getValue().equals("@")) {
						input.resetRead();
						sourceToken.getTokenList().add(new BlancoApexSyntaxAnnotationParser(input).parse());
					} else {
						// copy simply.
						System.out.println(
								"SOURCE(L89): Unexpected token. simply copy.: " + inputToken.getDisplayString());
						sourceToken.getTokenList().add(inputToken);
					}
				} else {
					System.out.println("SOURCE(L93): Unexpected token. simply copy.: " + inputToken.getDisplayString());
					sourceToken.getTokenList().add(inputToken);
				}
			}
		} finally {
			if (ISDEBUG)
				System.out.println("source parser: end: " + input.getIndex());
		}

		return sourceToken;
	}

	/**
	 * Seeking phase.
	 */
	protected void parseSourceReservedKeyword() {
		// class start!!!

		// mark here!
		input.markRead();

		for (; input.availableToken();) {
			final BlancoApexToken inputToken = input.readToken();

			// seek class or interface or something...
			if (inputToken instanceof BlancoApexWordToken) {
				final BlancoApexWordToken wordToken = (BlancoApexWordToken) inputToken;
				if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(wordToken.getValue(),
						BlancoApexSyntaxConstants.RESERVED_KEYWORDS)) {
					wordToken.setReservedKeyword(true);
				}

				if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(wordToken.getValue(),
						BlancoApexSyntaxConstants.MODIFIER_KEYWORDS)) {
					// will be modifier of class.
					// skip and read next
				} else if (wordToken.getValue().equalsIgnoreCase("class")
						|| wordToken.getValue().equalsIgnoreCase("interface")) {
					// found class!
					// TODO I'm not sure interface is equals to class or
					// not.

					// reset mark and parse from beginning.
					input.resetRead();

					// open class parser.
					sourceToken.getTokenList().add(new BlancoApexSyntaxClassParser(input).parse());

					// exit here!
					return;

				} else if (wordToken.getValue().equalsIgnoreCase("enum")) {
					// reset mark and parse from beginning.
					input.resetRead();

					// open class parser.
					sourceToken.getTokenList().add(new BlancoApexSyntaxEnumParser(input).parse());

					// exit here!
					return;

				} else {
					System.out.println("SOURCE(L155): non supported token.: " + inputToken.getDisplayString());
				}
			} else {
				// do nothing
				// ignorable for seeking.
			}
		}

		{
			// dislike path.
			// simply consume token.
			input.resetRead();
			final BlancoApexToken inputToken = input.readToken();
			sourceToken.getTokenList().add(inputToken);
			System.out.println("SOURCE(L169): simply process given token.: " + inputToken.getDisplayString());
		}
	}
}
