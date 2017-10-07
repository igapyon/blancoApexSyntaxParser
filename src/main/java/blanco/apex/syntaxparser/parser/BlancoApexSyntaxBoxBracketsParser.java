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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBoxBracketsToken;

/**
 * Syntax parser for box brackets [].
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxBoxBracketsParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxBoxBracketsToken boxbracketsToken = new BlancoApexSyntaxBoxBracketsToken();

    public BlancoApexSyntaxBoxBracketsParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    /**
     * Parse token from current position.
     * 
     * @return Result of parse.
     */
    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxBoxBracketsToken parse() {
        if (ISDEBUG)
            System.out.println("boxbrackets parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        // consume '['
        boxbracketsToken.getTokenList().add(input.readToken());

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("boxbrackets parser: process(" + input.getIndex() + "): "
                            + input.getTokenAt(input.getIndex()).getDisplayString());

                if (inputToken instanceof BlancoApexWordToken) {
                    if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(inputToken.getValue(), new String[] { "select" })) {
                        input.resetRead();
                        boxbracketsToken.getTokenList().add(new BlancoApexSyntaxSOQLParser(input).parse());
                    } else {
                        boxbracketsToken.getTokenList().add(inputToken);
                    }
                } else if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("]")) {
                        // end of boxbrackets.
                        boxbracketsToken.getTokenList().add(inputToken);
                        return boxbracketsToken;
                    } else if (specialCharToken.getValue().equals("[")) {
                        // entering new nested one.
                        input.resetRead();
                        boxbracketsToken.getTokenList().add(new BlancoApexSyntaxBoxBracketsParser(input).parse());
                    } else if (specialCharToken.getValue().equals("(")) {
                        // non SOQL and starts (
                        input.resetRead();
                        boxbracketsToken.getTokenList().add(new BlancoApexSyntaxParenthesisParser(input).parse());
                    } else {
                        boxbracketsToken.getTokenList().add(inputToken);
                    }
                } else {
                    boxbracketsToken.getTokenList().add(inputToken);
                }
            }
        } finally

        {
            if (ISDEBUG)
                System.out.println("boxbrackets parser: end: " + input.getIndex());
        }

        return boxbracketsToken;
    }
}
