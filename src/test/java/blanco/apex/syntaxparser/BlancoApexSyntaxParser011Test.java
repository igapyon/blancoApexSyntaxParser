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

import java.util.List;

import org.junit.Test;

import blanco.apex.parser.BlancoApexParser;
import blanco.apex.parser.token.BlancoApexToken;

public class BlancoApexSyntaxParser011Test {

	@Test
	public void test() throws Exception {
		// TODO change apex class filename to adapt your environment.
		final List<BlancoApexToken> sourceTokenList = new BlancoApexParser().parse( //
				"public class myOuterClass {\n" //
						+ "    // code here\n" //
						+ "    class myInnerClass {\n" //
						+ "    // inner code here\n" //
						+ "   }\n" //
						+ "}"); //

		final List<BlancoApexToken> tokenList = new BlancoApexSyntaxParser().parse(sourceTokenList);
		final StringBuffer strbuf = new StringBuffer();
		for (BlancoApexToken token : tokenList) {
			strbuf.append(token.getDisplayString());
		}

		if (false)
			System.out.println(strbuf.toString());

		assertEquals("check changes.",
				"SOURCE[CLASS[MODIFIER[WORD[public]],WHITESPACE[ ],WORD[class],WHITESPACE[ ],WORD[myOuterClass],WHITESPACE[ ],BLOCK(CLASS_DEF)[SPECIAL_CHAR[{],NEWLINE[n],WHITESPACE[    ],COMMENT(SINGLE_LINE)[// code here],NEWLINE[n],WHITESPACE[    ],CLASS[WORD[class],WHITESPACE[ ],WORD[myInnerClass],WHITESPACE[ ],BLOCK(CLASS_DEF)[SPECIAL_CHAR[{],NEWLINE[n],WHITESPACE[    ],COMMENT(SINGLE_LINE)[// inner code here],NEWLINE[n],WHITESPACE[   ],SPECIAL_CHAR[}]]],NEWLINE[n],SPECIAL_CHAR[}]]]]",
				strbuf.toString());
		// System.out.println(BlancoApexParserUtil.tokenList2String(tokenList));
	}
}
