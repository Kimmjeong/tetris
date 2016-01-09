package com.tetris.controll;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class TetrisMove implements KeyListener, Runnable {

	private int time;			//빠르기
	protected boolean[][] grid;	//
	private boolean isKey = true;		//키보드활성화여부
	
	private TetrisView nextBrick;		//다음나올것
	private TetrisView currentBrick;	//현재의 아이템
	TetrisView view;
	
	Long score=0L; // 점수
	
//	public static boolean isRight = false;		//오른쪽여부
	Thread t;

	public TetrisMove(){
		view=new TetrisView();
		grid = new boolean[BasicSetting.xCnt][BasicSetting.yCnt];
		
		time = 500;

		view.addKeyListener(this);
		
		// 블럭 시작셋팅
		currentBrick = view.brickList.get(view.rnd.nextInt(view.brickList.size()));
		currentBrick.setColor(view.colorList.get(view.rnd.nextInt(view.colorList.size())));
		currentBrick.setDefaultLocation();
		setNextBrick();
				
		t = new Thread(this);
		t.start();
	}
	//넥스트 아이템 셋팅
	public void setNextBrick(){
		TetrisView temp;
		do{
			temp = view.brickList.get(view.rnd.nextInt(view.brickList.size()));
		}
		while (temp==currentBrick);		//현재아이템과 중복X
		nextBrick = temp;
		nextBrick.setColor(view.colorList.get(view.rnd.nextInt(view.colorList.size())));
		nextBrick.setNextLocation();	//위치셋팅
	}
	//아이템 새로 나오기 셋팅
	public void setNewBrick(){
		currentBrick = nextBrick;
		currentBrick.setDefaultLocation();
		setNextBrick();
	}
	//백그라운드 블럭 채우기
	public void setBack(int x, int y, Color c){
		view.background[x][y].setBackground(c);
		view.background[x][y].setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		grid[x][y] = true;
//		System.out.println("x="+x+", y="+y);
	}
	//백그라운드 블럭 비우기
	public void setEmptyBack(int x, int y){
		view.background[x][y].setBorder(null);
		view.background[x][y].setBackground(BasicSetting.bgColor);
		grid[x][y] = false;
	}
	//현재의 블록 백그라운드로 복사
	public void setCopyBlock(){
		Block[] tempBlock = currentBrick.getBlock();
		for (int i=0; i<tempBlock.length; i++){
			setBack(tempBlock[i].getX(), tempBlock[i].getY(), currentBrick.getColor());
		}
		currentBrick.setReadyLocation();	//대기위치로 돌아가기
	}
	//줄없애기 체크
	public void checkLine(){
		for (int i=0; i<grid[0].length; i++){	// i = Y값 = ROW
//			System.out.println(
			boolean isLine = true;
			for (int p=0; p<grid.length; p++){	// p = X값 = Column
//				System.out.print(p+","+i+" : " + grid[p][i]);
				if(!grid[p][i]){	//하나라도 공백이 있으면 break;
					isLine = false;
					break;
				}
			}
			if(isLine){	//줄없앰
				deleteLine(i);
				score=score+10;
			}
		}
	}
	//줄없애고 위에거 한칸씩 내리기
	public void deleteLine(int line){
		JPanel	tempPanel[] = new JPanel[BasicSetting.xCnt];

		
		for (int i=line; i>0; i--){		// i = 줄 = Y
			for (int p=0; p<grid.length; p++){	// p = 열 = X
				if(i==line){	//현재줄 템프변수에 저장
					tempPanel[p] = view.background[p][i];
					tempPanel[p].setLocation(p*BasicSetting.area,0);
				}
				//모든줄 한칸씩 내리기
				grid[p][i] = grid[p][i-1];
				view.background[p][i] = view.background[p][i-1];
				view.background[p][i].setLocation(p*BasicSetting.area, i*BasicSetting.area);
			}
		}
		//없앤줄 맨위로 올리기
		for (int i=0; i<grid.length; i++){
			view.background[i][0] = tempPanel[i];
			setEmptyBack(i,0);
		}
	}
	//프린트정보출력 임시
	public void printInfo(){
		Block temp = currentBrick.getCurrentXY();
		System.out.println("x : " + temp.getX() + ", y : " + temp.getY());
	}
	
	//아이템 회전체크 -> 회전
	public void goRotate(){
		Block[] tempBlock = currentBrick.getNextBlock();
		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX();
			int y = tempBlock[i].getY();
			if(x<0 || x>=BasicSetting.xCnt || y+1>=BasicSetting.yCnt || grid[x][y]){
				return;
			}
		}
		currentBrick.moveRotate();
	}
	//아이템다운체크 -> 이동
	public boolean goDown(){
		Block[] tempBlock = currentBrick.getBlock();
		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX();
			int y = tempBlock[i].getY() + 1;
			if(y+1 >= BasicSetting.yCnt || grid[x][y]){
				if(currentBrick.getCurrentXY().getY()<=1)	gameEnd();	//게임끝
				setCopyBlock();	//백그라운드블럭 셋팅
				checkLine();	//줄없애기 체크
				setNewBrick();	//다음아이템 셋팅
				return false;
			}
		}
		currentBrick.moveDown();
		return true;
	}
	//아이템오른쪽이동체크 -> 이동
	public void goRight(){
		Block[] tempBlock = currentBrick.getBlock();

		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX()+1;
			int y = tempBlock[i].getY();
			if(x >= BasicSetting.xCnt || grid[x][y]){
				return;
			}
		}
		currentBrick.moveRight();
	}
	//아이템왼쪽이동체크 -> 이동
	public void goLeft(){
		Block[] tempBlock = currentBrick.getBlock();

		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX()-1;
			int y = tempBlock[i].getY();
			if(x < 0 || grid[x][y]){
				return;
			}
		}
		currentBrick.moveLeft();
	}
	//벽돌없애기 체크 -> 없애기
	
	//키보드액션리스너
	public void keyPressed(KeyEvent e){
		if(!isKey)	return;
		switch (e.getKeyCode()){
			case KeyEvent.VK_DOWN:
				goDown();
				break;
			case KeyEvent.VK_LEFT:
				goLeft();
				break;
			case KeyEvent.VK_RIGHT:
				goRight();
				break;
			case KeyEvent.VK_UP:
				goRotate();
				break;
			case KeyEvent.VK_SPACE:
				while(goDown()){}
				break;
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	//게임종료체크	
	public void gameEnd(){
		int result=JOptionPane.showConfirmDialog(null, "게임이 종료되었습니다.", "게임종료", JOptionPane.OK_OPTION);
		
		view.recordView();
		
		if(result==0){
			view.content.removeAll();
			view.content.add(view.endPanel);
			view.revalidate();
			view.repaint();
		}
		
		t.stop();
	}
	//쓰레드메인
	public void run(){
		try
		{
			while(true){
				Thread.sleep(time);
				//판넬위쪽이면 키리스너 동작X
				if(currentBrick.getCurrentXY().getY() < 1)	isKey = false;
				else	isKey = true;
				goDown();		//아이템밑으로이동
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
