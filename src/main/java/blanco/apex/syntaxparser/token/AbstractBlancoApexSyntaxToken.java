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
package blanco.apex.syntaxparser.token;

import java.util.ArrayList;
import java.util.List;

import blanco.apex.parser.token.BlancoApexToken;

/**
 * Abstract class of syntax token.
 * 
 * @author Toshiki Iga
 */
public abstract class AbstractBlancoApexSyntaxToken extends BlancoApexToken {
    /**
     * List of child token of this token..
     */
    protected List<BlancoApexToken> tokenList = new ArrayList<BlancoApexToken>();

    /**
     * Return list of child of token.
     * 
     * @return List of token.
     */
    public List<BlancoApexToken> getTokenList() {
        return tokenList;
    }
}
