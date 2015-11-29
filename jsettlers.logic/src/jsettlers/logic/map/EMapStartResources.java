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
package jsettlers.logic.map;

import jsettlers.common.map.object.MapObject;
import jsettlers.common.map.object.MovableObject;
import jsettlers.common.map.object.StackObject;
import jsettlers.common.material.EMaterialType;
import jsettlers.common.movable.EMovableType;

import java.util.List;
import java.util.Vector;

public enum EMapStartResources {
	LOW_GOODS(1),
	MEDIUM_GOODS(2),
	HIGH_GOODS(3);

	public final int value;

	EMapStartResources(int value) {
		this.value = value;
	}

	public static EMapStartResources FromMapValue(int mapValue) {
		for (int i=0; i< EMapStartResources.values().length; i++) {
			if (EMapStartResources.values()[i].value == mapValue) return EMapStartResources.values()[i];
		}

		System.err.println("wrong value for 'EMapStartResources' "+ Integer.toString(mapValue) +"!");

		return EMapStartResources.HIGH_GOODS;
	}

	public static List<MapObject> generateStackObjects(EMapStartResources mapStartResources) {
		List<MapObject> goods = new Vector<MapObject>();
		switch (mapStartResources) {
		case LOW_GOODS:
			goods.add(new StackObject(EMaterialType.PLANK, 6));
			goods.add(new StackObject(EMaterialType.PLANK, 6));
			goods.add(new StackObject(EMaterialType.STONE, 6));
			goods.add(new StackObject(EMaterialType.STONE, 6));
			goods.add(new StackObject(EMaterialType.BLADE, 5));
			goods.add(new StackObject(EMaterialType.HAMMER, 6));
			goods.add(new StackObject(EMaterialType.AXE, 3));
			goods.add(new StackObject(EMaterialType.PICK, 2));
			goods.add(new StackObject(EMaterialType.SAW, 1));
			break;
		case MEDIUM_GOODS:
			goods.add(new StackObject(EMaterialType.PLANK, 7));
			goods.add(new StackObject(EMaterialType.PLANK, 6));
			goods.add(new StackObject(EMaterialType.PLANK, 6));
			goods.add(new StackObject(EMaterialType.PLANK, 6));
			goods.add(new StackObject(EMaterialType.STONE, 7));
			goods.add(new StackObject(EMaterialType.STONE, 6));
			goods.add(new StackObject(EMaterialType.STONE, 6));
			goods.add(new StackObject(EMaterialType.STONE, 6));
			goods.add(new StackObject(EMaterialType.COAL, 8));
			goods.add(new StackObject(EMaterialType.COAL, 7));
			goods.add(new StackObject(EMaterialType.IRONORE, 5));
			goods.add(new StackObject(EMaterialType.FISH, 3));
			goods.add(new StackObject(EMaterialType.BREAD, 7));
			goods.add(new StackObject(EMaterialType.MEAT, 5));
			goods.add(new StackObject(EMaterialType.BLADE, 5));
			goods.add(new StackObject(EMaterialType.BLADE, 5));
			goods.add(new StackObject(EMaterialType.HAMMER, 8));
			goods.add(new StackObject(EMaterialType.HAMMER, 7));
			goods.add(new StackObject(EMaterialType.AXE, 6));
			goods.add(new StackObject(EMaterialType.PICK, 4));
			goods.add(new StackObject(EMaterialType.SAW, 2));
			goods.add(new StackObject(EMaterialType.SCYTHE, 1));
			goods.add(new StackObject(EMaterialType.FISHINGROD, 1));
			break;
		default:
			goods.add(new StackObject(EMaterialType.PLANK, 8));
			goods.add(new StackObject(EMaterialType.PLANK, 8));
			goods.add(new StackObject(EMaterialType.PLANK, 7));
			goods.add(new StackObject(EMaterialType.PLANK, 7));
			goods.add(new StackObject(EMaterialType.STONE, 8));
			goods.add(new StackObject(EMaterialType.STONE, 8));
			goods.add(new StackObject(EMaterialType.STONE, 8));
			goods.add(new StackObject(EMaterialType.STONE, 7));
			goods.add(new StackObject(EMaterialType.COAL, 7));
			goods.add(new StackObject(EMaterialType.COAL, 7));
			goods.add(new StackObject(EMaterialType.COAL, 6));
			goods.add(new StackObject(EMaterialType.COAL, 6));
			goods.add(new StackObject(EMaterialType.IRONORE, 6));
			goods.add(new StackObject(EMaterialType.IRONORE, 6));
			goods.add(new StackObject(EMaterialType.FISH, 7));
			goods.add(new StackObject(EMaterialType.BREAD, 8));
			goods.add(new StackObject(EMaterialType.BREAD, 7));
			goods.add(new StackObject(EMaterialType.MEAT, 8));
			goods.add(new StackObject(EMaterialType.BLADE, 8));
			goods.add(new StackObject(EMaterialType.BLADE, 7));
			goods.add(new StackObject(EMaterialType.HAMMER, 7));
			goods.add(new StackObject(EMaterialType.HAMMER, 6));
			goods.add(new StackObject(EMaterialType.HAMMER, 6));
			goods.add(new StackObject(EMaterialType.HAMMER, 6));
			goods.add(new StackObject(EMaterialType.AXE, 8));
			goods.add(new StackObject(EMaterialType.PICK, 5));
			goods.add(new StackObject(EMaterialType.SAW, 3));
			goods.add(new StackObject(EMaterialType.SCYTHE, 3));
			goods.add(new StackObject(EMaterialType.FISHINGROD, 2));
			break;
		}
		return goods;
	}

