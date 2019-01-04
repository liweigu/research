package research.model.recommend;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 推荐模型
 * 
 * @author liweigu714@163.com
 *
 */
public abstract class RecommendModel implements RecommendModelI {
	static Logger Log = LoggerFactory.getLogger(RecommendModel.class);
	static Object Model;
	static Map<String, Object> InitProps = new HashMap<String, Object>();

	/**
	 * 初始化模型
	 * 
	 * @param initProps 初始化参数
	 */
	public void initModel(Map<String, Object> initProps) {
		throw new IllegalStateException("需调用RecommendModel子类的方法。");
	}

	/**
	 * 返回模型
	 * 
	 * @return
	 */
	public Object getModel() {
		if (Model == null) {
			throw new IllegalStateException("模型未初始化");
		}
		return Model;
	}

	/**
	 * 返回初始化参数
	 * 
	 * @return 初始化参数
	 */
	public Map<String, Object> getInitProps() {
		return InitProps;
	}

	/**
	 * 保存模型
	 * 
	 * @param path 模型路径
	 */
	public boolean save(String path) {
		boolean flag = false;

		if (Model != null) {
			if (Model instanceof ComputationGraph) {
				ComputationGraph computationGraph = (ComputationGraph) Model;
				File modelFile = new File(path);
				try {
					// writeModel会自动删除旧的模型文件
					ModelSerializer.writeModel(computationGraph, modelFile, true);
					flag = true;
				} catch (IOException e) {
					Log.info("保存模型失败", e);
				}
			}
		}

		return flag;
	}

	/**
	 * 加载模型
	 * 
	 * @param path 模型路径
	 */
	protected boolean restore(String path) {
		boolean flag = false;

		if (Model == null) {
			if (path != null && path.length() > 0) {
				File modelFile = null;
				if (path.startsWith("http")) {
					String tmpdir = System.getProperty("java.io.tmpdir");
					String localFilename = new File(path).getName();
					File cachedFile = new File(tmpdir, localFilename);
					try {
						if (!cachedFile.exists()) {
							// 下载预训练模型
							Log.info("下载模型(" + path + ")到： " + cachedFile.toString());
							FileUtils.copyURLToFile(new URL(path), cachedFile);
						} else {
							// 模型已下载
							Log.info("使用已下载的模型： " + cachedFile.toString());
						}
						modelFile = cachedFile;
					} catch (Exception e) {
						Log.info("下载模型文件失败", e);
					}
				} else {
					modelFile = new File(path);
					if (!modelFile.exists()) {
						Log.info("加载模型失败，模型文件不存在：path = " + path);
						modelFile = null;
					}
				}
				if (modelFile != null) {
					// TODO: 识别并加载不同类型的模型
					try {
						Model = ModelSerializer.restoreComputationGraph(modelFile, true);
						flag = true;
					} catch (IOException e) {
						Log.info("加载模型失败", e);
					}
				}
			} else {
				Log.info("加载模型失败：path为空");
			}
		} else {
			Log.info("加载模型失败：模型不为空，初始化过的模型不能再加载。");
		}

		return flag;
	}
}
