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

import blanco.apex.parser.token.BlancoApexToken;

/**
 * Token class of block syntax.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxBlockToken extends AbstractBlancoApexSyntaxToken {
    public enum BlockType {
        UNDEFINED, MULTI_STATEMENT, CLASS_DEF, METHOD_DEF, PROPERTY_DEF, ENUM_DEF, RESERVED_VAL_INIT
    }

    protected BlockType blockType = BlockType.UNDEFINED;

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
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

        return "BLOCK(" + blockType + ")[" + strbuf.toString() + "]";
    }
}
