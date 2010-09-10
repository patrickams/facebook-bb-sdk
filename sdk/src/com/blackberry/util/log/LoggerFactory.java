/**
 * Copyright (c) E.Y. Baskoro, Research In Motion Limited.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * This License shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * The name(s) of the above copyright holders shall not be used 
 * in advertising or otherwise to promote the sale, use or other 
 * dealings in this Software without prior written authorization.
 * 
 */
package com.blackberry.util.log;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.blackberry.util.properties.Properties;
import com.blackberry.util.string.StringUtils;

public class LoggerFactory {

	private static final String DEFAULT_TEXT_LOG_FILENAME = "file:///SDCard/log.txt";
	private static final String DEFAULT_RICHTEXT_LOG_FILENAME = "file:///SDCard/log.html";
	private static final String DEFAULT_PROPERTIES_FILENAME = "log.properties";

	private static Hashtable loggers = new Hashtable();
	private static final Logger GOD_LOGGER = createLogger("", Logger.CONSOLE, null);

	static {
		load(DEFAULT_PROPERTIES_FILENAME);
	}

	public static void load(String propFile) {
		try {
			Properties prop = Properties.loadProperties("/" + propFile);
			Enumeration enum = prop.getEnumeratedNames();

			while (enum.hasMoreElements()) {
				String key = null;
				String value = null;
				String[] values = null;
				String loggerName = null;
				String loggerType = null;
				String loggerDest = null;

				key = ((String) enum.nextElement());

				if ((key != null) && !key.equals("")) {
					loggerName = key.trim();
					value = prop.getProperty(key);
					if ((value != null) && !value.equals("")) {
						values = StringUtils.split(value, ',', 0);
						if ((values != null) && (values.length > 0)) {
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									loggerType = values[i].trim();
								} else if (i == 1) {
									loggerDest = values[i].trim();
								}
							}

							// create logger for this line
							if (loggerType.equals(Logger.CONSOLE)) {
								loggers.put(loggerName, createLogger(loggerName, Logger.CONSOLE, loggerDest));

							} else if (loggerType.equals(Logger.TEXT_FILE)) {
								if ((loggerDest == null) || loggerDest.equals("")) {
									loggerDest = DEFAULT_TEXT_LOG_FILENAME;
								}
								loggers.put(loggerName, createLogger(loggerName, Logger.TEXT_FILE, loggerDest));

							} else if (loggerType.equals(Logger.RICH_TEXT_FILE)) {
								if ((loggerDest == null) || loggerDest.equals("")) {
									loggerDest = DEFAULT_RICHTEXT_LOG_FILENAME;
								}
								loggers.put(loggerName, createLogger(loggerName, Logger.RICH_TEXT_FILE, loggerDest));

							} else if (loggerType.equals(Logger.SCREEN)) {
								loggers.put(loggerName, createLogger(loggerName, Logger.SCREEN, loggerDest));

							} else if (loggerType.equals(Logger.EVENT_LOG)) {
								if ((loggerDest != null) && !loggerDest.equals("")) {
									loggers.put(loggerName, createLogger(loggerName, Logger.EVENT_LOG, loggerDest));
								}

							}
						}
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	public static Logger getLogger() {
		return getLogger("DEFAULT");
	}

	public static Logger getLogger(String name) {
		if ((name != null) && !name.equals("")) {
			if (loggers.containsKey(name)) {
				return (Logger) loggers.get(name);
			} else {
				if (!name.equals("DEFAULT")) {
					return getLogger("DEFAULT");
				} else {
					return GOD_LOGGER;
				}
			}
		} else {
			return null;
		}
	}

	public static void clear() {
		Enumeration enum = loggers.elements();
		if (enum != null) {
			while (enum.hasMoreElements()) {
				((Logger) enum.nextElement()).close();
			}
		}
		loggers = new Hashtable();
	}

	public static void reset() {
		clear();
		load(DEFAULT_PROPERTIES_FILENAME);
	}

	private static Logger createLogger(String name, String type, String destination) {

		Logger out = null;

		if (type.trim().equalsIgnoreCase(Logger.CONSOLE)) {
			out = new ConsoleLogger(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Logger.TEXT_FILE)) {
			out = new TextFileLogger(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Logger.RICH_TEXT_FILE)) {
			out = new RichTextFileLogger(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Logger.SCREEN)) {
			out = new ScreenLogger(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Logger.EVENT_LOG)) {
			out = new EventLogger(name, type, destination);
		}

		return out;

	}
}
