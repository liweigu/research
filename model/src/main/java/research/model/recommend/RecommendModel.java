package research.model.recommend;

import java.util.HashMap;
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
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.nd4j.linalg.schedule.ISchedule;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;

/**
 * 推荐模型
 * 
 * @author liweigu714@163.com
 *
 */
public abstract class RecommendModel implements RecommendModelI {
	static ComputationGraph ComputationGraph;
	static Map<String, Object> InitProps = new HashMap<String, Object>();

	public Object getModel() {
		if (ComputationGraph == null) {
			throw new IllegalStateException("模型未初始化");
		}
		return ComputationGraph;
	}

	public Map<String, Object> getInitProps() {
		return InitProps;
	}

	public void initModel(Map<String, Object> initProps) {
		if (ComputationGraph == null) {
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
			double dropOut = 0;

			NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
			builder.seed(140);
			builder.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
			builder.weightInit(WeightInit.XAVIER);
			if (l2 > 0) {
				System.out.println("l2 = " + l2);
				builder.setL2(l2);
			}
			if (dropOut > 0) {
				System.out.println("dropOut = " + dropOut);
				builder.dropOut(dropOut);
			}
			builder.updater(new Adam(mapSchedule));

			GraphBuilder graphBuilder = builder.graphBuilder().backpropType(BackpropType.Standard).addInputs("input").setOutputs("output");
			graphBuilder = graphBuilder.addLayer("dense1", new DenseLayer.Builder().nIn(inputSize).nOut(100) // 20
					.updater(new Adam(mapSchedule)).weightInit(WeightInit.RELU).activation(Activation.RELU).build(), "input");
			graphBuilder = graphBuilder.addLayer("dense2",
					new DenseLayer.Builder().nIn(100).nOut(10).updater(new Adam(mapSchedule)).weightInit(WeightInit.RELU).activation(Activation.RELU).build(),
					"dense1");
			graphBuilder = graphBuilder.addLayer("output", new OutputLayer.Builder(LossFunction.MSE).nIn(10).nOut(outputSize).updater(new Adam(mapSchedule))
					.weightInit(WeightInit.XAVIER).activation(Activation.IDENTITY).build(), "dense2");

			ComputationGraph = new ComputationGraph(graphBuilder.build());
			ComputationGraph.init();

			UIServer uiServer = UIServer.getInstance();
			StatsStorage memoryStatsStorage = new InMemoryStatsStorage();
			uiServer.attach(memoryStatsStorage);
			int listenerFrequency = 10;
			ComputationGraph.setListeners(new StatsListener(memoryStatsStorage, listenerFrequency), new ScoreIterationListener(listenerFrequency));

			System.out.println(ComputationGraph.summary());
		}
	}

	public void save(String path) {
		throw new UnsupportedOperationException();
	}

	public void restore(String path) {
		throw new UnsupportedOperationException();
	}
}
