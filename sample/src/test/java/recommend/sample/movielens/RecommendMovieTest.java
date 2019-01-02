package recommend.sample.movielens;

import junit.framework.TestCase;

public class RecommendMovieTest extends TestCase{
	public void testRun(){
		String basePath = "/data/dl4j/data/movielens/ml-1m/";
		RecommendMovie.run(basePath);
	}
}
