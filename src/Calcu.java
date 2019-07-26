
public class Calcu {
	public static double calcu(String numstr) {
	double re = result(numstr);
		if(Math.abs(re)<=0.01) {
			return re;
		}
		else {
//			BigDecimal b=new BigDecimal(re);
//			double r=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();/		
			double r=(double)Math.round(re*100)/100;			
			return r;
//			DecimalFormat df = new DecimalFormat("#0.00");
//			return Double.valueOf(df.format(re));
		}
	}
    public static double result(String numstr){
        //先把括号中的值给算出来然后替换，找的括号是最小范围的
        //最后一个'('出现的位置 到 第一个')'出现的位置
    	//每次处理一个括号
        StringBuffer sb=new StringBuffer(numstr);
            String rs=bracketsString(sb);
            while(rs!=null){
                double calBracketsValue = calBracketsValue(rs);
                String calBracketString;
                if(calBracketsValue<0) {
                	calBracketString = String.valueOf(calBracketsValue);
                	calBracketString = "0"+calBracketString;
                }
                else {
                	calBracketString = String.valueOf(calBracketsValue);
                }
                sb.replace(sb.indexOf("@"), sb.indexOf("@")+1, calBracketString);
                rs=bracketsString(sb);
            }
            return calBracketsValue(sb.toString());

    }
    
    //每次都找最里面的一对括号,若有优先级相同的括号，先算左边的括号
    public static String bracketsString(StringBuffer str){
        int leftBracketsPoint=str.lastIndexOf("(");
        int rightBracketsPoint=str.indexOf(")");
        if(leftBracketsPoint==-1 || rightBracketsPoint==-1){
            return null;
        }
        String res="";
        if(rightBracketsPoint>leftBracketsPoint) {
        	res= str.substring(leftBracketsPoint+1,rightBracketsPoint);
        	str.replace(leftBracketsPoint, rightBracketsPoint+1, "@");
        }
        else if(rightBracketsPoint<=leftBracketsPoint) {
        	leftBracketsPoint=str.indexOf("(");
        	rightBracketsPoint=str.indexOf(")");
        	res= str.substring(leftBracketsPoint+1,rightBracketsPoint);
        	str.replace(leftBracketsPoint, rightBracketsPoint+1, "@");
        }
        return res;
    }
    //递归处理平方，开平方，三角函数组合的式子
    public static double Complex(String st) {
		char ch[] = st.toCharArray();
		if(ch[0]=='√') {
			return Math.sqrt(Complex(st.substring(1,st.length())));
		}
		else if(ch[0]=='s') {
			return Math.sin(Complex(st.substring(3,st.length())));
		}
		else if(ch[0]=='c') {
			return Math.cos(Complex(st.substring(3,st.length())));
		}
		else if(ch[0]=='t') {
			return Math.tan(Complex(st.substring(3,st.length())));
		}
		else {
			double num = 0.0;
			if(ch[ch.length-1]=='²') {
				String numstr = st.substring(0,st.length()-1);
				num = Double.parseDouble(numstr);
				num = Math.pow(num, 2);
			}
			else {
				num = Double.parseDouble(st);
			}	
			return num;
		}
   	}
    /***
     * 计算无括号的式子的值，从外到内，先找一级的运算符，即+-，然后以+-做分隔符，得到局部式子，每一个局部式子的值
     * 都是加数，减数，被减数，对每一个局部式子用相同的思路处理，以*和除号做分隔符，得到不含四则运算的式子，每一个
     * 局部式子代表乘数，除数，再对每一个更小的局部处理，以三角函数符号做分隔符，分情况讨论数与平方，开平方的位置
     * 关系，最后从内到外计算值，又局部到整体，最终返回整个式子的值；
     * @param numStr
     * @return，返回值为double
     */
    public static double calBracketsValue(String numStr){
    	double fin_result = 0;
    	int s_count = 0;
    	char sym_jiajian[] = new char[10];
    	char str[] = numStr.toCharArray();
    	for(int l=0;l<str.length;l++) {
    		if(str[l]=='+'||str[l]=='-') {
    			sym_jiajian[s_count] = str[l];
    			s_count++;
    		}
    	}
    	
    	String jiajian[] = numStr.split("\\+|-");
    	double result_j[] = new double[jiajian.length];
    		
    	for(int i=0;i<jiajian.length;i++) {

    		char sym_chengchu[] = new char[10];
    		char s_t[] = jiajian[i].toCharArray();
    		int count = 0;	
    		for(int l=0;l<s_t.length;l++) {
	    		if(s_t[l]=='*'||s_t[l]=='/') {
	    			sym_chengchu[count] = s_t[l];
	    			count++;
	    		}
    		}
    				
    		String chengchu[] = jiajian[i].split("\\*|/");
    				
    		double result_c[] = new double[chengchu.length];

    		for(int j=0;j<chengchu.length;j++) {
    			result_c[j] = Complex(chengchu[j]);		
    			if(j==0) {
    				result_j[i]= result_c[j];
    			}
    			else {
    				if(sym_chengchu[j-1]=='*')
    					result_j[i]=result_j[i]*result_c[j];
    				else if(sym_chengchu[j-1]=='/')
    					result_j[i]=result_j[i]/result_c[j];
    			}
    		}
    				
    		if(i==0) {
    			fin_result = result_j[i];
    		}
    		else {
    			if(sym_jiajian[i-1]=='+') {
    				fin_result +=result_j[i];
    			}
    			else if(sym_jiajian[i-1]=='-') {
    				fin_result-=result_j[i];
    			}
    		}
    			

    	}
 
    	return fin_result;
    }
}
