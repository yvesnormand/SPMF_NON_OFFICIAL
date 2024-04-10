package ca.pfv.spmf.tools.dataset_converter.graphconverter;


import ca.pfv.spmf.algorithms.graph_mining.tkg.GSPAN2GraphMLConverter;
import ca.pfv.spmf.algorithms.graph_mining.tkg.Graph;
import ca.pfv.spmf.gui.graphviewer.MainTestGraphViewer_PatternFile;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Test file to convert a graph file from the GSpan format to the ML format
 *
 * @see GSPAN2GraphMLConverter
 */
public class MainTestConvertGSpanFormatToMLFormat {

    public static void main(String[] arg) throws IOException, InterruptedException, TransformerConfigurationException, SAXException {

        String input = fileToPath("patterns.txt");
        String output = "output.dot";

        GSPAN2GraphMLConverter converter = new GSPAN2GraphMLConverter();
        List<Graph> graphsDatabase = converter.readCGSPANGraphs(input);
        converter.writeGraphMLGraphs(output, graphsDatabase);

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestGraphViewer_PatternFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
    }
}