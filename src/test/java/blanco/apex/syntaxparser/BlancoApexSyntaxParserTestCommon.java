package blanco.apex.syntaxparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class BlancoApexSyntaxParserTestCommon {
    public static final String file2String(final File file) throws IOException {
        final StringWriter writer = new StringWriter();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        final char[] cbuf = new char[4096];
        for (;;) {
            final int length = reader.read(cbuf);
            if (length <= 0) {
                break;
            }
            writer.write(cbuf, 0, length);
        }
        reader.close();
        writer.close();

        return writer.toString();
    }

}
