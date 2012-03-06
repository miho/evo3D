package xmarti29.evo3D;

import org.jgap.*;
import org.jgap.impl.*;

import com.trolltech.qt.gui.QMessageBox;

/**
 * This class represents the evolution algorithm. It takes input from user
 * and based on the values evolves parameters of the 3D object. Fitness values
 * should be in range 1 and up, where 1 is the worst.
 */
public class GA
{
	Configuration conf = null;
	FitnessFunction ff = null;
	
	Gene[] genes = new Gene[11];
	
	Chromosome chromosome = null;
	
	Genotype population = null;
	
	double FitValues[] = {1.0, 1.0, 1.0, 1.0, 1.0};
	
	public GA()
	{
		conf = new DefaultConfiguration();
		
		ff = new GAFitness();
		
		try
		{
			conf.setFitnessFunction(ff);
		
			genes[0] = new DoubleGene(conf, 0.0, 1.0);	// colorIn R
			genes[1] = new DoubleGene(conf, 0.0, 1.0);	// colorIn G
			genes[2] = new DoubleGene(conf, 0.0, 1.0);	// colorIn B
			genes[3] = new DoubleGene(conf, 0.0, 1.0);	// colorOut R
			genes[4] = new DoubleGene(conf, 0.0, 1.0);	// colorOut G
			genes[5] = new DoubleGene(conf, 0.0, 1.0);	// colorOut B
			//genes[6] = new DoubleGene(conf, 2.0, 10.0);
			//genes[7] = new DoubleGene(conf, 2.0, 10.0);
			genes[6] = new BooleanGene(conf);	// top branch
			genes[7] = new BooleanGene(conf);	// left branch
			genes[8] = new BooleanGene(conf);	// right branch
			genes[9] = new BooleanGene(conf);	// front branch
			genes[10] = new BooleanGene(conf);	// back branch
			
			chromosome = new Chromosome(conf, genes);
			
			conf.setSampleChromosome(chromosome);
			
			conf.setPopulationSize(5);
			
			population = Genotype.randomInitialGenotype(conf);
		}
		catch (InvalidConfigurationException e)
		{
			QMessageBox.critical(null, "Invalid configuration", e.getMessage());
		}
	}
	
	/**
	 * Store fitness values from user to each chromosome in the population
	 * and evolve.
	 */
	public void evolve(double[] fv)
	{
		for (int i = 0; i < 5; i++)
			population.getPopulation().getChromosome(i).setApplicationData(fv[i]);
		
		population.evolve();
		
		//System.out.println("pop: " + population.getPopulation().size());
		
		//Population pop = population.getPopulation();
		
		/*for (int i = 0; i < pop.size(); i++)
		{
			System.out.println("GGG: " + i);
		System.out.println("r: " + (pop.getChromosome(i).getGene(0).getAllele()));
		System.out.println("r: " + (pop.getChromosome(i).getGene(1).getAllele()));
		System.out.println("r: " + (pop.getChromosome(i).getGene(2).getAllele()));
		}*/
	}
	
	public int getPopulationSize()
	{
		return population.getPopulation().size();
	}
	
	public float[] getColorIn(int index)
	{
		float []col = new float[4];
		
		col[0] = ((Double)population.getPopulation().getChromosome(index).getGene(0).getAllele()).floatValue();
		col[1] = ((Double)population.getPopulation().getChromosome(index).getGene(1).getAllele()).floatValue();
		col[2] = ((Double)population.getPopulation().getChromosome(index).getGene(2).getAllele()).floatValue();
		col[3] = 0.0f;
		
		return col;
	}
	
	public float[] getColorOut(int index)
	{
		float []col = new float[4];
		
		col[0] = ((Double)population.getPopulation().getChromosome(index).getGene(3).getAllele()).floatValue();
		col[1] = ((Double)population.getPopulation().getChromosome(index).getGene(4).getAllele()).floatValue();
		col[2] = ((Double)population.getPopulation().getChromosome(index).getGene(5).getAllele()).floatValue();
		col[3] = 0.0f;
		
		return col;
	}
	
	/*public float getSizeIn(int index)
	{
		return ((Double)population.getPopulation().getChromosome(index).getGene(6).getAllele()).floatValue();
	}
	
	public float getSizeOut(int index)
	{
		return ((Double)population.getPopulation().getChromosome(index).getGene(7).getAllele()).floatValue();
	}*/
	
	public boolean getTopBranch(int index)
	{
		return ((BooleanGene)population.getPopulation().getChromosome(index).getGene(6)).booleanValue();
	}
	
	public boolean getLeftBranch(int index)
	{
		return ((BooleanGene)population.getPopulation().getChromosome(index).getGene(7)).booleanValue();
	}
	
	public boolean getRightBranch(int index)
	{
		return ((BooleanGene)population.getPopulation().getChromosome(index).getGene(8)).booleanValue();
	}
	
	public boolean getFrontBranch(int index)
	{
		return ((BooleanGene)population.getPopulation().getChromosome(index).getGene(9)).booleanValue();
	}
	
	public boolean getBackBranch(int index)
	{
		return ((BooleanGene)population.getPopulation().getChromosome(index).getGene(10)).booleanValue();
	}
	
	/**
	 * Fitness function returns the values that user set in user interface.
	 */
	public class GAFitness extends FitnessFunction
	{
		public double evaluate(IChromosome chr)
		{
			return (Double)chr.getApplicationData();
		}
	}
}
