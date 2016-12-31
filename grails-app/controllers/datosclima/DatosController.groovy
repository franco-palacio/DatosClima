package datosclima

import grails.converters.JSON

class DatosController {

    def datosService
    def movimientoPlanetaService
    CalculoUtil calculoUtil

    def index() {
        def response = datosService.getPeriodos()

        render(view: "index", model:[periodo: response])
    }

    def getClima() {
        def response = [ status : 100, message : "OK" ]
        try {
//            def dato = Datos.findByDia(params.dia)
//
//            response.dia = dato.dia
//            response.clima = dato.periodo

            response = datosService.getClima(params.dia)
        } catch (Exception e) {
            response.status = 500
            response.message = e.getMessage()
        }
        render ( response as JSON )
    }

    def getPeriodos() {
        def response = [ status : 100, message : "OK" ]
        try {
            response = datosService.getPeriodos()
        } catch (Exception e) {
            response.status = 500
            response.message = e.getMessage()
        }

        render( response as JSON )
    }

    def list(){
        //completaDatos()

        def response = [ status : 100, message : "" ]

//        def dates = Datos.findAll().collect {
//            it.properties
//        }
//        List<Datos> listaDatos = new ArrayList<Datos>()
//        listaDatos = Datos.findAll()
//        for (Datos dato : listaDatos) {
//            System.out.println(dato.dia)
//        }

        //response.resultado = datosService.delete(1)
        //response.resultadoDelete = Datos.findByDia(1)
        //response.resultadoUpdate = datosService.update(3650, 3649)
        //response.resultadoNuevo = Datos.findById(3650)
        //response.prueba = MovimientoPlaneta.find("from MovimientoPlaneta m where m.planeta = :planeta and m.dia = :dia", [planeta: 'Vulcanos', dia: 365])

//        datosService.delete(1)
//        def datoE = Datos.findByDia(1)
//        int pId = datoE.id
//        response.entidad = datoE
//        def id = MovimientoPlaneta.executeQuery("select max(m.id) from MovimientoPlaneta m where m.planeta = :planeta", [planeta: 'Vulcanos'])
//        response.planeta = id
//        response.planetaCompleto = MovimientoPlaneta.findById(id)
        response.listado = Datos.findAll("from Datos d order by d.dia")
        render ( response as JSON )
    }

    def prueba() {
        def response = [ status : 100, message : "" ]

        def nuevoDato
        def nuevoMovimiento
        calculoUtil = new CalculoUtil()

        def datoE = Datos.findByDia(1)
        int pId = datoE.id
        datosService.delete(pId)

//        List<Datos> listaDatos = new ArrayList<Datos>()
//        listaDatos = Datos.findAll("from Datos d order by d.dia")
//        for (Datos dato : listaDatos) {
//            int id = dato.id
//            int diaNuevo = dato.dia - 1
//            datosService.update(id, diaNuevo)
//        }
        datosService.update(2, 1)

        def idV = MovimientoPlaneta.executeQuery("select max(m.id) from MovimientoPlaneta m where m.planeta = :planeta", [planeta: ParametroUtil.planetaVulcano])
        def movimientoPlanetaVU = MovimientoPlaneta.findById(idV)
        def idF = MovimientoPlaneta.executeQuery("select max(m.id) from MovimientoPlaneta m where m.planeta = :planeta", [planeta: ParametroUtil.planetaFerengi])
        def movimientoPlanetaFU = MovimientoPlaneta.findById(idF)
        def idB = MovimientoPlaneta.executeQuery("select max(m.id) from MovimientoPlaneta m where m.planeta = :planeta", [planeta: ParametroUtil.planetaBetasoide])
        def movimientoPlanetaBU = MovimientoPlaneta.findById(idB)

        int gradosV = movimientoPlanetaVU.grados
        int gradosF = movimientoPlanetaFU.grados
        int gradosB = movimientoPlanetaBU.grados
        int diaNuevo = movimientoPlanetaVU.dia + 1

        int dia = 3650
        float area = 0
        boolean esLineal = false
        boolean porOrigen = false
        //CalculoUtil de coordenadas para cada planeta
        float posicionXV = calculoUtil.obtieneCoordenadasX(gradosV, 1000)
        float posicionYV = calculoUtil.obtieneCoordenadasY(gradosV, 1000)
        float posicionXF = calculoUtil.obtieneCoordenadasX(gradosF, 500)
        float posicionYF = calculoUtil.obtieneCoordenadasY(gradosF, 500)
        float posicionXB = calculoUtil.obtieneCoordenadasX(gradosB, 2000)
        float posicionYB = calculoUtil.obtieneCoordenadasY(gradosB, 2000)
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

        nuevoMovimiento = movimientoPlanetaService.create("Vulcanos", diaNuevo, gradosV, posicionXV, posicionYV)
        nuevoMovimiento = movimientoPlanetaService.create("Ferengis", diaNuevo, gradosF, posicionXF, posicionYF)
        nuevoMovimiento = movimientoPlanetaService.create("Betasoides", diaNuevo, gradosB, posicionXB, posicionYB)

        response.listado = Datos.findAll("from Datos d order by d.dia")
        render ( response as JSON )
    }
}
