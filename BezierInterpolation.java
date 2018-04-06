/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import java.util.*;
import java.awt.geom.Point2D;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.CubicCurve2D;

/**
 * Bezier Interpolator.
 * Adapted from :http://devmag.org.za/2011/06/23/bzier-path-algorithms/  
 * Created by Cristhian Murcia 13/11/2017. 
 * */
 
public class BezierInterpolator {
    /**
     * Computes an interpolating function for the data set.
     * @param x the arguments for the control points
     * @param y the values for the control points
     * @return a set of Bezier curves that interpolate the data set
     * @throws DimensionMismatchException if {@code x} and {@code y}
     * have different sizes.
     * @throws NonMonotonicSequenceException if {@code x} is not sorted in
     * strict increasing order.
     * @throws NumberIsTooSmallException if the size of {@code x} is smaller
     * than 3.
     */
   
    public LinkedList interpolate(float x[], float y[], float scale)
        throws DimensionMismatchException,
               NumberIsTooSmallException,
               NonMonotonicSequenceException {
        System.out.println("Starting Bezier Interpolation");
        if (x.length != y.length) {
            throw new DimensionMismatchException(x.length, y.length);
        }

        if (x.length < 3) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS,
                                                x.length, 3, true);
        }

        final int n = x.length;
        final int nBezierCurves = n - 1;
        LinkedList<Point2D> controlPoints = new LinkedList<Point2D>();
        Point2D point;
        for (int i = 0; i<n; i++) {
            // First point
            if (i == 0) {
                
                Vector2 p1 = new Vector2(x[i], y[i]);                            
                Vector2 p2 = new Vector2(x[i + 1], y[i + 1]);                
                Vector2 tangent = p2.cpy().sub(p1).scl(scale);               
                Vector2 q1 = p1.cpy().add(tangent); 
                point = new Point2D.Double ((double) p1.x, (double) p1.y);
                controlPoints.add(point);
                point = new Point2D.Double ((double) q1.x, (double) q1.y);
                controlPoints.add(point);
            }
            else if (i == n - 1) {
                Vector2 p0 = new Vector2(x[i - 1], y[i - 1]);
                Vector2 p1 = new Vector2(x[i], y[i]);
                Vector2 tangent = p1.cpy().sub(p0).scl(scale);
                Vector2 q0 = p1.cpy().sub(tangent);
                point = new Point2D.Double ((double) q0.x, (double) q0.y);
                controlPoints.add(point);
                point = new Point2D.Double ((double) p1.x, (double) p1.y);
                controlPoints.add(point);
            }
            else {
                Vector2 p0 = new Vector2(x[i - 1], y[i - 1]);
                Vector2 p1 = new Vector2(x[i], y[i]);
                Vector2 p2 = new Vector2(x[i + 1], y[i + 1]);
                Vector2 tangent = p2.cpy().sub(p0).nor();
                float factor1 = p1.cpy().sub(p0).len();
                float factor2 = p2.cpy().sub(p1).len();
               
                Vector2 q0 = p1.cpy().sub(tangent.cpy().scl(scale).scl(factor1));
                Vector2 q1 = p1.cpy().add(tangent.cpy().scl(scale).scl(factor2));
                point = new Point2D.Double ((double) q0.x, (double) q0.y);
                controlPoints.add(point);
                point = new Point2D.Double ((double) p1.x, (double) p1.y);
                controlPoints.add(point);
                point = new Point2D.Double ((double) q1.x, (double) q1.y);
                controlPoints.add(point);
            }
        }
        LinkedList<CubicCurve2D> bezierCurves = new LinkedList<CubicCurve2D>();
        //Creating Bezier curves based on the calculated control points
        for (int index = 0; index < nBezierCurves; index++){
            int curveIndex = index * 3;
                    
            // create new CubicCurve2D.Double
            CubicCurve2D bezierCurve = new CubicCurve2D.Double();
            // draw CubicCurve2D.Double with set coordinates
            Point2D.Double p1 = (Point2D.Double) controlPoints.get(curveIndex);
            Point2D.Double ctrlP1 = (Point2D.Double) controlPoints.get(curveIndex + 1);
            Point2D.Double ctrlP2 = (Point2D.Double) controlPoints.get(curveIndex + 2);
            Point2D.Double p2 = (Point2D.Double) controlPoints.get(curveIndex + 3);
            bezierCurve.setCurve(p1, ctrlP1, ctrlP2, p2);   
            bezierCurves.add(bezierCurve);           
        }     
      
        return bezierCurves;
    }  
    public LinkedList interpolate(float x[], float y[]) {
        return interpolate(x, y, 1.0f);
    }
    public LinkedList interpolate(double x[], double y[]) {
        float [] xD = new float[x.length];
        float [] yD = new float[x.length];
         for (int i = 0 ; i < x.length; i++)
        {
            xD[i] = (float) x[i];
            yD[i] = (float) y[i];
        }
        return interpolate(xD, yD, 1.0f);
    }    

}
