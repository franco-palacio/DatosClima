package datosclima

class Datos {

    int dia
    float area
    String periodo
    boolean esLineal
    boolean contieneSol

    static constraints = {
        dia(nullable: false)
    }
}
