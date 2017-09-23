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

/**
 * Force.com Apex constants for parser.
 * 
 * @author Toshiki Iga
 */
public class BlancoApexSyntaxConstants {
	public static final String VERSION = "v1.1";

	public static String getVersion() {
		return VERSION;
	}

	/**
	 * reserved keywords on Force.com Apex language.
	 * 
	 * via: <code>
	 * https://developer.salesforce.com/docs/atlas.en-us.apexcode.meta/apexcode/apex_reserved_words.htm
	 * </code>
	 */
	public static final String[] RESERVED_KEYWORDS = new String[] { "abstract", "activate"/* resv */
			, "and", "any"/* resv */
			, "array", "as", "asc", "autonomous"/* resv */
			, "begin"/* resv */
			, "bigdecimal"/* resv */
			, "blob", "break", "bulk", "by", "byte"/* resv */
			, "case"/* resv */
			, "cast"/* resv */
			, "catch", "char"/* resv */
			, "class", "collect"/* resv */
			, "commit", "const"/* resv */
			, "continue", "convertcurrency", "decimal", "default"/* resv */
			, "delete", "desc", "do", "else", "end"/* resv */
			, "enum", "exception", "exit"/* resv */
			, "export"/* resv */
			, "extends", "false", "final", "finally", "float"/* resv */
			, "for", "from", "future", "global", "goto"/* resv */
			, "group"/* resv */
			, "having"/* resv */
			, "hint"/* resv */
			, "if", "implements", "import"/* resv */
			, "inner"/* resv */
			, "insert", "instanceof", "interface", "into"/* resv */
			, "int", "join"/* resv */
			, "last_90_days", "last_month", "last_n_days", "last_week", "like", "limit", "list", "long",
			"loop"/* resv */
			, "map", "merge", "new", "next_90_days", "next_month", "next_n_days", "next_week", "not", "null", "nulls",
			"number"/* resv */
			, "object"/* resv */
			, "of"/* resv */
			, "on", "or", "outer"/* resv */
			, "override", "package", "parallel"/* resv */
			, "pragma"/* resv */
			, "private", "protected", "public", "retrieve"/* resv */
			, "return", "returning"/* resv */
			, "rollback", "savepoint", "search"/* resv */
			, "select", "set", "short"/* resv */
			, "sort", "stat"/* resv */
			, "static", "super", "switch"/* resv */
			, "synchronized"/* resv */
			, "system", "testmethod", "then"/* resv */
			, "this", "this_month"/* resv */
			, "this_week", "throw", "today", "tolabel", "tomorrow", "transaction"/* resv */
			, "trigger", "true", "try", "type"/* resv */
			, "undelete", "update", "upsert", "using", "virtual", "webservice", "when"/* resv */
			, "where", "while", "yesterday"
			// non reserved keywords, but will be needed to care.
			, "after", "before", "count", "excludes", "first", "includes", "last", "order", "sharing", "with" };

	public static final String[] MODIFIER_KEYWORDS = new String[] { "global", "public", "protected", "private",
			"abstract", "without", "with", "static", "virtual", "sharing", "webservice", "testMethod" };

	/**
	 * keywords that seems to be reserved keywords.
	 */
	public static final String[] MAYBE_KEYWORDS = new String[] { "get", };

	/**
	 * Force.com Apex namespaces
	 * 
	 * Point: currently those are not used in parser.
	 */
	public static final String[] APEX_NAMESPACES = new String[] { "ApexPages", "AppLauncher", "Approval", "Auth",
			"Cache", /* FIXME skipping... */"Database",
			/* FIXME skipping... */"System" /* FIXME skipping... */ };

	/**
	 * Apex System classes that is shown in default namespace.
	 * 
	 * TODO ... Enum Methods / / Custom Settings Methods
	 * 
	 * via:
	 * <code> https://developer.salesforce.com/docs/atlas.en-us.apexcode.meta/apexcode/apex_namespace_System.htm</code>
	 */
	public static final String[] APEX_SYSTEM_CLASSES = new String[] { "Address", "Answers", "ApexPages", "Approval",
			"Blob", "Boolean", "BusinessHours", "Cases", "Comparable", "Continuation", "Cookie", "Crypto", "Database",
			"Date", "Datetime", "Decimal", "Double", "EncodingUtil", "FlexQueue", "Http", "HttpCalloutMock",
			"HttpRequest", "HttpResponse", "Id", "Ideas", "InstallHandler", "Integer", "JSON", "JSONGenerator",
			"JSONParser", "JSONToken Enum", "Limits", "List", "Location", "Long", "Map", "Matcher", "Math", "Messaging",
			"MultiStaticResourceCalloutMock", "Network", "PageReference", "Pattern", "Queueable", "QueueableContext",
			"QuickAction", "RemoteObjectController", "ResetPasswordResult", "RestContext", "RestRequest",
			"RestResponse", "SandboxPostCopy", "Schedulable", "SchedulableContext", "Schema", "Search", "SelectOption",
			"Set", "Site", "sObject", "StaticResourceCalloutMock", "String", "System", "Test", "Time", "TimeZone",
			"Trigger", "Type", "UninstallHandler", "URL", "UserInfo", "Version", "WebServiceCallout", "WebServiceMock",
			"XmlStreamReader", "XmlStreamWriter",

			/** System Exceptions (Exception and Built-In Exceptions) */
			"Exception", "AsyncException", "CalloutException", "DmlException", "EmailException",
			"ExternalObjectException", "InvalidParameterValueException", "LimitException", "JSONException",
			"ListException", "MathException", "NoAccessException", "NoDataFoundException", "NoSuchElementException",
			"NullPointerException", "QueryException", "RequiredFeatureMissing", "SearchException", "SecurityException",
			"SerializationException", "SObjectException", "StringException", "TypeException", "VisualforceException",
			"XmlException" };
}
