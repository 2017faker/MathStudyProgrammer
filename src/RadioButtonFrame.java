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
 * ����Ϊ��ԭ�ΰ�ť�ĳ������������ÿ�γ�һ���⣬ͨ�������ύ��ť���ظ����ù��캯�����ظ����⣻trans_para����
 * ��Ϊ�ض��ġ��ӿڡ����������������࣬��ˢ�£�ͨ������ԭ�ΰ�ť��״̬�ı��Լ��Ƿ�ѡ����ȷ�����ı�isRight��ֵ��
 * �ټ����ύ��ť���ύʱ�ж�isRight��ֵ������÷֣�ÿ��һ�Σ���̬��Ա����s��һ��
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

//�൱���ⲿ�ӿڣ���������ֵ�������У���Ϊ��ĳ�Ա��������ˢ�³�Ա����ֵ
	public static void trans_para(Java_UI j,String st,File ques_txt) {
		ja=j;
		str=st;
		num_name=0;
		s=0;
		txt=ques_txt;
	}
	
	public static String use_ques() {
		String que="";
		if(str.equals("Сѧ")) {
			que = Questioning.Primary();
		}
		else if(str.equals("����")) {
			que = Questioning.Junior();
		}
		else if(str.equals("����")) {
			que = Questioning.Senior();
		}
		//���⣬���ý������еĽ��⺯��		
		double result=Calcu.calcu(que);
		//���ڸ����¸���������������Ϊ��������������³���
		try {
			
		while(Double.isNaN(result)||result==0.0) {
			if(str.equals("Сѧ")) {
				
				que = Questioning.Primary();
			}
			else if(str.equals("����")) {
				que = Questioning.Junior();
			}
			else if(str.equals("����")) {
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
	
	//���캯����һ��ҳ�湹����������Բ�ΰ�ť��
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
		issue.setFont(new Font("����",Font.BOLD,20));
		num_name++;
		
		
		//���⣬���ó������е���Ӧ���⺯��
		String que = use_ques();
		double result=Calcu.calcu(que);
		
		//׷�����		
		String question=String.valueOf(num_name)+": "+que;
		
		//�����ļ�������Ϊ
		String txt_in=question+" =="+String.valueOf(result);
		
		//���Ӳ��ع���
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
				if(bf_str.equals(que+" =="+String.valueOf(result))) {//����ظ��ˣ���Ŀ���������ɣ��������²���
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
		
		//����ѡ�����õ�����ţ�������ȷ����֮��Ӧ		
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
		
		//��ʼ��ԭ�ΰ�ť��״̬δѡ��		
		selA = new JRadioButton("A��"+String.valueOf(opt[0]),false);
		selB = new JRadioButton("B��"+String.valueOf(opt[1]),false);
		selC = new JRadioButton("C��"+String.valueOf(opt[2]),false);
		selD = new JRadioButton("D��"+String.valueOf(opt[3]),false);
		
		p1.add(selA);
		p1.add(selB);
		p1.add(selC);
		p1.add(selD);
		
		//��ť��		
		group = new ButtonGroup();
		group.add(selA);
		group.add(selB);
		group.add(selC);
		group.add(selD);
		
		submit=new JButton("�ύ");
		p1.add(submit);
		
		//Բ�ΰ�ť��ѡ��ť��������		
		JRadioHandler rHand = new JRadioHandler();
		//�ύ��ť������		
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
	
	//ʵ��ѡ��ť�������Ľӿڣ���дitemStateChanged����	
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
	
	
	//ʵ���ύ��ť�ļ������ӿڣ���дactionPerformed����	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==submit) {
//				System.out.print("�����"+group.isSelected(opt[right_order].getModel()));
				if(isRight) {
					s++;
//					System.out.println("true");
			
				}
				dispose();
				
				//������������ʱ���ظ����ù��캯��			
				if(num_name<ja.question_num) {
					try {
						new RadioButtonFrame();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
				//���������⣬�����÷֣���ѡ�񷵻ػ������				
					JFrame jf = new JFrame("show grades");
					jf.setLayout(new FlowLayout());
					JTextField show = new JTextField();
					int grader = (int) (s/(double)num_name*100);
					show.setText("your point is: "+String.valueOf(grader));
					show.setColumns(30);
					show.setFont(new Font("����",Font.BOLD,20));
					show.setEditable(false);
					jf.add(show);
					
					JButton button = new JButton("����");
					jf.add(button);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ja.sel_Face();//���û�������ȥ
							jf.dispose();
						}
					});
					
					JButton button_exit = new JButton("�˳�");
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
