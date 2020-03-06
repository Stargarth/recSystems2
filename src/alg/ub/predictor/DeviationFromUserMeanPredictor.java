package alg.ub.predictor;

import java.util.Map;
import java.util.Set;

import alg.ub.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

public class DeviationFromUserMeanPredictor implements Predictor {

	@Override
	public Double getPrediction(Integer userId, Integer itemId, Map<Integer, Profile> userProfileMap,
			Map<Integer, Profile> itemProfileMap, Neighbourhood neighbourhood, SimilarityMap simMap) {
		
		Set<Integer> neighbours = neighbourhood.getNeighbours(userId);
		Double s1 = 0d, s2 = 0d;
		// return null if the user has no neighbours
		if (neighbours == null)
			return null;
		
		for(Integer neighbour: neighbours) // iterate over each neighbour
		{
			Double rating = userProfileMap.get(neighbour).getValue(itemId); // get the neighbour's rating for the target item
			Profile profile = simMap.getSimilarities(userId);
			if (profile != null && rating != null) {
				double sim = simMap.getSimilarity(userId, neighbour);
				if (userProfileMap.get(neighbour) == null)
					continue;
				
				s1+=(sim*(rating-userProfileMap.get(neighbour).getMeanValue()));
				s2+=sim;
			}
		}
		return s2 != 0d ? userProfileMap.get(userId).getMeanValue()+(s1/s2) : 0d;
	}

}
