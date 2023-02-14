package test_library.alerts


enum class AlertsEnum {
// Енум используемый в функции checkAlert класса BaseTest.
// Представляет собой словарь содержащий возможные варианты части локатора для элемента страницы "alert" различных РМ КИАП.
// Используется по name.
    snackbarWarning,
    snackbarSuccess,
    snackbarError,
    snackbarInfo
}