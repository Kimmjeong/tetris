package com.tetris.controll;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import com.tetris.block.LeftTwo;
import com.tetris.block.Line;
import com.tetris.block.OneThree;
import com.tetris.block.Rectangular;
import com.tetris.block.RightTwo;
import com.tetris.block.ThreeOne;
import com.tetris.block.Triangle;

public class TetrisMove implements KeyListener, Runnable {

	private int time;			//빠르기
	protected boolean[][] grid;	// 라인이 채워졌는지 빈 부분이 있는지 체크하기 위함
	private boolean isKey = true;		//키보드활성화여부
	
	private TetrisView nextBrick;		//다음나올것
	private TetrisView currentBrick;	//현재의 아이템
	TetrisView view;
	
	Long point=0L; // 점수
	
//	public static boolean isRight = false;		//오른쪽여부
	Thread t;

	public TetrisMove(){
		view=new TetrisView();
		grid = new boolean[BasicSetting.xCnt][BasicSetting.yCnt];
		
		time = 500;

		view.addKeyListener(this);
		
		// 블럭 시작셋팅
		setcurrBrick();
				
		t = new Thread(this);
		t.start();
	}
	
	// 색 지정
	public void setColor(TetrisView brick){
		if(brick instanceof Line)
			brick.setColor(new Color(67,116,217));
		else if(brick instanceof LeftTwo)
			brick.setColor(Color.green);
		else if(brick instanceof OneThree)
			brick.setColor(Color.blue);
		else if(brick instanceof Rectangular)
			brick.setColor(Color.yellow);
		else if(brick instanceof RightTwo)
			brick.setColor(Color.red);
		else if(brick instanceof ThreeOne)
			brick.setColor(Color.orange);
		else if(brick instanceof Triangle)
			brick.setColor(new Color(170, 40, 150));
	}

	// 현재 블럭 셋팅
	public void setcurrBrick() {
		// 현재 블럭을 랜덤으로 지정
		currentBrick = view.brickList.get(view.rnd.nextInt(view.brickList.size())); //블럭 자체의 위치만 가지고 있음
		setColor(currentBrick); // 블럭 색 지정

		currentBrick.setDefaultLocation(); // 현재 블럭의 시작 위치 잡기
		
		setNextBrick(); // 다음 블럭 셋팅
	}
	//넥스트 블럭 셋팅
	public void setNextBrick(){
		TetrisView temp;
		
		do{
			temp = view.brickList.get(view.rnd.nextInt(view.brickList.size())); //블럭 자체의 위치만 가지고 있음
		}
		while (temp==currentBrick);		//현재블럭과 같으면 계속 돈다. 현재와 다른게 나올때까지
		// 다음에 나올 블럭 지정
		nextBrick = temp; 
		setColor(nextBrick);
		nextBrick.setNextLocation();	// 다음에 나올 블럭 위치 잡기
	}
	// 새로운 블럭 나오기 셋팅
	public void setNewBrick(){
		currentBrick = nextBrick; // 다음 블럭이 현재 블럭이 된다.
		currentBrick.setDefaultLocation();
		
		setNextBrick(); // 다음 블럭 셋팅
	}
	
	//현재의 블록 백그라운드로 복사 (currentBrick, nextBrick는 위치를 잡아주는 애이고 실제로 패널에 그리는 애는 view.background이다.)
	public void setCopyBlock(){ 
		Block[] tempBlock = currentBrick.getBlock();
		for (int i=0; i<tempBlock.length; i++){
			setBack(tempBlock[i].getX(), tempBlock[i].getY(), currentBrick.getColor());
		}
		point++; // 블럭 1개 쌓으면 1점 추가
		currentBrick.setReadyLocation();	//대기위치로 돌아가기
	}
	
	//백그라운드 블럭 채우기
		public void setBack(int x, int y, Color c){ // x, y는 실제 채워지는 위치
			view.background[x][y].setBackground(c);
			view.background[x][y].setBorder(new SoftBevelBorder(BevelBorder.RAISED));
			grid[x][y] = true;
		}
		
		//백그라운드 블럭 비우기
		public void setEmptyBack(int x, int y){
			view.background[x][y].setBorder(null);
			view.background[x][y].setBackground(Color.white);
			view.background[x][y].setBorder(new LineBorder(Color.gray));
			grid[x][y] = false;
		}
		
	//줄없애기 체크
	public void checkLine(){ // 한 줄씩 공백이 있는지 없는지 확인
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
				point=point+10; // 한줄 없애면 10점 추가
			}
		}
	}
	
	/*★★★★★★★★★★★★★★★★★★ */
	//줄없애고 위에서 한칸씩 내리기
	/*★★★★★★★★★★★★★★★★★★ */
	public void deleteLine(int line){
		JPanel	tempPanel[] = new JPanel[BasicSetting.xCnt];
		
		for (int i=line; i>0; i--){		// i = 줄 = Y
			for (int p=0; p<grid.length; p++){	// p = 열 = X
				if(i==line){	
					tempPanel[p] = view.background[p][i]; //현재줄 템프변수에 저장
					tempPanel[p].setLocation(p*BasicSetting.area,0); // 현재 줄을 맨 위로 옮기기
				}
				//모든줄 한칸씩 내리기
				grid[p][i] = grid[p][i-1]; // 이전 줄을 현재 줄로 옮김
				view.background[p][i] = view.background[p][i-1]; // 이전 줄을 현재 줄로 옮김
				view.background[p][i].setLocation(p*BasicSetting.area, i*BasicSetting.area); // 실제 위치 셋팅
			}
		}
		//없앤줄 맨위로 올리기
		for (int i=0; i<grid.length; i++){
			view.background[i][0] = tempPanel[i]; // x, y ( i, 0) y는 0으로 맨 윗줄로 옮기고 x 반복해서 셋팅
			setEmptyBack(i,0); // 패널에 그려진 그림 지움
		}
	}
	
	//아이템 회전체크 -> 회전
	public void goRotate(){
		Block[] tempBlock = currentBrick.getNextBlock(); // 다음으로 나타날 회전 블럭 
		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX();
			int y = tempBlock[i].getY();
			if(x<0 || x>=BasicSetting.xCnt || y>BasicSetting.yCnt || grid[x][y]){
				// x 축의 왼쪽, 오른쪽 밖으로 나가거나, y축의 아래로 나가거나, 채워져있는 줄을 만나면
				return; // 회전하지 않고 끝냄
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
		String nickName=JOptionPane.showInputDialog("닉네임을 입력해주세요.");
		
		view.recordView(nickName, point);

		view.content.removeAll();
		view.content.add(view.endPanel);
		view.revalidate();
		view.repaint();
		
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
