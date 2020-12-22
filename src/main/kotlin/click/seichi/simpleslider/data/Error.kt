package click.seichi.simpleslider.data

enum class Error(val reason: String) {
    RegionsDuplicatedException("Where you are standing is protected by two or more regions."),
    BlockNotFoundException("Failed to get block at the represented location."),
    SliderNotFoundInSameRegion("The slider doesn't exist in the same region."),
    SliderNotFound("The slider doesn't exist."),
}

sealed class Result<out R> {
    data class Ok<out T>(val data: T) : Result<T>()
    data class Err(val exception: Error) : Result<Nothing>()
}
