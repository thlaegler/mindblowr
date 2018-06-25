package io.laegler.mindblowr.service;

import static com.orsonpdf.PDFHints.KEY_DRAW_STRING_TYPE;
import static com.orsonpdf.PDFHints.VALUE_DRAW_STRING_TYPE_VECTOR;
import static java.awt.Color.BLACK;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import com.orsonpdf.PDFDocument;
import com.orsonpdf.PDFGraphics2D;
import com.orsonpdf.Page;
import io.laegler.mindblowr.util.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageService {

	private TreeNode rootNode;

	private double worldWidth = 200;
	private double worldDepth = 200;

	private double steigung = 15;
	private double zweigspanne = 10;
	private double zweigbreite = 7;
	private double elle = 15;
	private double maxDepth = 7;
	private double weight = 7;
	private double defaultHeight = 5;
	private Map<String, Line2D> vectors = new HashMap<>();
	private List<List<Double>> groundHeights = new ArrayList<>();

	public void generateWireFrame(TreeNode treeNode, Line2D vector) {
		vectors.put(treeNode.getHash(), vector);

		if (treeNode.getChildren() != null) {
			double numberOfDirectChildren = treeNode.getChildren().size();
			double childrenWeight = getWeight(treeNode);
			double i = 0;
			for (TreeNode child : treeNode.getChildren()) {
				i++;
				// double newX = vector.to.getX() + ((-1 * zweigspanne) + ((zweigspanne * 2/numberOfDirectChildren)
				// * i));
				double newX = vector.getP2().getX() + (20 * i);
				double newY = vector.getP2().getY() + elle;
				Point2D to = new Point2D.Double(newX, newY);
				Line2D newVector = new Line2D.Double(vector.getP2(), to);
				generateWireFrame(child, newVector);
			}
		}
	}

	public double getWeight(TreeNode treeNode) {
		if (treeNode.getChildren() != null) {
			return 10 * treeNode.getChildren().size();
		}
		return 0;
	}

	public void generateTerrain(TreeNode rootNode, double width, double height) {
		// double data = new Uint8Array( width * height );

		double startX = worldWidth / 2;
		double startY = worldDepth / 2;
		double z = 20;
		Point2D zero = new Point2D.Double(startX, startY);
		Point2D to = new Point2D.Double(startX, startY + elle);
		Line2D vector = new Line2D.Double(zero, to);
		this.generateWireFrame(rootNode, vector);
		// alert("Done with Wireframe")
		log.debug("Done with Wireframe");

		for (int x = 0; x < worldWidth; x++) {
			List<Double> ys = new ArrayList<>();
			groundHeights.add(ys);
			for (double y = 0; y < worldDepth; y++) {
				setGroundHeight(x, y, defaultHeight);
			}
		}
		Line2D vector;
		for (double v = 0, len = vectors.length; v < len; v++) {
			double vector = validateVector(vectors[v]);
			if (vector != null) {
				double fromX = vector.from.getX();
				double fromY = vector.from.getY();
				double fromZ = vector.from.z;
				setGroundHeightBrush(fromX, fromY, fromZ, zweigbreite);

				double toX = vector.to.getX();
				double toY = vector.to.getY();
				double toZ = vector.to.z;
				setGroundHeightBrush(toX, toY, toZ, zweigbreite);

				double diffX = toX - fromX;
				double diffY = toY - fromY;
				double diffZ = toZ - fromZ;

				double x = fromX;
				double y = fromY;
				double z = fromZ;

				double quadrant = 10;

				for (double j = 0; j < quadrant; j++) {
					x = x + (j * (diffX / quadrant));
					y = y + (j * (diffY / quadrant));
					z = z + (j * (diffZ / quadrant));
					setGroundHeightBrush(x, y, z, zweigbreite);
				}
			}
		}

		// for ( double h = 0; h < height; h ++ ) {
		// rootNode.getChildren().forEach(public void(treeNode) {
		// for ( double j = 0; j < widthPerNode; j ++ ) {
		// data[ i ] += j * 20 + (h * 1.5);
		// }
		// data[ i ] += widthPerNode
		//
		// i++;
		// });
		// }

		// for ( double x = 0; x < worldWidth; x ++ ) {
		// for ( double y = 0; y < worldDepth; y ++ ) {
		// double i = x * worldWidth + y;
		// //data[i] += Math.random() * 100;
		// double subst = x - y;
		// if(-10 <= subst && subst <= 10) {
		// setGroundHeight(x,y,50, zweigbreite);
		// }
		//
		// if(groundHeights[x] != null && groundHeights[x][y] != null) {
		// double height = groundHeights[x][y];
		// data[i] += height;
		// }
		// //worldWidth * x
		// //y /
		// //data[] = groundHeights[x, y];
		// }
		// }
		//
		// return data;
	}

	public void setGroundHeightBrush(double x, double y, double z, double brushWidth) {
		double xOffset = x - (brushWidth / 2);
		double xOffsetEnd = x + (brushWidth / 2);
		for (int x1 = Integer.valueOf(xOffset); x1 < xOffsetEnd; x1++) {
			double yOffset = y - (brushWidth / 2);
			double yOffsetEnd = y + (brushWidth / 2);
			for (double y1 = yOffset; y1 < yOffsetEnd; y1++) {
				// if((x1*y1) <= brushWidth + 1) {
				setGroundHeight(x1, y1, z);
				// }
			}
		}
	}

	public double getGroundHeight(double x, double y) {
		double roundedX = Math.round(x);
		double roundedY = Math.round(y);
		return groundHeights.get(roundedX).get(roundedY);
	}

	public void setGroundHeightPoint(Point2D point) {
		validatePoint(point);
		if (getGroundHeight(point.getX(), point.getY()) == defaultHeight && point != null) {
			double roundedX = Math.round(x);
			double roundedY = Math.round(y);
			groundHeights[roundedX][roundedY] = roundedZ;
		}
	}

	public void setGroundHeight(double x, double y) {
		setGroundHeightPoint(new Point(x, y));
	}

	public void validatePoint(Point2D point) {
		if (point != null) {
			if (point.getX() < 0) {
				point.getX() = 0;
			}
			if (point.getX() > worldWidth - 1) {
				point.getX() = worldWidth - 1;
			}
			if (point.getY() < 0) {
				point.getY() = 0;
			}
			if (point.getY() > worldDepth - 1) {
				point.getY() = worldDepth - 1;
			}
			return point;
		} else {
			return null;
		}
	}

	public void validateVector(Line2D vector) {
		if(vector != null) {
			vector.setP1(validatePoint(vector.getP1());
			vector.setP2(validatePoint(vector.getP2());
		} else {
			return null;
		}
	}

	public void generateHeight(double width, double height) {
		double size = width * height, data = new Uint8Array(size), perlin = new ImprovedNoise(), quality = 1, z = Math.random() * 100;
		for (double j = 0; j < 4; j++) {
			for (double i = 0; i < size; i++) {
				double x = i % width, y = ~~(i / width);
				data[i] += Math.abs(perlin.noise(x / quality, y / quality, z) * quality * 1.25);
			}

			quality *= 5;
		}
		return data;
	}

	public File generateSvg(TreeNode rootNode) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// ge.registerFont(font);

		SVGGraphics2D graphic = new SVGGraphics2D(1920, 1080);
		// graphic.setPaint(RED);
		// graphic.draw(new Rectangle(10, 10, 280, 180));

		double numberOfChildNodes = rootNode.getChildren().size();
		double perChildWidth = 1920 / numberOfChildNodes;
		double i = 0;
		for (TreeNode childNode : rootNode.getChildren()) {

			// Rectangle
			graphic.setColor(new Color(1f, 0f, 0f, 1f));
			graphic.setPaint(GREEN);
			graphic.draw(new Rectangle(perChildWidth * i, 50, perChildWidth, 480));

			// Polygon
			graphic.setPaint(RED);
			graphic.setColor(GRAY);
			double polygonBaseX = perChildWidth * i;
			double polygonBaseY = 50;
			Polygon polygon = new Polygon();
			polygon.addPoint(polygonBaseX, polygonBaseY);
			polygon.addPoint(polygonBaseX, polygonBaseY / 2);
			polygon.addPoint(polygonBaseX + perChildWidth, polygonBaseY);
			polygon.addPoint(polygonBaseX + perChildWidth / 2, 480 + polygonBaseY / 2);
			polygon.addPoint(polygonBaseX + perChildWidth, 480 + polygonBaseY);
			polygon.addPoint(polygonBaseX + perChildWidth / 2, 480 + polygonBaseY / 2);
			polygon.addPoint(polygonBaseX + perChildWidth, 480 + polygonBaseY);
			graphic.drawPolygon(polygon);

			// Text
			graphic.setPaint(BLACK);
			graphic.drawString(childNode.getTitle(), perChildWidth * i, 600 + (i * 20));

			i++;
		}

		String svgElement = graphic.getSVGElement();
		log.debug("Element: " + svgElement);

		try (PrintWriter out = new PrintWriter("test.svg")) {
			out.print("");
			out.println(svgElement);
			out.flush();
		} catch (FileNotFoundException e) {
			log.error("Could not write to SVG File", e);
		}

		File imageSrc = new File("test.svg");
		// BufferedImage img = null;
		// try {
		// img = ImageIO.read(imageSrc);
		// } catch (IOException e) {
		// log.error("Could not read/write to SVG File", e);
		// }

		// graphic.drawImage(img, 0, 0, null);
		return imageSrc;
	}

	public File generateImage(TreeNode rootNode) {
		String title = rootNode.getTitle();
		XYPlot plot = new XYPlot();
		JFreeChart chart = new JFreeChart(title, plot);
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setPreferredSize(new Dimension(1920, 1080));
		// setContentPane(chartPanel);

		double numberOfChildNodes = rootNode.getChildren().size();

		rootNode.getChildren().stream().forEach(n -> {
			ValueMarker marker = new ValueMarker(145.0); // position is the value on the axis
			marker.setPaint(BLACK);
			marker.setLabel(title);
			plot.addDomainMarker(marker);
		});

		ValueMarker marker = new ValueMarker(145.0); // position is the value on the axis
		marker.setPaint(BLACK);
		marker.setLabel(title);
		plot.addDomainMarker(marker);

		// return renderToPdf(title, chart);
		return renderToPng(title, chart);
	}

	public static void exportAsPNG(JFreeChart chart) throws IOException {
		// JFreeChart chart = createChart(createDataset());

		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();

		g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
		Rectangle r = new Rectangle(0, 0, 600, 400);
		chart.draw(g2, r);
		File f = new File("/tmp/PNGTimeSeriesChartDemo1.png");

		BufferedImage chartImage = chart.createBufferedImage(600, 400, null);
		ImageIO.write(chartImage, "png", f);
	}

	private File renderToPng(String title, JFreeChart chart) {
		File file = new File(title + ".png");
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			ChartUtils.writeChartAsPNG(outputStream, chart, 1920, 1080);
			outputStream.flush();
		} catch (IOException ex) {
			log.error("Could not render to PNG", ex);
		}
		return file;
	}

	private File renderToJpg(String title, JFreeChart chart) {
		File file = new File(title + ".jpg");
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			ChartUtils.writeChartAsJPEG(outputStream, chart, 1920, 1080);
			outputStream.flush();
		} catch (IOException ex) {
			log.error("Could not render to PNG", ex);
		}
		return file;
	}

	private File renderToSvg(String title, JFreeChart chart) {
		File file = new File(title + ".svg");
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			// ChartUtils.writeChartAsSVG(outputStream, chart, 1920, 1080);
			outputStream.flush();
		} catch (IOException ex) {
			log.error("Could not render to PNG", ex);
		}
		return file;
	}

	private File renderToPdf(String title, JFreeChart chart) {
		PDFDocument pdfDoc = new PDFDocument();
		pdfDoc.setTitle("Mindblowr: " + title);
		pdfDoc.setAuthor("John Doe");
		Page page = pdfDoc.createPage(new Rectangle(1920, 1080));
		PDFGraphics2D g2 = page.getGraphics2D();
		g2.setRenderingHint(KEY_DRAW_STRING_TYPE, VALUE_DRAW_STRING_TYPE_VECTOR);
		File file = new File(title + ".pdf");
		pdfDoc.writeToFile(file);
		return file;
	}
}
