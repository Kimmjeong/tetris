package com.tetris.block;

import java.awt.Container;

import com.tetris.controll.Block;
import com.tetris.controll.TetrisView;

public class RightTwo extends TetrisView {

	public RightTwo(Container con){
		super(2, 4);	//영역길이, 각도갯수, 판넬개수

		blockInfo[0][0] = new Block(0,0);
		blockInfo[0][1] = new Block(1,0);
		blockInfo[0][2] = new Block(1,1);
		blockInfo[0][3] = new Block(2,1);

		// _|-
		blockInfo[1][0] = new Block(0,1);
		blockInfo[1][1] = new Block(0,0);
		blockInfo[1][2] = new Block(1,0);
		blockInfo[1][3] = new Block(1,-1);

		setDefaultRandom();	//랜덤셋팅
		setBlock(con);			//컨테이너에 등록
	}
	
}
