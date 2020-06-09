package mid2019.sandbox;

import java.awt.Color;
import java.awt.Graphics;
import java.security.PublicKey;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import mid2019.graphicsLib.G;
import mid2019.graphicsLib.G.V;
import mid2019.graphicsLib.G.XY;


public class ShuntYard {
  static Stack opStack = new Stack();
  public static boolean isOp(String w){
    if(w.equals("+") || w.equals("-") || w.equals("*") || w.equals("/")){
      return true;
    }
    return false;
  }
  public static boolean isArg(String w){
    return Character.isDigit(w.charAt(0));
  }

//  public static int pow(String w){
//    if(OCPairs.isOpen(w)){ return 0; }
//    if(OCPairs.isClose(w)) {return 1;}
//    if(w.equals("+")||w.equals("-")){ return 1; }
//    return 2;
//  }

//  public static List<String> postFix(String input) {
//    input = "{ " + input + " }";
//    String[] inFix = input.split(" ");
//    List<String> sb = new ArrayList<>();
//    for (String w:inFix){
//      if (isArg(w)) { sb.add(w); }
//      if (isOp(w)) { popVips(opStack, sb, pow(w));opStack.push(w); }
//      if (OCPairs.isOpen(w)){opStack.push(w);}
//      if (OCPairs.isClose(w)){
//        popVips(opStack, sb, pow(w)); testBalance(opStack, w);}
//    }
//    popVips(opStack, sb, 0);
//    return sb;
//  }
//
//  static void popVips(Stack stack, List<String> w, int p) {
//    while (!stack.isEmpty() && p <= pow(stack.peek().toString())) {
//      w.add(opStack.pop().toString());
//    }
//  }
//  public static boolean testBalance(Stack<String> stack, String w){
//    if(!stack.empty()){
//      if(OCPairs.openers.indexOf(stack.pop())== OCPairs.closers.indexOf(w)){
//        return true;
//      }
//    }
//    System.out.println("ERROR");
//    return false;
//  }

//  public static int eval(List<String> postFixedString){
//    Deque<Integer> stack = new ArrayDeque<>();
//    for(String w: postFixedString) {
//      if (isArg(w)) { stack.push(Integer.parseInt(w)); }
//      if (isOp(w)) {
//        switch (w.charAt(0)) {
//          case '+': stack.push(stack.pop() + stack.pop());break;
//          case '*': stack.push(stack.pop() * stack.pop());break;
//          case '-': int num1 = stack.pop();stack.push(stack.pop() - num1);break;
//          case '/': num1 = stack.pop();stack.push(stack.pop() / num1);break;
//        }
//      }
//    }
//    return stack.pop();
//  }

  public static class OCPairs{   // open and close pairs
    public String open, close;
    public static String openers = "", closers = "";
    static {
      new OCPairs("(", ")");
      new OCPairs("[", "]");
      new OCPairs("{", "}");
    }
    public OCPairs(String open, String close) {
      this.open = open;
      this.close = close;
      openers += open;
      closers += close;
    }
    public static boolean isOpen(String word){
      return openers.indexOf(word) >= 0;
    }
    public static boolean isClose(String word){
      return closers.indexOf(word) >= 0;
    }
  }
//--------------------------------------------binary tree------------------------------------------
  public static class BT{
    public Token toke;
    public BT left, right;

    public BT(Token toke, BT left, BT right) {
      this.toke = toke;
      this.left = left;
      this.right = right;
    }

