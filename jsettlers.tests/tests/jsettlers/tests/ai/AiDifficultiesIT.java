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
package jsettlers.tests.ai;

import static org.junit.Assert.fail;

import org.junit.Test;

import jsettlers.TestUtils;
import jsettlers.ai.highlevel.AiStatistics;
import jsettlers.common.CommonConstants;
import jsettlers.common.ai.EWhatToDoAiType;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.logging.StatisticsStopWatch;
import jsettlers.graphics.startscreen.interfaces.IStartedGame;
import jsettlers.logic.constants.MatchConstants;
import jsettlers.logic.map.save.MapList;
import jsettlers.logic.map.save.loader.MapLoader;
import jsettlers.logic.player.PlayerSetting;
import jsettlers.main.JSettlersGame;
import jsettlers.main.replay.ReplayTool;
import jsettlers.network.client.OfflineNetworkConnector;

/**
 * @author codingberlin
 */
public class AiDifficultiesIT {
	public static final int MINUTES = 1000 * 60;
	public static final int JUMP_FORWARD = 10 * MINUTES;

	static {
		CommonConstants.ENABLE_CONSOLE_LOGGING = true;
		TestUtils.setupResourcesManager();
	}

	@Test
	public void easyShouldConquerVeryEasy() {
		holdBattleBetween(EWhatToDoAiType.ROMAN_EASY, EWhatToDoAiType.ROMAN_VERY_EASY, 90 * MINUTES);
	}

	@Test
	public void hardShouldConquerEasy() {
		holdBattleBetween(EWhatToDoAiType.ROMAN_HARD, EWhatToDoAiType.ROMAN_EASY, 70 * MINUTES);
	}

	@Test
	public void veryHardShouldConquerHard() {
		holdBattleBetween(EWhatToDoAiType.ROMAN_VERY_HARD, EWhatToDoAiType.ROMAN_HARD, 90 * MINUTES);
	}

	@Test
	public void verHardShouldProduceCertainAmountOfSoldiersWithin90Minutes() {
		PlayerSetting[] playerSettings = new PlayerSetting[4];
		playerSettings[0] = new PlayerSetting(true, EWhatToDoAiType.ROMAN_VERY_HARD);
		playerSettings[1] = new PlayerSetting(false, null);
		playerSettings[2] = new PlayerSetting(false, null);
		playerSettings[3] = new PlayerSetting(false, null);
		JSettlersGame.GameRunner startingGame = createStartingGame(playerSettings);
		IStartedGame startedGame = ReplayTool.waitForGameStartup(startingGame);

		MatchConstants.clock().fastForwardTo(90 * MINUTES);
		ReplayTool.awaitShutdown(startedGame);

		short expectedMinimalProducedSoldiers = 160;
		short producedSoldiers = startingGame.getMainGrid().getPartitionsGrid().getPlayer(0).getEndgameStatistic().getAmountOfProducedSoldiers();
		if (producedSoldiers < expectedMinimalProducedSoldiers) {
			fail("ROMAN_VERY_HARD was not able to produce " + expectedMinimalProducedSoldiers + " within 90 minutes.\nOnly " + producedSoldiers + " "
					+ "soldiers were produced. Some code changes make the AI weaker.");
		}
		ensureRuntimePerformance("to apply rules", startingGame.getAiExecutor().getApplyRulesStopWatch(), 50, 2500);
		ensureRuntimePerformance("tp update statistics", startingGame.getAiExecutor().getUpdateStatisticsStopWatch(), 50, 2500);
	}

	private void holdBattleBetween(EWhatToDoAiType expectedWinner, EWhatToDoAiType expectedLooser, int maximumTimeToWin) {
		PlayerSetting[] playerSettings = new PlayerSetting[4];
		playerSettings[0] = new PlayerSetting(true, expectedLooser);
		playerSettings[1] = new PlayerSetting(true, expectedWinner);
		playerSettings[2] = new PlayerSetting(false, null);
		playerSettings[3] = new PlayerSetting(false, null);

		JSettlersGame.GameRunner startingGame = createStartingGame(playerSettings);
		IStartedGame startedGame = ReplayTool.waitForGameStartup(startingGame);
		AiStatistics aiStatistics = new AiStatistics(startingGame.getMainGrid());

		int targetGameTime = 0;
		do {
			targetGameTime += JUMP_FORWARD;
			MatchConstants.clock().fastForwardTo(targetGameTime);
			aiStatistics.updateStatistics();
			if (aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.TOWER, (byte) 1) == 0) {
				stopAndFail(expectedWinner + " was defeated by " + expectedLooser, startedGame);
			}
			if (MatchConstants.clock().getTime() > maximumTimeToWin) {
				stopAndFail(expectedWinner + " was not able to defeat " + expectedLooser + " within " + (maximumTimeToWin / 60000)
						+ " minutes.\nIf the AI code was changed in a way which makes the " + expectedLooser + " stronger with the sideeffect that "
						+ "the " + expectedWinner + " needs more time to win you could make the " + expectedWinner + " stronger, too, or increase "
						+ "the maximumTimeToWin.",
						startedGame);
			}
		} while (aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.TOWER, (byte) 0) > 0);
		System.out.println("The battle between " + expectedWinner + " and " + expectedLooser + " took " + (MatchConstants.clock().getTime() / 60000) +
				" minutes.");
		ReplayTool.awaitShutdown(startedGame);

		ensureRuntimePerformance("to apply rules", startingGame.getAiExecutor().getApplyRulesStopWatch(), 50, 3000);
		ensureRuntimePerformance("tp update statistics", startingGame.getAiExecutor().getUpdateStatisticsStopWatch(), 50, 2500);
	}

	private void ensureRuntimePerformance(String description, StatisticsStopWatch stopWatch, long median, int max) {
		System.out.println(description + ": " + stopWatch);
		if (stopWatch.getMedian() > median) {
			fail(description + "'s median is higher than " + median + ". It was " + stopWatch.getMedian() + ".\nSomething in the code changed which "
					+ "caused the AI to have a worse runtime performance.");
		}
		if (stopWatch.getMax() > max) {
			fail(description + "'s max is higher than " + max + ". It was " + stopWatch.getMax() + ".\nSomething in the code changed which "
					+ "caused the AI to have a worse runtime performance.");
		}
	}

	private JSettlersGame.GameRunner createStartingGame(PlayerSetting[] playerSettings) {
		// TODO change the map to River when https://github.com/jsettlers/settlers-remake/pull/238 was merged
		// because river is a map with identically start spots. There we can better compare the performance of the different AIs
		MapLoader mapCreator = MapList.getDefaultList().getMapById("066d3c28-8f37-41cf-96c1-270109f00b9f");

		JSettlersGame game = new JSettlersGame(mapCreator, 1l, new OfflineNetworkConnector(), (byte) 0, playerSettings);
		return (JSettlersGame.GameRunner) game.start();
	}

	private void stopAndFail(String reason, IStartedGame startedGame) {
		ReplayTool.awaitShutdown(startedGame);
		fail(reason);
	}

}
