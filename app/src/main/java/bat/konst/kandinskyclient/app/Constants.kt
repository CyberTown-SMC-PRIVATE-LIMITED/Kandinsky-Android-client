package bat.konst.kandinskyclient.app

// config
const val CONFIG_XKEY = "xkey"
const val CONFIG_XSECRET = "xsecret"
const val CONFIG_DEFAULT_VALUE = ""

// kandinsky
const val KANDINSKY_MODEL_ID_UNDEFINED = ""
const val KANDINSKY_GENERATE_RESULT_FAIL = "FAIL"
const val KANDINSKY_GENERATE_RESULT_DONE = "DONE"
const val KANDINSKY_GENERATE_RESULT_INITIAL = "INITIAL"
const val KANDINSKY_QUEUE_MAX = 5
const val KANDINSKY_REQUEST_UNTERVAL_SEC: Long = 10
const val KANDINSKY_STYLE_DEFAULT = "DEFAULT"

// file storage
const val FILE_IMAGE_THUMB_HEIGHT = 250
const val FILE_IMAGE_THUMB_WIDTH = 250

lateinit var FILE_STORAGE_PATH: String // путь к папке с изображениями

// request rules
const val REQUEST_MIN_LENGTH = 4
