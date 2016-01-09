package com.tetris.controll;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.tetris.block.LeftTwo;
import com.tetris.block.Line;
import com.tetris.block.OneThree;
import com.tetris.block.Rectangular;
import com.tetris.block.RightTwo;
import com.tetris.block.ThreeOne;
import com.tetris.block.Triangle;
import com.tetris.dao.TetrisDao;
import com.tetris.vo.TetrisVo;

@SuppressWarnings("serial")
public class TetrisView extends JFrame{
	/**
	 * 
	 */
	JPanel[] panel;
	public Block[] block; // 현재 블럭의 좌표
	public Block[][] blockInfo; // 각 회전별 블럭 좌표 정보
	public Block currentXY;
	Color color; // 색
	private int cnt; // 총 판넬 갯수
	private int angle; // 총 회전 갯수
	
	public int currentAngle; // 현재 회전 값
	
	private int width;			//가로
	private int height;			//세로
	protected Container container;		//컨테이너
	protected ArrayList<TetrisView> brickList;
	protected JPanel center, top, next, content, record, endPanel;
	protected ArrayList<Color> colorList;	//컬러리스트
	
	protected JPanel[][] background; 
	protected Random rnd;
	
	public TetrisView(){
		
		setTitle("테트리스");
		
		width = BasicSetting.xCnt * BasicSetting.area; // 실행 화면 가로 길이
		height = BasicSetting.yCnt * BasicSetting.area; // 실행 화면 세로 길이
		
		setBounds(200,200,800,height+100); // 전체 컨테이너 x좌표, y좌표, 가로길이, 세로길이
		
		brickList = new ArrayList<TetrisView>(); // 벽돌 모양 저장하는 리스트
		background = new JPanel[BasicSetting.xCnt][BasicSetting.yCnt]; // 블럭을 넣을 배경 패널
		rnd = new Random(System.currentTimeMillis());

		// 전체 컨테이너
		container = getContentPane(); // 컨테이너 생성
		
		// 게임 종료시 화면 전환 패널
		endPanel= new JPanel();
		endPanel.setLayout(null);
		endPanel.setBounds(0, 0, 800,height+100);
		endPanel.setBackground(Color.white);
		
		// 게임 기록 보여주는 패널
		record=new JPanel();
		record.setBounds(20, 20, width, height);
		record.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// 게임 시 기본 바탕 패널
		content=new JPanel();
		content.setLayout(new BorderLayout());
		
		// 테트리스 실제 실행 패널
		center = new JPanel();
		center.setLayout(null);
		center.setBounds(20,20,width,height);
		center.setBackground(Color.white);
		center.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// 게임 시 기본 바탕 패널
		content.setLayout(null);
		content.setBounds(0, 0, width, height);
		content.setBackground(Color.white);
		
		// 기본 바탕 패널에 테트리스 실제 실행 패널 추가
		content.add(center);
		
		//========== 기본설정 셋팅 끝 ===========

		//블럭 추가하기
		brickList.add(new Rectangular(center));
		brickList.add(new OneThree(center));
		brickList.add(new ThreeOne(center));
		brickList.add(new Line(center));
		brickList.add(new Triangle(center));
		brickList.add(new LeftTwo(center));
		brickList.add(new RightTwo(center));
		
		//색 추가
		colorList = new ArrayList<Color>();
		colorList.add(Color.red);
		colorList.add(Color.blue);
		colorList.add(Color.green);
		colorList.add(Color.orange);
		colorList.add(Color.pink);
		colorList.add(new Color(170,40,150));	//보라

		//다음블럭 셋팅 시작======

		next = new JPanel();
		next.setLayout(null);
		next.setBounds(width+50, 20,BasicSetting.area*4, BasicSetting.area*4);
		next.setBackground(Color.white);
		next.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// 기본 바탕 패널에 다음블럭을 보여주는 패널 추가
		content.add(next);
		
		//다음블럭 셋팅 끝======
		
		// 컨테이너에 기본 바탕 패널 추가
		container.add(content);
		
		//백그라운드 패널 셋팅 시작 ==========
		for (int i=0; i<background.length; i++){ // 세로
			for (int p=0; p<background[i].length; p++){ // 가로
				background[i][p] = new JPanel(); // 한 칸씩 JPanel 생성
				background[i][p].setBounds(i * BasicSetting.area, p * BasicSetting.area, BasicSetting.area, BasicSetting.area); // x좌표, y좌표, 너비, 높이
				background[i][p].setBackground(BasicSetting.bgColor); // 하얀색
				center.add(background[i][p]); // center패널에 한칸씩 JPanel추가함
			}
		}
		//백그라운드 패널 셋팅 시작 ==========

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼 누르면 꺼지도록
		setVisible(true);
	}
	
	public TetrisView(int angle, int cnt) {
		this.cnt = cnt;
		this.angle = angle;
		
		panel=new JPanel[cnt]; // 필요한 칸 수대로 패널 배열 만듬
		block=new Block[cnt]; // 필요한 칸 수대로 블럭 배열 만듬
		blockInfo=new Block[angle][cnt]; // 블럭 회전, 갯수 세팅
		
		currentXY=new Block(0,0); // 현재 값
		
		for(int i=0;i<cnt;i++){ // 필요한 칸수로 블럭 만들어주기
			panel[i]=new JPanel(); // 패널 생성
		}
		
	}
	
