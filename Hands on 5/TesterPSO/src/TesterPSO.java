public class TesterPSO {
	Writer logWriter;
	ParticleSwarmOptimization pso;
	int MAX_RUN;
	int MAX_LENGTH;
	long[] runtimes;

	public TesterPSO() {
		logWriter = new Writer();
		MAX_RUN = 50;
		runtimes = new long[MAX_RUN];
	}

	public void test(int maxLength, double maxVelocity, int maxEpoch) {
		MAX_LENGTH = maxLength;
		pso = new ParticleSwarmOptimization(MAX_LENGTH);		
		pso.setVMax(maxVelocity);
		pso.setMaxEpoch(maxEpoch);
		long testStart = System.nanoTime();
		String filepath = "PSO-N"+MAX_LENGTH+"-"+maxVelocity+"-"+maxEpoch+".txt";
		long startTime = 0;
        long endTime = 0;
        long totalTime = 0;
        int fail = 0;
        int success = 0;
        
		logParameters();
        
        for(int i = 0; i < MAX_RUN; ) {												
        	startTime = System.nanoTime();
        	if(pso.algorithm()) {
        		endTime = System.nanoTime();
        		totalTime = endTime - startTime;
        		
        		System.out.println("Comienza "+(i+1));
            	System.out.println("Nanosegundos "+totalTime);
            	
            	runtimes[i] = totalTime;
            	i++;
            	success++;
            	
            	logWriter.add((String)("Comienza "+i));
            	logWriter.add((String)("Tiempo de ejecución "+totalTime));
            	logWriter.add((String)("Encontrado "+pso.getEpoch()));
            	logWriter.add((String)("Tamaño de la poblacion "+pso.getPopSize()));
            	logWriter.add("");
            	
            	for(Particle p: pso.getSolutions()) {								
					logWriter.add(p);
					logWriter.add("");
    			}
        	} else {																
        		fail++;
        		System.out.println("error!");
        	}
        	
        	if(fail >= 100) {
        		System.out.println("No se puede encontrar la solución con estos parámetros ");
        		break;
        	}
        	startTime = 0;															
        	endTime = 0;
        	totalTime = 0;
        }
	
        System.out.println("Número de éxito " +success);
        System.out.println("Numero de error "+fail);
        logWriter.add("Runtime summary");
        logWriter.add("");
        
		for(int x = 0; x < runtimes.length; x++){									
			logWriter.add(Long.toString(runtimes[x]));
		}
		
		long testEnd = System.nanoTime();
		logWriter.add(Long.toString(testStart));
		logWriter.add(Long.toString(testEnd));
		logWriter.add(Long.toString(testEnd - testStart));
		
      
       	logWriter.writeFile(filepath);
       	printRuntimes();
	}

	public void logParameters() {
        logWriter.add("Particle Swarm Optimization Algorithm");
        logWriter.add("Parameters");
        logWriter.add((String)("MAX_LENGTH/N: "+MAX_LENGTH));
        logWriter.add((String)("STARTING_POPULATION: "+pso.getParticleCount()));
        logWriter.add((String)("MAX_EPOCHS: "+pso.getMaxEpoch()));
        logWriter.add((String)("MAX_VELOCITY: "+pso.getVmax()));
        logWriter.add((String)("MINIMUM_SHUFFLES: "+pso.getShuffleMin()));
        logWriter.add((String)("MAXIMUM_SHUFFLES: "+pso.getShuffleMax()));
        logWriter.add("");
	}

	public void printRuntimes() {
		for(long x: runtimes){
			System.out.println("Tiempo "+x+" Nanosegundos");
		}	
	}

	public static void main(String args[]) {
		TesterPSO tester = new TesterPSO();

		tester.test(4, 4, 1000);
	}
}
