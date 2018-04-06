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

/**
 *
 * @author murc_cr
 */

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

/**
 * A supplementary function to calculate the length of a continuously differentiable polynomial function.
 *
 * @author Michael Scholz (michael.scholz@dlr.de)
 */
public class CurveLengthFunction implements UnivariateFunction {

    /**
     * The continuously differentiable function.
     */
    PolynomialFunction poly;
    PolynomialFunction polyX;
    PolynomialFunction polyY;
    
    public CurveLengthFunction(PolynomialFunction poly) {
        this.poly = poly;
    }
    
    public CurveLengthFunction(PolynomialFunction polyX, PolynomialFunction polyY) {
        this.polyX = polyX;
        this.polyY = polyY;
    }


    /**
     * Calculates the function's value to be used for later integration.
     *
     * @see <a href="https://de.wikipedia.org/wiki/L%C3%A4nge_%28Mathematik%29#L.C3.A4nge_eines_Funktionsgraphen">Länge
     * eines Funktionsgraphen</a>
     * @param x Input x.
     * @return The function's value.
     */
    public double value(double x) {
        //return Math.sqrt(1 + Math.pow(poly.derivative().value(x), 2));
        return Math.sqrt(Math.pow(polyX.derivative().value(x), 2) + Math.pow(polyY.derivative().value(x), 2));
    }
}
