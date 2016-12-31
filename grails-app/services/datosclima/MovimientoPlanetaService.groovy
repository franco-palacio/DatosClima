package datosclima

import grails.transaction.Transactional

@Transactional
class MovimientoPlanetaService {

    def create(pPlaneta, pDia, pGrados, pPosicionX, pPosicionY) {
        MovimientoPlaneta nuevoMovimiento = new MovimientoPlaneta( planeta: pPlaneta, dia: pDia, grados: pGrados, posicionX: pPosicionX, posicionY: pPosicionY )
        nuevoMovimiento.save( failOnError: true )
        nuevoMovimiento
    }

    def delete(pId) {
        def eliminaMovimiento = MovimientoPlaneta.get(pId)
        eliminaMovimiento.delete( failOnError: true )
        eliminaMovimiento
    }

    def update(pId, pDiaActual) {
        def modificaMovimiento = MovimientoPlaneta.get(pId)
        modificaMovimiento.dia = pDiaActual
        modificaMovimiento.save( failOnError: true)
        modificaMovimiento
    }

    def getUltimo(pPlaneta) {
        def id = MovimientoPlaneta.executeQuery("select max(m.id) from MovimientoPlaneta m where m.planeta = :planeta", [planeta: pPlaneta])
        def movimientoPlaneta = MovimientoPlaneta.findById(id)
        movimientoPlaneta
    }
}
