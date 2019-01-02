package research.model.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import research.core.data.DataFrame;

/**
 * 用户物品推荐
 * 
 * @author liweigu714@163.com
 *
 */
public class UserItemRecommend extends RecommendModel {

	@Override
	public void fit(List<List<Double>> features, List<List<Double>> labels) {
		ComputationGraph computationGraph = (ComputationGraph) this.getModel();

		DataSet trainDataSet = getDataSet(features, labels);
		computationGraph.fit(trainDataSet);
	}

	private DataSet getDataSet(List<List<Double>> features, List<List<Double>> labels) {
		DataSet dataSet = null;
		Map<String, Object> initProps = this.getInitProps();
		if (initProps != null) {
			int inputSize = Integer.parseInt((String) initProps.get("inputSize"));
			int outputSize = 1;
			int batchSize = features.size();
			double[] featuresData = new double[inputSize * batchSize];
			for (int i = 0; i < batchSize; i++) {
				for (int j = 0; j < inputSize; j++) {
					double value = features.get(i).get(j);
					featuresData[i + j * batchSize] = value;
				}
			}
			INDArray featuresINDArray = Nd4j.create(featuresData, new int[] { batchSize, inputSize });

			INDArray labelsINDArray = null;
			if (labels != null) {
				double[] labelsData = new double[batchSize];
				for (int i = 0; i < batchSize; i++) {
					labelsData[i] = labels.get(i).get(0);
				}
				labelsINDArray = Nd4j.create(labelsData, new int[] { batchSize, outputSize });
			}

			dataSet = new DataSet(featuresINDArray, labelsINDArray);
		}
		return dataSet;
	}

	@Override
	public void fit(DataFrame dataFrame) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<List<Double>> output(List<List<Double>> features) {
		List<List<Double>> result = new ArrayList<List<Double>>();

		ComputationGraph computationGraph = (ComputationGraph) this.getModel();

		DataSet dataSet = this.getDataSet(features, null);
		INDArray featuresINDArray = dataSet.getFeatures();

		INDArray[] outputs = computationGraph.output(featuresINDArray);
		for (INDArray output : outputs) {
			double value = output.getDouble(0);
			result.add(Collections.singletonList(value));
		}

		return result;
	}

	@Override
	public void evaluate(List<List<Double>> features, List<List<Double>> labels) {

	}

}
