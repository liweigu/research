package research.model.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration.GraphBuilder;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.nd4j.linalg.schedule.ISchedule;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;

import research.core.data.DataFrame;
import research.core.vo.Item;
import research.core.vo.Label;
import research.core.vo.Rating;
import research.core.vo.User;
import research.model.eval.RatingEvaluator;

/**
 * 用户物品推荐模型
 * 
 * @author liweigu714@163.com
 *
 */
public class UserItemRecommendModel extends RecommendModel {

	/**
	 * 初始化模型
	 * 
	 * @param initProps 初始化参数
	 */
	public void initModel(Map<String, Object> initProps) {
		if (Model == null) {
			int inputSize;
			if (initProps != null && initProps.containsKey("inputSize")) {
				inputSize = (int) initProps.get("inputSize");
			} else {
				throw new IllegalArgumentException("initProps缺少属性：inputSize");
			}
			InitProps = initProps;
			int outputSize = 1;

			double learningRate = 1e-4;
			System.out.println("learningRate = " + learningRate);
			Map<Integer, Double> lrSchedule = new HashMap<Integer, Double>();
			lrSchedule.put(0, learningRate);
			lrSchedule.put(1000, 0.8 * learningRate);
			lrSchedule.put(5000, 0.5 * learningRate);
			lrSchedule.put(10000, 0.2 * learningRate);
			lrSchedule.put(15000, 0.1 * learningRate);
			System.out.println("lrSchedule = " + lrSchedule);
			ISchedule mapSchedule = new MapSchedule(ScheduleType.ITERATION, lrSchedule);

			double l2 = 1e-5;

			NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
			builder.seed(140);
			builder.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
			builder.weightInit(WeightInit.XAVIER);
			if (l2 > 0) {
				System.out.println("l2 = " + l2);
				builder.setL2(l2);
			}
			builder.updater(new Adam(mapSchedule));

			GraphBuilder graphBuilder = builder.graphBuilder().backpropType(BackpropType.Standard).addInputs("input").setOutputs("output");
			graphBuilder = graphBuilder.addLayer("dense1", new DenseLayer.Builder().nIn(inputSize).nOut(100).updater(new Adam(mapSchedule))
					.weightInit(WeightInit.RELU).activation(Activation.RELU).build(), "input");
			graphBuilder = graphBuilder.addLayer("dense2",
					new DenseLayer.Builder().nIn(100).nOut(10).updater(new Adam(mapSchedule)).weightInit(WeightInit.RELU).activation(Activation.RELU).build(),
					"dense1");
			graphBuilder = graphBuilder.addLayer("output", new OutputLayer.Builder(LossFunction.MSE).nIn(10).nOut(outputSize).updater(new Adam(mapSchedule))
					.weightInit(WeightInit.XAVIER).activation(Activation.IDENTITY).build(), "dense2");

			ComputationGraph computationGraph = new ComputationGraph(graphBuilder.build());
			computationGraph.init();

			UIServer uiServer = UIServer.getInstance();
			StatsStorage memoryStatsStorage = new InMemoryStatsStorage();
			uiServer.attach(memoryStatsStorage);
			int listenerFrequency = 10;
			computationGraph.setListeners(new StatsListener(memoryStatsStorage, listenerFrequency), new ScoreIterationListener(listenerFrequency));

			System.out.println(computationGraph.summary());

			Model = computationGraph;
		}
	}

	/**
	 * 训练
	 * 
	 * @param ratings 评分列表
	 */
	public void fit(List<Rating> ratings) {
		List<List<Double>> features = new ArrayList<List<Double>>();
		List<List<Double>> labels = new ArrayList<List<Double>>();
		for (Rating rating : ratings) {
			User user = rating.getUser();
			Item item = rating.getItem();
			Label label = rating.getLabel();

			List<Double> feature = new ArrayList<Double>();
			feature.addAll(user.doubleValue());
			feature.addAll(item.doubleValue());
			// System.out.println(feature.size());
			features.add(feature);
			labels.add(label.doubleValue());
		}
		this.fit(features, labels);
	}

	/**
	 * 训练
	 * 
	 * @param features 特征值
	 * @param labels 标签值
	 */
	@Override
	public void fit(List<List<Double>> features, List<List<Double>> labels) {
		ComputationGraph computationGraph = (ComputationGraph) this.getModel();

		DataSet trainDataSet = getDataSet(features, labels);
		computationGraph.fit(trainDataSet);
	}

	/**
	 * 将数值转为DataSet
	 * 
	 * @param features 特征值
	 * @param labels 标签值
	 * @return DataSet 数据集
	 */
	private DataSet getDataSet(List<List<Double>> features, List<List<Double>> labels) {
		DataSet dataSet = null;
		Map<String, Object> initProps = this.getInitProps();
		if (initProps != null) {
			int inputSize = (int) initProps.get("inputSize");
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

	/**
	 * 训练
	 * 
	 * @param dataFrame 数据框架
	 */
	@Override
	public void fit(DataFrame dataFrame) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 预测
	 * 
	 * @param features 特征值
	 */
	@Override
	public List<List<Double>> output(List<List<Double>> features) {
		List<List<Double>> result = new ArrayList<List<Double>>();

		ComputationGraph computationGraph = (ComputationGraph) this.getModel();

		DataSet dataSet = this.getDataSet(features, null);
		INDArray featuresINDArray = dataSet.getFeatures();

		for (int i = 0; i < featuresINDArray.rows(); i++) {
			INDArray featureINDArray = featuresINDArray.getRow(i);
			INDArray[] outputs = computationGraph.output(featureINDArray);
			// System.out.println("outputs.length = " + outputs.length);
			double value = outputs[0].getDouble(0);
			result.add(Collections.singletonList(value));
		}

		return result;
	}

	/**
	 * 结果评估
	 * 
	 * @param ratings 评分列表
	 */
	public void evaluate(List<Rating> ratings) {
		List<List<Double>> features = new ArrayList<List<Double>>();
		List<List<Double>> labels = new ArrayList<List<Double>>();
		for (Rating rating : ratings) {
			User user = rating.getUser();
			Item item = rating.getItem();
			Label label = rating.getLabel();

			List<Double> feature = new ArrayList<Double>();
			feature.addAll(user.doubleValue());
			feature.addAll(item.doubleValue());
			features.add(feature);
			labels.add(label.doubleValue());
		}
		this.evaluate(features, labels);
	}

	/**
	 * 结果评估
	 * 
	 * @param features 特征值
	 * @param labels 标签值
	 */
	@Override
	public void evaluate(List<List<Double>> features, List<List<Double>> labels) {
		List<List<Double>> predictedResults = this.output(features);
		// System.out.println("predictedResults.size() = " + predictedResults.size());
		Map<String, Double> evalResults = RatingEvaluator.eval(predictedResults, labels, null);
		System.out.println("mae = " + evalResults.get("mae") + ", mse = " + evalResults.get("mse") + ", rmse = " + evalResults.get("rmse"));
	}

}
