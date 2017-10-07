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
 * Token class of method syntax.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxMethodToken extends AbstractBlancoApexSyntaxToken {
    /**
     * area of definition.
     */
    protected List<BlancoApexToken> defineList = new ArrayList<BlancoApexToken>();

    protected BlancoApexSyntaxModifierToken modifiers = new BlancoApexSyntaxModifierToken();

    /**
     * default be null.
     */
    protected BlancoApexSyntaxTypeToken returnType = null;

    protected List<BlancoApexToken> defineArgsList = new ArrayList<BlancoApexToken>();

    public BlancoApexSyntaxModifierToken getModifiers() {
        return modifiers;
    }

    public void setModifiers(BlancoApexSyntaxModifierToken modifier) {
        this.modifiers = modifier;
    }

    public BlancoApexSyntaxTypeToken getReturn() {
        return returnType;
    }

    public void setReturn(BlancoApexSyntaxTypeToken returnType) {
        this.returnType = returnType;
    }

    public List<BlancoApexToken> getDefineList() {
        return defineList;
    }

    public List<BlancoApexToken> getDefineArgsList() {
        return defineArgsList;
    }

    @Override
    public String getValue() {
        final StringBuffer strbuf = new StringBuffer();
        for (BlancoApexToken token : tokenList) {
            strbuf.append(token.getValue());
        }

        return strbuf.toString();
    }

    @Override
    public String getDisplayString() {
        final StringBuffer strbuf = new StringBuffer();
        for (BlancoApexToken token : tokenList) {
            strbuf.append(token.getDisplayString());
        }

        return "METHOD[" + strbuf.toString() + "]";
    }

    public String getDefineString() {
        final StringBuffer strbuf = new StringBuffer();
        for (BlancoApexToken defineToken : getDefineList()) {
            strbuf.append(defineToken.getValue());
        }
        return strbuf.toString();
    }

    public String getDefineArgsString() {
        final StringBuffer strbuf = new StringBuffer();

        boolean isFirst = true;
        for (BlancoApexToken defineArgsToken : getDefineArgsList()) {
            if (isFirst) {
                isFirst = false;
            } else {
                strbuf.append(',');
            }
            strbuf.append(defineArgsToken.getValue());
        }
        return strbuf.toString();
    }
}
