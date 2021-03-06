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

import blanco.apex.syntaxparser.BlancoApexSyntaxParserInput;

/**
 * Abstract class of syntax parser.
 * 
 * @author Toshiki Iga
 */
public abstract class AbstractBlancoApexSyntaxSyntaxParser {
    protected BlancoApexSyntaxParserInput input = null;

    /**
     * Construct parser instance.
     * 
     * @param input
     *            Input setting/information of syntax parser.
     */
    public AbstractBlancoApexSyntaxSyntaxParser(final BlancoApexSyntaxParserInput input) {
        this.input = input;
    }
}
