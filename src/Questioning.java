import java.util.Date;
import java.util.Random;

public class Questioning {
	static String operate[]= {"+","-","*","/","²","√","sin","cos","tan"};//表示操作符号
	static String name[]= {"张三1","张三2","张三3","李四1","李四2","李四3","王五1","王五2","王五3"};
	static Random r=new Random( new Date().getMinutes());//随机数种子跟着当前时间
	
	public static void swap(int array[],int i,int j) {
		int k=array[i];
		array[i]=array[j];
		array[j]=k;
	}
	public static void sort(int array[],int length) {
		for(int i=0;i<length;i++) {
			for(int j=i+1;j<length;j++) {
				if(array[i]>array[j]) {
					swap(array,i,j);
				}
			}
		}
		
		
	}
	public static int bracket(int ran_left[],int ran_right[],int operate_count) {
		boolean judge=r.nextBoolean();//括号的有无
		if(operate_count==2) {//如果只有2个操作数 就不用括号s
			judge=false;
		}
		if(judge) {//如果有括号，再考虑括号的多少
			int bracket_num=r.nextInt(operate_count-2)+1;//括号的个数随机
			for(int k=0;k<bracket_num;k++) {
				ran_left[k]=r.nextInt(operate_count-1);//左括号的位置不能在最后面
				for(int z=0;z<k;z++) {//左括号不能和右括号组成(1)
					if(ran_right[z]==ran_left[k]) {
						ran_left[k]--;
						z=0;
					}
				}
				
				ran_right[k]=ran_left[k]+r.nextInt(operate_count-ran_left[k]-1)+1;//右括号的位置至少在左的后面一位
				for(int z=0;z<k;z++) {//括号内不能只有一个操作数,不然就会出现(3)这样的事情,所以对于每一个右括号，判断是否有一个左括号使得这个括号内只有一个操作数,如果有，右括号右移
					if(ran_left[z]==ran_right[k]) {
						ran_right[k]++;
						z=0;
					}
				}
				
				//考虑到最开头的最结尾不能用括号括起来,这一对括号重新随机
				if(ran_left[k]==0 && ran_right[k]==operate_count-1) {
					ran_right[k]=0;
					k--;
					continue;
				}
				
				//考虑到括号冗余 也就是((   ))的情况
				for(int z=0;z<k;z++) {
					if(ran_left[z]==ran_left[k]) {
						if(ran_right[z]==ran_right[k]) {
							ran_left[k]=0;
							ran_right[k]=0;
							bracket_num--;
							k--;
							break;
						}
					}
				}
				for(int z=0;z<k;z++) {
					if(ran_right[z]==ran_left[k]) {
						for(int h=0;h<k;h++) {
							if(ran_left[h]==ran_left[k]) {
								k--;
								z=k-1;
								bracket_num--;
								break;
							}
						}
					}
				}
			}
			
			
			//括号配对，消除最左边和最右边的配对情况
			if(bracket_num>1) {
				sort(ran_left,bracket_num);
				sort(ran_right,bracket_num);
				int flag[]=new int [bracket_num];//标记这个右括号是否被匹配
				for(int k=bracket_num-1;k>=0;k--) {
					for(int z=0;z<bracket_num;z++) {//找到第一个在最右左括号右边的，配对
						if(ran_right[z]>ran_left[k] && flag[z]==0) {
							if(z==bracket_num-1 && k==0) {//出现了最右最左
								swap(ran_left,k,bracket_num-1);
								bracket_num--;
								break;
							}
							
							if(z+1<bracket_num) {
								if(ran_right[z+1]==ran_right[z] && ran_left[k-1]==ran_left[k]) {
									if(ran_right[z+1]<operate_count-1) {
										ran_right[z+1]++;
									}
									else {
										ran_left[k-1]--;
									}
								}
							}
							
							flag[z]=1;
							break;
						}
					}
				}
				
				
				
			}
			
			
			
			
			
			return bracket_num;
		}
		
		return 0;
	}
	public static String Primary() {
		
			int operate_count=r.nextInt(4);
			operate_count=operate_count+2;//操作数2-5个
			String line="";
			
			int ran_left[]=new int [5];
			int ran_right[]=new int [5];
			int num_bra=bracket(ran_left,ran_right,operate_count);//num_bra表示括号的数量
			
			for(int j=0;j<operate_count;j++) {//每个操作数后面接一个操作符，最后一个操作数后面接“=”号
				int ran_int=1+r.nextInt(100);
				
				if(num_bra!=0) {//添加左括号
					for(int k=0;k<num_bra;k++) {
						if(j==ran_left[k]) {
							line+="(";
						}
					}
				}
				
				line+=String.valueOf(ran_int);
				
				if(num_bra!=0) {//添加右括号
					for(int k=0;k<num_bra;k++) {
						if(j==ran_right[k]){
							line+=")";
						}
					}
				}
				
				if(j==operate_count-1) {
					break;
				} 
				int ran_ope=r.nextInt(4);//对应的操作符
				line+=operate[ran_ope];
			}
			
			return line;
		
		
	}

