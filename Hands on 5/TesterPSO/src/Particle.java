public class Particle  implements Comparable<Particle> {
	private int MAX_LENGTH;
    private int data[];
    private double velocity; 
    private int conflicts; 

    public Particle(int n) {
    	MAX_LENGTH = n;
    	data = new int[MAX_LENGTH];
        this.velocity = 0.0;
        this.conflicts = 0;
        initData();
    }


    public int compareTo(Particle p) {
    	return this.conflicts - p.getConflicts();
    }


	public void computeConflicts() { 
		String board[][] = new String[MAX_LENGTH][MAX_LENGTH]; 
		int x = 0; 
        int y = 0; 
        int tempx = 0; 
        int tempy = 0; 
        
        int dx[] = new int[] {-1, 1, -1, 1}; 
        int dy[] = new int[] {-1, 1, 1, -1}; 
        
        boolean done = false; 
        int conflicts = 0; 
        
        clearBoard(board); 
        plotQueens(board); 
 
        
        for(int i = 0; i < MAX_LENGTH; i++) {
            x = i;
            y = data[i];

            for(int j = 0; j < 4; j++) { 
                tempx = x;
                tempy = y; 
                done = false;
                
                while(!done) {
                    tempx += dx[j];
                    tempy += dy[j];
                    
                    if((tempx < 0 || tempx >= MAX_LENGTH) || (tempy < 0 || tempy >= MAX_LENGTH)) { 
                        done = true;
                    } else {
                        if(board[tempx][tempy].equals("Q")) {
                            conflicts++;
                        }
                    }
                }
            }
        }

        this.conflicts = conflicts; 
	}
	
	public void plotQueens(String[][] board) {
        for(int i = 0; i < MAX_LENGTH; i++) {
            board[i][data[i]] = "Q";
        }
	}
	
	public void clearBoard(String[][] board) {
		for (int i = 0; i < MAX_LENGTH; i++) {
			for (int j = 0; j < MAX_LENGTH; j++) {
				board[i][j] = "";
			}
		}
	}
	
    public void initData() {
    	for(int i = 0; i < MAX_LENGTH; i++) {
    		data[i] = i;
    	}
    }

    public int getData(int index)  {
    	return this.data[index];
    }
    
    public void setData(int index, int value) {
        this.data[index] = value;
    }
    
    public int getConflicts() {
    	return this.conflicts;
    }

    public void setConflicts(int conflicts) {
    	this.conflicts = conflicts;
    }

    public double getVelocity()  {
    	return this.velocity;
    }
    
    public void setVelocity(double velocityScore) {
       this.velocity = velocityScore;
    }
    
    public int getMaxLength() {
    	return MAX_LENGTH;
    }
}