package datosclima



class DatosJob {
    def datosService
    def movimientoPlanetaService
    CalculoUtil calculoUtil

    static triggers = {
        //simple repeatInterval: 5000l // execute job once in 5 seconds
//        cron cronExpression: "0 0 0/1 1/1 * ? *"//1 hora
        cron cronExpression: "0 0/15 * 1/1 * ? *"//15 min
//        cron cronExpression: "0 0/10 * 1/1 * ? *"//10 min
//        cron cronExpression: "0 0/5 * 1/1 * ? *"//5 min
//        cron cronExpression: "0 0/2 * 1/1 * ? *"//2 min
//        cron cronExpression: "0 0/1 * 1/1 * ? *"//1 min
        //cron cronExpression: "0 0 0 1/1 * ? *"//00:00 hs
    }

    def execute() {
        // execute job
        System.out.println("Entra en Job")
        try {
            def nuevoDato
            def nuevoMovimiento
            calculoUtil = new CalculoUtil()

            def datoE = Datos.findByDia(1)
            int pId = datoE.id
            datosService.delete(pId)

//            List<Datos> listaDatos = new ArrayList<Datos>()
//            listaDatos = datosService.getListaOrdenada()
//            for (Datos dato : listaDatos) {
//                int id = dato.id
//                int diaNuevo = dato.dia - 1
//                datosService.update(id, diaNuevo)
//            }
            datosService.update()

            def movimientoPlanetaVU = movimientoPlanetaService.getUltimo(ParametroUtil.planetaVulcano)
            def movimientoPlanetaFU = movimientoPlanetaService.getUltimo(ParametroUtil.planetaFerengi)
            def movimientoPlanetaBU = movimientoPlanetaService.getUltimo(ParametroUtil.planetaBetasoide)

            int gradosV = movimientoPlanetaVU.grados
            int gradosF = movimientoPlanetaFU.grados
            int gradosB = movimientoPlanetaBU.grados
            int diaNuevo = movimientoPlanetaVU.dia + 1

            int dia = 3650
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
            //CalculoUtil de pendientes para rectas entre dos puntos
            float pendienteVB = calculoUtil.obtienePendiente(posicionXV, posicionXB, posicionYV, posicionYB)
            float pendienteBF = calculoUtil.obtienePendiente(posicionXB, posicionXF, posicionYB, posicionYF)
            float pendienteFV = calculoUtil.obtienePendiente(posicionXF, posicionXV, posicionYF, posicionYV)

            if (pendienteBF.infinite && pendienteFV.infinite && pendienteVB.infinite) {
                //Pendientes indefinidas, rectas paralelas al eje Y
                if (posicionXV == 0 && posicionXF == 0 && posicionXB == 0) {
                    //Si las posiciones de X son = 0 es una recta que pasa por Origen
                    esLineal = true
                    porOrigen = true
                } else {
                    //Si las posiciones de X son <> 0 es un triangulo
                    area = calculoUtil.obtieneArea(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                    porOrigen = calculoUtil.contieneSol(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                }
            } else {
                if (pendienteVB == pendienteBF && pendienteBF == pendienteFV) {
                    esLineal = true
                    //Si las pendientes son = entre si, son una recta
                    float posY = pendienteBF * posicionXB
                    //Determino que pase por Origen
                    if (posicionYB == posY) {
                        porOrigen = true
                    }
                } else {
                    //Si las pendientes son <> entre si, son un triangulo
                    area = calculoUtil.obtieneArea(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                    porOrigen = calculoUtil.contieneSol(posicionXV, posicionXF, posicionXB, posicionYV, posicionYF, posicionYB)
                }
            }

            String tiempo = calculoUtil.obtienePeriodo(esLineal, porOrigen)

            nuevoDato = datosService.create(dia, area, tiempo, porOrigen, esLineal)

            nuevoMovimiento = movimientoPlanetaService.create(ParametroUtil.planetaVulcano, diaNuevo, gradosV, posicionXV, posicionYV)
            nuevoMovimiento = movimientoPlanetaService.create(ParametroUtil.planetaFerengi, diaNuevo, gradosF, posicionXF, posicionYF)
            nuevoMovimiento = movimientoPlanetaService.create(ParametroUtil.planetaBetasoide, diaNuevo, gradosB, posicionXB, posicionYB)

        } catch (Exception e) {
            System.out.println(e.getMessage())
        }
        System.out.println("Termina Job")
    }
}
