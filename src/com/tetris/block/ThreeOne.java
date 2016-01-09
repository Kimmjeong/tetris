package com.tetris.block;

import java.awt.Container;

import com.tetris.controll.Block;
import com.tetris.controll.TetrisView;

public class ThreeOne extends TetrisView {

	public ThreeOne(Container con){
		super(4, 4);	// 회전수 4개, 블럭4칸 필요

		// ┌
		blockInfo[0][0] = new Block(0,1);
		blockInfo[0][1] = new Block(0,0);
		blockInfo[0][2] = new Block(1,0);
		blockInfo[0][3] = new Block(2,0);

		// ┐
		blockInfo[1][0] = new Block(1,2);
		blockInfo[1][1] = new Block(0,2);
		blockInfo[1][2] = new Block(0,1);
		blockInfo[1][3] = new Block(0,0);

		// ┘
		blockInfo[2][0] = new Block(2,0);
		blockInfo[2][1] = new Block(2,1);
		blockInfo[2][2] = new Block(1,1);
		blockInfo[2][3] = new Block(0,1);

		// └
		blockInfo[3][0] = new Block(0,0);
		blockInfo[3][1] = new Block(1,0);
		blockInfo[3][2] = new Block(1,1);
		blockInfo[3][3] = new Block(1,2);

		setDefaultRandom();	//랜덤셋팅
		setBlock(con);			//컨테이너에 등록
	}
}
