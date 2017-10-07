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
import blanco.apex.syntaxparser.BlancoApexSyntaxConstants;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxPropertyToken;

/**
 * Syntax parser for property.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxPropertyParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxPropertyToken propertyToken = new BlancoApexSyntaxPropertyToken();

    /**
     * Construct parser instance.
     * 
     * @param input
     *            Input setting/information of syntax parser.
     */
    public BlancoApexSyntaxPropertyParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    /**
     * Parse token from current position.
     * 
     * @return Result of parse.
     */
    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxPropertyToken parse() {
        if (ISDEBUG)
            System.out.println("property parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        // propertyToken.getTokenList().add(input.readToken());

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("property parser: process(" + input.getIndex() + "): "
                            + input.getTokenAt(input.getIndex()).getDisplayString());

                if (inputToken instanceof BlancoApexWordToken) {
                    if (propertyToken.getType() == null) {
                        // before method name.
                        if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(inputToken.getValue(),
                                BlancoApexSyntaxConstants.MODIFIER_KEYWORDS)) {
                            input.resetRead();
                            propertyToken.setModifiers(new BlancoApexSyntaxModifierParser(input).parse());
                            propertyToken.getTokenList().add(propertyToken.getModifiers());
                        } else {
                            input.resetRead();
                            propertyToken.setType(new BlancoApexSyntaxTypeParser(input).parse());
                            propertyToken.getTokenList().add(propertyToken.getType());
                        }
                    } else {
                        propertyToken.getTokenList().add(inputToken);
                    }
                } else if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("{")) {
                        // entering new nested block.
                        input.resetRead();
                        propertyToken.getTokenList()
                                .add(new BlancoApexSyntaxBlockParser(input, BlockType.PROPERTY_DEF).parse());
                        return propertyToken;
                    } else {
                        propertyToken.getTokenList().add(inputToken);
                    }
                } else {
                    propertyToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("property parser: end: " + input.getIndex());
        }

        return propertyToken;
    }
}
