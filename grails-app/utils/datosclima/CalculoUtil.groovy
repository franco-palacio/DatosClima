package datosclima

import java.text.DecimalFormat

/**
 * Created by Franco on 28/12/2016.
 */
class CalculoUtil {

    def obtieneCoordenadasX(int grados, int radio) {
        float coordenada
        try {
            DecimalFormat formato = new DecimalFormat("0.00");
            double angulo = Math.toRadians(new Double(grados))
            double cosAngulo = Math.cos(angulo)
            String cosAng = formato.format(cosAngulo)
            coordenada = radio * formato.parse(cosAng)
            coordenada
        } catch (Exception e) {
            coordenada
        }
    }

    def obtieneCoordenadasY(int grados, int radio) {
        float coordenada
        try {
            DecimalFormat formato = new DecimalFormat("0.00");
            double angulo = Math.toRadians(new Double(grados))
            double senAngulo = Math.sin(angulo)
            String senAng = formato.format(senAngulo)
            coordenada = radio * formato.parse(senAng)
            coordenada
        } catch (Exception e) {
            coordenada
        }
    }

    def obtienePendiente(float coordenadaX1, float coordenadaX2, float coordenadaY1, float coordenadaY2) {
        float pendiente
        try {
            float denominador = coordenadaX2 - coordenadaX1
            float numerador = coordenadaY2 - coordenadaY1
            pendiente = numerador/denominador
            if (pendiente < 0) {
                pendiente = pendiente * (-1)
            }
            pendiente
        } catch (Exception e) {
            pendiente
        }
    }

    def obtieneArea(float coordenadaX1, float coordenadaX2, float coordenadaX3, float coordenadaY1, float coordenadaY2, float coordenadaY3) {
        float area
        try {
            float primerSumando = (coordenadaX1 * coordenadaY2) + (coordenadaX2 * coordenadaY3) + (coordenadaX3 * coordenadaY1)
            float segundoSumando = (coordenadaX2 * coordenadaY1) + (coordenadaX3 * coordenadaY2) + (coordenadaX1 * coordenadaY3)
            float resultado = primerSumando - segundoSumando
            area = resultado/2
            if (area < 0) {
                area = area * (-1)
            }
            area
        } catch (Exception e) {
            area
        }
    }

    def contieneSol(float coordenadaX1, float coordenadaX2, float coordenadaX3, float coordenadaY1, float coordenadaY2, float coordenadaY3) {
        boolean contiene = false
        try {
//            float primerSumando = (coordenadaX1 * coordenadaY2) + (coordenadaX2 * coordenadaY3) + (coordenadaX3 * coordenadaY1)
//            float segundoSumando = (coordenadaX2 * coordenadaY1) + (coordenadaX3 * coordenadaY2) + (coordenadaX1 * coordenadaY3)
//            float resultado = primerSumando - segundoSumando
//            float areaTotal = resultado/2
//            if (areaTotal < 0) {
//                areaTotal = areaTotal * (-1)
//            }
            float areaTotal = obtieneArea(coordenadaX1, coordenadaX2, coordenadaX3, coordenadaY1, coordenadaY2, coordenadaY3)
            float primerResultado = obtieneArea(coordenadaX1, coordenadaX2, 0, coordenadaY1, coordenadaY2, 0)
            float segundoResultado = obtieneArea(coordenadaX1, 0, coordenadaX3, coordenadaY1, 0, coordenadaY3)
            float tercerResultado = obtieneArea(0, coordenadaX2, coordenadaX3, 0, coordenadaY2, coordenadaY3)

            //float primerResultado = (coordenadaX1 * coordenadaY2 + coordenadaX2 * 0 + 0 * coordenadaY1) - (coordenadaX2 * coordenadaY1 + 0 * coordenadaY2 + coordenadaX1 * 0)
            //primerResultado = primerResultado/2
            //float segundoResultado = (coordenadaX1 * 0 + 0 * coordenadaY3 + coordenadaX3 * coordenadaY1) - (0 * coordenadaY1 + coordenadaX3 * 0 + coordenadaX1 * coordenadaY3)
            //segundoResultado = segundoResultado/2
            //float tercerResultado = (0 * coordenadaY2 + coordenadaX2 * coordenadaY3 + coordenadaX3 * 0) - (coordenadaX2 * 0 + coordenadaX3 * coordenadaY2 + 0 * coordenadaY3)
            //tercerResultado = tercerResultado/2
            float resultadoTotal = primerResultado + segundoResultado + tercerResultado
            if (areaTotal == resultadoTotal) {
                contiene = true
            }
            contiene
        } catch (Exception e) {
            contiene
        }
    }

    def obtienePeriodo(boolean esLineal, boolean contieneSol) {
        String periodo = ParametroUtil.periodoIndeterminado
        if (esLineal && contieneSol) {
            periodo = ParametroUtil.periodoSequia
        }
        if (esLineal && !contieneSol) {
            periodo = ParametroUtil.periodoOptimo
        }
        if (!esLineal && contieneSol) {
            periodo = ParametroUtil.periodoLluvia
        }
        periodo
    }
}
