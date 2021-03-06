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

import java.util.ArrayList;
import java.util.List;

import blanco.apex.parser.token.BlancoApexSpecialCharToken;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.parser.token.BlancoApexWordToken;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxTypeToken;

/**
 * Syntax parser for type.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxTypeParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxTypeToken typeToken = new BlancoApexSyntaxTypeToken();

    /**
     * Construct parser instance.
     * 
     * @param input
     *            Input setting/information of syntax parser.
     */
    public BlancoApexSyntaxTypeParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    /**
     * Parse token from current position.
     * 
     * @return Result of parse.
     */
    public BlancoApexSyntaxTypeToken parse() {
        if (ISDEBUG)
            System.out.println("type parser: begin");

        // 最初の読み込みはなし。

        try {
            // 2つめのWORDが来たら終わる。<>ではないところでカンマが来ても終わる。
            String typeName = "";
            int deapthDiamond = 0;
            final List<BlancoApexToken> keepTokenList = new ArrayList<BlancoApexToken>();
            for (input.markRead(); input.availableToken();) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println(
                            "type parser: process(" + input.getIndex() + "): " + inputToken.getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("<")) {
                        deapthDiamond++;
                    } else if (specialCharToken.getValue().equals(">")) {
                        deapthDiamond--;
                    } else if (specialCharToken.getValue().equals(",") || specialCharToken.getValue().equals(";")) {
                        if (deapthDiamond == 0) {
                            // end!
                            input.resetRead();
                            break;
                        }
                    }
                    keepTokenList.add(inputToken);
                    typeToken.getTokenList().addAll(keepTokenList);
                    keepTokenList.clear();
                    input.markRead();
                    typeName += inputToken.getValue();
                } else if (inputToken instanceof BlancoApexWordToken) {
                    if (deapthDiamond == 0) {
                        if (typeName.trim().length() > 0) {
                            // end!
                            input.resetRead();
                            break;
                        }
                    }

                    keepTokenList.add(inputToken);
                    typeToken.getTokenList().addAll(keepTokenList);
                    keepTokenList.clear();
                    input.markRead();
                    typeName += inputToken.getValue();
                } else {
                    keepTokenList.add(inputToken);
                }
            }
            typeToken.setValue(typeName);
        } finally {
            if (ISDEBUG)
                System.out.println("type parser: end: " + input.getIndex());
        }

        return typeToken;
    }
}
