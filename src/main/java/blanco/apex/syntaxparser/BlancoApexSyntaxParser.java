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
import java.util.ArrayList;
import java.util.List;

import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.syntaxparser.parser.BlancoApexSyntaxSourceParser;

/**
 * Simple Apex language Syntax Parser.
 * 
 * <p>
 * This is Syntax Parser of Apex written in Java.
 * </p>
 * 
 * <p>
 * Useage:
 * </p>
 * 
 * <code>
 * List&lt;BlancoApexToken&gt; sourceTokenList = new BlancoApexParser().parse(fileString);
 * List&lt;BlancoApexToken&gt; tokenList = new BlancoApexSyntaxParser().parse(sourceTokenList);
 * BlancoApexSyntaxUtil.dumpAsTokenTree(tokenList);
 * </code>
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxParser {
    /**
     * output token list.
     */
    protected List<BlancoApexToken> tokenList = new ArrayList<BlancoApexToken>();

    /**
     * Entry point of Apex syntax parser.
     * 
     * @param sourceTokenList
     *            List of token of source code
     * @return Formatted list of token.
     */
    public List<BlancoApexToken> parse(final List<BlancoApexToken> sourceTokenList) {
        final BlancoApexSyntaxParserInput input = new BlancoApexSyntaxParserInput(sourceTokenList);

        tokenList.add(new BlancoApexSyntaxSourceParser(input).parse());

        return tokenList;
    }

    /**
     * Entry point of Apex syntax parser.
     * 
     * @param sourceTokenList
     *            List of token of source code
     * @param sourceFile
     *            File of source code. It will used to know file information.
     * @return Formatted list of token.
     */
    public List<BlancoApexToken> parse(final List<BlancoApexToken> sourceTokenList, final File sourceFile) {
        final BlancoApexSyntaxParserInput input = new BlancoApexSyntaxParserInput(sourceTokenList);

        tokenList.add(new BlancoApexSyntaxSourceParser(input, sourceFile).parse());

        return tokenList;
    }
}
