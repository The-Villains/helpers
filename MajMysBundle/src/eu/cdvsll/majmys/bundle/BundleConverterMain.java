package eu.cdvsll.majmys.bundle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BundleConverterMain {

	private static void process(StringBuilder sb, Object key, List<String> langs, 
			Map<String, Properties> props) {
		sb.append(key).append(",");
		sb.append(langs.stream().map(lang -> 
			"\"" + String.valueOf(props.get(lang).get(key)).replace("\r", "").replace("\n", "\\n") + "\"" 
		).collect(Collectors.joining(",")));
		sb.append("\n");
	}

	public static void main(String[] args) throws IOException {
		var langs = Arrays.asList("en", "de", "fr");
		SortedSet<Object> keys = new TreeSet<>();
		Map<String, Properties> props = new HashMap<>();
		for (String lang : langs) {
			Properties prop = new Properties();
			prop.load(new FileReader(new File(System.getProperty("user.dir") + "/bundle_" + lang + ".properties")));
			keys.addAll(new HashSet<Object>(prop.keySet()));
			props.put(lang, prop);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(",");
		sb.append(langs.stream().collect(Collectors.joining(",")));
		sb.append("\n");
		keys.stream().forEach(key -> process(sb, key, langs, props));
		try (FileWriter fw = new FileWriter("strings_final.csv", Charset.forName("UTF-8"))) {
			fw.write(sb.toString());
		}
	}
}
