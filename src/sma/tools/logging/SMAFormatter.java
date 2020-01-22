/**
 * 
 */
package sma.tools.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import sma.parameters.Config;
/**
*
*/
public class SMAFormatter extends Formatter {

	static final String lineSep = System.getProperty("line.separator");
	private DateFormat dateFormat;

		@Override
	public String format(LogRecord record) {
		StringBuffer buf = new StringBuffer(180);

		Object[] param = record.getParameters();
		
		if(Config.DEBUG_LOG){
			if (dateFormat == null)
				dateFormat = DateFormat.getDateTimeInstance();
			
			buf.append(record.getLevel());
			buf.append(":");
			buf.append(dateFormat.format(new Date(record.getMillis())));
			buf.append(' ');
			buf.append(record.getSourceClassName());
			buf.append(' ');
			buf.append(record.getSourceMethodName());
			buf.append(lineSep);
		}
		
		if(param != null && param[0] != null)
			buf.append("\t"+"("+record.getLevel().toString().charAt(0)+")"+param[0].toString()+" : "+formatMessage(record));
		else
			buf.append("\t"+"("+record.getLevel().toString().charAt(0)+")"+formatMessage(record));
			
		buf.append(lineSep);
		
		Throwable throwable = record.getThrown();
		if (throwable != null){
			StringWriter sink = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sink, true));
			buf.append(sink.toString());
		}
		
		return buf.toString();
	}

}
