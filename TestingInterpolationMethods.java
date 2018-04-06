/*
 * Copyright 2017 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Main;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.ClampedSplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.densify.Densifier;
import javax.swing.*;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.exception.OutOfRangeException;
import Main.CurveLengthFunction;

/**
 *
 * @author murc_cr
 */
public class Test {

    public static void main(String[] args) throws ParseException {

        double[] x1 = {0, 1, 2, 3};
        double[] y1 = {1, Math.E, Math.pow(Math.E, 2), Math.pow(Math.E, 3)};
        splineInformation(x1, y1, 1,  Math.pow(Math.E, 3));        
        ClampedSplineInterpolator spliner = new ClampedSplineInterpolator();
        PolynomialSplineFunction spline = spliner.interpolate(x1, y1, 1, Math.pow(Math.E, 3));
        
        String [] fileNames = {"RawRoad1.txt", "RawRoad2.txt", "RawRoad3.txt"};
        String path = "D:\\Murcia\\Data\\RoadExercise\\";
        String line1S = "LineString (605705.96156002709176391 5798150.906071444042027, 605704.16247453237883747 5798149.79599741660058498, 605702.89928684465121478 5798148.83903704676777124, 605701.06192293530330062 5798147.53757094405591488, 605700.42343760898802429 5798146.99077399913221598, 605699.7604568328242749 5798146.54233215935528278, 605698.22932024148758501 5798145.66192861925810575, 605696.69818365026731044 5798144.89636032376438379, 605694.86081974068656564 5798143.93939995486289263, 605693.02345583122223616 5798143.0589964147657156, 605690.99469984788447618 5798142.15945366770029068, 605689.17480320087634027 5798141.29747107066214085)";
        String line2S = "LineString (605705.96156002709176391 5798150.906071444042027, 605706.7632226919522509 5798151.35047962982207537, 605707.26793539454229176 5798151.58765967190265656, 605707.77919970185030252 5798151.77194251399487257, 605708.37153330107685179 5798151.86918974947184324, 605709.27097543270792812 5798151.96410471852868795, 605710.2183706141076982 5798151.97054057754576206, 605711.53059043467510492 5798151.89955802354961634, 605712.97736538050230592 5798151.74531651381403208, 605713.87159411562606692 5798151.64494497701525688, 605714.16649437707383186 5798151.59823619201779366, 605714.52661864412948489 5798151.5082726925611496, 605714.9931207406334579 5798151.35117440763860941, 605715.67830105952452868 5798151.07894995529204607, 605716.28140237834304571 5798150.79853086080402136, 605716.9211883369134739 5798150.45094193611294031, 605717.51563024113420397 5798150.07865845039486885, 605717.9988702330738306 5798149.73615807574242353, 605718.40675007354002446 5798149.39600912760943174, 605718.7479076636955142 5798149. 05466148536652327, 605719.17102710111066699 5798148.5696572307497263, 605719.41731342498678714 5798148.26409619394689798, 605719.59370644681621343 5798148.00219441391527653, 605720.04940094507765025 5798147.08317841961979866, 605720.4969890343490988 5798146.1316301915794611, 605721.23655553360003978 5798144.45061424747109413)";
        String line3S = "LineString (605721.23655553360003978 5798144.45061424747109413, 605724.0500190197 5798137.579638796, 605726.86348250601440668 5798130.70866334438323975)";
        line1S = reorderWKT(line1S);
        String [] lines = {line1S, line2S, line3S};
        stringToTextFile (lines,  path, fileNames);        
        Map <String, Object> lineSlopes = getSlopes(lines); 
        double[] slopeFPO = (double[]) lineSlopes.get("FPO");
        double[] slopeFPN = (double[]) lineSlopes.get("FPN"); 
        for(int i = 0; i < slopeFPN.length; i++){
            System.out.println("FPO " + i + " " +slopeFPO[i] + " FPN " + i + " " + slopeFPN[i]);
        }
        String [] interpolatedLines;
        double[] slopeFPO2 = {0, 20, 0, 0};
        double[] slopeFPN2 = {0,-40.44, 0, 0}; 
        PolynomialSplineFunction [] clampedPolys =  buildClampedPolynomials (lines,slopeFPO2, slopeFPN2 );
        interpolatedLines =  testPolinomyals(clampedPolys, lines, 0.01);  
        String [] newfileNames = {"a.txt", "b.txt", "c.txt"};
        stringToTextFile (interpolatedLines,  path, newfileNames);
        
        /*String [] interpolatedLines;
        Map <String, Object> lineSlopes = getSlopes(lines); 
        double[] slopeFPO = (double[]) lineSlopes.get("FPO");
        double[] slopeFPN = (double[]) lineSlopes.get("FPN");        

        PolynomialSplineFunction [] clampedPolys =  buildClampedPolynomials (lines, slopeFPO, slopeFPN);
        System.out.println("Number f clamped polys is " + clampedPolys.length);
        interpolatedLines =  testPolinomyals(clampedPolys, lines, 0.5);  
        String [] newfileNames = {"ClampedRawRoad1.txt", "ClampedRawRoad2.txt", "ClampedRawRoad3.txt"};
        stringToTextFile (interpolatedLines,  path, newfileNames);
        
        clampedPolys =  buildClampedPolynomials (lines);
        System.out.println("Number f clamped polys is " + clampedPolys.length);
        interpolatedLines =  testPolinomyals(clampedPolys, lines, 0.5);  
        String [] newfileNames2 = {"ClampedRawRoad1Z.txt", "ClampedRawRoad2Z.txt", "ClampedRawRoad3Z.txt"};
        stringToTextFile (interpolatedLines,  path, newfileNames2);   
           
        spline = buildClampedPolynomials (line3S);  
        System.out.println("Number of splines : " + spline.getPolynomials().length);
        
        double [] pDistances =  polinomyalDistance (line2S);
        
        double [] lDistances = WKTDistance(line2S);
        System.out.println("Number of P distances : " + pDistances.length);
        
        System.out.println("Number of L distances : " + lDistances.length);
        for(int i = 0; i < pDistances.length; i++) {
            boolean test = pDistances[i] > lDistances[i];
            double difference = pDistances[i] -  lDistances[i];
            System.out.println("Polinomyal Distance is : " + pDistances[i] + " Line Distance is : " + lDistances[i] + "is P distance > than L distance? " + test + " differences "+ difference);           
        }
        System.out.println(line3S);
        System.out.println(line3S); */
        
        double[] x = (double[]) getCoordinatesFromWKT(line1S, false).get("x");
        double[] y = (double[]) getCoordinatesFromWKT(line1S, false).get("y");
        double [] measurement =  splineLengths( x, y);
        
        double [] pDistances2 =  WKTDistance (line1S);
        for (int i = 0; i < measurement.length; i++){
            System.out.println("P" + i + " : " + measurement[i] + " S" + i + " : " + pDistances2[i]);
            
        }
        
        WKTReader reader = new WKTReader();
        Geometry geometry;
        geometry = reader.read(line1S);
        System.out.println("Line Standar length is  "+ geometry.getLength());
        
        double t = 0;
        for (int i = 0; i < measurement.length; i++){
            t += measurement[i];
        }
        System.out.println("Line Polinomyal length is "+ t);
    }
    
    
    public static double [] polinomyalDistance (String line) {
        PolynomialSplineFunction spline = buildClampedPolynomials (line);
        
        double[] x = (double[]) getCoordinatesFromWKT(line, false).get("x");
        double[] y = (double[]) getCoordinatesFromWKT(line, false).get("y");
        //buildWKT(x, y); 
        double polyDistances [] = new double [x.length - 1];
        for(int i = 0; i < x.length - 1; i++){
            double tempX [] = new double [2];
            double tempY [] = new double [2];
            tempX[0] = x[i];
            tempX[1] = x[i + 1];
            tempY [0] = y[i];
            tempY [1] = y[i + 1];
            double dist = tempX[1] - tempX[0];
            double increment = dist/200;
            String line2 = buildWKT(tempX, tempY);
            
            try {            
                WKTReader reader = new WKTReader();                
                Geometry geometry;            
                geometry = reader.read(line2);
                Densifier d = new Densifier(geometry);
                geometry = d.densify(geometry, increment);             
                Map <String, Object> coordinatesLine = getCoordinatesFromWKT(geometry.toString(), false);
                double[] newX = (double[]) coordinatesLine.get("x");
                double[] newY = new double [newX.length];            
                for (int j= 0; j < newX.length; j++) {
                   newY[j] = spline.value(newX[j]);
                }   
                //Critical New X and New Y
                double [] distances = computeDistances (newX, newY);
                double total = totalDistance(distances);
                polyDistances [i] = total;              
                              
            } catch (ParseException ex) {
                Logger.getLogger(Painter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return polyDistances ;
    }
    
    public static double [] WKTDistance(String line) {
        double[] x = (double[]) getCoordinatesFromWKT(line, false).get("x");
        double[] y = (double[]) getCoordinatesFromWKT(line, false).get("y");        
        double [] distances = computeDistances (x, y);
        return distances;
    }    
    
    
    public static double [] computeDistances (double [] x, double []y) {
        int n = x.length;
        double [] distances = new double [n - 1];
        for (int i = 0; i < n - 1; i++) {
            double internal = Math.pow(x[i] - x[i + 1], 2) + Math.pow(y[i] - y[i + 1], 2); 
            distances [i] = Math.pow(internal, 0.5);
        }        
        return distances;
    }
    
    public static double totalDistance (double [] distances) {
        double total = 0;
        for (int i = 0; i < distances.length; i++) {
            total += distances[i];
        }
        return total;
    }
    
    
    
    public static double distance(double [] x, double [] y){
        double internal = Math.pow(x[1] - x[0], 2) + Math.pow(y[1] - y[0], 2);
        return Math.pow(internal, 0.5);
    }
    
    public static void stringToTextFile (String content, String path, String fileName) {
        path+=fileName;
        String updatedText = "Geom\n";
        updatedText+=content;        
        updatedText = updatedText.replaceAll("\n", System.lineSeparator());
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(updatedText);
            // Always close files.
            bufferedWriter.close();

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }        
    } 
    
    public static void stringToTextFile (String [] content, String path, String [] fileNames) {
        for (int i = 0; i<content.length; i++) {            
            System.out.println("Writing file " + fileNames[i]);
            stringToTextFile (content[i], path, fileNames[i]);
        }
    }
    
    public static void splineInformation(double[] x, double[] y, double FPO, double FPN) {
        ClampedSplineInterpolator spliner = new ClampedSplineInterpolator();
        PolynomialSplineFunction spline = spliner.interpolate(x, y, FPO, FPN);
        System.out.println("This Spline is conformed by : " + spline.getPolynomials().length + " Polynomials");
        System.out.println("The coefficients are : " );
        for (int i = 0; i<spline.getPolynomials().length; i++){
            System.out.println("P " + i + " Information "); 
            double coeff [] = spline.getPolynomials()[i].getCoefficients();
            String [] names = {"a", "b", "c", "d"};
            for (int j = 0; j < coeff.length; j++) {
                System.out.println(names[j] + " : " + coeff[j]);
            }
        }
    }
    
    public static void splineInformation(double[] x, double[] y) {
        ClampedSplineInterpolator spliner = new ClampedSplineInterpolator();
        PolynomialSplineFunction spline = spliner.interpolate(x, y);
        System.out.println("This Spline is conformed by : " + spline.getPolynomials().length + " Polynomials");
        System.out.println("The coefficients are : " );
        for (int i = 0; i<spline.getPolynomials().length; i++){
            System.out.println("P " + i + " Information "); 
            double coeff [] = spline.getPolynomials()[i].getCoefficients();
            String [] names = {"a", "b", "c", "d"};
            for (int j = 0; j < coeff.length; j++) {
                System.out.println(names[j] + " : " + coeff[j]);
            }
        }
    }
    
    
    public static String reorderWKT(String WKT){
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry;
            geometry = reader.read(WKT);
            Coordinate[] geometryCoordinates = geometry.getCoordinates();
            if (geometryCoordinates[0].x > geometryCoordinates[1].x) {
                geometry = geometry.reverse();
            }
            return geometry.toString();
        } catch (ParseException ex) {
            Logger.getLogger(Painter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public static  Map <String, Object> getSlopes(String [] lines) {
        Map <String, Object> slopes = new HashMap<String, Object>();
        int n = lines.length;
        double FPO [] = new double [n];
        double FPN [] = new double [n];
        
        FPO[0] = 0;
        FPN[n - 1] = 0;        
        
        for (int i = 1; i < n; i++){
            
            double[] x = (double[]) getCoordinatesFromWKT(lines[i], false).get("x");
            double[] y = (double[]) getCoordinatesFromWKT(lines[i], false).get("y");
            double slope = (y[1] - y[0])/(x[1] - x[0]);
            FPO[i] = slope;
            FPN[i - 1] =  slope;
        }
        slopes.put("FPO", FPO);
        slopes.put("FPN", FPN);
        return slopes;
    }
    public static Map <String, Object> getCoordinatesFromWKT(String wktGeometry, boolean information) {
        //Read a WKT string and returns its coordinates
        Map<String, Object> coordinates = new HashMap<String, Object>();
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry;
            geometry = reader.read(wktGeometry);
            Coordinate[] geometryCoordinates = geometry.getCoordinates();
            if (geometryCoordinates[0].x > geometryCoordinates[1].x) {
                geometry = geometry.reverse();
            }
            geometryCoordinates = geometry.getCoordinates();
            
            double [] x = new double [geometryCoordinates.length];
            double [] y = new double [geometryCoordinates.length];            
            for(int i = 0; i<x.length; i++) {
                x[i] = geometryCoordinates[i].x;
                y[i] = geometryCoordinates[i].y;
            }
            coordinates.put("x", x);
            coordinates.put("y", y);             
            if (information) 
                showGeometryInfo(geometry);            
            return coordinates;
        } catch (ParseException ex) {
            Logger.getLogger(Painter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return coordinates;
    }
    
    public static PolynomialSplineFunction [] buildClampedPolynomials (String [] lines, double[] FPO, double[] FPN) {
        int n = lines.length;
        PolynomialSplineFunction [] clampedPolinomyals = new PolynomialSplineFunction[n];
        for (int i = 0; i < n; i++){
            double[] x = (double[]) getCoordinatesFromWKT(lines[i], false).get("x");
            double[] y = (double[]) getCoordinatesFromWKT(lines[i], false).get("y");
            ClampedSplineInterpolator spliner = new ClampedSplineInterpolator();
            PolynomialSplineFunction spline = spliner.interpolate(x, y, FPO[i], FPN[i]);
            clampedPolinomyals[i] = spline;
        }
        return clampedPolinomyals;
    }
    
    public static PolynomialSplineFunction [] buildClampedPolynomials (String [] lines) {
        int n = lines.length;
        PolynomialSplineFunction [] clampedPolinomyals = new PolynomialSplineFunction[n];
        for (int i = 0; i < n; i++){
            double[] x = (double[]) getCoordinatesFromWKT(lines[i], false).get("x");
            double[] y = (double[]) getCoordinatesFromWKT(lines[i], false).get("y");
            ClampedSplineInterpolator spliner = new ClampedSplineInterpolator();
            PolynomialSplineFunction spline = spliner.interpolate(x, y);
            clampedPolinomyals[i] = spline;
        }
        return clampedPolinomyals;
    }
    
    public static PolynomialSplineFunction buildClampedPolynomials (String line) {      
        double[] x = (double[]) getCoordinatesFromWKT(line, false).get("x");
        double[] y = (double[]) getCoordinatesFromWKT(line, false).get("y");
        ClampedSplineInterpolator spliner = new ClampedSplineInterpolator();
        PolynomialSplineFunction spline = spliner.interpolate(x, y);       
        return spline;
    }
    
    public static String buildWKT(double [] x, double [] y) {
        //"LineString (605705.96156002709176391 5798150.906071444042027,
        String line = "LineString (";
        for (int i = 0; i < x.length; i++) {
            if (i == x.length - 1) {
                line += String.valueOf(x[i]) + " " + String.valueOf(y[i]) + ")";
            } else {
                line += String.valueOf(x[i]) + " " + String.valueOf(y[i]) + ", ";   
            }                    
        }
        return line;
    }
    
    
    
    //Takes as input the polinomyal and the line that passes through that polinomyal
    //Computes interpolations according to the provided distance
    //Return a WKT with interpolated measurements
    public static String testPolinomyal(PolynomialSplineFunction spline, String line, double densification) {
        String InterpolatedLine ="LINESTRING (";
        try {            
            WKTReader reader = new WKTReader();
            WKTWriter writer = new WKTWriter();
            Geometry geometry;            
            geometry = reader.read(line);
            Densifier d = new Densifier(geometry);
            geometry = d.densify(geometry, densification);             
            Map <String, Object> coordinatesLine = getCoordinatesFromWKT(geometry.toString(), false);
            double[] x = (double[]) coordinatesLine.get("x");
            double[] y = (double[]) coordinatesLine.get("y");
            
            for (int i = 0; i < x.length; i++) {
                //y[i] = spline.value(x[i]);
                if (i< x.length - 1){
                   InterpolatedLine+= x[i] + " " + y[i] + ", "; 
                } else {
                   InterpolatedLine +=  x[i] + " " + y[i] + ")";
                }                
            }            
            return InterpolatedLine;
            
        } catch (ParseException ex) {
            Logger.getLogger(Painter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return InterpolatedLine;
    }
    
    public static String [] testPolinomyals(PolynomialSplineFunction [] splines, String [] lines, double densification){
        int n = splines.length;
        String [] interpolatedLines = new String [n];
        for (int i = 0; i < n; i++) {
            interpolatedLines[i] = testPolinomyal(splines[i], lines[i], densification);
        }
        return interpolatedLines;
    }
    
    public static void splineInformation(PolynomialSplineFunction spline) {
        PolynomialFunction[] polynomials = spline.getPolynomials();
        System.out.println("Number of polynomials is " + polynomials.length);
        for (int i = 0; i < polynomials.length; i++) {
            System.out.println("Coefficients of polinomyal number " + i + " are: ");
            double[] coefficients = polynomials[i].getCoefficients();
            for (int j = 0; j < 4; j++) {
                System.out.println(coefficients[j]);
            }
        }
    }
    
    public static void showGeometryInfo(Geometry geometry) {               
        Coordinate[] coordinates = geometry.getCoordinates();       
        for (int i = 0; i < coordinates.length; i++) {
            System.out.println("Coordinate number " + i + coordinates[i]);
        }
    }
    public static double curveLength(PolynomialFunction polynomial, double intervalMin, double intervalMax) {
        CurveLengthFunction clf = new CurveLengthFunction(polynomial);
        RombergIntegrator ri = new RombergIntegrator();
        return ri.integrate(RombergIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, clf, intervalMin, intervalMax);
    }

    
    public static double [] splineLengths(double [] x, double [] y) {
        PolynomialSplineFunction spline = fitSpline(x, y);
        PolynomialFunction [] polynomials = spline.getPolynomials();
        double[] knots = spline.getKnots();
        int n = knots.length - 1;
        double[] m  = new double [n];        
        for (int i = 0; i < n; i++){
            double intervalMin = knots[i];
            double intervalMax = knots[i + 1];
            //PolynomialFunction currentPolinomyal = intersectingPolinomial(intervalMin, knots, spline);
            
            //m [i] =  curveLength(polynomials[i], intervalMin, intervalMax);
            m [i] =  curveLength(polynomials[i], 0, 1);   
        }
        return m;
    } 
    
    public static PolynomialFunction intersectingPolinomial(double v, double[] knots,PolynomialSplineFunction spline) {
        
        int n = knots.length - 1;
        PolynomialFunction [] polynomials = spline.getPolynomials();
        if (v < knots[0] || v > knots[n]) {
            throw new OutOfRangeException(v, knots[0], knots[n]);
        }
        int i = Arrays.binarySearch(knots, v);
        if (i < 0) {
            i = -i - 2;
        }
        // This will handle the case where v is the last knot value
        // There are only n-1 polynomials, so if v is the last knot
        // then we will use the last polynomial to calculate the value.
        if ( i >= polynomials.length ) {
            i--;
        }
        return polynomials[i];
    }
    
    public static PolynomialSplineFunction fitSpline(double[] x, double[] y) {
        SplineInterpolator spliner = new SplineInterpolator();
        PolynomialSplineFunction spline = spliner.interpolate(x, y);
        return spline;
    }    
}
