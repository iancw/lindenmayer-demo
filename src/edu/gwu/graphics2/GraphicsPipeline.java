/**
 * 
 */
package edu.gwu.graphics2;

import java.awt.Dimension;
import java.util.logging.Logger;


/**
 * This class contains the logic that converts camera properties into transform
 * matrices and invokes those in appropriate order on a Model.  The primary
 * method of interest is renderModel, which applies the view and perspective
 * transformations and executes culling.
 * 
 * The calculation of U, V, and N based on camera position, reference position,
 * and up vector occurs in the constructor.
 * 
 * The remaining get methods are used to populate the gui initially.
 * 
 * @author ian
 *
 */
public class GraphicsPipeline
{
	private final static Logger logger = Logger.getLogger(GraphicsPipeline.class.getName());
	
	private Vector U;
	private Vector V;
	private Vector N;
	private Vector camera;
	private Light light;
	private Vector reference;
	private Vector up;
	private float h;
	private float d;
	private float f;
	
	private Matrix Mview;
	private Matrix Mpers;
	private Matrix Mscreen;
	
	private Dimension dimension;
	
	public GraphicsPipeline(LabSettings settings, Dimension dim)
	{
		setDimension(dim);
		updateSettings(settings);
	}
	
	public void setDimension(Dimension dim)
	{
		this.dimension = dim;
		Mscreen = new Matrix(new float[][]{
				{dimension.width/2, 0, 0, dimension.width/2},
				{0, -dimension.height/2, 0, dimension.height/2},
				{0, 0, 1, 0},
				{0, 0, 0, 1}});
	}
	
	public void updateSettings(LabSettings settings)
	{
		this.camera = settings.getCamera();
		this.light = settings.getLight();
		this.reference = settings.getReference();
		this.up = settings.getUp();
		this.h = settings.getH();
		this.d = settings.getD();
		this.f = settings.getF();

		Vector refTrans = reference.subtract(camera);
		this.N = refTrans.scale(1/refTrans.magnitude());
		Vector prescaledU = this.N.cross(up);
		this.U = prescaledU.scale(1/prescaledU.magnitude());
		this.V = this.U.cross(this.N);
		
		Mview = Matrix.fromUVN(U, V, N).multiply(Matrix.fromTranslationVector(camera));
		Mpers = Matrix.fromHDF(h, d, f);
	}
	
	public Vector getCamera(){ return camera; }
	public Light getLight(){ return light; }
	public Vector getUp(){ return up; }
	public Vector getReference(){ return reference; }
	public float getD(){ return d; }
	public float getH(){ return h; }
	public float getF(){ return f; }
	
	
	/**
	 * This is the method where all the transformation are applied and where culling
	 * occurs.  
	 * @param model the model to render
	 * @return the resulting model after all transforms and culling, this returned
	 * model exists in screen space
	 */
	public Model renderModel(Model model)
	{
		model = model.cullBackfacing(camera);
		Model view = model.multiplyAll(Mview);
		Model pers = view.multiplyAll(Mpers);
		//logger.info(Mpers.toString());
		//logger.info("Range of z in perspective space is ["+pers.findMinZ()+","+pers.findMaxZ()+"]");
		Model screen = pers.multiplyAll(Mscreen);
		//logger.info("Range of z in perspective space is ["+screen.findMinZ()+","+screen.findMaxZ()+"]");
		return screen;
	}
	
	public Vector transformVector(Vector v)
	{
		return Mscreen.multiply(Mpers.multiply(Mview.multiply(v)));
	}
	
}
