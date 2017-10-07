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

import java.io.File;
import java.util.List;

import org.junit.Test;

import blanco.apex.parser.BlancoApexConstants;
import blanco.apex.parser.BlancoApexParser;
import blanco.apex.parser.token.BlancoApexToken;

public class BlancoApexSyntaxParser002Test {

    @Test
    public void testMain() throws Exception {
        System.err.println("Test simple syntax parsing.");
        System.err.println("    lexical parser: " + BlancoApexConstants.getVersion());
        System.err.println("     syntax parser: " + BlancoApexSyntaxConstants.getVersion());

        final String fileString = BlancoApexSyntaxParserTestCommon
                .file2String(new File("./test/data/apex/MySimpleTest2.cls"));

        final List<BlancoApexToken> sourceTokenList = new BlancoApexParser().parse(fileString);

        final List<BlancoApexToken> tokenList = new BlancoApexSyntaxParser().parse(sourceTokenList);

        BlancoApexSyntaxUtil.dumpAsTokenTree(tokenList);
    }
}
