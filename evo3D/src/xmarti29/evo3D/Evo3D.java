package xmarti29.evo3D;

import com.trolltech.qt.gui.*;

/**
 * Evo3D evolves simple fractal-like 3D object based on user defined
 * fitness values.
 */
public class Evo3D extends QMainWindow
{
    Ui_Evo3D ui = new Ui_Evo3D();
    
    PaintWidget paintWidget = null;
    
    PaintWidget paintThumb[] = new PaintWidget[5];
    
    Workspace workspace = new Workspace();
    
    Workspace workThumb[] = new Workspace[5];
    
    GA ga = new GA();
    
    double [] fitValues = {1.0, 1.0, 1.0, 1.0, 1.0};
    
    int maxLevel = 2;

    public static void main(String[] args)
    {
        QApplication.initialize(args);

        Evo3D testEvo3D = new Evo3D();
        testEvo3D.show();

        QApplication.exec();
    }

    public Evo3D() {
        ui.setupUi(this);
        
        init();
    }

    public Evo3D(QWidget parent) {
        super(parent);
        ui.setupUi(this);
        
        init();
    }
    
    private void init()
    {
    	ui.actionFileExit.triggered.connect(this, "close()");
    	
    	ui.actionThumbnails.triggered.connect(this, "showThumbnails()");
    	ui.actionEvolution.triggered.connect(this, "showEvolution()");
    	ui.actionConfiguration.triggered.connect(this, "showConfiguration()");
    	
    	ui.actionAboutQt.triggered.connect(QApplication.instance(), "aboutQt()");
    	ui.actionAboutQtJambi.triggered.connect(QApplication.instance(), "aboutQtJambi()");
    	
    	ui.btnEvolve.clicked.connect(this, "evolve()");

    	ga.evolve(fitValues);
    	
    	paintWidget =  new PaintWidget(this, -1, ga);
    	
    	setCentralWidget(paintWidget);
    	
    	QWidget wi = new QWidget();
    	
    	QHBoxLayout lay = new QHBoxLayout();
    	for (int i = 0; i < 5; i++)
    	{
    		paintThumb[i] = new PaintWidget(this, i, ga);
    		
    		lay.addWidget(paintThumb[i]);
    		paintThumb[i].show();
    		
    		workThumb[i] = new Workspace();
    		
    		workThumb[i].setMaxLevels(ui.spinLevels.value());
    		workThumb[i].setBranches(ui.spinCurLev.value()-1,
    								 ga.getTopBranch(i),
    								 ga.getLeftBranch(i),
    								 ga.getRightBranch(i),
    								 ga.getFrontBranch(i),
    								 ga.getBackBranch(i));
    		workThumb[i].setColors(ga.getColorIn(i), ga.getColorOut(i));
    		//workThumb[i].setSizes(ga.getSizeIn(i), ga.getSizeOut(i));
    		workThumb[i].clearFractal();
    		workThumb[i].createFractal(0.0f, 0.0f, 0.0f, 1);
    		
    		paintThumb[i].setWorkspace(workThumb[i]);
    	}
    	wi.setLayout(lay);
    	ui.dockThumbs.setWidget(wi);
    	
    	workspace.setMaxLevels(ui.spinLevels.value());
    	workspace.setBranches(ui.spinCurLev.value()-1,
    						  ga.getTopBranch(0),
    						  ga.getLeftBranch(0),
    						  ga.getRightBranch(0),
    						  ga.getFrontBranch(0),
    						  ga.getBackBranch(0));
    	workspace.setColors(ga.getColorIn(0), ga.getColorOut(0));
    	//workspace.setSizes(ga.getSizeIn(0), ga.getSizeOut(0));
    	workspace.createFractal(0.0f, 0.0f, 0.0f, 1);
    	paintWidget.setWorkspace(workspace);
    }
    
    public void showThumbnails()
    {
    	ui.dockThumbs.show();
    }
    
    public void showEvolution()
    {
    	ui.dockEvolution.show();
    }
    
    public void showConfiguration()
    {
    	ui.dockConfig.show();
    }
    
    /**
     * This method gets called when user wants a bigger view of a thumbnail
     * (clicks it with middle button).
     */
    public void updateWorkspace(int idx)
    {
    	workspace.setMaxLevels(workThumb[idx].getMaxLevels());
    	workspace.setBranchesArr(workThumb[idx].getTop(),
    							 workThumb[idx].getLeft(),
    							 workThumb[idx].getRight(),
    							 workThumb[idx].getFront(),
    							 workThumb[idx].getBack());
    	workspace.setColors(workThumb[idx].getColorIn(), workThumb[idx].getColorOut());
    	//workspace.setSizes(workThumb[idx].getSizeIn(), workThumb[idx].getSizeOut());
    	workspace.clearFractal();
    	workspace.createFractal(0.0f, 0.0f, 0.0f, 1);
    	
    	paintWidget.update();
    }
    
    protected void evolve()
    {
    	fitValues[0] = ui.spin0.value();
    	fitValues[1] = ui.spin1.value();
    	fitValues[2] = ui.spin2.value();
    	fitValues[3] = ui.spin3.value();
    	fitValues[4] = ui.spin4.value();
    	
    	maxLevel = ui.spinLevels.value();
    	
    	ga.evolve(fitValues);
    	
    	for (int i = 0; i < 5; i++)
    	{
    		workThumb[i].setMaxLevels(ui.spinLevels.value());
    		workThumb[i].setBranches(ui.spinCurLev.value()-1,
					 				 ga.getTopBranch(i),
					 				 ga.getLeftBranch(i),
					 				 ga.getRightBranch(i),
					 				 ga.getFrontBranch(i),
					 				 ga.getBackBranch(i));
    		workThumb[i].setColors(ga.getColorIn(i), ga.getColorOut(i));
    		workThumb[i].setColors(ga.getColorIn(i), ga.getColorOut(i));
    		//workThumb[i].setSizes(ga.getSizeIn(i), ga.getSizeOut(i));
    		workThumb[i].clearFractal();
    		workThumb[i].createFractal(0.0f, 0.0f, 0.0f, 1);
    		
    		paintThumb[i].update();
    	}
    }
}
