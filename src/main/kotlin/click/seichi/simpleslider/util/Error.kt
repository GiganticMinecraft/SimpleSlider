package click.seichi.simpleslider.util

enum class Error(val reason: String) {
    RegionsDuplicatedException("Where you are standing was protected by two or more regions."),
    BlockNotFoundException("Failed to get block at the represented location."),
    SliderNotFoundInSameRegion("The slider didn't exist in the same region."),
    SliderNotFound("The slider didn't exist."),
}

sealed class Result<out R> {
    data class Ok<out T>(val data: T) : Result<T>()
    data class Err(val exception: Error) : Result<Nothing>()
}