    public BT(Token toke) { this(toke, null, null); }
    public boolean isLeaf(){ return left == null; }
    //-----------------------------------------BT View--------------------------------------
    public static class View{
      public BT bt;
      public XY loc = new XY(0, 0);
      public View vL, vR;   // left, right
      public String openP = "", closeP = "";  // open piece of parenthesis
      public static boolean isInfix = false;
      public View(BT bt, View vL, View vR) {
        this.bt = bt;
        this.vL = vL;
        this.vR = vR;
      }
      public static View getView(BT bt){
        if(bt.isLeaf()){ return new View(bt, null, null);}
        View left = getView(bt.left);
        View right = getView(bt.right);
        return new View(bt, left, right);
      }
      public int setXInfix(int x, int t){
        isInfix = true;
        if(isLeaf()){ loc.x.set(x, t); return x+100; }
        x = vL.setXInfix(x, t);
        loc.x.set(x, t);
        x = vR.setXInfix(x+100, t);
        return x;
      }
      public int setXPostfix(int x, int t){
        isInfix = false;
        if(isLeaf()){ loc.x.set(x, t); return x+100; }
        x = vL.setXPostfix(x, t);
        x = vR.setXPostfix(x, t);
        loc.x.set(x, t);
        return x+100;
      }
      public void setY(int y, int width, int nP, int t){
        loc.y.set(y, t);
        if(isLeaf()){ return; }
        int pL=(needsParen(vL.bt.toke))?(nP + 1):nP, pR=(needsParen(vR.bt.toke))?(nP + 1):nP;     // the num of parenthesis
        if(vL.isLeaf()){
          vL.loc.y.set(y + width, t);
          vL.openP = parens(pL, '(');
          vL.closeP = "";
        } else {
          vL.setY(y + width, width, pL, t);
        }
        if(vR.isLeaf()){
          vR.loc.y.set(y + width, t);
          vR.openP = "";
          vR.closeP = parens(pR, ')');
        } else{
          vR.setY(y + width, width, pR, t);
        }
      }
      public String parens(int nP, char c){
        String res = "";
        for (int i = 0; i < nP; i++){res += c;}
        return res;
      }
      public boolean needsParen(Token t){
        if(t.type == Token.LIT){
          return false;
        }
        return this.bt.toke.pow > t.pow;
      }
      public boolean isLeaf(){
        // System.out.println(bt.toke.name + ": " + bt.isLeaf());
        return bt.isLeaf();
      }
      public void show(Graphics g){
        g.setColor(Color.BLACK);
        if(!isLeaf()){
          this.loc.drawLine(g, vL.loc);
          this.loc.drawLine(g, vR.loc);
          vL.show(g);
          vR.show(g);
        }
        G.drawCircle(g, loc, 15);
        String str = this.bt.toke.name;
        if(isInfix){str = openP  + str + closeP;}
        g.drawString(str, loc.x.val, loc.y.val);
      }
    }
  }
  //----------------------------------------------TOKEN-------------------------------------------------
  public static class Token{
    public static final int BIN = 17, LIT = 28, OPEN = 5, CLOSE = 99;
    public static Token OPEN_EXPRESSION, CLOSE_EXPRESSION;
    public String name;
    public int type;  // BIN, LIT
    public int pow;
    public static HashMap<String, Token> NAME_TOKEN = new HashMap<>();
    public Token(String name, int type, int pow) {
      this.name = name;
      this.type = type;
      this.pow = pow;
      NAME_TOKEN.put(name, this);
    }
    public Token(String name){ this(name, LIT, 0); NAME_TOKEN.put(name, this);}
    public Token(String name, int pow){ this(name, BIN, pow); NAME_TOKEN.put(name, this);}

