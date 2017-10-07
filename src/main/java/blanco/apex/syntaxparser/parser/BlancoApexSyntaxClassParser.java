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
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxBlockToken.BlockType;
import blanco.apex.syntaxparser.token.BlancoApexSyntaxClassToken;

/**
 * Syntax parser for class.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxClassParser extends AbstractBlancoApexSyntaxSyntaxParser {
    public static final boolean ISDEBUG = false;

    protected final BlancoApexSyntaxClassToken classToken = new BlancoApexSyntaxClassToken();

    public BlancoApexSyntaxClassParser(final BlancoApexSyntaxParserInput input) {
        super(input);
    }

    public BlancoApexSyntaxClassToken parse() {
        if (ISDEBUG)
            System.out.println("class parser: begin: " + input.getIndex());

        boolean isDefineArea = true;

        try {
            for (input.markRead(); input.availableToken(); input.markRead()) {
                final BlancoApexToken sourceToken = input.readToken();

                if (ISDEBUG)
                    System.out.println(
                            "class parser: process(" + input.getIndex() + "): " + sourceToken.getDisplayString());

                if (sourceToken instanceof BlancoApexSpecialCharToken) {
                    final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) sourceToken;
                    if (specialCharToken.getValue().equals("{")) {
                        input.resetRead();
                        isDefineArea = false;

                        classToken.getTokenList().add(parseClassBlock());

                        input.resetRead();

                        // prevent process.
                        return classToken;
                    } else {
                        classToken.getTokenList().add(sourceToken);

                        if (isDefineArea) {
                            // save define area.
                            classToken.getDefineList().add(sourceToken);
                        }
                    }
                } else if (sourceToken instanceof BlancoApexWordToken) {
                    if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(sourceToken.getValue(),
                            BlancoApexSyntaxConstants.MODIFIER_KEYWORDS)) {
                        input.resetRead();
                        classToken.setModifiers(new BlancoApexSyntaxModifierParser(input).parse());
                        classToken.getTokenList().add(classToken.getModifiers());
                    } else {
                        // will be class name or something.
                        classToken.getTokenList().add(sourceToken);
                        if (classToken.getName() == null && false == sourceToken.getValue().equalsIgnoreCase("class")) {
                            // save first name.
                            classToken.setName(sourceToken.getValue());
                        }
                    }

                    if (isDefineArea) {
                        // save define area.
                        classToken.getDefineList().add(sourceToken);
                    }
                } else {
                    classToken.getTokenList().add(sourceToken);
                }
            }
        } finally {
            if (ISDEBUG)
                System.out.println("class parser: end: " + input.getIndex());

            // update mark
            input.markRead();
        }

        return classToken;
    }

    protected BlancoApexSyntaxBlockToken parseClassBlock() {
        final BlancoApexSyntaxBlockToken blockToken = new BlancoApexSyntaxBlockToken();
        blockToken.setBlockType(BlockType.CLASS_DEF);

        if (ISDEBUG)
            System.out.println("class parser: block: begin: " + input.getIndex());

        // consume '{'
        blockToken.getTokenList().add(input.readToken());

        for (; input.availableToken(); input.markRead()) {

            final BlancoApexToken inputToken = input.readToken();

            if (ISDEBUG)
                System.out.println("class parser: process(" + input.getIndex() + "): " + inputToken.getDisplayString());

            if (inputToken instanceof BlancoApexWordToken) {
                final BlancoApexWordToken wordToken = (BlancoApexWordToken) inputToken;
                if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(wordToken.getValue(), new String[] { "class" })) {
                    // inner class found.
                    input.resetRead();
                    blockToken.getTokenList().add(new BlancoApexSyntaxClassParser(input).parse());
                    continue;
                }

                final BlancoApexSpecialCharToken nextSpecial = BlancoApexSyntaxUtil
                        .getFirstSpecialCharTokenWithPreviousOne(input, new String[] { ";", "=", "(", "{" });
                if (nextSpecial == null) {
                    blockToken.getTokenList().add(inputToken);
                    System.out.println("class parser: Unexpected (L88): " + input.getIndex());
                } else {
                    if (nextSpecial.getValue().equals(";")) {
                        // field
                        input.resetRead();
                        blockToken.getTokenList().add(new BlancoApexSyntaxFieldParser(input).parse());
                    } else if (nextSpecial.getValue().equals("=")) {
                        if (ISDEBUG)
                            System.out.println("class parser: block: =: " + input.getIndex());

                        // field with load value
                        input.resetRead();
                        blockToken.getTokenList().add(new BlancoApexSyntaxFieldParser(input).parse());
                    } else if (nextSpecial.getValue().equals("(")) {
                        if (ISDEBUG)
                            System.out.println("class parser: block: (: " + input.getIndex());

                        // method
                        input.resetRead();
                        blockToken.getTokenList().add(new BlancoApexSyntaxMethodParser(input, classToken).parse());
                    } else if (nextSpecial.getValue().equals("{")) {
                        if (ISDEBUG)
                            System.out.println("class parser: block: {: " + input.getIndex());

                        // will be class or property.
                        final BlancoApexToken checkNextToken = BlancoApexSyntaxUtil.getFirstTokenByValue(input,
                                new String[] { "class", "interface", "{" });
                        if (checkNextToken == null) {
                            System.out.println("class parser: unexpected (L101)state: " + input.getIndex());
                            blockToken.getTokenList().add(inputToken);
                        } else if (checkNextToken.getValue().equals("{")) {
                            // property;
                            input.resetRead();
                            blockToken.getTokenList().add(new BlancoApexSyntaxPropertyParser(input).parse());
                        } else {
                            input.resetRead();
                            blockToken.getTokenList().add(new BlancoApexSyntaxClassParser(input).parse());
                        }
                    }
                }
            } else if (inputToken instanceof BlancoApexSpecialCharToken) {
                final BlancoApexSpecialCharToken specialCharToken = (BlancoApexSpecialCharToken) inputToken;
                if (specialCharToken.getValue().equals("}")) {
                    // end of class def.
                    blockToken.getTokenList().add(inputToken);
                    input.markRead();
                    return blockToken;
                } else if (specialCharToken.getValue().equals("@")) {
                    input.resetRead();
                    blockToken.getTokenList().add(new BlancoApexSyntaxAnnotationParser(input).parse());
                } else if (specialCharToken.getValue().equals("{")) {
                    // I'm not sure, but Apex has non-named method? Is it
                    // true?
                    input.resetRead();
                    blockToken.getTokenList().add(new BlancoApexSyntaxBlockParser(input, BlockType.UNDEFINED).parse());
                } else {
                    System.out.println("class parser: unexpected state(L123): " + specialCharToken.getDisplayString());
                    blockToken.getTokenList().add(inputToken);
                }
            } else {
                blockToken.getTokenList().add(inputToken);
            }
        }

        return blockToken;
    }
}
