package com.tetris.block;

import java.awt.Container;

import com.tetris.controll.Block;
import com.tetris.controll.TetrisView;

public class LeftTwo extends TetrisView {

	public LeftTwo(Container con){
		super(2, 4);	//회전수2, 블럭4칸필요

		// -|_
		blockInfo[0][0] = new Block(0,0);
		blockInfo[0][1] = new Block(0,1);
		blockInfo[0][2] = new Block(1,1);
		blockInfo[0][3] = new Block(1,2);

		// 
		blockInfo[1][0] = new Block(1,0);
		blockInfo[1][1] = new Block(0,0);
		blockInfo[1][2] = new Block(0,1);
		blockInfo[1][3] = new Block(-1,1);

		setDefaultRandom();	//랜덤셋팅
		setBlock(con);			//컨테이너에 등록
	}
}
