package edu.gwu.graphics2;

import java.util.List;

public interface Model {

	Model cullBackfacing(Vector camera);

	Model multiplyAll(Matrix mview);
	
	List<Line> toLines();

}
