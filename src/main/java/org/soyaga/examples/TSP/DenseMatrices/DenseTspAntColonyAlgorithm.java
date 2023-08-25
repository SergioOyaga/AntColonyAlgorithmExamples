package org.soyaga.examples.TSP.DenseMatrices;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.AntColonyAlgorithm.StatsAntColonyAlgorithm;
import org.soyaga.aco.Colony;
import org.soyaga.aco.SolutionConstructorPolicy.SolutionConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.aco.StatsRetrievalPolicy.StatsRetrievalPolicy;
import org.soyaga.aco.StoppingPolicy.StoppingCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.UpdatePheromonePolicy;
import org.soyaga.aco.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class extends StatsAntColonyAlgorithm and specifies the method of result collection, which involves creating a GIF in this case.
 */
public class DenseTspAntColonyAlgorithm extends StatsAntColonyAlgorithm {
    /**
     * This method receives all the parameters necessary to create an object of this class.
     *
     * @param ID                        A string representing the name of the ACO.
     * @param world                     A world object containing information about the "Graph" and "PheromoneContainer."
     * @param colony                    A Colony object containing the ants and the best solution found during optimization.
     * @param stoppingCriteriaPolicy    A StoppingCriteriaPolicy object with the predefined criteria.
     * @param acoInitializer            An ACOInitializer object with the initialization defined.
     * @param solutionConstructorPolicy A SolutionConstructorPolicy object responsible for constructing ants' solutions.
     * @param updatePheromonePolicy     An UpdatePheromonePolicy object that manages the addition and removal of pheromone in the world.
     * @param statsRetrievalPolicy      StatsRetrievalPolicy object with the stats defined.
     */
    public DenseTspAntColonyAlgorithm(String ID, World world, Colony colony,
                                       StoppingCriteriaPolicy stoppingCriteriaPolicy, ACOInitializer acoInitializer,
                                       SolutionConstructorPolicy solutionConstructorPolicy,
                                       UpdatePheromonePolicy updatePheromonePolicy,
                                       StatsRetrievalPolicy statsRetrievalPolicy) {
        super(ID, world, colony, stoppingCriteriaPolicy, acoInitializer, solutionConstructorPolicy,
                updatePheromonePolicy, statsRetrievalPolicy);
    }

    /**
     * This method retrieves the actual result from an optimized solution in a format convenient for the problem.
     * In this case, it returns true, indicating that a GIF image has been created using the images in the ImageStat object.
     *
     * @param resultArgs Additional information required to obtain the results (varargs).
     * @return An object containing the optimization result. In this case, a String with the output path.
     */
    @Override
    public Object getResult(Object... resultArgs) {
        String outputPath = (String) resultArgs[0];
        Stat imageStat = this.getStatsRetrievalPolicy().getStats().stream().filter(stat->stat.getHeader().get(0).equals("ImageSaved")).findAny().get();
        ArrayList<BufferedImage> images = ((ImageStat)imageStat).getColonyImages();
        try {
            GifCreator.createGif(images, outputPath + ".gif", 100);
            ImageIO.write(images.get(images.size() - 1), "png", new File(outputPath + ".png"));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return true;
    }
}
