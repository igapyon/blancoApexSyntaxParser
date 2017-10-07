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

public class BlancoApexSyntaxClassToken extends AbstractBlancoApexSyntaxToken {
    /**
     * Class name
     */
    protected String name;

    /**
     * area of definition.
     */
    protected List<BlancoApexToken> defineList = new ArrayList<BlancoApexToken>();

    protected BlancoApexSyntaxModifierToken modifiers = new BlancoApexSyntaxModifierToken();

    /**
     * class Name
     * 
     * @return Name of class.
     */
    public String getName() {
        return name;
    }

    /**
     * class name
     * 
     * @param name
     *            Name of class.
     */
    public void setName(String name) {
        this.name = name;
    }

    public BlancoApexSyntaxModifierToken getModifiers() {
        return modifiers;
    }

    public void setModifiers(BlancoApexSyntaxModifierToken modifiers) {
        this.modifiers = modifiers;
    }

    public List<BlancoApexToken> getDefineList() {
        return defineList;
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
        boolean isFirst = true;
        for (BlancoApexToken token : tokenList) {
            if (isFirst) {
                isFirst = false;
            } else {
                strbuf.append(',');
            }
            strbuf.append(token.getDisplayString());
        }

        return "CLASS[" + strbuf.toString() + "]";
    }

    public String getDefineString() {
        final StringBuffer strbuf = new StringBuffer();
        for (BlancoApexToken defineToken : getDefineList()) {
            strbuf.append(defineToken.getValue());
        }
        return strbuf.toString();
    }
}