    static {
      new Token("+", 2);
      new Token("-", 2);
      new Token("*", 3);
      new Token("/", 3);
      new Token("%", 3);
      OPEN_EXPRESSION = new Token("(", OPEN,0);
      CLOSE_EXPRESSION = new Token(")", CLOSE,1);
    }
    public String toString(){ return this.name;}
    public static Token getToken(String str){
      Token result = NAME_TOKEN.get(str);
      if (result == null){
        return new Token(str);
      }
      return result;
    }
    public static Token.List lex(String str){
      String[] strings = str.split(" +");  // regular expression
      Token.List res = new Token.List();
      for(String s: strings){ res.add(getToken(s)); }
      return res;
    }
    public void balance(Stack ops){
      if(!ops.isEmpty() && ops.peek().type == OPEN){
        ops.pop();
      }else{
        System.out.println("WTF???????????? This is an unbalanced parenthesis");
      }
    }
    //-----------------------------------------LIST OF TOKEN------------------------------------------
    public static class List extends ArrayList<Token>{
      public List toPostFix(){
        // input = "{ " + input + " }";
        Stack ops = new Stack();
        Token.List res = new Token.List();
        ops.push(OPEN_EXPRESSION);
        for (Token toke: this){
//          if (toke.type == ) { sb.add(w); }
//          if (isOp(w)) { popVips(opStack, sb, pow(w));opStack.push(w); }
//          if (OCPairs.isOpen(w)){opStack.push(w);}
//          if (OCPairs.isClose(w)){
//            popVips(opStack, sb, pow(w)); testBalance(opStack, w);}
          switch (toke.type){
            case BIN: ops.popVips(res, toke.pow); ops.push(toke); break;          // binary ops
            case LIT: res.add(toke); break;   // literal values
            case OPEN: ops.push(toke); break;
            case CLOSE: ops.popVips(res, 1); toke.balance(ops); break;
            default: System.out.println("WTF???"); break;
          }
        }
        ops.popVips(res, 1);
        CLOSE_EXPRESSION.balance(ops);
        if(!ops.isEmpty()){
          System.out.println("WTF????unbalanced parenthesis");
        }
        return res;
      }
      public BT toBT(){
        java.util.Stack<BT> btStack = new java.util.Stack<>();
        for(Token toke : this){
          switch (toke.type) {
            case BIN: BT right = btStack.pop(); btStack.push(new BT(toke, btStack.pop(), right));break;          // binary ops
            case LIT: btStack.push(new BT(toke));break;   // literal values
            default: System.out.println("WTF?????????");
          }
        }
        if(btStack.size() != 1){
          System.out.println("WTF");
        }
        return btStack.pop();
      }
      public LinkedList<Integer> evalUpTo(int n){
        LinkedList<Integer> res = new LinkedList<>();
        for(int i = 0; i <= n ; i ++){
          Token t= this.get(i);
          switch (t.type){
            case LIT : res.push(Integer.parseInt(t.name)); break;
            case BIN : if(t.name.equals("*")){
                res.push(res.pop() * res.pop());
              }else if(t.name.equals("+")){
                res.push(res.pop() + res.pop());
              }else if(t.name.equals("-")) {
                Integer f = res.pop();
                res.push(res.pop() - f);
              }else if(t.name.equals("/")) {
                Integer f = res.pop();
                res.push(res.pop() / f);
              } else{
                Integer f = res.pop();
                res.push(res.pop() % f);
              }break;
            default:
              System.out.println("WTF " + t.name);break;
          }
        }
        return res;
      }
      public int eval(){return evalUpTo(this.size() - 1).pop();}
      public void showUpTo(Graphics g, int n){
        int h = 30, w = 100, bY = 600, x = 800;
        LinkedList<Integer> stack = evalUpTo(n);
        for(int i = 0; i < 8; i++){
          g.setColor(Color.WHITE);g.fillRect(x, bY - h * i, w, h);
          g.setColor(Color.BLACK);g.drawRect(x, bY - h * i, w, h);
          if(i < stack.size()){
            g.drawString("" + stack.get(stack.size() -1 - i), x + 40, bY - h*i + 20); // we need to reverse i because it's a stack
          }
        }

      }
      public void show(Graphics g){}
    }
    //-----------------------------------------STACK OF TOKEN-----------------------------------------
    public static class Stack extends java.util.Stack<Token>{
      public void popVips(Token.List out, int pow){
        while(!isEmpty() && peek().pow >= pow){ out.add(pop()); }
      }
    }
  }
  public static void main(String[] args){
    String input = "( 1 + 2 ) * 3 - 4 + 3";
    // System.out.println(postFix(input));
    // System.out.println(eval(postFix(input)));
    Token.List inFix = Token.lex(input);
    System.out.println(inFix);
    Token.List postFix = inFix.toPostFix();
    System.out.println(postFix);
  }
}

