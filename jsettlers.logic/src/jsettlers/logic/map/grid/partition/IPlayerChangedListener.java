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
package jsettlers.logic.map.grid.partition;

/**
 * An implementor of this interface may be called by the {@link PartitionsGrid} when a position changed it's player.
 * 
 * @author Andreas Eberle
 */
public interface IPlayerChangedListener {
	/**
	 * This is a default implementation doing nothing on calls to the methods of {@link IPlayerChangedListener}.
	 */
	public static final IPlayerChangedListener DEFAULT_IMPLEMENTATION = new IPlayerChangedListener() {
		@Override
		public void playerChangedAt(int x, int y, byte newPlayerId) {
		}
	};

	/**
	 * This method is called when the player of a position is changed.
	 * 
	 * @param x
	 *            x coordinate of the position.
	 * @param y
	 *            y coordinate of the position.
	 * @param newPlayerId
	 *            The id of the new player.
	 */
	void playerChangedAt(int x, int y, byte newPlayerId);
}
