/*******************************************************************************
 * Copyright (c) 2015
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.ai.construction;

import static jsettlers.common.movable.EMovableType.DIGGER;

import java.util.ArrayList;
import java.util.List;

import jsettlers.ai.highlevel.AiStatistics;
import jsettlers.algorithms.construction.AbstractConstructionMarkableMap;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.position.ShortPoint2D;

/**
 * Algorithm: find all possible construction points within the borders of the player - calculates a score and take the position with the best score -
 * score is affected by the distance to needed buildings (eg. lumberjacks for sawmills) - score is affected by the distance to diggers so that the
 * diggers and bricklayers don't need to walk over the whole map to build the next building
 * 
 * @author codingberlin
 */
public class NearNeededBuildingConstructionPositionFinder implements IBestConstructionPositionFinder {

	EBuildingType buildingType;
	EBuildingType neededBuildingType;

	public NearNeededBuildingConstructionPositionFinder(EBuildingType ownBuildingType, EBuildingType neededBuildingType) {
		this.buildingType = ownBuildingType;
		this.neededBuildingType = neededBuildingType;
	}

	@Override
	public ShortPoint2D findBestConstructionPosition(AiStatistics aiStatistics, AbstractConstructionMarkableMap constructionMap, byte playerId) {
		List<ShortPoint2D> neededBuildings = aiStatistics.getBuildingPositionsOfTypeForPlayer(neededBuildingType, playerId);
		List<ShortPoint2D> diggers = aiStatistics.getMovablePositionsByTypeForPlayer(DIGGER, playerId);
		List<ScoredConstructionPosition> scoredConstructionPositions = new ArrayList<ScoredConstructionPosition>();
		for (ShortPoint2D point : aiStatistics.getLandForPlayer(playerId)) {
			if (constructionMap.canConstructAt(point.x, point.y, buildingType, playerId) && !aiStatistics.blocksWorkingAreaOfOtherBuilding(point)) {
				ShortPoint2D nearestDiggerPosition = aiStatistics.detectNearestPointFromList(point, diggers);
				ShortPoint2D nearestNeededBuilding = aiStatistics.detectNearestPointFromList(point, neededBuildings);
				double nearestNeededBuildingDistance = aiStatistics.getDistance(point, nearestNeededBuilding);
				double nearestDiggerDistance = aiStatistics.getDistance(point, nearestDiggerPosition);
				scoredConstructionPositions.add(new ScoredConstructionPosition(point, nearestNeededBuildingDistance + nearestDiggerDistance));
			}
		}

		return ScoredConstructionPosition.detectPositionWithLowestScore(scoredConstructionPositions);
	}
}