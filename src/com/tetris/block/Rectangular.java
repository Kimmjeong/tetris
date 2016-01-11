package com.tetris.block;

import java.awt.Container;

import com.tetris.controll.Block;
import com.tetris.controll.TetrisView;

public class Rectangular extends TetrisView {

	public Rectangular(Container con) {
		super(1,4); // 회전수 1개, 블럭4칸필요
		
		// 블럭 한칸의 모양 잡아주기
		// 네모는 회전수가 1개 이기 때문에 blockInfo가 1개이다
		blockInfo[0][0] = new Block(0,0);
		blockInfo[0][1] = new Block(0,1);
		blockInfo[0][2] = new Block(1,0);
		blockInfo[0][3] = new Block(1,1);
		
		System.out.println("Rectangular");
		setDefaultRandom(); // 랜덤세팅
		setBlock(con); // 컨테이너에 등록
	}
	
}
