package mid2019.sandbox;

public class DPExample {
  static String A = "cream", B = "ice";
  static String[][] reason = new String[A.length()+1][B.length()+1];
  static int I = 4, D = 4, R = 4;
  static int ed(int a, int b){
    if(a == 0){reason[a][b]  = " " + b + "*I"; return b*I;}
    if(b == 0){reason[a][b] = " " + a + "*D"; return a*D;}
    int res = ed(a - 1, b - 1);
    if(A.charAt(a-1) == B.charAt(b-1)){
      reason[a][b] = reason[a-1][b-1] + " " + A.charAt(a-1) + "K";
    }else{
      res += R;
      reason[a][b] = reason[a-1][b-1] + " " + A.charAt(a-1) + "R" + B.charAt(b-1);
    }
    int d = ed(a-1, b) + D;
    if(d < res){res = d;
      //System.out.println(A.charAt(a-1) + " delete");
      reason[a][b] = reason[a-1][b]+ " " + A.charAt(a-1) + "D";}
    int i = ed(a, b - 1) + I;
    if (i< res) {res = i;
      //System.out.println(A.charAt(a-1) + " insert");
      reason[a][b] = reason[a][b-1]+ " " + B.charAt(b-1) + "I";}
    return res;
  }
  public static void main(String[] args){
    int x = ed(A.length(), B.length());
    System.out.println(A + "->" + B +" is " + ed(A.length(), B.length()));
    System.out.println(reason[A.length()][B.length()]);
  }
}