	public static List<MapObject> generateMovableObjects(EMapStartResources mapStartResources, byte playerId) {
		List<MapObject> movables = new Vector<MapObject>();
		switch (mapStartResources) {
		case LOW_GOODS:
			for (byte i = 0; i < 2; i++) movables.add(new MovableObject(EMovableType.MINER, playerId));
			movables.add(new MovableObject(EMovableType.SMITH, playerId));
			movables.add(new MovableObject(EMovableType.BOWMAN_L1, playerId));
			movables.add(new MovableObject(EMovableType.PIKEMAN_L1, playerId));
			for (byte i = 0; i < 6; i++) movables.add(new MovableObject(EMovableType.SWORDSMAN_L1, playerId));
			for (byte i = 0; i < 16; i++) movables.add(new MovableObject(EMovableType.BEARER, playerId));
			break;
		case MEDIUM_GOODS:
			for (byte i = 0; i < 4; i++) movables.add(new MovableObject(EMovableType.MINER, playerId));
			for (byte i = 0; i < 2; i++) movables.add(new MovableObject(EMovableType.SMITH, playerId));
			for (byte i = 0; i < 2; i++) movables.add(new MovableObject(EMovableType.BOWMAN_L1, playerId));
			for (byte i = 0; i < 2; i++) movables.add(new MovableObject(EMovableType.PIKEMAN_L1, playerId));
			for (byte i = 0; i < 10; i++) movables.add(new MovableObject(EMovableType.SWORDSMAN_L1, playerId));
			for (byte i = 0; i < 32; i++) movables.add(new MovableObject(EMovableType.BEARER, playerId));
			for (byte i = 0; i < 4; i++) movables.add(new MovableObject(EMovableType.DONKEY, playerId));
			break;
		default:
			for (byte i = 0; i < 6; i++) movables.add(new MovableObject(EMovableType.MINER, playerId));
			for (byte i = 0; i < 3; i++) movables.add(new MovableObject(EMovableType.SMITH, playerId));
			for (byte i = 0; i < 4; i++) movables.add(new MovableObject(EMovableType.BOWMAN_L1, playerId));
			for (byte i = 0; i < 4; i++) movables.add(new MovableObject(EMovableType.PIKEMAN_L1, playerId));
			for (byte i = 0; i < 12; i++) movables.add(new MovableObject(EMovableType.SWORDSMAN_L1, playerId));
			for (byte i = 0; i < 50; i++) movables.add(new MovableObject(EMovableType.BEARER, playerId));
			for (byte i = 0; i < 6; i++) movables.add(new MovableObject(EMovableType.DONKEY, playerId));
			break;
		}
		return movables;
	}
}