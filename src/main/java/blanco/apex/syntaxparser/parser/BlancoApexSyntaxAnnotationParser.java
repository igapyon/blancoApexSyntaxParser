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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxAnnotationToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxParenthesisToken;

/**
 * Syntax parser for annotation.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxAnnotationParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxAnnotationToken annotationToken = new BlancoApexSyntaxAnnotationToken();

    public BlancoApexSyntaxAnnotationParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxAnnotationToken parse() {
        if (ISDEBUG)
            System.out.println("annotation parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        annotationToken.getTokenList().add(input.readToken());

        boolean isAlreadyAnnotationNameRead = false;

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("annotation parser: process(" + input.getIndex() + "): "
                            + input.getTokenAt(input.getIndex()).getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals("(")) {
                        input.resetRead();

                        final BlancoApexSyntaxParenthesisToken parenthesisToken = new BlancoApexSyntaxParenthesisParser(
                                input).parse();
                        annotationToken.getTokenList().add(parenthesisToken);

                        return annotationToken;
                    } else {
                        annotationToken.getTokenList().add(inputToken);
                    }
                } else if (inputToken instanceof BlancoApexWordToken) {
                    if (isAlreadyAnnotationNameRead) {
                        input.resetRead();
                        return annotationToken;
                    }
                    isAlreadyAnnotationNameRead = true;
                    annotationToken.getTokenList().add(inputToken);
                } else {
                    annotationToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("annotation parser: end: " + input.getIndex());
        }

        return annotationToken;
    }
}
