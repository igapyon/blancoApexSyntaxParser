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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.junit.Test;

import blanco.apex.parser.BlancoApexParser;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.syntaxparser.token.AbstractBlancoApexSyntaxToken;

public class BlancoApexSyntaxParserSimpleTest {

	@Test
	public void testReadFukugen() throws Exception {
		final String fileString = file2String(new File("./test/data/apex/MySimpleTest.cls"));

		final List<BlancoApexToken> sourceTokenList = new BlancoApexParser().parse(fileString);

		final List<BlancoApexToken> tokenList = new BlancoApexSyntaxParser().parse(sourceTokenList);

		for (BlancoApexToken token : tokenList) {
			if (token instanceof AbstractBlancoApexSyntaxToken) {
				System.out.println(token.getClass().getSimpleName());
				displayDebug((AbstractBlancoApexSyntaxToken) token, 1);
			} else {
				System.out.println("    " + token.getDisplayString());
			}
		}
	}

	void displayDebug(final AbstractBlancoApexSyntaxToken tokenParent, final int indentLevel) {
		for (BlancoApexToken token : tokenParent.getTokenList()) {
			if (token instanceof AbstractBlancoApexSyntaxToken) {
				System.out.println(getIndentString(indentLevel) + token.getClass().getSimpleName());
				displayDebug((AbstractBlancoApexSyntaxToken) token, indentLevel + 1);
			} else {
				System.out.println(getIndentString(indentLevel) + token.getDisplayString());
			}
		}
	}

	static final String getIndentString(final int level) {
		final StringBuffer strbuf = new StringBuffer();
		for (int index = 0; index < level * 4; index++) {
			strbuf.append(' ');
		}
		return strbuf.toString();
	}

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
}
