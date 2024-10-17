package bat.konst.kandinskyclient.app

// config
const val CONFIG_XKEY = "xkey"
const val CONFIG_XSECRET = "xsecret"
const val CONFIG_DEFAULT_VALUE = ""

// kandinsky
const val KANDINSKY_MODEL_ID = "4"
const val KANDINSKY_GENERATE_RESULT_FAIL = "FAIL"
const val KANDINSKY_GENERATE_RESULT_DONE = "DONE"
const val KANDINSKY_GENERATE_RESULT_INITIAL = "INITIAL"

data class AppValues(
    var internalStorageDir : String = ""
)
