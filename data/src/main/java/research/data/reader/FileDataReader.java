package research.data.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileDataReader {
	/**
	 * 编码值为 "utf-8"
	 */
	private static String Encoding = "utf-8";

	/**
	 * 读本地文件
	 * 
	 * @param filePath 文件路径
	 * @return 文件内容，每行内容对应列表里一个字符串。
	 */
	public static List<String> readFile(String filePath) {
		return readFile(new File(filePath));
	}

	/**
	 * 读本地文件
	 * 
	 * @param inputFile 文件
	 * @return 文件内容，每行内容对应列表里一个字符串。
	 */
	public static List<String> readFile(File inputFile) {
		return readFile(inputFile, 0, -1);
	}

	/**
	 * 读本地文件
	 * 
	 * @param inputFile 文件
	 * @param start
	 * @param count
	 * @return 文件内容，每行内容对应列表里一个字符串。
	 */
	public static List<String> readFile(File inputFile, int start, int count) {
		ArrayList<String> lines = new ArrayList<String>();

		boolean readAll = count == -1;

		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(inputFile), Encoding);
			BufferedReader read = new BufferedReader(isr);
			String line = null;
			int index = 0;
			while ((line = read.readLine()) != null) {
				if (index >= start && (readAll || count > 0)) {
					lines.add(line);
					--count;
				}
				++index;
				if (!readAll && count < 0) {
					break;
				}
			}
			read.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lines;
	}

}
