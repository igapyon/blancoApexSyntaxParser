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

import blanco.apex.parser.token.BlancoApexNewlineToken;
import blanco.apex.parser.token.BlancoApexSpecialCharToken;
import blanco.apex.parser.token.BlancoApexToken;
import blanco.apex.parser.token.BlancoApexWhitespaceToken;
import blanco.apex.syntaxparser.token.AbstractBlancoApexSyntaxToken;

/**
 * Util class for syntax parser.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxUtil {
	/**
	 * Check string included in string list.
	 * 
	 * @param value
	 * @param list
	 * @return
	 */
	public static boolean isIncludedIgnoreCase(final String value, final String[] list) {
		for (String lookup : list) {
			if (lookup.equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @deprecated most case getFirstSpecialCharTokenWithPreviousOne to be used.
	 * @param input
	 * @param specialChars
	 * @return
	 */
	public static BlancoApexSpecialCharToken getFirstSpecialCharToken(final BlancoApexSyntaxParserInput input,
			final String[] specialChars) {
		int index = input.getIndex();
		for (; index < input.getTokenCount(); index++) {
			if (input.getTokenAt(index) instanceof BlancoApexSpecialCharToken) {
				final BlancoApexSpecialCharToken special = (BlancoApexSpecialCharToken) input.getTokenAt(index);
				if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(special.getValue(), specialChars)) {
					return special;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static BlancoApexSpecialCharToken getFirstSpecialCharTokenWithPreviousOne(
			final BlancoApexSyntaxParserInput input, final String[] specialChars) {
		int index = input.getIndex() - 1;
		for (; index < input.getTokenCount(); index++) {
			if (input.getTokenAt(index) instanceof BlancoApexSpecialCharToken) {
				final BlancoApexSpecialCharToken special = (BlancoApexSpecialCharToken) input.getTokenAt(index);
				if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(special.getValue(), specialChars)) {
					return special;
				}
			}
		}
		return null;
	}

	/**
	 * get first token by value.
	 * 
	 * @param input
	 * @param stringArray
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BlancoApexToken getFirstTokenByValue(final BlancoApexSyntaxParserInput input,
			final String[] stringArray) {
		int index = input.getIndex();
		for (; index < input.getTokenCount(); index++) {
			final BlancoApexToken token = (BlancoApexToken) input.getTokenAt(index);
			if (BlancoApexSyntaxUtil.isIncludedIgnoreCase(token.getValue(), stringArray)) {
				return token;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static BlancoApexToken getFirstTokenWithoutWhitespaceNewline(final BlancoApexSyntaxParserInput input) {
		int index = input.getIndex();
		for (; index < input.getTokenCount(); index++) {
			final BlancoApexToken token = (BlancoApexToken) input.getTokenAt(index);
			if (token instanceof BlancoApexWhitespaceToken || token instanceof BlancoApexNewlineToken) {
				return token;
			}
		}
		return null;
	}

	/////////////////////////////////////////
	// dump as token tree.

	/**
	 * Dump given token list to System.err.
	 * 
	 * @param tokenList
	 */
    public static void dumpAsTokenTree(final List<BlancoApexToken> tokenList) {
        for (BlancoApexToken token : tokenList) {
            if (token instanceof AbstractBlancoApexSyntaxToken) {
                System.err.println(token.getClass().getSimpleName());
                dumpAsTokenTreeInternal((AbstractBlancoApexSyntaxToken) token, 1);
            } else {
                System.err.println("    " + token.getDisplayString());
            }
        }
    }

    static void dumpAsTokenTreeInternal(final AbstractBlancoApexSyntaxToken tokenParent, final int indentLevel) {
        for (BlancoApexToken token : tokenParent.getTokenList()) {
            if (token instanceof AbstractBlancoApexSyntaxToken) {
                System.err.println(getIndentString(indentLevel) + token.getClass().getSimpleName());
                dumpAsTokenTreeInternal((AbstractBlancoApexSyntaxToken) token, indentLevel + 1);
            } else {
                System.err.println(getIndentString(indentLevel) + token.getDisplayString());
            }
        }
    }

    static final String getIndentString(final int level) {
        final StringBuffer strbuf = new StringBuffer();
        for (int index = 0; index < level * 2; index++) {
            strbuf.append(' ');
        }
        return strbuf.toString();
    }
}
