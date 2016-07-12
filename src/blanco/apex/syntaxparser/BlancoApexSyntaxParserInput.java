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
 * Input of syntax parser.
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
	 */
	public BlancoApexSyntaxParserInput(final List<BlancoApexToken> inputTokenList) {
		this.inputTokenList = inputTokenList;
	}

	/**
	 * Check token available or not.
	 * 
	 * @return
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
	 * @return
	 */
	public BlancoApexToken readToken() {
		return inputTokenList.get(inputTokenIndex++);
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @deprecated for internal use only.
	 */
	public BlancoApexToken getTokenAt(int index) {
		return inputTokenList.get(index);
	}

	/**
	 * @deprecated for internal use only.
	 * @return
	 */
	public int getTokenCount() {
		return inputTokenList.size();
	}

	///////////////////////////////////////////
	// methods for index

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
