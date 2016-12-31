package datosclima

import grails.transaction.Transactional
import org.apache.jasper.tagplugins.jstl.core.Param

@Transactional
class DatosService {

    def create(pDia, pArea, pPeriodo, pContieneSol, pEsLineal) {
        Datos nuevoDatos = new Datos( dia: pDia, area: pArea, periodo: pPeriodo, contieneSol: pContieneSol, esLineal: pEsLineal )
        nuevoDatos.save( failOnError: true )
        nuevoDatos
    }

    def delete(pId) {
        def eliminaDatos = Datos.get(pId)
        eliminaDatos.delete( failOnError: true )
        eliminaDatos
    }

    def update() {
//        def modificaDatos = Datos.get(pId)
//        modificaDatos.dia = pDiaActual
//        modificaDatos.save( failOnError: true)
        Datos.executeUpdate("update Datos d set d.dia = d.dia - 1 where d.dia > 1 and d.dia <= 3650")
//        modificaDatos
    }

    def getClima(pDia) {
        def response = [ status : 100, message : "OK" ]
        def dato = Datos.findByDia(pDia)

        response.dia = dato.dia
        response.clima = dato.periodo

        response
    }

    def getPeriodos() {
        def response = [ status : 100, message : "OK" ]

        List<Datos> listaDatos = new ArrayList<Datos>()
        listaDatos = Datos.findAll("from Datos d order by d.dia")
        int cantSequia = 0
        int cantLluvia = 0
        int cantOptima = 0
        int cantIndef = 0
        int maxArea = 0
        int diaPico = 0
        //int areaAnt = 0
        //int diaAnt = 0
        String periodoAnt = ""
        for (Datos dato : listaDatos) {
            if (!dato.periodo.equals(periodoAnt)) {
                if (dato.periodo.equals(ParametroUtil.periodoSequia)) {
                    cantSequia = cantSequia + 1
                }
                if (dato.periodo.equals(ParametroUtil.periodoLluvia)) {
                    cantLluvia = cantLluvia + 1
                }
                if (dato.periodo.equals(ParametroUtil.periodoOptimo)) {
                    cantOptima = cantOptima + 1
                }
                if (dato.periodo.equals(ParametroUtil.periodoIndeterminado)) {
                    cantIndef = cantIndef + 1
                }
            }
            periodoAnt = dato.periodo

            if (dato.periodo.equals(ParametroUtil.periodoLluvia)) {
                if (maxArea < dato.area) {
                    diaPico = dato.dia
                    maxArea = dato.area
                }
            }
        }
        response.picoLluvia = diaPico
        response.periodoSequia = cantSequia
        response.periodoLluvia = cantLluvia
        response.periodoOptimo = cantOptima
        response.periodoIndeterminado = cantIndef

        response
    }

    def getListaOrdenada() {
        List<Datos> listaDatos = new ArrayList<Datos>()
        listaDatos = Datos.findAll("from Datos d order by d.dia")
        listaDatos
    }
}
