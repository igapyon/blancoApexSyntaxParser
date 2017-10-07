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
package blanco.apex.syntaxparser;

import java.util.List;

import blanco.apex.parser.token.BlancoApexToken;

/**
 * Input setting/information of syntax parser.
 * 
 * <p>
 * Input function that is markable, reset-able like input stream.
 * </p>
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxParserInput {
    private List<BlancoApexToken> inputTokenList = null;

    private int inputTokenIndex = 0;

    private int inputTokenMarkedIndex = 0;

    /**
     * Constructor.
     * 
     * @param inputTokenList
     *            List of token to process.
     */
    public BlancoApexSyntaxParserInput(final List<BlancoApexToken> inputTokenList) {
        this.inputTokenList = inputTokenList;
    }

    /**
     * Check token available or not.
     * 
     * @return true:Available, false:Not-Available
     */
    public boolean availableToken() {
        if (inputTokenIndex < inputTokenList.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Read token and go to next.
     * 
     * @return read token and increment current position.
     */
    public BlancoApexToken readToken() {
        return inputTokenList.get(inputTokenIndex++);
    }

    /**
     * Get token at the point.
     * 
     * @param index
     *            index to know.
     * @return Token.
     * @deprecated for internal use only.
     */
    public BlancoApexToken getTokenAt(int index) {
        return inputTokenList.get(index);
    }

    /**
     * Get count of token.
     * 
     * @deprecated for internal use only.
     * @return current token count.
     */
    public int getTokenCount() {
        return inputTokenList.size();
    }

    ///////////////////////////////////////////
    // methods for index

    /**
     * Get current index.
     * 
     * @return current index.
     */
    public int getIndex() {
        return inputTokenIndex;
    }

    /**
     * Mark token point.
     */
    public void markRead() {
        inputTokenMarkedIndex = inputTokenIndex;
    }

    /**
     * Reset token point to previous marked.
     */
    public void resetRead() {
        inputTokenIndex = inputTokenMarkedIndex;
    }
}