	// 기록
	public void recordView(){
		TetrisDao dao=new TetrisDao();
		List<TetrisVo> scoreList=dao.getScoreList();
		
		record.setLayout(new GridLayout(scoreList.size(),3));
		
		for(TetrisVo score:scoreList){
			System.out.println(score.getNickName());
			System.out.println(score.getScore());
			System.out.println(score.getRegDate());
			
			record.add(new Label(score.getNickName()));
			record.add(new Label(score.getScore().toString()));
			record.add(new Label(score.getRegDate()));
		}
		
		endPanel.add(record);
	}

	// 블럭 랜덤 세팅
	public void setDefaultRandom(){
		currentAngle=(int)(Math.random()*angle);
		block=blockInfo[currentAngle];
	}
	
	// 한칸
	public void setBlock(Container c){
		for(int i=0;i<panel.length;i++){
			panel[i].setBackground(color);
			panel[i].setSize(BasicSetting.area,BasicSetting.area);
			panel[i].setLocation(((block[i].getX())*BasicSetting.area)-100, ((block[i].getY())*BasicSetting.area)-100);
			panel[i].setBorder(new SoftBevelBorder(BevelBorder.RAISED)); // 한칸의 border를 3D로 보여줌
			c.add(panel[i]);	//컨테이너에 등록
		}
	}
	
	// 다음위치조정
	public void setNextLocation() {
		for (int i = 0; i < panel.length; i++) {
			int x = block[i].getX() + (BasicSetting.xCnt-3);
			int y = block[i].getY() + 1;
			
			panel[i].setLocation(x * BasicSetting.area, y * BasicSetting.area);
		}
		currentXY.setX(BasicSetting.xCnt-3);
		currentXY.setY(1);
	}

	// 시작위치조정
	public void setDefaultLocation() {
		for (int i = 0; i < panel.length; i++) {
			int x = block[i].getX() + (int) (BasicSetting.xCnt / 2 - 2);
			int y = block[i].getY()+1;
			panel[i].setLocation(x * BasicSetting.area, y * BasicSetting.area);
		}
		currentXY.setX((int) (BasicSetting.xCnt / 2 - 2));
		currentXY.setY(1);
	}
	
	//대기상태 위치조정
		public void setReadyLocation(){
			for (int i=0; i<panel.length; i++){
				panel[i].setLocation(((block[i].getX()) * BasicSetting.area)-100, ((block[i].getY()) * BasicSetting.area)-100);
			}
		}
		//현재위치조정
		public void setCurrentXY(int x, int y){
			currentXY.move(x,y);
		}
		//현재위치반환
		public Block getCurrentXY(){
			return currentXY;
		}
		//현재 포인트 리턴
		public Block[] getBlock(){
			Block[] temp = new Block[cnt];
			for (int i=0; i<block.length; i++){
				int x = block[i].getX() + currentXY.getX();
				int y = block[i].getY() + currentXY.getY();
				temp[i] = new Block(x,y);
			}
			return temp;
		}
		//다음움직일회전각도의 블럭정보 반환
		public Block[] getNextBlock(){
			int nextAngle;
			if(angle==1)	return getBlock();	//회전각도가1개뿐이면 리턴
			else if(angle-1 == currentAngle)	nextAngle=0;	//마지막앵글이면 1번앵글로
			else	nextAngle=currentAngle+1;	//다음각도 셋팅
			
			Block[] temp = new Block[cnt];
			for (int i=0; i<block.length; i++){
				int x = blockInfo[nextAngle][i].getX() + currentXY.getX();
				int y = blockInfo[nextAngle][i].getY() + currentXY.getY();
				temp[i] = new Block(x,y);
			}
			return temp;
		}
		//현재회전각도리턴
		public int getCurrentAngle(){
			return currentAngle;
		}
		//로테이트
		public void moveRotate(){
			if(angle==1)	return;	//각도가1개뿐이면 리턴
			if(currentAngle+1 == angle){	//최고각도면 처음각도로
				block = blockInfo[0];
				currentAngle = 0;
			}else{
				currentAngle++;
				block = blockInfo[currentAngle];
			}
			setMove();
		}
		//현재의 포인트 정보를 판넬에 적용하여 움직여라 
		public void setMove(){
			for (int i=0; i<panel.length; i++){
				//현재블록의 x,y값에 현재x,y포인트값을 더한값을 각BasicSetting.area값과 곱한다.
				int x = block[i].getX() + currentXY.getX();
				int y = block[i].getY() + currentXY.getY();;
				panel[i].setLocation(x * BasicSetting.area, y * BasicSetting.area);
			}
		}
		//아래로 한칸 움직임
		public void moveDown(){
			currentXY.move(0,1);		
			setMove();
		}
		//오른쪽으로 한칸 움직임
		public void moveRight(){
			currentXY.move(1,0);
			setMove();
		}
		//왼쪽으로 한칸 움직임
		public void moveLeft(){
			currentXY.move(-1,0);		
			setMove();
		}
		//현재 색 리턴
		public Color getColor(){
			return color;
		}
		//현재 색 셋팅
		public void setColor(Color c){
			color = c;
			for (int i=0; i<panel.length; i++){
				panel[i].setBackground(color);
			}
		}
}
