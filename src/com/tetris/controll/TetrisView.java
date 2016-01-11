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
import com.tetris.dao.TetrisDao;
import com.tetris.vo.TetrisVo;

@SuppressWarnings("serial")
public class TetrisView extends JFrame {
	/**
	 * 
	 */
	JPanel[] panel, nextPanel;
	public Block[] block; // 현재 블럭의 좌표
	public Block[][] blockInfo; // 각 회전별 블럭 좌표 정보
	public Block currentXY;
	private int cnt; // 총 판넬 갯수
	private int angle; // 총 회전 갯수
	Color color;
	public int currentAngle; // 현재 회전 값

	private int width; // 가로
	private int height; // 세로
	protected Container container; // 컨테이너
	protected ArrayList<TetrisView> brickList;
	protected JPanel center, top, next, content, record, endPanel;

	protected JPanel[][] background;
	protected Random rnd;

	public TetrisView() {

		setTitle("테트리스");

		width = BasicSetting.xCnt * BasicSetting.area; // 실행 화면 가로 길이
		height = BasicSetting.yCnt * BasicSetting.area; // 실행 화면 세로 길이

		setBounds(200, 200, 800, height + 100); // 전체 컨테이너 x좌표, y좌표, 가로길이, 세로길이

		brickList = new ArrayList<TetrisView>(); // 벽돌 모양 저장하는 리스트
		background = new JPanel[BasicSetting.xCnt][BasicSetting.yCnt]; // 블럭을 넣을
																		// 배경 패널
		rnd = new Random(System.currentTimeMillis());

		/* 실행 창 셋팅 */
		// 전체 컨테이너
		container = getContentPane(); // 컨테이너 생성

		// 게임 종료시 화면 전환 패널
		endPanel = new JPanel();
		endPanel.setLayout(null);
		endPanel.setBounds(0, 0, 800, height + 100);
		endPanel.setBackground(Color.white);

		// 게임 기록 보여주는 패널
		record = new JPanel();
		record.setBounds(20, 20, width, height);
		record.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// 게임 시 기본 바탕 패널
		content = new JPanel();
		content.setLayout(new BorderLayout());

		// 테트리스 실제 실행 패널
		center = new JPanel();
		center.setLayout(null);
		center.setBounds(20, 20, width, height);
		center.setBackground(Color.white);
		center.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// 게임 시 기본 바탕 패널
		content.setLayout(null);
		content.setBounds(0, 0, width, height);
		content.setBackground(Color.white);

		// 기본 바탕 패널에 테트리스 실제 실행 패널 추가
		content.add(center);
		// ========== 기본설정 셋팅 끝 ===========

		// 다음블럭 셋팅 시작======
		next = new JPanel();
		next.setLayout(null);
		next.setBounds(width + 50, 20, BasicSetting.area * 4, BasicSetting.area * 4);
		next.setBackground(Color.white);
		next.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// 기본 바탕 패널에 다음블럭을 보여주는 패널 추가
		content.add(next);

		// 다음블럭 셋팅 끝======

		// 컨테이너에 기본 바탕 패널 추가
		container.add(content);

		/*			*/

		// 블럭 추가하기
		brickList.add(new Rectangular(center));
		brickList.add(new OneThree(center));
		brickList.add(new ThreeOne(center));
		brickList.add(new Line(center));
		brickList.add(new Triangle(center));
		brickList.add(new LeftTwo(center));
		brickList.add(new RightTwo(center));

		// 백그라운드 패널 셋팅 시작 ==========
		for (int i = 0; i < background.length; i++) { // 세로
			for (int p = 0; p < background[i].length; p++) { // 가로
				background[i][p] = new JPanel(); // 한 칸씩 JPanel 생성
				background[i][p].setBounds(i * BasicSetting.area, p * BasicSetting.area, BasicSetting.area,
						BasicSetting.area); // x좌표, y좌표, 너비, 높이
				background[i][p].setBackground(Color.white); // 하얀색
				background[i][p].setBorder(new LineBorder(Color.gray));
				center.add(background[i][p]); // center패널에 한칸씩 JPanel추가함
			}
		}
		// 백그라운드 패널 셋팅 시작 ==========

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼 누르면 꺼지도록
		setVisible(true);
	}

	public TetrisView(int angle, int cnt) {
		// 각 블럭 좌표 세팅할 때 불려져 셋팅되는 생성자
		this.cnt = cnt;
		this.angle = angle;

		panel = new JPanel[cnt]; // 필요한 칸 수대로 패널 배열 만듬
		block = new Block[cnt]; // 필요한 칸 수대로 블럭 배열 만듬
		blockInfo = new Block[angle][cnt]; // 블럭 회전, 갯수 세팅

		currentXY = new Block(0, 0); // 현재 좌표 // 기본값 0,0

		for (int i = 0; i < cnt; i++) { // 필요한 칸수로 블럭 만들어주기
			panel[i] = new JPanel(); // 패널 생성
		}

	}

	// 기록
	public void recordView(String nickName, Long point) {
		TetrisDao dao = new TetrisDao();
		dao.insertScore(nickName, point);

		List<TetrisVo> scoreList = dao.getScoreList();

		record.setLayout(new GridLayout(scoreList.size(), 3));

		for (TetrisVo score : scoreList) {
			record.add(new Label(score.getNickName()));
			record.add(new Label(score.getScore().toString()));
			record.add(new Label(score.getRegDate()));
		}

		endPanel.add(record);
	}

