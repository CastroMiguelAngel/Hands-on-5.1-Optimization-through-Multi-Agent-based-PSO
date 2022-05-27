import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class ParticleSwarmOptimization {

	private int MAX_LENGTH;				
	private int PARTICLE_COUNT;			
	private double V_MAX; 				
	private int MAX_EPOCHS;
	private int TARGET; 					
    private int SHUFFLE_RANGE_MIN;		
    private int SHUFFLE_RANGE_MAX;

    private Random rand;
    private ArrayList<Particle> particles;
    private ArrayList<Particle> solutions;
    private int epoch;

	public ParticleSwarmOptimization(int n) {
		MAX_LENGTH = n;
		PARTICLE_COUNT = 40;	
		V_MAX = 4; 
		MAX_EPOCHS = 5000; 
		TARGET = 0;
		SHUFFLE_RANGE_MIN = 8;
		SHUFFLE_RANGE_MAX = 20;
		epoch = 0;
	}


	public boolean algorithm() {
		particles = new ArrayList<Particle>();
		solutions = new ArrayList<Particle>();
		rand = new Random();
		epoch = 0;
		boolean done = false;
		Particle aParticle = null;

		initialize();

		while(!done) {
			if(epoch < MAX_EPOCHS) {
	            for(int i = 0; i < PARTICLE_COUNT; i++)  {
	                aParticle = particles.get(i);
	                aParticle.computeConflicts();
	                if(aParticle.getConflicts() == TARGET){
	                    done = true;
	                }
	            } 
	            
	            Collections.sort(particles); 					
	            
	            getVelocity();
	            
	            updateParticles();
	    
				epoch++;
				System.out.println("Epoca: " + epoch);
			} else {
				done = true;
			}
		}

		System.out.println("Listo");
		if(epoch == MAX_EPOCHS) {
			System.out.println("No se encontró ninguna solución");
			done = false;
		}
		
		for(Particle p: particles) {							
			if(p.getConflicts() == TARGET) {
				System.out.println("Solucion");
				solutions.add(p);
                printSolution(p);
                System.out.println("conflictos "+p.getConflicts());
			}
		}
		
		return done;
	}

	public void updateParticles() {
		Particle source = null;
		Particle destination = null;
		
	    for(int i = 1; i < PARTICLE_COUNT; i++) {
    		
	    	source = particles.get(i-1);
	    	destination = particles.get(i);
	    	
	    	int changes = (int)Math.floor(Math.abs(destination.getVelocity()));
    		
        	for(int j = 0; j < changes; j++) {
        		if(new Random().nextBoolean()) { 
        			randomlyArrange(i);
        		}
        		
        		copyFromParticle(source, destination); 
        	} 
        	destination.computeConflicts();;
	    } 	
	}
	
	public void copyFromParticle(Particle best, Particle destination) {

		int targetA = getRandomNumber(0, MAX_LENGTH - 1); 					
		int targetB = 0;
		int indexA = 0;
		int indexB = 0;
		int tempIndex = 0;
		
		int i = 0;
		for(; i < MAX_LENGTH; i++) {
			if(best.getData(i) == targetA) {
				if(i == MAX_LENGTH - 1) {
					targetB = best.getData(0); 								
				} else {
					targetB = best.getData(i + 1);
				}
				break;
			}
		}
		
		for(int j = 0; j < MAX_LENGTH; j++) {
			if(destination.getData(j) == targetA) {
				indexA = j;
			}
			if(destination.getData(j) == targetB) {
				indexB = j;
			}
		}
		
		if(indexA == MAX_LENGTH - 1){
			tempIndex = 0;
		}else{
			tempIndex = indexA + 1;
		}
		
		int temp = destination.getData(tempIndex);
		destination.setData(tempIndex, destination.getData(indexB));
		destination.setData(indexB, temp);
		
	}

	public void getVelocity() {
		double worstResults = 0;
		double vValue = 0.0;
		Particle aParticle = null;
		
	    worstResults = particles.get(PARTICLE_COUNT - 1).getConflicts();

	    for(int i = 0; i < PARTICLE_COUNT; i++) {
	    	aParticle = particles.get(i);
	        vValue = (V_MAX * aParticle.getConflicts()) / worstResults;

	        if(vValue > V_MAX){
	        	aParticle.setVelocity(V_MAX);
	        }else if(vValue < 0.0){
	        	aParticle.setVelocity(0.0);
	        }else{
	        	aParticle.setVelocity(vValue);
	        }
	    }
	}

	public void initialize() {
		int newParticleIndex = 0;
		int shuffles = 0;
		
		for(int i = 0; i < PARTICLE_COUNT; i++) {
	        Particle newParticle = new Particle(MAX_LENGTH);
	   
	        particles.add(newParticle);
	        newParticleIndex = particles.indexOf(newParticle);
	        
	        shuffles = getRandomNumber(SHUFFLE_RANGE_MIN, SHUFFLE_RANGE_MAX);
	        
	        for(int j = 0; j < shuffles; j++) {
	        	randomlyArrange(newParticleIndex);
	        }
	        
	        particles.get(newParticleIndex).computeConflicts();
	    } 	
	}

	public void randomlyArrange(int index) { 
		int positionA = getRandomNumber(0, MAX_LENGTH - 1);
		int positionB = getExclusiveRandomNumber(MAX_LENGTH - 1, positionA);
		Particle thisParticle = particles.get(index);
		int temp = thisParticle.getData(positionA);
		thisParticle.setData(positionA, thisParticle.getData(positionB));
		thisParticle.setData(positionB, temp);		
	}

    public int getRandomNumber(int low, int high) {
   		return (int)Math.round((high - low) * rand.nextDouble() + low);
    }

    public int getExclusiveRandomNumber(int high, int except) {
    	boolean done = false;
    	int getRand = 0;

    	while(!done) {
    		getRand = rand.nextInt(high);
    		if(getRand != except){
    			done = true;
    		}
    	}

        return getRand;    	
    }   

    public void printSolution(Particle solution) {
       	String board[][] = new String[MAX_LENGTH][MAX_LENGTH];
           
       for(int x = 0; x < MAX_LENGTH; x++) {
           for(int y = 0; y < MAX_LENGTH; y++) {
               board[x][y] = "";
           }
       }

       for(int x = 0; x < MAX_LENGTH; x++) {
           board[x][solution.getData(x)] = "Q";
       }

       for(int y = 0; y < MAX_LENGTH; y++) {
           for(int x = 0; x < MAX_LENGTH; x++) {
        	   if(board[x][y] == "Q") {
                   //System.out.print("1 ");
               } else {
                   //System.out.print("0 ");
               }
           }
           //System.out.print("\n");
       }
       System.out.print("\n");
    }
    
	public ArrayList<Particle> getSolutions() {
		return solutions;
	}

	public int getEpoch() {
		return epoch;
	}

	public int getPopSize() {
		return particles.size();
	}

	public int getParticleCount() {
		return PARTICLE_COUNT;
	}

	public double getVmax() {
		return V_MAX;
	}


	public int getMaxEpoch() {
		return MAX_EPOCHS;
	}


	public int getShuffleMin() {
		return SHUFFLE_RANGE_MIN;
	}


	public int getShuffleMax() {
		return SHUFFLE_RANGE_MAX;
	}

	
	public void setMaxEpoch(int newMaxEpochs) {
		this.MAX_EPOCHS = newMaxEpochs;
	}

	
	public void setVMax(double newMaxVelocity) {
		this.V_MAX = newMaxVelocity;
	}
}


