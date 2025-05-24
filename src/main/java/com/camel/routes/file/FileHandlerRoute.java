package com.camel.routes.file;

import com.camel.processor.MaskSensitiveInfo;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author kansanja on 24/05/25.
 */
public class FileHandlerRoute extends RouteBuilder {

    public static final String APPEND = "&fileExist=Append";
    //    private static String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static String TMP_DIR = "/tmp/";
    //    public static final String FROM_DIR = TMP_DIR + "camel/?noop=true&";
    public static final String FROM_DIR = TMP_DIR + "camel/?";
    public static final String TO_DIR = TMP_DIR + "camel/?";

    @Override
    public void configure() throws Exception {
        from("file://" + FROM_DIR + "fileName=camel-demo-in.txt")
                .log(LoggingLevel.ERROR, ">> ${body}")
                .process(new MaskSensitiveInfo())
                .to("file://" + TO_DIR + "fileName=camel-demo-out.txt" + APPEND);


    }
}
