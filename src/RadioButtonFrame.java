import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.Random;

/**
 * 该类为带原形按钮的出题面板容器，每次出一道题，通过监听提交按钮来重复调用构造函数来重复出题；trans_para方法
 * 作为特定的“接口”，将参数传进该类，并刷新，通过监听原形按钮的状态改变以及是否选中正确答案来改变isRight的值，
 * 再监听提交按钮，提交时判定isRight的值来计算得分，每对一次，静态成员变量s加一。
 */
public class RadioButtonFrame extends JFrame{
	static Random r=new Random();
	private JTextField issue;
	private JRadioButton selA,selB,selC,selD;
	private JPanel p1;
	private JButton submit;
	private boolean isRight;
	private int right_order;
	double opt[]=new double[4];
	ButtonGroup group;
	public static int s;
	public static int num_name=0;
	public static Java_UI ja;
	public static String str;
	public static String user_name;
	public static File txt;

//相当于外部接口，并将参数值传进类中，作为类的成员变量，并刷新成员变量值
	public static void trans_para(Java_UI j,String st,File ques_txt) {
		ja=j;
		str=st;
		num_name=0;
		s=0;
		txt=ques_txt;
	}
	
	public static String use_ques() {
		String que="";
		if(str.equals("小学")) {
			que = Questioning.Primary();
		}
		else if(str.equals("初中")) {
			que = Questioning.Junior();
		}
		else if(str.equals("高中")) {
			que = Questioning.Senior();
		}
		//解题，调用解题类中的解题函数		
		double result=Calcu.calcu(que);
		//对于根号下负数的情况，即结果为非数的情况，重新出题
		try {
			
		while(Double.isNaN(result)||result==0.0) {
			if(str.equals("小学")) {
				
				que = Questioning.Primary();
			}
			else if(str.equals("初中")) {
				que = Questioning.Junior();
			}
			else if(str.equals("高中")) {
				que = Questioning.Senior();
			}
			result=Calcu.calcu(que);
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return que;
	}
	
	//构造函数，一个页面构造器，包括圆形按钮组
	public RadioButtonFrame() throws IOException {
		super("questioning");
		setLayout(new GridLayout(1,1));
		isRight = false;
		p1 = new JPanel();
//		p2 = new JPanel();
		add(p1);
//		add(p2);
		p1.setLayout(new GridLayout(6,1));
//		p2.setLayout(new GridLayout(1,1));
		issue = new JTextField();
		issue.setColumns(40);
		issue.setFont(new Font("宋体",Font.BOLD,20));
		num_name++;
		
		
		//出题，调用出题类中的相应出题函数
		String que = use_ques();
		double result=Calcu.calcu(que);
		
		//追加题号		
		String question=String.valueOf(num_name)+": "+que;
		
		//读入文件的内容为
		String txt_in=question+" =="+String.valueOf(result);
		
		//增加查重功能
		File parent_file=new File(Java_UI.user_path);
		File [] list=parent_file.listFiles();

		for(int i=0;i<list.length-1;i++) {
			BufferedReader bf=new BufferedReader(new FileReader(list[i]));
			String bf_str=bf.readLine();
			if(bf_str!=null) {
				for(int j=0;j<bf_str.length();j++) {
					if(bf_str.charAt(j)==':') {
						bf_str=bf_str.substring(j+2, bf_str.length());
						break;
					}
				}
				if(bf_str.equals(que+" =="+String.valueOf(result))) {//如果重复了，题目就重新生成，并且重新查重
					que=use_ques();
					result=Calcu.calcu(que);
					i=0;
				}
			}
			bf.close();
		}
		question=String.valueOf(num_name)+": "+que;
		txt_in=question+" =="+String.valueOf(result);
		txt_in=txt_in+"\r\n\r\n";
		FileWriter fw=new FileWriter(txt,true);
		fw.write(txt_in);
		fw.close();
		
		issue.setText(question);
		issue.setEditable(false);
		p1.add(issue);
		
		//干扰选项，随机得到答案题号，并将正确答案与之对应		
		right_order = r.nextInt(4);
		opt[right_order] = result;
		for(int i=0;i<4;i++) {
			if(i!=right_order) {
				opt[i] = r.nextInt(100)/10*result+result/2;
				opt[i]=(double)Math.round(opt[i]*100)/100;
				for(int j=0;j<i;j++) {
					if(opt[j]==opt[i]) {
						opt[i] = r.nextInt(100)/10*result+result/2;
						opt[i]=(double)Math.round(opt[i]*100)/100;
						j=0;
					}
				}
		//		opt[i]=(double)Math.round(opt[i]*100)/100;
			}	
		}
		
		//初始化原形按钮，状态未选；		
		selA = new JRadioButton("A、"+String.valueOf(opt[0]),false);
		selB = new JRadioButton("B、"+String.valueOf(opt[1]),false);
		selC = new JRadioButton("C、"+String.valueOf(opt[2]),false);
		selD = new JRadioButton("D、"+String.valueOf(opt[3]),false);
		
		p1.add(selA);
		p1.add(selB);
		p1.add(selC);
		p1.add(selD);
		
		//按钮组		
		group = new ButtonGroup();
		group.add(selA);
		group.add(selB);
		group.add(selC);
		group.add(selD);
		
		submit=new JButton("提交");
		p1.add(submit);
		
		//圆形按钮（选择按钮）监听器		
		JRadioHandler rHand = new JRadioHandler();
		//提交按钮监听器		
		ActionHandler aHand = new ActionHandler();
		
		selA.addItemListener(rHand);
		selB.addItemListener(rHand);
		selC.addItemListener(rHand);
		selD.addItemListener(rHand);
		
		submit.addActionListener(aHand);
//		refresh.addActionListener(aHand);
		setVisible(true);
		setSize(700,300);
		Java_UI.showInMiddle(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	//实现选择按钮监听器的接口，重写itemStateChanged方法	
	private class JRadioHandler implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			if(selA.isSelected()&&right_order==0) {
				isRight=true;
			}
			else if(selB.isSelected()&&right_order==1) {
				isRight=true;
			}
			else if(selC.isSelected()&&right_order==2) {
				isRight=true;
			}
			else if(selD.isSelected()&&right_order==3) {
				isRight=true;
			}
			else {
				isRight=false;
			}
		}
	}
	
	
	//实现提交按钮的监听器接口，重写actionPerformed方法	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==submit) {
//				System.out.print("结果："+group.isSelected(opt[right_order].getModel()));
				if(isRight) {
					s++;
//					System.out.println("true");
			
				}
				dispose();
				
				//出题数量不够时，重复调用构造函数			
				if(num_name<ja.question_num) {
					try {
						new RadioButtonFrame();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
				//出完所有题，给出得分，并选择返回或继续；				
					JFrame jf = new JFrame("show grades");
					jf.setLayout(new FlowLayout());
					JTextField show = new JTextField();
					int grader = (int) (s/(double)num_name*100);
					show.setText("your point is: "+String.valueOf(grader));
					show.setColumns(30);
					show.setFont(new Font("宋体",Font.BOLD,20));
					show.setEditable(false);
					jf.add(show);
					
					JButton button = new JButton("继续");
					jf.add(button);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ja.sel_Face();//把用户名传进去
							jf.dispose();
						}
					});
					
					JButton button_exit = new JButton("退出");
					jf.add(button_exit);
					button_exit.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							Java_UI j=new Java_UI();
							j.LoginFrame();
							jf.dispose();
						}
					});
					jf.setSize(400, 120);
					Java_UI.showInMiddle(jf);
					Java_UI.setBg(jf);
					jf.setVisible(true);
					jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					JPanel contentPane = new JPanel();
					contentPane.setOpaque(false);
					contentPane.setBorder(new EmptyBorder(5,5,5,5));
					setContentPane(contentPane);
					contentPane.setLayout(null);
				}
			}
		}
	}
}
