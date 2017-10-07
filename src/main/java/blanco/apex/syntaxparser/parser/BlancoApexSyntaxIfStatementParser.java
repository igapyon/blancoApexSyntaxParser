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
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxIfStatementToken;

/**
 * Syntax parser for 'if statement'.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxIfStatementParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxIfStatementToken ifStatementToken = new BlancoApexSyntaxIfStatementToken();

    public BlancoApexSyntaxIfStatementParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    /**
     * Parse token from current position.
     * 
     * @return Result of parse.
     */
    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxIfStatementToken parse() {
        if (ISDEBUG)
            System.out.println("if_statement parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        // if () {}
        // if () {} aaa;
        // if () {} else {}
        // if () aaa; else bbb;

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("if_statement parser: process(" + input.getIndex() + "): "
                            + input.getTokenAt(input.getIndex()).getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("(")) {
                        input.resetRead();
                        ifStatementToken.getTokenList().add(new BlancoApexSyntaxParenthesisParser(input).parse());
                        // () processed.
                        break;
                    } else {
                        ifStatementToken.getTokenList().add(inputToken);
                    }
                } else {
                    ifStatementToken.getTokenList().add(inputToken);
                }
            }

            processIfStatementBody();

            input.markRead();

            // check else exist or not.
            for (;;) {
                final BlancoApexToken nextToken = BlancoApexSyntaxUtil.getFirstTokenWithoutWhitespaceNewline(input);
                if (nextToken == null) {
                    input.resetRead();
                    break;
                }
                if (false == nextToken.getValue().equalsIgnoreCase("else")) {
                    // non else statement
                    break;
                }

                // else found.
                final BlancoApexToken next2Token = BlancoApexSyntaxUtil.getFirstTokenWithoutWhitespaceNewline(input);
                if (next2Token == null) {
                    // FIXME this should not be.
                    input.resetRead();
                    break;
                }

                if (next2Token.getValue().equalsIgnoreCase("if")) {
                    // }else if{ style
                    input.resetRead();
                    processIfStatementBody();
                    input.markRead();
                    continue;
                } else {
                    // simple }else{ token
                    input.resetRead();
                    processIfStatementBody();
                    input.markRead();
                    break;
                }
            }

            input.resetRead();

        } finally {
            if (ISDEBUG)
                System.out.println("if_statement parser: end: " + input.getIndex());
        }

        return ifStatementToken;
    }

    @SuppressWarnings("deprecation")
    void processIfStatementBody() {
        for (input.markRead(); input.availableToken(); input.markRead()) {
            final BlancoApexToken inputToken = input.readToken();

            if (ISDEBUG)
                System.out.println("if_statement parser: process(" + input.getIndex() + "): "
                        + input.getTokenAt(input.getIndex()).getDisplayString());

            if (inputToken instanceof BlancoApexSpecialCharToken) {
                final BlancoApexSpecialCharToken nextSpecial = BlancoApexSyntaxUtil
                        .getFirstSpecialCharTokenWithPreviousOne(input,
                                new String[] { ";"/* simple statement */, "{"/* block */ });
                if (nextSpecial == null) {
                    ifStatementToken.getTokenList().add(inputToken);
                    System.out.println("block parser: Unexpected: " + input.getIndex());
                } else if (nextSpecial.getValue().equals(";")) {
                    input.resetRead();
                    ifStatementToken.getTokenList().add(new BlancoApexSyntaxStatementParser(input).parse());
                    break;
                } else if (nextSpecial.getValue().equals("{")) {
                    input.resetRead();
                    ifStatementToken.getTokenList()
                            .add(new BlancoApexSyntaxBlockParser(input, BlockType.MULTI_STATEMENT).parse());
                    break;
                } else {
                    System.out.println("if_statement parser: Unexpected one: ");
                }
            } else {
                ifStatementToken.getTokenList().add(inputToken);
            }
        }
    }
}
