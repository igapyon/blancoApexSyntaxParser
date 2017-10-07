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

import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.parser.token.BlancoApexWordToken;
import blanco.apex.syntaxparser.BlancoApexSyntaxConstants;
import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;
import blanco.apex.syntaxparser.BlancoApexSyntaxUtil;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxModifierToken;

/**
 * Syntax parser for modifier.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxModifierParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    final BlancoApexSyntaxModifierToken modifierToken = new BlancoApexSyntaxModifierToken();

    public BlancoApexSyntaxModifierParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxModifierToken parse() {
        if (ISDEBUG)
            System.out.println("modifier parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        // modifierToken.getTokenList().add(input.readToken());

        List<BlancoApexToken> modifierList = new ArrayList<BlancoApexToken>();

        try {
            final List<BlancoApexToken> keepTokenList = new ArrayList<BlancoApexToken>();
            for (input.markRead(); input.availableToken();) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println(
                            "modifier parser: process(" + input.getIndex() + "): " + inputToken.getDisplayString());

                if (inputToken instanceof BlancoApexWordToken) {
                    final String valueLookup = inputToken.getValue();
                    if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(valueLookup,
                            BlancoApexSyntaxConstants.MODIFIER_KEYWORDS) == false) {
                        input.resetRead();
                        break;
                    }

                    keepTokenList.add(inputToken);
                    modifierList.add(inputToken);
                    modifierToken.getTokenList().addAll(keepTokenList);
                    keepTokenList.clear();
                    input.markRead();
                } else {
                    keepTokenList.add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("modifier parser: end: " + input.getIndex());
        }

        return modifierToken;
    }
}
