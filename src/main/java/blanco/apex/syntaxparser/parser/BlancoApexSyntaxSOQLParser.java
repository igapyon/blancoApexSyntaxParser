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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxSOQLToken;

/**
 * Syntax parser for SOQL.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxSOQLParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    final BlancoApexSyntaxSOQLToken soqlToken = new BlancoApexSyntaxSOQLToken();

    public BlancoApexSyntaxSOQLParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxSOQLToken parse() {
        if (ISDEBUG)
            System.out.println("soql parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("soql parser: process(" + input.getIndex() + "): "
                            + input.getTokenAt(input.getIndex()).getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    // ' is already processed by lexical parser.

                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("]")) {
                        // end of boxbrackets. = end of SOQL.
                        input.resetRead();
                        return soqlToken;
                    } else {
                        soqlToken.getTokenList().add(inputToken);
                    }
                } else {
                    soqlToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("soql parser: end: " + input.getIndex());
        }

        // ここには来ないはず。
        return soqlToken;
    }
}
