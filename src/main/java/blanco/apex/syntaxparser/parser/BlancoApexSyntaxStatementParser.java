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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxStatementToken;

public class BlancoApexSyntaxStatementParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    final BlancoApexSyntaxStatementToken statementToken = new BlancoApexSyntaxStatementToken();

    public BlancoApexSyntaxStatementParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxStatementToken parse() {
        if (ISDEBUG)
            System.out.println("statement parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        statementToken.getTokenList().add(input.readToken());

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("statement parser: process(" + (input.getIndex() - 1) + "): "
                            + input.getTokenAt(input.getIndex() - 1).getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals(";")) {
                        // end of statement.
                        statementToken.getTokenList().add(inputToken);
                        return statementToken;
                    } else if (specialCharToken.getValue().equals("}")) {
                        input.resetRead();
                        return statementToken;
                    } else if (specialCharToken.getValue().equals("{")) {
                        // entering new nested block.
                        input.resetRead();
                        statementToken.getTokenList()
                                .add(new BlancoApexSyntaxBlockParser(input, BlockType.MULTI_STATEMENT).parse());
                    } else if (specialCharToken.getValue().equals("(")) {
                        // entering new nested block.
                        input.resetRead();
                        statementToken.getTokenList().add(new BlancoApexSyntaxParenthesisParser(input).parse());
                    } else if (specialCharToken.getValue().equals("[")) {
                        // entering new nested block.
                        input.resetRead();
                        statementToken.getTokenList().add(new BlancoApexSyntaxBoxBracketsParser(input).parse());
                    } else {
                        statementToken.getTokenList().add(inputToken);
                    }
                } else {
                    statementToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("statement parser: end: " + input.getIndex());
        }

        return statementToken;
    }
}
