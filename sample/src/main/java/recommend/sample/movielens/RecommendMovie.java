package recommend.sample.movielens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import research.core.vo.Rating;
import research.data.reader.UserItemReader;
import research.model.recommend.UserItemRecommendModel;

/**
 * ��Ӱ�Ƽ�
 * 
 * @author liweigu714@163.com
 *
 */
public class RecommendMovie {
	public static String BasePath = "E:/data/ai/librec/movielens/ml-1m/";

	public static void main(String[] args) {
		// ����
		// TODO: ��ʱѵ����/��֤��/���Լ���Ԥ�Ȼ��ֺõ��ļ����������Ϸָ�����֧�ֶ�̬�������ݡ�
		String userDataPath = BasePath + "users.dat";
		String itemDataPath = BasePath + "movies.dat";
		String trainDataPath = BasePath + "ratings_train.dat";
		String validDataPath = BasePath + "ratings_valid.dat";
		String testDataPath = BasePath + "ratings_test.dat";

		// ѵ��ģ��
		UserItemRecommendModel userItemRecommendModel = new UserItemRecommendModel();
		Map<String, Object> initProps = new HashMap<String, Object>();
		initProps.put("inputSize", 23);
		userItemRecommendModel.initModel(initProps);

		int epoch = 100; // 2500
		int batchSize = 32;
		for (int i = 0; i < epoch; i++) {
			if (i % 100 == 0) {
				System.out.println("i = " + i);
			}
			int start = batchSize * i;
			// ������ȡ����
			List<Rating> ratings = UserItemReader.readRatings(userDataPath, itemDataPath, trainDataPath, start,
					batchSize);
			userItemRecommendModel.fit(ratings);
		}

		// Ԥ���������
		// ��ȡȫ������
		List<Rating> validRatings = UserItemReader.readRatings(userDataPath, itemDataPath, validDataPath, 0, -1);
		// ����
		userItemRecommendModel.evaluate(validRatings);
	}

}
