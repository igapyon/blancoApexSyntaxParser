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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxClassToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxMethodToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxParenthesisToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxTypeToken;

public class BlancoApexSyntaxMethodParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected BlancoApexSyntaxMethodToken methodToken = new BlancoApexSyntaxMethodToken();

    protected BlancoApexSyntaxClassToken classToken = null;

    public BlancoApexSyntaxMethodParser(final BlancoApexSyntaxParserInput input,
            final BlancoApexSyntaxClassToken classToken) {
        super(input);
        this.classToken = classToken;
    }

    public BlancoApexSyntaxMethodToken parse() {
        if (ISDEBUG)
            System.out.println("method parser: begin: " + input.getIndex());

        boolean isDefineArea = true;

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken inputToken = input.readToken();

                if (ISDEBUG)
                    System.out.println(
                            "method parser: process(" + input.getIndex() + "): " + inputToken.getDisplayString());

                if (inputToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;

                    // processing () needed.

                    if (specialCharToken.getValue().equals("(")) {
                        input.resetRead();

                        isDefineArea = false;

                        // start method def.
                        final BlancoApexSyntaxParenthesisToken parenthesisToken = new BlancoApexSyntaxMethodParenthesisParser(
                                input, methodToken).parse();
                        methodToken.getTokenList().add(parenthesisToken);
                        methodToken.getDefineArgsList().add(parenthesisToken);
                    } else if (specialCharToken.getValue().equals("{")) {
                        isDefineArea = false;

                        input.resetRead();
                        // start class def.
                        methodToken.getTokenList()
                                .add(new BlancoApexSyntaxBlockParser(input, BlockType.METHOD_DEF).parse());

                        // exit process.
                        return methodToken;
                    } else if (specialCharToken.getValue().equals(";")) {
                        isDefineArea = false;

                        // seems non body method.
                        methodToken.getTokenList().add(inputToken);
                        // exit process.
                        return methodToken;
                    } else {
                        methodToken.getTokenList().add(inputToken);

                        if (isDefineArea) {
                            methodToken.getDefineList().add(inputToken);
                        }
                    }
                } else if (inputToken instanceof BlancoApexWordToken) {
                    if (isDefineArea) {
                        if (methodToken.getReturn() == null) {
                            // before method name.
                            if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(inputToken.getValue(),
                                    BlancoApexSyntaxConstants.MODIFIER_KEYWORDS)) {
                                input.resetRead();
                                methodToken.setModifiers(new BlancoApexSyntaxModifierParser(input).parse());
                                methodToken.getTokenList().add(methodToken.getModifiers());
                            } else {
                                // System.out.println("TRACE: コンストラクタかどうかを判定。:["
                                // + inputToken.getValue() + "], クラス名:["
                                // + classToken.getName() + "]");
                                boolean isConstructor = false;
                                if (inputToken.getValue().equalsIgnoreCase(classToken.getName())) {
                                    // Constructor
                                    // System.out.println("TRACE: コンストラクタ!");
                                    isConstructor = true;
                                }

                                input.resetRead();
                                if (isConstructor == false) {
                                    methodToken.setReturn(new BlancoApexSyntaxTypeParser(input).parse());
                                } else {
                                    final BlancoApexSyntaxTypeToken typeToken = new BlancoApexSyntaxTypeToken();
                                    // 実際はコンストラクター。表現は未決定。
                                    typeToken.setValue("");
                                    methodToken.setReturn(typeToken);
                                }
                                methodToken.getTokenList().add(methodToken.getReturn());
                            }
                            methodToken.getDefineList().add(inputToken);
                        } else {
                            methodToken.getDefineList().add(inputToken);
                            methodToken.getTokenList().add(inputToken);
                        }
                    } else {
                        methodToken.getTokenList().add(inputToken);
                    }
                } else {
                    if (isDefineArea) {
                        methodToken.getDefineList().add(inputToken);
                    }
                    methodToken.getTokenList().add(inputToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println(
                        "method parser: end: " + methodToken.getDefineString() + methodToken.getDefineArgsString());

            // update mark
            input.markRead();
        }

        return methodToken;
    }
}
