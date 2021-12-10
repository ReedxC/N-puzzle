import java.util.*;
public class Puzzle
{
    ArrayList<Board> visited =  new ArrayList<Board>();
    Board start;
    Board goal;
    int n;
    int dist;
    boolean solvable;
    Board current; 
    Puzzle()
    {
        init();      
    }
    void init()
    {
        start = Board.consoleInput();
        goal = new Board(start.size);
        n=start.size;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                goal.matrix[i][j]=(n*i + j);
        solvable = isSolvable(start);
        if(!solvable){System.out.println("\n!!!UNSOLVABLE!!!"); return;}
        dist = manhattanDist(start,goal);
        current = new Board(start);
        current.getLeaves();
        current.parent = null;
    }
    int manhattanDist(Board s, Board e)
    {
        int d=0;
        for(int i=0;i<s.size;i++)
            for(int j=0;j<s.size;j++)
            {
                outer:
                for(int r=0;r<s.size;r++)
                {
                    for(int c=0;c<s.size;c++)
                    {
                        if((s.matrix[i][j]==e.matrix[r][c]))
                        {
                            d+=((int)Math.abs(r-i))+((int)Math.abs(c-j));
                            break outer;
                        }
                        if(s.matrix[i][j]==0)break outer;
                    }
                }
            }
        return d;
    }
    boolean isSolvable(Board s)
    {
        int v[] = new int[s.size*s.size];
        int c=0;
        int inv = 0;
        int zero_row = 0;
        for(int i=0;i<s.size;i++)
            for(int j=0;j<s.size;j++)
             {
                 v[c++]=s.matrix[i][j];
                 if(s.matrix[i][j]==0)zero_row=i+1;
             }
        for(int i=0;i<v.length-1;i++)
            for(int j=i+1;j<v.length;j++)
                if(v[i]>v[j]&&v[j]!=0)inv++;
               
        if(s.size%2!=0)
            if(inv%2==0) return true;
            else return false;
       
        else
            if(zero_row%2!=0)
                if(inv%2==0)return true;
                else return false;
            else
                if(inv%2!=0)return true;
                else return false;
    }
    boolean isVisited(Board b)
    {
        for(int i=0;i<visited.size();i++)
            if(Board.equals(visited.get(i),b))
                return true;
        return false;
    }
    void solveAstar()
    {
        do
        {
            visited.add(current);
            if(dist==0)break;
            int dleft=Integer.MAX_VALUE,
                dright=Integer.MAX_VALUE,
                dup=Integer.MAX_VALUE,
                ddown=Integer.MAX_VALUE,
                minD=Integer.MAX_VALUE;
            if(current.left!=null&&!Board.equals(current.left,current.parent)&&!isVisited(current.left))
            {
                dleft = manhattanDist(current.left,goal);
                if(dleft<minD)
                    minD=dleft;
            }    
            if(current.right!=null&&!Board.equals(current.right,current.parent)&&!isVisited(current.right))
            {
                dright = manhattanDist(current.right,goal);
                if(dright<minD)
                    minD=dright;
            }  
            if(current.up!=null&&!Board.equals(current.up,current.parent)&&!isVisited(current.up))
            {
                dup = manhattanDist(current.up,goal);
                if(dup<minD)
                    minD=dup;
            }
            if(current.down!=null&&!Board.equals(current.down,current.parent)&&!isVisited(current.down))
            {
                ddown = manhattanDist(current.down,goal);
                if(ddown<minD)
                    minD=ddown;
            }  
            
            dist = minD;
            if(Board.equals(current.parent,current)|| (minD==Integer.MAX_VALUE))
            {
                current=current.parent;
                continue;
            }
            if(minD==dleft)
            {
                current.left.parent = current;
                current = current.left;
                current.getLeaves();
            }
            else if(minD==dright)
            {
                current.right.parent = current;
                current = current.right;
                current.getLeaves();
            }
            else if(minD==dup)
            {
                current.up.parent = current;
                current = current.up;
                current.getLeaves();
            }
            else if(minD==ddown)
            {
                current.down.parent = current;
                current = current.down;
                current.getLeaves();
            }
        } while(dist!=0);
        ArrayList<Board> steps = new ArrayList<Board>();
        while(current!=null)
        {
            steps.add(0,current);
            current = current.parent;
        }
        for(int i=0;i<steps.size();i++)
            steps.get(i).consoleOutput();
        System.out.println("\nMoves: "+(steps.size()-1));    
    }
    void solveBFS()
    {
        ArrayList<Board> queue = new ArrayList<Board>();
        queue.add(start);
        Board temp = start;
        while(!queue.isEmpty())
        {
            temp = queue.remove(0);
            if(Board.equals(temp,goal))break;
            temp.getLeaves();
            if(temp==start)
                temp.parent = null;
            if(temp.left!=null&&!Board.equals(temp.left,temp.parent))
            {
                queue.add(temp.left);
                temp.left.parent = temp;
            }
            if(temp.right!=null&&!Board.equals(temp.right,temp.parent))
            {
                queue.add(temp.right);
                temp.right.parent = temp;
            }
            if(temp.down!=null&&!Board.equals(temp.down,temp.parent))
            {
                queue.add(temp.down);
                temp.down.parent = temp;
            }
            if(temp.up!=null&&!Board.equals(temp.up,temp.parent))
            {
                queue.add(temp.up);
                temp.up.parent = temp;
            }
        }
        queue = new ArrayList<Board>();
        while(temp!=null)
        {
            queue.add(0,temp);
            temp=temp.parent;
        }
        for(int i=0;i<queue.size();i++)
            queue.get(i).consoleOutput();
    }
}
class Board
{
    int matrix[][];
    int size;
    Board left,right,up,down,parent;
    Board(int n)
    {
        size=n;
        matrix = new int[size][size];
    }
    Board()
    {
        size=0;
        matrix=null;
    }
    Board(Board b)
    {
        size=b.size;
        matrix = new int[size][size];
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                matrix[i][j] = b.matrix[i][j];
    }
    Board(int[][] m)
    {
        matrix = new int[m.length][m.length];
        for(int i=0;i<m.length;i++)
            for(int j=0;j<m.length;j++)
                matrix[i][j]=m[i][j];
        size = m.length;
    }
    void getLeaves()
    {
        if(size<=1||matrix == null)
            return;
        int z=0;
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
            {    
                if(matrix[i][j]==0)z++;
                if(z>1)return;
            }
        Board temp = new Board(matrix);
        if(Board.moveLeft(temp))
            left=temp;
        else left = null;
        temp = new Board(matrix);
        if(Board.moveRight(temp=new Board(this)))
            right=temp;
        else right = null; 
        temp = new Board(matrix);
        if(Board.moveUp(temp=new Board(this)))
            up=temp;
        else up=null;    
        temp = new Board(matrix);
        if(Board.moveDown(temp=new Board(this)))
            down=temp;
        else down = null;    
    }
    static boolean equals(Board a, Board b)
    {
        if(a==null||b==null)return false;
        if(a.size!=b.size)return false;
        for(int i=0;i<a.size;i++)
            for(int j=0;j<a.size;j++)
                if(a.matrix[i][j]!=b.matrix[i][j])return false;
        return true;        
    }
    static Board consoleInput()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the size of the matrix");
        int s = sc.nextInt();
        System.out.println("Enter the elements of the matrix");
        int m[][] = new int[s][s];
        for(int i=0;i<s;i++)
            for(int j=0;j<s;j++)
                m[i][j]=sc.nextInt();
        Board b = new Board(m);
        return b;
    }
    void consoleOutput()
    {
        System.out.println("The matrix:");
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++)
                System.out.print(matrix[i][j] + "\t");    
            System.out.println();
        }
    }
    static void consoleOutput(Board b)
    {
        System.out.println("The matrix:");
        for(int i=0;i<b.size;i++){
            for(int j=0;j<b.size;j++)
                System.out.print(b.matrix[i][j] + "\t");    
            System.out.println();
        }
    }
    static boolean moveLeft(Board b)
    {
        int[][] m = b.matrix;
        int s = b.size;
        outer:
        for(int i=0;i<s;i++)
            for(int j=0;j<s;j++)
                if(m[i][j]==0)
                    if(j>0)
                    {
                        m[i][j] = m[i][j-1];
                        m[i][j-1] = 0;
                        break outer;
                    }
                    else
                        return false;
        return true;             
    }
    static boolean moveRight(Board b)
    {
        int[][] m = b.matrix;
        int s = b.size;
        outer:
        for(int i=0;i<s;i++)
            for(int j=0;j<s;j++)
                if(m[i][j]==0)
                    if(j<s-1)
                    {
                        m[i][j] = m[i][j+1];
                        m[i][j+1] = 0;
                        break outer;
                    }
                    else
                        return false;
        return true;
    }
    static boolean moveUp(Board b)
    {
        int[][] m = b.matrix;
        int s = b.size;
        outer:
        for(int i=0;i<s;i++)
            for(int j=0;j<s;j++)
                if(m[i][j]==0)
                    if(i>0)
                    {
                        m[i][j] = m[i-1][j];
                        m[i-1][j] = 0;
                        break outer;
                    }
                    else
                        return false;
        return true;                
    }
    static boolean moveDown(Board b)
    {
        int[][] m = b.matrix;
        int s = b.size;
        outer:
        for(int i=0;i<s;i++)
            for(int j=0;j<s;j++)
                if(m[i][j]==0)
                    if(i<s-1)
                    {
                        m[i][j] = m[i+1][j];
                        m[i+1][j] = 0;
                        break outer;
                    }
                    else
                        return false;
        return true;                
    }
}
