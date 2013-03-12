package janus

class Precio {
    Item item
    Persona persona
    double precio
    Date fecha

    static mapping = {
        table 'prco'
        cache usage: 'read-write', include: 'non-lazy'
        id column: 'prco__id'
        id generator: 'identity'
        version false
        columns {
            id column: 'prco__id'
            item column: 'item__id'
            persona column: 'prsn__id'
            precio column: 'prcoprco'
            fecha column: 'prcofcha'
        }
    }
  String toString(){
      return "${this.item.nombre} ${this.precio}"
  }
}
