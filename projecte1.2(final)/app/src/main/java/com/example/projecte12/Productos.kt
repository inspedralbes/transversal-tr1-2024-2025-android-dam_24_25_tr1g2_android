import android.os.Parcel
import android.os.Parcelable

data class Producto(
    val id: Int,
    val producto: String,
    val precio: String,
    val imagen: String,
    var cantidad: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(producto)
        parcel.writeString(precio)
        parcel.writeString(imagen)
        parcel.writeInt(cantidad)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto = Producto(parcel)
        override fun newArray(size: Int): Array<Producto?> = arrayOfNulls(size)
    }
}
