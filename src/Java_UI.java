import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class Java_UI extends JFrame{
	int question_num;
	public static int All_score;
	public static String path=System.getProperty("user.dir");
	public static File txt=new File(path+File.separator+"用户信息.txt");
//	public static File user_file;
	public static String user_path;
	
	//更新部分变量值：题目数量，得分；再答题完成后，想继续答题时会调用该构造函数
	public Java_UI() {
		question_num = 0;
		All_score = 0;
	}
	//系统提示构造器，用于提示注册成功或失败
	//参数：警告语
	public void frame(String warning) {
		JFrame newframe=new JFrame("系统提示");
		newframe.setLayout(new BorderLayout());
		JLabel warn=new JLabel(warning);
		warn.setFont(new Font("宋体",Font.BOLD,20));
		newframe.add(warn,BorderLayout.CENTER);
		
		JButton shutdown = new JButton("关闭");
		newframe.add(shutdown,BorderLayout.SOUTH);
		shutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newframe.dispose();
			}
		});
		
		newframe.setSize(300, 200);
		showInMiddle(newframe);
		setBg(newframe);
		newframe.setVisible(true);
		
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	//发送短信
	//参数：电话号码和验证码
	public void message(String pho,int num) throws ClientException {
		
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
		//替换成你的AK
		final String accessKeyId = "替换成你的AK";//你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = "替换成你的AK";//你的accessKeySecret，参考本文档步骤2
		//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
		 request.setPhoneNumbers(pho);
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName("小初高题库");
		 //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
		 request.setTemplateCode("SMS_146804393");
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam("{\"name\":\"Tom\", \"code\":\""+String.valueOf(num)+"\"}");
		 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");

		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 request.setOutId("yourOutId");

		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
		//请求成功
		}
	}
	
	//注册界面
	public void otherFace() {
		
		JFrame regist= new JFrame("注册");
		regist.setLayout(new FlowLayout());
		
		JLabel phone=new JLabel("手机号码:    ");
		regist.add(phone);
		
		JTextField pho=new JTextField();
		pho.setColumns(30);
		pho.setFont(new Font("宋体",Font.BOLD,20));
		regist.add(pho);
		
		JLabel passwLabel=new JLabel("请输入密码:");
		regist.add(passwLabel);
		
		JPasswordField keyField = new JPasswordField();
		keyField.setColumns(30);
		keyField.setFont(new Font("宋体",Font.BOLD,20));
		keyField.setEchoChar('*');
		regist.add(keyField);
		
		
		JLabel passwLabelagain=new JLabel("请再次输入密码:");
		regist.add(passwLabelagain);
		
		JPasswordField keyFieldagain = new JPasswordField();
		keyFieldagain.setColumns(28);
		keyFieldagain.setFont(new Font("宋体",Font.BOLD,20));
		keyFieldagain.setEchoChar('*');
		regist.add(keyFieldagain);
		
		JButton regist_button=new JButton("点击此处获取验证码");//监听到这个事件后 生成验证码并且发送
		regist.add(regist_button);

		
		JTextField current_regist=new JTextField();
		current_regist.setColumns(30);
		current_regist.setFont(new Font("宋体",Font.BOLD,20));
		regist.add(current_regist);
		
		JLabel curr_regist=new JLabel("请在上框输入验证码");
		regist.add(curr_regist);
			
		regist_button.addActionListener(new ActionListener(){//监听手机号码
			public void actionPerformed(ActionEvent e) {
				char num1[]=keyField.getPassword();
				String pass1 = String.valueOf(num1);
				char num2[]=keyFieldagain.getPassword();
				String pass2 = String.valueOf(num2);
				try {
				if(pass1.equals(pass2)&&!checkRepeat(txt,pho.getText(),String.valueOf(keyField.getPassword()))) {
					Code.produce_Code();
					String pho_num=pho.getText();
//					System.out.println(Code.getCode());
				
					try {
						message(pho_num,Code.getCode());
					} catch (ClientException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				else if(!pass1.equals(pass2)&&!checkRepeat(txt,pho.getText(),String.valueOf(keyField.getPassword()))){
					String warn="两次密码不一样";
					frame(warn);
				}
				else {
					String warn="您的手机号码已注册";
					frame(warn);	
				}
				}
				catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		});
		
		JButton over=new JButton("完成");
		regist.add(over);
		
		over.addActionListener(new ActionListener() {//判断注册是否成功
			public void actionPerformed(ActionEvent e) {
				if(String.valueOf(Code.getCode()).equals(current_regist.getText())) {
					String warn="注册成功";
					writeNamePass(txt,pho.getText(),String.valueOf(keyField.getPassword()));
					File user_file=new File(path+File.separator+pho.getText());
					if(!user_file.exists()) {//创建用户文件夹
							user_file.mkdirs();				
					}
					try {
						user_path=user_file.getCanonicalPath();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					frame(warn);
					regist.dispose();
				}
				else {
					String warn="注册失败";
					frame(warn);
					regist.dispose();

				}
				
			}
			
		});
		regist.setSize(450, 250);
		showInMiddle(regist);
		setBg(regist);
		regist.setVisible(true);
	//	regist.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	//查重，用于检查用户信息文本中是否存在某一用户
	//参数：用户信息文本，待检查手机号码及密码
	//返回值：真或假
	public static boolean checkRepeat(File t,String phone,String pass){
		BufferedReader br = null;
		try{
			br=new BufferedReader(new FileReader(t));
			String s = null;
			while ((s = br.readLine()) != null) {
				// 使用readLine方法，一次读一行
				if(s.equals(phone+" "+pass)) {
					br.close();
					return true;
				}
			}
			br.close();	
			return false;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//写用户信息文本，注册成功时添加新用户信息
	//参数：用户信息文本，待检查手机号码及密码
	public static void writeNamePass(File t,String phone,String pass) {
		FileWriter fw=null;
		try {
			fw=new FileWriter(t,true);
			fw.write(String.valueOf(phone+" "+pass));
			fw.write("\r\n");
			fw.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//设置界面背景图片
	//参数：待设置的页面构造器
	public static void setBg(JFrame jf){ 
		((JPanel)jf.getContentPane()).setOpaque(false); 
		ImageIcon img = new ImageIcon
		(path+File.separator+"简洁图片.jpg"); 
		JLabel background = new JLabel(img);
		jf.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE)); 
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight()); 
	}

	//控制界面的位置，使其显示在屏幕中央
	//参数：待设置的页面构造器
	public static void showInMiddle(JFrame j) {
		int windowWidth = j.getWidth(); //获得窗口宽
		int windowHeight = j.getHeight(); //获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包
		Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸
		int screenWidth = screenSize.width; //获取屏幕的宽
		int screenHeight = screenSize.height; //获取屏幕的高
		j.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);
	}
	
	//登录界面，选择登录或注册
	public void LoginFrame() {
		
		JFrame keyFrame = new JFrame("猿媛答题");
		keyFrame.setLayout(new FlowLayout());	
		
		JLabel account = new JLabel("登录名:");
		keyFrame.add(account);
		
		JTextField name = new JTextField();
		name.setColumns(29);
		name.setFont(new Font("宋体",Font.BOLD,20));
		keyFrame.add(name);	
		
		JLabel pass = new JLabel("密码:  ");
		keyFrame.add(pass);
		
		JPasswordField keyField = new JPasswordField();
		keyField.setColumns(29);
		keyField.setFont(new Font("宋体",Font.BOLD,20));
		keyField.setEchoChar('*');
		keyFrame.add(keyField);
		
		keyField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					char key[] = keyField.getPassword();
					String acc_name = name.getText();
					String keyStr = String.valueOf(key);
					if(keyStr.equals("123")&&acc_name.equals("张三1")) {
						user_path=path+File.separator+acc_name;
						sel_Face();
						keyFrame.setVisible(false);
					}
					else {
						JOptionPane.showMessageDialog(null,"输入密码不正确");
					}
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
			
		JButton sure = new JButton("登录");
		keyFrame.add(sure);
		
		sure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char key[] = keyField.getPassword();
				String acc_name = name.getText();
				String keyStr = String.valueOf(key);
				if(checkRepeat(txt,acc_name,String.valueOf(keyStr))) {
					//登陆后把用户名作为参数传进去，用来知道在那个目录下创建文档存储题目
					user_path=path+File.separator+acc_name;
					sel_Face();
					keyFrame.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null,"输入密码不正确");
				}
			}
		});
		
		JButton register = new JButton("注册");
		keyFrame.add(register);
		
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				otherFace();
			}
		});
		
		keyFrame.setSize(400,150);
		showInMiddle(keyFrame);
		setBg(keyFrame);
		keyFrame.setVisible(true);
		keyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	//选择出题难度
	public void sel_Face() {
		final JFrame sel_frame = new JFrame("SelectionFace");
		sel_frame.setLayout(new FlowLayout());
		JButton button_p = new JButton("小学");
		sel_frame.add(button_p);
		button_p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				question_Num("小学");
				sel_frame.dispose();
			}
		});
		
		JButton button_m = new JButton("初中");
		sel_frame.add(button_m);
		button_m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				question_Num("初中");
				sel_frame.dispose();
			}
		});
		
		JButton button_h = new JButton("高中");
		sel_frame.add(button_h);
		button_h.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				question_Num("高中");
				sel_frame.dispose();
			}
		});
		
		sel_frame.setSize(400, 85);
		showInMiddle(sel_frame);
		setBg(sel_frame);
		sel_frame.setVisible(true);
		sel_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	//选择出题数量，并在此处为用户创建记录题目及答案的文本
	public void question_Num(String str)  {
		JFrame q_frame = new JFrame("question number");
		q_frame.setLayout(new FlowLayout());
		
		JTextField tishi = new JTextField();
		tishi.setColumns(30);
		tishi.setFont(new Font("宋体",Font.BOLD,20));
		tishi.setText("请输入出题数量：");
		tishi.setEditable(false);
		q_frame.add(tishi);
		
		JTextField str_num_Field = new JTextField();
		str_num_Field.setColumns(30);
		str_num_Field.setFont(new Font("宋体",Font.BOLD,20));
		q_frame.add(str_num_Field);
					
		JButton sure = new JButton("确定");
		Dimension preferredSize = new Dimension(150,30);
		sure.setPreferredSize(preferredSize );
		q_frame.add(sure);
			
		sure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				SimpleDateFormat set_time=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				Date now=new Date();
				String time=set_time.format(now);
				File ques_txt=new File(user_path+File.separator+time+".txt");
				
				if(!ques_txt.exists()) {
					try {
						ques_txt.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				String str_num = str_num_Field.getText();
				Java_UI ju = new Java_UI();
				ju.question_num = Integer.parseInt(str_num);
				try {
					output_Face(ju,str,ques_txt);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				q_frame.dispose();
			}
		});
		
		q_frame.setSize(400, 150);
		showInMiddle(q_frame);
		setBg(q_frame);
		q_frame.setVisible(true);
		q_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	//出题界面，调用了RadioButtonFrame类
	//参数：本类对象，主要是题目数量和得分，出题难度，用户题目文本
	public void output_Face(Java_UI ju,String str,File ques_txt) throws IOException {
		RadioButtonFrame.trans_para(ju, str,ques_txt);				
		new RadioButtonFrame();
	}

	public static void main(String args[]) throws IOException {
		if(!txt.exists()) {
			txt.createNewFile();
		}
		
		Java_UI ju = new Java_UI();
		ju.LoginFrame();
	
	}
	
}