	// 블럭 랜덤 세팅
	public void setDefaultRandom() {
		// 각 블럭마다 회전블럭이 있는데, 그 회전블럭 중 어느 모양으로 처음에 내보낼 지 랜덤으로 정하는 메소드
		currentAngle = (int) (Math.random() * angle); // 모든 회전 블럭 랜덤 셋팅
		block = blockInfo[currentAngle];
	}

	// 한 블럭 셋팅
	public void setBlock(Container c) {
		// 각 블럭에서 호출하여 center 패널에 세팅하도록 하는 메소드
		for (int i = 0; i < panel.length; i++) {
			panel[i].setBackground(color);
			panel[i].setSize(BasicSetting.area, BasicSetting.area);

			/*---------------- (-100)?? -----------------*/
			panel[i].setLocation(((block[i].getX()) * BasicSetting.area) - 100, ((block[i].getY()) * BasicSetting.area) - 100);
			panel[i].setBorder(new SoftBevelBorder(BevelBorder.RAISED)); // 한칸의
																			// border를
																			// 3D로
																			// 보여줌
			c.add(panel[i]); // center에 등록
		}
	}

	// 대기상태 위치조정
		public void setReadyLocation() {
			for (int i = 0; i < panel.length; i++) {
				panel[i].setLocation(((block[i].getX()) * BasicSetting.area) - 100,((block[i].getY()) * BasicSetting.area) - 100);
			}
		}
		
	// 시작위치조정 ( 패널에서 실제로 시작하는 위치)
	public void setDefaultLocation() {
		for (int i = 0; i < panel.length; i++) {
			int x = block[i].getX() + (int) (BasicSetting.xCnt / 2 - 2); // 중심에서 2칸 앞에 위치하도록
			int y = block[i].getY();
			panel[i].setLocation(x * BasicSetting.area, y * BasicSetting.area);
		}
		currentXY.setX((int) (BasicSetting.xCnt / 2 - 2));
		currentXY.setY(0);
	}

	// 다음위치조정
	public void setNextLocation() {
		for (int i = 0; i < panel.length; i++) {
			// 블럭을 이루는 각 칸마다 위치를 설정해줌(다음 위치 블럭을 보여줄 곳은 오른쪽에서 -3 된 곳에서부터 시작이므로)
			int x = block[i].getX() + (BasicSetting.xCnt - 3);
			int y = block[i].getY() + 1;
			// 실제 패널에 올려놓을 위치
			panel[i].setLocation(x * BasicSetting.area, y * BasicSetting.area);
		}
		currentXY.setX(BasicSetting.xCnt - 3);
		currentXY.setY(1);
	}
	

	// 현재위치조정
	public void setCurrentXY(int x, int y) {
		currentXY.move(x, y);
	}

	// 현재위치반환
	public Block getCurrentXY() {
		return currentXY;
	}

	// 현재 블럭 리턴
	public Block[] getBlock() { // 실제 블럭 이동 좌표 / 위치 움직이는 것처럼 보이도록 하는 것이므로 이동할 때마다 불려짐
		Block[] temp = new Block[cnt];
		for (int i = 0; i < block.length; i++) {
			
			int x = block[i].getX() + currentXY.getX();
			int y = block[i].getY() + currentXY.getY();
			
			temp[i] = new Block(x, y);
		}
		return temp;
	}

	// 다음 움직일 회전각도의 블럭정보 반환
	public Block[] getNextBlock() {
		int nextAngle;
		if (angle == 1)
			return getBlock(); // 회전각도가1개뿐이면 리턴
		else if (angle - 1 == currentAngle)
			nextAngle = 0; // 마지막앵글이면 1번앵글로
		else
			nextAngle = currentAngle + 1; // 다음각도 셋팅

		Block[] temp = new Block[cnt];
		for (int i = 0; i < block.length; i++) {
			int x = blockInfo[nextAngle][i].getX() + currentXY.getX();
			int y = blockInfo[nextAngle][i].getY() + currentXY.getY();
			temp[i] = new Block(x, y);
		}
		return temp;
	}

	// 현재회전각도리턴
	public int getCurrentAngle() {
		return currentAngle;
	}

	// 로테이트
	public void moveRotate() {
		if (angle == 1)
			return; // 각도가1개뿐이면 리턴
		if (angle-1 == currentAngle) { // 최고각도면 처음각도로
			block = blockInfo[0];
			currentAngle = 0;
		} else {
			currentAngle++;
			block = blockInfo[currentAngle];
		}
		setMove();
	}

	public void setMove() { // 회전 후 블럭을 세팅시키기 위한 용도 
		for (int i = 0; i < panel.length; i++) {
			// 블럭의 위치와 현재 좌표를 더해 실제 패널에 적용할 위치를 구함
			int x = block[i].getX() + currentXY.getX();
			int y = block[i].getY() + currentXY.getY();
			panel[i].setLocation(x * BasicSetting.area, y * BasicSetting.area);
		}
	}

	// 아래로 한칸 움직임
	public void moveDown() {
		currentXY.move(0, 1);
		setMove();
	}

	// 오른쪽으로 한칸 움직임
	public void moveRight() {
		currentXY.move(1, 0);
		setMove();
	}

	// 왼쪽으로 한칸 움직임
	public void moveLeft() {
		currentXY.move(-1, 0);
		setMove();
	}

	// 현재 색 리턴
	public Color getColor() {
		return color;
	}

	// 현재 색 셋팅
	public void setColor(Color c) {
		color = c;
		for (int i = 0; i < panel.length; i++) {
			panel[i].setBackground(color);
		}
	}
}
