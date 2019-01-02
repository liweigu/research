package recommend.sample.movielens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import research.core.vo.Rating;
import research.data.reader.UserItemReader;
import research.model.recommend.UserItemRecommendModel;

/**
 * 电影推荐
 * 
 * @author liweigu714@163.com
 *
 */
public class RecommendMovie {
	public static String BasePath = "E:/data/ai/librec/movielens/ml-1m/";

	public static void main(String[] args) {
		// 数据
		// TODO: 暂时预先划分了训练集/验证集/测试集，实现数据分割器后再支持动态分组。
		String userDataPath = BasePath + "users.dat";
		String itemDataPath = BasePath + "movies.dat";
		String trainDataPath = BasePath + "ratings_train.dat";
		String validDataPath = BasePath + "ratings_valid.dat";
		String testDataPath = BasePath + "ratings_test.dat";

		// 训练
		UserItemRecommendModel userItemRecommendModel = new UserItemRecommendModel();
		Map<String, Object> initProps = new HashMap<String, Object>();
		initProps.put("inputSize", 23);
		userItemRecommendModel.initModel(initProps);

		int epoch = 50; // 100, 2500
		int batchSize = 32;
		for (int i = 0; i < epoch; i++) {
			if (i % 10 == 0) {
				System.out.println("i = " + i);
			}
			int start = batchSize * i;
			// 分批读取训练数据
			List<Rating> ratings = UserItemReader.readRatings(userDataPath, itemDataPath, trainDataPath, start,
					batchSize);
			userItemRecommendModel.fit(ratings);
		}

		// 验证与评估
		// 读取全部验证数据
		List<Rating> validRatings = UserItemReader.readRatings(userDataPath, itemDataPath, validDataPath, 0, -1);
		// 结果评估
		userItemRecommendModel.evaluate(validRatings);
	}

}
