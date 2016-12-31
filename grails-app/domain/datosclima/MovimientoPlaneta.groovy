package datosclima

class MovimientoPlaneta {

    String planeta
    int dia
    int grados
    float posicionX
    float posicionY

    static constraints = {
        planeta(nullable: false)
        dia(nullable: false)
        grados(nullable: false)
        posicionX(nullable: false)
        posicionY(nullable: false)
    }
}
