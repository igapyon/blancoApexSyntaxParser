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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxMethodToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxParenthesisToken;

/**
 * method () class
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxMethodParenthesisParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    final BlancoApexSyntaxParenthesisToken methodParenthesisToken = new BlancoApexSyntaxParenthesisToken();

    protected BlancoApexSyntaxMethodToken methodToken = null;

    public BlancoApexSyntaxMethodParenthesisParser(final BlancoApexSyntaxParserInput input,
            final BlancoApexSyntaxMethodToken methodToken) {
        super(input);
        this.methodToken = methodToken;
    }

    @SuppressWarnings("deprecation")
    public BlancoApexSyntaxParenthesisToken parse() {
        if (ISDEBUG)
            System.out.println("parenthesis parser: begin: " + input.getIndex() + ": "
                    + input.getTokenAt(input.getIndex()).getDisplayString());

        // カッコ開始を消費。
        methodParenthesisToken.getTokenList().add(input.readToken());

        try {
            boolean isTypeParsed = false;
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println("method-parenthesis parser: process(" + input.getIndex() + "): "
                            + inputToken.getDisplayString());

                if (inputToken instanceof BlancoApexWordToken) {
                    if (isTypeParsed == false) {
                        // 型をパース。
                        input.resetRead();
                        methodParenthesisToken.getTokenList().add(new BlancoApexSyntaxTypeParser(input).parse());
                        isTypeParsed = true;
                    } else {
                        methodParenthesisToken.getTokenList().add(inputToken);
                    }
                } else if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                    if (specialCharToken.getValue().equals(")")) {
                        // end of parenthesis.
                        methodParenthesisToken.getTokenList().add(inputToken);
                        if (ISDEBUG)
                            System.out.println("method-parenthesis parser: ) given: " + input.getIndex());
                        return methodParenthesisToken;
                    } else if (specialCharToken.getValue().equals("(")) {
                        // entering new nested one.
                        input.resetRead();
                        // ここではメソッドではなく普通のトークン。
                        methodParenthesisToken.getTokenList().add(new BlancoApexSyntaxParenthesisParser(input).parse());
                    } else if (specialCharToken.getValue().equals(",")) {
                        // カンマの場合は、引数が次に。
                        isTypeParsed = false;
                        methodParenthesisToken.getTokenList().add(inputToken);
                    } else {
                        methodParenthesisToken.getTokenList().add(inputToken);
                    }
                } else {
                    methodParenthesisToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("method-parenthesis parser: end: " + input.getIndex());
        }

        return methodParenthesisToken;
    }
}