	public static String Junior()  {

			int operate_count=r.nextInt(4);
			operate_count=operate_count+2;
			String line="";
			
			int ran_left[]=new int [5];
			int ran_right[]=new int [5];
			int num_bra=bracket(ran_left,ran_right,operate_count);//num_bra表示括号的数量
			

			
			int count=0;//用于保证至少有一个开方
			
			for(int j=0;j<operate_count;j++) {
				int ran_int=1+r.nextInt(100);
				if(num_bra!=0) {//添加左括号
					for(int k=0;k<num_bra;k++) {
						if(j==ran_left[k]) {
							line+="(";
						}
					}
				}
				
				boolean ran_judge=r.nextBoolean();//当前操作数是否平方或者开方
				int choose=0;
				if(ran_judge || (count==0 && j==operate_count-1)) {
					choose=r.nextInt(3)+1;
					count++;
				}
				if(choose==1) {
					line+=operate[5];
				}
				
				line+=String.valueOf(ran_int);
				
				if(choose==2) {//考虑在括号后面加平方的可能
					line+=operate[4];
				}
				
				if(num_bra!=0) {//添加右括号
					for(int k=0;k<num_bra;k++) {
						if(j==ran_right[k]){
							line+=")";
						}
					}
				}
				
				if(choose==3) {
					line+=operate[4];
				}
				
				if(j==operate_count-1) {
					break;
				} 
				int ran_ope=r.nextInt(4);//对应的操作符
				line+=operate[ran_ope];
			}
			return line;
		
	}
	

	public static String add_tri(int choose_triangle,String line, int ran_int) {
		if(choose_triangle==1) {
			line+=operate[6];
		}
		else if(choose_triangle==2) {
			line+=operate[7];
		}
		else if(choose_triangle==3) {
			if(ran_int!=90) {
				line+=operate[8];
			}
			else {
				line+=operate[7];
			}
		}
		return line;
	}
	public static String Senior() {

			int operate_count=r.nextInt(4);
			operate_count=operate_count+2;
			String line="";
			
			int ran_left[]=new int [5];
			int ran_right[]=new int [5];
			int num_bra=bracket(ran_left,ran_right,operate_count);//num_bra表示括号的数量
			
			int count=0;//用于保证至少有一个有sin  cos tan
			
			for(int j=0;j<operate_count;j++) {
				int ran_int=1+r.nextInt(100);
				
				boolean ran_triangle=r.nextBoolean();
				int choose_triangle=0;
				boolean ran_brck=r.nextBoolean();//控制三角函数在括号内还是括号外,true,在内，false在外
				if(ran_triangle || (j==operate_count-1 && count==0)) {//控制添加sin cos tan的控制数
					choose_triangle=r.nextInt(3)+1;
					count++;
				}
				
				if(!ran_brck) {
					line=add_tri(choose_triangle,line,ran_int);
				}
				
				if(num_bra!=0) {//添加左括号
					for(int k=0;k<num_bra;k++) {
						if(j==ran_left[k]) {
							line+="(";
						}
					}
				}
				
				boolean ran_judge=r.nextBoolean();//当前操作数是否平方或者开方
				int choose=0;
				if(ran_judge ) {
					choose=r.nextInt(3)+1;
				}
				if(choose==1) {//添加开方
					line+=operate[5];
				}
				
				if(ran_brck) {
					line=add_tri(choose_triangle,line,ran_int);
				}
				
				line+=String.valueOf(ran_int);
				
				if(choose==2) {
					line+=operate[4];
				}
				
				if(num_bra!=0) {//添加右括号
					for(int k=0;k<num_bra;k++) {
						if(j==ran_right[k]){
							line+=")";
						}
					}
				}
				if(choose==3) {
					line+=operate[4];
				}
				
				if(j==operate_count-1) {
					break;
				} 
				int ran_ope=r.nextInt(4);//对应的操作符
				line+=operate[ran_ope];
			}
			return line;
		
	}
	
}
