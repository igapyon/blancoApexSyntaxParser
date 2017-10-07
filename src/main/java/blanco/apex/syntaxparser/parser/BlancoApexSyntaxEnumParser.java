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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxEnumToken;

/**
 * Syntax parser for enum.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxEnumParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    /**
     * Construct parser instance.
     * 
     * @param input
     *            Input setting/information of syntax parser.
     */
    public BlancoApexSyntaxEnumParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    /**
     * Parse token from current position.
     * 
     * @return Result of parse.
     */
    public BlancoApexSyntaxEnumToken parse() {
        if (ISDEBUG)
            System.out.println("enum parser: begin: " + input.getIndex());

        final BlancoApexSyntaxEnumToken enumToken = new BlancoApexSyntaxEnumToken();

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken sourceToken = input.readToken();
                if (sourceToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) sourceToken;

                    if (specialCharToken.getValue().equals("{")) {
                        input.resetRead();
                        // start enum def.
                        enumToken.getTokenList()
                                .add(new BlancoApexSyntaxBlockParser(input, BlockType.ENUM_DEF).parse());

                        // exit process.
                        return enumToken;
                    } else {
                        enumToken.getTokenList().add(sourceToken);
                    }
                } else {
                    enumToken.getTokenList().add(sourceToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("enum parser: end: " + input.getIndex());

            // update mark
            input.markRead();
        }

        return enumToken;
    }
}
