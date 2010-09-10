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

import java.util.Vector;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class LogScreen extends MainScreen {

	protected VerticalFieldManager vfm;
	protected Vector logEntries;
	protected int displayLevel;

	public LogScreen(String pName) {
		logEntries = new Vector();
		displayLevel = LogLevel.DEBUG;

		setTitle("Log Screen: " + pName);
		vfm = new VerticalFieldManager();
		add(vfm);

		addMenuItem(new MenuItem(">= DEBUG", 200000, 10) {
			public void run() {
				setDisplayLevel(LogLevel.DEBUG);
				refreshDisplay();
			}
		});

		addMenuItem(new MenuItem(">= INFO", 200000, 10) {
			public void run() {
				setDisplayLevel(LogLevel.INFO);
				refreshDisplay();
			}
		});

		addMenuItem(new MenuItem(">= WARN", 200000, 10) {
			public void run() {
				setDisplayLevel(LogLevel.WARN);
				refreshDisplay();
			}
		});

		addMenuItem(new MenuItem(">= ERROR", 200000, 10) {
			public void run() {
				setDisplayLevel(LogLevel.ERROR);
				refreshDisplay();
			}
		});

		addMenuItem(new MenuItem(">= FATAL", 200000, 10) {
			public void run() {
				setDisplayLevel(LogLevel.FATAL);
				refreshDisplay();
			}
		});

	}

	public void addLog(LogEntryField field) {
		logEntries.addElement(field);
		if (LogLevel.isGreaterOrEqual(field.getLogLevel(), displayLevel)) {
			vfm.add(field);
		}
	}

	public void clearAll() {
		if (vfm != null) {
			vfm.deleteAll();
		}
		if (logEntries != null) {
			logEntries.removeAllElements();
		}
	}

	public void refreshDisplay() {
		vfm.deleteAll();
		for (int i = 0; i < logEntries.size(); i++) {
			LogEntryField curr = (LogEntryField) logEntries.elementAt(i);
			if (LogLevel.isGreaterOrEqual(curr.getLogLevel(), displayLevel)) {
				vfm.add(curr);
			}
		}
	}

	//	public boolean onClose() {
	//		clearAll();
	//		vfm = null;
	//		logEntries = null;
	//		return true;
	//	}

	public void setDisplayLevel(int pLevel) {
		displayLevel = pLevel;
	}

	protected boolean onSavePrompt() {
		return true;
	}

}
