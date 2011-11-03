package utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class OutilsGeometriques {

	public static double distanceDroitePoint(Point2D.Double p1, Point2D.Double p2, Point2D.Double p) {
		return Line2D.Double.ptLineDist(p1.x, p1.y, p2.x, p2.y, p.x, p.y);
	}

	public static double distanceDroitePointCarre(Point2D.Double p1, Point2D.Double p2, Point2D.Double p) {
		return Line2D.Double.ptLineDistSq(p1.x, p1.y, p2.x, p2.y, p.x, p.y);
	}

	public static double produitScalaire(Point2D.Double pA1, Point2D.Double pA2, Point2D.Double pB1, Point2D.Double pB2) {
		return (pA2.x - pA1.x) * (pB2.x - pB1.x) + (pA2.y - pA1.y) * (pB2.y - pB1.y);
	}
}
