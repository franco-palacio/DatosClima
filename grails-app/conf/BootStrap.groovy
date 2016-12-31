import datosclima.CalculoUtil
import datosclima.ParametroUtil

class BootStrap {

    def datosService
    def movimientoPlanetaService
    CalculoUtil calculoUtil

    def init = { servletContext ->
        System.out.println("Arranca Boots")
        completaDatos()
        System.out.println("Fin Boots")
    }
    def destroy = {
    }

    def completaDatos() {
        try {
            calculoUtil = new CalculoUtil()
            int gradosV = 0
            int gradosF = 0
            int gradosB = 0
            int dia = 1
            for (int i = 1; i <= 3650; i++) {
                def nuevoDato
                def nuevoMovimiento

                float area = 0
                boolean esLineal = false
                boolean porOrigen = false
                //CalculoUtil de coordenadas para cada planeta
                float posicionXV = calculoUtil.obtieneCoordenadasX(gradosV, ParametroUtil.radioVulcano)
                float posicionYV = calculoUtil.obtieneCoordenadasY(gradosV, ParametroUtil.radioVulcano)
                float posicionXF = calculoUtil.obtieneCoordenadasX(gradosF, ParametroUtil.radioFerengi)
                float posicionYF = calculoUtil.obtieneCoordenadasY(gradosF, ParametroUtil.radioFerengi)
                float posicionXB = calculoUtil.obtieneCoordenadasX(gradosB, ParametroUtil.radioBetasoide)
                float posicionYB = calculoUtil.obtieneCoordenadasY(gradosB, ParametroUtil.radioBetasoide)
//                System.out.println("posicionXV " + posicionXV)
//                System.out.println("posicionYV " + posicionYV)
//                System.out.println("posicionXF " + posicionXF)
//                System.out.println("posicionYF " + posicionYF)
//                System.out.println("posicionXB " + posicionXB)
//                System.out.println("posicionYB " + posicionYB)
                //CalculoUtil de pendientes para rectas entre dos puntos
                float pendienteVB = calculoUtil.obtienePendiente(posicionXV, posicionXB, posicionYV, posicionYB)
                float pendienteBF = calculoUtil.obtienePendiente(posicionXB, posicionXF, posicionYB, posicionYF)
                float pendienteFV = calculoUtil.obtienePendiente(posicionXF, posicionXV, posicionYF, posicionYV)
//                System.out.println(pendienteVB)
//                System.out.println(pendienteBF)
//                System.out.println(pendienteFV)

                if (pendienteBF.infinite && pendienteFV.infinite && pendienteVB.infinite) {
//                    System.out.println("Pendientes Indefinidas")
                    //Pendientes indefinidas, rectas paralelas al eje Y
                    if (posicionXV == 0 && posicionXF == 0 && posicionXB == 0) {
//                        System.out.println("Forman Recta - Pasan por Origen")
                        //Si las posiciones de X son = 0 es una recta que pasa por Origen
                        esLineal = true
                        porOrigen = true
                    } else {
                        if (posicionXV == posicionXF == posicionXB) {
                            //Si las posiciones son <> 0 pero iguales entre si forman una recta que no pasa por origen
                            esLineal = true
                            porOrigen = false
                        } else {
//                          System.out.println("Forman un triangulo")
                            //Si las posiciones de X son <> 0 y distintas entre si es un triangulo
                            area = calculoUtil.obtieneArea(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                            porOrigen = calculoUtil.contieneSol(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                        }
                    }
                } else {
//                    System.out.println("Pendientes Definidas")
                    if (pendienteVB == pendienteBF && pendienteBF == pendienteFV) {
//                        System.out.println("Pendietes Iguales - Es Recta")
                        esLineal = true
                        //Si las pendientes son = entre si, son una recta
                        float posY = pendienteVB * posicionXB
                        //Determino que pase por Origen
                        if (posicionYB == posY) {
//                            System.out.println("Pasa por Origen")
                            porOrigen = true
                        }
                    } else {
//                        System.out.println("Pendientes Distintas - Es triangulo")
                        //Si las pendientes son <> entre si, son un triangulo
                        area = calculoUtil.obtieneArea(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                        porOrigen = calculoUtil.contieneSol(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                    }
                }

                String tiempo = calculoUtil.obtienePeriodo(esLineal, porOrigen)

                nuevoDato = datosService.create(dia, area, tiempo, porOrigen, esLineal)

                nuevoMovimiento = movimientoPlanetaService.create(ParametroUtil.planetaVulcano, dia, gradosV, posicionXV, posicionYV)
                nuevoMovimiento = movimientoPlanetaService.create(ParametroUtil.planetaFerengi, dia, gradosF, posicionXF, posicionYF)
                nuevoMovimiento = movimientoPlanetaService.create(ParametroUtil.planetaBetasoide, dia, gradosB, posicionXB, posicionYB)

                //Aumento de grados, movimiento de cada planeta
                gradosV = gradosV + 5
                gradosF = gradosF - 1
                gradosB = gradosB - 3
                dia = dia + 1
            }
        } catch (Exception e) {
            System.out.println(e.message)
        }
    }
}
