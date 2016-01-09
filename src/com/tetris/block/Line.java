package com.tetris.block;

import java.awt.Container;

import com.tetris.controll.Block;
import com.tetris.controll.TetrisView;

public class Line extends TetrisView {

	public Line(Container con){
		super(2, 4);	//회전수 2개, 블럭4칸 필요

		// ─
		blockInfo[0][0] = new Block(0,-1);
		blockInfo[0][1] = new Block(0,0);
		blockInfo[0][2] = new Block(0,1);
		blockInfo[0][3] = new Block(0,2);

		// │
		blockInfo[1][0] = new Block(-1,0);
		blockInfo[1][1] = new Block(0,0);
		blockInfo[1][2] = new Block(1,0);
		blockInfo[1][3] = new Block(2,0);

		this.setDefaultRandom();	//랜덤셋팅
		this.setBlock(con);			//컨테이너에 등록
	}
}
