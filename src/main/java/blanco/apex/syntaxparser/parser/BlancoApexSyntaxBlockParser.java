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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;

/**
 * Syntax parser for block {}.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxBlockParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxBlockToken blockToken = new BlancoApexSyntaxBlockToken();

    /**
     * Construct parser instance.
     * 
     * @param input
     *            Input setting/information of syntax parser.
     * @param blockType
     *            Type of block.
     */
    public BlancoApexSyntaxBlockParser(final BlancoApexSyntaxParserInput input, final BlockType blockType) {
        super(input);
        blockToken.setBlockType(blockType);
    }

    /**
     * Parse token from current position.
     * 
     * @return Result of parse.
     */
    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxBlockToken parse() {
        if (ISDEBUG)
            System.out.println("block parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        // read { char for itself
        blockToken.getTokenList().add(input.readToken());

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("block parser: process(" + input.getIndex() + "): "
                            + input.getTokenAt(input.getIndex() - 1).getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("{")) {
                        input.resetRead();
                        // entering new nested block.
                        blockToken.getTokenList()
                                .add(new BlancoApexSyntaxBlockParser(input, BlockType.MULTI_STATEMENT).parse());
                    } else if (specialCharToken.getValue().equals("}")) {
                        // end of block
                        blockToken.getTokenList().add(specialCharToken);
                        // prevent process
                        return blockToken;
                    } else if (specialCharToken.getValue().equals("(")) {
                        input.resetRead();
                        blockToken.getTokenList()
                                .add(new BlancoApexSyntaxBlockParser(input, BlockType.MULTI_STATEMENT).parse());
                    } else {
                        blockToken.getTokenList().add(inputToken);
                    }
                } else if (inputToken instanceof BlancoApexWordToken) {
                    final BlancoApexWordToken wordToken = (BlancoApexWordToken) inputToken;

                    if (wordToken.getValue().equalsIgnoreCase("if")) {
                        input.resetRead();
                        blockToken.getTokenList().add(new BlancoApexSyntaxIfStatementParser(input).parse());
                        continue;
                    } else if (wordToken.getValue().equalsIgnoreCase("for")) {
                        input.resetRead();
                        blockToken.getTokenList().add(new BlancoApexSyntaxForStatementParser(input).parse());
                        continue;
                    } else if (wordToken.getValue().equalsIgnoreCase("while")) {
                        input.resetRead();
                        blockToken.getTokenList().add(new BlancoApexSyntaxWhileStatementParser(input).parse());
                        continue;
                    }

                    input.resetRead();
                    blockToken.getTokenList().add(new BlancoApexSyntaxStatementParser(input).parse());
                } else {
                    blockToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("block parser: end: " + input.getIndex());
        }

        return blockToken;
    }
}
